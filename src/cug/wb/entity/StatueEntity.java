package cug.wb.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import cug.crawer.DbManager;

public class StatueEntity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	String userid;
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getPosttime() {
		return posttime;
	}

	public void setPosttime(Timestamp posttime) {
		this.posttime = posttime;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}


	String mid;
	String text;
	Timestamp  posttime;	
	String json;
	double lat;
	double lon;	
	String tag;
	
	


	/**
	 * 保存到数据库
	 * @throws SQLException
	 */
	public void save2Db() throws SQLException
	{
		Connection con=new DbManager().getConnection();	
				
		PreparedStatement ps=con.prepareStatement("insert into wb_statues(mid,userid,text,lat,lon,posttime,json,savetime,tag) values(?,?,?,?,?,?,?,?,?)");
		ps.setString(1,mid);
		ps.setString(2,userid);
		ps.setString(3, text);
		ps.setDouble(4, lat);
		ps.setDouble(5,lon);
		
		ps.setTimestamp(6,new java.sql.Timestamp(this.posttime.getTime()));
		ps.setString(7,json);
		ps.setTimestamp(8,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		ps.setString(9,tag);
		
		ps.executeUpdate();	
	}
	
	/**
	 * 是否数据已经在数据库中存在
	 * @return
	 */
	public boolean isExist()
	{
		Connection con;
		try {
			con = new DbManager().getConnection();		
		PreparedStatement ps=con.prepareStatement("select uid from wb_statues where mid=? ");
		ps.setString(1,mid);	
				
		ResultSet rs=ps.executeQuery();
		if(rs.next())
			return true;
		return false;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	}
}


