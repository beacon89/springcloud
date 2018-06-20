package com.kayak.base.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.kayak.base.spring.KPropertyPlaceholderConfigurer;
import com.kayak.base.sql.object.SqlCheck;
import com.kayak.base.sql.object.SqlConfig;
import com.kayak.base.sql.object.SqlConfigFile;
import com.kayak.base.sql.object.SqlExecute;
import com.kayak.base.sql.object.SqlQueryTree;
import com.kayak.base.sql.object.SqlTransfer;
import com.kayak.base.system.IReloadConfig;
import com.kayak.base.system.SysBeans;
import com.kayak.base.util.FileUtil;
import com.kayak.base.util.Tools;
import com.kayak.base.util.XmlUtil;

public class SysSql implements IReloadConfig {

	private static Logger log = Logger.getLogger(SysSql.class);

	public static final String DIALECT_ORA = "oracle";
	public static final String DIALECT_DB2 = "db2";

	@Autowired
	private KPropertyPlaceholderConfigurer propertyPlaceholderConfigurer;

	/**
	 * 要读取的SQL配置文件集合
	 */
	private List<String> configFiles = null;

	/**
	 * 定义SQL配置对象保存到缓存中的KEY前缀
	 */
	private static final String SQLINFO_KEY_PREFIX = "SQLCONFIG.";

	/**
	 * 定义SQL执行配置对象保存到缓存中的KEY前缀
	 */
	private final String EXECUTE_KEY_PREFIX = "SQLEXECUTE.";

	/**
	 * 已读取的配置文件与sqlid对应关系<br />
	 * 以每个页面配置文件的名称作为key，保存此配配置文件中有哪些sqlid定义在这里
	 */
	private static List<SqlConfigFile> sqlinfoFiles;
	/**
	 * 已读取的配置文件与exeid对应关系<br />
	 * 以每个面板配置文件的名称作为key，保存此配配置文件中有哪些exeid定义在这里
	 */
	private static List<SqlConfigFile> executeFiles;

	/**
	 * 用于在加载配置时保存已加载的sqlid值
	 */
	private static List<String> loadedSqlids;

	/**
	 * 用于在加载配置时保存已加载的exeid值
	 */
	private static List<String> loadedExeids;

	private boolean init;

	private Map<String, SqlExecute> exeidCache = new ConcurrentHashMap<String, SqlExecute>();
	private Map<String, SqlConfig> sqlidCache = new ConcurrentHashMap<String, SqlConfig>();

	public void init() {
		// 清缓存
		this.exeidCache.clear();
		this.sqlidCache.clear();

		if (loadedSqlids == null)
			loadedSqlids = new ArrayList<String>();
		else
			loadedSqlids.clear();

		if (sqlinfoFiles == null)
			sqlinfoFiles = new ArrayList<SqlConfigFile>();
		else
			sqlinfoFiles.clear();

		if (loadedExeids == null)
			loadedExeids = new ArrayList<String>();
		else
			loadedExeids.clear();

		if (executeFiles == null)
			executeFiles = new ArrayList<SqlConfigFile>();
		else
			executeFiles.clear();

		reload();

		init = true;
	}

	public synchronized void reload() {
		// 读取配置文件内容
		if (this.configFiles == null || this.configFiles.isEmpty()) {
			log.error("没有指定SQL配置文件（sysSql bean 的 configFiles 属性）");
			return;
		}

		// 先加载所有SQL-INFO配置
		loadSqlInfo();

		// 加载完SQL-INFO配置后再加载SQL-EXECUTE配置
		loadSqlExecute();
	}

	public void destroy() {
		this.exeidCache.clear();
		this.sqlidCache.clear();
	}

	private void loadSqlInfo() {
		Resource[] resources = FileUtil.getResources("classpath*:conf/sql/**/sql-**-info.xml");

		if (resources != null && resources.length > 0) {
			for (Resource resource : resources) {
				try {
					loadConfig(resource, true);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	private void loadSqlExecute() {
		Resource[] resources = FileUtil.getResources("classpath*:conf/sql/**/sql-*-execute.xml");

		if (resources != null && resources.length > 0) {
			for (Resource resource : resources) {
				try {
					loadConfig(resource, false);
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 加载SQL配置文件
	 * 
	 * @param file
	 * @param loadSqlinfo
	 *            true则加载SQLINFO配置，false则加载SQLEXECUTE配置
	 * @return
	 * @throws IOException
	 */
	private void loadConfig(Resource resource, boolean loadSqlinfo) throws IOException {
		if (loadSqlinfo)
			loadSqlInfoFile(resource);
		else
			loadSqlExecuteFile(resource);
	}

	public String getDBType(String datasource) {
		String propname = String.format("%s.db.type", datasource);
		String dbtype = propertyPlaceholderConfigurer.getPropValue(propname);
		if (dbtype == null || "".equals(dbtype)) {// 取不到dataSource指定的dbtype，则返回总的dbtype
			return propertyPlaceholderConfigurer.getPropValue("default.db.type");
		}
		return dbtype;
	}

	/**
	 * 添加 SQL-INFO 配置缓存
	 * 
	 * @param config
	 */
	public void putSqlConfig(SqlConfig config) {
		if (config == null)
			return;

		String sqlid = config.getSqlid();

		if (Tools.strIsEmpty(sqlid)) {
			log.error("添加 SqlConfig 对象到缓存出错：sqlid不能为空");
			return;
		}
		if (loadedSqlids.contains(genSqlid(sqlid, ""))) {
			log.error("sqlid=" + genSqlid(sqlid, "") + " 的SQL-INFO配置重复，只有最后被加载的配置有效");
		}
		putSqlConfig(genSqlid(sqlid, ""), config);
		loadedSqlids.add(genSqlid(sqlid, ""));
	}

	/**
	 * 添加SqlConfig对象到缓存
	 * 
	 * @param sqlid
	 * @param config
	 */
	private void putSqlConfig(String sqlid, SqlConfig config) {
		this.sqlidCache.put(sqlInfoCacheKey(sqlid), config);
	}

	public static String sqlInfoCacheKey(String sqlid) {
		return SQLINFO_KEY_PREFIX + sqlid;
	}

	/**
	 * 添加 SQL-EXECUTE 配置缓存
	 * 
	 * @param config
	 */
	public void putSqlExecute(SqlExecute config) {
		if (config == null)
			return;

		String exeid = config.getExeid();
		if (Tools.strIsEmpty(exeid)) {
			log.error("添加 SqlExecute 对象到缓存出错：exeid不能为空");
			return;
		}
		if (loadedExeids.contains(exeid)) {
			log.error("exeid=" + exeid + " 的SQL-EXECUTE配置已重复，只有最后被加载的配置有效");
		}
		putSqlExecute(exeid, config);
		loadedExeids.add(exeid);
	}

	/**
	 * 添加SqlExecute对象到缓存
	 * 
	 * @param exeid
	 * @param config
	 */
	private void putSqlExecute(String exeid, SqlExecute config) {
		this.exeidCache.put(this.EXECUTE_KEY_PREFIX + exeid, config);
	}

	/**
	 * 获取缓存的SqlConfig对象
	 * 
	 * @param sqlid
	 *            SQLINFO中的id
	 * @return
	 */
	private SqlConfig getSqlConfig(String sqlid, String dialect) {
		String key = sqlInfoCacheKey(genSqlid(sqlid, dialect));
		SqlConfig obj = this.sqlidCache.get(key);
		return obj;
	}

	/**
	 * 移除 sqlid 在缓存中的SqlConfig对象
	 * 
	 * @param sqlid
	 */
	// public void removeSqlConfig(String sqlid,String dbtype)
	// {
	// oscache.flushEntry(sqlInfoCacheKey(sqlid, dbtype));
	// loadedSqlids.remove(sqlid);
	// }

	/**
	 * 移除 exeid 在缓存中的SqlExecute对象
	 * 
	 * @param exeid
	 */
	public void removeSqlExecute(String exeid) {
		exeidCache.remove(EXECUTE_KEY_PREFIX + exeid);
		loadedExeids.remove(exeid);
	}

	/**
	 * 获取缓存的SqlExecute对象
	 * 
	 * @param sqlid
	 * @return
	 */
	public SqlExecute getSqlExecute(String exeid) {
		String key = this.EXECUTE_KEY_PREFIX + exeid;
		SqlExecute obj = this.exeidCache.get(key);
		return obj;
	}

	/**
	 * 删除原来从filename这个文件中读取的SQL配置（包括内存和缓存）
	 */
	@SuppressWarnings("unlikely-arg-type")
	private void removeSqlinfoConfig(SqlConfigFile file) {
		List<String> ids = file.getIds();
		if (ids == null)
			return;
		for (String id : ids) {
			sqlidCache.remove(sqlInfoCacheKey(id));
			loadedSqlids.remove(id);
		}
		sqlinfoFiles.remove(file.getFilename());
		ids = null;
	}

	/**
	 * 删除原来从filename这个文件中读取的SQL执行配置（包括内存和缓存）
	 */
	@SuppressWarnings("unlikely-arg-type")
	private void removeExecuteConfig(SqlConfigFile file) {
		List<String> ids = file.getIds();
		if (ids == null)
			return;
		for (String id : ids) {
			exeidCache.remove(EXECUTE_KEY_PREFIX + id);
			loadedExeids.remove(id);
		}
		executeFiles.remove(file.getFilename());
		ids = null;
	}

	/**
	 * 删除原来从filename这个文件中读取的配置（包括内存和缓存）
	 */
	public void removeLoadedConfig(Resource resource) {
		SqlConfigFile file = SqlConfigFile.find(sqlinfoFiles, resource);
		if (file != null) {
			removeSqlinfoConfig(file);
		} else {
			file = SqlConfigFile.find(executeFiles, resource);
			if (file != null) {
				removeExecuteConfig(file);
			}
		}
	}

	/**
	 * 添加sqlid与filename配置文件的关联
	 */
	private static void add2Sqlinfofiles(SqlConfigFile file, String sqlid) {
		List<String> ids = file.getIds();
		if (ids == null) {
			ids = new ArrayList<String>();
			file.setIds(ids);
		}
		if (!ids.contains(sqlid)) {
			ids.add(sqlid);
		}
	}

	/**
	 * 添加exeid与filename配置文件的关联
	 */
	private static void add2Executefiles(SqlConfigFile file, String exeid) {
		List<String> ids = file.getIds();
		if (ids == null) {
			ids = new ArrayList<String>();
			file.setIds(ids);
		}
		if (!ids.contains(exeid)) {
			ids.add(exeid);
		}
	}

	/**
	 * 加载sql-info配置文件，根据每个sqlinfo配置生成SqlConfig
	 * 
	 * @throws IOException
	 */
	public String loadSqlInfoFile(Resource resource) throws IOException {
		String filename = resource.getFilename();
		String filepath = resource.getURI().getPath();

		SqlConfigFile confFile = SqlConfigFile.find(sqlinfoFiles, resource);
		if (confFile == null) {
			confFile = new SqlConfigFile(filename, resource.lastModified());
			sqlinfoFiles.add(confFile);
		} else {
			long lastModified = resource.lastModified();
			if (lastModified == confFile.getLastModified())
				// 文件没有更新，不需要重新加载
				return "";
		}

		String res;

		Element root = null;// 引用读出的配置文件的根节点
		try {
			// 读取XML配置文件
			SAXReader saxReader = new SAXReader();// 创建读取xml文件的对象
			Document doc = saxReader.read(resource.getInputStream());// 读取xml文件
			root = doc.getRootElement();// 取根节点
		} catch (DocumentException e) {
			res = "打开配置文件失败：" + resource.getFilename();
			log.error(res, e);
			return res;
		}

		confFile.setLastModified(resource.lastModified());

		// 先删除原来从这个文件中读取的配置（包括内存和缓存）
		removeLoadedConfig(resource);

		if (root == null) {// 没有根节点，直接退出
			res = "配置文件没有配置信息：" + resource.getFilename();
			log.info(res);
			return res;
		}

		// 取配置节点集合
		@SuppressWarnings("rawtypes")
		Iterator configs = root.elementIterator();
		if (configs == null) {// 没有配置节点，直接退出
			res = "配置文件没有配置信息：" + resource.getFilename();
			log.info(res);
			return res;
		}

		int n = 0;// sqlinfo节点数累计变量
		while (configs.hasNext()) {
			n++;// sqlinfo节点数累计，用以获取当前加载第几个节点
			// 取得sqlinfo节点对象
			Element sqlinfoEl = (Element) configs.next();
			Element el;

			// 定义构造SqlConfig对象所需变量
			String sqlid = null, sqlname = null;
			List<SqlCheck> checks = null;
			List<SqlTransfer> transfers = null;
			SqlQueryTree querytree = null;

			// 从配置节点中获取信息
			sqlid = XmlUtil.getAttributeString(sqlinfoEl, "sqlid");
			// 获取数据库方言标识
			// dialect = XmlUtil.getAttributeString(sqlinfoEl, "dialect");
			// if(StringUtils.isEmpty(dialect)){
			// dialect = DIALECT_ORA;
			// // dialect = "";
			// }

			if (Tools.strIsEmpty(sqlid)) {
				log.error("加载SQL配置文件" + filename + "时出错：第" + n + "个sqlinfo节点的sqlid属性为空或未指定");
				continue;
			}
			if (loadedSqlids.contains(genSqlid(sqlid, ""))) {
				log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的SQL配置重复，第" + n + "个sqlinfo节点的配置将不会被加载");
				continue;
			}

			sqlname = XmlUtil.getElementTextTrim(sqlinfoEl.element("sqlname"));
			if (Tools.strIsEmpty(sqlname)) {
				log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > sqlname节点未指定或值为空");
				continue;
			}

			// sql = XmlUtil.getElementTextTrim(sqlinfoEl.element("sql"));

			// 读取SQL执行前校验配置信息
			el = sqlinfoEl.element("checks");
			if (el != null) {// 存在checks执行前校验配置
				@SuppressWarnings("rawtypes")
				Iterator checkIt = el.elementIterator("check");
				if (checkIt != null) {
					checks = new ArrayList<SqlCheck>();// 用于保存读取到的配置信息
					int nc = 0;// 用于计数，表示读取到第几个check节点配置
					while (checkIt.hasNext()) {
						nc++;
						Element checkEl = (Element) checkIt.next();
						String checkname = null, checksql = null, checksqlid = null, checkstring = null,
								compareval = null, comparesign = null, errtext = null;
						Boolean exitall = null;

						checkname = XmlUtil.getElementTextTrim(checkEl.element("checkname"));
						checksql = XmlUtil.getElementTextTrim(checkEl.element("checksql"));
						List<Element> sqlEls = checkEl.elements("checksql");

						// List<Element> elCheckSqls =
						// checkEl.elements("checksql");
						// if(elCheckSqls != null){
						// for(Element elCheckSql:elCheckSqls){
						//
						// }
						// }
						checksqlid = XmlUtil.getElementTextTrim(checkEl.element("checksqlid"));
						checkstring = XmlUtil.getElementTextTrim(checkEl.element("checkstring"));
						if (Tools.strIsEmpty(checksql) && Tools.strIsEmpty(checksqlid)
								&& Tools.strIsEmpty(checkstring)) {
							log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > checks的第" + nc
									+ "个check节点中必须指定checksql/checksqlid/checkstring其中一个作为子节点");
							continue;
						}
						compareval = XmlUtil.getElementTextTrim(checkEl.element("compareval"));
						if (Tools.strIsEmpty(compareval)) {
							log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > checks的第" + nc
									+ "个check > compareval节点未指定或值为空");
							continue;
						}
						comparesign = XmlUtil.getAttributeString(checkEl, "comparesign", "eql");
						if (Tools.strIsEmpty(comparesign)) {
							log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > checks的第" + nc
									+ "个check节点的comparesign属性为空或未指");
							continue;
						}
						errtext = XmlUtil.getElementTextTrim(checkEl.element("errtext"));
						if (Tools.strIsEmpty(errtext)) {
							log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > checks的第" + nc
									+ "个check > errtext节点未指定或值为空");
							continue;
						}
						exitall = XmlUtil.getAttributeBoolean(checkEl, "exitall", true);
						SqlCheck chk = new SqlCheck(checkname, checksql, checksqlid, checkstring, compareval,
								comparesign, errtext, exitall);
						if (sqlEls != null) {
							for (Element e : sqlEls) {
								String dialect = XmlUtil.getAttributeString(e, "dialect");
								chk.addCheckSql(XmlUtil.getElementTextTrim(e), dialect);
							}
						}
						checks.add(chk);
					}
				}
			}

			// 读取SQL查询结果字段字典转换配置信息
			el = sqlinfoEl.element("transfers");
			if (el != null) {// 存在transfers查询结果字典转换配置
				@SuppressWarnings("rawtypes")
				Iterator transferIt = el.elementIterator("transfer");
				if (transferIt != null) {
					transfers = new ArrayList<SqlTransfer>();// 用于保存读取到的配置信息
					int nc = 0;// 用于计数，表示读取到第几个transfer节点配置
					while (transferIt.hasNext()) {
						nc++;
						Element transferEl = (Element) transferIt.next();
						String column = null, target = null, dict = null;

						column = XmlUtil.getAttributeString(transferEl, "column");
						if (Tools.strIsEmpty(column)) {
							log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > transfers的第" + nc
									+ "个transfer节点的column属性为空或未指");
							continue;
						}
						target = XmlUtil.getAttributeString(transferEl, "target");
						if (Tools.strIsEmpty(target)) {
							log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > transfers的第" + nc
									+ "个transfer节点的target属性为空或未指");
							continue;
						}
						dict = XmlUtil.getAttributeString(transferEl, "dict");
						if (Tools.strIsEmpty(dict)) {
							log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > transfers的第" + nc
									+ "个transfer节点的dict属性为空或未指");
							continue;
						}
						transfers.add(new SqlTransfer(column, target, dict));
					}
				}
			}

			el = sqlinfoEl.element("querytree");
			if (el != null) {// 存在查询结果树结构定义配置
				String idcolumn = null, textcolumn = null, iconClscolumn = null, iconcolumn = null, diffway = null,
						diffcondition = null, initexpand = null;
				Boolean isasync = null;
				idcolumn = XmlUtil.getAttributeString(el, "idcolumn");
				if (Tools.strIsEmpty("idcolumn")) {
					log.error(
							"加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > querytree节点的idcolumn属性为空或未指定");
					continue;
				}
				textcolumn = XmlUtil.getAttributeString(el, "textcolumn");
				if (Tools.strIsEmpty("textcolumn")) {
					log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid
							+ "的sqlinfo > querytree节点的textcolumn属性为空或未指定");
					continue;
				}
				diffway = XmlUtil.getAttributeString(el, "diffway", "upper");
				if (Tools.strIsEmpty("diffway")) {
					log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > querytree节点的diffway属性为空或未指定");
					continue;
				}
				diffcondition = XmlUtil.getAttributeString(el, "diffcondition");
				if (Tools.strIsEmpty("diffcondition")) {
					log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid
							+ "的sqlinfo > querytree节点的diffcondition属性为空或未指定");
					continue;
				}
				iconClscolumn = XmlUtil.getAttributeString(el, "iconClscolumn");
				iconcolumn = XmlUtil.getAttributeString(el, "iconcolumn");
				initexpand = XmlUtil.getAttributeString(el, "initexpand", "none");
				isasync = XmlUtil.getAttributeBoolean(el, "isasync", false);
				querytree = new SqlQueryTree(idcolumn, textcolumn, iconClscolumn, iconcolumn, diffway, diffcondition,
						initexpand, isasync);
			}

			if (checks != null && checks.isEmpty())
				checks = null;
			if (transfers != null && transfers.isEmpty())
				transfers = null;

			SqlConfig conf = new SqlConfig(sqlid, sqlname, checks, transfers, querytree);
			// sql结点可出现多个，实现不同数据库的sql语句
			List<Element> sqlList = sqlinfoEl.elements("sql");

			if (sqlList == null || sqlList.size() < 1) {
				log.error("加载SQL配置文件" + filename + "时出错：sqlid=" + sqlid + "的sqlinfo > sql节点未指定或值为空");
				continue;
			}
			if (sqlList != null) {
				for (Element elSql : sqlList) {
					String sql = XmlUtil.getElementTextTrim(elSql);
					String dialect = XmlUtil.getAttributeString(elSql, "dialect");
					conf.addSql(sql, dialect);
				}
			}

			// 添加到缓存
			this.putSqlConfig(conf);
			// 收集已加载的sqlid
			if (!loadedSqlids.contains(genSqlid(sqlid, "")))
				loadedSqlids.add(genSqlid(sqlid, ""));

			// 与文件名称关联
			add2Sqlinfofiles(confFile, genSqlid(sqlid, ""));
		}
		res = "加载配置文件完成：" + filepath;
		log.info(res);
		return res;
	}

	/**
	 * 生成真正的SQLINFO ID，由id+dbtyp组成
	 * 
	 * @param sqlid
	 * @param dialect
	 * @return
	 */
	public static String genSqlid(String sqlid, String dialect) {
		// return sqlid+"."+dialect;
		return sqlid;
	}

	/**
	 * 加载sql-execute配置文件，根据配置生成exeid与多SqlConfig对象集合的Map
	 * 
	 * @throws IOException
	 */
	public String loadSqlExecuteFile(Resource resource) throws IOException {

		String filename = resource.getFilename();
		String filepath = resource.getURI().getPath();

		SqlConfigFile confFile = SqlConfigFile.find(executeFiles, resource);
		if (confFile == null) {
			confFile = new SqlConfigFile(filename, resource.lastModified());
			executeFiles.add(confFile);
		} else {
			long lastModified = resource.lastModified();
			if (lastModified == confFile.getLastModified())
				// 文件没有更新，不需要重新加载
				return "";
		}

		String res;
		Element root = null;// 引用读出的配置文件的根节点
		try {
			// 读取XML配置文件
			SAXReader saxReader = new SAXReader();// 创建读取xml文件的对象
			Document doc = saxReader.read(resource.getInputStream());// 读取xml文件
			root = doc.getRootElement();// 取根节点
		} catch (DocumentException e) {
			res = "打开配置文件失败：" + filepath;
			log.error(res, e);
			return res;
		}

		confFile.setLastModified(resource.lastModified());

		// 先删除原来从这个文件中读取的配置（包括内存和缓存）
		removeLoadedConfig(resource);

		if (root == null) {// 没有根节点，直接退出
			res = "配置文件没有配置信息：" + filepath;
			log.info(res);
			return res;
		}

		// 取配置节点集合
		@SuppressWarnings("rawtypes")
		Iterator configs = root.elementIterator();
		if (configs == null) {// 没有配置节点，直接退出
			res = "配置文件没有配置信息：" + filepath;
			log.info(res);
			return res;
		}

		int n = 0;// sqlexecute节点数累计变量
		while (configs.hasNext()) {
			n++;// sqlexecute节点数累计，用以获取当前加载第几个节点
			// 取得sqlexecute节点对象
			Element sqlexecuteEl = (Element) configs.next();

			String exeid = null, exename = null, sqlids = null, logexeid = null, datasource = null;
			boolean pstm = false;
			exeid = XmlUtil.getAttributeString(sqlexecuteEl, "exeid");
			if (Tools.strIsEmpty(exeid)) {
				log.error("加载SQL配置文件" + filename + "时出错：第" + n + "个sqlexecute节点的exeid属性为空或未指定");
				continue;
			}
			if (loadedExeids.contains(exeid)) {
				log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的SQL配置重复，第" + n + "个sqlexecute节点的配置将不会被加载");
				continue;
			}

			logexeid = XmlUtil.getAttributeString(sqlexecuteEl, "logexeid");
			datasource = XmlUtil.getAttributeString(sqlexecuteEl, "datasource");
			pstm = XmlUtil.getAttributeBoolean(sqlexecuteEl, "pstm", false);
			String dbtype = getDBType(datasource);
			if (Tools.strIsEmpty(datasource)) {
				log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute节点的datasource属性为空或未指定");
				continue;
			}

			exename = XmlUtil.getElementTextTrim(sqlexecuteEl.element("exename"));
			if (Tools.strIsEmpty(exename)) {
				log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > exename节点未指定或值为空");
				continue;
			}

			// /////////////////////////////////////////////////
			// 加载sqlids或内嵌sqlinfo
			// ////////////////////////////////////
			sqlids = XmlUtil.getElementTextTrim(sqlexecuteEl.element("sqlids"));
			List<String> sqlidList = new ArrayList<String>();
			if (!Tools.strIsEmpty(sqlids)) {
				// 将sqlids多sqlid字符串构造成String集合
				String[] sqlidsarr = sqlids.split("[,]");
				for (String sqlid : sqlidsarr) {
					sqlid = sqlid.trim();
					if (!loadedSqlids.contains(genSqlid(sqlid, dbtype))) {// 判断有没有该sqlid
						log.info("加载SQL配置文件" + filename + "中：exeid=" + exeid + "的sqlexecute > sqlids节点中指定的 "
								+ genSqlid(sqlid, dbtype) + " SQL配置不存在");
						continue;
					}
					if (!sqlidList.contains(sqlid))
						sqlidList.add(sqlid);
				}
				if (sqlidList.size() == 0) {
					log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute指定了sqlids节点不能为空");
					continue;
				}
			}

			Element sqlinfos = sqlexecuteEl.element("sqlinfos");
			List<SqlConfig> sqlinfoList = new ArrayList<SqlConfig>();
			if (sqlinfos != null) {
				@SuppressWarnings("rawtypes")
				Iterator configs_sqlinfo = sqlinfos.elementIterator();
				int m = 0;// sqlinfo节点数累计变量
				while (configs_sqlinfo.hasNext()) {
					m++;// sqlinfo节点数累计，用以获取当前加载第几个节点
					// 取得sqlinfo节点对象
					Element sqlinfoEl = (Element) configs_sqlinfo.next();
					Element el;

					// 定义构造SqlConfig对象所需变量
					String sqlname = null;
					List<SqlCheck> checks = null;
					List<SqlTransfer> transfers = null;
					SqlQueryTree querytree = null;

					sqlname = XmlUtil.getElementTextTrim(sqlinfoEl.element("sqlname"));

					// 读取SQL执行前校验配置信息
					el = sqlinfoEl.element("checks");
					if (el != null) {// 存在checks执行前校验配置
						@SuppressWarnings("rawtypes")
						Iterator checkIt = el.elementIterator("check");
						if (checkIt != null) {
							checks = new ArrayList<SqlCheck>();// 用于保存读取到的配置信息
							int nc = 0;// 用于计数，表示读取到第几个check节点配置
							while (checkIt.hasNext()) {
								nc++;
								Element checkEl = (Element) checkIt.next();
								String checkname = null, checksql = null, checkstring = null, compareval = null,
										comparesign = null, errtext = null;
								Boolean exitall = null;

								checkname = XmlUtil.getElementTextTrim(checkEl.element("checkname"));
								checksql = XmlUtil.getElementTextTrim(checkEl.element("checksql"));
								List<Element> sqlEls = checkEl.elements("checksql");

								checkstring = XmlUtil.getElementTextTrim(checkEl.element("checkstring"));
								if (Tools.strIsEmpty(checksql) && Tools.strIsEmpty(checkstring)) {
									log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
											+ "个sqlinfo > checks的第" + nc
											+ "个check节点中必须指定checksql/checkstring其中一个作为子节点");
									continue;
								}
								compareval = XmlUtil.getElementTextTrim(checkEl.element("compareval"));
								if (Tools.strIsEmpty(compareval)) {
									log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
											+ "个sqlinfo > checks的第" + nc + "个check > compareval节点未指定或值为空");
									continue;
								}
								comparesign = XmlUtil.getAttributeString(checkEl, "comparesign", "eql");
								if (Tools.strIsEmpty(comparesign)) {
									log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
											+ "个sqlinfo > checks的第" + nc + "个check节点的comparesign属性为空或未指");
									continue;
								}
								errtext = XmlUtil.getElementTextTrim(checkEl.element("errtext"));
								if (Tools.strIsEmpty(compareval)) {
									log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
											+ "个sqlinfo > checks的第" + nc + "个check > errtext节点未指定或值为空");
									continue;
								}
								exitall = XmlUtil.getAttributeBoolean(checkEl, "exitall", true);
								SqlCheck chk = new SqlCheck(checkname, checksql, checkstring, compareval, comparesign,
										errtext, exitall);
								if (sqlEls != null) {
									for (Element e : sqlEls) {
										String dialect = XmlUtil.getAttributeString(e, "dialect");
										chk.addCheckSql(XmlUtil.getElementTextTrim(e), dialect);
									}
								}
								checks.add(chk);
							}
						}
					}

					// 读取SQL查询结果字段字典转换配置信息
					el = sqlinfoEl.element("transfers");
					if (el != null) {// 存在transfers查询结果字典转换配置
						@SuppressWarnings("rawtypes")
						Iterator transferIt = el.elementIterator("transfer");
						if (transferIt != null) {
							transfers = new ArrayList<SqlTransfer>();// 用于保存读取到的配置信息
							int nc = 0;// 用于计数，表示读取到第几个transfer节点配置
							while (transferIt.hasNext()) {
								nc++;
								Element transferEl = (Element) transferIt.next();
								String column = null, target = null, dict = null;

								column = XmlUtil.getAttributeString(transferEl, "column");
								if (Tools.strIsEmpty(column)) {
									log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
											+ "个sqlinfo > transfers的第" + nc + "个transfer节点的column属性为空或未指");
									continue;
								}
								target = XmlUtil.getAttributeString(transferEl, "target");
								if (Tools.strIsEmpty(target)) {
									log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
											+ "个sqlinfo > transfers的第" + nc + "个transfer节点的target属性为空或未指");
									continue;
								}
								dict = XmlUtil.getAttributeString(transferEl, "dict");
								if (Tools.strIsEmpty(dict)) {
									log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
											+ "个sqlinfo > transfers的第" + nc + "个transfer节点的dict属性为空或未指");
									continue;
								}
								transfers.add(new SqlTransfer(column, target, dict));
							}
						}
					}

					el = sqlinfoEl.element("querytree");
					if (el != null) {// 存在查询结果树结构定义配置
						String idcolumn = null, textcolumn = null, iconClscolumn = null, iconcolumn = null,
								diffway = null, diffcondition = null, initexpand = null;
						Boolean isasync = null;
						idcolumn = XmlUtil.getAttributeString(el, "idcolumn");
						if (Tools.strIsEmpty("idcolumn")) {
							log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
									+ "个sqlinfo > querytree节点的idcolumn属性为空或未指定");
							continue;
						}
						textcolumn = XmlUtil.getAttributeString(el, "textcolumn");
						if (Tools.strIsEmpty("textcolumn")) {
							log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
									+ "个sqlinfo > querytree节点的textcolumn属性为空或未指定");
							continue;
						}
						diffway = XmlUtil.getAttributeString(el, "diffway", "upper");
						if (Tools.strIsEmpty("diffway")) {
							log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
									+ "个sqlinfo > querytree节点的diffway属性为空或未指定");
							continue;
						}
						diffcondition = XmlUtil.getAttributeString(el, "diffcondition");
						if (Tools.strIsEmpty("diffcondition")) {
							log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
									+ "个sqlinfo > querytree节点的diffcondition属性为空或未指定");
							continue;
						}
						iconClscolumn = XmlUtil.getAttributeString(el, "iconClscolumn");
						iconcolumn = XmlUtil.getAttributeString(el, "iconcolumn");
						initexpand = XmlUtil.getAttributeString(el, "initexpand", "none");
						isasync = XmlUtil.getAttributeBoolean(el, "isasync", false);
						querytree = new SqlQueryTree(idcolumn, textcolumn, iconClscolumn, iconcolumn, diffway,
								diffcondition, initexpand, isasync);
					}

					if (checks != null && checks.isEmpty())
						checks = null;
					if (transfers != null && transfers.isEmpty())
						transfers = null;

					SqlConfig conf = new SqlConfig(sqlname, checks, transfers, querytree);
					// sql结点可出现多个，实现不同数据库的sql语句
					List<Element> sqlList = sqlinfoEl.elements("sql");

					if (sqlList == null || sqlList.size() < 1) {
						log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute > 第" + m
								+ "个sqlinfo > sql节点未指定或值为空");
						continue;
					}
					if (sqlList != null) {
						for (Element elSql : sqlList) {
							String sql = XmlUtil.getElementTextTrim(elSql);
							String dialect = XmlUtil.getAttributeString(elSql, "dialect");
							conf.addSql(sql, dialect);
						}
					}

					sqlinfoList.add(conf);
				}
				if (sqlinfoList.size() == 0) {
					log.error("加载SQL配置文件" + filename + "时出错：exeid=" + exeid + "的sqlexecute指定了sqlinfos节点不能为空");
					continue;
				}
			} // end if sqlinfos!=null
				// /////////////////////////////////////////////////////////////////

			SqlExecute sqlexe = new SqlExecute(exeid, exename, logexeid, datasource, sqlidList, sqlinfoList);
			// 是否 以预编译方式来执行SQL语句
			sqlexe.setPstm(pstm);
			// 添加到缓存
			this.putSqlExecute(exeid, sqlexe);
			// 收集已加载的exeid
			if (!loadedExeids.contains(exeid))
				loadedExeids.add(exeid);

			// 与文件名称关联
			add2Executefiles(confFile, exeid);
		}
		res = "加载配置文件完成：" + filepath;
		log.info(res);
		return res;
	}

	/**
	 * 获取SQL执行关联配置对象
	 * 
	 * @param exeid
	 * @return
	 * @throws Bizr2SysSqlErrorExceptiona
	 */
	public static SqlExecute getExeid(String exeid) {
		SysSql sysSql = SysBeans.getSysSql();

		exeid = Tools.trimString(exeid);
		SqlExecute config = sysSql.getSqlExecute(exeid);
		if (config == null) {
			log.error("找不到exeid=" + exeid + "的SQL执行关联配置信息");
		}
		return config;
	}

	/**
	 * 获取SQL配置
	 */
	// public static SqlConfig getSqlid(String sqlid)
	// {
	// return getSqlid(sqlid, "oracle");
	// SysSql sysSql = SysBeans.getSysSql();
	//
	// sqlid = Tools.trimString(sqlid);
	// SqlConfig config = sysSql.getSqlConfig(sqlid);
	// if (config == null)
	// {
	// log.error("找不到sqlid=" + sqlid + "的SQL配置信息");
	// }
	// return config;
	// }
	public static SqlConfig getSqlid(String sqlid) {
		return getSqlid(sqlid, DIALECT_ORA);
	}

	public static SqlConfig getSqlid(String sqlid, String dbtype) {
		SysSql sysSql = SysBeans.getSysSql();

		sqlid = Tools.trimString(sqlid);
		SqlConfig config = sysSql.getSqlConfig(sqlid, dbtype);
		if (config == null) {
			log.error("找不到sqlid=" + sqlid + "的SQL配置信息");
		}
		return config;
	}

	/**
	 * 获取已加载的sqlid集合
	 */
	public static List<String> getSqlids() {
		return loadedSqlids;
	}

	/**
	 * 获取已加载的exeid集合
	 */
	public static List<String> getExeids() {
		return loadedExeids;
	}

	/**
	 * 获取已加载的SQL配置文件集合
	 */
	public static List<String> getLoadedSqlinfoFiles() {
		List<String> list = new ArrayList<String>();
		list.addAll(SqlConfigFile.listFilenames(sqlinfoFiles));
		return list;
	}

	/**
	 * 获取已加载的SQL执行配置文件集合
	 */
	public static List<String> getLoadedExecuteFiles() {
		List<String> list = new ArrayList<String>();
		list.addAll(SqlConfigFile.listFilenames(executeFiles));
		return list;
	}

	/**
	 * 这个对象由SPRING自动注入，在这个类里需要使用这个对象来获取配置文件的实际路径
	 */
	@SuppressWarnings("unused")
	private ServletContext servletContext;

	/**
	 * 提供给SPRING注入的方法
	 */
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/**
	 * 提供给SPRING注入的方法
	 */
	public void setConfigFiles(List<String> configFiles) {
		this.configFiles = configFiles;
	}

	/**
	 * 已读取的配置文件与sqlid对应关系<br />
	 * 以每个页面配置文件的名称作为key，保存此配配置文件中有哪些sqlid定义在这里
	 * 
	 * @return
	 */
	public static List<SqlConfigFile> getSqlinfoFiles() {
		return sqlinfoFiles;
	}

	public boolean isInited() {
		return init;
	}

	public static void main(String[] args) {
		String name = "dbtype_oracle";
		String str = "dbtype_";
		String dbtype = null;
		if (name.startsWith(str)) {
			dbtype = name.substring(str.length());
		}
		System.out.println(dbtype);
	}

}