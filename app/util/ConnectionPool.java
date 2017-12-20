package util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class ConnectionPool {
	static Logger logger = Logger.getLogger(ConnectionPool.class.getName());
	/**
	 * 数据库
	 */
	private String jdbcDriver = "";
	/**
	 * 数据 URL
	 */
	private String dbUrl = "";
	/**
	 * 数据库用户名
	 */
	private String dbUsername = "";
	/**
	 * 数据库用户密
	 */
	private String dbPassword = "";
	/**
	 * 测试连接是否可用的测试表名
	 */
	private String testTable = "";
	/**
	 * 是否启用自动提交
	 */
	private boolean isAutoCommit = true;
	/**
	 * 连接池的初始大小
	 */
	private int initialConnections = 10;
	/**
	 * 连接池自动增加的大小
	 */
	private int incrementalConnections = 5;
	/**
	 * 连接池最大的大小
	 */
	private int maxConnections = 50;
	/**
	 * 连接池最大空闲连接数
	 */
	private int maxIdleConnections = 10;
	/**
	 * 目前的空闲连接数
	 */
	private AtomicInteger idles = new AtomicInteger(0);
	/**
	 * 存放连接池中数据库连接的向量 , 初始时为 null 它中存放的对象为 {@link PooledConnection}
	 */
	private Vector<PooledConnection> connections = null;
	/**
	 * 是否处于“正在关闭连接池状
	 */
	private AtomicBoolean isClosing = new AtomicBoolean(false);

	/**
	 * 函数
	 * 
	 * @param jdbcDriver
	 *            String JDBC 驱动类串
	 * @param dbUrl
	 *            String 数据URL
	 * @param dbUsername
	 *            String 连接数据库用户名
	 * @param dbPassword
	 *            String 连接数据库用户的密码
	 */
	public ConnectionPool(String jdbcDriver, String dbUrl, String dbUsername,
			String dbPassword) {
		this.jdbcDriver = jdbcDriver;
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}

	/**
	 * 返回自动提交参数
	 * 
	 * @return 初始连接池中可获得的连接数量
	 */
	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	/**
	 * 设置是否自动提交
	 * 
	 * @return 初始连接池中可获得的连接数量
	 */
	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

	/**
	 * 返回连接池的初始大小
	 * 
	 * @return 初始连接池中可获得的连接数量
	 */
	public int getInitialConnections() {
		return this.initialConnections;
	}

	/**
	 * 设置连接池的初始大小
	 * 
	 * @param 用于设置初始连接池中连接
	 */
	public void setInitialConnections(int initialConnections) {
		this.initialConnections = initialConnections;
	}

	/**
	 * 返回连接池自动增加的大小
	 * 
	 * @return 连接池自动增加的大小
	 */
	public int getIncrementalConnections() {
		return this.incrementalConnections;
	}

	/**
	 * 设置连接池自动增加的大小
	 * 
	 * @param 连接池自动增加的大小
	 */
	public void setIncrementalConnections(int incrementalConnections) {
		this.incrementalConnections = incrementalConnections;
	}

	/**
	 * 返回连接池中的可用连接数
	 * 
	 * @return 连接池中的可用连接数
	 */
	public int getMaxConnections() {
		return this.maxConnections;
	}

	/**
	 * 设置连接池中可用的连接数
	 * 
	 * @param 设置连接池中可用的连接数量
	 */
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	/**
	 * 返回连接池中可允许的空闲连接数量
	 * 
	 * @return
	 */
	public int getMaxIdleConnections() {
		return maxIdleConnections;
	}

	/**
	 * 设置连接池中可允许的空闲连接数量
	 * 
	 * @param maxIdleConnections
	 */
	public void setMaxIdleConnections(int maxIdleConnections) {
		this.maxIdleConnections = maxIdleConnections;
	}

	/**
	 * 获取测试数据库表的名
	 * 
	 * @return 测试数据库表的名
	 */
	public String getTestTable() {
		return this.testTable;
	}

	/**
	 * 设置测试表的名字
	 * 
	 * @param testTable
	 *            String 测试表的名字
	 */
	public void setTestTable(String testTable) {
		this.testTable = testTable;
	}

	/**
	 * 创建数据库连接池，连接池中的可用连接的数量采用类成员{@link #initialConnections}中设置的
	 * 
	 */
	public synchronized void createPool() throws Exception {
		// 确保连接池没有创
		// 如果连接池己经创建了，保存连接的向量 connections 不会为空
		if (connections != null) {
			return; // 如果己经创建，则返回
		}
		// 实例JDBC Driver 中指定的驱动类实
		Driver driver = (Driver) (Class.forName(this.jdbcDriver).newInstance());
		DriverManager.registerDriver(driver); // 注册 JDBC 驱动程序
		// 创建保存连接的向, 初始时有 0 个元
		connections = new Vector<PooledConnection>();
		// 根据 initialConnections 中设置的值，创建连接
		createConnections(this.initialConnections);
		//logger.info("Database connection established");
	}

	/**
	 * 创建numConnections 指定数目的数据库连接 , 并把这些连接放入 connections 向量
	 * 
	 * @param numConnections
	 *            要创建的数据库连接的数目
	 */
	private void createConnections(int numConnections) throws SQLException {
		// 循环创建指定数目的数据库连接
		for (int x = 0; x < numConnections; x++) {
			// 是否连接池中的数据库连接的数量己经达到最大？值由类成maxConnections指出
			// 如果 maxConnections 0 或负数，表示连接数量没有限制
			// 如果连接数己经达到最大，即出
			if (this.maxConnections > 0
					&& this.connections.size() >= this.maxConnections) {
				break;
			}
			// add a new PooledConnection object to connections vector
			// 增加连接到连接池中（向量 connections 中）
			try {
				connections.addElement(new PooledConnection(newConnection()));
				this.idles.incrementAndGet();
			} catch (SQLException e) {
				logger.error("Falied to create DB connection" + e.getMessage());
				logger.debug(e.getMessage(), e);
				throw new SQLException();
			}
			//logger.info("A new DB connection created");
		}
	}

	/**
	 * 创建新的数据库连接并返回
	 * 
	 * @return 返回新创建的数据库连
	 */
	private Connection newConnection() throws SQLException {
		// 创建数据库连
		Connection conn = DriverManager.getConnection(dbUrl, dbUsername,
				dbPassword);
		conn.setAutoCommit(this.isAutoCommit);
		// 如果这是第一次创建数据库连接，即数据库，获得此数据库允许支持的最大客户连接数
		// connections.size()==0 表示目前没有连接己被创建
		if (connections.size() == 0) {
			DatabaseMetaData metaData = conn.getMetaData();
			int driverMaxConnections = metaData.getMaxConnections();
			// 数据库返回的 driverMaxConnections 若为 0 ，表示此数据库没有最
			// 连接限制，或数据库的连接限制不知
			// driverMaxConnections 为返回的整数，表示此数据库允许客户连接的数目
			// 如果连接池中设置的最大连接数量大于数据库允许的连接数, 则置连接池的
			// 连接数目为数据库允许的最大数
			if (driverMaxConnections > 0
					&& this.maxConnections > driverMaxConnections) {
				this.maxConnections = driverMaxConnections;
			}
		}
		return conn; // 返回创建的新的数据库连接
	}

	/**
	 * 通过调用 {@link #getFreeConnection()}函数返回可用的数据库连接,
	 * 如果当前没有可用的数据库连接，并且更多的数据库连接不能创建（如连接池大小的限制），此函数等待再尝试获取
	 * 
	 * @return 返回可用的数据库连接对象
	 */
	public synchronized Connection getConnection() throws SQLException {
		// 确保连接池己被创
		if (connections == null) {
			return null; // 连接池还没创建，则返null
		}
		Connection conn = null; // getFreeConnection();
		// 如果目前没有可以使用的连接，即所有的连接都在使用
		while (conn == null) {
			// 如果处于正在关闭状，则返回null
			if (this.isClosing.get() == true)
				break;
			conn = getFreeConnection(); // 获得可用的数据库连接，或者重新再试，直到获得可用的连接，如果
			// 如果有返
			if (conn != null)
				break;
			// getFreeConnection() 返回的为 null, 则表明创建一批连接后也不可获得可用连
			wait(250); // 等一会再
		}
		return conn;
	}

	/**
	 * 本函数从连接池向connections 中返回一个可用的的数据库连接br/> 如果当前没有可用的数据库连接，本函数则根
	 * {@link #incrementalConnections}设置的创建几个数据库连接，并放入连接池中<br/>
	 * 如果创建后，的连接仍都在使用中，则返null
	 * 
	 * @return 返回可用的数据库连接
	 */
	private Connection getFreeConnection() throws SQLException {
		// 从连接池中获得一个可用的数据库连
		Connection conn = findFreeConnection();
		if (conn == null) {
			// 如果目前连接池中没有可用的连
			// 创建连接
			createConnections(incrementalConnections);
			// 重新从池中查找是否有可用连接
			conn = findFreeConnection();
			if (conn == null) {
				// 如果创建连接后仍获得不到可用的连接，则返null
				return null;
			}
		}
		return conn;
	}

	/**
	 * 查找连接池中的连接，查找可用的数据库连接，如果没有可用的连接，返null
	 * 
	 * @return 返回可用的数据库连接
	 */
	private Connection findFreeConnection() throws SQLException {
		Connection conn = null;
		PooledConnection pConn = null;
		// 获得连接池向量中的对
		Enumeration<PooledConnection> enumerate = connections.elements();
		// 遍历的对象，看是否有可用的连
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			if (!pConn.isBusy()) {
				// 如果此对象不忙，则获得它的数据库连接并把它设为忙
				conn = pConn.getConnection();
				pConn.setBusy(true);
				this.idles.decrementAndGet();
				// 测试此连接是否可
				if (!testConnection(conn)) {
					logger.debug("Connection not valid. Try to recreate.");
					// 如果此连接不可再用了，则创建新的连接
					// 并替换此不可用的连接对象，如果创建失败，返回 null
					try {
						conn = newConnection();
					} catch (SQLException e) {
						logger.error("Falied to create DB connection"
								+ e.getMessage());
						logger.debug(e.getMessage(), e);
						return null;
					}
					logger.debug("Successfully recreated a connection");
					pConn.setConnection(conn);
				}
				break; // 己经找到可用的连接，
			}
		}
		return conn;// 返回找到到的可用连接
	}

	/**
	 * 测试连接是否可用，如果不可用，关掉它并返false,否则可用返回 true
	 * 
	 * @param conn
	 *            测试的数据库连接
	 * @return 返回 true 表示此连接可用， false 表示不可
	 */
	private boolean testConnection(Connection conn) {
		try {
			// 判断测试表是否存
			if (testTable.equals("")) {
				// 如果测试表为空，试着使用此连接的 setAutoCommit() 方法来判断连接否可用
				// （此方法只在部分数据库可用，如果不可,抛出异常）注意：使用测试表的方法更可靠
				// lzl-此处改为用isValid测试
				return conn.isValid(10);
				// conn.setAutoCommit(true);
			} else {// 有测试表的时候使用测试表测试
				// check if this connection is valid
				Statement stmt = conn.createStatement();
				stmt.execute("select count(*) from " + testTable);
			}
		} catch (SQLException e) {
			// 上面抛出异常，此连接己不可用，关闭它，并返回 false;
			closeConnection(conn);
			return false;
		}
		// 连接可用，返true
		return true;
	}

	/**
	 * 此函数返回一个数据库连接到连接池中，并把此连接置为空闲<br/>
	 * 使用连接池获得的数据库连接均应在不使用此连接时返回它
	 * 
	 * @param 回到连接池中的连接对
	 */
	public void returnConnection(Connection conn) {
		// 确保连接池存在，如果连接没有创建（不存在），直接返回
		if (connections == null) {
			logger.error("Connection pool not exists. Can't return this connection to pool. This connection would be close!");
			closeConnection(conn);
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		// 遍历连接池中的所有连接，找到这个要返回的连接对象
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// 先找到连接池中的要返回的连接对象
			if (conn == pConn.getConnection()) {
				// 找到, 看是否有足够的空连接，够则杀掉，否则设置此连接为空闲状
				if (this.idles.get() >= this.maxIdleConnections) {
					closeConnection(conn);
					connections.remove(pConn);
				} else {
					pConn.setBusy(false);
					this.idles.incrementAndGet();
				}
				return;
			}
		}
		logger.warn("This connection is not belong to the pool. This connection would be close!");
		closeConnection(conn);
	}

	/**
	 * 刷新连接池中的连接对象<br/>
	 * 此方法使用前必须注意：会关闭已存在的连接，保证在连接全部在池中的情况下才使用
	 * 
	 */
	public synchronized void refreshConnections() throws SQLException {
		// 确保连接池己创建存在，并且不处于正在关闭状
		if (connections == null && this.isClosing.get() == false) {
			logger.error("Can't refresh connection pool because of inexistent or closing!");
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		while (enumerate.hasMoreElements()) {
			// 获得连接对象
			pConn = (PooledConnection) enumerate.nextElement();
			// 如果对象忙则5 ,5 秒后直接刷新
			if (pConn.isBusy()) {
				wait(5000); // 5
			}
			// 关闭此连接，用一个新的连接代替它
			closeConnection(pConn.getConnection());
			pConn.setConnection(newConnection());
			pConn.setBusy(false);
		}
		this.idles.set(connections.size());
	}

	/**
	 * 关闭连接池中的连接，并清空连接池
	 * 
	 */
	public synchronized void closeConnectionPool() throws SQLException {
		// 确保连接池存在，如果不存在，返回
		if (connections == null) {
			logger.error("Can't close connection pool because of inexistent!");
			return;
		}
		// 处于正在关闭数据库连接状
		this.isClosing = new AtomicBoolean(true);
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// 如果忙，5
			if (pConn.isBusy()) {
				wait(5000); // 5
			}
			// 5 秒后直接关闭
			closeConnection(pConn.getConnection());
			// 从连接池向量中删除它
			connections.removeElement(pConn);
		}
		// 置连接池为空
		connections = null;
		this.idles.set(0);
		this.isClosing = new AtomicBoolean(false);
	}

	/**
	 * 关闭数据库连
	 * 
	 * @param 关闭的数据库连接
	 */
	private void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			logger.error("Error when close connection pool" + e.getMessage());
			logger.debug(e.getMessage(), e);
		}
	}

	/**
	 * 关闭statement
	 * 
	 * @param stmt
	 */
	public static void closeStmt(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.debug(e.getMessage(), e);
			}
		}
	}

	/**
	 * 关闭statement
	 * 
	 * @param stmt
	 */
	public static void closePstmt(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				logger.debug(e.getMessage(), e);
			}
		}
	}

	/**
	 * 使程序等待给定的毫秒
	 * 
	 * @param 给定的毫秒数
	 */
	private void wait(int mSeconds) {
		try {
			Thread.sleep(mSeconds);
		} catch (InterruptedException e) {

		}
	}

	/**
	 * 内部使用的用于保存连接池中连接对象的类 此类中有两个成员，一个是数据库的连接，另是指示此连接是否正在使用的标志
	 */
	class PooledConnection {
		Connection connection = null;// 数据库连
		boolean busy = false; // 此连接是否正在使用的标志，默认没有正在使

		// 构函数，根据一Connection 构告 PooledConnection 对象
		public PooledConnection(Connection connection) {
			this.connection = connection;
		}

		// 返回此对象中的连
		public Connection getConnection() {
			return connection;
		}

		// 设置此对象的，连
		public void setConnection(Connection connection) {
			this.connection = connection;
		}

		// 获得对象连接是否
		public boolean isBusy() {
			return busy;
		}

		// 设置对象的连接正在忙
		public void setBusy(boolean busy) {
			this.busy = busy;
		}
	}
}