package cug.wb.entity;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;

import cug.crawer.DbManager;

/**
 * 微博实体类
 * @author li
 *
 */
public class TweetEntity {
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getContent() {
		return mcontent;
	}
	public void setContent(String mcontent) {
		this.mcontent = mcontent;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getDate() {
		return mdate;
	}
	public void setDate(Date mdate) {
		this.mdate = mdate;
	}
	public String getUrl() {
		return mUrl;
	}
	public void setUrl(String mUrl) {
		this.mUrl = mUrl;
		int n=mUrl.lastIndexOf("/");
		this.userid=mUrl.substring(n+1);
		this.userid=userid.substring(0,userid.length()-1);
	}
	public Boolean getIsForward() {
		return mIsForward;
	}
	public void setIsForward(Boolean mIsForward) {
		this.mIsForward = mIsForward;
	}
	private String city;
	private String mid;
	private String mcontent;
	private String userid;
	private Date mdate;
	private String mUrl;
	private Boolean mIsForward=false;
	private String title;
	private String tag;
	private String txt;
	private   int  reposts_count;
	private   int comments_count;
	private   int attitudes_count; 
	public String getTxt() {
		return txt;
	}
	public void setTxt(String txt) {
		this.txt = txt;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	private int nPraise;
	public int getnPraise() {
		return nPraise;
	}
	public void setnPraise(int nPraise) {
		this.nPraise = nPraise;
	}
	public int getnForward() {
		return nForward;
	}
	public void setnForward(int nForward) {
		this.nForward = nForward;
	}
	public int getnComment() {
		return nComment;
	}
	public void setnComment(int nComment) {
		this.nComment = nComment;
	}
	private String  geo;
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
	private int nForward;
	private int nComment;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	private int distance;
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
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
	private double lat;
	private double lon;
	
	/**
	 * 保存到数据库
	 * @throws SQLException
	 */
	public void save2Db() throws SQLException
	{
		Connection con=new DbManager().getConnection();	
				
		PreparedStatement ps=con.prepareStatement("insert into wb_tweet(mid,userid,posttime,url,content,txt,title,savetime,tag,geo,lat,lon,distance,city,reposts_count,comments_count,attitudes_count ) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		ps.setString(1, mid);
		ps.setString(2,userid);
		ps.setTimestamp(3, new java.sql.Timestamp(mdate.getTime()));
		ps.setString(4, mUrl);
		ps.setString(5,"");
		ps.setString(6, "");
		ps.setString(7,title);
		ps.setTimestamp(8,new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
		ps.setString(9,tag);
		ps.setString(10,geo);
		ps.setDouble(11, lat);
		ps.setDouble(12, lon);
		ps.setInt(13,distance);
		ps.setString(14, city);
		ps.setInt(15,this.reposts_count);
		ps.setInt(16, this.comments_count);
		ps.setInt(17, this.attitudes_count);
		ps.executeUpdate();	
		
		 ps=con.prepareStatement("update  wb_tweet set content=?,txt=? where mid=?");//tag,geo,lat,lon,distance,city) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		 ps.setString(1, this.mcontent);
		 ps.setString(2, txt);
		 ps.setString(3, mid);
		 ps.executeUpdate();
	}
	
	
	/**
	 * 更新地理位置信息
	 * @param tableName
	 * @return
	 */
	public boolean  updateFromApi(String tableName) 
	{
		try {
			Connection con=new DbManager().getConnection();	
					
			PreparedStatement ps=con.prepareStatement("update "+tableName
					+" set lat=? , lon=?, userid=?,reposts_count=?,comments_count=?,attitudes_count=?, geo ='API'  where mid=?");
			
			int n=1;
			
			ps.setDouble(n++, lat);
			ps.setDouble(n++, lon);
			ps.setString(n++,userid);
			
			ps.setInt(n++,this.reposts_count);
			ps.setInt(n++, this.comments_count);
			ps.setInt(n++,this.attitudes_count);
			ps.setString(n++,mid);
			ps.executeUpdate();
	
			ps=con.prepareStatement("update "+tableName
					+" set txt=? , content=?,geo=? where mid=?");
		//
			n=1;
			ps.setString(n++,txt);
		ps.setString(n++,this.mcontent);
				if(this.geo==null || this.geo.length()==0)
					this.geo="From API";
			ps.setString(n++,this.geo);
				ps.setString(n++,mid);
				ps.executeUpdate();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		return false;
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
	/**
	 * @return the reposts_count
	 */
	public int getReposts_count() {
		return reposts_count;
	}
	/**
	 * @param reposts_count the reposts_count to set
	 */
	public void setReposts_count(int reposts_count) {
		this.reposts_count = reposts_count;
	}
	/**
	 * @return the comments_count
	 */
	public int getComments_count() {
		return comments_count;
	}
	/**
	 * @param comments_count the comments_count to set
	 */
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}
	/**
	 * @return the attitudes_count
	 */
	public int getAttitudes_count() {
		return attitudes_count;
	}
	/**
	 * @param attitudes_count the attitudes_count to set
	 */
	public void setAttitudes_count(int attitudes_count) {
		this.attitudes_count = attitudes_count;
	}
	
}
