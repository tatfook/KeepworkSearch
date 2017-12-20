package test.java.net.aimeizi.models;

import java.util.Date;

public class ImageInfo {

    private String title;
    private String author;
    private String category;
    private Date publish_time;
    private String content;


    public ImageInfo() {
    }

    public ImageInfo(String title, String author, String category, 
    		Date publish_time, String content) 
    {
        super();
        this.title = title;
        this.author = author;
        this.category = category;
        this.publish_time = publish_time;
        this.content = content;

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

	public Date getPublish_time() {
		return publish_time;
	}

	public String getContent() {
		return content;
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

	public void setPublish_time(Date publish_time) {
		this.publish_time = publish_time;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
   
}
