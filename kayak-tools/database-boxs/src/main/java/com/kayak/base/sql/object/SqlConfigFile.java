package com.kayak.base.sql.object;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;

public class SqlConfigFile {
	private String filename;

	private List<String> ids;

	private long lastModified;

	public SqlConfigFile(String filename, long lastModified) {
		this.filename = filename;
		this.lastModified = lastModified;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the ids
	 */
	public List<String> getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * @param lastModified
	 *            the lastModified to set
	 */
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public static SqlConfigFile find(List<SqlConfigFile> files, Resource resource) {
		for (SqlConfigFile file : files) {
			if (file.getFilename().equals(resource.getFilename())) {
				return file;
			}
		}
		return null;
	}

	public static List<String> listFilenames(List<SqlConfigFile> files) {
		List<String> list = new ArrayList<String>();
		for (SqlConfigFile file : files) {
			list.add(file.getFilename());
		}
		return list;
	}
}
