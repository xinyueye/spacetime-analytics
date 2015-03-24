package cug.wb.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cug.crawer.DbManager;

/**
 * Location, span, Topic,num统计表
 * 
 * @author li
 * 
 */
public class LSTNumEntity {
	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

	public Date getDtStart() {
		return dtStart;
	}

	public void setDtStart(Date dtStart) {
		this.dtStart = dtStart;
	}

	public Date getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(Date dtEnd) {
		this.dtEnd = dtEnd;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {

		this.city = city;
	}

	/**
	 * 设置位置信息
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		if (location == null)
			return;
		String[] strs = location.split(":");
		this.province = strs[0];
		if (strs.length > 1) {
			this.city = strs[1];
		}
	}

	public void saveToDb() throws SQLException {
		Connection con = new DbManager().getConnection();

		PreparedStatement ps = con
				.prepareStatement("insert into wb_lstnum(province,city,topic,dtStart,dtEnd,num,savetime,tag) values(?,?,?,?,?,?,?,?)");
		ps.setString(1, this.province);
		ps.setString(2, this.city);
		ps.setString(3, this.topic);

		if (this.dtStart != null)
			ps.setTimestamp(4, new java.sql.Timestamp(dtStart.getTime()));
		if (this.dtEnd != null)
			ps.setTimestamp(5, new java.sql.Timestamp(dtEnd.getTime()));
		ps.setLong(6, num);
		ps.setTimestamp(7, new java.sql.Timestamp(dtEnd.getTime()));
		ps.setTimestamp(7, new java.sql.Timestamp(Calendar.getInstance()
				.getTime().getTime()));
		ps.setString(8, tag);

		ps.executeUpdate();

	}

	long num;
	Date dtStart;
	Date dtEnd;
	String topic;
	String province;;
	String city;
	String tag;

	public String getTag() {
		return tag;
	}

	public boolean find(String tag) {
		String strSql = "select * from wb_lstnum where %s order by uid desc ";
		String strWhere = "";

		ArrayList arr=new ArrayList();
		int n = 0;
		if (province != null) {
			strWhere += "and  province=?  ";
			arr.add(this.province);
		}
		else 
			strWhere+="and isnull(province)";
		if (city != null) {
			strWhere += " and city=?  ";
			arr.add( this.city);
		}
		else 
			strWhere+="and isnull(city)";
		if (topic != null) {
			strWhere += " and topic =?  ";
			arr.add(this.topic);
		}
		else 
			strWhere+="and isnull(topic)";
		// ps.setString(3,this.topic);

		if (this.dtStart != null) {
			strWhere += " and dtStart=?  ";
			arr.add(new java.sql.Timestamp(dtStart.getTime()));
		}
		else 
			strWhere+="and isnull(dtStart)";
		if (this.dtEnd != null) {
			strWhere += " and dtEnd=?  ";
			arr.add(new java.sql.Timestamp(dtEnd.getTime()));
		}
		else 
			strWhere+="and isnull(dtEnd)";

		strSql = String.format(strSql, strWhere.substring(4));
		DbManager db = new DbManager();
		try {
			Statement st = db.getStatement();
			PreparedStatement ps = db.getConnection().prepareStatement(strSql);
			for(int i=0;i<arr.size();i++)
				ps.setObject(i+1,arr.get(i));

			ResultSet rs = ps.executeQuery();
			
			if (rs.next())
				return true;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}