package test.java.net.aimeizi.model;

import java.util.Date;

//历史原因，这里的ImageInfo实际对应接口的baike数据，索引:baike_index
public class BaikeInfo { 
	
	private String iid;
	private String md5;          //new add by ycy 20170908
	private String sitename;     //new add by ycy 20170908
	private String section;      //new add by ycy 20170908
    private String publish_time;
    private String title;
    private String content;
    private String path;         //new add by ycy 20170908
    private String author;
    private String category;
	private String create_time;  //new add by ycy 20170908
    private String update_time;  //new add by ycy 20170908

    private int divide_flag;			 //new add by ycy 20171013
    private int  page_no;     			 //new add by ycy 20171013

    private int page_num;                //new add by ycy 20171017
    private String article_id;            //new add by ycy 20171017

	private String article_uid;         //new add by ycy 20171117
    private int page;                   //new add by ycy 20171117
    private int totalpage;              //new add by ycy 20171117
	private String keepwork_url;         //new add by ycy 20171117
	
	private boolean public_status;
    private int source;
    
	public BaikeInfo() {
    }

	//10+2
    public BaikeInfo(String id, String md5, String sitename, String section, String publish_time,
    		String title, String content, String path, String author, String category) 
    {
        super();
        this.iid = id;
        this.md5 = md5;
        this.sitename = sitename;
        this.section = section;
        this.publish_time = publish_time;
        
        this.title = title;
        this.content = content;
        this.path = path;
        this.author = author;
        this.category = category;
    }
    
    //9+2
    public BaikeInfo(String id, String sitename, String section, String publish_time,
    		String title, String content, String path, String author, String category) 
    {
        super();
        this.iid = id;
        this.sitename = sitename;
        this.section = section;
        this.publish_time = publish_time;
        
        this.title = title;
        this.content = content;
        this.path = path;
        this.author = author;
        this.category = category;
    }
    
    //多加了一个字段，flag以规避冲突。 12+2
    public BaikeInfo(String id, String md5, String sitename, String section, String publish_time,
    		String title, String content, String path, String author, String category, 
    		int divide_flag, int page_no, int page_num, String article_id) 
    {
        super();
        this.iid = id;
        this.md5 = md5;
        this.sitename = sitename;
        this.section = section;
        this.publish_time = publish_time;
        
        this.title = title;
        this.content = content;
        this.path = path;
        this.author = author;
        this.category = category;
        this.divide_flag = divide_flag;
        this.page_no = page_no;
        this.page_num = page_num;
        this.article_id = article_id;
    }
    
    //多加了一个字段，flag以规避冲突。 11+1
    public BaikeInfo(String id, String sitename, String section, String publish_time,
    		String title, String content, String path, String author, String category, 
    		int divide_flag, int page_no, int page_num) 
    {
        this.iid = id;
        this.sitename = sitename;
        this.section = section;
        this.publish_time = publish_time;
        
        this.title = title;
        this.content = content;
        this.path = path;
        this.author = author;
        this.category = category;

  	    this.divide_flag = divide_flag;
        this.page_no = page_no;
        this.page_num = page_num;
        //this.article_id = article_id;
    }
    
    //new add 20171117
    public BaikeInfo(String id, String sitename, String section, String publish_time,
	    		String title, String content, String path, String author, String category, 
	    		String article_uid,  int page, int totalpage) 
	{
	        this.iid = id;
	        this.sitename = sitename;
	        this.section = section;
	        this.publish_time = publish_time;
	        
	        this.title = title;
	        this.content = content;
	        this.path = path;
	        this.author = author;
	        this.category = category;

	  	    this.article_uid = article_uid;
	        this.page = page;
	        this.totalpage = totalpage;
	        //this.article_id = article_id;
	}
    public BaikeInfo(String id, String md5, String sitename, String section, String publish_time,
    		String title, String content, String path, String author, String category, 
    		String article_uid,  int page, int totalpage) 
    {
        super();
        this.iid = id;
        this.md5 = md5;
        this.sitename = sitename;
        this.section = section;
        this.publish_time = publish_time;
        
        this.title = title;
        this.content = content;
        this.path = path;
        this.author = author;
        this.category = category;
        this.article_uid = article_uid;
        this.page = page;
        this.totalpage = totalpage;
    }
    
    
    //new add 20171122
    public BaikeInfo(String article_uid, String title, String content, 
			 		 String path, Integer page, Integer totalpage, 
			 		 boolean public_status, Integer source,
			 		 String author, String publish_time) 
    {
        super();
        this.article_uid = article_uid; 
        this.title = title;
        this.content = content;
        
        this.path = path;
        this.page = page;
        this.totalpage = totalpage;
        this.public_status = public_status;
        this.source = source;
        this.author = author;
        this.publish_time = publish_time;
    }
    
    public boolean getPublic_status() {
		return public_status;
	}

	public void setPublic_status(boolean public_status) {
		this.public_status = public_status;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}
    
    
    public String getKeepwork_url() {
		return keepwork_url;
	}

	public void setKeepwork_url(String keepwork_url) {
		this.keepwork_url = keepwork_url;
	}
    
    public String getArticle_uid() {
		return article_uid;
	}

	public void setArticle_uid(String article_uid) {
		this.article_uid = article_uid;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
    
   /* public int getPage_num() {
 		return page_num;
 	}

 	public void setPage_num(int page_num) {
 		this.page_num = page_num;
 	}*/

 	public String getArticle_id() {
 		return article_id;
 	}

 	public void setArticle_id(String article_id) {
 		this.article_id = article_id;
 	}
    
    public int getDivide_flag() {
		return divide_flag;
	}

	public void setDivide_flag(int divide_flag) {
		this.divide_flag = divide_flag;
	}

	
   /* public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		this.page_no = page_no;
	} */

	
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
    
    public String getMd5() {
		return md5;
	}


	public void setMd5(String md5) {
		this.md5 = md5;
	}


	public String getSitename() {
		return sitename;
	}


	public void setSitename(String sitename) {
		this.sitename = sitename;
	}


	public String getSection() {
		return section;
	}


	public void setSection(String section) {
		this.section = section;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}

    
    public String getIid(){
    	return iid;
    }

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getCategory() {
		return category;
	}

	public String getPublish_time() {
		return publish_time;
	}

	public String getContent() {
		return content;
	}
	
    public void setIid(String id){
    	this.iid = id;
    }
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
   
}
