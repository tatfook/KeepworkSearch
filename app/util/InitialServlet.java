package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.collections.map.StaticBucketMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import play.Play;

import Service.DbService;

public class InitialServlet extends HttpServlet{

	private static final long serialVersionUID = 8007434774453553342L;
	static Logger logger = Logger.getLogger(InitialServlet.class.getName());
	public static String extra = "";
	public static String extra_get() {
		return extra;
	}
	
	public static void initConfig() throws FileNotFoundException{
		logger.info("Config init...");
		// 启动时初始化
		String db_path = "./dbs";
	 //	boolean init_flag = YzmRec.InitWords(db_path);
	//	if(init_flag == false){
	//		System.out.println("找不到字库，无法处理！");
	//		return;
	//	}
	}
}
