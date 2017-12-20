package org.ansj.person.add;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ansj.splitWord.analysis.ToAnalysis;
import org.ansj.util.MyStaticValue;

/*
 * 功能：分词频度最高的五组词，和对应的频度
 * 作者：lhy
 * 时间：2017年8月17日
 */
public class WordCount {
    public HashMap<String , Integer> getGarticiples(String str){
		MyStaticValue.isNumRecognition = true ;
		MyStaticValue.isQuantifierRecognition = false ;
		String[]ss;
		String s = ToAnalysis.parse(str).toString(); 
		ss = s.split(",");
		SameStringCount Count = new SameStringCount();
		
		String regex = "([\u4e00-\u9fa5]+){2,10}";    //匹配两个以上中文的正则表达式
		for(int i=0; i<ss.length; i++){
			boolean flag = match(regex, ss[i].toString());
			if(flag){
				Count.hashInsert(ss[i]);   //添加该分词
			}
		}
		
		HashMap map = Count.getHashMap();
		HashMap<String, Integer> news = new HashMap<String,Integer>();      //定义一个新的哈希图来保存分词
	    String temp ;
	    List<Map.Entry<String,Integer>> list=new ArrayList<>();   
	    list.addAll(map.entrySet());  
	    WordCount.ValueComparator vc=new ValueComparator();  
	    Collections.sort(list,vc);      //分词频度的排序
	    int num = 0;
	    Iterator<Map.Entry<String, Integer>> it = list.iterator();
	    while (it.hasNext()) {
	       if(num == 5){
	    	   break;
	       }
	       Map.Entry<String, Integer> entry = it.next();
//	       System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
	       news.put(entry.getKey(), entry.getValue());
	       num++;
	   }
	    return news;     //返回频度最高的五组词
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
