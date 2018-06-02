package com.kayak.base.spring;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.kayak.base.util.AESUtil;

/**
 * 继承spring的PropertyPlaceholderConfigurer类为了使用在context.xml里配置的jdbc.
 * properties文件里的配置信息
 * 
 * @author chenhq
 *
 */
public class KPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private static Logger log = Logger.getLogger(KPropertyPlaceholderConfigurer.class);

	public final static String deskey = "kayakdes";

	private Properties props;

	public String getPropValue(String key) {
		if (this.props == null) {
			return null;
		}

		return this.props.getProperty(key);
	}

	@Override
	protected void loadProperties(Properties props) throws IOException {
		super.loadProperties(props);
		this.props = props;

		String isencode = this.props.getProperty("default.db.encode");
		if ("true".equals(isencode)) {// DES加密的账户密码，进行解密返回
			Set<Object> keys = this.props.keySet();
			if (keys != null && keys.size() > 0) {
				for (Object keyObj : keys) {
					String key = keyObj.toString();
					if (key.contains("jdbc.username") || key.contains("jdbc.password")) {
						String value = this.props.getProperty(key);
						try {
							this.props.setProperty(key, AESUtil.decrypt(value, deskey));
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			}
		}
	}

}
