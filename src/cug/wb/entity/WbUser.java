package cug.wb.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import cug.crawer.DbManager;

/**
 * 用户实体类
 * @author li
 *
 */
public class WbUser {

	
	private String userId;
	String uclass;
	int province;
	int city;
	int followers_count;
	int friends_count;
	int statuses_count;
	int favourites_count;
	String name;
	String screen_name;
	String json;
	
	/**
	 * 保存到数据库
	 * @throws SQLException
	 */
	public void save2Db() throws SQLException
	{
		Connection con=new DbManager().getConnection();			
				
		PreparedStatement ps=con.prepareStatement(
				"insert into wb_user(userid,uclass,province,city,followers_count,friends_count,statuses_count,favourites_count,name,screen_name,json,savetime)"
		     +" values(?,?,?,? ,?,?,?,? ,?,?,?,?)");
		int n=1;
		ps.setString(n++, userId);
		ps.setString(n++,uclass);
		ps.setInt(n++,province);
		ps.setInt(n++,city);
		ps.setInt(n++,followers_count);
		ps.setInt(n++,friends_count);
		ps.setInt(n++,statuses_count);
		ps.setInt(n++,favourites_count);
		ps.setString(n++,name);
		ps.setString(n++,screen_name);
		ps.setString(n++,json);
		ps.setTimestamp(n++,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		
		ps.executeUpdate();
		
		
		
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}


	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}


	/**
	 * @return the uclass
	 */
	public String getUclass() {
		return uclass;
	}


	/**
	 * @param uclass the uclass to set
	 */
	public void setUclass(String uclass) {
		this.uclass = uclass;
	}


	/**
	 * @return the province
	 */
	public int getProvince() {
		return province;
	}


	/**
	 * @param province the province to set
	 */
	public void setProvince(int province) {
		this.province = province;
	}


	/**
	 * @return the city
	 */
	public int getCity() {
		return city;
	}


	/**
	 * @param city the city to set
	 */
	public void setCity(int city) {
		this.city = city;
	}


	/**
	 * @return the followers_count
	 */
	public int getFollowers_count() {
		return followers_count;
	}


	/**
	 * @param followers_count the followers_count to set
	 */
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}


	/**
	 * @return the friends_count
	 */
	public int getFriends_count() {
		return friends_count;
	}


	/**
	 * @param friends_count the friends_count to set
	 */
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}


	/**
	 * @return the statuses_count
	 */
	public int getStatuses_count() {
		return statuses_count;
	}


	/**
	 * @param statuses_count the statuses_count to set
	 */
	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}


	/**
	 * @return the favourites_count
	 */
	public int getFavourites_count() {
		return favourites_count;
	}


	/**
	 * @param favourites_count the favourites_count to set
	 */
	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the screen_name
	 */
	public String getScreen_name() {
		return screen_name;
	}


	/**
	 * @param screen_name the screen_name to set
	 */
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}


	/**
	 * @return the json
	 */
	public String getJson() {
		return json;
	}


	/**
	 * @param json the json to set
	 */
	public void setJson(String json) {
		this.json = json;
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
		PreparedStatement ps=con.prepareStatement("select uid from wb_user where userId=? ");
		ps.setString(1,userId);	
				
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
