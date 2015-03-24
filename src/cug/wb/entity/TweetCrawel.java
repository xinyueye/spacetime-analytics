package cug.wb.entity;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;

import cug.crawer.DbManager;

/**
 * 微博实体类
 * 
 * @author li
 * 
 */
public class TweetCrawel {

	private int uid;
	private String url;
	private int isOk;
	private int page;
	private long tweetNum;
	private String tag;
	private String key;
	private String city;

	/**
	 * 根据 URL和tag判定是否存在
	 * @return
	 */
	public boolean isExist() {
		Connection con;
		try {
			con = new DbManager().getConnection();

			PreparedStatement ps = con
					.prepareStatement("select * from  wb_tweetCrawl "
							+ "where  url=? and tag =?");
			int n = 1;
			ps.setString(n++, url);
			ps.setString(n++,tag);
			ResultSet rs = ps.executeQuery();
			//this.isOk=rs.getInt("isOk");
			if(!rs.next())
				return false;
			this.isOk=rs.getInt("isOk");
			this.tweetNum=rs.getLong("tweetNum");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 保存到数据库
	 * 
	 * @throws SQLException
	 */
	public void save2Db()  {

		Connection con;
		try {
			con = new DbManager().getConnection();
		

		PreparedStatement ps = null;
		if (isExist()) {
			ps = con.prepareStatement("update  wb_tweetCrawl"
					+ " set isok=?,page=?,tweetNum=?,tag=?,ukey=?,city=? "
					+ "where  url=?");
			int n = 1;

			ps.setInt(n++, isOk);
			ps.setInt(n++, page);
			ps.setLong(n++, tweetNum);
			ps.setString(n++, tag);
			ps.setString(n++, key);
			ps.setString(n++, city);
			ps.setString(n++, url);
			ps.executeUpdate();
		}

		// PreparedStatement
		else {
			ps = con.prepareStatement("insert into wb_tweetCrawl"
					+ "(url,isok,page,tweetNum,tag,ukey,city)  values"
					+ "(?,?,?,?,?,?,?)");
			int n = 1;
			ps.setString(n++, url);
			ps.setInt(n++, isOk);
			ps.setInt(n++, page);
			ps.setLong(n++, tweetNum);
			ps.setString(n++, tag);
			ps.setString(n++, key);
			ps.setString(n++, city);
			ps.executeUpdate();
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the isOk
	 */
	public int getIsOk() {
		return isOk;
	}

	/**
	 * @param isOk the isOk to set
	 */
	public void setIsOk(int isOk) {
		this.isOk = isOk;
	}

	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * @return the tweetNum
	 */
	public Long getTweetNum() {
		return tweetNum;
	}

	/**
	 * @param tweetNum the tweetNum to set
	 */
	public void setTweetNum(long tweetNum) {
		this.tweetNum = tweetNum;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

}
