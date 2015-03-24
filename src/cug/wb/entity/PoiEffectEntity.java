package cug.wb.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import cug.crawer.DbManager;
import cug.tools.CharsetTranslate;

public class PoiEffectEntity {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

	String key;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}

	String pid;
	int num;
	String tag;
	int distance;
	
	

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
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
		
		PreparedStatement ps=con.prepareStatement("select uid from wb_poisEffect where pid=? and ukey=? and tag=? and distance=?");
		ps.setString(1,pid);
		ps.setString(2,key);
		ps.setString(3, tag);
		ps.setInt(4,distance);
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
	
	/**
	 * 保存到数据库
	 * @throws SQLException
	 */
	public void save2Db() throws SQLException
	{
		Connection con=new DbManager().getConnection();	
				
		PreparedStatement ps=con.prepareStatement("insert into wb_poisEffect(pid,ukey,tag,num,savetime,distance) values(?,?,?,?,?,?)");
		ps.setString(1,pid);
		ps.setString(2,key);
		ps.setString(3, tag);
		ps.setInt(4, num);
		ps.setTime(5,new java.sql.Time(Calendar.getInstance().getTime().getTime()));
		ps.setInt(6,distance);
		
		ps.executeUpdate();	
	}
}
