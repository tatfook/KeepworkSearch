package test.java.io.searchbox.client;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import main.java.io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import main.java.io.searchbox.client.config.HttpClientConfig;
import io.searchbox.cluster.Health;
import io.searchbox.cluster.NodesInfo;
import io.searchbox.cluster.NodesStats;
import io.searchbox.core.*;
import io.searchbox.core.BulkResult.BulkResultItem;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.search.aggregation.Aggregation;
import io.searchbox.core.search.aggregation.StatsAggregation;
import io.searchbox.core.search.sort.Sort;
import io.searchbox.indices.*;
import io.searchbox.params.Parameters;
import test.java.net.aimeizi.model.Article;
import test.java.net.aimeizi.model.FileProcess;
import test.java.net.aimeizi.model.ImageExtInfo;
import test.java.net.aimeizi.model.BaikeInfo;
import test.java.net.aimeizi.model.JsonParse;
import test.java.net.aimeizi.model.KwInfo;
import test.java.net.aimeizi.model.highlightProc;
import test.java.net.aimeizi.model.ESConfig;
import test.java.net.aimeizi.model.ExtraDataInfo;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.json.JSONObject;
import org.junit.Test;

import java.io.FileInputStream;
//import static org.junit.Assert.*;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JestExample {
	
	private static final String WBINDEX ="wb_data_rst_info";
   // private static final String KWINDEX = "kwindex";
   // private static final String KWTYPE = "kwtype";
   // private static final String IMAGEINDEX = "image_index";
   // private static final String IMAGETYPE = "image_type";
    private static final int MAXSEARCHCNT = 500000;
    private static final int MAX_INFO_LEN = 40;
	private static final String WINDOWS_ERR_MSG = "[ERROR]: Result window is too large, "
			+ "from + size must be less than or equal to: [500000]!";
	private static final String EMPTY_ERR_MSG = "[ERROR]: Return empty message!";
	private final static String ERR_PARAM_MSG = "[ERROR] Invalid params!";
	private final static String SLABLE_TITLE = "标题";
	private final static String SLABLE_CONTENT = "正文";
	private final static String SLABLE_TOTAL = "全部";

	static Logger logger = Logger.getLogger(JestExample.class);
	
	public static void main(String[] args) throws Exception {
		//初始化日志配置
    	PropertyConfigurator.configure("log4j.properties");
    	
    	//scrollSearchTest();
    	test();
    	
    //	String strTermVal = "曹操";
    //	imageListFullTextSearch(strTermVal, 5);
    	
		//1.批量插入本地json数据
		//String strJsonPath = ".\\input\\test.json";
		//kwBulkIndex(strJsonPath);
	}
	
	public static void test() {
		String content = "大文件分割测试3.txt_0"
				+ "施公案"
				+ "清·佚名 编撰"
				+ "第1回　胡秀才告状鸣冤　施贤臣得梦访案"
				+ "话说江都县有一秀才，姓胡，名登举。他的父母为人所杀，头颅不见。胡登举合家吓得胆裂魂飞，慌忙出门，去禀县主。"
				+ "跑到县衙，正遇升堂，就进去喊冤。走至堂上，打了一躬，手举呈词，口称：“父师在上，门生祸从天降。叩禀老父师，即赐严拿。”说着，将呈词递上。书吏接过，铺在公案。施公静心细阅。上写：具呈生员胡登举，祖居江都县。生父曾作翰林，告老家居，广行善事，怜恤穷苦，并无苛刻待人之事。不意于某日夜间，生父母闭户安眠。至天晓，生往请安，父母俱不言语。生情急，踢开门户，见父母尸身俱在床上，两个人头，并没踪影。生忝居学校，父母如此死法，何以身列校庠对双亲而无愧乎？为此具呈，嚎叩老父师大人恩准，速赐拿获凶手，庶生冤仇得雪。感戴无既。沾仁。上呈。"
				+ "施公看罢，不由点头，暗暗吃惊，想道：“夤夜入院，非奸即盗。胡翰林夫妇年老被杀，而不窃去财物，且将人头拿去，其中情由，显系仇谋。此宗无题文章，令人如何做法？”为难良久，说道：“即委捕厅四老爷，前去验尸。你只管入殓，自有头绪结断。”胡秀才一听，只得含泪下堂，出衙回家，伺候验尸。"
				+ "且说施公吩咐速去知会四衙，往胡家验尸呈报，把呈词收入袖内，吩咐退堂。进内书房坐下，长随送茶毕，用过了饭，把呈词取出，铺在案上翻阅。低头细想，此案难结。欠身伸手，在书架上拿了古书一部，系《拍案称奇》，放在桌上要看；对证此案，即日好断这没头之事。将《拍案称奇》，自头至尾看完，又取了一部，系海瑞参拿严嵩的故事。不觉困倦，放下书本，伏于书案之上，朦胧打睡。梦中看见外边墙头之下，有群黄雀儿九只，点头摇尾，唧哩喳啦，不住乱叫。施公一见，心中甚惊。又听见地上哼哼唧唧的猪叫；原来是油光儿的七个小猪儿，望着贤臣乱叫。施公梦中称奇，方要去细看，那九只黄雀儿，一齐飞下墙来，与地下七个小猪儿，点头乱噪。那七个小猪儿，站起身来，望黄雀拱抓，口内哼哼乱叫。雀噪猪叫，偶然起了一阵怪风，把猪雀都裹了去了。施公梦中一声惊觉，大叫说：“奇怪的事！”施安在旁边站立，见主人如此惊叫，不知何故，连忙叫：“老爷醒来！醒来！”施公听言，抬头睁眼，沉吟多时。想梦中之事，说：“奇哉！怪哉！”就问施安这天有多时了。施安答道：“日色西沉了。”施公点头，又问：“方才你可见些什么东西没有？”施安说：“并没见什么东西，倒有一阵风刮过墙去。”施公闻言，心中细想，这九只黄雀、七个小猪奇怪，想来内有曲情。将书搁在架上，前思后想，一夜未睡。直到天明，净面整衣，吩咐传梆升堂。坐下，抽签叫快头英公然、张子仁上来。二人走至堂上，跪下叩头。施公就将昨日梦见九只黄雀、七个小猪为题出签差人，说：“限你二人五日之期，将九黄、七猪拿来，如若迟延，重责不饶。”将签递于二人。二人跪趴半步，口称：“老爷容禀：小的们请个示来。"
				+ "这九黄、七猪，是两个人名，还是两个物名，现在何处？求老爷吩咐明白，小的们好去访拿。”言罢叩头。施公一听，说道：“无用奴才，连个九黄、七猪都不知道，还在本县应役么？分明偷闲躲懒，安心抗差玩法。”吩咐：“给我拉下去打！”两边发喊按倒，每人打了十五板。二人跪下叩头，复又讨示，叫声：“老爷，究竟吩咐明白，待小的们好去拿人。”施公闻言，心中不由大怒，说：“好大胆的奴才！本县深知你二人久惯应役，极会搪塞，如敢再行罗唣，定加重责！”二人闻言，万分无奈，站起退下去，访拿九黄、七猪而去。施公也随退堂。"
				+ "施公一连五日，假装有恙，并未升堂。到了第六日，一早吩咐点鼓升堂，坐下。衙役人等伺候。只见一人走至公堂案下，手捧呈词，口称：“父师，门生胡登举父母被杀之冤，求父师明鉴。倘迟久不获，凶犯走脱难捉。且生员读书一场，岂不有愧？如门生另去投呈伸冤，老父台那时休怨！”言罢一躬，将呈递上。施公带笑道：“贤契不必急躁。本县已经差人明捕暗访，专拿形迹可疑之人，审得自然替你申冤。”胡登举无奈，说道：“父台！速替门生伸冤，感恩不尽！”施公说：“贤契请回，催呈留下。”胡登举打躬下堂，出衙回家。且说施公为难多会，方要提胡宅管家的审问，只见公差英公然、张子仁上堂，跪下回禀：“小的二人，并访不着九黄、七猪，求老爷宽限。”"
				+ "施公闻言，激恼成怒，喝叫左右拉下，每人打十五大板。不容分说，只打的哀求不止，鲜血直流。打完提裤，战战兢兢，跪在地下，口尊：“老爷，叩讨明示，以便好去捉人。”施公闻言无奈，硬着心肠说道：“再宽你们三日限期，如其再不捉拿凶犯，定行处死！”二差闻言，筛糠打战，只是磕头，如鸡食碎米一般。施公又说：“你们不必多说，快快去捕要紧。”施公想二役两次受刑，亦觉心中不忍，退堂进内。可怜二人还在下面叩头，大叫：“老爷，可怜小的们性命罢！”言毕，又是咚咚的叩头。县堂上未散的三班六房之人，见二人这样，个个兔死狐悲，叹惜不止，一齐说：“罢呀！起来罢！老爷进去了，还求那个？”二人闻言，抬头不看见老爷，忍气站起，腿带棒伤，身形晃乱。旁边上来四个人，用手挽架下堂。"
				+ "且说施公退堂，书房坐下，心中想：“昨日梦得奇怪：黄雀、小猪，我即以九黄、七猪为凶人之名，出票差人。无凭无据，真难察访。不得巳，两次当堂责打差役，倘不能获住，去官罢职，甚属小事；怨声载道，而遗臭万年。”前思后想，忽然灵心一动，转又欢悦，如此这般方好。随叫施安说道：“我要私访。”施安听得，不由吓了一跳、口称：“老爷，如要私访，想当初扮做老道，熊宅私访，危及性命，幸亏内里有人护救。而今再去，内外人役，谁不认得？”施公一听，说：“不必多言，你快去就把你穿的破烂衣服取来，待我换上。”施安不敢违拗，只得答应。出书房到自己屋内，将破烂衣服搬出，送至老爷房内。"
				+ "且说施公将衣换上，拿几百钱，带在身上，以为盘费之用。"
				+ "施公自到任后，没有家眷，只跟来施安等二人，衙内并无多人，还有两名厨子。施公吩咐晚饭用毕，趁着天黑，好出衙门，以便办事。吩咐施安小心看守，施安答应，随将主人悄悄送出，又对看门皂隶说道：“老爷今日出去私访，不许高声，快快开门。”施公步出，一溜一点而去。"
				+ "施公正走中间，只见茶坊之内，一些人在灯下坐着吃茶。";
		//字段拆分处理 20171011
	  /*  long splitedSize = 10 * 10 ;  //100Byte 
		splitedSize = splitedSize / 2;      //汉字除以2
	    String splitArray[] = content.split("(?<=\\G.{" +  splitedSize +  "})");
	 	int iArrLen = splitArray.length;
	    System.out.println("Len = " + iArrLen);
	 	for (int i = 0; i < iArrLen; ++i){
	 		System.out.println("i = " + i + splitArray[i]);
	 	}
	 	*/
		
		int i = 0;
		for(final String token : Splitter.fixedLength(100).split(content)){
		    System.out.println("i = " + i + token);
		    ++i;
		}
	}
	
	/**
	 ** @brief:获取Jest客户端
	 ** @param:空
	 ** @return:空
	 ** @author:ycy
	 ** @date:2017-06-22
	 */
	private static JestClient getJestClient() {
		try {
			PropertyConfigurator.configure("KeepworkSearch/log4j.properties");
			initConfig("KeepworkSearch/conf/jest.properties");
		//	PropertyConfigurator.configure("log4j.properties");
		//	initConfig("conf/jest.properties");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://" + ESConfig.es_ipaddr + ":" + ESConfig.es_port)
		                        .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
		                        .multiThreaded(true)
		                        .readTimeout(60000)
		                        .build());
		 JestClient client = factory.getObject();
		 return client;
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
				ESConfig.es_imageindex = propertie.getProperty("es_imageindex");
				ESConfig.es_imagetype = propertie.getProperty("es_imagetype");
				ESConfig.es_kwindex = propertie.getProperty("es_kwindex");
				ESConfig.es_kwtype = propertie.getProperty("es_kwtype");
				ESConfig.es_baikeindex = propertie.getProperty("es_baikeindex");
				ESConfig.es_baiketype = propertie.getProperty("es_baiketype");
				ESConfig.es_imageextindex = propertie.getProperty("es_imageextindex");
				ESConfig.es_imageexttype = propertie.getProperty("es_imageexttype");
				logger.info("Jest config load ok!");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * 将删除所有的索引
	 * @throws Exception
	 */
	private static void deleteIndex() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		DeleteIndex deleteIndex = new DeleteIndex.Builder("article").build();
		JestResult result = jestClient.execute(deleteIndex);
		System.out.println(result.getJsonString());
	}



	/**
	 * 清缓存
	 * @throws Exception
	 */
	private static void clearCache() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		ClearCache closeIndex = new ClearCache.Builder().build();
		JestResult result = jestClient.execute(closeIndex);
		System.out.println(result.getJsonString());
	}

	/**
	 * 关闭索引
	 * @throws Exception
	 */
	private static void closeIndex() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		CloseIndex closeIndex = new CloseIndex.Builder("article").build(); 
		JestResult result = jestClient.execute(closeIndex);
		System.out.println(result.getJsonString());
	}

	/**
	 * 优化索引
	 * @throws Exception
	 */
	private static void optimize() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		Optimize optimize = new Optimize.Builder().build(); 
		JestResult result = jestClient.execute(optimize);
		System.out.println(result.getJsonString());
	}

	/**
	 * 刷新索引
	 * @throws Exception
	 */
	private static void flush() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		Flush flush = new Flush.Builder().build(); 
		JestResult result = jestClient.execute(flush);
		System.out.println(result.getJsonString());
	}

	/**
	 * 判断索引目录是否存在
	 * @throws Exception
	 */
	private static void indicesExists() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		IndicesExists indicesExists = new IndicesExists.Builder("article").build();
		JestResult result = jestClient.execute(indicesExists);
		System.out.println(result.getJsonString());
	}

	/**
	 * 查看节点信息
	 * @throws Exception
	 */
	private static void nodesInfo() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		NodesInfo nodesInfo = new NodesInfo.Builder().build();
		JestResult result = jestClient.execute(nodesInfo);
		System.out.println(result.getJsonString());
	}


	/**
	 * 查看集群健康信息
	 * @throws Exception
	 */
	private static void health() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		Health health = new Health.Builder().build();
		JestResult result = jestClient.execute(health);
		System.out.println(result.getJsonString());
	}

	/**
	 * 节点状态
	 * @throws Exception
	 */
	private static void nodesStats() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		NodesStats nodesStats = new NodesStats.Builder().build();
		JestResult result = jestClient.execute(nodesStats);
		System.out.println(result.getJsonString());
	}

	/**
	 * 更新Document
	 * @param index
	 * @param type
	 * @param id
	 * @throws Exception
	 */
	private static void updateDocument(String index, String type, String id) throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		Article article = new Article();
		article.setId(Integer.parseInt(id));
		article.setTitle("中国3颗卫星拍到阅兵现场高清照");
		article.setContent(
				"据中国资源卫星应用中心报道，9月3日，纪念中国人民抗日战争暨世界反法西斯战争胜利70周年大阅兵在天安门广场举行。资源卫星中心针对此次盛事，综合调度在轨卫星，9月1日至3日连续三天持续观测首都北京天安门附近区域，共计安排5次高分辨率卫星成像。在阅兵当日，高分二号卫星、资源三号卫星及实践九号卫星实现三星联合、密集观测，捕捉到了阅兵现场精彩瞬间。为了保证卫星准确拍摄天安门及周边区域，提高数据处理效率，及时制作合格的光学产品，资源卫星中心运行服务人员从卫星观测计划制定、复核、优化到系统运行保障、光学产品图像制作，提前进行了周密部署，并拟定了应急预案，为圆满完成既定任务奠定了基础。");
		article.setPubdate(new Date());
		article.setAuthor("匿名");
		article.setSource("新华网");
		article.setUrl("http://news.163.com/15/0909/07/B32AGCDT00014JB5.html");
		String script = "{" + "    \"doc\" : {" + "        \"title\" : \"" + article.getTitle() + "\","
				+ "        \"content\" : \"" + article.getContent() + "\"," + "        \"author\" : \""
				+ article.getAuthor() + "\"," + "        \"source\" : \"" + article.getSource() + "\","
				+ "        \"url\" : \"" + article.getUrl() + "\"," + "        \"pubdate\" : \""
				+ new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(article.getPubdate()) + "\"" + "    }" + "}";
		Update update = new Update.Builder(script).index(index).type(type).id(id).build();
		JestResult result = jestClient.execute(update);
		System.out.println(result.getJsonString());
	}

	/**
	 * 删除Document
	 * @param index
	 * @param type
	 * @param id
	 * @throws Exception
	 */
	private static void deleteDocument(String index,String type,String id) throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		Delete delete = new Delete.Builder(id).index(index).type(type).build();
		JestResult result = jestClient.execute(delete);
		System.out.println(result.getJsonString());
	}

	/**
	 * 获取Document
	 * @param index
	 * @param type
	 * @param id
	 * @throws Exception
	 */
	private static void getDocument(String index,String type,String id) throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		Get get = new Get.Builder(index, id).type(type).build();
		JestResult result = jestClient.execute(get);
		Article article = result.getSourceAsObject(Article.class);
		System.out.println(article.getTitle()+","+article.getContent());
	}

	/**
	 * Suggestion
	 * @throws Exception
	 */
	private static void suggest() throws Exception{
		String suggestionName = "my-suggestion";
		JestClient jestClient = JestExample.getJestClient();
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
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
        List<KwInfo> kwList = new ArrayList<KwInfo>();
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", queryString));

		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		//highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);//高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = iPageNo;
		int pageSize = 10;
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
        
        if (pageNumber * pageSize >= MAXSEARCHCNT){
	    	System.out.println("Result window is too large, "
	    			+ "from + size must be less than or equal to: [500000]");
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
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
        List<KwInfo> kwList = new ArrayList<KwInfo>();
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", queryString));

		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		//highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);//高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = iPageNo;
		int pageSize = 10;
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
        
        if (pageNumber  * pageSize >= MAXSEARCHCNT){
	    	logger.error("Result window is too large, "
	    			+ "from + size must be less than or equal to: [500000]");
	    	return null;
	    }
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
		                                //multiple index or types can be added.
		                                .addIndex(ESConfig.es_imageindex)
		                                .addType(ESConfig.es_imagetype)
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
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		System.out.println("keyword = " + queryString);
		
		List<KwInfo> kwList = new ArrayList<KwInfo>();
        
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		
		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		//highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);//高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;
		
		if (pageNumber * pageSize >= MAXSEARCHCNT){
	    	System.out.println("Result window is too large, "
	    			+ "from + size must be less than or equal to: [500000]");
	    	return null;
	    }
		
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
			//	.addIndex("es_msearch_wx")
				.build();
		   
		SearchResult result = jestClient.execute(search);
		//logger.info(result.getJsonString());  
		
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
	 **@brief:image索引的全文检索
	 **@param:待检索内容
	 **@return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 **@author:ycy
	 **@date:2017-06-23
	 */
	public static String fullTextByImageOfPageQuery(String queryString, int pageNo) throws Exception {
		if(queryString == null || queryString.length() == 0 || queryString.equals("") 
				|| pageNo <= 0 || pageNo >= MAXSEARCHCNT){
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[fulltext]keyword = " + queryString);
     
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		
		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("content");//高亮content
		//highlightBuilder.preTags("<em>").postTags("</em>");//高亮标签
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);//高亮内容长度
		highlightBuilder.numOfFragments(5); //返回的最大片段数
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;
		
		if (pageNumber * pageSize >= MAXSEARCHCNT){
	    	System.out.println("Result window is too large, "
	    			+ "from + size must be less than or equal to: [500000]");
	    	return null;
	    }
		
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
		
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex(ESConfig.es_imageindex)
				.build();
		   
		SearchResult result = jestClient.execute(search);
		if (result == null) {
			logger.error("result = null!");
			return null;
		}
	//	logger.info("[fulltext]: " + result.getJsonString());  
		
		//解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
	    long took = jsonObject.get("took").getAsLong();
	    float tooksecond = (float)(took)/1000f;
	    int total = result.getTotal();
	    logger.info("[fulltext] 找到 " + total +" 条结果 （用时 " + tooksecond + " 秒）");
	    
	    //获取分页数
	    double dMaxPages = Math.ceil((double)total/(double)pageSize);
	    int iMaxPages = (int)dMaxPages;
	    logger.info("共 " + iMaxPages +" 页");
	    if (iMaxPages < pageNo){
	    	logger.error( pageNo + "页 不存在! 最大页为: " + iMaxPages);
	    }		
	    return result.getJsonString();
	}
	
	/**
	 **@brief:返回镜像title文件的列表信息。
	 **@param:待检索内容
	 **@return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 **@author:ycy
	 **@date:2017-06-23
	 */
	public static String imageListTitleSearch(String queryString, int pageNo, int highlight) throws Exception {
		String imageJsonRst = null;
		if(queryString == null || queryString.length() == 0 || queryString.equals("") 
				|| pageNo <= 0 || pageNo >= MAXSEARCHCNT){
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[title]keyword = " + queryString);
     
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should(QueryBuilders.termQuery("title.keyword", queryString)); 
	    boolQueryBuilder.should(QueryBuilders.termQuery("title", queryString)); 
	    boolQueryBuilder.should(QueryBuilders.wildcardQuery("title.keyword", "*"+queryString+"*")); 
		searchSourceBuilder.query(boolQueryBuilder);
	    
		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("title.keyword");//高亮title
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);//高亮内容长度
		highlightBuilder.numOfFragments(5); //返回的最大片段数
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;
		if (pageNumber * pageSize >= MAXSEARCHCNT){	
			return errProcess(WINDOWS_ERR_MSG);
	    }
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
        //new add by ycy 限定字符输出
        searchSourceBuilder.fetchSource(new String[] {
        	      "title",
        	      "content"
        	    }, null);
        //System.out.println(searchSourceBuilder.toString());
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex(ESConfig.es_imageindex)
				.build();
		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}
		
		//logger.info("[imageListTitleSearch]: " + result.getJsonString());  
		//解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
	    long took = jsonObject.get("took").getAsLong();
	    float tooksecond = (float)(took)/1000f;
	    int total = result.getTotal();
	    logger.info("[imageListTitleSearch] 找到 " + total +" 条结果 （用时 " + tooksecond + " 秒）");
	    
	    //获取分页数
	    double dMaxPages = Math.ceil((double)total/(double)pageSize);
	    int iMaxPages = (int)dMaxPages;
	    logger.info("共 " + iMaxPages +" 页");
	    if (iMaxPages < pageNo){
	    	logger.info( pageNo + "页 不存在! 最大页为: " + iMaxPages);
	    }		
	    
	    LinkedList<BaikeInfo> imageList = new LinkedList<BaikeInfo>();
	        
	    //解析字段、拼接Json
	    JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
	    JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
            BaikeInfo curImageInfo = new BaikeInfo();
            
            //_id字段
            curImageInfo.setIid(jsonHitsObject.get("_id").getAsString());         
            // 获取返回字段
            JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();        
            // 封装Article对象
            curImageInfo.setTitle(sourceObject.get("title").getAsString());
            String totalContent = sourceObject.get("content").getAsString();
            if (totalContent.length() > 50){
            	totalContent = totalContent.substring(0, MAX_INFO_LEN);
            }
            curImageInfo.setContent(totalContent);
            
            if (highlight == 1) {
            	  // 获取高亮字段
            	if (jsonHitsObject.get("highlight") != null) {
	                JsonObject highlightObject = jsonHitsObject.get("highlight").getAsJsonObject();
	             //   String content = "";
	                String title = "";
	     /*           if (highlightObject.get("content") != null) {
	                    content = highlightObject.get("content").getAsJsonArray().get(0).getAsString();
	                }*/
	                if (highlightObject.get("title") != null) {
	                    title = highlightObject.get("title").getAsJsonArray().get(0).getAsString();
	                }    
	                else if (highlightObject.get("title.keyword") != null) {
	                    title = highlightObject.get("title.keyword").getAsJsonArray().get(0).getAsString();
	                }
	                
	               /* if (!content.equalsIgnoreCase("")) {
	                	if (content.length() > 50)
	                	{
	                		String spanWord = "</span>";
							int spanLen = spanWord.length();
							int spanStartPos = content.indexOf(spanWord);
							//System.out.println("spanStartPos = " + spanStartPos + "spanLen =" + spanLen);
							int stratPos = 0;
							if (spanStartPos + spanLen > 50) {
								stratPos = spanStartPos + spanLen - 50;
								content = content.substring(stratPos, spanStartPos + spanLen);
							} else {
								//此处为了高亮，不得已，没有更好的方案。20170830
								content = content.substring(0, spanStartPos + spanLen);
							}
							//System.out.println("content = " + content);
	                	}
	                	curImageInfo.setContent(content);
	                }*/
	                if (!title.equalsIgnoreCase("")) {
	                	curImageInfo.setTitle(title);
	                }
            	}//end (jsonHitsObject.get("highlight")
            }//end if-highlight
            imageList.add(curImageInfo);
        }
        
       // Map<String, Object> imageData = new HashMap<String, Object>();
        
        Map<String, Object> imageData = new HashMap<String, Object>();
        imageData.put("num", total);
        imageData.put("list", imageList);
	    
    	Map<String, Object> imageRst = new HashMap<String, Object>();
    	imageRst.put("code", 200);
    	imageRst.put("msg", "success");
    	imageRst.put("total", total);
    	imageRst.put("took", tooksecond);
    	imageRst.put("data", imageData);
    	imageJsonRst = JSON.toJSONString(imageRst);
		//logger.info(imageJsonRst);
    	
		return imageJsonRst;
	}
	
	/**
	 **@brief:返回baiketitle文件的列表信息。
	 **@param:待检索内容
	 **@return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 **@author:ycy
	 **@date:2017-06-23
	 */
	public static String baikeListTitleSearch(String queryString, int pageNo, int highlight) throws Exception {
		String imageJsonRst = null;
		if(queryString == null || queryString.length() == 0 || queryString.equals("") 
				|| pageNo <= 0 || pageNo >= MAXSEARCHCNT){
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[title]keyword = " + queryString);
     
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.should(QueryBuilders.fuzzyQuery("title", queryString)); 
		boolQueryBuilder.should(QueryBuilders.termQuery("title.keyword", queryString)); 
	    boolQueryBuilder.should(QueryBuilders.termQuery("title", queryString)); 
	    boolQueryBuilder.should(QueryBuilders.wildcardQuery("title.keyword", "*"+queryString+"*")); 
		searchSourceBuilder.query(boolQueryBuilder);
	    
		//新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");//高亮title
		highlightBuilder.field("title.keyword");//高亮title
		highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);//高亮内容长度
		highlightBuilder.numOfFragments(5); //返回的最大片段数
		searchSourceBuilder.highlighter(highlightBuilder);
		
	    // 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;
		if (pageNumber * pageSize >= MAXSEARCHCNT){	
			return errProcess(WINDOWS_ERR_MSG);
	    }
        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
        searchSourceBuilder.size(pageSize);//设置页大小
        //new add by ycy 限定字符输出
        searchSourceBuilder.fetchSource(new String[] {
        	      "title",
        	     "content",
        	     "page",
        	     "divide_flag",
        	     "article_id",
        	     "totalpage",
        	     "path"
        	    }, null);
        //System.out.println(searchSourceBuilder.toString());
		Search search = new Search.Builder(searchSourceBuilder.toString())
				.addIndex(ESConfig.es_baikeindex)
				.build();
		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}
		
		//logger.info("[imageListTitleSearch]: " + result.getJsonString());  
		//解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
	    long took = jsonObject.get("took").getAsLong();
	    float tooksecond = (float)(took)/1000f;
	    int total = result.getTotal();
	    logger.info("[baikeListTitleSearch] 找到 " + total +" 条结果 （用时 " + tooksecond + " 秒）");
	    
	    //获取分页数
	    double dMaxPages = Math.ceil((double)total/(double)pageSize);
	    int iMaxPages = (int)dMaxPages;
	    logger.info("共 " + iMaxPages +" 页");
	    if (iMaxPages < pageNo){
	    	logger.error( pageNo + "页 不存在! 最大页为: " + iMaxPages);
	    }		
	    
	    LinkedList<BaikeInfo> imageList = new LinkedList<BaikeInfo>();
	        
	    //解析字段、拼接Json
	    JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
	    JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
            BaikeInfo curBaikeInfo = new BaikeInfo();
            
            //_id字段
            curBaikeInfo.setIid(jsonHitsObject.get("_id").getAsString());         
            // 获取返回字段
            JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();        
            // 封装Article对象
            curBaikeInfo.setTitle(sourceObject.get("title").getAsString());
            String totalContent = sourceObject.get("content").getAsString();
            if (totalContent.length() > 50){
            	totalContent = totalContent.substring(0, MAX_INFO_LEN);
            }
            curBaikeInfo.setContent(totalContent);
            //new add by ycy 20171117
            if (sourceObject.get("path") != null){
	            String strCurPath = sourceObject.get("path").getAsString();
	            if (strCurPath != null){
	            	strCurPath = strCurPath.substring(0,strCurPath.length()-3);
	            	strCurPath = "http://keepwork.com/keepwork/baike" + strCurPath;
	            	curBaikeInfo.setKeepwork_url(strCurPath);
	            }
            }
            
            //new add by ycy 20171017
            if (null != sourceObject.get("page")){
	            int ipageNo = sourceObject.get("page").getAsInt();
	            if (ipageNo != 0){
	                curBaikeInfo.setPage(ipageNo);   
	            }
            }
            if (null != sourceObject.get("divide_flag")){
	            int idivide_flag = sourceObject.get("divide_flag").getAsInt();
	            if (idivide_flag != 0){
	            	curBaikeInfo.setDivide_flag(idivide_flag);   
	            }
            }
            
            if (null != sourceObject.get("article_id")){
	            String article_id = sourceObject.get("article_id").getAsString();
	            if (article_id != null){
	            	 curBaikeInfo.setArticle_id(article_id);
	            }
            }
            if (null != sourceObject.get("totalpage")){
	            int pag_num = sourceObject.get("totalpage").getAsInt();
	            if (pag_num != 0){
	            	 curBaikeInfo.setTotalpage(pag_num);   
	            }
            }
            if (highlight == 1) {
            	  // 获取高亮字段
            	if (jsonHitsObject.get("highlight") != null) {
	                JsonObject highlightObject = jsonHitsObject.get("highlight").getAsJsonObject();
	             //   String content = "";
	                String title = "";
	     /*           if (highlightObject.get("content") != null) {
	                    content = highlightObject.get("content").getAsJsonArray().get(0).getAsString();
	                }*/
	                if (highlightObject.get("title") != null) {
	                    title = highlightObject.get("title").getAsJsonArray().get(0).getAsString();
	                }    
	                else if (highlightObject.get("title.keyword") != null) {
	                    title = highlightObject.get("title.keyword").getAsJsonArray().get(0).getAsString();
	                }
	                
	               /* if (!content.equalsIgnoreCase("")) {
	                	if (content.length() > 50)
	                	{
	                		String spanWord = "</span>";
							int spanLen = spanWord.length();
							int spanStartPos = content.indexOf(spanWord);
							//System.out.println("spanStartPos = " + spanStartPos + "spanLen =" + spanLen);
							int stratPos = 0;
							if (spanStartPos + spanLen > 50) {
								stratPos = spanStartPos + spanLen - 50;
								content = content.substring(stratPos, spanStartPos + spanLen);
							} else {
								//此处为了高亮，不得已，没有更好的方案。20170830
								content = content.substring(0, spanStartPos + spanLen);
							}
							//System.out.println("content = " + content);
	                	}
	                	curImageInfo.setContent(content);
	                }*/
	                if (!title.equalsIgnoreCase("")) {
	                	curBaikeInfo.setTitle(title);
	                }
            	}//end (jsonHitsObject.get("highlight")
            }//end if-highlight
            imageList.add(curBaikeInfo);
        }
        
       // Map<String, Object> imageData = new HashMap<String, Object>();
        
        Map<String, Object> imageData = new HashMap<String, Object>();
        imageData.put("num", total);
        imageData.put("list", imageList);
	    
    	Map<String, Object> imageRst = new HashMap<String, Object>();
    	imageRst.put("code", 200);
    	imageRst.put("msg", "success");
    	imageRst.put("total", total);
    	imageRst.put("took", tooksecond);
    	imageRst.put("data", imageData);
    	imageJsonRst = JSON.toJSONString(imageRst);
		//logger.info(imageJsonRst);
    	
		return imageJsonRst;
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
		if (queryString == null || queryString.length() == 0 || queryString.equals("") ) {
			logger.error("[kwdelbyquery]Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}

		JestClient jestClient = JestExample.getJestClient();
		DeleteByQuery deleteByQuery = new DeleteByQuery.Builder(queryString)
	                .addIndex(ESConfig.es_kwindex)
	                .addType(ESConfig.es_kwtype)
	                .build();
	    JestResult result = jestClient.execute(deleteByQuery);
	  //  logger.info("result = " + result.getJsonString());
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}
		
		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		int deleteLines = jsonObject.get("deleted").getAsInt();
		String strMsg = "";
		if (deleteLines >= 1){
			strMsg = "deleted " + deleteLines + " documents!";
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
	
	/**
	 ** @brief:用户自定义检索。
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String kwcustom_search(String queryString) throws Exception {
		String kwJsonRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("") ) {
			logger.error("[kwcustom_search]Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}

		// logger.info("[kwcustom_search]keyword = " + queryString);
		JestClient jestClient = JestExample.getJestClient();
		Search search = new Search.Builder(queryString)
				// multiple index or types can be added.
				.addIndex(ESConfig.es_kwindex).build();

		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}
		
		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[kwcustom_search] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		LinkedList<KwInfo> kwList = new LinkedList<KwInfo>();
		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
			// 获取返回字段
			int show_all = 1;
			KwInfo curKwInfo = highlightProc.getProcessedKwInfo(jsonHitsObject, 1, show_all);
			kwList.add(curKwInfo);
		}

		Map<String, Object> imageData = new HashMap<String, Object>();
		imageData.put("num", total);
		imageData.put("list", kwList);

		Map<String, Object> kwRst = new HashMap<String, Object>();
		kwRst.put("code", 200);
		kwRst.put("msg", "success");
		kwRst.put("total", total);
		kwRst.put("took", tooksecond);
		kwRst.put("data", imageData);
		kwJsonRst = JSON.toJSONString(kwRst);
		return kwJsonRst;
	}

	/**
	 ** @brief:返回镜像title文件的列表信息。
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String kwaccurate_search(String queryKey, String queryString, int fuzzymatch, int pageNo,
			int highlight, int size) throws Exception {
		String kwJsontRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("") || pageNo <= 0
				|| pageNo >= MAXSEARCHCNT || size <= 0) {
			logger.error("[kwaccurate_search]Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[kwaccurate_search]keyword = " + queryString);

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		if (fuzzymatch == 0) {
			logger.info("[kwaccurate_search] termQuery !");
			// 时间字段单独处理...
			if (queryKey.contains("time")) {
				searchSourceBuilder.query(QueryBuilders.termQuery(queryKey, queryString));
			} else {
				searchSourceBuilder.query(QueryBuilders.termQuery(queryKey + ".keyword", queryString));
			}

		} else if (fuzzymatch == 1) {
			logger.info("[kwaccurate_search] wildcardQuery !"); // wildcardQuery
			searchSourceBuilder.query(QueryBuilders.wildcardQuery(queryKey + ".keyword", queryString));
		} else if (fuzzymatch == 2) {
			logger.info("[kwaccurate_search] matchQuery !"); // matchQuery
			searchSourceBuilder.query(QueryBuilders.matchQuery(queryKey, queryString));
		}/* else if (fuzzymatch == 3) {
			logger.info("[kwaccurate_search] prefixQuery !"); // matchQuery
			searchSourceBuilder.query(QueryBuilders.prefixQuery(queryKey, queryString));
		}*/ else {
			logger.error("[kwaccurate_search]fuzzymatch Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		if (queryKey.equals("tags") || queryKey.equals("content")) {
			// 新增高亮处理
			highlightBuilder.field("tags.keyword");// 高亮title
			highlightBuilder.field("tags");// 高亮title
			highlightBuilder.field("content");// 高亮content
			highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮红色标签
			highlightBuilder.postTags("</span>");
			highlightBuilder.fragmentSize(100);// 高亮内容长度
			highlightBuilder.numOfFragments(5); // 返回的最大片段数
			searchSourceBuilder.highlighter(highlightBuilder);
		}else if(queryKey.equals("extra_search")){
			// 新增高亮处理
			//System.out.println( "extra_search!!");
			highlightBuilder.field("extra_search");// 高亮extra_search
			highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮红色标签
			highlightBuilder.postTags("</span>");
			highlightBuilder.fragmentSize(100);// 高亮内容长度
			highlightBuilder.numOfFragments(5); // 返回的最大片段数
			searchSourceBuilder.highlighter(highlightBuilder);
		}
		// 设置分页
		int pageNumber = pageNo;
		int pageSize = size;

		if (pageNumber * pageSize >= MAXSEARCHCNT) {
			return errProcess(WINDOWS_ERR_MSG);
		}

		searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小
		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESConfig.es_kwindex).build();

		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}else{
			//logger.info("[kwaccurate_search] result.isSucceeded");
		}
		//logger.info("result = " + result.getJsonString());

		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[kwaccurate_search] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		// 获取分页数
		double dMaxPages = Math.ceil((double) total / (double) pageSize);
		int iMaxPages = (int) dMaxPages;
		logger.info("共 " + iMaxPages + " 页");
		if (iMaxPages < pageNo) {
			logger.error(pageNo + "页 不存在! 最大页为: " + iMaxPages);
		}

		LinkedList<KwInfo> kwList = new LinkedList<KwInfo>();

		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
		    int show_all = 1;
			KwInfo curKwInfo = highlightProc.getProcessedKwInfo(jsonHitsObject, highlight, show_all);
			kwList.add(curKwInfo);
		}

		Map<String, Object> kwData = new HashMap<String, Object>();
		kwData.put("num", total);
		kwData.put("list", kwList);

		Map<String, Object> kwRst = new HashMap<String, Object>();
		kwRst.put("code", 200);
		kwRst.put("msg", "success");
		kwRst.put("total", total);
		kwRst.put("took", tooksecond);
		kwRst.put("data", kwData);
		kwJsontRst = JSON.toJSONString(kwRst);
		return kwJsontRst;
	}

	/**
	 ** @brief:返回keepwork url推荐列表信息
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String kwurllist_search(String queryString, int pageNo, int size) throws Exception {
		String kwJsonRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("") || pageNo <= 0
				|| pageNo >= MAXSEARCHCNT || size <= 0) {
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[kwurllist_search]keyword = " + queryString);

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.fuzzyQuery("url", queryString));

		// 设置分页
		int pageNumber = pageNo;
		int pageSize = size;

		if (pageNumber * pageSize >= MAXSEARCHCNT) {
			return errProcess(WINDOWS_ERR_MSG);
		}

		searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小

		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESConfig.es_kwindex).build();

		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}

		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[kwurllist_search] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		// 获取分页数
		double dMaxPages = Math.ceil((double) total / (double) pageSize);
		int iMaxPages = (int) dMaxPages;
		logger.info("共 " + iMaxPages + " 页");
		if (iMaxPages < pageNo) {
			logger.error(pageNo + "页 不存在! 最大页为: " + iMaxPages);
		}

		LinkedList<KwInfo> kwList = new LinkedList<KwInfo>();

		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
			KwInfo curKwInfo = new KwInfo();

			// 获取返回字段
			JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();
			String kwUrl = sourceObject.get("url").getAsString();
			curKwInfo.setUrl(kwUrl);

			kwList.add(curKwInfo);
		}

		Map<String, Object> kwData = new HashMap<String, Object>();
		kwData.put("num", total);
		kwData.put("list", kwList);

		Map<String, Object> kwRst = new HashMap<String, Object>();
		kwRst.put("code", 200);
		kwRst.put("msg", "success");
		kwRst.put("total", total);
		kwRst.put("took", tooksecond);
		kwRst.put("data", kwData);

		kwJsonRst = JSON.toJSONString(kwRst);
		return kwJsonRst;
	}

	/**
	 ** @brief:返回keepwork文件的全文检索结果信息。
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String kwfulltext_search(String queryString, int pageNo, int highlight, int size)
			throws Exception {
		String kwJsonRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("") || pageNo <= 0
				|| pageNo >= MAXSEARCHCNT || size <= 0) {
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[kwFulltext]keyword = " + queryString);

		 JestClient jestClient = JestExample.getJestClient();
		 //new add 20170926	
		 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		 BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		 boolQueryBuilder.must(QueryBuilders.wildcardQuery("extra_type.keyword", "*pageinfo*")); 
		 boolQueryBuilder.must(QueryBuilders.queryStringQuery(queryString));		
		 // 新增高亮处理
		 HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
		 highlightBuilder.field("tags");// 高亮title
		 highlightBuilder.field("extra_search");// 高亮content
		 highlightBuilder.field("extra_data");// 高亮content
		 highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮红色标签
		 highlightBuilder.postTags("</span>");
		 highlightBuilder.fragmentSize(100);// 高亮内容长度
		 highlightBuilder.numOfFragments(5); // 返回的最大片段数
		 searchSourceBuilder.highlighter(highlightBuilder);
		
		//searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		// 设置分页
		int pageNumber = pageNo;
		int pageSize = size;
		if (pageNumber * pageSize >= MAXSEARCHCNT) {
			return errProcess(WINDOWS_ERR_MSG);
		}

		searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小
		searchSourceBuilder.query(boolQueryBuilder);
		Search search = new Search.Builder(searchSourceBuilder.toString())
								.addIndex(ESConfig.es_kwindex).build();
		SearchResult result = jestClient.execute(search);
		//logger.info("result: " + result.getJsonString());
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}

		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[kwFulltext] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		// 获取分页数
		double dMaxPages = Math.ceil((double) total / (double) pageSize);
		int iMaxPages = (int) dMaxPages;
		logger.info("共 " + iMaxPages + " 页");
		if (iMaxPages < pageNo) {
			logger.error(pageNo + "页 不存在! 最大页为: " + iMaxPages);
		}
		
		LinkedList<KwInfo> kwList = new LinkedList<KwInfo>();
		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
			
			// 获取返回字段
			int show_all = 0;
			KwInfo curKwInfo = highlightProc.getProcessedKwInfo(jsonHitsObject, highlight, show_all);
			kwList.add(curKwInfo);
		}

		Map<String, Object> kwData = new HashMap<String, Object>();
		kwData.put("num", total);
		kwData.put("list", kwList);
		Map<String, Object> kwRst = new HashMap<String, Object>();
		kwRst.put("code", 200);
		kwRst.put("msg", "success");
		kwRst.put("total", total);
		kwRst.put("took", tooksecond);
		kwRst.put("data", kwData);
		kwJsonRst = JSON.toJSONString(kwRst);
		return kwJsonRst;
	}

	/**
	 ** @brief:返回镜像文件的列表信息。
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String imageListFullTextSearch(String queryString, int pageNo, int highlight) throws Exception {
		String imageJsonRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("") || pageNo <= 0
				|| pageNo >= MAXSEARCHCNT) {
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[imagefulltext]keyword = " + queryString);

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		//searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		searchSourceBuilder.query(QueryBuilders.matchQuery("content", queryString));

		// 新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(false);
		//highlightBuilder.field("title");// 高亮title
		highlightBuilder.field("content");// 高亮content
		highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);// 高亮内容长度
		highlightBuilder.numOfFragments(5); // 返回的最大片段数
		searchSourceBuilder.highlighter(highlightBuilder);
		// 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;
		if (pageNumber * pageSize >= MAXSEARCHCNT) {
			return errProcess(WINDOWS_ERR_MSG);
		}

		searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小
		 //new add by ycy 限定字符输出
        searchSourceBuilder.fetchSource(new String[] {
        	      "title"
        	    }, null);
		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESConfig.es_baikeindex).build();
		SearchResult result = jestClient.execute(search);
	    //logger.info("[fulltext]: " + searchSourceBuilder.toString());
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}

		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[fulltext] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		// 获取分页数
		double dMaxPages = Math.ceil((double) total / (double) pageSize);
		int iMaxPages = (int) dMaxPages;
		logger.info("共 " + iMaxPages + " 页");
		if (iMaxPages < pageNo) {
			logger.error(pageNo + "页 不存在! 最大页为: " + iMaxPages);
		}

		LinkedList<BaikeInfo> imageList = new LinkedList<BaikeInfo>();
		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
			BaikeInfo curImageInfo = new BaikeInfo();

			// _id字段
			curImageInfo.setIid(jsonHitsObject.get("_id").getAsString());
			// 获取返回字段
			JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();
			// 封装Article对象
			curImageInfo.setTitle(sourceObject.get("title").getAsString());
			if (sourceObject.get("content") != null) {
				String totalContent = sourceObject.get("content").getAsString();
				if (totalContent.length() > 50) {
					totalContent = totalContent.substring(0, MAX_INFO_LEN);
				}
				curImageInfo.setContent(totalContent);
			}
			if (highlight == 1) {
				// 获取高亮字段
				if (jsonHitsObject.get("highlight") != null) {
					JsonObject highlightObject = jsonHitsObject.get("highlight").getAsJsonObject();

					String content = "";
					String title = "";
					if (highlightObject.get("content") != null) {
						content = highlightObject.get("content").getAsJsonArray().get(0).getAsString();
					}
					if (highlightObject.get("title") != null) {
						title = highlightObject.get("title").getAsJsonArray().get(0).getAsString();
					}

					if (!content.equalsIgnoreCase("")) {
						/*if (content.length() > 50) {
							String spanWord = "</span>";
							int spanLen = spanWord.length();
							int spanStartPos = content.indexOf(spanWord);
							//System.out.println("spanStartPos = " + spanStartPos + "spanLen =" + spanLen);
							int stratPos = 0;
							if (spanStartPos + spanLen > 50) {
								stratPos = spanStartPos + spanLen - 50;
								content = content.substring(stratPos, spanStartPos + spanLen);
							} else {
								content = content.substring(0, spanStartPos + spanLen);
							}
							//System.out.println("content = " + content);
						}*/
						curImageInfo.setContent(content);
					}
					if (!title.equalsIgnoreCase("")) {
						curImageInfo.setTitle(title);
					}
				}
			} // end highlight

			imageList.add(curImageInfo);
		}

		// Map<String, Object> imageData = new HashMap<String, Object>();

		Map<String, Object> imageData = new HashMap<String, Object>();
		imageData.put("num", total);
		imageData.put("list", imageList);

		Map<String, Object> imageRst = new HashMap<String, Object>();

		imageRst.put("code", 200);
		imageRst.put("msg", "success");
		imageRst.put("total", total);
		imageRst.put("took", tooksecond);
		imageRst.put("data", imageData);

		imageJsonRst = JSON.toJSONString(imageRst);
		// logger.info(imageJsonRst);

		return imageJsonRst;
	}

	/**
	 ** @brief:返回url对应的content信息（通过ID检索）
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String kwDetailByUrlSearch(String queryString) throws Exception {
		String kwJsonRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("")) {
			logger.error("[kwDetailByUrlSearch] Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[kwDetailByUrlSearch]keyword = " + queryString);

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("url", queryString));

		// 设置分页
		int pageSize = 10;
		searchSourceBuilder.from(0);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小

		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESConfig.es_kwindex).build();

		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}

		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[kwDetailByUrlSearch] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		logger.info("size = " + jsonArray.size());

		if (0 == jsonArray.size()) {
			return errProcess(EMPTY_ERR_MSG);
		}

		JsonObject jsonHitsObject = jsonArray.get(0).getAsJsonObject();
		KwInfo curKwInfo = new KwInfo();

		// 获取返回字段
		JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();
		// 封装Article对象
		if (sourceObject.get("url") != null) {
			curKwInfo.setUrl(sourceObject.get("url").getAsString());
		}
		if (sourceObject.get("tags") != null) {
			curKwInfo.setTags(sourceObject.get("tags").getAsString());
		}
		if (sourceObject.get("extra_data") != null) {
			String kwextra_data = sourceObject.get("extra_data").getAsString();
			  //获取json内容
	         ExtraDataInfo kwExtraData = (ExtraDataInfo) JSON.parseObject(kwextra_data, ExtraDataInfo.class);  
	         String totalContent = kwExtraData.getContent();
		     curKwInfo.setContent(totalContent);
			//curKwInfo.setContent(sourceObject.get("extra_data").getAsString());
		}

		Map<String, Object> kwRst = new HashMap<String, Object>();
		kwRst.put("code", 200);
		kwRst.put("msg", "success");
		kwRst.put("total", total);
		kwRst.put("took", tooksecond);
		kwRst.put("data", curKwInfo);
		kwJsonRst = JSON.toJSONString(kwRst);
		return kwJsonRst;
	}

	/**
	 ** @brief:返回镜像文件的详情信息（通过ID检索）
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String imageDetailByIdSearch(String queryString, int pageNo) throws Exception {
		String imageJsonRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("") || pageNo <= 0
				|| pageNo >= MAXSEARCHCNT) {
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[imageDetailByIdSearch]keyword = " + queryString);

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("_id", queryString));
		// 新增高亮处理
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");// 高亮title
		highlightBuilder.field("content");// 高亮content
		highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);// 高亮内容长度
		highlightBuilder.numOfFragments(5); // 返回的最大片段数
		searchSourceBuilder.highlighter(highlightBuilder);

		// 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;

		if (pageNumber * pageSize >= MAXSEARCHCNT) {
			return errProcess(WINDOWS_ERR_MSG);
		}
		searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小
		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESConfig.es_imageindex).build();

		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}
		//logger.info("[imageDetailByIdSearch]: " + result.getJsonString());

		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[imageDetailByIdSearch] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		// 获取分页数
		double dMaxPages = Math.ceil((double) total / (double) pageSize);
		int iMaxPages = (int) dMaxPages;
		logger.info("共 " + iMaxPages + " 页");
		if (iMaxPages < pageNo) {
			logger.error(pageNo + "页 不存在! 最大页为: " + iMaxPages);
		}

		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		logger.info("size = " + jsonArray.size());
		if (0 == jsonArray.size()) {
			Map<String, Object> errRst = new HashMap<String, Object>();
			errRst.put("code", 200);
			errRst.put("msg", "success");
			errRst.put("total", total);
			errRst.put("took", tooksecond);
			errRst.put("data", "");
			imageJsonRst = JSON.toJSONString(errRst);
			return imageJsonRst;
		}

		JsonObject jsonHitsObject = jsonArray.get(0).getAsJsonObject();
		BaikeInfo curImageInfo = new BaikeInfo();

		// _id字段
		curImageInfo.setIid(jsonHitsObject.get("_id").getAsString());
		// 获取返回字段
		JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();
		// 封装Article对象
		curImageInfo.setTitle(sourceObject.get("title").getAsString());
		String totalContent = sourceObject.get("content").getAsString();

		curImageInfo.setContent(totalContent);

		Map<String, Object> imageRst = new HashMap<String, Object>();

		imageRst.put("code", 200);
		imageRst.put("msg", "success");
		imageRst.put("total", total);
		imageRst.put("took", tooksecond);
		imageRst.put("data", curImageInfo);

		imageJsonRst = JSON.toJSONString(imageRst);
		
		return imageJsonRst;
	}

	/**
	 ** @brief:返回百科文件的详情信息（通过ID检索）
	 ** @param:待检索内容
	 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String baikeDetailByIdSearch(String queryString, int pageNo) throws Exception {
		String imageJsonRst = null;
		if (queryString == null || queryString.length() == 0 || queryString.equals("") || pageNo <= 0
				|| pageNo >= MAXSEARCHCNT) {
			logger.error("Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		logger.info("[imageDetailByIdSearch]keyword = " + queryString);

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("_id", queryString));
		// 新增高亮处理
		/*HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		highlightBuilder.field("title");// 高亮title
		highlightBuilder.field("content");// 高亮content
		highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮红色标签
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);// 高亮内容长度
		highlightBuilder.numOfFragments(5); // 返回的最大片段数
		searchSourceBuilder.highlighter(highlightBuilder);*/

		// 设置分页
		int pageNumber = pageNo;
		int pageSize = 10;

		if (pageNumber * pageSize >= MAXSEARCHCNT) {
			return errProcess(WINDOWS_ERR_MSG);
		}
		searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小
		 //new add by ycy 限定字符输出
        searchSourceBuilder.fetchSource(new String[] {
        	      "title", 
        	      "content"
        	    }, null);
		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESConfig.es_baikeindex).build();

		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}
		//logger.info("[imageDetailByIdSearch]: " + result.getJsonString());

		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[imageDetailByIdSearch] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

		// 获取分页数
		double dMaxPages = Math.ceil((double) total / (double) pageSize);
		int iMaxPages = (int) dMaxPages;
		logger.info("共 " + iMaxPages + " 页");
		if (iMaxPages < pageNo) {
			logger.error(pageNo + "页 不存在! 最大页为: " + iMaxPages);
		}

		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		logger.info("size = " + jsonArray.size());
		if (0 == jsonArray.size()) {
			Map<String, Object> errRst = new HashMap<String, Object>();
			errRst.put("code", 200);
			errRst.put("msg", "success");
			errRst.put("total", total);
			errRst.put("took", tooksecond);
			errRst.put("data", "");
			imageJsonRst = JSON.toJSONString(errRst);
			return imageJsonRst;
		}

		JsonObject jsonHitsObject = jsonArray.get(0).getAsJsonObject();
		BaikeInfo curImageInfo = new BaikeInfo();

		// _id字段
		curImageInfo.setIid(jsonHitsObject.get("_id").getAsString());
		// 获取返回字段
		JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();
		// 封装Article对象
		curImageInfo.setTitle(sourceObject.get("title").getAsString());
		String totalContent = sourceObject.get("content").getAsString();

		curImageInfo.setContent(totalContent);

		Map<String, Object> imageRst = new HashMap<String, Object>();

		imageRst.put("code", 200);
		imageRst.put("msg", "success");
		imageRst.put("total", total);
		imageRst.put("took", tooksecond);
		imageRst.put("data", curImageInfo);

		imageJsonRst = JSON.toJSONString(imageRst);
		
		return imageJsonRst;
	}
	
	
	/**
	 * 查询全部
	 * 
	 * @throws Exception
	 */
	private static void fullTextQuery(String queryString) throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));

		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("article").addIndex("mkyong")
				.build();
		SearchResult result = jestClient.execute(search);
		//System.out.println(result.getJsonString());
		System.out.println("本次查询共查到：" + result.getTotal() + "篇文章！");
	}

	/**
	 * 手动解析查询结果
	 * 
	 * @param articles
	 * @param result
	 * @throws ParseException
	 */
	private void parseSearchResult(List<Article> articles, SearchResult result) throws ParseException {
	}

	/**
	 * 查询全部
	 * 
	 * @throws Exception
	 */
	private static void searchAll() throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());

		// .setQuery(QueryBuilders.queryStringQuery(queryString));

		Search search = new Search.Builder(searchSourceBuilder.toString())
				// .addIndex("article")
				.build();
		SearchResult result = jestClient.execute(search);
		System.out.println("本次查询共查到：" + result.getTotal() + "篇文章！");
		List<Hit<Article, Void>> hits = result.getHits(Article.class);
		for (Hit<Article, Void> hit : hits) {
			Article source = hit.source;
			System.out.println("标题：" + source.getTitle());
			System.out.println("内容：" + source.getContent());
			System.out.println("url：" + source.getUrl());
			System.out.println("来源：" + source.getSource());
			System.out.println("作者：" + source.getAuthor());
		}
	}

	private static void matchSearch(String queryString) throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery("title", queryString));

		Search search = new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex("article").addType("article").build();

		SearchResult result = jestClient.execute(search);
		System.out.println("本次查询共查到：" + result.getTotal() + "篇文章！");
		List<Hit<Article, Void>> hits = result.getHits(Article.class);
		for (Hit<Article, Void> hit : hits) {
			Article source = hit.source;
			System.out.println("标题：" + source.getTitle());
			System.out.println("内容：" + source.getContent());
			System.out.println("url：" + source.getUrl());
			System.out.println("来源：" + source.getSource());
			System.out.println("作者：" + source.getAuthor());
		}
	}

	private static void createSearch(String queryString) throws Exception {
		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("title");// 高亮title
		highlightBuilder.field("content");// 高亮content
		highlightBuilder.preTags("<em>").postTags("</em>");// 高亮标签
		highlightBuilder.fragmentSize(100);// 高亮内容长度
		searchSourceBuilder.highlighter(highlightBuilder);
		Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex("article").build();
		SearchResult result = jestClient.execute(search);
		System.out.println("本次查询共查到：" + result.getTotal() + "篇文章！");
		List<Hit<Article, Void>> hits = result.getHits(Article.class);
		for (Hit<Article, Void> hit : hits) {
			Article source = hit.source;
			// 获取高亮后的内容
			Map<String, List<String>> highlight = hit.highlight;
			if (highlight != null) {
				List<String> titlelist = highlight.get("title");// 高亮后的title
				if (titlelist != null) {
					source.setTitle(titlelist.get(0));
				}

				List<String> contentlist = highlight.get("content");// 高亮后的content

				if (contentlist != null) {
					source.setContent(contentlist.get(0));
				}
			}
			System.out.println("标题：" + source.getTitle());
			System.out.println("内容：" + source.getContent());
			System.out.println("url：" + source.getUrl());
			System.out.println("来源：" + source.getSource());
			System.out.println("作者：" + source.getAuthor());
		}
	}

	private static void bulkIndex() throws Exception {
		JestClient jestClient = JestExample.getJestClient();

		Article article1 = new Article(4, "中国获租巴基斯坦瓜达尔港2000亩土地 为期43年",
				"巴基斯坦(瓜达尔港)港务局表示，将把瓜达尔港2000亩土地，长期租赁给中方，用于建设(瓜达尔港)首个经济特区。分析指，瓜港首个经济特区的建立，不但能对巴基斯坦的经济发展模式，产生示范作用，还能进一步提振经济水平。"
						+ "据了解，瓜达尔港务局于今年6月完成了1500亩土地的征收工作，另外500亩的征收工作也将很快完成，所征土地主要来自巴基斯坦海军和俾路支省政府紧邻规划的集装箱货堆区，该经济特区相关基础设施建设预计耗资3500万美元。瓜港务局表示，目前已将这2000亩地租赁给中国，中方将享有43年的租赁权。巴基斯坦前驻华大使马苏德·汗表示，这对提振巴基斯坦经济大有助益：“我认为巴基斯坦所能获得的最大益处主要还是在于经济互通领域”。"
						+ "为了鼓励国内外投资者到经济特区投资，巴政府特别针对能源、税制等国内投资短板，专门为投资者出台了利好政策来鼓励投资。这些举措包括三个方面，一是保障能源，即保障经济特区的电力和天然气供应方面享有优先权；二是减免税收，即为经济特区投资的企业提供为期10年的税收假期，并为企业有关生产设备的进口给予免关税待遇；三是一站式服务，即为有意投资经济特区的投资者提供一站式快捷服务，包括向投资者提供有关优惠政策的详尽资讯。马苏德·汗还指出，为了让巴基斯坦从投资中受益，巴政府尽可能提供了各种优惠政策：“(由于)巴基斯坦想从中获益，为此做了大量力所能及的工作”。"
						+ "巴政府高度重视瓜港经济特区建设，将其视为现代化港口的“标配”，期待其能够最大化利用瓜港深水良港的自然禀赋，吸引国内外投资者建立生产、组装以及加工企业。为鼓励投资，巴方还开出了20年免税的优惠条件。"
						+ "除了瓜达尔港，中巴还有哪些项目？"
						+ "根据中国和巴基斯坦两国4月20日发表的联合声明，双方将积极推进喀喇昆仑公路升级改造二期(塔科特至哈维连段)、瓜达尔港东湾快速路、新国际机场、卡拉奇至拉合尔高速公路(木尔坦至苏库尔段)、拉合尔轨道交通橙线、海尔－鲁巴经济区、中巴跨境光缆、在巴实行地面数字电视传输标准等重点合作项目及一批基础设施和能源电力项目。"
						+ "应巴基斯坦总统侯赛因和总理谢里夫邀请，中国国家主席习近平于4月20日至21日对巴基斯坦进行国事访问。访问期间，习近平主席会见了侯赛因总统、谢里夫总理以及巴基斯坦议会、军队和政党领导人，同巴各界人士进行了广泛接触。"
						+ "双方高度评价将中巴经济走廊打造成丝绸之路经济带和21世纪海上丝绸之路倡议重大项目所取得的进展。巴方欢迎中方设立丝路基金并将该基金用于中巴经济走廊相关项目。"
						+ "巴方将坚定支持并积极参与“一带一路”建设。丝路基金宣布入股三峡南亚公司，与长江三峡集团等机构联合开发巴基斯坦卡洛特水电站等清洁能源项目，这是丝路基金成立后的首个投资项目。丝路基金愿积极扩展中巴经济走廊框架下的其他项目投融资机会，为“一带一路”建设发挥助推作用。"
						+ "双方认为，“一带一路”倡议是区域合作和南南合作的新模式，将为实现亚洲整体振兴和各国共同繁荣带来新机遇。"
						+ "双方对中巴经济走廊建设取得的进展表示满意，强调走廊规划发展将覆盖巴全国各地区，造福巴全体人民，促进中巴两国及本地区各国共同发展繁荣。"
						+ "双方同意，以中巴经济走廊为引领，以瓜达尔港、能源、交通基础设施和产业合作为重点，形成“1+4”经济合作布局。双方欢迎中巴经济走廊联委会第四次会议成功举行，同意尽快完成《中巴经济走廊远景规划》。",
				"http://news.163.com/15/0909/14/B332O90E0001124J.html", Calendar.getInstance().getTime(), "中国青年网",
				"匿名");
		Article article2 = new Article(5, "中央党校举行秋季学期开学典礼 刘云山出席讲话",
				"新华网北京9月7日电 中共中央党校7日上午举行2015年秋季学期开学典礼。中共中央政治局常委、中央党校校长刘云山出席并讲话，就深入学习贯彻习近平总书记系列重要讲话精神、坚持党校姓党提出要求。"
						+ "刘云山指出，党校姓党是党校工作的根本原则。坚持党校姓党，重要的是坚持坚定正确的政治方向、贯彻实事求是的思想路线、落实从严治校的基本方针。要把党的基本理论教育和党性党风教育作为主课，深化中国特色社会主义理论体系学习教育，深化对习近平总书记系列重要讲话精神的学习教育，深化党章和党纪党规的学习教育。要坚持实事求是的思想方法和工作方法，弘扬理论联系实际的学风，提高教学和科研工作的针对性实效性。要严明制度、严肃纪律，把从严治校要求体现到党校工作和学员管理各方面，使党校成为不正之风的“净化器”。"
						+ "刘云山指出，坚持党校姓党，既是对党校教职工的要求，也是对党校学员的要求。每一位学员都要强化党的意识，保持对党忠诚的政治品格，忠诚于党的信仰，坚定道路自信、理论自信、制度自信；忠诚于党的宗旨，牢记为了人民是天职、服务人民是本职，自觉践行党的群众路线；忠诚于党的事业，勤政敬业、先之劳之、敢于担当，保持干事创业的进取心和精气神。要强化党的纪律规矩意识，经常看一看党的政治准则、组织原则执行得怎么样，看一看党的路线方针政策落实得怎么样，看一看重大事项请示报告制度贯彻得怎么样，找差距、明不足，做政治上的明白人、遵规守纪的老实人。"
						+ "刘云山强调，领导干部来党校学习，就要自觉接受党的优良作风的洗礼，修好作风建设这门大课。要重温党的光荣传统，学习革命先辈、英雄模范和优秀典型的先进事迹和崇高风范，自觉践行社会主义核心价值观，永葆共产党人的先进性纯洁性，以人格力量传递作风建设正能量。要认真落实党中央关于从严治党、改进作风的一系列要求，贯彻从严精神、突出问题导向，把自己摆进去、把职责摆进去，推动思想问题和实际问题一起解决，履行好党和人民赋予的职责使命。"
						+ "赵乐际出席开学典礼。" + "中央有关部门负责同志，中央党校校委成员、全体学员和教职工在主会场参加开学典礼。中国浦东、井冈山、延安干部学院全体学员和教职工在分会场参加开学典礼。",
				"http://news.163.com/15/0907/20/B2UGF9860001124J.html", Calendar.getInstance().getTime(), "新华网",
				"NN053");
		Article article3 = new Article(6, "俞正声率中央代表团赴大昭寺看望宗教界人士",
				"国际在线报道：7号上午，赴西藏出席自治区50周年庆祝活动的中共中央政治局常委、全国政协主席俞正声与中央代表团主要成员赴大昭寺慰问宗教界爱国人士。俞正声向大昭寺赠送了习近平总书记题写的“加强民族团结，建设美丽西藏”贺幛，珐琅彩平安瓶，并向僧人发放布施，他在会见僧众时表示，希望藏传佛教坚持爱国爱教传统。"
						+ "至今已有1300多年历史的大昭寺，在藏传佛教中拥有至高无上的地位。西藏的寺院归属于某一藏传佛教教派，大昭寺则是各教派共尊的神圣寺院。一年四季不论雨雪风霜，大昭寺外都有从四面八方赶来磕长头拜谒的虔诚信众。"
						+ "7号上午，赴西藏出席自治区50周年庆祝活动的中共中央政治局常委、全国政协主席俞正声与中央代表团主要成员专门到大昭寺看望僧众，“我代表党中央、国务院和习主席向大家问好。藏传佛教有许多爱国爱教的传统，有许多高僧大德维护祖国统一和民族团结，坚持传播爱国爱教的正信。党和政府对此一贯给予充分肯定，并对藏传佛教的发展给予了支持。”"
						+ "俞正声回忆这已是他自1995年以来第三次来大昭寺。他表示，过去二十年发生了巨大的变化，而藏传佛教的发展与祖国的发展、西藏的发展息息相关。藏传佛教要更好发展必须与社会主义社会相适应。他也向僧人们提出期望：“佛教既是信仰，也是一种文化和学问，希望大家不断提高自己对佛教的认识和理解，提高自己的水平。希望大家更好地管理好大昭寺，搞好民主管理、科学管理，使我们的管理更加规范。”"
						+ "今天是中央代表团抵达拉萨的第二天，当天安排有多项与自治区成立五十周年相关活动。继上午接见自治区领导成员、离退休老同志、各族各界群众代表宗教界爱国人士，参观自治区50年成就展外，中央代表团下午还慰问了解放军驻拉萨部队、武警总队等。当天晚些时候，庆祝西藏自治区成立50周年招待会、文艺晚会将在拉萨举行。（来源：环球资讯）",
				"http://news.163.com/15/0907/16/B2U3O30R00014JB5.html", Calendar.getInstance().getTime(), "国际在线",
				"全宇虹");
		Article article4 = new Article(7, "张德江:发挥人大主导作用 加快完备法律规范体系",
				"新华网广州9月7日电 中共中央政治局常委、全国人大常委会委员长张德江9月6日至7日在广东出席第21次全国地方立法研讨会，并在佛山市就地方人大工作进行调研。他强调，要全面贯彻落实党的十八大和十八届三中、四中全会精神，深入学习贯彻习近平总书记系列重要讲话精神，认真实施立法法，充分发挥人大及其常委会在立法工作中的主导作用，抓住提高立法质量这个关键，加快形成完备的法律规范体系，为协调推进“四个全面”战略布局提供法治保障。"
						+ "张德江指出，立法权是人大及其常委会的重要职权。做好新形势下立法工作，要坚持党的领导，贯彻党的路线方针政策和中央重大决策部署，切实增强思想自觉和行动自觉。要牢固树立依法立法、为民立法、科学立法理念，尊重改革发展客观规律和法治建设内在规律，加强重点领域立法，做到立法主动适应改革和经济社会发展需要。充分发挥在立法工作中的主导作用，把握立项、起草、审议等关键环节，科学确定立法项目，协调做好立法工作，研究解决立法中的重点难点问题，建立健全立法工作格局，形成立法工作合力。"
						+ "张德江说，地方性法规是我国法律体系的重要组成部分。地方立法关键是在本地特色上下功夫、在有效管用上做文章。当前要落实好立法法的规定，扎实推进赋予设区的市地方立法权工作，明确步骤和时间，做好各项准备工作，标准不能降低，底线不能突破，坚持“成熟一个、确定一个”，确保立法质量。"
						+ "张德江强调，加强和改进立法工作，要有高素质的立法工作队伍作为保障。要把思想政治建设摆在首位，全面提升专业素质能力，充实力量，培养人才，努力造就一支忠于党、忠于国家、忠于人民、忠于法律的立法工作队伍。"
						+ "张德江一直非常关注地方人大特别是基层人大工作。在粤期间，他来到佛山市人大常委会，详细询问立法、监督等工作情况，希望他们与时俱进、开拓创新，切实担负起宪法法律赋予的职责。他走进南海区人大常委会、顺德区乐从镇人大主席团，同基层人大代表和人大工作者亲切交谈，肯定基层人大代表联系群众的有益做法，强调人大代表不能脱离人民群众，必须把人民利益放在心中，时刻为群众着想，听取群众意见，反映群众意愿，帮助群众解决实际问题。张德江指出，县乡人大在基层治理体系和治理能力建设中具有重要作用。要贯彻落实中央关于加强县乡人大工作和建设的精神，认真实施新修改的地方组织法、选举法、代表法，不断提高基层人大工作水平，推动人大工作迈上新台阶。"
						+ "中共中央政治局委员、广东省委书记胡春华参加上述活动。",
				"http://news.163.com/15/0907/20/B2UGEUTJ00014PRF.html", Calendar.getInstance().getTime(), "新华网", "陈菲");

		Bulk bulk = new Bulk.Builder().defaultIndex("article").defaultType("article")
				.addAction(Arrays.asList(new Index.Builder(article1).build(), new Index.Builder(article2).build(),
						new Index.Builder(article3).build(), new Index.Builder(article4).build()))
				.build();
		jestClient.execute(bulk);
	}

	/**
	 ** @brief:批量插入json数据
	 ** @param:json路径
	 ** @return:空
	 ** @author:ycy
	 ** @date:2017-06-22
	 */
	private static void kwBulkIndex(String strJsonPath) throws Exception {
		JestClient jestClient = JestExample.getJestClient();

		// 获取Json路径
		List<KwInfo> kwList = JsonParse.GetJsonList(strJsonPath);

		// 遍历，此处是否有不用循环的写法？20170622 ycy
		for (KwInfo perKw : kwList) {
			Bulk bulk = new Bulk.Builder().defaultIndex(ESConfig.es_kwindex).defaultType(ESConfig.es_kwtype)
					.addAction(Arrays.asList(new Index.Builder(perKw).build())).build();
			JestResult result = jestClient.execute(bulk);
			System.out.println(result.toString());
		}
	}

	/**
	 ** @brief:遍历Json本地文件完成插入json数据
	 ** @param:json路径
	 ** @return:空
	 ** @author:ycy
	 ** @date:2017-06-22
	 */
	private static void imageBulkIndex() throws Exception {
		// 遍历文件
		final String JSONFILEINPUT = "E:\\JavaNew\\Jest-master\\jest\\data\\json";
		LinkedList<String> curJsonList = FileProcess.getJsonFilePath(JSONFILEINPUT);
		logger.info("size = " + curJsonList.size());

		for (int i = 0; i < curJsonList.size(); ++i) {
			// System.out.println(" i = " + i + " " + curJsonList.get(i));
			String curJsonPath = curJsonList.get(i);
			BaikeInfo curImageInfo = JsonParse.GetImageJson(curJsonPath);
			// JsonParse.printImageJson(curImageInfo);
			if (curImageInfo == null) {
				continue;
			}
			//imageInsertIndex(curImageInfo);
		}

	}
	
	/*
	 **@brief:检查唯一baikeMD5值
	 * @param：md5值
	 * @return:_id
	 * @author:ycy
	 * @date: 20170908
	 */
	public static String getUniqIdByMd5(String md5) throws Exception {
		String strId = null;

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("md5", md5));

		Search search = new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex(ESConfig.es_baikeindex).addType(ESConfig.es_baiketype).build();

		SearchResult result = jestClient.execute(search);
		//System.out.println(result.getJsonString());
		int total = result.getTotal();
		// System.out.println("找到 " + total + " 条结果!" );

		List<SearchResult.Hit<Object, Void>> hits = result.getHits(Object.class);
		// System.out.println(hits.get(0).id);

		if (total >= 1) {
			strId = hits.get(0).id;
		}

		return strId;
	}
	
	/*
	 ** @brief:更新唯一id记录操作
	 * @param：import_json 待插入的json
	 **
	 */
	public static boolean kwBaikeUpdate(BaikeInfo baikeInfo, String strUniqId) throws Exception {
		boolean bUpdateFlag = false;
		JestClient jestClient = JestExample.getJestClient();

		// 转成json串
		String strImageJson = JSON.toJSONString(baikeInfo);
		strImageJson = "{" + "    \"doc\" :" + strImageJson + "}";
		Update update = new Update.Builder(strImageJson).index(ESConfig.es_baikeindex).type(ESConfig.es_baiketype).id(strUniqId).build();
		JestResult result = jestClient.execute(update);
		// System.out.println(result.getJsonString());
		
		if (result.isSucceeded()) {
			bUpdateFlag = true;
		}

		return bUpdateFlag;

	}

	/**
	 ** @brief:baike数据插入json数据
	 ** @param:json路径
	 ** @return:空
	 ** @author:ycy
	 ** @date:2017-06-22
	 */
	public static String baikeUpsert(BaikeInfo baikeInfo) throws Exception {
		String baikeJsonRst = null;
		
		//根据content生成md5
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(baikeInfo.getContent()));
		String contentMd5 = String.format("%032x", new BigInteger(1, md5.digest()));
		logger.info("contentMd5 = " + contentMd5);
		baikeInfo.setMd5(contentMd5);
		baikeInfo.setArticle_id(contentMd5);  //new add by20171017
				
		//打印baikeInfo
		//JsonParse.PrintBaikeJson(baikeInfo);
		
		//查找MD5是否存在，存在则更新；否则则插入
		Map<String, Object> baikeRst = new HashMap<String, Object>();
		JestClient jestClient = JestExample.getJestClient();
		// 获取当前时间...
		String curTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								  .format(Calendar.getInstance().getTime());
		String uniqId = getUniqIdByMd5(contentMd5);
		if (uniqId != null && uniqId.length() != 0){
			//更新
			System.out.println("uniqId = " + uniqId);
			baikeInfo.setUpdate_time(curTimeStamp);
			if (kwBaikeUpdate(baikeInfo, uniqId)) {
				logger.info("update success!");
				baikeRst.put("code", 200);
				baikeRst.put("msg", "update success");
			} else {
				logger.error("update failed");
				baikeRst.put("code", 404);
				baikeRst.put("msg", "update failed");
			}
		}else{
			//插入
			baikeInfo.setCreate_time(curTimeStamp);
			Bulk bulk = new Bulk.Builder()
					.defaultIndex(ESConfig.es_baikeindex)
					.defaultType(ESConfig.es_baiketype)
					.addAction(Arrays.asList(
							new Index.Builder(baikeInfo).build()
					)).build();
	    	JestResult result = jestClient.execute(bulk);	 
	    	if (result.isSucceeded()) {
				logger.info("insert success!");
				baikeRst.put("code", 200);
				baikeRst.put("msg", "insert success");
			} else {
				logger.error("insert failed " + result.getErrorMessage());
				baikeRst.put("code", 404);
				baikeRst.put("msg", result.getErrorMessage());
			}
		}
		baikeJsonRst = JSON.toJSONString(baikeRst);	
    	return baikeJsonRst;
	}

	/**
	 ** @brief:baike数据插入json数据
	 ** @param:json路径
	 ** @return:空
	 ** @author:ycy
	 ** @date:2017-06-22
	 */
	public static String baikeUpsertext(BaikeInfo baikeInfo) throws Exception {
		String baikeJsonRst = null;
		
		//根据content生成md5
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(StandardCharsets.UTF_8.encode(baikeInfo.getContent()));
		String contentMd5 = String.format("%032x", new BigInteger(1, md5.digest()));
		logger.info("contentMd5 = " + contentMd5);
		baikeInfo.setMd5(contentMd5);
		baikeInfo.setArticle_id(contentMd5);  //new add by20171017
				
		//打印baikeInfo
		//JsonParse.PrintBaikeJson(baikeInfo);
		
		//查找MD5是否存在，存在则更新；否则则插入
		Map<String, Object> baikeRst = new HashMap<String, Object>();
		JestClient jestClient = JestExample.getJestClient();
		// 获取当前时间...
		String curTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								  .format(Calendar.getInstance().getTime());
		String uniqId = getUniqIdByMd5(contentMd5);
		if (uniqId != null && uniqId.length() != 0){
			//更新
			System.out.println("uniqId = " + uniqId);
			baikeInfo.setUpdate_time(curTimeStamp);
			if (kwBaikeUpdate(baikeInfo, uniqId)) {
				logger.info("update success!");
				baikeRst.put("iid", baikeInfo.getIid());
				baikeRst.put("code", 200);
				baikeRst.put("msg", "update success");
			} else {
				logger.error("update failed");
				baikeRst.put("iid", baikeInfo.getIid());
				baikeRst.put("code", 404);
				baikeRst.put("msg", "update failed");
			}
		}else{
			//插入
			baikeInfo.setCreate_time(curTimeStamp);
			Bulk bulk = new Bulk.Builder()
					.defaultIndex(ESConfig.es_baikeindex)
					.defaultType(ESConfig.es_baiketype)
					.addAction(Arrays.asList(
							new Index.Builder(baikeInfo).build()
					)).build();
	    	JestResult result = jestClient.execute(bulk);	 
	    	if (result.isSucceeded()) {
				logger.info("insert success!");
				baikeRst.put("iid", baikeInfo.getIid());
				baikeRst.put("code", 200);
				baikeRst.put("msg", "insert success");
			} else {
				logger.error("insert failed " + result.getErrorMessage());
				baikeRst.put("iid", baikeInfo.getIid());
				baikeRst.put("code", 404);
				baikeRst.put("msg", result.getErrorMessage());
			}
		}
		baikeJsonRst = JSON.toJSONString(baikeRst);	
    	return baikeJsonRst;
	}
	

	public static void testBasicUriGeneration() {
		JestClient jestClient = JestExample.getJestClient();
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

	/*
	 ** @brief:更新唯一id记录操作
	 * @param：import_json 待插入的json
	 **
	 */
	public static boolean kwUpdateIndex(KwInfo kwInfo, String strUniqId) throws Exception {

		boolean bUpdateFlag = false;
		JestClient jestClient = JestExample.getJestClient();

		// 转成json串
		String strKwJson = JSON.toJSONString(kwInfo);

		strKwJson = "{" + "    \"doc\" :" + strKwJson + "}";

		Update update = new Update.Builder(strKwJson).index(ESConfig.es_kwindex).type(ESConfig.es_kwtype).id(strUniqId).build();
		JestResult result = jestClient.execute(update);
		// System.out.println(result.getJsonString());

		if (result.isSucceeded()) {
			bUpdateFlag = true;
		}

		return bUpdateFlag;

	}

	/*
	 ** @brief:检索唯一url
	 * @param：import_json 待插入的json
	 */
	public static String kwUrlSearch(String strInsertUrl) throws Exception {
		// boolean bExist = false;
		String strId = null;

		JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("url.keyword", strInsertUrl));

		Search search = new Search.Builder(searchSourceBuilder.toString())
				// multiple index or types can be added.
				.addIndex(ESConfig.es_kwindex).addType(ESConfig.es_kwtype).build();

		SearchResult result = jestClient.execute(search);
		// System.out.println(result.getJsonString());
		int total = result.getTotal();
		// System.out.println("找到 " + total + " 条结果!" );

		List<SearchResult.Hit<Object, Void>> hits = result.getHits(Object.class);
		// System.out.println(hits.get(0).id);

		if (total >= 1) {
			strId = hits.get(0).id;
		}

		return strId;
	}

	/*
	 ** @brief:索引插入结构体数据
	 * @param：import_json 待插入的json
	 **
	 */
	public static String kwInsertIndex(KwInfo kwInfo) throws Exception {
		String kwJsonRst = null;
		JestClient jestClient = JestExample.getJestClient();

		KwInfo.kwInfoPrint(kwInfo);
		// logger.info("url = " + kwInfo.getUrl() );
		try {
			// 查询url，存在则update；不存在，则insert操作。存在，则更新操作。
			String insertUrl = kwInfo.getUrl();
			String strUniqId = kwUrlSearch(insertUrl);
			System.out.println("strUniqId = " + strUniqId);
			Map<String, Object> kwRst = new HashMap<String, Object>();

			// 获取当前时间...
			String curTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

			if (strUniqId != null && strUniqId.length() != 0) {
				// 更新操作...
				kwInfo.setUpdate_time(curTimeStamp);
				if (kwUpdateIndex(kwInfo, strUniqId)) {
					logger.info("update success!");
					kwRst.put("code", 200);
					kwRst.put("msg", "update success");
				} else {
					logger.error("update failed");
					kwRst.put("code", 404);
					kwRst.put("msg", "update failed");
				}

			} else {
				// 插入操作...
				kwInfo.setCreate_time(curTimeStamp);
				Index index = new Index.Builder(kwInfo).index(ESConfig.es_kwindex).type(ESConfig.es_kwtype).build();
				JestResult result = jestClient.execute(index);
				if (result.isSucceeded()) {
					logger.info("insert success!");
					kwRst.put("code", 200);
					kwRst.put("msg", "insert success");
				} else {
					logger.error("insert failed");
					kwRst.put("code", 404);
					kwRst.put("msg", "insert failed");
				}
			}
			kwJsonRst = JSON.toJSONString(kwRst);
		} catch (Exception e) {
			logger.error("[err]kwInfo = " + kwInfo);
			logger.info(e);
		}
		return kwJsonRst;
	}
	
	/**
	 ** @brief:bool检索
	 ** @param:待检索内容
	 ** @return:
	 ** @author:ycy
	 ** @date:2017-06-23
	 */
	public static String kwBoolSearch(int fuzzymatch, int pageNo, int size, IdentityHashMap<String, String> argsHashMap) throws Exception {
		String kwJsonRst = null;
		if (pageNo <= 0 || pageNo >= MAXSEARCHCNT || size <= 0 || fuzzymatch > 1 || fuzzymatch < 0) {
			logger.error("[kwBoolSearch]Param invalid! exit...");
			return errProcess(ERR_PARAM_MSG);
		}
		
		JestClient jestClient = JestExample.getJestClient();
		//解析动态变量
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (String key : argsHashMap.keySet()) { 
			//logger.info("key = " + key + "\tval = " + argsHashMap.get(key));
			if (key.equals("extra_search")){
				if (fuzzymatch == 0){
					//模糊匹配
					boolQueryBuilder.must(QueryBuilders.wildcardQuery(key + ".keyword", argsHashMap.get(key))); 
				}else if (fuzzymatch == 1){
					//分词检索
					boolQueryBuilder.must(QueryBuilders.matchQuery(key, argsHashMap.get(key))); 
				}else{
					return errProcess(ERR_PARAM_MSG);
				}
			}else if (key.contains("time")) {
				boolQueryBuilder.must(QueryBuilders.termQuery(key, argsHashMap.get(key)));
			}else{
				//模糊匹配
				boolQueryBuilder.must(QueryBuilders.wildcardQuery(key + ".keyword", argsHashMap.get(key))); 
			}
		}
		
		HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
		// 新增高亮处理
		highlightBuilder.field("extra_search");// 高亮extra_search
		highlightBuilder.preTags("<span>"); // 高亮标签,根据wxa反馈修改
		highlightBuilder.postTags("</span>");
		highlightBuilder.fragmentSize(100);// 高亮内容长度
		highlightBuilder.numOfFragments(5); // 返回的最大片段数
		searchSourceBuilder.highlighter(highlightBuilder);
		
		// 设置分页
		int pageNumber = pageNo;
		int pageSize = size;
		if (pageNumber * pageSize >= MAXSEARCHCNT) {
			return errProcess(WINDOWS_ERR_MSG);
		}
		searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
		searchSourceBuilder.size(pageSize);// 设置页大小
	    searchSourceBuilder.query(boolQueryBuilder);
		Search search = new Search.Builder(searchSourceBuilder.toString())
							.addIndex(ESConfig.es_kwindex).build();
		SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			return errProcess(errMsg);
		}
		//logger.info("[kwBoolSearch]: " + result.getJsonString());
		
		// 解析Json获取查询时间(ms)
		JsonObject jsonObject = result.getJsonObject();
		long took = jsonObject.get("took").getAsLong();
		float tooksecond = (float) (took) / 1000f;
		int total = result.getTotal();
		logger.info("[kwBoolSearch] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");
		// 获取分页数
		double dMaxPages = Math.ceil((double) total / (double) pageSize);
		int iMaxPages = (int) dMaxPages;
		logger.info("共 " + iMaxPages + " 页");
		if (iMaxPages < pageNo) {
			logger.info(pageNo + "页 不存在! 最大页为: " + iMaxPages);
		}

		LinkedList<KwInfo> kwList = new LinkedList<KwInfo>();
		// 解析字段、拼接Json
		JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
			// 获取返回字段
			int show_all = 1;
			KwInfo curKwInfo = highlightProc.getProcessedKwInfo(jsonHitsObject, 1, show_all);
			kwList.add(curKwInfo);
		}

		Map<String, Object> imageData = new HashMap<String, Object>();
		imageData.put("num", total);
		imageData.put("list", kwList);

		Map<String, Object> kwRst = new HashMap<String, Object>();
		kwRst.put("code", 200);
		kwRst.put("msg", "success");
		kwRst.put("total", total);
		kwRst.put("took", tooksecond);
		kwRst.put("data", imageData);
		kwJsonRst = JSON.toJSONString(kwRst);
		return kwJsonRst;
	}

	/*
	 ** @brief:最大页数异常处理
	 * @params:空
	 * @return:String
	 * @author:ycy
	 * @date:20170821
	 **
	 */
	public static String errProcess(String errMsg) {
		String errJsonRst = null;
		System.out.println("errMsg= " + errMsg);

		Map<String, Object> errRst = new HashMap<String, Object>();
		errRst.put("code", 404);
		errRst.put("msg", errMsg);

		errJsonRst = JSON.toJSONString(errRst);
		return errJsonRst;
	}
	
	 /*
	  **@brief:截取高亮字符串信息，拼串
	  **@param:SearchHit hit
	  **@return:拼串结果
	  **@author:ycy
	  **@date:20170901
	  */
	 private String getExcerpt(SearchHit hit) {
	        StringBuilder excerptBuilder = new StringBuilder();
	        for (Map.Entry<String, HighlightField> highlight : hit.getHighlightFields().entrySet()) {
	            for (Text text : highlight.getValue().fragments()) {
	                excerptBuilder.append(text.string());
	                excerptBuilder.append(" ... ");
	            }
	        }
	        return excerptBuilder.toString();
	 }
	 
		/*
		 ** @brief:检索唯一url
		 * @param：import_json 待插入的json
		 */
		public static String mirrorPathSearch(String strPath) throws Exception {
			// boolean bExist = false;
			String strId = null;

			JestClient jestClient = JestExample.getJestClient();
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.termQuery("path.keyword", strPath));

			Search search = new Search.Builder(searchSourceBuilder.toString())
					// multiple index or types can be added.
					.addIndex(ESConfig.es_imageindex).addType(ESConfig.es_imagetype).build();

			SearchResult result = jestClient.execute(search);
			// System.out.println(result.getJsonString());
			int total = result.getTotal();
			// System.out.println("找到 " + total + " 条结果!" );

			List<SearchResult.Hit<Object, Void>> hits = result.getHits(Object.class);
			// System.out.println(hits.get(0).id);

			if (total >= 1) {
				strId = hits.get(0).id;
			}

			return strId;
		}
	 
	 /**
		 **@brief:单条数据插入json数据
		 **@param:json路径
		 **@return:空
		 **@author:ycy
		 **@date:2017-06-22
		 */
		public static String imageExtInsert(ImageExtInfo imageExtInfo) throws Exception {
			String imageJsonRst = null;
			Map<String, Object> imageRst = new HashMap<String, Object>();
			JestClient jestClient = JestExample.getJestClient();
			
			String curPath = imageExtInfo.getPath();
			String curId =  mirrorPathSearch(curPath);
			if (null != curId && curId.length() != 0){
				logger.error(curId + " already exist!");
				imageRst.put("code", 404);
				imageRst.put("msg", curId + " already exist!");
			}else{		
				Bulk bulk = new Bulk.Builder()
						.defaultIndex(ESConfig.es_imageindex)
						.defaultType(ESConfig.es_imagetype)
						.addAction(Arrays.asList(
								new Index.Builder(imageExtInfo).build()
						)).build();
		    	JestResult result = jestClient.execute(bulk);	 
		    	if (result.isSucceeded()) {
					logger.info("insert success!");
					imageRst.put("code", 200);
					imageRst.put("msg", "insert success");
				} else {
					logger.error("insert failed");
					imageRst.put("code", 404);
					imageRst.put("msg", "insert failed");
				}
			}
			
			imageJsonRst = JSON.toJSONString(imageRst);	
	    	return imageJsonRst;
		}
		
		/**
		 ** @brief:返回百科文件的列表信息。
		 ** @param:待检索内容
		 ** @return:检索结果值，3部分(返回总条数，返回时间，list详情)
		 ** @author:ycy
		 ** @date:2017-06-23
		 */
		public static String baikeListFullTextSearch(String queryString, int pageNo, int highlight) throws Exception {
			String imageJsonRst = null;
			if (queryString == null || queryString.length() == 0 || queryString.equals("") || pageNo <= 0
					|| pageNo >= MAXSEARCHCNT) {
				logger.error("Param invalid! exit...");
				return errProcess(ERR_PARAM_MSG);
			}
			logger.info("[baikefulltext]keyword = " + queryString);

			JestClient jestClient = JestExample.getJestClient();
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			//searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
			searchSourceBuilder.query(QueryBuilders.matchQuery("content", queryString));

			// 新增高亮处理
			HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(false);
			//highlightBuilder.field("title");// 高亮title
			highlightBuilder.field("content");// 高亮content
			highlightBuilder.preTags("<span style=\"color:red\">"); // 高亮红色标签
			highlightBuilder.postTags("</span>");
			highlightBuilder.fragmentSize(100);// 高亮内容长度
			highlightBuilder.numOfFragments(5); // 返回的最大片段数
			searchSourceBuilder.highlighter(highlightBuilder);
			// 设置分页
			int pageNumber = pageNo;
			int pageSize = 10;
			if (pageNumber * pageSize >= MAXSEARCHCNT) {
				return errProcess(WINDOWS_ERR_MSG);
			}

			searchSourceBuilder.from((pageNumber - 1) * pageSize);// 设置起始页
			searchSourceBuilder.size(pageSize);// 设置页大小
			 //new add by ycy 限定字符输出
	        searchSourceBuilder.fetchSource(new String[] {
	        	      "title", "page",
	         	     "divide_flag", "article_id",
	         	     "total_page","path"
	        	    }, null);
			Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(ESConfig.es_baikeindex).build();
			SearchResult result = jestClient.execute(search);
		    //logger.info("[fulltext]: " + searchSourceBuilder.toString());
			if (!result.isSucceeded()){
				String errMsg = result.getErrorMessage();
				return errProcess(errMsg);
			}

			// 解析Json获取查询时间(ms)
			JsonObject jsonObject = result.getJsonObject();
			long took = jsonObject.get("took").getAsLong();
			float tooksecond = (float) (took) / 1000f;
			int total = result.getTotal();
			logger.info("[fulltext] 找到 " + total + " 条结果 （用时 " + tooksecond + " 秒）");

			// 获取分页数
			double dMaxPages = Math.ceil((double) total / (double) pageSize);
			int iMaxPages = (int) dMaxPages;
			logger.info("共 " + iMaxPages + " 页");
			if (iMaxPages < pageNo) {
				logger.error(pageNo + "页 不存在! 最大页为: " + iMaxPages);
			}

			LinkedList<BaikeInfo> imageList = new LinkedList<BaikeInfo>();
			// 解析字段、拼接Json
			JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
			JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
				BaikeInfo curBaikeInfo = new BaikeInfo();

				// _id字段
				curBaikeInfo.setIid(jsonHitsObject.get("_id").getAsString());
				// 获取返回字段
				JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();
				// 封装Article对象
				curBaikeInfo.setTitle(sourceObject.get("title").getAsString());
				if (sourceObject.get("content") != null) {
					String totalContent = sourceObject.get("content").getAsString();
					if (totalContent.length() > 50) {
						totalContent = totalContent.substring(0, MAX_INFO_LEN);
					}
					curBaikeInfo.setContent(totalContent);
				}
				
				  //new add by ycy 20171117
			    if (sourceObject.get("path") != null){
		            String strCurPath = sourceObject.get("path").getAsString();
		            if (strCurPath != null){
		            	strCurPath = strCurPath.substring(0,strCurPath.length()-3);
		            	strCurPath = "http://keepwork.com/keepwork/baike" + strCurPath;
		            	curBaikeInfo.setKeepwork_url(strCurPath);
		            }
	            }
				
				 //new add by ycy 20171017
	            if (null != sourceObject.get("page")){
		            int ipageNo = sourceObject.get("page").getAsInt();
		            if (ipageNo != 0){
		                curBaikeInfo.setPage(ipageNo);   
		            }
	            }
	            if (null != sourceObject.get("divide_flag")){
		            int idivide_flag = sourceObject.get("divide_flag").getAsInt();
		            if (idivide_flag != 0){
		            	curBaikeInfo.setDivide_flag(idivide_flag);   
		            }
	            }
	            
	            if (null != sourceObject.get("article_id")){
		            String article_id = sourceObject.get("article_id").getAsString();
		            if (article_id != null){
		            	 curBaikeInfo.setArticle_id(article_id);
		            }
	            }
	            if (null != sourceObject.get("totalpage")){
		            int pag_num = sourceObject.get("totalpage").getAsInt();
		            if (pag_num != 0){
		            	 curBaikeInfo.setTotalpage(pag_num);   
		            }
	            }
				
				if (highlight == 1) {
					// 获取高亮字段
					if (jsonHitsObject.get("highlight") != null) {
						JsonObject highlightObject = jsonHitsObject.get("highlight").getAsJsonObject();

						String content = "";
						String title = "";
						if (highlightObject.get("content") != null) {
							content = highlightObject.get("content").getAsJsonArray().get(0).getAsString();
						}
						if (highlightObject.get("title") != null) {
							title = highlightObject.get("title").getAsJsonArray().get(0).getAsString();
						}

						if (!content.equalsIgnoreCase("")) {
							/*if (content.length() > 50) {
								String spanWord = "</span>";
								int spanLen = spanWord.length();
								int spanStartPos = content.indexOf(spanWord);
								//System.out.println("spanStartPos = " + spanStartPos + "spanLen =" + spanLen);
								int stratPos = 0;
								if (spanStartPos + spanLen > 50) {
									stratPos = spanStartPos + spanLen - 50;
									content = content.substring(stratPos, spanStartPos + spanLen);
								} else {
									content = content.substring(0, spanStartPos + spanLen);
								}
								//System.out.println("content = " + content);
							}*/
							curBaikeInfo.setContent(content);
						}
						if (!title.equalsIgnoreCase("")) {
							curBaikeInfo.setTitle(title);
						}
					}
				} // end highlight

				imageList.add(curBaikeInfo);
			}

			// Map<String, Object> imageData = new HashMap<String, Object>();

			Map<String, Object> imageData = new HashMap<String, Object>();
			imageData.put("num", total);
			imageData.put("list", imageList);

			Map<String, Object> imageRst = new HashMap<String, Object>();

			imageRst.put("code", 200);
			imageRst.put("msg", "success");
			imageRst.put("total", total);
			imageRst.put("took", tooksecond);
			imageRst.put("data", imageData);

			imageJsonRst = JSON.toJSONString(imageRst);
			// logger.info(imageJsonRst);

			return imageJsonRst;
		}
	 
		
	/**
	 ** @brief:返回所有信息
	 ** @param:待检索内容
	 ** @return:检索结果值，
	 ** @author:ycy
	 ** @date:2017-09-27
	 */
	 public static SearchResult startScrollSearch(Long size) throws IOException {
     //   String query = ConfigurationFactory.loadElasticScript("my_es_search_script.json");
        
    	JestClient jestClient = JestExample.getJestClient();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	    searchSourceBuilder.query(QueryBuilders.matchAllQuery());
	    searchSourceBuilder.fetchSource(new String[] {
        	      "title"
        	    }, null);
	    Search search = new Search.Builder(searchSourceBuilder.toString())
	    						  .addIndex(ESConfig.es_baikeindex)
	    						  .addSort(new Sort("_doc"))
	    					      .setParameter(Parameters.SIZE, size)
	    						  .setParameter(Parameters.SCROLL, "5m")
	    						  .build();
	    SearchResult result = jestClient.execute(search);
		if (!result.isSucceeded()){
			String errMsg = result.getErrorMessage();
			logger.info("[startScrollSearch]errMsg = " + errMsg);
		}
		return result;
	}
	 
	 /**
	 ** @brief:读取scroll信息
	 ** @param:待检索内容
	 ** @return:检索结果值，
	 ** @author:ycy
	 ** @date:2017-09-27
	 */
	public static JestResult readMoreFromSearch(String scrollId, Long size) throws IOException {
		    SearchScroll scroll = new SearchScroll.Builder(scrollId, "1m").build();
		               // .setParameter(Parameters.SIZE, size).build();
		    JestClient jestClient = JestExample.getJestClient();
		    JestResult searchResult = jestClient.execute(scroll);
		    if (!searchResult.isSucceeded()){
				String errMsg = searchResult.getErrorMessage();
				logger.info("[readMoreFromSearch]errMsg = " + errMsg);
			}
		    logger.info("rst = " + searchResult.getJsonString());
		    return searchResult;
	}
	
	 /**
	 ** @brief:scrollTest信息
	 ** @param:
	 ** @return:
	 ** @author:ycy
	 * @throws IOException 
	 ** @date:2017-09-27
	 */
	public static void scrollSearchTest() throws IOException{
		SearchResult result = startScrollSearch((long) 50);
    	String scrollId = result.getJsonObject().get("_scroll_id").getAsString();
    	System.out.println("scrollId = " + scrollId);
    	long size = 10L;
    	readMoreFromSearch(scrollId, size);
	    //打印信息
	}
		
	 protected static Map<String, List<String>> extractHighlight(JsonObject highlight) {
	        Map<String, List<String>> retval = null;

	        if (highlight != null) {
	            Set<Map.Entry<String, JsonElement>> highlightSet = highlight.entrySet();
	            retval = new HashMap<String, List<String>>(highlightSet.size());

	            for (Map.Entry<String, JsonElement> entry : highlightSet) {
	                List<String> fragments = new ArrayList<String>();
	                for (JsonElement element : entry.getValue().getAsJsonArray()) {
	                    fragments.add(element.getAsString());
	                }
	                retval.put(entry.getKey(), fragments);
	            }
	        }

	        return retval;
	    }

	 	/**
		 ** @brief:百科高级检索
		 ** @param:待检索内容
		 ** @return:
		 ** @author:ycy
		 ** @date:2017-06-23
		 */
		public static String baikeAdvanceSearch(Integer pageNo, String p1, String p2, 
	    		String p3, String p4, String p5, String p6, String p7) throws Exception {
			String bkJsonRst = null;
			if (pageNo <= 0 || pageNo >= MAXSEARCHCNT || isEmpty(p7)) {
				logger.error("Param invalid! exit...");
				return errProcess(ERR_PARAM_MSG);
			}
		   /*logger.info("[baikefulltext]keyword = " + keyword);
			
			//解析keyword
			String[] wordArray = keyword.split("&");
			String p1 = wordArray[0];
			String p2 = wordArray[1];
			String p3 = wordArray[2];
			String p4 = wordArray[3];
			String p5 = wordArray[4];
			String p6 = wordArray[5];
			String p7 = wordArray[6];*/
			
			JestClient jestClient = JestExample.getJestClient();
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			
			//获取待检索类别20171102
			LinkedList<String> lableList = new LinkedList<String>();
			if (p7.equalsIgnoreCase(SLABLE_TITLE)){
				lableList.add("title");
			}else if (p7.equalsIgnoreCase(SLABLE_CONTENT)){
				lableList.add("content");
			}else if (p7.equalsIgnoreCase(SLABLE_TOTAL)){
				lableList.add("title");
				lableList.add("content");
			}else{
				logger.error("[baikeAdvanceSearch]Param invalid! exit...");
				return errProcess(ERR_PARAM_MSG);
			}
			
			BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
			
			int iLableSize = lableList.size();
			if (p7.equalsIgnoreCase(SLABLE_TITLE) || p7.equalsIgnoreCase(SLABLE_CONTENT)){
				for (int i = 0; i < iLableSize; ++i){
					//p1->分词检索
					if (!isEmpty(p1)){
						boolQueryBuilder.must(QueryBuilders.matchQuery(lableList.get(i), p1));   
					}
					//p2->wildcard检索
					if (!isEmpty(p2)){
						boolQueryBuilder.must(QueryBuilders.wildcardQuery(lableList.get(i) + ".keyword", "*"+p2+"*")); 
					}
					
					//p3->拆分检索 !
					if (!isEmpty(p3)){
						String[] p3Array = p3.split(" ");
						for(String p3Value : p3Array){
							if (!isEmpty(p3Value)){
								boolQueryBuilder.should(QueryBuilders.wildcardQuery(lableList.get(i) + ".keyword", "*"+p3Value+"*")); 
							}
						}  
					}
					
				    //p4->wildcard检索-不包含
					if (!isEmpty(p4)){
						boolQueryBuilder.mustNot(QueryBuilders.matchQuery(lableList.get(i), p4)); 
					}
				    //p5开始时间、p6结束时间
					if (!isEmpty(p5) && !isEmpty(p6)){
						boolQueryBuilder.must(QueryBuilders.rangeQuery("create_time").from(p5).to(p6));
					}
				} 
			}else if(p7.equalsIgnoreCase(SLABLE_TOTAL)){
				//p1->分词检索 ok
				if (!isEmpty(p1)){
					boolQueryBuilder.must(QueryBuilders.queryStringQuery(p1).field("title").field("content"));   
				}
				//p2->wildcard检索
				if (!isEmpty(p2)){
					boolQueryBuilder.should(QueryBuilders.wildcardQuery("title.keyword", "*"+p2+"*")); 
					boolQueryBuilder.should(QueryBuilders.wildcardQuery("content.keyword", "*"+p2+"*")); 
				}
				//p3->拆分检索 
				if (!isEmpty(p3)){
					String[] p3Array = p3.split(" ");
					for(String p3Value : p3Array){
						if (!isEmpty(p3Value)){
							boolQueryBuilder.should(QueryBuilders.wildcardQuery("title.keyword", "*"+p3Value+"*")); 
							boolQueryBuilder.should(QueryBuilders.wildcardQuery("content.keyword", "*"+p3Value+"*")); 
						}   
					}
				}
			    //p4->wildcard检索-不包含
				if (!isEmpty(p4)){
					boolQueryBuilder.mustNot(QueryBuilders.queryStringQuery(p4)); 
				}
			    //p5开始时间、p6结束时间
				if (!isEmpty(p4) && !isEmpty(p5)){
					boolQueryBuilder.must(QueryBuilders.rangeQuery("create_time").from(p5).to(p6));
				}
			}else{
				logger.error("[baikeAdvanceSearch]Param invalid! exit...");
				return errProcess(ERR_PARAM_MSG);
			}
		    searchSourceBuilder.query(boolQueryBuilder);
		    
			//新增高亮处理
			HighlightBuilder highlightBuilder = new HighlightBuilder().requireFieldMatch(true);
			highlightBuilder.field("title");//高亮title
			highlightBuilder.field("content");//高亮title
			highlightBuilder.preTags("<span style=\"color:red\">");  //高亮红色标签
			highlightBuilder.postTags("</span>");
			highlightBuilder.fragmentSize(100);//高亮内容长度
			highlightBuilder.numOfFragments(5); //返回的最大片段数
			searchSourceBuilder.highlighter(highlightBuilder);
			
		    // 设置分页
			int pageNumber = pageNo;
			int pageSize = 10;
			if (pageNumber * pageSize >= MAXSEARCHCNT){	
				return errProcess(WINDOWS_ERR_MSG);
		    }
	        searchSourceBuilder.from((pageNumber - 1) * pageSize);//设置起始页
	        searchSourceBuilder.size(pageSize);//设置页大小
	        //new add by ycy 限定字符输出
	        searchSourceBuilder.fetchSource(new String[] {
	        	      "title",
	        	     "content",
	        	     "page_no",
	        	     "divide_flag",
	        	     "article_id",
	        	     "page_num"
	        	    }, null);
	        //logger.info(searchSourceBuilder.toString());
			Search search = new Search.Builder(searchSourceBuilder.toString())
					.addIndex(ESConfig.es_baikeindex)
					.build();
			SearchResult result = jestClient.execute(search);
			if (!result.isSucceeded()){
				String errMsg = result.getErrorMessage();
				return errProcess(errMsg);
			}
			
			//logger.info("[baikeAdvanceSearch]: " + result.getJsonString());  
			//解析Json获取查询时间(ms)
			JsonObject jsonObject = result.getJsonObject();
		    long took = jsonObject.get("took").getAsLong();
		    float tooksecond = (float)(took)/1000f;
		    int total = result.getTotal();
		    logger.info("[baikeAdvanceSearch] 找到 " + total +" 条结果 （用时 " + tooksecond + " 秒）");
		    
		    //获取分页数
		    double dMaxPages = Math.ceil((double)total/(double)pageSize);
		    int iMaxPages = (int)dMaxPages;
		    logger.info("共 " + iMaxPages +" 页");
		    if (iMaxPages < pageNo){
		    	logger.error( pageNo + "页 不存在! 最大页为: " + iMaxPages);
		    }		
		    
		    LinkedList<BaikeInfo> imageList = new LinkedList<BaikeInfo>();
		        
		    //解析字段、拼接Json
		    JsonObject hitsobject = jsonObject.getAsJsonObject("hits");
		    JsonArray jsonArray = hitsobject.getAsJsonArray("hits");
	        for (int i = 0; i < jsonArray.size(); i++) {
	            JsonObject jsonHitsObject = jsonArray.get(i).getAsJsonObject();
	            BaikeInfo curBaikeInfo = new BaikeInfo();
	            
	            //_id字段
	            curBaikeInfo.setIid(jsonHitsObject.get("_id").getAsString());         
	            // 获取返回字段
	            JsonObject sourceObject = jsonHitsObject.get("_source").getAsJsonObject();        
	            // 封装Article对象
	            curBaikeInfo.setTitle(sourceObject.get("title").getAsString());
	            String totalContent = sourceObject.get("content").getAsString();
	            if (totalContent.length() > 50){
	            	totalContent = totalContent.substring(0, MAX_INFO_LEN);
	            }
	            curBaikeInfo.setContent(totalContent);
	            
	            //new add by ycy 20171017
	            /*if (null != sourceObject.get("page_no")){
		            int ipageNo = sourceObject.get("page_no").getAsInt();
		            if (ipageNo != 0){
		                curBaikeInfo.setPage_no(ipageNo);   
		            }
	            }*/
	            if (null != sourceObject.get("divide_flag")){
		            int idivide_flag = sourceObject.get("divide_flag").getAsInt();
		            if (idivide_flag != 0){
		            	curBaikeInfo.setDivide_flag(idivide_flag);   
		            }
	            }
	            
	            if (null != sourceObject.get("article_id")){
		            String article_id = sourceObject.get("article_id").getAsString();
		            if (article_id != null){
		            	 curBaikeInfo.setArticle_id(article_id);
		            }
	            }
	          /*  if (null != sourceObject.get("page_num")){
		            int pag_num = sourceObject.get("page_num").getAsInt();
		            if (pag_num != 0){
		            	 curBaikeInfo.setPage_num(pag_num);   
		            }
	            }*/
            	  // 获取高亮字段
            	if (jsonHitsObject.get("highlight") != null) {
	                JsonObject highlightObject = jsonHitsObject.get("highlight").getAsJsonObject();
	        
	            	String content = "";
					String title = "";
					if (highlightObject.get("content") != null) {
						content = highlightObject.get("content").getAsJsonArray().get(0).getAsString();
					}
					if (highlightObject.get("title") != null) {
						title = highlightObject.get("title").getAsJsonArray().get(0).getAsString();
					}
					if (!title.equalsIgnoreCase("")) {
		                curBaikeInfo.setTitle(title);
		            }
					if (!content.equalsIgnoreCase("")) {
						curBaikeInfo.setContent(content);
					}
            	}//end (jsonHitsObject.get("highlight")
	            imageList.add(curBaikeInfo);
	        }
	        Map<String, Object> imageData = new HashMap<String, Object>();
	        imageData.put("num", total);
	        imageData.put("list", imageList);
		    
	    	Map<String, Object> bkRst = new HashMap<String, Object>();
	    	bkRst.put("code", 200);
	    	bkRst.put("msg", "success");
	    	bkRst.put("total", total);
	    	bkRst.put("took", tooksecond);
	    	bkRst.put("data", imageData);
	    	bkJsonRst = JSON.toJSONString(bkRst);
	    
			return bkJsonRst;
		}
		
		/**
	     * Returns true if the string is null or 0-length.
	     * @param str the string to be examined
	     * @return true if str is null or zero length
	     */
	    public static boolean isEmpty(String str) {
	        if (str == null || str.length() == 0 || str.equals(""))
	            return true;
	        else
	            return false;
	    }

	 
}