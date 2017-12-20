import java.util.HashMap;
import java.util.Map;
import java.util.*;
import org.junit.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;

import io.searchbox.client.JestClient;
import play.test.*;
import test.java.io.searchbox.client.JestExample;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;
import org.ansj.app.keyword.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.action.search.*;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONObject;
import org.junit.Test;
import org.apache.commons.lang.*;
import org.apache.http.HttpEntity;
import org.apache.http.entity.*;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;



public class ApplicationTest extends FunctionalTest {

	  @Test
	  //镜像检索——标题列表接口
	    public void testEssearch_title() {
	        Response response = GET("/Application/essearch?keyword=%E6%98%A5%E7%A7%8B&page=1&highlight=1&flag=0");
	        assertIsOk(response);
	        String title = response.out.toString();
	        System.out.println(JSONPath.read(title, "$.msg"));
	        System.out.println(title);
	        System.out.println(JSONPath.read(title, "$.total"));
	        assertEquals(200,JSONPath.read(title, "$.code"));
	        assertEquals("success",JSONPath.read(title, "$.msg"));
	        Object test = JSONPath.read(title, "$.data.list[0:100000000:1]"); 
	        String str= null;
	        str = test.toString();
	        List<String> list = new ArrayList<String>();
	        list.add(str);
	        String[] strArray= list.toArray(new String[list.size()]);
	        String result = JSONPath.read(strArray[0], "$.url").toString();
	        System.out.println(strArray[0]);
	        System.out.println(result);
	        
	        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"title\":\"春秋\"}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}}");
	        String url = new String ("http://10.0.1.36:9200/image_index/_search");
	        String response_es = sendPost(url,body);
	        System.out.println(response_es);
	        System.out.println(JSONPath.read(response_es, "$.hits.total"));
	        Object test_es = JSONPath.read(response_es, "$.hits.hits[0:100000000:1]");      
	        String str_es = test_es.toString();
	        List<String> list_es = new ArrayList<String>();
	        list.add(str_es);
	        String[] strArray_es= list.toArray(new String[list_es.size()]);
	        String result_es = JSONPath.read(strArray_es[0], "$.url").toString();
	        System.out.println(result_es);
	        System.out.println(strArray_es[1]);
	        System.out.println(JSONPath.read(strArray[0], "$.url"));
	        assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(title, "$.total"));
	        assertEquals(result_es,result);
	    }
	
	  @Test
	  //镜像检索——全文列表接口
	    public void testEssearch_fulltext() {
	        Response response = GET("/Application/essearch?keyword=%E6%98%A5%E7%A7%8B&page=1&highlight=1&flag=1");
	        assertIsOk(response);
	        String fulltext = response.out.toString();
	        System.out.println(JSONPath.read(fulltext, "$.msg"));
	        System.out.println(JSONPath.read(fulltext, "$.code"));
	        System.out.println(JSONPath.read(fulltext, "$.total"));
	        assertEquals(200,JSONPath.read(fulltext, "$.code"));
	        assertEquals("success",JSONPath.read(fulltext, "$.msg"));
	        Object test = JSONPath.read(fulltext, "$.data.list[0:100000000:1]"); 
	        String str = test.toString();
	        List<String> list = new ArrayList<String>();
	        list.add(str);
	        String[] strArray= list.toArray(new String[list.size()]);
	        String result = JSONPath.read(strArray[0], "$.url").toString();
	        System.out.println(strArray[0]);
	        System.out.println(result);
	        
	        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"default_field\":\"_all\",\"query\":\"春秋\"}}]}}}");
	        String url = new String ("http://10.0.1.36:9200/image_index/_search");
	        String response_es = sendPost(url,body);
	        System.out.println(response_es);
	        System.out.println(JSONPath.read(response_es, "$.hits.total"));
	        Object test_es = JSONPath.read(response_es, "$.hits.hits[0:100000000:1]");      
	        String str_es = test_es.toString();
	        List<String> list_es = new ArrayList<String>();
	        list.add(str_es);
	        String[] strArray_es= list.toArray(new String[list_es.size()]);
	        String result_es = JSONPath.read(strArray_es[0], "$.url").toString();
	        System.out.println(result_es);
	        System.out.println(strArray_es[1]);
	        System.out.println(JSONPath.read(strArray[0], "$.url"));
	        assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(fulltext, "$.total"));
	        assertEquals(result_es,result);
	    } 
	
	  @Test
	  //镜像检索——详情接口
	    public void testEssearch_details() {
	        Response response = GET("/Application/essearch?keyword=AV1ZXeEhLcH9rGedVRnN&page=1&flag=2");
	        assertIsOk(response);
	        String details = "";
	        details = response.out.toString();
	        System.out.println(details);
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(200,JSONPath.read(details, "$.code"));
	        assertEquals("success",JSONPath.read(details, "$.msg"));
	        assertEquals(1,JSONPath.read(details, "$.total"));
	    }
	
	  @Test
	  //镜像检索flag不等于0/1/2
	    public void testEssearch_flag() {
	        Response response = GET("/Application/essearch?keyword=AV1ZXeEhLcH9rGedVRnN&page=1&flag=3");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }
	  @Test
	  //镜像检索请求字段keyword缺失
	    public void testEssearch_keyword() {
	        Response response = GET("/Application/essearch?page=1&flag=1");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }
	  @Test
	  //镜像检索请求字段page缺失
	    public void testEssearch_page() {
	        Response response = GET("/Application/essearch?keyword=AV1ZXeEhLcH9rGedVRnN&flag=2");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }
	  @Test
	  //镜像检索请求字段flag缺失
	    public void testEssearch_flag_miss() {
	        Response response = GET("/Application/essearch?keyword=AV1ZXeEhLcH9rGedVRnN&page=1");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }
	  @Test
	  //镜像检索请求flag=0时，highlight缺失
	    public void testEssearch_highlight_miss() {
	        Response response = GET("/Application/essearch?keyword=%E6%9B%B9%E6%93%8D&page=1&flag=0");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    } 
	  @Test
	  //镜像检索请求flag=1时，highlight缺失
	    public void testEssearch_highlight1_miss() {
	        Response response = GET("/Application/essearch?keyword=%E6%9B%B9%E6%93%8D&page=1&flag=1");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }  
	  @Test
	  //镜像检索请求flag=0时，highlight取值非0/1
	    public void testEssearch_highlight() {
	        Response response = GET("/Application/essearch?keyword=%E6%9B%B9%E6%93%8D&page=1&flag=0&highlight=3");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }
	  @Test
	  //镜像检索请求flag=1时，highlight取值非0/1
	    public void testEssearch_highlight1() {
	        Response response = GET("/Application/essearch?keyword=%E6%9B%B9%E6%93%8D&page=1&flag=1&highlight=5");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }
	 
	 
	  @Test
	  //Keepwork检索数据提交接口
	    public void testkwupsert() {
		  try{
		String filename=RandomStringUtils.randomAlphanumeric(10);
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    String request = "http://221.0.111.131:19001/Application/kwupsert?tags=www&user_name=www&site_name=www&access_url=www&data_source_url=www&content=www&page_name=www&commit_id=www&extra_data=www&extra_search=www&url="+ filename;
	    HttpPost postRequest = new HttpPost(request);
	    org.apache.http.HttpResponse response = httpClient.execute(postRequest);
//	    if (response.getStatusLine().getStatusCode() != 200) {
//	        throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
//	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {
	        System.out.println(output);
	        assertEquals("{\"msg\":\"insert success\",\"code\":200}",output);
	    }		
		  }
		  catch (Exception ex) {
			    //handle exception here
				ex.printStackTrace();		   
			}   
	    } 
	  @Test
	  //Keepwork检索数据更新接口
	    public void testkwupsert_update() {
		  try{
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    String request = "http://221.0.111.131:19001/Application/kwupsert?tags=www&user_name=www&site_name=www&access_url=www&data_source_url=www&content=www&page_name=www&commit_id=www&extra_data=www&extra_search=www&url=/ttttt/test/测试页面3";
	    HttpPost postRequest = new HttpPost(request);
	    org.apache.http.HttpResponse response = httpClient.execute(postRequest);
//	    if (response.getStatusLine().getStatusCode() != 200) {
//	        throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
//	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {
	        System.out.println(output);
	        assertEquals("{\"msg\":\"update success\",\"code\":200}",output);
	    }		
		  }
		  catch (Exception ex) {
			    //handle exception here
				ex.printStackTrace();		   
			}   
	    }
	    
	  @Test
	  //Keepwork检索数据接口url不存在
	    public void testkwupsert_url() {
		  try{
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    String request = "http://221.0.111.131:19001/Application/kwupsert?tags=hello&user_name=zjx&site_name=zjx&access_url=123&data_source_url=123&content=1234321&page_name=123&commit_id=123&extra_data=123&extra_search=1133";
	    HttpPost postRequest = new HttpPost(request);
	    org.apache.http.HttpResponse response = httpClient.execute(postRequest);
//	    if (response.getStatusLine().getStatusCode() != 200) {
//	        throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
//	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {
	        System.out.println(output);
	        assertEquals("{\"msg\":\"[ERROR] Invalid params!\",\"code\":404}",output);
	    }		
		  }
		  catch (Exception ex) {
			    //handle exception here
				ex.printStackTrace();		   
			}   
	    } 
	  @Test
	  //Keepwork检索数据接口url为空
	    public void testkwupsert_url_null() {
		  try{
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    String request = "http://221.0.111.131:19001/Application/kwupsert?url=&tags=hello&user_name=zjx&site_name=zjx&access_url=123&data_source_url=123&content=1234321&page_name=123&commit_id=123&extra_data=123&extra_search=1133";
	    HttpPost postRequest = new HttpPost(request);
	    org.apache.http.HttpResponse response = httpClient.execute(postRequest);
//	    if (response.getStatusLine().getStatusCode() != 200) {
//	        throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
//	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {
	        System.out.println(output);
	        assertEquals("{\"msg\":\"[ERROR] Invalid params!\",\"code\":404}",output);
	    }		
		  }
		  catch (Exception ex) {
			    //handle exception here
				ex.printStackTrace();		   
			}   
	    } 
	  @Test
	  //Keepwork检索数据接口除url外其他参数非必填
	    public void testkwupsert_url_necessary() {
		  try{
	    String filename=RandomStringUtils.randomAlphanumeric(10);
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    String request = "http://221.0.111.131:19001/Application/kwupsert?url="+ filename;;
	    HttpPost postRequest = new HttpPost(request);
	    org.apache.http.HttpResponse response = httpClient.execute(postRequest);
//	    if (response.getStatusLine().getStatusCode() != 200) {
//	        throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
//	    }
	    BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
	    String output;
	    System.out.println("Output from Server .... \n");
	    while ((output = br.readLine()) != null) {
	        System.out.println(output);
	        assertEquals("{\"msg\":\"insert success\",\"code\":200}",output);
	    }		
		  }
		  catch (Exception ex) {
			    //handle exception here
				ex.printStackTrace();		   
			}   
	    } 
	 
	@Test
    //url推荐 接口
    public void testKwurllist_search() {
        Response response = GET("/Application/Kwurllist_search?keyword=sangfo&page=1&size=10");
        assertIsOk(response);
        String Kwfulltext_search = response.out.toString();
        System.out.println(Kwfulltext_search);
        System.out.println(JSONPath.read(Kwfulltext_search, "$.total"));
        assertEquals("success",JSONPath.read(Kwfulltext_search, "$.msg"));
        assertEquals(200,JSONPath.read(Kwfulltext_search, "$.code"));       
    } 
    @Test
    //url推荐 接口 size=0
    public void testKwurllist_search_size0() {
        Response response = GET("/Application/Kwurllist_search?keyword=sangfo&page=1&size=0");
        assertIsOk(response);
        String Kwfulltext_search = response.out.toString();
        System.out.println(Kwfulltext_search);
        System.out.println(JSONPath.read(Kwfulltext_search, "$.total"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search, "$.msg"));
        assertEquals(404,JSONPath.read(Kwfulltext_search, "$.code"));       
    }
	  @Test
	  //url推荐 接口keyword缺失
	    public void testKwurllist_keyword_miss() {
	        Response response = GET("/Application/Kwurllist_search?page=1&size=10");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    }
	  @Test
	  //url推荐 接口keyword缺失
	    public void testKwurllist_page_miss() {
	        Response response = GET("/Application/Kwurllist_search?keyword=sangfo&size=10");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    } 
	  @Test
	  //url推荐 接口keyword缺失
	    public void testKwurllist_size_miss() {
	        Response response = GET("/Application/Kwurllist_search?keyword=sangfo&page=1");
	        assertIsOk(response);
	        String details = response.out.toString();
	        System.out.println(JSONPath.read(details, "$.msg"));
	        System.out.println(JSONPath.read(details, "$.code"));
	        System.out.println(JSONPath.read(details, "$.total"));
	        assertEquals(404,JSONPath.read(details, "$.code"));
	        assertEquals("[ERROR] Invalid params!",JSONPath.read(details, "$.msg"));
	    } 
      @Test
    //用户自定义检索
    public void testKwcustom_search() {
        Response response = GET("/Application/kwcustom_search?keyword={\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"default_field\":\"_all\",\"query\":\"hello\"}}]}}}&page=1&highlight=1&size=10");
        assertIsOk(response);
        String Kwfulltext_search = response.out.toString();
        System.out.println(Kwfulltext_search);
        System.out.println(JSONPath.read(Kwfulltext_search, "$.total"));
        assertEquals("success",JSONPath.read(Kwfulltext_search, "$.msg"));
        assertEquals(200,JSONPath.read(Kwfulltext_search, "$.code"));
        Object test = JSONPath.read(Kwfulltext_search, "$.data.list[0:100000000:1]"); 
        String str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);       
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"default_field\":\"_all\",\"query\":\"hello\"}}]}}}");
        String url = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response_es = sendPost(url,body);
        System.out.println(response_es);
        System.out.println(JSONPath.read(response_es, "$.hits.total"));
        Object test_es = JSONPath.read(response_es, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray[0], "$.url"));
        assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(Kwfulltext_search, "$.total"));
        assertEquals(result_es,result);
   }    
 
    @Test
    //全文检索
    public void testKwfulltext_search() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&page=1&highlight=1&size=10");
        assertIsOk(response);
        String Kwfulltext_search = response.out.toString();
        System.out.println(Kwfulltext_search);
        System.out.println(JSONPath.read(Kwfulltext_search, "$.total"));
        assertEquals("success",JSONPath.read(Kwfulltext_search, "$.msg"));
        assertEquals(200,JSONPath.read(Kwfulltext_search, "$.code"));
        Object test = JSONPath.read(Kwfulltext_search, "$.data.list[0:100000000:1]"); 
        String str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);       
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"query_string\":{\"default_field\":\"_all\",\"query\":\"1234\"}}],\"must_not\":[],\"should\":[]}},\"from\":0,\"size\":10,\"sort\":[],\"aggs\":{}}");
        String url = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response_es = sendPost(url,body);
        System.out.println(response_es);
        System.out.println(JSONPath.read(response_es, "$.hits.total"));
        Object test_es = JSONPath.read(response_es, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray[0], "$.url"));
        assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(Kwfulltext_search, "$.total"));
        assertEquals(result_es,result);
   }  
   
    @Test
    //全文检索 size=0
    public void testKwfulltext_search_size0() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&page=1&highlight=1&size=0");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    }
    @Test
    //全文检索 keyword缺失
    public void testKwfulltext_search_keyword() {
        Response response = GET("/Application/kwfulltext_search?page=1&highlight=1&size=0");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    }   
    @Test
    //全文检索 page缺失
    public void testKwfulltext_search_page() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&highlight=1&size=0");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    }   
    @Test
    //全文检索 highlight缺失
    public void testKwfulltext_search_highlight() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&page=1&size=0");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    } 
    @Test
    //全文检索 size缺失
    public void testKwfulltext_search_size() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&page=1&highlight=1");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    }
    @Test
    //全文检索 keyword为空
    public void testKwfulltext_search_keyword_null() {
        Response response = GET("/Application/kwfulltext_search?keyword=&page=1&highlight=1&size=0");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    }   
    
    @Test
    //全文检索 page为空
    public void testKwfulltext_search_page_null() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&highlight=1&page=&size=0");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    }   
    @Test
    //全文检索 highlight为空
    public void testKwfulltext_search_highlight_null() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&page=1&size=0&highlight=");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    } 
    @Test
    //全文检索 size为空
    public void testKwfulltext_search_size_null() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&page=1&highlight=1&size=");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    }
    
    @Test
    //全文检索 page*size大于10000
    public void testKwfulltext_search_page_1001() {
        Response response = GET("/Application/kwfulltext_search?keyword=1234&highlight=1&page=1001&size=10");
        assertIsOk(response);
        String Kwfulltext_search_size0 = response.out.toString();
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.msg"));
        System.out.println(JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals(404,JSONPath.read(Kwfulltext_search_size0, "$.code"));
        assertEquals("[ERROR]: Result window is too large, from + size must be less than or equal to: [10000]!",JSONPath.read(Kwfulltext_search_size0, "$.msg"));    
    } 

    @Test
  //URL精确匹配
    public void testKwaccurate_search_url() {   	
        Response response_url = GET("/Application/kwaccurate_search?querytype=url&keyword=/dujiao/hello/index&fuzzymatch=0&page=1&highlight=1&size=10");
        assertIsOk(response_url);
        String url = response_url.out.toString();
        System.out.println(url);
        System.out.println(JSONPath.read(url, "$.data.list[0].url"));
        assertEquals("success",JSONPath.read(url, "$.msg"));
        assertEquals(1,JSONPath.read(url, "$.total"));
        assertEquals(200,JSONPath.read(url, "$.code"));
        assertEquals("/dujiao/hello/index",JSONPath.read(url, "$.data.list[0].url"));
    }

    @Test
  //access_url精确匹配
    public void testKwaccurate_search_access_url() {
        Response response_access_url = GET("/Application/kwaccurate_search?querytype=access_url&keyword=http://dev.keepwork.com/liang1/test/html&fuzzymatch=0&page=1&highlight=1&size=10");
        assertIsOk(response_access_url);
        String access_url = response_access_url.out.toString();
        Object test = JSONPath.read(access_url, "$.data.list[0:100000000:1]"); 
        String str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"access_url.keyword\":\"http://dev.keepwork.com/liang1/test/html\"}}]}}}");
        String url = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(access_url, "$.msg"));
        assertEquals(200,JSONPath.read(access_url, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(access_url, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
    @Test
  //data_source_url精确匹配
    public void testKwaccurate_search_data_source_url() {   
        Response response_data_source_url = GET("/Application/kwaccurate_search?querytype=data_source_url&keyword=http://keepwork.com/sangfor123456/kwquestions/detail_interface&fuzzymatch=0&page=1&highlight=1&size=10");
        assertIsOk(response_data_source_url);
        String data_source_url = response_data_source_url.out.toString();
        System.out.println(data_source_url);
        System.out.println(JSONPath.read(data_source_url, "$.total"));
        System.out.println(JSONPath.read(data_source_url, "$.data.list[0].data_source_url"));
        assertEquals("success",JSONPath.read(data_source_url, "$.msg"));
        assertEquals(200,JSONPath.read(data_source_url, "$.code"));
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"data_source_url.keyword\":\"http://keepwork.com/sangfor123456/kwquestions/detail_interface\"}}]}}}");
        String url = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url,body);
        System.out.println(JSONPath.read(response, "$.hits.hits[0]._source.data_source_url"));
        System.out.println(JSONPath.read(response, "$.hits.total"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(data_source_url, "$.total"));
        assertEquals(JSONPath.read(response, "$.hits.hits[0]._source.url"),JSONPath.read(data_source_url, "$.data.list[0].url"));
        assertEquals(JSONPath.read(response, "$.hits.hits[0]._source.data_source_url"),JSONPath.read(data_source_url, "$.data.list[0].data_source_url"));
     }
    @Test
   //tags精确匹配
    public void testKwaccurate_search_tags() {   
          Response response_tags = GET("/Application/kwaccurate_search?querytype=tags&keyword=hello&fuzzymatch=0&page=1&highlight=1&size=10");
          assertIsOk(response_tags);
          String tags = response_tags.out.toString();
          System.out.println(tags);
          System.out.println(JSONPath.read(tags, "$.total"));  
          System.out.println(JSONPath.read(tags, "$.data.list[0].tags"));          
          assertEquals(200,JSONPath.read(tags, "$.code"));
          assertEquals("success",JSONPath.read(tags, "$.msg"));
          assertEquals("<span style=\"color:red\">hello</span> ",JSONPath.read(tags, "$.data.list[0].tags")); 
            String body = new String("{\"query\" : {\"bool\":{\"must\":[{\"term\":{\"tags.keyword\":\"hello\"}}]}}}");
            String url = new String ("http://10.0.1.36:9200/kwindex/_search");
            String response = sendPost(url,body);
            System.out.println(response);
            System.out.println(JSONPath.read(response, "$.hits.total"));
            System.out.println(JSONPath.read(response, "$.hits.total"));
            assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(tags, "$.total"));
            assertEquals(JSONPath.read(response, "$.hits.hits[last]._source.url"),JSONPath.read(tags, "$.data.list[last].url"));
    }  
  @Test
  //user_name精确匹配
  public void testKwaccurate_search_user_name() {   
      Response response = GET("/Application/kwaccurate_search?querytype=user_name&keyword=test&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String user_name = response.out.toString();
      System.out.println(user_name);
      System.out.println(JSONPath.read(user_name, "$.total"));
      System.out.println(JSONPath.read(user_name, "$.data.list[0].user_name"));
      assertEquals(JSONPath.read(user_name, "$.msg"),"success");
      assertEquals(JSONPath.read(user_name, "$.code"),200);
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"user_name.keyword\":\"test\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.user_name"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(user_name, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(user_name, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.data_source_url"),JSONPath.read(user_name, "$.data.list[0].data_source_url"));
   }
  @Test
  //site_name精确匹配
  public void testKwaccurate_search_site_name() {   
      Response response = GET("/Application/kwaccurate_search?querytype=site_name&keyword=test&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String site_name = response.out.toString();
      System.out.println(site_name);
      System.out.println(JSONPath.read(site_name, "$.total"));
      System.out.println(JSONPath.read(site_name, "$.data.list[0].site_name"));
      assertEquals("success",JSONPath.read(site_name, "$.msg"));
      assertEquals(200,JSONPath.read(site_name, "$.code"));
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"site_name.keyword\":\"test\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.site_name"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(site_name, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(site_name, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.site_name"),JSONPath.read(site_name, "$.data.list[0].site_name"));
   }
  @Test
  //page_name精确匹配
  public void testKwaccurate_search_page_name() {   
      Response response = GET("/Application/kwaccurate_search?querytype=page_name&keyword=test&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String page_name = response.out.toString();
      System.out.println(page_name);
      System.out.println(JSONPath.read(page_name, "$.total"));
      System.out.println(JSONPath.read(page_name, "$.data.list[0].page_name"));
      assertEquals("success",JSONPath.read(page_name, "$.msg"));
      assertEquals(200,JSONPath.read(page_name, "$.code"));
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"page_name.keyword\":\"test\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.page_name"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(page_name, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(page_name, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.page_name"),JSONPath.read(page_name, "$.data.list[0].page_name"));
   }
  @Test
  //commit_id精确匹配
  public void testKwaccurate_search_commit_id() {   
      Response response = GET("/Application/kwaccurate_search?querytype=commit_id&keyword=test&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String commit_id = response.out.toString();
      System.out.println(commit_id);
      System.out.println(JSONPath.read(commit_id, "$.total"));
      System.out.println(JSONPath.read(commit_id, "$.data.list[0].commit_id"));
      assertEquals("success",JSONPath.read(commit_id, "$.msg"));
      assertEquals(200,JSONPath.read(commit_id, "$.code"));
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"commit_id.keyword\":\"test\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.commit_id"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(commit_id, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(commit_id, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.commit_id"),JSONPath.read(commit_id, "$.data.list[0].commit_id"));
   }
  @Test
  //extra_data精确匹配
  public void testKwaccurate_search_extra_data() {   
      Response response = GET("/Application/kwaccurate_search?querytype=extra_data&keyword=test_zjx&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String extra_data = response.out.toString();
      System.out.println(extra_data);
      System.out.println(JSONPath.read(extra_data, "$.total"));
      System.out.println(JSONPath.read(extra_data, "$.data.list[0].extra_data"));
      assertEquals("success",JSONPath.read(extra_data, "$.msg"));
      assertEquals(200,JSONPath.read(extra_data, "$.code"));
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"extra_data.keyword\":\"test_zjx\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(response_es);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.extra_data"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(extra_data, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(extra_data, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.extra_data"),JSONPath.read(extra_data, "$.data.list[0].extra_data"));
   }
  @Test
  //extra_search精确匹配
  public void testKwaccurate_search_extra_search() {   
      Response response = GET("/Application/kwaccurate_search?querytype=extra_search&keyword=1233_test&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String extra_search = response.out.toString();
      System.out.println(extra_search);
      System.out.println(JSONPath.read(extra_search, "$.total"));
      System.out.println(JSONPath.read(extra_search, "$.data.list[0].extra_data"));
      assertEquals("success",JSONPath.read(extra_search, "$.msg"));
      assertEquals(200,JSONPath.read(extra_search, "$.code"));
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"extra_search.keyword\":\"1233_test\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(response_es);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.extra_search"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(extra_search, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(extra_search, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.extra_search"),JSONPath.read(extra_search, "$.data.list[0].extra_search"));
   }

  @Test
  //create_time精确匹配
  public void testKwaccurate_search_create_time() {   
      Response response = GET("/Application/kwaccurate_search?querytype=create_time&keyword=2017-08-18 16:11:18&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String create_time = response.out.toString();
      System.out.println(create_time);
      System.out.println(JSONPath.read(create_time, "$.total"));
      System.out.println(JSONPath.read(create_time, "$.data.list[0].extra_data"));
      assertEquals("success",JSONPath.read(create_time, "$.msg"));
      assertEquals(200,JSONPath.read(create_time, "$.code"));
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"create_time\":\"2017-08-18 16:11:18\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(response_es);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.create_time"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(create_time, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(create_time, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.create_time"),JSONPath.read(create_time, "$.data.list[0].create_time"));
   }
 
  @Test
  //update_time精确匹配
  public void testKwaccurate_search_update_time() {   
      Response response = GET("/Application/kwaccurate_search?querytype=update_time&keyword=2017-08-20 16:50:43&fuzzymatch=0&page=1&highlight=1&size=10");
      assertIsOk(response);
      String update_time = response.out.toString();
      System.out.println(update_time);
      System.out.println(JSONPath.read(update_time, "$.total"));
      System.out.println(JSONPath.read(update_time, "$.data.list[0].extra_data"));
      assertEquals("success",JSONPath.read(update_time, "$.msg"));
      assertEquals(200,JSONPath.read(update_time, "$.code"));
      String body = new String("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"update_time\":\"2017-08-20 16:50:43\"}}]}}}");
      String url = new String ("http://10.0.1.36:9200/kwindex/_search");
      String response_es = sendPost(url,body);
      System.out.println(response_es);
      System.out.println(JSONPath.read(response_es, "$.hits.hits[0]._source.update_time"));
      System.out.println(JSONPath.read(response_es, "$.hits.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.total"),JSONPath.read(update_time, "$.total"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.url"),JSONPath.read(update_time, "$.data.list[0].url"));
      assertEquals(JSONPath.read(response_es, "$.hits.hits[0]._source.update_time"),JSONPath.read(update_time, "$.data.list[0].update_time"));
   }

    @Test
    //精确匹配字段querytype缺省
      public void testKwaccurate_search_querytype_miss() {         
          Response response = GET("/Application/kwaccurate_search?keyword=hello&fuzzymatch=0&page=1&highlight=1&size=10");
          assertIsOk(response);
          String miss = response.out.toString();
          System.out.println(miss);
          System.out.println(JSONPath.read(miss, "$.msg"));
          assertEquals(404,JSONPath.read(miss, "$.code"));
          assertEquals("[ERROR] Invalid params!",JSONPath.read(miss, "$.msg")); 
      }
    @Test
    //精确匹配字段keyword缺省
      public void testKwaccurate_search_keyword_miss() {         
          Response response = GET("/Application/kwaccurate_search?querytype=AAA&fuzzymatch=0&page=1&highlight=1&size=10");
          assertIsOk(response);
          String miss = response.out.toString();
          System.out.println(miss);
          System.out.println(JSONPath.read(miss, "$.msg"));
          assertEquals(404,JSONPath.read(miss, "$.code"));
          assertEquals("[ERROR] Invalid params!",JSONPath.read(miss, "$.msg")); 
      }
    @Test
    //精确匹配字段page缺省
      public void testKwaccurate_search_page_miss() {         
          Response response = GET("/Application/kwaccurate_search?querytype=AAA?keyword=hello&fuzzymatch=0&highlight=1&size=10");
          assertIsOk(response);
          String miss = response.out.toString();
          System.out.println(miss);
          System.out.println(JSONPath.read(miss, "$.msg"));
          assertEquals(404,JSONPath.read(miss, "$.code"));
          assertEquals("[ERROR] Invalid params!",JSONPath.read(miss, "$.msg")); 
      }
    @Test
    //精确匹配字段highlight缺省
      public void testKwaccurate_search_highlight_miss() {         
          Response response = GET("/Application/kwaccurate_search?querytype=AAA?keyword=hello&fuzzymatch=0&page=1&size=10");
          assertIsOk(response);
          String miss = response.out.toString();
          System.out.println(miss);
          System.out.println(JSONPath.read(miss, "$.msg"));
          assertEquals(404,JSONPath.read(miss, "$.code"));
          assertEquals("[ERROR] Invalid params!",JSONPath.read(miss, "$.msg")); 
      }
    @Test
    //精确匹配字段size缺省
      public void testKwaccurate_search_size_miss() {         
          Response response = GET("/Application/kwaccurate_search?querytype=AAA?keyword=hello&fuzzymatch=0&page=1&highlight=1");
          assertIsOk(response);
          String miss = response.out.toString();
          System.out.println(miss);
          System.out.println(JSONPath.read(miss, "$.msg"));
          assertEquals(404,JSONPath.read(miss, "$.code"));
          assertEquals("[ERROR] Invalid params!",JSONPath.read(miss, "$.msg")); 
      }
    @Test
    //精确匹配字段fuzzymatch缺省
      public void testKwaccurate_search_fuzzymatch_miss() {         
          Response response = GET("/Application/kwaccurate_search?querytype=AAA?keyword=hello&page=1&highlight=1&size=10");
          assertIsOk(response);
          String miss = response.out.toString();
          System.out.println(miss);
          System.out.println(JSONPath.read(miss, "$.msg"));
          assertEquals(404,JSONPath.read(miss, "$.code"));
          assertEquals("[ERROR] Invalid params!",JSONPath.read(miss, "$.msg")); 
      }
    @Test
  //精确匹配查询字段不存在
    public void testKwaccurate_search_null() {         
        Response response = GET("/Application/kwaccurate_search?querytype=AAA&keyword=hello&fuzzymatch=0&page=1&highlight=1&size=10");
        assertIsOk(response);
        String querytype_not_exit = response.out.toString();
        System.out.println(querytype_not_exit);
        System.out.println(JSONPath.read(querytype_not_exit, "$.msg"));
        assertEquals(404,JSONPath.read(querytype_not_exit, "$.code"));
        assertEquals("This querytype does not support !",JSONPath.read(querytype_not_exit, "$.msg")); 
    }

    @Test
  //querytype == null
    public void testKwaccurate_search_querytype_null() {         
        Response response = GET("/Application/kwaccurate_search?querytype=&keyword=hello&fuzzymatch=0&page=1&highlight=1&size=10");
        assertIsOk(response);
        String querytype_null = response.out.toString();
        System.out.println(querytype_null);
        System.out.println(JSONPath.read(querytype_null, "$.msg"));
        assertEquals(404,JSONPath.read(querytype_null, "$.code"));
        assertEquals("This querytype does not support !",JSONPath.read(querytype_null, "$.msg"));  
    }
    @Test
  //keyword == null
    public void testKwaccurate_search_keyword_null() {         
        Response response = GET("/Application/kwaccurate_search?querytype=url&keyword=&fuzzymatch=0&page=1&highlight=1&size=10");
        assertIsOk(response);
        String keyword_null = response.out.toString();
        System.out.println(keyword_null);
        System.out.println(JSONPath.read(keyword_null, "$.msg"));
        assertEquals(404,JSONPath.read(keyword_null, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(keyword_null, "$.msg"));  
    }
    
    @Test
  //fuzzymatch == null
    public void testKwaccurate_search_fuzzymatch_null() {         
        Response response = GET("/Application/kwaccurate_search?querytype=url&keyword=&fuzzymatch=&page=1&highlight=1&size=10");
        assertIsOk(response);
        String fuzzymatch_null = response.out.toString();
        System.out.println(fuzzymatch_null);
        System.out.println(JSONPath.read(fuzzymatch_null, "$.msg"));
        assertEquals(404,JSONPath.read(fuzzymatch_null, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(fuzzymatch_null, "$.msg"));  
    } 
    @Test
  //page == null
    public void testKwaccurate_search_page_null() {         
        Response response = GET("/Application/kwaccurate_search?querytype=url&keyword=&fuzzymatch=1&page=&highlight=1&size=10");
        assertIsOk(response);
        String page_null = response.out.toString();
        System.out.println(page_null);
        System.out.println(JSONPath.read(page_null, "$.msg"));
        assertEquals(404,JSONPath.read(page_null, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(page_null, "$.msg"));  
    } 
    @Test
  //highlight == null
    public void testKwaccurate_search_highlight_null() {         
        Response response = GET("/Application/kwaccurate_search?querytype=url&keyword=&fuzzymatch=1&page=1&highlight=&size=10");
        assertIsOk(response);
        String highlight_null = response.out.toString();
        System.out.println(highlight_null);
        System.out.println(JSONPath.read(highlight_null, "$.msg"));
        assertEquals(404,JSONPath.read(highlight_null, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(highlight_null, "$.msg"));  
    } 
    @Test
  //size == null
    public void testKwaccurate_search_size_null() {         
        Response response = GET("/Application/kwaccurate_search?querytype=url&keyword=&fuzzymatch=1&page=1&highlight=&size=");
        assertIsOk(response);
        String size_null = response.out.toString();
        System.out.println(size_null);
        System.out.println(JSONPath.read(size_null, "$.msg"));
        assertEquals(404,JSONPath.read(size_null, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(size_null, "$.msg"));  
    }   
    @Test
  //size==0
    public void testKwaccurate_search_size0() {       
        Response response_size0 = GET("/Application/kwaccurate_search?querytype=tags&keyword=hello&fuzzymatch=0&page=1&highlight=1&size=0");
        assertIsOk(response_size0);
        String size0 = response_size0.out.toString();
        System.out.println(size0);
        System.out.println(JSONPath.read(size0, "$.msg"));
        assertEquals(404,JSONPath.read(size0, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(size0, "$.msg")); 
    }
    @Test
  //fuzzymatch < 0
    public void testKwaccurate_search_fuzzymatch_little() {           
        Response response_fuzzymatch_nevigate = GET("/Application/kwaccurate_search?querytype=tags&keyword=hello&fuzzymatch=-1&page=1&highlight=1&size=10");
        assertIsOk(response_fuzzymatch_nevigate);
        String fuzzymatch_nevigate = response_fuzzymatch_nevigate.out.toString();
        System.out.println(fuzzymatch_nevigate);
        System.out.println(JSONPath.read(fuzzymatch_nevigate, "$.msg"));
        assertEquals(404,JSONPath.read(fuzzymatch_nevigate, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(fuzzymatch_nevigate, "$.msg"));
    }
    @Test
  //fuzzymatch > 2
    public void testKwaccurate_search_fuzzymatch_big() {     
        Response response_fuzzymatch_2 = GET("/Application/kwaccurate_search?querytype=tags&keyword=hello&fuzzymatch=3&page=1&highlight=1&size=10");
        assertIsOk(response_fuzzymatch_2);
        String fuzzymatch_2 = response_fuzzymatch_2.out.toString();
        System.out.println(fuzzymatch_2);
        System.out.println(JSONPath.read(fuzzymatch_2, "$.msg"));
        assertEquals(404,JSONPath.read(fuzzymatch_2, "$.code"));
        assertEquals("[ERROR] Invalid params!",JSONPath.read(fuzzymatch_2, "$.msg"));
    }
    
    @Test
  //querytype == creat_time && fuzzymatch ==1
    public void testKwaccurate_search_creat_time1() {            
        Response response_creat_time_1 = GET("/Application/kwaccurate_search?querytype=create_time&keyword=2017&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_creat_time_1);
        String creat_time_1 = response_creat_time_1.out.toString();
        System.out.println(creat_time_1);
        System.out.println(JSONPath.read(creat_time_1, "$.msg"));
        assertEquals(404,JSONPath.read(creat_time_1, "$.code"));
        assertEquals("The time type does not support fuzzy matching and word divided matching !",JSONPath.read(creat_time_1, "$.msg"));
    }
    @Test
  //querytype == creat_time && fuzzymatch ==2
    public void testKwaccurate_search_creat_time2() {        
        Response response_creat_time_2 = GET("/Application/kwaccurate_search?querytype=create_time&keyword=2017&fuzzymatch=2&page=1&highlight=1&size=10");
        assertIsOk(response_creat_time_2);
        String creat_time_2 = response_creat_time_2.out.toString();
        System.out.println(creat_time_2);
        System.out.println(JSONPath.read(creat_time_2, "$.msg"));
        assertEquals(404,JSONPath.read(creat_time_2, "$.code"));
        assertEquals("The time type does not support fuzzy matching and word divided matching !",JSONPath.read(creat_time_2, "$.msg"));       
    }
    @Test
 //querytype == update_time && fuzzymatch ==1
    public void testKwaccurate_search_update_time1() {   
        Response response_update_time_1 = GET("/Application/kwaccurate_search?querytype=update_time&keyword=2017&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_update_time_1);
        String update_time_1 = response_update_time_1.out.toString();
        System.out.println(update_time_1);
        System.out.println(JSONPath.read(update_time_1, "$.msg"));
        assertEquals(404,JSONPath.read(update_time_1, "$.code"));
        assertEquals("The time type does not support fuzzy matching and word divided matching !",JSONPath.read(update_time_1, "$.msg"));
    }
    @Test
  //querytype == update_time && fuzzymatch ==2
    public void testKwaccurate_search_update_time2() {          
        Response response_update_time_2 = GET("/Application/kwaccurate_search?querytype=update_time&keyword=2017&fuzzymatch=2&page=1&highlight=1&size=10");
        assertIsOk(response_update_time_2);
        String update_time_2 = response_update_time_2.out.toString();
        System.out.println(update_time_2);
        System.out.println(JSONPath.read(update_time_2, "$.msg"));
        assertEquals(404,JSONPath.read(update_time_2, "$.code"));
        assertEquals("The time type does not support fuzzy matching and word divided matching !",JSONPath.read(update_time_2, "$.msg"));       
        
    }

    @Test
  //url模糊匹配
    public void testKwaccurate_search_url_fuzzy() {
        Response response_url = GET("/Application/kwaccurate_search?querytype=url&keyword=*hello*&fuzzymatch=1&page=1&highlight=0&size=10");
        assertIsOk(response_url);
        String url = response_url.out.toString();
        Object test = JSONPath.read(url, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"url.keyword\":\"*hello*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(url, "$.msg"));
        assertEquals(200,JSONPath.read(url, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(url, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
 

    @Test
  //access_url模糊匹配
    public void testKwaccurate_search_access_url_fuzzy() {
        Response response_access_url = GET("/Application/kwaccurate_search?querytype=access_url&keyword=*baidu*&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_access_url);
        String access_url = response_access_url.out.toString();
        Object test = JSONPath.read(access_url, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"access_url.keyword\":\"*baidu*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals(JSONPath.read(access_url, "$.msg"),"success");
        assertEquals(JSONPath.read(access_url, "$.code"),200);
        assertEquals(JSONPath.read(access_url, "$.total"),JSONPath.read(response, "$.hits.total"));
        assertEquals(JSONPath.read(strArray[0], "$.content").toString(),JSONPath.read(strArray_es[0], "$._source.content").toString());
        assertEquals(result,result_es);
  }
    

    @Test
  //access_url模糊匹配
    public void testKwaccurate_search_data_source_url_fuzzy() {
        Response response_data_source_url = GET("/Application/kwaccurate_search?querytype=data_source_url&keyword=*keepwork*&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_data_source_url);
        String data_source_url = response_data_source_url.out.toString();
        Object test = JSONPath.read(data_source_url, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"data_source_url.keyword\":\"*keepwork*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(data_source_url, "$.msg"));
        assertEquals(200,JSONPath.read(data_source_url, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(data_source_url, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
    

    @Test
  //user_name模糊匹配
    public void testKwaccurate_search_user_name_fuzzy() {
        Response response_user_name = GET("/Application/kwaccurate_search?querytype=user_name&keyword=?est*&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_user_name);
        String user_name = response_user_name.out.toString();
        Object test = JSONPath.read(user_name, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"user_name.keyword\":\"?est*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(user_name, "$.msg"));
        assertEquals(200,JSONPath.read(user_name, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(user_name, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
   

    @Test
  //site_name模糊匹配
    public void testKwaccurate_search_site_name_fuzzy() {
        Response response_site_name = GET("/Application/kwaccurate_search?querytype=site_name&keyword=*zjx*&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_site_name);
        String site_name = response_site_name.out.toString();
        Object test = JSONPath.read(site_name, "$.data.list[0:100000000:1]");
        String str= null; 
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"site_name.keyword\":\"*zjx*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(site_name, "$.msg"));
        assertEquals(200,JSONPath.read(site_name, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(site_name, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }

    @Test
  //page_name模糊匹配
    public void testKwaccurate_search_page_name_fuzzy() {
        Response response_page_name = GET("/Application/kwaccurate_search?querytype=page_name&keyword=*zjx*&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_page_name);
        String page_name = response_page_name.out.toString();
        Object test = JSONPath.read(page_name, "$.data.list[0:100000000:1]");
        String str= null; 
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"page_name.keyword\":\"*zjx*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(page_name, "$.msg"));
        assertEquals(200,JSONPath.read(page_name, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(page_name, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }

    @Test
  //commit_id模糊匹配
    public void testKwaccurate_search_commit_id_fuzzy() {
        Response response_commit_id = GET("/Application/kwaccurate_search?querytype=commit_id&keyword=*zjx*&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_commit_id);
        String commit_id = response_commit_id.out.toString();
        Object test = JSONPath.read(commit_id, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"commit_id.keyword\":\"*zjx*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(commit_id, "$.msg"));
        assertEquals(200,JSONPath.read(commit_id, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(commit_id, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
   
    @Test
  //extra_data模糊匹配
    public void testKwaccurate_search_extra_data_fuzzy() {
        Response response_extra_data = GET("/Application/kwaccurate_search?querytype=extra_data&keyword=*test_zj?&fuzzymatch=1&page=1&highlight=1&size=10");
        assertIsOk(response_extra_data);
        String extra_data = response_extra_data.out.toString();
        Object test = JSONPath.read(extra_data, "$.data.list[0:100000000:1]");
        String str= null; 
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"extra_data.keyword\":\"*test_zj?\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(extra_data, "$.msg"));
        assertEquals(200,JSONPath.read(extra_data, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(extra_data, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
 	
    @Test
  //extra_search模糊匹配
    public void testKwaccurate_search_extra_search_fuzzy() {
        Response response_extra_search = GET("/Application/kwaccurate_search?querytype=extra_search&keyword=*123*&fuzzymatch=1&page=1&highlight=0&size=10");
        assertIsOk(response_extra_search);
        String extra_search = response_extra_search.out.toString();
        Object test = JSONPath.read(extra_search, "$.data.list[0:100000000:1]");
        String str= null; 
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"extra_search.keyword\":\"*123*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(extra_search, "$.msg"));
        assertEquals(200,JSONPath.read(extra_search, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(extra_search, "$.total"));
        assertEquals(result_es,result);
  }


    @Test
  //tags模糊匹配
    public void testKwaccurate_search_tags_fuzzy() {
        Response response_tags = GET("/Application/kwaccurate_search?querytype=tags&keyword=*hello*&fuzzymatch=1&page=1&highlight=0&size=10");
        assertIsOk(response_tags);
        String tags = response_tags.out.toString();
        Object test = JSONPath.read(tags, "$.data.list[0:100000000:1]");
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"tags.keyword\":\"*hello*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        assertEquals("success",JSONPath.read(tags, "$.msg"));
        assertEquals(200,JSONPath.read(tags, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(tags, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }

    @Test
  //content模糊匹配
    public void testKwaccurate_search_content_fuzzy() {
        Response response_content = GET("/Application/kwaccurate_search?querytype=content&keyword=*%E5%91%A8%E4%BA%94*&fuzzymatch=1&page=1&highlight=0&size=10");
        assertIsOk(response_content);
        String content = response_content.out.toString();
        Object test = JSONPath.read(content, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        System.out.println(content);
        String body = new String("{\"query\":{\"bool\":{\"must\":[{\"wildcard\":{\"content.keyword\":\"*周五*\"}}]}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.url"));
        System.out.println(JSONPath.read(content, "$.total"));
        assertEquals("success",JSONPath.read(content, "$.msg"));
        assertEquals(200,JSONPath.read(content, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(content, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
    @Test
  //url 分词检索
    public void testKwaccurate_search_url_fuzzy2() {
        Response response_url = GET("/Application/kwaccurate_search?querytype=url&keyword=new&fuzzymatch=2&page=1&highlight=1&size=10");
        assertIsOk(response_url);
        String url = response_url.out.toString();
        Object test = JSONPath.read(url, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
        System.out.println(strArray[0]);
        System.out.println(result);
        String body = new String("{ \"query\": {\"match\" : {\"url\" : {\"query\" : \"new\",\"operator\" : \"or\"}}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
        System.out.println(result_es);
        System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source..url"));
        assertEquals("success",JSONPath.read(url, "$.msg"));
        assertEquals(200,JSONPath.read(url, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(url, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }

    @Test
  //access_url 分词检索
    public void testKwaccurate_search_access_url_fuzzy2() {
        Response response_access_url = GET("/Application/kwaccurate_search?querytype=access_url&keyword=baidu&fuzzymatch=2&page=1&highlight=1&size=10");
        assertIsOk(response_access_url);
        String access_url = response_access_url.out.toString();
        Object test = JSONPath.read(access_url, "$.data.list[0:100000000:1]"); 
        String str= null;
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
     //   System.out.println(strArray[0]);
        System.out.println(result);
        System.out.println(JSONPath.read(strArray[0], "$.access_url").toString());
        String body = new String("{ \"query\": {\"match\" : {\"access_url\" : {\"query\" : \"baidu\",\"operator\" : \"or\"}}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
      //  System.out.println(result_es);
      //  System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.access_url"));
        assertEquals("success",JSONPath.read(access_url, "$.msg"));
        assertEquals(200,JSONPath.read(access_url, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(access_url, "$.total"));
     //   assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
     //   assertEquals(result_es,result);
  }
 
    @Test
  //data_source_url 分词检索
    public void testKwaccurate_search_data_source_url_fuzzy2() {
        Response response_data_source_url = GET("/Application/kwaccurate_search?querytype=data_source_url&keyword=keepwork&fuzzymatch=2&page=1&highlight=1&size=10");
        assertIsOk(response_data_source_url);
        String data_source_url = response_data_source_url.out.toString();
        Object test = JSONPath.read(data_source_url, "$.data.list[0:100000000:1]");
        String str= null; 
        str = test.toString();
        List<String> list = new ArrayList<String>();
        list.add(str);
        String[] strArray= list.toArray(new String[list.size()]);
        String result = JSONPath.read(strArray[0], "$.url").toString();
     //   System.out.println(strArray[0]);
        System.out.println(result);
        System.out.println(JSONPath.read(strArray[0], "$.data_source_url").toString());
        String body = new String("{ \"query\": {\"match\" : {\"data_source_url\" : {\"query\" : \"keepwork\",\"operator\" : \"or\"}}}}");
        String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
        String response = sendPost(url_request,body);
        Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
        String str_es = test_es.toString();
        List<String> list_es = new ArrayList<String>();
        list_es.add(str_es);
        String[] strArray_es= list_es.toArray(new String[list_es.size()]);
        String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
      //  System.out.println(result_es);
      //  System.out.println(strArray_es[0]);
        System.out.println(JSONPath.read(strArray_es[0], "$._source.data_source_url"));
        assertEquals("success",JSONPath.read(data_source_url, "$.msg"));
        assertEquals(200,JSONPath.read(data_source_url, "$.code"));
        assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(data_source_url, "$.total"));
        assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
        assertEquals(result_es,result);
  }
     
	   @Test
	   //tags 分词检索
	     public void testKwaccurate_search_tags_fuzzy2() {
	         Response response_tags = GET("/Application/kwaccurate_search?querytype=tags&keyword=hello&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_tags);
	         String tags = response_tags.out.toString();
	         Object test = JSONPath.read(tags, "$.data.list[0:100000000:1]");
	         String str= null; 
	         str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(strArray[0]);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.tags").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"tags\" : {\"query\" : \"hello\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	         System.out.println(result_es);
	         System.out.println(strArray_es[0]);
	         System.out.println(JSONPath.read(strArray_es[0], "$._source.tags"));
	         assertEquals("success",JSONPath.read(tags, "$.msg"));
	         assertEquals(200,JSONPath.read(tags, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(tags, "$.total"));
	         assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
	         assertEquals(result_es,result);
	   }

	   @Test
	   //content 分词检索
	     public void testKwaccurate_search_content_fuzzy2() {
	         Response response_content = GET("/Application/kwaccurate_search?querytype=content&keyword=%E5%91%A8%E4%BA%94&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_content);
	         String content = response_content.out.toString();
	         Object test = JSONPath.read(content, "$.data.list[0:100000000:1]"); 
	         String str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(content);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.content").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"content\" : {\"query\" : \"周五\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	         System.out.println(result_es);
	         System.out.println(strArray_es[0]);
	         System.out.println(JSONPath.read(strArray_es[0], "$._source.content"));
	         assertEquals("success",JSONPath.read(content, "$.msg"));
	         assertEquals(200,JSONPath.read(content, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(content, "$.total"));
	     //    assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
	    //     assertEquals(result_es,result);
	   }
	
	   @Test
	   //user_name 分词检索
	     public void testKwaccurate_search_user_name_fuzzy2() {
	         Response response_user_name = GET("/Application/kwaccurate_search?querytype=user_name&keyword=zjx&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_user_name);
	         String user_name = response_user_name.out.toString();
	         Object test = JSONPath.read(user_name, "$.data.list[0:100000000:1]"); 
	         String str= null;
	         str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(user_name);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.content").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"user_name\" : {\"query\" : \"zjx\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	       //  System.out.println(result_es);
	       //  System.out.println(strArray_es[0]);
	      //   System.out.println(JSONPath.read(strArray_es[0], "$._source.content"));
	         assertEquals("success",JSONPath.read(user_name, "$.msg"));
	         assertEquals(200,JSONPath.read(user_name, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(user_name, "$.total"));
	         assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
	         assertEquals(result_es,result);
	   }   

	   @Test
	   //site_name 分词检索
	     public void testKwaccurate_search_site_name_fuzzy2() {
	         Response response_site_name = GET("/Application/kwaccurate_search?querytype=site_name&keyword=zjx&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_site_name);
	         String site_name = response_site_name.out.toString();
	         Object test = JSONPath.read(site_name, "$.data.list[0:100000000:1]");
	         String str= new String(); 
	         str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(site_name);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.content").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"site_name\" : {\"query\" : \"zjx\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	       //  System.out.println(result_es);
	       //  System.out.println(strArray_es[0]);
	      //   System.out.println(JSONPath.read(strArray_es[0], "$._source.content"));
	         assertEquals("success",JSONPath.read(site_name, "$.msg"));
	         assertEquals(200,JSONPath.read(site_name, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(site_name, "$.total"));
	         assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
	         assertEquals(result_es,result);
	   } 
	 
	   @Test
	   //page_name 分词检索
	     public void testKwaccurate_search_page_name_fuzzy2() {
	         Response response_page_name = GET("/Application/kwaccurate_search?querytype=page_name&keyword=zjx&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_page_name);
	         String page_name = response_page_name.out.toString();
	         Object test = JSONPath.read(page_name, "$.data.list[0:100000000:1]"); 
	         String str= null;
	         str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(page_name);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.content").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"page_name\" : {\"query\" : \"zjx\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	         System.out.println(result_es);
	         System.out.println(strArray_es[0]);
	         System.out.println(JSONPath.read(strArray_es[0], "$._source.content"));
	         assertEquals("success",JSONPath.read(page_name, "$.msg"));
	         assertEquals(200,JSONPath.read(page_name, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(page_name, "$.total"));
	         assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
	         assertEquals(result_es,result);
	   } 
	  
	   @Test
	   //commit_id 分词检索
	     public void testKwaccurate_search_commit_id_fuzzy2() {
	         Response response_commit_id = GET("/Application/kwaccurate_search?querytype=commit_id&keyword=zjx&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_commit_id);
	         String commit_id = response_commit_id.out.toString();
	         Object test = JSONPath.read(commit_id, "$.data.list[0:100000000:1]"); 
	         String str= null;
	         str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(commit_id);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.content").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"commit_id\" : {\"query\" : \"zjx\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	         System.out.println(result_es);
	         System.out.println(strArray_es[0]);
	         System.out.println(JSONPath.read(strArray_es[0], "$._source.content"));
	         assertEquals("success",JSONPath.read(commit_id, "$.msg"));
	         assertEquals(200,JSONPath.read(commit_id, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(commit_id, "$.total"));
	         assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
	         assertEquals(result_es,result);
	   }
	   
	   @Test
	   //extra_data 分词检索
	     public void testKwaccurate_search_extra_data_fuzzy2() {
	         Response response_extra_data = GET("/Application/kwaccurate_search?querytype=extra_data&keyword=zjx&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_extra_data);
	         String extra_data = response_extra_data.out.toString();
	         Object test = JSONPath.read(extra_data, "$.data.list[0:100000000:1]"); 
	         String str= null;
	         str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(extra_data);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.content").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"extra_data\" : {\"query\" : \"zjx\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	         System.out.println(result_es);
	         System.out.println(strArray_es[0]);
	         System.out.println(JSONPath.read(strArray_es[0], "$._source.content"));
	         assertEquals("success",JSONPath.read(extra_data, "$.msg"));
	         assertEquals(200,JSONPath.read(extra_data, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(extra_data, "$.total"));
	         assertEquals(JSONPath.read(strArray_es[0], "$._source.content").toString(),JSONPath.read(strArray[0], "$.content").toString());
	         assertEquals(result_es,result);
	   }  
	 
	   @Test
	   //extra_search 分词检索
	     public void testKwaccurate_search_extra_search_fuzzy2() {
	         Response response_extra_search = GET("/Application/kwaccurate_search?querytype=extra_search&keyword=test&fuzzymatch=2&page=1&highlight=0&size=10");
	         assertIsOk(response_extra_search);
	         String extra_search = response_extra_search.out.toString();
	         Object test = JSONPath.read(extra_search, "$.data.list[0:100000000:1]"); 
	         String str= null;
	         str = test.toString();
	         List<String> list = new ArrayList<String>();
	         list.add(str);
	         String[] strArray= list.toArray(new String[list.size()]);
	         String result = JSONPath.read(strArray[0], "$.url").toString();
	         System.out.println(extra_search);
	         System.out.println(result);
	         System.out.println(JSONPath.read(strArray[0], "$.content").toString());
	         String body = new String("{ \"query\": {\"match\" : {\"extra_search\" : {\"query\" : \"test\",\"operator\" : \"or\"}}}}");
	         String url_request = new String ("http://10.0.1.36:9200/kwindex/_search");
	         String response = sendPost(url_request,body);
	         Object test_es = JSONPath.read(response, "$.hits.hits[0:100000000:1]");      
	         String str_es = test_es.toString();
	         List<String> list_es = new ArrayList<String>();
	         list_es.add(str_es);
	         String[] strArray_es= list_es.toArray(new String[list_es.size()]);
	         String result_es = JSONPath.read(strArray_es[0], "$._source.url").toString();
	         System.out.println(result_es);
	         System.out.println(strArray_es[0]);
	         System.out.println(JSONPath.read(strArray_es[0], "$._source.content"));
	         assertEquals("success",JSONPath.read(extra_search, "$.msg"));
	         assertEquals(200,JSONPath.read(extra_search, "$.code"));
	         assertEquals(JSONPath.read(response, "$.hits.total"),JSONPath.read(extra_search, "$.total"));
	         assertEquals(result_es,result);
	   }   

public static void httpPostProess(){
	try {
		    HttpClient httpClient = HttpClientBuilder.create().build();
		    HttpPost postRequest = new HttpPost("http://10.0.1.36:9200/kwindex/kwtype/_search ");
//		    StringEntity params =new StringEntity("{ "
//		    		+ "\"query\" : {"
//		    		+ "\"bool\":{"
//		    		+ "\"must\":[{\"term\":{\"tags.keyword\":\"hello\"}}]"
//		    		+ "}"
//		    		+ "}"
//		    		+ "}");
//		    System.out.println("resultJsonobject:  "+ params.toString());
//		    postRequest.setEntity(params);
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

public  String sendPost(String url, String data) {
    String response = null;
//    log.info("url: " + url);
//    log.info("request: " + data);
    try {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse httpresponse = null;
        try {
            httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(url);
            StringEntity stringentity = new StringEntity(data,
                    ContentType.create("text/json", "UTF-8"));
            httppost.setEntity(stringentity);
            httpresponse = httpclient.execute(httppost);
            response = EntityUtils
                    .toString(httpresponse.getEntity());
 //           log.info("response: " + response);
        } finally {
            if (httpclient != null) {
                httpclient.close();
            }
            if (httpresponse != null) {
                httpresponse.close();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return response;
}

}
