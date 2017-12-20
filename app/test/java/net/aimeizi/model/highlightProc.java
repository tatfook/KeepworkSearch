package test.java.net.aimeizi.model;

import java.util.List;
import java.util.Map;

import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class highlightProc {
	 /*
	  **@brief:高亮处理接口
	  **@param:
	  **@return:Kwinfo
	  **@author:ycy
	  **@date:20170901
	  */
	  public static KwInfo getProcessedKwInfo(JsonObject jsonHitsObject, int highlight, int show_all){
       	//需显示全部..show_all = 1
    	KwInfo curKwInfo = new KwInfo();
    	JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();   
		/*if (sourceObject.get("content") != null){
	     	 // 封装kwInfo对象
	         String totalContent = sourceObject.get("content").getAsString();
	         if (totalContent.length() > 50){
	        	String spanWord = "-->";
            	int spanLen = spanWord.length();
            	int spanStartPos = totalContent.indexOf(spanWord);
            	int stratPos = 0;
            	if (spanStartPos + spanLen > 50){
            		stratPos = spanStartPos + spanLen - 50;
            		totalContent = totalContent.substring(stratPos, spanStartPos + spanLen);
            	}else{
            		totalContent = totalContent.substring(0, 50);
            	}
	         	//totalContent = totalContent.substring(0, 50);
	         }
	         curKwInfo.setContent(totalContent);
	     }*/
		
	     if (sourceObject.get("tags") != null){
	     	String strTags = "";
	     	 boolean bArrayFlag = sourceObject.get("tags").isJsonArray();
	        	 if (bArrayFlag){
	        		 JsonArray tagsArray = sourceObject.getAsJsonArray("tags");
	 	            for (int j = 0; j < tagsArray.size(); j++) {
	 	                String strTagTmp = tagsArray.get(j).getAsString();
	 	                strTagTmp = strTagTmp + " ";
	 	                strTags = strTags + strTagTmp;
	 	            }
	        	}else{
	        		strTags = sourceObject.get("tags").getAsString();
	        	}            
	            curKwInfo.setTags(strTags);
	     }
	           
	     if (sourceObject.get("url") != null){
	     	 String kwUrl = sourceObject.get("url").getAsString();
	          curKwInfo.setUrl(kwUrl);
	     }
	     if (sourceObject.get("access_url") != null){
	     	String kwShortUrl = sourceObject.get("access_url").getAsString();
	     	curKwInfo.setShort_url(kwShortUrl);
	     }
	     if (sourceObject.get("data_source_url") != null){
	     	String kwDataSourceUrl = sourceObject.get("data_source_url").getAsString();
	     	curKwInfo.setData_source_url(kwDataSourceUrl);
	     }
	     if (sourceObject.get("user_name") != null){
	     	String kwUserName = sourceObject.get("user_name").getAsString();
	     	curKwInfo.setUser_name(kwUserName);
	     }
	     if (sourceObject.get("site_name") != null){
	     	String kwSiteName = sourceObject.get("site_name").getAsString();
	     	curKwInfo.setSite_name(kwSiteName);
	     }
	     if (sourceObject.get("page_name") != null){
	     	String kwPageName = sourceObject.get("page_name").getAsString();
	     	curKwInfo.setPage_name(kwPageName);
	     }
	     if (sourceObject.get("commit_id") != null){
	     	String kwcommit_id = sourceObject.get("commit_id").getAsString();
	     	curKwInfo.setCommit_id(kwcommit_id);
	     } 
	     if (sourceObject.get("extra_data") != null){
	     	 String kwextra_data = sourceObject.get("extra_data").getAsString();
	         curKwInfo.setExtra_data(kwextra_data);
	         //获取json内容
	         ExtraDataInfo kwExtraData = (ExtraDataInfo) JSON.parseObject(kwextra_data, ExtraDataInfo.class);  
	         String totalContent = kwExtraData.getContent();
	         if (totalContent.length() > 50){
		        	String spanWord = "-->";
	            	int spanLen = spanWord.length();
	            	int spanStartPos = totalContent.indexOf(spanWord);
	            	int stratPos = 0;
	            	if (spanStartPos + spanLen > 50){
	            		stratPos = spanStartPos + spanLen - 50;
	            		totalContent = totalContent.substring(stratPos, spanStartPos + spanLen);
	            	}else{
	            		totalContent = totalContent.substring(0, 50);
	            	}
		         	//totalContent = totalContent.substring(0, 50);
		         }
	         	 totalContent = totalContent.replaceAll("(?i)[^a-zA-Z0-9\u4E00-\u9FA5]", "");	
	             //System.out.println("!!! content = " + totalContent);
		         curKwInfo.setContent(totalContent);
	     }
	     //20170901 此处需要特殊处理...
	     if (sourceObject.get("extra_search") != null){
	     	 String kwextra_search = sourceObject.get("extra_search").getAsString();
	         if (kwextra_search.length() > 50){
	        	String spanWord = "-->";
            	int spanLen = spanWord.length();
            	int spanStartPos = kwextra_search.indexOf(spanWord);
            	int stratPos = 0;
            	if (spanStartPos + spanLen > 50){
            		stratPos = spanStartPos + spanLen - 50;
            		kwextra_search = kwextra_search.substring(stratPos, spanStartPos + spanLen);
            	}else{
            		kwextra_search = kwextra_search.substring(0, 50);
            	}
	         	//totalContent = totalContent.substring(0, 50);
	         }
		     curKwInfo.setExtra_search(kwextra_search);
	     }
	     //20170907 此处需要特殊处理...
	     if (sourceObject.get("extra_type") != null){
	     	String kwextra_type = sourceObject.get("extra_type").getAsString();
	        curKwInfo.setExtra_type(kwextra_type);
	     }
	     if (sourceObject.get("create_time") != null){
	     	String kwcreate_time = sourceObject.get("create_time").getAsString();
	        curKwInfo.setCreate_time(kwcreate_time);
		 }	
	     if (sourceObject.get("update_time") != null){
	     	String kwupdate_time = sourceObject.get("update_time").getAsString();
	        curKwInfo.setUpdate_time(kwupdate_time);
		 }	
	     
	     
	     //ycy 此处有严重高亮bug，未发现根本原因。
         if (highlight == 1) {
         	  // 获取高亮字段
         	if (jsonHitsObject.get("highlight") != null) {
	                JsonObject highlightObject = jsonHitsObject.get("highlight").getAsJsonObject();
	                String content = "";
	                String hlTags = "";
	                String extrasearchHighlight = "";
	                boolean bKeyWordMatched = false;
	                //20170901 begin
	                if (highlightObject.get("extra_search") != null) {
	                	extrasearchHighlight = highlightObject.get("extra_search").getAsJsonArray().get(0).getAsString();
	                    if (!extrasearchHighlight.equalsIgnoreCase("")) {
		                	curKwInfo.setHighlight_ext(extrasearchHighlight);
		                }
	                }else if (highlightObject.get("extra_search.keyword") != null) {
	                	extrasearchHighlight = highlightObject.get("extra_search.keyword").getAsJsonArray().get(0).getAsString();
	                    if (!extrasearchHighlight.equalsIgnoreCase("")) {
		                	curKwInfo.setHighlight_ext(extrasearchHighlight);
		                }
	                }      
	                //end   
	                if (highlightObject.get("extra_data") != null) {
	                    content = highlightObject.get("extra_data").getAsJsonArray().get(0).getAsString();
	                   // System.out.println("content high = " + content);
	                }else if (highlightObject.get("extra_data.keyword") != null){
	                	content = highlightObject.get("extra_data.keyword").getAsJsonArray().get(0).getAsString();
	                	bKeyWordMatched = true;
	                }
	                if (highlightObject.get("tags") != null) {
	                	 //hlTags = highlightObject.get("tags").getAsJsonArray().get(0).getAsString();
	                	 JsonArray hlTagsArray = highlightObject.get("tags").getAsJsonArray();
	                     for (int j = 0; j < hlTagsArray.size(); j++) {
	                         String strHighTagTmp = hlTagsArray.get(j).getAsString();
	                         strHighTagTmp = strHighTagTmp + " ";
	                         hlTags = hlTags + strHighTagTmp;
	                     }
	                }else if (highlightObject.get("tags.keyword") != null){
	   		    	// System.out.println("tags.keyword != null");
	   		    	 JsonArray hlTagsArray = highlightObject.get("tags.keyword").getAsJsonArray();
                     for (int j = 0; j < hlTagsArray.size(); j++) {
                         String strHighTagTmp = hlTagsArray.get(j).getAsString();
                         strHighTagTmp = strHighTagTmp + " ";
                         hlTags = hlTags + strHighTagTmp;
                     	}
	                }
	                
	                if (!content.equalsIgnoreCase("")) {
	                		//获取最后一个</span>的位置
	                	//System.out.println("[highlight]content = " + content);
	                	//无需显示全部..show_all = 0
	                	if (show_all == 0){
	                		if (content.length() > 50)
		                	{
		                		String spanStartWord = "<span";
		                		String spanEndWord = "</span>";
		                		if (bKeyWordMatched){
		                		
			                		int spanStartPos = content.indexOf(spanStartWord);	
			                		if (content.length() > 80) {
			                			content = content.substring(spanStartPos, spanStartPos + 80);
			                		}else{
			                			content = content.substring(spanStartPos, content.length());
			                		}
			                		if (!content.contains(spanEndWord)){
			                			content += spanEndWord;
			                		}
		                		}else{
		                			
			                		int spanLen = spanEndWord.length();
			                		int spanStartPos = content.indexOf(spanEndWord);
			                		int stratPos = 0;
			                	//	System.out.println("spanStartPos = " + spanStartPos);
			                		if (spanStartPos + spanLen > 50){
			                			stratPos = spanStartPos + spanLen - 50;
			                		}
			                		//System.out.println("startPos = " + stratPos);
			                		content = content.substring(stratPos, spanStartPos + spanLen);
		                		}
		                	}
	                	}//show all显示全部
	                	curKwInfo.setContent(content);
	                }
	                if (!hlTags.equalsIgnoreCase("")) {
	                	curKwInfo.setTags(hlTags);
	                }
         	}//end jsonHitsObject.get("highlight")
         }//end highlight
	     
	     
		return curKwInfo;
    }
}
