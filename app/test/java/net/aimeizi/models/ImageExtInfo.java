package test.java.net.aimeizi.models;

public class ImageExtInfo {

    private String title;
	private String path;
    private String content;
    private String create_time;
   	private String update_time;

    public ImageExtInfo() {
    }

    public ImageExtInfo(String title, String path, String content, String create_time, String update_time) 
    {
        super();
        this.title = title;
        this.path = path;
        this.content = content;
        this.create_time = create_time;
        this.update_time = update_time;
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

    public String getPath() {
  		return path;
  	}

  	public void setPath(String path) {
  		this.path = path;
  	}
  	
	public String getTitle() {
		return title;
	}
	
	public String getContent() {
		return content;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
