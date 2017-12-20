package test.java.net.aimeizi.models;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import test.java.io.searchbox.client.keepworkUpsert;
import util.FileUtil;

import java.io.FileInputStream;
import org.apache.log4j.PropertyConfigurator;

public class FileParse {
	static Logger logger = Logger.getLogger(FileParse.class);
	public static int stotalcnt = 1;
	public static void main(String[] args) {	
		PropertyConfigurator.configure("log4j.properties");
		try {
			traverseImageImport(new File("D:\\360安全浏览器下载\\win2012R2eight.Actv"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ImageExtInfo mdInfo = readXFile("data\\1.md");
		ImageExtPrint(mdInfo);
		
		ImageExtInfo txtInfo = readXFile("data\\test.txt");
		ImageExtPrint(txtInfo);
		
		ImageExtInfo htmlInfo = readXFile("data\\1.json");
		ImageExtPrint(htmlInfo);
		
		ImageExtInfo docInfo = readXFile("data\\test.doc");
		ImageExtPrint(docInfo);
		
		ImageExtInfo docxInfo = readXFile("data\\2.docx");
		ImageExtPrint(docxInfo);
	}
	
	public static void ImageExtPrint(ImageExtInfo imageInfo){
		logger.info("title = " + imageInfo.getTitle());
		logger.info("path = " + imageInfo.getPath());
		logger.info("content = " + imageInfo.getContent());
		logger.info("creat_time = " + imageInfo.getCreate_time());
	}
	
	 /**
     * 
     * @param object json对象
     * @return 转化后的Map
	 * @throws Exception 
     */
	public static void traverseImageImport(File dir) throws Exception {
		try {
			File[] files = dir.listFiles();
			
			for (File file : files) {
				if (file.isDirectory()) {
					logger.info("cur directory:" + file.getCanonicalPath());
					traverseImageImport(file);
				} else {
					String fileInput = file.getCanonicalPath();
					//System.out.println("     file:" + file.getCanonicalPath());
					ImageExtInfo curImageInfo = new ImageExtInfo();
					curImageInfo = readXFile(fileInput);
					if (null != curImageInfo){
						try {
							keepworkUpsert.postMirrorUpsert(curImageInfo);	
						}catch (IOException e) {
							e.printStackTrace();
						}	
						logger.info("upsert cnt = " + stotalcnt);
						stotalcnt++;
					}else{
						continue;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 **@brief:不同类型的文档解析
	 **@param:文件路径
	 **@return：解析结果
	 **@author:ycy
	 **@date:20170905 13:26
	 */
	public static ImageExtInfo readXFile(String inputPath){
		ImageExtInfo imageExtInfo = new ImageExtInfo();
		File file = new File(inputPath.trim());
		String fileExt = FileUtil.getFileExtension(file);
		switch(fileExt){
			case "docx":
				imageExtInfo = readDocxFile(inputPath);
				break;
			case "doc":
				imageExtInfo = readDocFile(inputPath);
				break;
			case "json":
			case "txt":
			case "md":
				imageExtInfo = readTxtFile(inputPath);
				break;
			default:
				logger.info("unknow type " + fileExt);
				imageExtInfo = null;	
		}
		return imageExtInfo;
	}
	
	/*
	 **@brief:doc文档解析
	 **@param:文件路径
	 **@return：解析结果
	 **@author:ycy
	 **@date:20170905 10:01
	 */
	public static ImageExtInfo readDocFile(String inputPath) {
		ImageExtInfo imageExtInfo = new ImageExtInfo();
		try {		
			File file = new File(inputPath.trim());
			
			//1.获取文件名称
	        String fileTitle = file.getName();  
	        
			//2.获取绝对路径
	        String filePath = file.getAbsolutePath();
			
			//3.获取文件内容
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			HWPFDocument doc = new HWPFDocument(fis);
			WordExtractor we = new WordExtractor(doc);
			String[] paragraphs = we.getParagraphText();
			StringBuilder contentBuilder = new StringBuilder();
			//System.out.println("Total no of paragraph "+paragraphs.length);
			for (String para : paragraphs) {
				contentBuilder.append(para.toString().trim());
				//System.out.println(para.toString());
			}
			String content = contentBuilder.toString();
			//System.out.println("content = " + content);
					
			imageExtInfo.setTitle(fileTitle);
			imageExtInfo.setPath(filePath);
			imageExtInfo.setContent(content);	
			// 获取当前时间...
			String curTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			imageExtInfo.setCreate_time(curTimeStamp);
			
			we.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageExtInfo;
	}
	
	
	/*
	 **@brief:docx文档解析
	 **@param:文件路径
	 **@return：解析结果
	 **@author:ycy
	 **@date:20170905 10:01
	 */
	public static ImageExtInfo readDocxFile(String inputPath) {
		ImageExtInfo imageExtInfo = new ImageExtInfo();
		try {
			File file = new File(inputPath.trim());
			
			//1.获取文件名称
	        String fileTitle = file.getName();  
	        
			//2.获取绝对路径
	        String filePath = file.getAbsolutePath();
			
			//3.获取文件内容
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			XWPFDocument document = new XWPFDocument(fis);
			List<XWPFParagraph> paragraphs = document.getParagraphs();
			StringBuilder contentBuilder = new StringBuilder();
			//System.out.println("Total no of paragraph "+paragraphs.size());
			for (XWPFParagraph para : paragraphs) {
				//System.out.println(para.getText());
				contentBuilder.append(para.getText().trim());
			}
			
			String content = contentBuilder.toString();
			imageExtInfo.setTitle(fileTitle);
			imageExtInfo.setPath(filePath);
			imageExtInfo.setContent(content.trim());
			// 获取当前时间...
			String curTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			imageExtInfo.setCreate_time(curTimeStamp);
			
			document.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageExtInfo;
	}
	
	/*
	 **@brief:html和txt文档解析
	 **@param:文件路径
	 **@return：解析结果
	 **@author:ycy
	 **@date:20170905 10:01
	 */
	public static ImageExtInfo readTxtFile(String inputPath){
		ImageExtInfo imageExtInfo = new ImageExtInfo();
		StringBuilder contentBuilder = new StringBuilder();
		File file = new File(inputPath.trim());
		//1.获取文件名称
        String fileTitle = file.getName();  
        
		//2.获取绝对路径
        String filePath = file.getAbsolutePath();
		
		//3.获取文件内容
		try {
			//"data\\1.html"
		    BufferedReader in = new BufferedReader(new FileReader(inputPath));
		    String str;
		    while ((str = in.readLine()) != null) {
		        contentBuilder.append(str.trim());
		    }
		    in.close();
		} catch (IOException e) {
		}
		String content = contentBuilder.toString();
		//System.out.println("content = " + content);
		
		imageExtInfo.setTitle(fileTitle);
		imageExtInfo.setPath(filePath);
		imageExtInfo.setContent(content.trim());
		// 获取当前时间...
		String curTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		imageExtInfo.setCreate_time(curTimeStamp);
		
		return imageExtInfo;
	}
	
	
	/**
     * 功能：Java读取txt文件的内容 步骤：1：先获得文件句柄 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
     * 3：读取到输入流后，需要读取生成字节流 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
     * 
     * @param filePath
     *            文件路径[到达文件:如： D:\aa.txt]
     * @return 将这个文件按照每一行切割成数组存放到list中。
     */
    public static List<String> readTxtFileIntoStringArrList(String filePath)
    {
        List<String> list = new ArrayList<String>();
        try
        {
            String encoding = "UTF8";
            File file = new File(filePath);
            if (file.isFile() && file.exists())
            { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null)
                {
                	logger.info("line = " + lineTxt);
                    list.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            }
            else
            {
                System.out.println("找不到指定的文件");
            }
        }
        catch (Exception e)
        {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return list;
    }
}

