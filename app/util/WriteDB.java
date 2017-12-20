package util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

public class WriteDB {
	static Logger logger = Logger.getLogger(WriteDB.class);
	/*static*/ ConnectionPool mysqlPool = null;
	/*static*/ Connection mysql = null;

	public /*static*/ void init_mysql() {
		try {
		//	logger.info("init_mysql");
			String mysql_host = dbconf.mysql_host;
			int mysql_port = dbconf.mysql_port;
			String mysql_db = dbconf.mysql_db;
			String mysql_user = dbconf.mysql_user;
			String mysql_psw = dbconf.mysql_psw;

			mysqlPool = new ConnectionPool("com.mysql.jdbc.Driver",
					"jdbc:mysql://" + mysql_host + ":" + mysql_port + "/"
							+ mysql_db, mysql_user, mysql_psw);
			mysqlPool.setMaxConnections(5);
			mysqlPool.setAutoCommit(false);
			mysqlPool.createPool();


		} catch (Exception e) {
			logger.info(e.getMessage());
			logger.info("MySQL error! Cannot init basic data from database.  Exit...");
		}
	}

	public /*static*/ void close_mysql() {
		//logger.info("close_mysql");
		if (mysql != null) {
			try {
				mysql.close();
			} catch (Exception ignore) {
				mysql = null;
			}
			mysql = null;
		}

		if (mysqlPool != null) {
			try {
				mysqlPool.closeConnectionPool();
			} catch (SQLException e) {
				logger.error("Mysql pool close failed...", e);
				mysqlPool = null;
			}
			mysqlPool = null;
		}
	}

	  /*
  	 ** @brief:配置初始化
  	 * @params:空
  	 * @return:空
  	 * @author:ycy
  	 **
  	 */
    public static void init_config(String propertie_path) {
		Properties propertie = new Properties();
		FileInputStream inputFile;
		try {
			inputFile = new FileInputStream(propertie_path);
			propertie.load(inputFile);
			inputFile.close();
			dbconf.mysql_host = propertie.getProperty("mysql_host");
			dbconf.mysql_port = Integer.parseInt(propertie
					.getProperty("mysql_port"));
			dbconf.mysql_db = propertie.getProperty("mysql_db");
			dbconf.mysql_db_app = propertie.getProperty("mysql_db_app");
			dbconf.mysql_user = propertie.getProperty("mysql_user");
			dbconf.mysql_psw = propertie.getProperty("mysql_psw");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	
	
	/*
	 **@brief:判定用户名是否存在
	 **@return:存在，返回true; 不存在，返回false；
	 **@param:用户名
	 **@author:ycy
	 **@date:20171108
	 */
	public LinkedHashMap<String, String> check_userinfo(String userName, String password) throws SQLException {
		LinkedHashMap userInfoMap = new LinkedHashMap<String, String>(); 
		String selectSql = "SELECT username, password FROM `user_info` "
				+ "WHERE  username = \'" + userName + "\'";
		//logger.info("selectSql = " + selectSql);
		String curUserName = null;
		String curPassword = null;
		if (mysql == null) {
			init_mysql();
			mysql = mysqlPool.getConnection();
		}
		try {
			// 执行查询
			Statement stmt = mysql.createStatement();
			ResultSet sqlRst = stmt.executeQuery(selectSql);
			while (sqlRst.next()) {
				curUserName = sqlRst.getString("username").trim();
				curPassword = sqlRst.getString("password").trim();
				logger.info("curUserName = " + curUserName);
				if (curUserName != null || curUserName.equals("") || curUserName.length() != 0){
				
					userInfoMap.put(curUserName, curPassword);
				}
			}
			stmt.close();
			close_mysql();  //new add by ycy 20170419 21:31
		} catch (Exception ex) {
			// TODO: handle exception
			logger.info(ex);
		}
	
		return userInfoMap;
	}
	/*
	 **@brief:判定用户名是否存在
	 **@return:存在，返回true; 不存在，返回false；
	 **@param:用户名
	 **@author:ycy
	 **@date:20171108
	 */
	public boolean judgeUsername(String userName) throws SQLException {
		String selectSql = "SELECT username FROM `user_info` "
				+ "WHERE  username = \'" + userName + "\'";
		//logger.info("selectSql = " + selectSql);
		String curUserName = null;
		if (mysql == null) {
			init_mysql();
			mysql = mysqlPool.getConnection();
		}
		try {
			// 执行查询
			Statement stmt = mysql.createStatement();
			ResultSet sqlRst = stmt.executeQuery(selectSql);
			while (sqlRst.next()) {
				curUserName = sqlRst.getString("username").trim();
			}
			stmt.close();
			close_mysql();  //new add by ycy 20170419 21:31
		} catch (Exception ex) {
			// TODO: handle exception
			logger.info(ex);
		}
	
		if (curUserName == null || curUserName.equals("") || curUserName.length() == 0){
			return false;
		}else{
			return true;
		}
	}
	
	
	/**
	 * brief:将结果入库 param: MsearchArticle
	 * 
	 * @author:yangcy@int-yt.com
	 * @date:20161122
	 **/
	public /*static*/ void importUserInfo(String username, String password)
			throws SQLException {
		
		logger.info("执行入库！");
		if (mysql == null) {
			init_mysql();
			mysql = mysqlPool.getConnection();
		}
		String insertSql = "insert into user_info"
				+ " (username, password) values(?, ?) ";

		PreparedStatement preparedStatement = null;
		preparedStatement = mysql.prepareStatement(insertSql);
		try {
			    int k = 1;
				//留下中间内容
				preparedStatement.setString(k++, username);
				preparedStatement.setString(k++, password);
				preparedStatement.addBatch();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error("", e);
		}
		preparedStatement.executeBatch();
		// 按任务提交MySQL执行
		mysql.commit();
		preparedStatement.clearBatch();
		preparedStatement.close();   
		close_mysql();  
	}

}