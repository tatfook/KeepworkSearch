package test.java.net.aimeizi.model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonParse {
	static Logger logger = Logger.getLogger(JsonParse.class);
	public static void main(String[] args) {
		
		//String strJsonPath = ".\\input\\test.json";
		//List<KwInfo> kwList = GetJsonList(strJsonPath);	
		//printJson(kwList);
	}
	
	 /**
     * JSONObject转为map
     * @param object json对象
     * @return 转化后的Map
     */
    public static Map<String, Object> toMap(JSONObject object){
    	
        Map<String, Object> map = new HashMap<String, Object>();

        for (String key : object.keySet()) {
            Object value = object.get(key);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }

        return map;
    }
	
	 /**
     * JSONArray转为List
     * @param array json数组
     * @return 转化后的List
     */
    public static List<Object> toList(JSONArray array){
        List<Object> list = new ArrayList<Object>();
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }
	
	/**
	 **@brief:json解析
	 **@param:文件路径
	 **@return:Json解析后的List
	 **@author:ycy
	 **@date:2017-06-22
	 */
	public static List<KwInfo> GetJsonList(String strJsonPath){
		 // 把字符串转为Json对象，这是因为我的json数据首先是json对象
		String strJson = ReadFile(strJsonPath, "gbk");
	    JSONObject jobj = JSON.parseObject(strJson);
	    
	    // 然后是jsonArray，可以根据我的json数据知道
	    JSONArray arr = jobj.getJSONArray("");
	    
	    // 根据Bean类的到每一个json数组的项
	    List<KwInfo> kwList = JSON.parseArray(arr.toString(), KwInfo.class);
	    return kwList;
	}
	
	
	/**
	 **@brief:image json 解析
	 **@param:文件路径
	 **@return:Json解析后的List
	 **@author:ycy
	 **@date:2017-07-19
	 */
	public static BaikeInfo GetImageJson(String strJsonPath){
		 // 把字符串转为Json对象，这是因为我的json数据首先是json对象
		//System.out.println("jsonPath = " + strJsonPath);
    	PropertyConfigurator.configure("log4j.properties");
		String strJson = ReadFile(strJsonPath, "utf-8");
		//System.out.println("strJson = " + strJson);
	
		BaikeInfo curImageInfo = null;
		try{
			curImageInfo = JSON.parseObject(strJson, BaikeInfo.class);	
		}catch(Exception e){
			logger.info("[err]strJson = " + strJson);
			logger.info(e);
		}
		
	    return curImageInfo;
	}
	
	/**
	 **@brief:json打印(测试用)
	 **@param:Json解析后的List
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	public static void printJson(List<KwInfo> kwList){
		 // 遍历
	    for(KwInfo perKw : kwList){
	        // 我这个demo的json数据获得第一层的数据
/*System.out.println(perKw.getId());
System.out.println(perKw.getUrl());
System.out.println(perKw.getName());
System.out.println(perKw.getTag());
System.out.println(perKw.getPath());
System.out.println(perKw.getTime());
System.out.println(perKw.getPlace());
System.out.println(perKw.getHistory());
System.out.println(perKw.getEntity());
System.out.println(perKw.getAttribute());

System.out.println("\n\n");*/
	        // 我这个demo的json数据获得第二层的数据
	        //System.out.println(perKw.getUserBean().getFollowers_count());
	    }
	}
	
	/**
	 **@brief:Image json打印(测试用)
	 **@param:Image Json解析后的List
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	public static void PrintImageJson(BaikeInfo imageInfo){
		 	// 遍历
	       // System.out.println("title = \t"  + imageInfo.getTitle());
	       // System.out.println("author = \t" + imageInfo.getAuthor());
	       // System.out.println("category = \t" + imageInfo.getCategory());
	       // System.out.println("publish_time = \t" + imageInfo.getPublish_time());
	       // System.out.println("content = \t" + imageInfo.getContent());
	       // System.out.println("\n\n");

	}
	
	/**
	 **@brief:baike json打印(测试用)
	 **@param:baike Json解析后的List
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	public static void PrintBaikeJson(BaikeInfo baikeInfo){
		 	// 遍历
	    	System.out.println("iid = \t"  + baikeInfo.getIid());
	    	System.out.println("md5 = \t" + baikeInfo.getMd5());
	    	System.out.println("sitename = \t" + baikeInfo.getSitename());
	    	System.out.println("section = \t" + baikeInfo.getSection());
	    	System.out.println("publish_time = \t" + baikeInfo.getPublish_time());
	        System.out.println("title = \t"  + baikeInfo.getTitle());
	        System.out.println("content = \t" + baikeInfo.getContent());
	        System.out.println("path = \t" + baikeInfo.getPath());
	        System.out.println("author = \t" + baikeInfo.getAuthor());
	        System.out.println("category = \t" + baikeInfo.getCategory());
	        System.out.println("create_time = \t" + baikeInfo.getCreate_time());
	        System.out.println("update_time = \t" + baikeInfo.getUpdate_time());
	        
	     //   System.out.println("page_no = \t" + baikeInfo.getPage_no());
	    //    System.out.println("page_num = \t" + baikeInfo.getPage_num());
	    //    System.out.println("divide_flag = \t" + baikeInfo.getDivide_flag());
	        System.out.println("article_id = \t" + baikeInfo.getArticle_id());
	        System.out.println("\n\n");
	}
	
	
	/**
	 **@brief:读取Json文件
	 **@param:文件路径
	 **@return:Json文件内容
	 **@author:ycy
	 **@date:2017-06-22
	 */
	public static String ReadFile(String Path, String codeFormat){
		BufferedReader reader = null;
		String laststr = "";
		try{
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, codeFormat);
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				laststr += tempString;
		}
		reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("laststr = " + laststr);
		return laststr;
	}

}
