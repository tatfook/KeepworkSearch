package test.java.net.aimeizi.models;

import io.searchbox.annotations.JestId;


public class KwInfo {
    private String url;
    private String short_url;
    private String data_source_url;
    private String tags;
    private String content;
    private String user_name;
    private String site_name;
    private String page_name;


    public KwInfo() {
    }

    public KwInfo(String url, String short_url, String data_source_url, String tags,
    		      String content, String user_name, String site_name, String page_name) 
    {
        super();
        this.url = url;
        this.short_url = short_url;
        this.data_source_url = data_source_url;
        this.tags = tags;
        this.content = content;
        this.user_name = user_name;
        this.site_name = site_name;
        this.page_name = page_name;
    }

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the short_url
	 */
	public String getShort_url() {
		return short_url;
	}

	/**
	 * @return the data_source_url
	 */
	public String getData_source_url() {
		return data_source_url;
	}

	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the user_name
	 */
	public String getUser_name() {
		return user_name;
	}

	/**
	 * @return the site_name
	 */
	public String getSite_name() {
		return site_name;
	}

	/**
	 * @return the page_name
	 */
	public String getPage_name() {
		return page_name;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param short_url the short_url to set
	 */
	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}

	/**
	 * @param data_source_url the data_source_url to set
	 */
	public void setData_source_url(String data_source_url) {
		this.data_source_url = data_source_url;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @param user_name the user_name to set
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	/**
	 * @param site_name the site_name to set
	 */
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	/**
	 * @param page_name the page_name to set
	 */
	public void setPage_name(String page_name) {
		this.page_name = page_name;
	}
    
   
}
