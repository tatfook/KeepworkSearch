package org.ansj.person.add;

import java.util.HashMap;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	   String str = "《热爱生命》,可以说是汪国真的代表作之一,这首诗以四个肯定的回答表达出为何要热爱生命的哲理.四个段落,看似相似,却各有其趣.四个段落分别以“成功”、“爱情”、“奋斗历程”和“未来”为意象进行分析和回答.这四个意象可以说是包括汪国真、席慕容在内的一些清新哲理派诗人惯用的几个意象,不晦涩,不故弄玄虚,不生僻难解,可以说是完全区别于朦胧诗的特点,也是汪国真的诗歌取得成功之原因所在.";
	   
	   //第一种方式
	   WordCount word = new WordCount();
	   HashMap<String, Integer> hash = word.getGarticiples(str);
//	   System.out.println(hash);
	   
	   //第二种方式
	   WordDegrees degre = new WordDegrees();
	   String result = degre.WordProcessing(str);
	   System.out.println(result); 
	}

}

