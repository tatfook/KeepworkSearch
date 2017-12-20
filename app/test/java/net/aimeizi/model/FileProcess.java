package test.java.net.aimeizi.model;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileProcess {

	static final String JSONFILEINPUT = "E:\\JavaNew\\Jest-master\\jest\\input";
	//static LinkedList<String> sJsonFileList = new LinkedList<String>(); 
	
	public static void main (String args[]) {
		LinkedList<String> curJsonList = getJsonFilePath(JSONFILEINPUT);
		System.out.println("size = " + curJsonList.size());
		
		for (int i = 0; i < curJsonList.size(); ++i){
			System.out.println(" i = " + i + " " + curJsonList.get(i));
			//	String curJsonPath = curJsonList.get(i);
		  //  ImageInfo curImageInfo = JsonParse.GetImageJson(curJsonPath);
		    //JsonParse.printImageJson(curImageInfo);
			break;
		}
	}

	/*
	 **@brief: 递归遍历Json文件
	 **@param: 路径名称
	 **@return：空
	 **@author: ycy
	 **@date: 20170719
	 */
	public static LinkedList<String> getJsonFilePath(String path){
		
	 int fileNum = 0;
	 int folderNum = 0;  
	 
	 LinkedList<String> jsonFileList = new LinkedList<String>();
	 
        File file = new File(path);  
        if (file.exists()) {  
            LinkedList<File> list = new LinkedList<File>();  
            File[] files = file.listFiles();  
            for (File file2 : files) {  
                if (file2.isDirectory()) {  
                  //  System.out.println("文件夹:" + file2.getAbsolutePath());  
                    list.add(file2);  
                    folderNum++;  
                } else {  
                   // System.out.println("文件:" + file2.getAbsolutePath());  
                    jsonFileList.add(file2.getAbsolutePath());
                    fileNum++;  
                }  
            }  
            File temp_file;  
            while (!list.isEmpty()) {  
                temp_file = list.removeFirst();  
                files = temp_file.listFiles();  
                for (File file2 : files) {  
                    if (file2.isDirectory()) {  
                       // System.out.println("文件夹:" + file2.getAbsolutePath());  
                        list.add(file2);  
                        folderNum++;  
                    } else {  
                      //  System.out.println("文件:" + file2.getAbsolutePath());  
                        jsonFileList.add(file2.getAbsolutePath());
                        fileNum++;  
                    }  
                }  
            }  
        } else {  
            System.out.println("文件不存在!");  
        }  
       // System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);  
        return jsonFileList;
	}//end 

}
