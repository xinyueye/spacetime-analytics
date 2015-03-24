package cug.crawer;

import java.sql.*;

import cug.tools.Config;



/**
 * 简单数据库管理类
 * 
 * @author li
 * 
 */
public class DbManager {
	public static String dburl = null;

	boolean CreateTables()
	{
		
		return true;
		
	}
	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if (dburl == null) {
			String strDriver = Config.getStringProperty("db.driver",
					"com.mysql.jdbc.Driver");
			dburl = Config.getStringProperty("db.url",
					"jdbc:mysql://localhost:3306/sns?user=root&password=");
			try {
				Class.forName(strDriver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		Connection con = DriverManager.getConnection(dburl);
		return con;
	}

	/**
	 * 获取Statement
	 * @param strSql 
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Statement getStatement() throws SQLException {
		Statement stmt = getConnection().createStatement();
		return stmt;
	}
	
	/**
	 * 
	 */
	public ResultSet getResult(String strSql)
	{
		try {
		Statement st=getStatement();
	
			return st.executeQuery(strSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * 
	 * @param strSql
	 * @return
	 */
	public int ExecuteUpdate(String strSql)
	{
		try {
		Statement st=getStatement();
		
			return st.executeUpdate(strSql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return 0;
		}
		
		
		
	}
}
