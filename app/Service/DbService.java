package Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.persistence.Cache;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import play.Play;
import play.data.validation.Min;
import play.data.validation.Range;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Http.Request;
import util.InitialServlet;
import util.JDBCPool;

import com.google.gson.Gson;

import controllers.Application;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * 处理接口
 * 
 * 
 */
public class DbService {
	static Logger logger = Logger.getLogger(DbService.class.getName());
	

	
}
