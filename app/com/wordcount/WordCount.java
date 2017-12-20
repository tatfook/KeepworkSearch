package com.wordcount;

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
 * ���ܣ��ִ�Ƶ����ߵ�����ʣ��Ͷ�Ӧ��Ƶ��
 * ���ߣ�lhy
 * ʱ�䣺2017��8��17��
 */
public class WordCount {
    public HashMap<String , Integer> getGarticiples(String str){
		MyStaticValue.isNumRecognition = true ;
		MyStaticValue.isQuantifierRecognition = false ;
		String[]ss;
		String s = ToAnalysis.parse(str).toString(); 
		ss = s.split(",");
		SameStringCount Count = new SameStringCount();
		
		String regex = "([\u4e00-\u9fa5]+){2,10}";    //ƥ�������������ĵ�������ʽ
		for(int i=0; i<ss.length; i++){
			boolean flag = match(regex, ss[i].toString());
			if(flag){
				Count.hashInsert(ss[i]);   //��Ӹ÷ִ�
			}
		}
		
		HashMap map = Count.getHashMap();
		HashMap<String, Integer> news = new HashMap<String,Integer>();      //����һ���µĹ�ϣͼ������ִ�
	    String temp ;
	    List<Map.Entry<String,Integer>> list=new ArrayList<>();   
	    list.addAll(map.entrySet());  
	    WordCount.ValueComparator vc=new ValueComparator();  
	    Collections.sort(list,vc);      //�ִ�Ƶ�ȵ�����
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
	    return news;     //����Ƶ����ߵ������
    }
    
	//������ʽ���ж�
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
