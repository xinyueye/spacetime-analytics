package cug.wb.da;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import cug.crawer.DbManager;

public class LSTCrossTable {

	/**
	 * 
	 * @param tableName
	 * @param conditions
	 * @return
	 * @throws SQLException
	 */
	public boolean Handle(String tableName, String conditions)
			throws SQLException {
		String strSql = String.format(
				" select DISTINCT(dtStart) from %s where %s ", tableName,
				conditions);
		DbManager db = new DbManager();
		Statement st = db.getStatement();
		ResultSet rs = st.executeQuery(strSql);

		for (; rs.next();) {
			CrossTable ct = new CrossTable();
			String strNew = String
				//	.format("select CONCAT(province,':0',city) as loc,topic,num  from %s  where %s and dtStart='%s'",
					.format("select cityname as loc,topic,num  from %s  where %s and dtStart='%s'",
							tableName, conditions, rs.getString(1));
			ct.MatrixWithLastValue(strNew);
			org.apache.log4j.Logger.getLogger("weibo").info(rs.getString(1).substring(0,10));
			ct.print(System.out);
		}

		CrossTable ct = new CrossTable();
		String strNew = String
				//.format("select CONCAT(province,':0',city) as loc,topic,num  from %s  where %s ",
				.format("select cityname as loc,topic,num  from %s  where %s ",
						tableName, conditions);
		ct.MatrixWithSum(strNew);
		org.apache.log4j.Logger.getLogger("weibo").info("求和");
		ct.print(System.out);

		return true;

	}



}
