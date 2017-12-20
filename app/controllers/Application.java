package controllers;

import play.*;
import play.libs.Files;
import play.mvc.*;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.wordcount.WordDegrees;

import Service.DbService;
import play.mvc.Controller;
import test.java.io.searchbox.client.JestExample;
import test.java.net.aimeizi.model.ImageExtInfo;
import test.java.net.aimeizi.model.BaikeInfo;
import test.java.net.aimeizi.model.KwInfo;
import util.IpListConf;
import util.SnowFlake;
import util.WriteDB;
import util.dbconf;
import net.sf.json.JSONObject;


public class Application extends Controller {
	public final static int IMAGE_TITLETYPE = 0;
	public final static int IMAGE_FULLTEXTTYPE = 1;
	public final static int IMAGE_DETAILTYPE = 2;
	public final static int KEEPWORK_TAGSTYPE = 3;
	public final static int KEEPWORK_FULLTEXTTYPE = 4;
	
	public final static int BAIKE_TITLETYPE = 0;
	public final static int BAIKE_FULLTEXTTYPE = 1;
	public final static int BAIKE_DETAILTYPE = 2;
	public final static int BAIKE_ADVANCETYPE = 3;
	
	public final static int ERROR_CODE = 404;
	public final static int SUCCESS_CODE = 200;
	public final static String ERR_PARAM_MSG = "[ERROR] Invalid params!";
	public final static String TIME_ERR_MSG = "The time type does not support fuzzy matching and word divided matching !";
	public final static String TYPE_ERR_MSG = "This querytype does not support !";
	
	static Logger logger = Logger.getLogger(Application.class);
	//static String[] accessIpList = {"121.14.117.251","121.14.117.252","121.14.117.253","10.200.1.138","10.200.1.137"};
	static String[] accessIpList = null;
    public static void index() {
        redirect("/public/index.html");    	
    }
    
    
    @After
    public static void addCORS(){
    	response.setHeader("Access-Control-Allow-Origin", "*");
    	response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
    	response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, "
    			+ "If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, "
    			+ "Content-Type, X-E4M-With Authorization");
    }
    
    /*
     **@brief: 分词接口
     **@author:ycy
     **@date: 20170818
     */
    public static void genUniqId(){
    	
    	SnowFlake snowFlake = new SnowFlake(2, 3);    	
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	rstMap = rstProcess(SUCCESS_CODE, String.valueOf(snowFlake.nextId()));
    	addCORS();
	   	renderJSON(rstMap);
    }
    
    /*
     **@brief: 分词接口
     **@author:ycy
     **@date: 20170818
     */
    public static void kwsegwords(String context){
   	   	Map<String, Object> rstMap = new HashMap<String, Object>();
   	   	if ((context == null || context.length() == 0 || context.trim().equals("")) ){
   	   		System.out.println("[kwsegwords] context invaild params !");
   	   		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
   	   		addCORS();
   	   		renderJSON(rstMap);
   	   	}

    	WordDegrees degree = new WordDegrees();
    //	System.out.println("context = " + context);
   	   	String dividedkeywords = degree.WordProcessing(context);
   	   	System.out.println("[divided words] " + dividedkeywords);

   	   	rstMap = rstProcess(SUCCESS_CODE, dividedkeywords);
   	   	addCORS();
   	   	renderJSON(rstMap);	
    }
            
    /*
     **@brief: 大富插入接口
     **@author:ycy
     **@date: 20170818
     */
    public static void kwupsert(String url, String access_url, String data_source_url,
		 String tags, String content, String user_name, String site_name, 
		 String page_name, String commit_id, String extra_data, 
		 String extra_search, String extra_type, String create_time, String publish_time) throws Exception {
		 
    	 Map<String, Object> rstMap = new HashMap<String, Object>();
    	 if (url == null || url.trim().equals("") || url.length() == 0){
    		 System.out.println("[kwupsert] url is null or empty!");
    		 rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		 addCORS();
    		 renderJSON(rstMap);	  
    	 }
    	
    	 //获取IP，并完成和访问控制列表比对。
    	 String incomingIpAddr = request.remoteAddress;
    	 System.out.println("incoming addr: " + incomingIpAddr);
    	 String path = "KeepworkSearch/conf/iplist.properties";
    	// String path = "conf/iplist.properties";
    	 Properties propertie = new Properties();
 		 FileInputStream inputFile;
 		 inputFile = new FileInputStream(path);
 		 propertie.load(inputFile);
 		 IpListConf.access_ip_list = propertie.getProperty("access_ip_list").toLowerCase().split(",");
 		 accessIpList = IpListConf.access_ip_list;
 		 inputFile.close();
 		 
 		 boolean bAccessFlag = false;
		 for (int i = 0; i < accessIpList.length; i++)
		 {
			// System.out.println("currIP = " + accessIpList[i].trim());
			if (incomingIpAddr.equalsIgnoreCase(accessIpList[i].trim()))
			{
				bAccessFlag = true;
				break;
		 	}
		 }
		 
		 if (!bAccessFlag){
			 	System.out.println("bAccessFlag = false");
			 	 rstMap = rstProcess(ERROR_CODE, "The current IP does not allow data to be inserted or updated!");
	    		 addCORS();
	    		 renderJSON(rstMap);	
		 }else{
			 	System.out.println("bAccessFlag = true");
			 	if (request.method.equalsIgnoreCase("OPTIONS")  || request.method.equalsIgnoreCase("GET")){
			 		response.setHeader("Access-Control-Allow-Origin", "*" );
			 		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization" );
			 		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS" );
			 		response.setHeader("Access-Control-Allow-Credentials", "true");
			 		System.out.println(request.method + " method!");
			 		//rstMap = rstProcess(ERROR_CODE, "[kwupsert]" + request.method + " method!");
			 		//addCORS();
			 		//renderJSON(rstMap);	
			 	}
		 
		    	//初始化日志配置
		    	String searchJsonRst = null;
			 	KwInfo curKwInfo = new KwInfo( url, access_url, data_source_url, tags, content, 
			 								user_name, site_name, page_name, commit_id, 
			 								extra_data, extra_search, extra_type);
		    	searchJsonRst = JestExample.kwInsertIndex(curKwInfo);   
		    	//System.out.println("rst = " + searchJsonRst);
		    	
		    	addCORS();
				renderJSON(searchJsonRst);	     
		 }//end else

  	}
    
    /*
     **@brief: 百科数据插入接口(按照杨宗泽需求修改)
     **@author:ycy
     **@date: 20171122 
     */
    public static void baikeupsert(String article_uid, String title, String content, 
    								 String path, Integer page, Integer totalpage, 
    								 boolean public_status, Integer source,
    								 String author, String publish_time) throws Exception {
    	
	 	if (request.method.equalsIgnoreCase("OPTIONS")  || request.method.equalsIgnoreCase("GET")){
	 		response.setHeader("Access-Control-Allow-Origin", "*" );
	 		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization" );
	 		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS" );
	 		response.setHeader("Access-Control-Allow-Credentials", "true");
	 		System.out.println(request.method + " method!");
	 	}
	 
		String searchJsonRst = null;

		BaikeInfo curBaikeInfo = new BaikeInfo(article_uid, title, content, 
				 path, page,  totalpage, public_status, source, author, publish_time);
		searchJsonRst = JestExample.baikeUpsert(curBaikeInfo);  
		//System.out.println("rst = " + searchJsonRst);
	
		addCORS();
	    renderJSON(searchJsonRst);	     
    }
    
    /*
     **@brief: 百科数据插入接口(更新了大文件处理)
     **@author:ycy
     **@date: 20171012
     */
   /* public static void baikeupsert(String iid, String sitename, String section, 
    								 String publish_time, String title, String content, String path,
    								 String author, String category) throws Exception {
    	
	 	if (request.method.equalsIgnoreCase("OPTIONS")  || request.method.equalsIgnoreCase("GET")){
	 		response.setHeader("Access-Control-Allow-Origin", "*" );
	 		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization" );
	 		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS" );
	 		response.setHeader("Access-Control-Allow-Credentials", "true");
	 		System.out.println(request.method + " method!");
	 	}
	 
		String searchJsonRst = null;
	 	//字段拆分处理 20171011
	   // long splitedSize = 1024;  //1MB 
		int splitedSize = 1024*1024 ;  //100Byte 
		splitedSize = splitedSize / 3;      //汉字除以2
		int totalLen = content.length();
		if (totalLen >= splitedSize){
			double dPages = (double)totalLen / (double)splitedSize;
			int page_num = (int) Math.ceil(dPages);
			System.out.println("dPages = " + dPages + " len = " + totalLen  
					+ "splitedSize = " + splitedSize 
					+ " page_num " + page_num
					+ " content > 1MB, needed splite process!");
			int i = 0;
			String thisTitle = title;
			for(final String splitCont : Splitter.fixedLength(splitedSize).split(content)){
				System.out.println("split_i = " + i);
			    title = thisTitle + "_" + i;
				BaikeInfo curBaikeInfo = new BaikeInfo(iid, sitename, section, publish_time, 
						   title, splitCont, path, author, category, 1, i+1, page_num );
				searchJsonRst = JestExample.baikeUpsert(curBaikeInfo); 
			    ++i;
			}
			
			//20171012加上页面字段处理
		}else{
			BaikeInfo curBaikeInfo = new BaikeInfo(iid, sitename, section, publish_time, 
												   title, content, path, author, category);
			searchJsonRst = JestExample.baikeUpsert(curBaikeInfo);  
			//System.out.println("rst = " + searchJsonRst);
		}
		
		addCORS();
	    renderJSON(searchJsonRst);	     
    }*/
    
    /*
     **@brief: 百科数据插入接口ext(更新了大文件处理)
     **@author:ycy
     **@date: 20171117
     */
    public static void baikeupsertext(String iid, String sitename, String section, 
    								 String publish_time, String title, String content, String path,
    								 String author, String category, String article_uid, 
    								 Integer page, Integer totalpage) throws Exception {
    	
	 	if (request.method.equalsIgnoreCase("OPTIONS")  || request.method.equalsIgnoreCase("GET")){
	 		response.setHeader("Access-Control-Allow-Origin", "*" );
	 		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization" );
	 		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS" );
	 		response.setHeader("Access-Control-Allow-Credentials", "true");
	 		System.out.println(request.method + " method!");
	 	}
	 
		String searchJsonRst = null;
		BaikeInfo curBaikeInfo = new BaikeInfo(iid, sitename, section, publish_time, 
						   title, content, path, author, category, article_uid, page, totalpage);
		searchJsonRst = JestExample.baikeUpsertext(curBaikeInfo); 
		addCORS();
	    renderJSON(searchJsonRst);	     
    }
    
    /*
     **@brief: 镜像数据插入接口,更新了大字段处理。
     **@author:ycy
     **@date: 20171012
     */
    public static void mirrorupsert(String title, String path, String content, 
    								  String create_time, String publish_time) throws Exception {
		 
	 	if (request.method.equalsIgnoreCase("OPTIONS")  || request.method.equalsIgnoreCase("GET")){
	 		response.setHeader("Access-Control-Allow-Origin", "*" );
	 		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization" );
	 		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS" );
	 		response.setHeader("Access-Control-Allow-Credentials", "true");
	 		System.out.println(request.method + " method!");
	 	}
	 	
	 	String searchJsonRst = null;
	 	//字段拆分处理 20171011
	    int splitedSize = 1024 * 1024 ;  //1MB 
		splitedSize = splitedSize / 3;      //汉字除以2
		int totalLen = content.length();

		if (totalLen >= splitedSize){
			System.out.println("len = " + totalLen + " content > 1MB, needed splite process!");
			int i = 0;
			String thisTitle = title;
			for(final String splitCont : Splitter.fixedLength(splitedSize).split(content)){
				System.out.println("split_i = " + i);
			    title = thisTitle + "_" + i;
			    ImageExtInfo curImageExtInfo = new ImageExtInfo(title, path, splitCont, create_time,
			    		publish_time, 1, i+1);
				searchJsonRst = JestExample.imageExtInsert(curImageExtInfo); 
			    ++i;
			}
			
			//20171012加上页面字段处理
		}else{
			ImageExtInfo curImageExtInfo = new ImageExtInfo(title, path, content, create_time, publish_time);
			searchJsonRst = JestExample.imageExtInsert(curImageExtInfo);  
		}
		
		addCORS();
	    renderJSON(searchJsonRst);	     
    }
    
    
    /*
     **@brief: baike检索对接接口
     **@author:ycy
     **@date: 20170818
     */
    public static void baikesearch(String keyword, Integer page, Integer flag, Integer highlight) throws Exception {
    	//初始化日志配置
    	String searchJsonRst = null;   
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	boolean bMatchRst = false;
    	
    	System.out.println("keyword =" + keyword + " page =" + page + " flag =" + flag + "highlight =" + highlight);
    	if (flag == null || (flag != null && (flag < BAIKE_TITLETYPE 
    		|| flag > BAIKE_DETAILTYPE)) || page == null)
    	{
    		System.out.println("[baikesearch] flag invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	if (flag >= BAIKE_TITLETYPE && flag <= BAIKE_FULLTEXTTYPE && 
    			(highlight == null || (highlight != null && (highlight  > 1 ||  highlight < 0))))
    	{
    		System.out.println("[baikesearch] highlight invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
    	if (keyword.length() >= 50){
    		keyword = keyword.substring(0, 50);
    	}
    
    	//此处复用了baikeInfo的结构体信息 20170908
    	//历史原因，接口人杨宗泽 20170905 pm16:00才说的要区分image和baike数据的。
    	if (BAIKE_TITLETYPE == flag){
    		searchJsonRst = JestExample.baikeListTitleSearch(keyword, page, highlight);
    	}else if (BAIKE_FULLTEXTTYPE == flag){
    		searchJsonRst = JestExample.baikeListFullTextSearch(keyword, page, highlight);
    	}else if (BAIKE_DETAILTYPE == flag){
    		searchJsonRst = JestExample.baikeDetailByIdSearch(keyword, page);
    	}/*else if (BAIKE_ADVANCETYPE == flag){
    		searchJsonRst = JestExample.baikeAdvanceSearch(keyword, page, highlight);
    	}*/else{
    		System.out.println("[baikesearch] Type is error, exit!");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
    	addCORS();
		renderJSON(searchJsonRst);	     
  	}
    
    /*
     **@brief: baike检索对接接口
     **@author:ycy
     **@date: 20170818
     */
    public static void bkASearch(Integer page, String p1, String p2, 
    		String p3, String p4, String p5, String p6, String p7) throws Exception {
    	//初始化日志配置
    	String searchJsonRst = null;   
  
    	searchJsonRst = JestExample.baikeAdvanceSearch(page, p1, p2, p3, p4, p5, p6, p7);
    
    	addCORS();
		renderJSON(searchJsonRst);	     
  	}
    
    /*
     **@brief: 镜像对接入口
     **@author:ycy
     **@date: 20170818
     */
    public static void essearch(String keyword, Integer page, Integer flag, Integer highlight) throws Exception {
    	//初始化日志配置
    	String searchJsonRst = null;   
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	boolean bMatchRst = false;
    	
    	System.out.println("keyword =" + keyword + " page =" + page + " flag =" + flag + "highlight =" + highlight);
    	if (flag == null || (flag != null && (flag < IMAGE_TITLETYPE 
    		|| flag > IMAGE_DETAILTYPE)) || page == null)
    	{
    		System.out.println("[essearch] flag invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	if (flag >= IMAGE_TITLETYPE && flag <= IMAGE_FULLTEXTTYPE && 
    			(highlight == null || (highlight != null && (highlight  > 1 ||  highlight < 0))))
    	{
    		System.out.println("[essearch] highlight invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
    	if (keyword.length() >= 50){
    		keyword = keyword.substring(0, 50);
    	}
    
      	//以下注释代码通过本地IP221.0.111.131:19001访问无法获取参数，通过url或者post请求可以
    	//可能和Mod的方式有关，为保证业务，将这块比对关闭
    	/*if (IMAGE_DETAILTYPE == flag){
    		String[] paramArray = {"keyword", "page", "flag"};
        	bMatchRst = GetParamMatchRst(paramArray);
    	}else{
    		String[] paramArray = {"keyword", "page", "flag", "highlight"};
    		bMatchRst = GetParamMatchRst(paramArray);
    	}
    	if (!bMatchRst){
    		 rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		 addCORS();
    		 renderJSON(rstMap);	
    	} 
		*/
    	//此处复用了baikeInfo的结构体信息 20170908
    	//历史原因，接口人杨宗泽 20170905 pm16:00才说的要区分image和baike数据的。
    	if (IMAGE_TITLETYPE == flag){
    		searchJsonRst = JestExample.imageListTitleSearch(keyword, page, highlight);
    	}else if (IMAGE_FULLTEXTTYPE == flag){
    		searchJsonRst = JestExample.imageListFullTextSearch(keyword, page, highlight);
    	}else if (IMAGE_DETAILTYPE == flag){
    		searchJsonRst = JestExample.imageDetailByIdSearch(keyword, page);
    	}else{
    		System.out.println("[essearch] Type is error, exit!");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
    	addCORS();
		renderJSON(searchJsonRst);	     
  	}
    
    /*
     **@brief: keepwork全文检索
     **@author:ycy
     **@date: 20170818
     */
    public static void kwfulltext_search(String keyword, Integer page,  Integer highlight, Integer size) throws Exception {
    	//初始化日志配置
    	String searchJsonRst = null;
    	Map<String, Object> rstMap = new HashMap<String, Object>(); 
    	//以下注释代码通过Mod访问无法获取参数，通过url或者post请求可以
    	//可能和Mod的方式有关，为保证业务，将这块比对关闭
    	//20170829 by ycy
    	/*String[] paramArray = {"keyword", "page", "highlight", "size"};
    	boolean bMatchRst = GetParamMatchRst(paramArray);
    	if (!bMatchRst){
    		System.out.println("[kwfulltext_search] Param match error !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}*/
    	    	
    	if (page == null || highlight == null || size == null || 
    		(size != null && size == 0)){
    		System.out.println("[kwfulltext_search] Invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
    	if (keyword.length() >= 50){
    		keyword = keyword.substring(0, 50);
    	}
    	System.out.println("keyword =" + keyword + " page =" + page + " size = " + size);
    	
    	searchJsonRst = JestExample.kwfulltext_search(keyword, page, highlight, size);
    	
    	addCORS();
		renderJSON(searchJsonRst);	     
  	}
    
    /*
     **@brief: keepwork url模糊匹配
     **@author:ycy
     **@date: 20170818
     */
    public static void kwurllist_search(String keyword, Integer page, Integer size) throws Exception {
    	//初始化日志配置
    	String searchJsonRst = null;	
    	Map<String, Object> rstMap = new HashMap<String, Object>(); 
    	String[] paramArray = {"keyword", "page", "size"};
    	boolean bMatchRst = GetParamMatchRst(paramArray);
    	if (!bMatchRst){
    		System.out.println("[kwurllist_search] Params match error !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}  
    	
    	if (page == null || size == null || (size != null && 0 == size )){
    		System.out.println("[kwurllist_search] Invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
    	if (keyword.length() >= 50){
    		keyword = keyword.substring(0, 50);
    	}
    	System.out.println("keyword =" + keyword + " page =" + page + " size = " + size);
    	
    	searchJsonRst = JestExample.kwurllist_search(keyword, page, size);
    	
    	addCORS();
		renderJSON(searchJsonRst);	     
  	}
    
    /*
     **@brief: keepwork tags精确匹配
     **@author:ycy
     **@date: 20170818
     */
    public static void kwaccurate_search(String querytype, String keyword, 
    		Integer fuzzymatch, Integer page,  Integer highlight, Integer size) throws Exception {
    	String searchJsonRst = null;    	
    	String[] paramArray = {"querytype", "keyword", "fuzzymatch", 
    							"page", "highlight", "size"};
    	Map<String, Object> rstMap = new HashMap<String, Object>(); 
    	boolean bMatchRst = GetParamMatchRst(paramArray);
    	if (!bMatchRst){
    		System.out.println("[kwaccurate_search] Param match error !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);	
    		addCORS();
    	    renderJSON(rstMap);
    	}  
    	
    	//默认字段不存在
    	boolean segExist = false;
    	int iSegsCnt = KwInfo.kwInfoSegName.length;
    	for (int i = 0; i < iSegsCnt; ++i){
    		if (querytype.equals(KwInfo.kwInfoSegName[i])){
    			segExist = true;
    			break;
    		}
    	}
    	if (!segExist){
    		System.out.println("[kwaccurate_search] " + TYPE_ERR_MSG);
    		rstMap = rstProcess(ERROR_CODE, TYPE_ERR_MSG);	
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
      	System.out.println("keyword =" + keyword + " page =" + page + " size = " + size + " highlight = " + highlight);
    	if (querytype == null || querytype.equalsIgnoreCase("") 
    			|| querytype.length()==0 || keyword == null || keyword.equalsIgnoreCase("") 
    			|| keyword.length()==0 || highlight == null
    			|| fuzzymatch == null || (fuzzymatch != null && (fuzzymatch < 0 || fuzzymatch > 2))
    		    || page == null || size == null || (size != null && 0 == size)){
    		System.out.println("[kwaccurate_search] Invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);	
    		addCORS();
    	    renderJSON(rstMap);
    	}

    	if (querytype.contains("time") && (fuzzymatch ==1 || fuzzymatch == 2)){
    		System.out.println("[kwaccurate_search] " + TIME_ERR_MSG);
    		rstMap = rstProcess(ERROR_CODE, TIME_ERR_MSG);
    		addCORS();
    	    renderJSON(rstMap);
		}
    	searchJsonRst = JestExample.kwaccurate_search(querytype, keyword, fuzzymatch, page, highlight, size);
    	
    	addCORS();
		renderJSON(searchJsonRst);	     
  	}
    
    

    /*
     **@brief: keepwork tags详情接口
     **@author:ycy
     **@date: 20170818
     */
    public static void kwdetail_search(String keyword) throws Exception {
    	//初始化日志配置
    	String searchJsonRst = null;    	
    	System.out.println("keyword =" + keyword);
    	
    	searchJsonRst = JestExample.kwDetailByUrlSearch(keyword);
    	
    	addCORS();
		renderJSON(searchJsonRst);	     
  	}
    
    /*
     **@brief: keepwork 用户自定义检索
     **@author:ycy
     **@date: 20170818
     */
    public static void kwcustom_search(String keyword) throws Exception {
    	String searchJsonRst = null;    	
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	String[] paramArray = {"keyword"};
    	boolean bMatchRst = GetParamMatchRst(paramArray);
    	
    	if (!bMatchRst ){
    		System.out.println("[kwcustom_search] Invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);	
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
    	//System.out.println("keyword =" + keyword + " page =" + page + " size = " + size);
    	searchJsonRst = JestExample.kwcustom_search(keyword);
    	
    	addCORS();
		renderJSON(searchJsonRst);	
  	}
    
    /*
     **@brief: keepwork bool 检索
     **@author:ycy
     **@date: 20170818
     */
    public static void kwbool_search(){
    	String searchJsonRst = null;
    	try {
        	String[] recv_param_values = request.querystring.split("&");
        	int recv_param_cnt = recv_param_values.length;  
        	if (0 == recv_param_cnt){
        		System.out.println("[kwcustom_search] Invaild params !");
        		Map<String, Object> rstMap = new HashMap<String, Object>();
        		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);	
        		addCORS();
        	    renderJSON(rstMap);
        	}else{
        		LinkedList<String> strList = new LinkedList<String>();
        		int fuzzymatch = 0; //模糊匹配
        		int page = 0;
        		int size = 0;
            	for (int i = 0; i < recv_param_cnt; ++i ){
            		String[] sing_param = recv_param_values[i].split("=");
            		for (int j = 0; j < 2; ++j){
            				strList.add(sing_param[j]);
            		}
            	}
            	IdentityHashMap<String, String> argsHashMap = new IdentityHashMap<String, String>();
            	for (int i = 0; i < strList.size(); i=i+2){
    				 String strKey = null;
    				 String strVal = null;
    				 strKey = strList.get(i); 
    				 strVal = strList.get(i+1);
    				
    				 if (strKey.equals("fuzzymatch")){	
    					 fuzzymatch = Integer.parseInt(strVal);
    					 System.out.println("fuzzyval = " + fuzzymatch);
    					 continue;
    				 }
    				 
    				 if (strKey.equals("page")){	
    					 page = Integer.parseInt(strVal);
    					 System.out.println("page = " + page);
    					 continue;
    				 }
    				 
    				 if (strKey.equals("size")){	
    					 size = Integer.parseInt(strVal);
    					 System.out.println("size = " + size);
    					 continue;
    				 }
    				 
    				 strVal = URLDecoder.decode(strVal, "UTF-8");
    				 System.out.println("key = " + strKey + "\tval = " + strVal);
    				 argsHashMap.put(strKey, strVal);
    			 }
        		
    			searchJsonRst = JestExample.kwBoolSearch(fuzzymatch, page, size, argsHashMap);
    			addCORS();
    			renderJSON(searchJsonRst);
        	}
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*
     **@brief: keepwork del by query
     **@author:ycy
     **@date: 20170818
     */
    public static void kwdelbyquery(String keyword) throws Exception { 
    	Map<String, Object> rstMap = new HashMap<String, Object>();
      	String searchJsonRst = null;
    	//获取IP，并完成和访问控制列表比对。
   	 	String incomingIpAddr = request.remoteAddress;
   	 	System.out.println("incoming addr: " + incomingIpAddr);
   	 	String path = "KeepworkSearch/conf/iplist.properties";
   	 	// String path = "conf/iplist.properties";
   	 	Properties propertie = new Properties();
		FileInputStream inputFile;
		inputFile = new FileInputStream(path);
		propertie.load(inputFile);
		IpListConf.access_ip_list = propertie.getProperty("access_ip_list").toLowerCase().split(",");
		accessIpList = IpListConf.access_ip_list;
		inputFile.close();
		 
		boolean bAccessFlag = false;
		for (int i = 0; i < accessIpList.length; i++){
			if (incomingIpAddr.equalsIgnoreCase(accessIpList[i].trim())){
				bAccessFlag = true;
				break;
		 	}
		 }
		 
		 if (!bAccessFlag){
			 	System.out.println("bAccessFlag = false");
			 	 rstMap = rstProcess(ERROR_CODE, "The current IP does not allow data to be inserted or updated!");
	    		 addCORS();
	    		 renderJSON(rstMap);	
		 }else{
			 	System.out.println("bAccessFlag = true");		 
		    	//初始化日志配置
				String[] paramArray = {"keyword"};
		    	boolean bMatchRst = GetParamMatchRst(paramArray);
		    	
		    	if (!bMatchRst){
		    		System.out.println("[kwdelbyquery] Invaild params !");
		    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);	
		    		addCORS();
		    	    renderJSON(rstMap);
		    	}
		    	
		    	//System.out.println("keyword =" + keyword + " page =" + page + " size = " + size);
		    	searchJsonRst = JestExample.kwdelbyquery(keyword);
		    	
		    	addCORS();
				renderJSON(searchJsonRst);	   
		 }//end else
  	}
    
    public static void upload(String keyword, int page, int flag) throws Exception {
    	//初始化日志配置
    	String searchJsonRst = null;
    	if (keyword.length() >= 50){
    		keyword = keyword.substring(0, 50);
    	}
    	System.out.println("keyword =" + keyword + " page =" + page + " flag =" + flag);
    	
    	if (IMAGE_TITLETYPE == flag){
    		searchJsonRst = JestExample.matchSearchOfImagePage(keyword, page);
    		//JestExample.imageDetailByIdSearch(input, num);
    	}else if (IMAGE_FULLTEXTTYPE == flag){
    		searchJsonRst = JestExample.fullTextByImageOfPageQuery(keyword, page);
    	}else if (KEEPWORK_FULLTEXTTYPE == flag){
    		//searchJsonRst = JestExample.fullTextByIndexOfPageQuery(input, num);
    	}else if (KEEPWORK_FULLTEXTTYPE == flag){
    		//searchJsonRst = JestExample.fullTextByIndexOfPageQuery(input, num);
    	}else{
    		System.out.println("Type is error, exit!");
    		return;
    	}
    	
		JSONObject jsonRst = new JSONObject();
		jsonRst.put("status", SUCCESS_CODE);
		jsonRst.put("status_info", searchJsonRst);		
		
		addCORS();
		renderJSON(jsonRst);	  
    	    
  	}
    
    /*
     **@brief: 对参数做强匹配验证，参数个数，参数名称必须完全一致。
     **@author:ycy
     **@date: 20170818
     */
    public static boolean GetParamMatchRst(String [] paramArray) {
    	boolean bMatchRst = true;
    	int paramCnt = paramArray.length;
    	//System.out.println("params = " + request.querystring);
    	String[] recv_param_values = request.querystring.split("&");
    	int recv_param_cnt = recv_param_values.length;
    	if	(recv_param_cnt != paramCnt){
    		System.out.println("params count is error!");
    		bMatchRst = false;
    		return bMatchRst;
    	}
    	
    	int iMatchCnt = 0;
    	for (int i = 0; i < recv_param_cnt; ++i ){
    		String[] sing_param = recv_param_values[i].split("=");
    		String cur_key = sing_param[0];
    		for (int j = 0; j < paramCnt; ++j){
       			if (cur_key.equals(paramArray[j])){
    				//System.out.println("key = " + cur_key + "\t paramArray[" + j + "]= " + paramArray[j]);
    				++iMatchCnt;
    				break;
    			}
    		}
    	}
    	if (iMatchCnt != paramCnt){
    		System.out.println("params match is error!");
    		bMatchRst = false;
    		return bMatchRst;
    	}
		return bMatchRst;
    }
    
  
    
    /*
  	 ** @brief:登录处理
  	 * @params:空
  	 * @return:String
  	 * @author:ycy
  	 * @date:20170821
  	 **
  	 */
    public static void login(String username, String password) throws Exception {
    	//初始化日志配置
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	if (username == null || username.equalsIgnoreCase("") 
    			|| username.length()==0 || password == null || 
    			password.equalsIgnoreCase("") || password.length() == 0){
    		System.out.println("[register] Invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);	
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
		JSONObject jsonRst = new JSONObject();	
		WriteDB writedb = new WriteDB();
		String path = "conf/jest.properties";
		writedb.init_config(path);
		jsonRst.put("status", SUCCESS_CODE);
		try {
			writedb.init_mysql();
			LinkedHashMap<String, String> curMap = writedb.check_userinfo(username, password);
			if(curMap == null || curMap.size() == 0){
				//用户名不存在，可以导入
				System.out.println("useranme is not exist!");
				jsonRst.put("status_info", "username is not exist!");	
			}else{
				System.out.println("useranme is exist!");
				//获取密码，判定密码是否和输入一致；
				if (password.equals(curMap.get(username))){
					jsonRst.put("status_info", "login success!");	
				}else{
					jsonRst.put("status_info", "password is Error!");	
				}	
			}
		}catch (Exception ex) {
			logger.info(ex);
		}  finally {
			writedb.close_mysql();
		}
		
		addCORS();
		renderJSON(jsonRst);	  
    }
    
    /*
  	 ** @brief:register
  	 * @params:空
  	 * @return:String
  	 * @author:ycy
  	 * @date:20170821
  	 **
  	 */
    public static void register(String username, String password) throws Exception {
    	Map<String, Object> rstMap = new HashMap<String, Object>();
    	if (username == null || username.equalsIgnoreCase("") 
    			|| username.length()==0 || password == null || 
    			password.equalsIgnoreCase("") || password.length() == 0){
    		System.out.println("[register] Invaild params !");
    		rstMap = rstProcess(ERROR_CODE, ERR_PARAM_MSG);	
    		addCORS();
    	    renderJSON(rstMap);
    	}
    	
		JSONObject jsonRst = new JSONObject();	
		WriteDB writedb = new WriteDB();
		String path = "conf/jest.properties";
		writedb.init_config(path);

		try {
			writedb.init_mysql();
			if (!writedb.judgeUsername(username)){
				//用户名不存在，可以导入
				System.out.println("username is not exist!");
				writedb.importUserInfo(username, password);
				jsonRst.put("status", SUCCESS_CODE);
				jsonRst.put("status_info", "user_info import ok!");	
			}else{
				System.out.println("useranme is exist!");
				jsonRst.put("status", SUCCESS_CODE);
				jsonRst.put("status_info", "username is not exist!");		
			}
		}catch (Exception ex) {
			logger.info(ex);
		}  finally {
			writedb.close_mysql();
		}
		
		addCORS();
		renderJSON(jsonRst);
    	
    }
    
    
    /*
	 ** @brief:消息处理
	 * @params:空
	 * @return:String
	 * @author:ycy
	 * @date:20170821
	 **
	 */
	public static Map<String, Object> rstProcess(int rstCode, String strMsg) {
		System.out.println("Msg= " + strMsg);

		Map<String, Object> rstMap = new HashMap<String, Object>();
		rstMap.put("code", rstCode);
		rstMap.put("msg", strMsg);
		return rstMap;
	}

}