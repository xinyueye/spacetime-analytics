package cug.wb.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import cug.crawer.DbManager;

public class PoiEntity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getPid() {
		return pid;
	}



	public void setPid(String pid) {
		this.pid = pid;
	}



	public String getCategorys() {
		return categorys;
	}



	public void setCategorys(String categorys) {
		this.categorys = categorys;
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



	public int getCheckin_num() {
		return checkin_num;
	}



	public void setCheckin_num(int chickin_num) {
		this.checkin_num = chickin_num;
	}
	String title;
	String city;
	String pid;
	String categorys;
	String json;
	double lat;
	double lon;
	int checkin_num;
	String tag;
	
	

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	/**
	 * 保存到数据库
	 * @throws SQLException
	 */
	public void save2Db() throws SQLException
	{
		Connection con=new DbManager().getConnection();	
				
		PreparedStatement ps=con.prepareStatement("insert into wb_pois(pid,title,city,categorys,json,lat,lon,checkin_num,savetime,tag) values(?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1,pid);
		ps.setString(2,title);
		ps.setString(3, city);
		ps.setString(4, this.categorys);
		ps.setString(5,json);
		ps.setDouble(6, lat);
		ps.setDouble(7,lon);
		ps.setInt(8,checkin_num);
		ps.setTimestamp(9,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		ps.setString(10,tag);
		
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
		PreparedStatement ps=con.prepareStatement("select uid from wb_pois where pid=?  and tag=?");
		ps.setString(1,pid);	
		ps.setString(2, tag);		
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


