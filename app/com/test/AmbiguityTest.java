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
		// ϰ��ƽ ϰ��ƽ nr
		// ������ ���� nr ���� vn
		// �������� ���� m ���� n
		// ��ȷ���� ��ȷ d �� v �� v
		// ����� �� a ���� n
		// ������ ���� nr �� c
		// ������Ϸ ���� n ��Ϸ n
		// ��ӱ����ǰ ��ӱ�� nr ��ǰ t
	//	System.out.println(ToAnalysis.parse("ѧϰ��ƽ�����ǿ����Ϊһ��ʱ��!"));
	//	System.out.println(ToAnalysis.parse("��������һ��!"));
//		System.out.println(ToAnalysis.parse("��������̧ˮ��!"));
//		System.out.println(ToAnalysis.parse("����˵,���µ�ȷ��������,�ҵ���!"));
//		System.out.println(ToAnalysis.parse("С��������һ���ʹ����һ���ĺ���ͷ"));
//		System.out.println(ToAnalysis.parse("��ϲ���涯����Ϸ"));
//		System.out.println(ToAnalysis.parse("��ӱ����ǰ��ϲ����һ��"));
		
		MyStaticValue.isNumRecognition = true ;
		MyStaticValue.isQuantifierRecognition = false ;
		
	//	System.out.println(ToAnalysis.parse("365�ڸ�����ҹҹ"));
//		String str = "����һ����ѧϰ�߷�����ˡ����ڶ��顢����Ӱ�Ӿ硢�������� ��ѧ����˼�����У����ţ�����һ����Ȥ�ġ����õġ�";
		String str = "���Ȱ�������,����˵��������Ĵ�����֮һ,����ʫ���ĸ��϶��Ļش����Ϊ��Ҫ�Ȱ�����������.�ĸ�����,��������,ȴ������Ȥ.�ĸ�����ֱ��ԡ��ɹ����������顱�����ܶ����̡��͡�δ����Ϊ������з����ͻش�.���ĸ��������˵�ǰ��������桢ϯĽ�����ڵ�һЩ����������ʫ�˹��õļ�������,����ɬ,����Ū����,����Ƨ�ѽ�,����˵����ȫ����������ʫ���ص�,Ҳ���������ʫ��ȡ�óɹ�֮ԭ������.";
		String[]ss;
		String s = ToAnalysis.parse(str).toString(); 
		ss = s.split(",");
		SameStringCount Count = new SameStringCount();
		
//	    String regex = "([\u4e00-\u9fa5]+)";    // ƥ��һ���������ĵ�������ʽ
		String regex = "([\u4e00-\u9fa5]+){2,10}";    //ƥ�������������ĵ�������ʽ
		for(int i=0; i<ss.length; i++){
			boolean flag = match(regex, ss[i].toString());
			if(flag){
				Count.hashInsert(ss[i]);   //��Ӹ÷ִ�
	//			System.out.println(ss[i]);
			}
		}
		
		HashMap map = Count.getHashMap();
		
		HashMap<String, Integer> news = new HashMap<String,Integer>();      //����һ���µĹ�ϣͼ������ִ�
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
