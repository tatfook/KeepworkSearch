package test.java.net.aimeizi.model;

import java.util.Date;

//历史原因，这里的ImageInfo实际对应接口的baike数据，索引:baike_index
public class ExtraDataInfo { 
	
	private String isConflict;
	private String sitename;    
	private String pagename;
	private String timestamp;
	private String content;
	private String url;
	private String username;
	private String isModify;
	private String itemId;

    public ExtraDataInfo() {
    }

    public ExtraDataInfo(String isConflict, String sitename, String pagename, String timestamp, String content,
    		String url, String username, String isModify, String itemId) 
    {
        super();
        this.isConflict = isConflict;
        this.sitename = sitename;
        this.pagename = pagename;
        this.timestamp = timestamp;
        this.content = content;
        this.url = url;
        this.username = username;
        this.isModify = isModify;
        this.itemId = itemId;
    }
    
    public String getIsConflict() {
		return isConflict;
	}

	public void setIsConflict(String isConflict) {
		this.isConflict = isConflict;
	}

	public String getSitename() {
		return sitename;
	}

	public void setSitename(String sitename) {
		this.sitename = sitename;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIsModify() {
		return isModify;
	}

	public void setIsModify(String isModify) {
		this.isModify = isModify;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

    
   
}
