package cug.wb.da;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import cug.crawer.DbManager;

public class CrossTable {
	ArrayList<String> lines;
	ArrayList<String> columns;
	long[][] matrix;

	public boolean wirite2File(FileWriter os) throws IOException {
		os.write("\t");
		for (int j = 0; j < columns.size(); j++) {
			os.write(columns.get(j) + "\t");
		}
		os.write("\n");
		for (int i = 0; i < lines.size(); i++) {
			os.write(lines.get(i) + "\t");
			for (int j = 0; j < columns.size(); j++) {
				os.write(matrix[i][j] + "\t");
			}
			os.write("\n");
		}

		return true;
	}

	public boolean print(PrintStream os) {
		return print(os, "\t");
	}

	public boolean print(PrintStream os, String token) {
		os.print(token);
		for (int j = 0; j < columns.size(); j++) {
			os.print(columns.get(j) + token);
		}
		os.println();
		for (int i = 0; i < lines.size(); i++) {
			os.print(lines.get(i) + token);
			for (int j = 0; j < columns.size(); j++) {
				os.print(matrix[i][j] + token);
			}
			os.println();
		}
		return true;

	}

	public boolean MatrixWithLastValue(String strSql) {
		return toMatrix(strSql, false);
	}

	/**
	 * 标记中间值，根据排序，标记第n % groupSize == groupSize / 2个，比如size=7,标注4,11,
	 * 
	 * @param strSQL
	 *            如"select uid from wb_tweets where tag like 'w%' order by
	 *            tag,cityname
	 * @param updateSql
	 *            update tag=tag+'_z' where uid=?
	 * @param groupSize  分组大小
	 * @return
	 */
	public boolean MarkMidValue(String strSQL, String updateSql, int groupSize) {
		DbManager db = new DbManager();
		Statement st;
		ResultSet rs;
		int n = 0;
		int un = 0;
		try {
			st = db.getStatement();
			rs = st.executeQuery(strSQL);
			PreparedStatement ps = db.getConnection().prepareStatement(
					updateSql);
			while (rs.next()) {
				if (n % groupSize == groupSize / 2) {
					// un+=stu.executeUpdate(updateSql+"'"+rs.getString(1)+"'");
					ps.setString(1, rs.getString(1));
				un+=	ps.executeUpdate();;
				}
				n++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
		// }
		System.out.println("共有" + n + "条，" + "更新" + un + "条");
		return true;
	}

	/**
	 * 
	 * @param strSql
	 *            line,colume,value
	 * @param bSum
	 * @return
	 */
	public boolean toMatrix(String strSql, Boolean bSum) {
		DbManager db = new DbManager();
		Statement st;
		ResultSet rs;
		try {
			st = db.getStatement();
			rs = st.executeQuery(strSql);
			lines = new ArrayList<String>();
			columns = new ArrayList<String>();
			ArrayList<String[]> data = new ArrayList<String[]>();
			while (rs.next()) {
				String strs[] = new String[3];
				strs[0] = rs.getString(1);
				strs[1] = rs.getString(2);
				strs[2] = rs.getString(3);
				data.add(strs);
				if (lines.indexOf(strs[0]) < 0)
					lines.add(strs[0]);
				if (columns.indexOf(strs[1]) < 0)
					columns.add(strs[1]);
			}

			matrix = new long[lines.size()][columns.size()];

			for (int i = 0; i < data.size(); i++) {
				String strs[] = data.get(i);
				long v = Long.parseLong(strs[2]);
				if (bSum)
					matrix[lines.indexOf(strs[0])][columns.indexOf(strs[1])] += v;
				else
					matrix[lines.indexOf(strs[0])][columns.indexOf(strs[1])] = v;
			}
			return true;

			// al.in
		} catch (SQLException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return false;
		}

	}

	public boolean MatrixWithSum(String strSql) {
		return toMatrix(strSql, true);

	}

	public static void main(String[] args) {
		CrossTable ct = new CrossTable();
		String strNew;// = String
				//.format("select CONCAT(province,':0',city) as loc,topic,num  from %s  where %s ",
				strNew="select  concat(province,\"-\",city) as loc,date_format(dtStart, '%Y%M') m ,num  from wb_lstnum where dtStart>'2009-12-30' and dtStart<'2014-12-28' and province<100 order by dtstart,province,city";
		ct.MatrixWithLastValue(strNew);
		org.apache.log4j.Logger.getLogger("weibo").info("求和");
		ct.print(System.out);
		
		
		ct.print(System.out);

	}

}
