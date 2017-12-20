package com.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ansj.util.MyStaticValue;
import com.test.ToAnalysis;
import com.wordcount.SameStringCount;

public class AmbiguityTest {
	public static void main(String[] args) {
		// 习近平 习近平 nr
		// 李民工作 李民 nr 工作 vn
		// 三个和尚 三个 m 和尚 n
		// 的确定不 的确 d 定 v 不 v
		// 大和尚 大 a 和尚 n
		// 张三和 张三 nr 和 c
		// 动漫游戏 动漫 n 游戏 n
		// 邓颖超生前 邓颖超 nr 生前 t
	//	System.out.println(ToAnalysis.parse("学习近平和李克强将称为一种时尚!"));
	//	System.out.println(ToAnalysis.parse("李民工作了一天!"));
//		System.out.println(ToAnalysis.parse("三个和尚抬水喝!"));
//		System.out.println(ToAnalysis.parse("我想说,这事的确定不下来,我得想!"));
//		System.out.println(ToAnalysis.parse("小和尚剃了一个和大和尚一样的和尚头"));
//		System.out.println(ToAnalysis.parse("我喜欢玩动漫游戏"));
//		System.out.println(ToAnalysis.parse("邓颖超生前最喜欢的一个"));
		
		MyStaticValue.isNumRecognition = true ;
		MyStaticValue.isQuantifierRecognition = false ;
		
	//	System.out.println(ToAnalysis.parse("365亿个日日夜夜"));
//		String str = "我是一个边学习边分享的人。关于读书、关于影视剧、关于足球， 所学，所思，所感，所闻，分享一切有趣的、有用的。";
		String str = "《热爱生命》,可以说是汪国真的代表作之一,这首诗以四个肯定的回答表达出为何要热爱生命的哲理.四个段落,看似相似,却各有其趣.四个段落分别以“成功”、“爱情”、“奋斗历程”和“未来”为意象进行分析和回答.这四个意象可以说是包括汪国真、席慕容在内的一些清新哲理派诗人惯用的几个意象,不晦涩,不故弄玄虚,不生僻难解,可以说是完全区别于朦胧诗的特点,也是汪国真的诗歌取得成功之原因所在.";
		String[]ss;
		String s = ToAnalysis.parse(str).toString(); 
		ss = s.split(",");
		SameStringCount Count = new SameStringCount();
		
//	    String regex = "([\u4e00-\u9fa5]+)";    // 匹配一个以上中文的正则表达式
		String regex = "([\u4e00-\u9fa5]+){2,10}";    //匹配两个以上中文的正则表达式
		for(int i=0; i<ss.length; i++){
			boolean flag = match(regex, ss[i].toString());
			if(flag){
				Count.hashInsert(ss[i]);   //添加该分词
	//			System.out.println(ss[i]);
			}
		}
		
		HashMap map = Count.getHashMap();
		
		HashMap<String, Integer> news = new HashMap<String,Integer>();      //定义一个新的哈希图来保存分词
//	    Iterator  it = map.keySet().iterator();
	    String temp ;
	    List<Map.Entry<String,Integer>> list=new ArrayList<>();   
	    list.addAll(map.entrySet());  
	    AmbiguityTest.ValueComparator vc=new ValueComparator();  
	    Collections.sort(list,vc);  
	    int num = 0;
	    String key="";
	    Iterator<Map.Entry<String, Integer>> it = list.iterator();
	    while (it.hasNext()) {
	       if(num == 5){
	    	   break;
	       }
	       Map.Entry<String, Integer> entry = it.next();
	       System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
	       news.put(entry.getKey(), entry.getValue());
	       key +=entry.getKey().toString()+" ";
	       num++;
	   }
//	    for(Iterator<Map.Entry<String,Integer>> it=list.iterator();num<5;num++)  
//	    {  
////	         news.put(it.next().getKey().toString(), Integer.valueOf(it.next().getValue()));
//	    	 System.out.println(it.next());
//	    }  
	    System.out.println(map); 
	    System.out.println(key); 
	//    System.out.println(news);
	}
	
	//正则表达式的判定
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
    }
	
	 private static class ValueComparator implements Comparator<Map.Entry<String,Integer>> {  
	    public int compare(Map.Entry<String,Integer> m,Map.Entry<String,Integer> n){  
		      return n.getValue()-m.getValue();  
		 }  
	 }  
}
