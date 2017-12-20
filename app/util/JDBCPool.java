package util;

import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class JDBCPool {
	public static DataSource mysqlPool = null;

	public static void mysqlConnect(String mysqlurl, String authname, String authpass) throws SQLException {
		PoolProperties p = new PoolProperties();
		p.setUrl(mysqlurl);
		p.setDriverClassName("com.mysql.jdbc.Driver");
		p.setUsername(authname);
		p.setPassword(authpass);

		p.setJmxEnabled(true);
		p.setTestWhileIdle(false);
		p.setTestOnBorrow(true);
		p.setValidationQuery("SELECT 1");
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(10);
		p.setInitialSize(1);
		p.setMaxWait(10000);
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(60000);
		p.setMinIdle(1);
		p.setMaxIdle(3);
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
		mysqlPool = new DataSource();
		mysqlPool.setPoolProperties(p);
	}
}
