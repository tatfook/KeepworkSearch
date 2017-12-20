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
 * 功能：分词频度最高的五组词,返回五组词，空格相连
 * 作者：lhy
 * 时间：2017年8月17日
 */
public class WordDegrees {
    public String WordProcessing(String str){
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
	    String temp ;
	    List<Map.Entry<String,Integer>> list=new ArrayList<>();   
	    list.addAll(map.entrySet());  
	    WordDegrees.ValueComparator vc=new ValueComparator();  
	    Collections.sort(list,vc);      //分词频度的排序
	    int num = 0;
	    String key="";    //存放五组分词的字符串
	    Iterator<Map.Entry<String, Integer>> it = list.iterator();
	    while (it.hasNext()) {
	       if(num == 5){
	    	   break;
	       }
	       Map.Entry<String, Integer> entry = it.next();
//	       System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
	       key +=entry.getKey().toString()+" ";
	       num++;
	   }
	    return key;     //返回频度最高的五组词
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
