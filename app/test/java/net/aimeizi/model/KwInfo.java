package test.java.net.aimeizi.model;

import io.searchbox.annotations.JestId;

import java.util.Date;

import org.elasticsearch.search.SearchHit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class KwInfo {
    private String url;
    private String access_url;
    private String data_source_url;
    private String tags;
    private String content;
    private String user_name;
    private String site_name;
    private String page_name;
    private String commit_id;
	private String extra_data;
    private String extra_search;
    private String create_time;
    private String update_time;
    private String highlight_ext;  //高亮扩展字段 20170901
    private String extra_type;  //新增20170907

	public static String kwInfoSegName[] = { "url", "access_url", "data_source_url", "tags", 
    		"content", "user_name", "site_name", "page_name", "commit_id", "extra_data",
    		"extra_search", "create_time", "update_time" };
    
	public KwInfo() {
    }

    public KwInfo(String url, String access_url, String data_source_url, String tags,
    		      String content, String user_name, String site_name, String page_name, String commit_id,
    		      String extra_data, String extra_search, String extra_type, String create_time, String update_time) 
    {
        super();
        this.url = url;
        this.access_url = access_url;
        this.data_source_url = data_source_url;
        this.tags = tags;
        this.content = content;
        this.user_name = user_name;
        this.site_name = site_name;
        this.page_name = page_name;
        //新增变量
        this.commit_id = commit_id;
        this.extra_data = extra_data;
        this.extra_search = extra_search;
        this.extra_type = extra_type;
        this.create_time = create_time;
        this.update_time = update_time;
    }
    
    public KwInfo(String url, String access_url, String data_source_url, String tags,
		      String content, String user_name, String site_name, String page_name, String commit_id,
	          String extra_data, String extra_search, String extra_type) 
    {
    	super();
    	this.url = url;
	 	this.access_url = access_url;
	 	this.data_source_url = data_source_url;
	 	this.tags = tags;
	 	this.content = content;
	 	this.user_name = user_name;
	 	this.site_name = site_name;
	 	this.page_name = page_name;
	 	//新增变量
	 	this.commit_id = commit_id;
	 	this.extra_data = extra_data;
	 	this.extra_search = extra_search;
	 	this.extra_type = extra_type;
    }

	public static void kwInfoPrint(KwInfo curKwInfo){
    	/*System.out.println("url =" + curKwInfo.getUrl());
    	System.out.println("access_url = " + curKwInfo.getAccess_url());
    	System.out.println("data_source_url = " + curKwInfo.getData_source_url() );
    	System.out.println("tags = " + curKwInfo.getData_source_url() );
    	System.out.println("content = " + curKwInfo.getContent());
    	System.out.println("user_name = " + curKwInfo.getUser_name());
    	System.out.println("site_name = " + curKwInfo.getSite_name());
    	System.out.println("page_nam = " + curKwInfo.getPage_name());
    	System.out.println("commit_id = " + curKwInfo.getCommit_id());
    	System.out.println("extra_data = " + curKwInfo.getExtra_data());
    	System.out.println("extra_search = " + curKwInfo.getExtra_search());
    	System.out.println("create_time = " + curKwInfo.getCreate_time());
    	System.out.println("update_time = " + curKwInfo.getUpdate_time());*/
    }
        
    //setting & getting process.......
	public String getExtra_type() {
		return extra_type;
	}

	public void setExtra_type(String extra_type) {
		this.extra_type = extra_type;
	}
	
	public String getHighlight_ext() {
			return highlight_ext;
	}

    public void setHighlight_ext(String highlight_ext) {
			this.highlight_ext = highlight_ext;
	}

    public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

    public String getExtra_data() {
		return extra_data;
	}

	public void setExtra_data(String extra_data) {
		this.extra_data = extra_data;
	}

	public String getExtra_search() {
		return extra_search;
	}

	public void setExtra_search(String extra_search) {
		this.extra_search = extra_search;
	}

	/**
	 * @return the url
	 
	public String getId() {
		return iid;
	}*/

    
    public String getCommit_id() {
		return commit_id;
	}

	public void setCommit_id(String commit_id) {
		this.commit_id = commit_id;
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
	public String getAccess_url() {
		return access_url;
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
	 
	public void setId(String id) {
		this.iid = id;
	}*/

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
		this.access_url = short_url;
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
