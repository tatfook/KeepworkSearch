package test.java.net.aimeizi.models;

import java.io.Serializable;

public class MArticlesInfo  implements Serializable{
	
	public  long id;
    public  String wb_id;
    public  String publish_timestamp; 
    public  String publish_time; 
    public  String wb_detail; 
    public  String update_time; 
    
    public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getWb_id() {
		return wb_id;
	}
	public void setWb_id(String wb_id) {
		this.wb_id = wb_id;
	}
	public String getPublish_timestamp() {
		return publish_timestamp;
	}
	public void setPublish_timestamp(String publish_timestamp) {
		this.publish_timestamp = publish_timestamp;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public String getWb_detail() {
		return wb_detail;
	}
	public void setWb_detail(String wb_detail) {
		this.wb_detail = wb_detail;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

}
