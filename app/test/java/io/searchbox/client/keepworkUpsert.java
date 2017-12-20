package test.java.io.searchbox.client;

import io.netty.handler.codec.http.HttpResponse;
import io.searchbox.client.JestClient;
import main.java.io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import main.java.io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.Health;
import io.searchbox.cluster.NodesInfo;
import io.searchbox.cluster.NodesStats;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.DeleteByQuery;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.Suggest;
import io.searchbox.core.SuggestResult;
import io.searchbox.core.Update;
import io.searchbox.indices.ClearCache;
import io.searchbox.indices.CloseIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.Flush;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Optimize;
import io.searchbox.indices.Stats;
import test.java.net.aimeizi.models.Article;
import test.java.net.aimeizi.models.ESConfig;
import test.java.net.aimeizi.models.FileParse;
import test.java.net.aimeizi.models.FileProcess;
import test.java.net.aimeizi.models.ImageExtInfo;
import test.java.net.aimeizi.models.ImageInfo;
import test.java.net.aimeizi.models.JsonParse;
import test.java.net.aimeizi.models.KwInfo;
import test.java.net.aimeizi.models.MArticlesInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import com.alibaba.fastjson.JSON;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
//import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class keepworkUpsert {

    private static final int MAXSEARCHCNT = 10000;
	static Logger logger = Logger.getLogger(keepworkUpsert.class);
		
	public static void main(String[] args) {
		//初始化日志配置
    	PropertyConfigurator.configure("log4j.properties");
    	
    	//获取基础配置
    	try {
			initConfig("conf/jest.properties");
			String upsertPathName = ESConfig.upsert_path_name;
			System.out.println(upsertPathName);
			FileParse.traverseImageImport(new File(upsertPathName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	/*try {
			initConfig("conf/jest.properties");
			List<String> lineList = new ArrayList<String>();
	    	lineList = FileParse.readTxtFileIntoStringArrList("data/bigger2M.txt");
	    	
	    	int iArrLen = lineList.size();
	    	for (int i = 0; i < iArrLen; ++i){
	    		String titleName = lineList.get(i);
	    		String delQueryInfo = "{"
	    				+ " \"query\": { "
						+ "  \"term\": {"
						+ "   \"title.keyword\": \""
					    + titleName 
					    + "\""
						+ " }"
						+ "}"
						+ "}";
	    		logger.info("delQuery = " + delQueryInfo);
	    		kwdelbyquery(delQueryInfo);
	    	}
	    	
	    	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
    	
    	
		/*
    	String import_json = "{\"url\":\"http://keepwork.com/sangfor457/test33\","
				+ "\"short_url\":\"/ycy/test\","
				+ " \"data_source_url\":\"http://keepwork.com/wiki/wikieditor#/sangfor123/test/index\","
				+ "\"tags\":["
				+ " \"测试1\","
				+ "\"测试2\","
				+ "\"测试3\""
				+ "],"
				+ "\"content\":\"入围作品查看更多最新上传查看更多全部作品查看更多评委成员1 2 3 >>sangfor123创建者大赛规则1参赛方式下载Paracraft客户端，将作品上传分享到个人作品网站，并申请参赛。2作品要求作品需使用电影方块，主题正面，电影类、创意类等均可。参赛作者可同时报名多个作品，但作品必须未参加过往届创意空间比赛。官方鼓励作品的原创与多样性，同时也鼓励改编和重构其他作品。如果参考了创意空间的其它作品需要注明来源，如发现公开冒充他人作品则直接取消参赛资格。3评选方式参赛作品由北京开放大学专业的老师和Paracraft工作人员共同审核评选。活动奖励一等奖 （1名）1888元现金奖励+获奖证书二等奖 （1名）1888元现金奖励+获奖证书三等奖 （1名）1888元现金奖励+获奖证书优秀奖 （3名）1888元现金奖励+获奖证书\","
				+ "\"user_name\":\"sangfor123\","
				+ "\"site_name\":\"test\","
				+ "\"page_name\":\"sangfor123 test\""
				+ "}";
		try {
			insertIndex(import_json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
	}
	
	
	/**
	 **@brief:获取基础配置
	 **@param:路径
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	public static void initConfig(String propertie_path) throws Exception {
			Properties propertie = new Properties();
			FileInputStream inputFile;
			try {
				inputFile = new FileInputStream(propertie_path);
				propertie.load(inputFile);
				inputFile.close();
				
				ESConfig.es_ipaddr = propertie.getProperty("es_ipaddr");
				ESConfig.es_port = propertie.getProperty("es_port");
				ESConfig.es_json_path = propertie.getProperty("json_path");
				ESConfig.es_index_name = propertie.getProperty("index_name");
				ESConfig.es_type_name = propertie.getProperty("type_name");
				ESConfig.es_mirror_upsert_addr = propertie.getProperty("es_mirror_upsert_addr");
				ESConfig.upsert_path_name = propertie.getProperty("upsert_path_name");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 **@brief:删除所有索引
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void deleteIndex() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		DeleteIndex deleteIndex = new DeleteIndex.Builder("article").build();
		JestResult result = jestClient.execute(deleteIndex);
		System.out.println(result.getJsonString());
	}
	
	/**
	 **@brief:清空缓存
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void clearCache() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		ClearCache closeIndex = new ClearCache.Builder().build();
		JestResult result = jestClient.execute(closeIndex);
		System.out.println(result.getJsonString());
	}



	
	/**
	 **@brief:关闭索引
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void closeIndex() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		CloseIndex closeIndex = new CloseIndex.Builder("article").build(); 
		JestResult result = jestClient.execute(closeIndex);
		System.out.println(result.getJsonString());
	}

	/**
	 **@brief:优化索引
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void optimize() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		Optimize optimize = new Optimize.Builder().build(); 
		JestResult result = jestClient.execute(optimize);
		System.out.println(result.getJsonString());
	}

	/**
	 **@brief:刷新索引
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void flush() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		Flush flush = new Flush.Builder().build(); 
		JestResult result = jestClient.execute(flush);
		System.out.println(result.getJsonString());
	}

	/**
	 **@brief:判定索引是否存在
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void indicesExists() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		IndicesExists indicesExists = new IndicesExists.Builder("article").build();
		JestResult result = jestClient.execute(indicesExists);
		System.out.println(result.getJsonString());
	}

	/**
	 **@brief:查看节点信息
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void nodesInfo() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		NodesInfo nodesInfo = new NodesInfo.Builder().build();
		JestResult result = jestClient.execute(nodesInfo);
		System.out.println(result.getJsonString());
	}


	/**
	 **@brief:查看集群健康状态
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void health() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		Health health = new Health.Builder().build();
		JestResult result = jestClient.execute(health);
		System.out.println(result.getJsonString());
	}

	/**
	 **@brief:查看节点状态
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void nodesStats() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		NodesStats nodesStats = new NodesStats.Builder().build();
		JestResult result = jestClient.execute(nodesStats);
		System.out.println(result.getJsonString());
	}

	/**
	 **@brief:更新文档
	 **@param:index, type, id
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void updateDocument(String index,String type,String id) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		Article article = new Article();
		article.setId(Integer.parseInt(id));
		article.setTitle("中国3颗卫星拍到阅兵现场高清照");
		article.setContent("据中国资源卫星应用中心报道，9月3日，纪念中国人民抗日战争暨世界反法西斯战争胜利70周年大阅兵在天安门广场举行。资源卫星中心针对此次盛事，综合调度在轨卫星，9月1日至3日连续三天持续观测首都北京天安门附近区域，共计安排5次高分辨率卫星成像。在阅兵当日，高分二号卫星、资源三号卫星及实践九号卫星实现三星联合、密集观测，捕捉到了阅兵现场精彩瞬间。为了保证卫星准确拍摄天安门及周边区域，提高数据处理效率，及时制作合格的光学产品，资源卫星中心运行服务人员从卫星观测计划制定、复核、优化到系统运行保障、光学产品图像制作，提前进行了周密部署，并拟定了应急预案，为圆满完成既定任务奠定了基础。");
		article.setPubdate(new Date());
		article.setAuthor("匿名");
		article.setSource("新华网");
		article.setUrl("http://news.163.com/15/0909/07/B32AGCDT00014JB5.html");
		String script = "{" +
				"    \"doc\" : {" +
				"        \"title\" : \""+article.getTitle()+"\"," +
				"        \"content\" : \""+article.getContent()+"\"," +
				"        \"author\" : \""+article.getAuthor()+"\"," +
				"        \"source\" : \""+article.getSource()+"\"," +
				"        \"url\" : \""+article.getUrl()+"\"," +
				"        \"pubdate\" : \""+new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(article.getPubdate())+"\"" +
				"    }" +
				"}";
		Update update = new Update.Builder(script).index(index).type(type).id(id).build();
		JestResult result = jestClient.execute(update);
		System.out.println(result.getJsonString());
	}


	/**
	 **@brief:删除文档
	 **@param:index, type, id
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void deleteDocument(String index,String type,String id) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		Delete delete = new Delete.Builder(id).index(index).type(type).build();
		JestResult result = jestClient.execute(delete);
		System.out.println(result.getJsonString());
	}

	/**
	 **@brief:获取文档
	 **@param:index, type, id
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void getDocument(String index,String type,String id) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		Get get = new Get.Builder(index, id).type(type).build();
		JestResult result = jestClient.execute(get);
		Article article = result.getSourceAsObject(Article.class);
		System.out.println(article.getTitle()+","+article.getContent());
	}

	/**
	 **@brief:suggest处理
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void suggest() throws Exception{
		String suggestionName = "my-suggestion";
		JestClient jestClient = keepworkUpsert.getJestClient();
		Suggest suggest = new Suggest.Builder("{" +
				"  \"" + suggestionName + "\" : {" +
				"    \"text\" : \"the amsterdma meetpu\"," +
				"    \"term\" : {" +
				"      \"field\" : \"body\"" +
				"    }" +
				"  }" +
				"}").build();
		SuggestResult suggestResult = jestClient.execute(suggest);
		System.out.println(suggestResult.isSucceeded());
		List<SuggestResult.Suggestion> suggestionList = suggestResult.getSuggestions(suggestionName);
		System.out.println(suggestionList.size());
		for(SuggestResult.Suggestion suggestion:suggestionList){
			System.out.println(suggestion.text);
		}
	}
	
	/**
	 **@brief:match索引检索（非全文检索）
	 **@param:待检索内容
	 **@return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 **@author:ycy
	 **@date:2017-06-23
	 */
	public static String matchSearchOfPage(String queryString, int iPageNo) throws Exception {
		if(queryString == null || queryString.length() == 0 || queryString.equals("") 
				|| iPageNo <= 0 || iPageNo >= MAXSEARCHCNT){
			System.out.println("Param invalid! exit...");
			return null;
		}
        List<KwInfo> kwList = new ArrayList<KwInfo>();
		JestClient jestClient = keepworkUpsert.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", queryString));

		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		//highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(200);//高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = iPageNo;
		int pageSize = 10;
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
        
        if ((pageNumber - 1) * pageSize >= MAXSEARCHCNT){
	    	System.out.println("Result window is too large, "
	    			+ "from + size must be less than or equal to: [10000]");
	    	return null;
	    }
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                //multiple index or types can be added.
		                                //.addIndex("kwindex")
		                                //.addType("kwtype")
		                                .build();

		SearchResult result = jestClient.execute(search);
		System.out.println(result.getJsonString());  
		
		//解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
	    long took = jsonObject.get("took").getAsLong();
	    float tooksecond = (float)(took)/1000f;
	    int total = result.getTotal();
	    System.out.println("找到 " + total +" 条结果 （用时 " + tooksecond + " 秒）");
	    
	    //获取分页数
	    double dMaxPages = Math.ceil((double)total/(double)pageSize);
	    int iMaxPages = (int)dMaxPages;
	    System.out.println("共 " + iMaxPages +" 页");
	    if (iMaxPages < iPageNo){
	    	System.out.println( iPageNo + "页 不存在! 最大页为: " + iMaxPages);
	    }
	  	
	    return result.getJsonString();
	}
	
	/**
	 **@brief:match image索引检索（非全文检索）
	 **@param:待检索内容
	 **@return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 **@author:ycy
	 **@date:2017-06-23
	 */
	public static String matchSearchOfImagePage(String queryString, int iPageNo) throws Exception {
		if(queryString == null || queryString.length() == 0 || queryString.equals("") 
				|| iPageNo <= 0 || iPageNo >= MAXSEARCHCNT){
			System.out.println("Param invalid! exit...");
			return null;
		}
        List<KwInfo> kwList = new ArrayList<KwInfo>();
		JestClient jestClient = keepworkUpsert.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", queryString));

		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		//highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(200);//高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = iPageNo;
		int pageSize = 10;
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
        
        if ((pageNumber - 1) * pageSize >= MAXSEARCHCNT){
	    	logger.info("Result window is too large, "
	    			+ "from + size must be less than or equal to: [10000]");
	    	return null;
	    }
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                //multiple index or types can be added.
		                                .addIndex("image_index")
		                                .addType("image_type")
		                                .build();

		SearchResult result = jestClient.execute(search);
		//logger.info(result.getJsonString());  
		
		//解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
	    long took = jsonObject.get("took").getAsLong();
	    float tooksecond = (float)(took)/1000f;
	    int total = result.getTotal();
	    logger.info("找到 " + total +" 条结果 （用时 " + tooksecond + " 秒）");
	    
	    //获取分页数
	    double dMaxPages = Math.ceil((double)total/(double)pageSize);
	    int iMaxPages = (int)dMaxPages;
	    logger.info("共 " + iMaxPages +" 页");
	    if (iMaxPages < iPageNo){
	    	logger.info( iPageNo + "页 不存在! 最大页为: " + iMaxPages);
	    }
	    
	    return result.getJsonString();
	}
		
	/**
	 **@brief:指定索引的全文检索
	 **@param:待检索内容
	 **@return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 **@author:ycy
	 **@date:2017-06-23
	 */
	public static String fullTextByIndexOfPageQuery(String queryString, int pageNo) throws Exception {
		if(queryString == null || queryString.length() == 0 || queryString.equals("") 
				|| pageNo <= 0 || pageNo >= MAXSEARCHCNT){
			System.out.println("Param invalid! exit...");
			return null;
		}
		System.out.println("keyword = " + queryString);
		
		List<KwInfo> kwList = new ArrayList<KwInfo>();
        
		JestClient jestClient = keepworkUpsert.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		
		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		//highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(200);//高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;
		
		if ((pageNumber - 1) * pageSize >= MAXSEARCHCNT){
	    	System.out.println("Result window is too large, "
	    			+ "from + size must be less than or equal to: [10000]");
	    	return null;
	    }
		
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
			//	.addIndex("es_msearch_wx")
				.build();
		   
		SearchResult result = jestClient.execute(search);
		System.out.println(result.getJsonString());  
		
		//解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
	    long took = jsonObject.get("took").getAsLong();
	    float tooksecond = (float)(took)/1000f;
	    int total = result.getTotal();
	    System.out.println("找到 " + total +" 条结果 （用时 " + tooksecond + " 秒）");
	    
	    //获取分页数
	    double dMaxPages = Math.ceil((double)total/(double)pageSize);
	    int iMaxPages = (int)dMaxPages;
	    System.out.println("共 " + iMaxPages +" 页");
	    if (iMaxPages < pageNo){
	    	System.out.println( pageNo + "页 不存在! 最大页为: " + iMaxPages);
	    }
	    return result.getJsonString();
	}
		
	/**
	 **@brief:全文检索
	 **@param:待检索内容
	 **@return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void fullTextQuery(String queryString) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex("article")
				.addIndex("mkyong")
				.build();
		SearchResult result = jestClient.execute(search);
		System.out.println(result.getJsonString());  
		System.out.println("本次查询共查到："+result.getTotal()+"篇文章！");
	}
	
	/**
	 **@brief:手动结果展示
	 **@param:List<Article> articles, SearchResult result
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
    private void parseSearchResult(List<Article> articles, SearchResult result) throws ParseException {
       /* 
    	JsonObject jsonObject = result.getJsonObject();
        long took = jsonObject.get("took").getAsLong();
        JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
        long total = hitsobject.get("total").getAsLong();
        JsonArray jsonArray = hitsobject.getAsJsonArray("hits");

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();

            // 获取返回字段
            JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();

            // 封装Article对象
            Article article = new Article();
            article.setTitle(sourceObject.get("title").getAsString());
            article.setContent(sourceObject.get("content").getAsString());
            article.setSource(sourceObject.get("source").getAsString());
            article.setAuthor(sourceObject.get("author").getAsString());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            article.setPubdate(sdf.parse(sourceObject.get("pubdate").getAsString()));
            article.setUrl(sourceObject.get("url").getAsString());

            // 获取高亮字段
            JsonObject highlightObject = jsonHitsObject.get("highlight").getAsJsonObject();
            String content = null;
            String title = null;
            String source = null;
            String author = null;
            if (highlightObject.get("content") != null) {
                content = highlightObject.get("content").getAsJsonArray().get(0).getAsString();
            }
            if (highlightObject.get("title") != null) {
                title = highlightObject.get("title").getAsJsonArray().get(0).getAsString();
            }
            if (highlightObject.get("source") != null) {
                source = highlightObject.get("source").getAsJsonArray().get(0).getAsString();
            }
            if (highlightObject.get("author") != null) {
                author = highlightObject.get("author").getAsJsonArray().get(0).getAsString();
            }
            if (content != null) {
                article.setContent(content);
            }
            if (title != null) {
                article.setTitle(title);
            }
            if (source != null) {
                article.setSource(source);
            }
            if (author != null) {
                article.setAuthor(author);
            }
            articles.add(article);
        }*/
    }
	

	/**
	 **@brief:查询全部
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void searchAll() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
			//	.addIndex("article")
				.build();
		SearchResult result = jestClient.execute(search);
		System.out.println("本次查询共查到："+result.getTotal()+"篇文章！");
		List<Hit<Article,Void>> hits = result.getHits(Article.class);
		for (Hit<Article, Void> hit : hits) {
			Article source = hit.source;
			System.out.println("标题："+source.getTitle());
			System.out.println("内容："+source.getContent());
			System.out.println("url："+source.getUrl());
			System.out.println("来源："+source.getSource());
			System.out.println("作者："+source.getAuthor());
		}
	}

	/**
	 **@brief:匹配检索
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void matchSearch(String queryString) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", queryString));

		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                // multiple index or types can be added.
		                                .addIndex("article")
		                                .addType("article")
		                                .build();

		SearchResult result = jestClient.execute(search);
		System.out.println("本次查询共查到："+result.getTotal()+"篇文章！");
		List<Hit<Article,Void>> hits = result.getHits(Article.class);
		for (Hit<Article, Void> hit : hits) {
			Article source = hit.source;
			System.out.println("标题："+source.getTitle());
			System.out.println("内容："+source.getContent());
			System.out.println("url："+source.getUrl());
			System.out.println("来源："+source.getSource());
			System.out.println("作者："+source.getAuthor());
		}
	}
	
	/**
	 **@brief:创建索引
	 **@param:待检索字符串
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void createSearch(String queryString) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.fragmentSize(200);//高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		Search search = new Search.Builder(searchSourceBuilder.toString())
        .addIndex("article")
        .build();
		SearchResult result = jestClient.execute(search);
		System.out.println("本次查询共查到："+result.getTotal()+"篇文章！");
		List<Hit<Article,Void>> hits = result.getHits(Article.class);
		for (Hit<Article, Void> hit : hits) {
			Article source = hit.source;
			//获取高亮后的内容
			Map<String, List<String>> highlight = hit.highlight;
			if(highlight != null){
				List<String> titlelist = highlight.get("title");//高亮后的title
				if(titlelist != null){
					source.setTitle(titlelist.get(0));
				}
	
				List<String> contentlist = highlight.get("content");//高亮后的content
		
				if(contentlist!=null){
					source.setContent(contentlist.get(0));
				}
			}
			System.out.println("标题："+source.getTitle());
			System.out.println("内容："+source.getContent());
			System.out.println("url："+source.getUrl());
			System.out.println("来源："+source.getSource());
			System.out.println("作者："+source.getAuthor());
		}
	}

	/**
	 **@brief:插入索引
	 **@param:json串
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void insertIndex(String import_json) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		//	logger.info("json = " + import_json);
		//json转换为类型
		try{
			//查询url，存在则update；不存在，则insert操作。存在，则更新操作。
			
			KwInfo curKwInfo = JSON.parseObject(import_json, KwInfo.class);		    	
	    	Index index = new Index.Builder(curKwInfo)
					    			.index("keepwork_index")
					    			.type("keepwork_type")
					    			.build();
	    	JestResult result = jestClient.execute(index);
	    	if (result.isSucceeded()){
	    	//	logger.info("insert success!");
	    	}else{
	    		logger.info("insert failed");
	    	}	
	    	
			
		}catch(Exception e){
			logger.info("[err]import_json = " + import_json);
			logger.info(e);
		}
	}
	
	/**
	 **@brief:批量插入操作
	 **@param:json串
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-23
	 */
	private static void bulkIndex() throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();

		Article article1 = new Article(4,"中国获租巴基斯坦瓜达尔港2000亩土地 为期43年","巴基斯坦(瓜达尔港)港务局表示，将把瓜达尔港2000亩土地，长期租赁给中方，用于建设(瓜达尔港)首个经济特区。分析指，瓜港首个经济特区的建立，不但能对巴基斯坦的经济发展模式，产生示范作用，还能进一步提振经济水平。" +
				"据了解，瓜达尔港务局于今年6月完成了1500亩土地的征收工作，另外500亩的征收工作也将很快完成，所征土地主要来自巴基斯坦海军和俾路支省政府紧邻规划的集装箱货堆区，该经济特区相关基础设施建设预计耗资3500万美元。瓜港务局表示，目前已将这2000亩地租赁给中国，中方将享有43年的租赁权。巴基斯坦前驻华大使马苏德·汗表示，这对提振巴基斯坦经济大有助益：“我认为巴基斯坦所能获得的最大益处主要还是在于经济互通领域”。" +
				"为了鼓励国内外投资者到经济特区投资，巴政府特别针对能源、税制等国内投资短板，专门为投资者出台了利好政策来鼓励投资。这些举措包括三个方面，一是保障能源，即保障经济特区的电力和天然气供应方面享有优先权；二是减免税收，即为经济特区投资的企业提供为期10年的税收假期，并为企业有关生产设备的进口给予免关税待遇；三是一站式服务，即为有意投资经济特区的投资者提供一站式快捷服务，包括向投资者提供有关优惠政策的详尽资讯。马苏德·汗还指出，为了让巴基斯坦从投资中受益，巴政府尽可能提供了各种优惠政策：“(由于)巴基斯坦想从中获益，为此做了大量力所能及的工作”。" +
				"巴政府高度重视瓜港经济特区建设，将其视为现代化港口的“标配”，期待其能够最大化利用瓜港深水良港的自然禀赋，吸引国内外投资者建立生产、组装以及加工企业。为鼓励投资，巴方还开出了20年免税的优惠条件。" +
				"除了瓜达尔港，中巴还有哪些项目？" +
				"根据中国和巴基斯坦两国4月20日发表的联合声明，双方将积极推进喀喇昆仑公路升级改造二期(塔科特至哈维连段)、瓜达尔港东湾快速路、新国际机场、卡拉奇至拉合尔高速公路(木尔坦至苏库尔段)、拉合尔轨道交通橙线、海尔－鲁巴经济区、中巴跨境光缆、在巴实行地面数字电视传输标准等重点合作项目及一批基础设施和能源电力项目。" +
				"应巴基斯坦总统侯赛因和总理谢里夫邀请，中国国家主席习近平于4月20日至21日对巴基斯坦进行国事访问。访问期间，习近平主席会见了侯赛因总统、谢里夫总理以及巴基斯坦议会、军队和政党领导人，同巴各界人士进行了广泛接触。" +
				"双方高度评价将中巴经济走廊打造成丝绸之路经济带和21世纪海上丝绸之路倡议重大项目所取得的进展。巴方欢迎中方设立丝路基金并将该基金用于中巴经济走廊相关项目。" +
				"巴方将坚定支持并积极参与“一带一路”建设。丝路基金宣布入股三峡南亚公司，与长江三峡集团等机构联合开发巴基斯坦卡洛特水电站等清洁能源项目，这是丝路基金成立后的首个投资项目。丝路基金愿积极扩展中巴经济走廊框架下的其他项目投融资机会，为“一带一路”建设发挥助推作用。" +
				"双方认为，“一带一路”倡议是区域合作和南南合作的新模式，将为实现亚洲整体振兴和各国共同繁荣带来新机遇。" +
				"双方对中巴经济走廊建设取得的进展表示满意，强调走廊规划发展将覆盖巴全国各地区，造福巴全体人民，促进中巴两国及本地区各国共同发展繁荣。" +
				"双方同意，以中巴经济走廊为引领，以瓜达尔港、能源、交通基础设施和产业合作为重点，形成“1+4”经济合作布局。双方欢迎中巴经济走廊联委会第四次会议成功举行，同意尽快完成《中巴经济走廊远景规划》。", "http://news.163.com/15/0909/14/B332O90E0001124J.html", Calendar.getInstance().getTime(), "中国青年网", "匿名");
		Article article2 = new Article(5,"中央党校举行秋季学期开学典礼 刘云山出席讲话","新华网北京9月7日电 中共中央党校7日上午举行2015年秋季学期开学典礼。中共中央政治局常委、中央党校校长刘云山出席并讲话，就深入学习贯彻习近平总书记系列重要讲话精神、坚持党校姓党提出要求。" +
				"刘云山指出，党校姓党是党校工作的根本原则。坚持党校姓党，重要的是坚持坚定正确的政治方向、贯彻实事求是的思想路线、落实从严治校的基本方针。要把党的基本理论教育和党性党风教育作为主课，深化中国特色社会主义理论体系学习教育，深化对习近平总书记系列重要讲话精神的学习教育，深化党章和党纪党规的学习教育。要坚持实事求是的思想方法和工作方法，弘扬理论联系实际的学风，提高教学和科研工作的针对性实效性。要严明制度、严肃纪律，把从严治校要求体现到党校工作和学员管理各方面，使党校成为不正之风的“净化器”。" +
				"刘云山指出，坚持党校姓党，既是对党校教职工的要求，也是对党校学员的要求。每一位学员都要强化党的意识，保持对党忠诚的政治品格，忠诚于党的信仰，坚定道路自信、理论自信、制度自信；忠诚于党的宗旨，牢记为了人民是天职、服务人民是本职，自觉践行党的群众路线；忠诚于党的事业，勤政敬业、先之劳之、敢于担当，保持干事创业的进取心和精气神。要强化党的纪律规矩意识，经常看一看党的政治准则、组织原则执行得怎么样，看一看党的路线方针政策落实得怎么样，看一看重大事项请示报告制度贯彻得怎么样，找差距、明不足，做政治上的明白人、遵规守纪的老实人。" +
				"刘云山强调，领导干部来党校学习，就要自觉接受党的优良作风的洗礼，修好作风建设这门大课。要重温党的光荣传统，学习革命先辈、英雄模范和优秀典型的先进事迹和崇高风范，自觉践行社会主义核心价值观，永葆共产党人的先进性纯洁性，以人格力量传递作风建设正能量。要认真落实党中央关于从严治党、改进作风的一系列要求，贯彻从严精神、突出问题导向，把自己摆进去、把职责摆进去，推动思想问题和实际问题一起解决，履行好党和人民赋予的职责使命。" +
				"赵乐际出席开学典礼。" +
				"中央有关部门负责同志，中央党校校委成员、全体学员和教职工在主会场参加开学典礼。中国浦东、井冈山、延安干部学院全体学员和教职工在分会场参加开学典礼。", "http://news.163.com/15/0907/20/B2UGF9860001124J.html", Calendar.getInstance().getTime(), "新华网", "NN053");
		Article article3 = new Article(6,"俞正声率中央代表团赴大昭寺看望宗教界人士","国际在线报道：7号上午，赴西藏出席自治区50周年庆祝活动的中共中央政治局常委、全国政协主席俞正声与中央代表团主要成员赴大昭寺慰问宗教界爱国人士。俞正声向大昭寺赠送了习近平总书记题写的“加强民族团结，建设美丽西藏”贺幛，珐琅彩平安瓶，并向僧人发放布施，他在会见僧众时表示，希望藏传佛教坚持爱国爱教传统。" +
				"至今已有1300多年历史的大昭寺，在藏传佛教中拥有至高无上的地位。西藏的寺院归属于某一藏传佛教教派，大昭寺则是各教派共尊的神圣寺院。一年四季不论雨雪风霜，大昭寺外都有从四面八方赶来磕长头拜谒的虔诚信众。" +
				"7号上午，赴西藏出席自治区50周年庆祝活动的中共中央政治局常委、全国政协主席俞正声与中央代表团主要成员专门到大昭寺看望僧众，“我代表党中央、国务院和习主席向大家问好。藏传佛教有许多爱国爱教的传统，有许多高僧大德维护祖国统一和民族团结，坚持传播爱国爱教的正信。党和政府对此一贯给予充分肯定，并对藏传佛教的发展给予了支持。”" +
				"俞正声回忆这已是他自1995年以来第三次来大昭寺。他表示，过去二十年发生了巨大的变化，而藏传佛教的发展与祖国的发展、西藏的发展息息相关。藏传佛教要更好发展必须与社会主义社会相适应。他也向僧人们提出期望：“佛教既是信仰，也是一种文化和学问，希望大家不断提高自己对佛教的认识和理解，提高自己的水平。希望大家更好地管理好大昭寺，搞好民主管理、科学管理，使我们的管理更加规范。”" +
				"今天是中央代表团抵达拉萨的第二天，当天安排有多项与自治区成立五十周年相关活动。继上午接见自治区领导成员、离退休老同志、各族各界群众代表宗教界爱国人士，参观自治区50年成就展外，中央代表团下午还慰问了解放军驻拉萨部队、武警总队等。当天晚些时候，庆祝西藏自治区成立50周年招待会、文艺晚会将在拉萨举行。（来源：环球资讯）", "http://news.163.com/15/0907/16/B2U3O30R00014JB5.html", Calendar.getInstance().getTime(), "国际在线", "全宇虹");
		Article article4 = new Article(7,"张德江:发挥人大主导作用 加快完备法律规范体系","新华网广州9月7日电 中共中央政治局常委、全国人大常委会委员长张德江9月6日至7日在广东出席第21次全国地方立法研讨会，并在佛山市就地方人大工作进行调研。他强调，要全面贯彻落实党的十八大和十八届三中、四中全会精神，深入学习贯彻习近平总书记系列重要讲话精神，认真实施立法法，充分发挥人大及其常委会在立法工作中的主导作用，抓住提高立法质量这个关键，加快形成完备的法律规范体系，为协调推进“四个全面”战略布局提供法治保障。" +
				"张德江指出，立法权是人大及其常委会的重要职权。做好新形势下立法工作，要坚持党的领导，贯彻党的路线方针政策和中央重大决策部署，切实增强思想自觉和行动自觉。要牢固树立依法立法、为民立法、科学立法理念，尊重改革发展客观规律和法治建设内在规律，加强重点领域立法，做到立法主动适应改革和经济社会发展需要。充分发挥在立法工作中的主导作用，把握立项、起草、审议等关键环节，科学确定立法项目，协调做好立法工作，研究解决立法中的重点难点问题，建立健全立法工作格局，形成立法工作合力。" +
				"张德江说，地方性法规是我国法律体系的重要组成部分。地方立法关键是在本地特色上下功夫、在有效管用上做文章。当前要落实好立法法的规定，扎实推进赋予设区的市地方立法权工作，明确步骤和时间，做好各项准备工作，标准不能降低，底线不能突破，坚持“成熟一个、确定一个”，确保立法质量。" +
				"张德江强调，加强和改进立法工作，要有高素质的立法工作队伍作为保障。要把思想政治建设摆在首位，全面提升专业素质能力，充实力量，培养人才，努力造就一支忠于党、忠于国家、忠于人民、忠于法律的立法工作队伍。" +
				"张德江一直非常关注地方人大特别是基层人大工作。在粤期间，他来到佛山市人大常委会，详细询问立法、监督等工作情况，希望他们与时俱进、开拓创新，切实担负起宪法法律赋予的职责。他走进南海区人大常委会、顺德区乐从镇人大主席团，同基层人大代表和人大工作者亲切交谈，肯定基层人大代表联系群众的有益做法，强调人大代表不能脱离人民群众，必须把人民利益放在心中，时刻为群众着想，听取群众意见，反映群众意愿，帮助群众解决实际问题。张德江指出，县乡人大在基层治理体系和治理能力建设中具有重要作用。要贯彻落实中央关于加强县乡人大工作和建设的精神，认真实施新修改的地方组织法、选举法、代表法，不断提高基层人大工作水平，推动人大工作迈上新台阶。" +
				"中共中央政治局委员、广东省委书记胡春华参加上述活动。", "http://news.163.com/15/0907/20/B2UGEUTJ00014PRF.html", Calendar.getInstance().getTime(), "新华网", "陈菲");

		Bulk bulk = new Bulk.Builder()
				.defaultIndex("article")
				.defaultType("article")
				.addAction(Arrays.asList(
						new Index.Builder(article1).build(),
						new Index.Builder(article2).build(),
						new Index.Builder(article3).build(),
						new Index.Builder(article4).build()
				)).build();
		jestClient.execute(bulk);
	}

	/**
	 **@brief:批量插入json数据
	 **@param:json路径
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	private static void kwBulkIndex(String strJsonPath) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		
		//获取Json路径
		List<KwInfo> kwList = JsonParse.GetJsonList(strJsonPath);	
		 // 遍历，此处是否有不用循环的写法？20170622 ycy
	    for(KwInfo perKw : kwList){
	    	Bulk bulk = new Bulk.Builder()
					.defaultIndex(ESConfig.es_index_name)
					.defaultType(ESConfig.es_type_name)
					.addAction(Arrays.asList(
							new Index.Builder(perKw).build()
					)).build();
	    	JestResult result = jestClient.execute(bulk);	 
	    	logger.info(result.toString());
	    }
	}
	
	/**
	 **@brief:批量插入mysql数据
	 **@param:json路径
	 **@return:空
	 **@author:ycy
	 **@date:2017-07-22
	 */
	public static void mysqlImportEs(String indexName, String typeName, 
			  List<MArticlesInfo> mArticleList) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
		
		 // 遍历，此处是否有不用循环的写法？20170622 ycy
	    for(MArticlesInfo preWb : mArticleList){
	    	Bulk bulk = new Bulk.Builder()
					.defaultIndex(indexName)
					.defaultType(typeName)
					.addAction(Arrays.asList(
							new Index.Builder(preWb).build()
					)).build();
	    	JestResult result = jestClient.execute(bulk);	 
	    	logger.info(result.toString());
	    }
	}
	
	
	/**
	 **@brief:遍历Json本地文件完成插入json数据
	 **@param:json路径
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	private static void imageBulkIndex() throws Exception {
		//遍历文件
		final String JSONFILEINPUT = ESConfig.es_json_path;
		logger.info("path = " + JSONFILEINPUT);
		LinkedList<String> curJsonList = FileProcess.getJsonFilePath(JSONFILEINPUT);
		logger.info("size = " + curJsonList.size());
		
		for (int i = 0; i < curJsonList.size(); ++i){
			//System.out.println(" i = " + i + " " + curJsonList.get(i));
			String curJsonPath = curJsonList.get(i);
		    ImageInfo curImageInfo = JsonParse.GetImageJson(curJsonPath);
		    //JsonParse.printImageJson(curImageInfo);
		    if (curImageInfo == null){
		    	continue;
		    }
			imageInsertIndex(curImageInfo);
		}
	}
	
	
	/**
	 **@brief:单条数据插入json数据
	 **@param:json路径
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	private static void imageInsertIndex(ImageInfo imageInfo) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();
	    JsonParse.PrintImageJson(imageInfo);
		
		Bulk bulk = new Bulk.Builder()
				.defaultIndex("image_index")
				.defaultType("image_type")
				.addAction(Arrays.asList(
						new Index.Builder(imageInfo).build()
				)).build();
    	JestResult result = jestClient.execute(bulk);	 
    	if (result.isSucceeded()){
    		System.out.println("insert success!");
    	}else{
    		System.out.println("insert failed");
    	}	
    	
	}
	
	/**
	 **@brief:单条数据插入json数据
	 **@param:json路径
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	public static void imageExtInsert(ImageExtInfo imageExtInfo) throws Exception {
		JestClient jestClient = keepworkUpsert.getJestClient();		
		Bulk bulk = new Bulk.Builder()
				.defaultIndex(ESConfig.es_index_name)
				.defaultType(ESConfig.es_type_name)
				.addAction(Arrays.asList(
						new Index.Builder(imageExtInfo).build()
				)).build();
    	JestResult result = jestClient.execute(bulk);	 
    	if (result.isSucceeded()){
    		System.out.println("insert success!");
    	}else{
    		System.out.println("insert failed");
    	}	
    	
	}
	
	public static void httpPostProess(){
		try {
			    HttpClient httpClient = HttpClientBuilder.create().build();
			    HttpPost postRequest = new HttpPost("http://10.0.1.36:9200/kwindex/kwtype/_search");
			    StringEntity params =new StringEntity("{ "
			    		+ "\"query\" : {"
			    		+ "\"bool\":{"
			    		+ "\"must\":[{\"term\":{\"tags.keyword\":\"hello\"}}]"
			    		+ "}"
			    		+ "}"
			    		+ "}");
			    System.out.println("resultJsonobject:  "+ params.toString());
			    postRequest.setEntity(params);
			    org.apache.http.HttpResponse response = httpClient.execute(postRequest);
			    if (response.getStatusLine().getStatusCode() != 200) {
			        throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			    }
			    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			    String output;
			    System.out.println("Output from Server .... \n");
			    while ((output = br.readLine()) != null) {
			        System.out.println(output);
			    }

		}catch (Exception ex) {

		    //handle exception here
			ex.printStackTrace();

		} finally {
		    //Deprecated
		    //httpClient.getConnectionManager().shutdown(); 
		}
	}
	
	/**
	 **@brief:post提交镜像数据
	 **@param:json路径
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	public static void postMirrorUpsert(ImageExtInfo imageExtInfo) throws ClientProtocolException, IOException{
		HttpClient httpclient =  HttpClients.custom()
		        							.setDefaultRequestConfig(RequestConfig.custom()
		        							.setCookieSpec(CookieSpecs.STANDARD).build())
		        							.build();  
		
		int timeoutSeconds = 30;
		int CONNECTION_TIMEOUT_MS = timeoutSeconds * 1000; // Timeout in millis.
		RequestConfig requestConfig = RequestConfig.custom()
		    .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
		    .setConnectTimeout(CONNECTION_TIMEOUT_MS)
		    .setSocketTimeout(CONNECTION_TIMEOUT_MS)
		    .build();

		HttpPost httppost = new HttpPost(ESConfig.es_mirror_upsert_addr);
		httppost.setConfig(requestConfig);
		
		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(5);
		params.add(new BasicNameValuePair("title", imageExtInfo.getTitle()));
		params.add(new BasicNameValuePair("path", imageExtInfo.getPath()));
		params.add(new BasicNameValuePair("content", imageExtInfo.getContent()));
		params.add(new BasicNameValuePair("create_time", imageExtInfo.getCreate_time()));
		params.add(new BasicNameValuePair("update_time", imageExtInfo.getUpdate_time()));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
	   
		//Execute and get the response.
		org.apache.http.HttpResponse response = httpclient.execute(httppost);
		/*HttpEntity entity = response.getEntity();
		if (entity != null) {
		    InputStream instream = entity.getContent();
		    try {
		        // do something useful
		    } finally {
		        instream.close();
		    }
		}*/
		  if (response.getStatusLine().getStatusCode() != 200) {
		        //throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			  logger.info("404 insert error!");
		  }else{
		    	logger.info("insert success !");
		  }
	}
	
	/**
	 **@brief:获取客户端
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	private static JestClient getJestClient() {
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://" + ESConfig.es_ipaddr + ":" + ESConfig.es_port)
		                        .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
		                        .multiThreaded(true)
		                        .readTimeout(10000)
		                        .build());
		 JestClient client = factory.getObject();
		 return client;
	}
	
	/**
	 **@brief:testBasicUriGeneration
	 **@param:空
	 **@return:空
	 **@author:ycy
	 **@date:2017-06-22
	 */
	 public static void testBasicUriGeneration() {
	   	 	JestClient jestClient = keepworkUpsert.getJestClient();
	      //  GetStartPage getStartPage = new GetStartPage.Builder().build();
	        Stats stats = new Stats.Builder().addIndex("es_msearch_wx").build();
	        try {
	            JestResult execute = jestClient.execute(stats);
	            JsonObject resultJson = execute.getJsonObject();
	            
	            System.out.println("result =" + execute.getJsonString());
	            System.out.println("count = " + resultJson.get("count"));
	        } catch (IOException ex) {
	            
	        	System.out.println("ex =" + ex);
	        }
	        
	    }

		/**
		 ** @brief:用户自定义删除。
		 ** @param:待检索内容
		 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
		 ** @author:ycy
		 ** @date:2017-06-23
		 */
		public static String kwdelbyquery(String queryString) throws Exception {
			String kwJsonRst = null;
			/*if (queryString == null || queryString.length() == 0 || queryString.equals("") ) {
				logger.info("[kwdelbyquery]Param invalid! exit...");
				return errProcess(ERR_PARAM_MSG);
			}*/

			JestClient jestClient = keepworkUpsert.getJestClient();
			DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(queryString)
		                .addIndex("baike_index_v2")
		                .addType("baike_type")
		                .build();
		    JestResult result = jestClient.execute(deleteByQuery);
		    logger.info("result = " + result.getJsonString());
			if (!result.isSucceeded()){
				String errMsg = result.getErrorMessage();
				logger.info("Error: " + errMsg);
				return null;
			}
			
			// 解析Json获取查询时间(ms)
			JsonObject jsonObject = result.getJsonObject();
			int deleteLines = jsonObject.get("deleted").getAsInt();
			String strMsg = "";
			if (deleteLines >= 1){
				strMsg = queryString + " deleted " + deleteLines + " documents!";
			}
			else{
				strMsg = "0 documents deleted or the del DSL is error!";
			}
			long took = jsonObject.get("took").getAsLong();
			float tooksecond = (float) (took) / 1000f;
			logger.info("[kwdelbyquery] delete ok （用时 " + tooksecond + " 秒）");

			Map<String, Object> kwRst = new HashMap<String, Object>();
			kwRst.put("code", 200);
			kwRst.put("msg", strMsg);
			kwRst.put("took", tooksecond);
			kwJsonRst = JSON.toJSONString(kwRst);
			return kwJsonRst;
		}

	
}