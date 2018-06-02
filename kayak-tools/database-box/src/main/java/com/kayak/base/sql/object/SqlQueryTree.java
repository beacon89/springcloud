package com.kayak.base.sql.object;

public class SqlQueryTree
{
	private String idcolumn;
	private String textcolumn;
	private String iconClscolumn;
	private String diffway;
	private String diffcondition;
	private String initexpand;
	private Boolean isasync;
	private String iconcolumn;

	public SqlQueryTree(String idcolumn, String textcolumn, String iconClscolumn, String iconcolumn,
			String diffway, String diffcondition, String initexpand, Boolean isasync)
	{
		this.idcolumn = idcolumn;
		this.textcolumn = textcolumn;
		this.iconClscolumn = iconClscolumn;
		this.iconcolumn = iconcolumn;
		this.diffway = diffway;
		this.diffcondition = diffcondition;
		this.initexpand = initexpand;
		this.isasync = isasync;
	}

	public String getIdcolumn()
	{
		return idcolumn;
	}

	public void setIdcolumn(String idcolumn)
	{
		this.idcolumn = idcolumn;
	}

	public String getTextcolumn()
	{
		return textcolumn;
	}

	public void setTextcolumn(String textcolumn)
	{
		this.textcolumn = textcolumn;
	}

	public String getIconClscolumn()
	{
		return iconClscolumn;
	}

	public void setIconClscolumn(String iconClscolumn)
	{
		this.iconClscolumn = iconClscolumn;
	}

	public String getDiffway()
	{
		return diffway;
	}

	public void setDiffway(String diffway)
	{
		this.diffway = diffway;
	}

	public String getDiffcondition()
	{
		return diffcondition;
	}

	public void setDiffcondition(String diffcondition)
	{
		this.diffcondition = diffcondition;
	}

	public String getInitexpand()
	{
		return initexpand;
	}

	public void setInitexpand(String initexpand)
	{
		this.initexpand = initexpand;
	}

	public Boolean getIsasync()
	{
		return isasync;
	}

	public void setIsasync(Boolean isasync)
	{
		this.isasync = isasync;
	}

	public String getIconcolumn()
	{
		return iconcolumn;
	}

	public void setIconcolumn(String iconcolumn)
	{
		this.iconcolumn = iconcolumn;
	}

}
