package cug.wb.api;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;

import org.apache.http.client.HttpClient;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import cug.crawer.CrawByCookie;
import cug.crawer.DbManager;
import cug.tools.CharsetTranslate;
import cug.tools.Config;
import cug.wb.entity.PoiEffectEntity;
import cug.wb.entity.StatueEntity;
import cug.wb.entity.TweetEntity;
import cug.wb.login.HttpTools;

public class NearByTweets  {
	private Config config;

	private String baseUrl = "https://api.weibo.com/2/place/nearby_timeline.json";

//	private String tag = "test";
	WbApiRequest war;
	public NearByTweets() {
		
		//super("config/api.get.txt", accessToken);

	}
	public void StartCraw(String setting,double lat1,double lon1,double lat2,double lon2,String dateStart, int days,String tag  ) {
		setting="wb.api."+setting+".";
		String access_token=Config.getStringProperty(setting+"access_token","");

		
		war=new WbApiRequest(Config.getStringProperty(setting+"apiCfgfile","config\\api.get.txt"),access_token	);
		if(access_token.length()==0)
		{
			org.apache.log4j.Logger.getLogger("weibo").info("access_toke is invalid, please check "+setting+"access_token  in config.properties");
			String client_id=Config.getStringProperty(setting+"client_id","");
			String client_secret=Config.getStringProperty(setting+"client_secret","");
			String redirect_uri=Config.getStringProperty(setting+"redirect_uri","");
			String  code=Config.getStringProperty(setting+"code","") ;		

			war.getAccessTokey(code,redirect_uri,client_id,client_secret);
		}
		
		this.getTweets(lat1, lon1, lat2, lon2, dateStart, days, tag);

	}
	public static void main(String[] args) throws ParseException {
		
		if(args.length==0)
		{
			org.apache.log4j.Logger.getLogger("weibo").info("缺少参数！");
			return ;
		}
		
		String setting=args[0];
		
	
		 Config.getStringProperty("db.driver","com.mysql.jdbc.Driver");
		 
		
		//war
		
		
		// NearBy l = new NearBy("", "");
		// l.login();
		// l.test();

		// String strTime="Sat May 31 22:06:41 +0800 2014";
		// Date dt= new
		// SimpleDateFormat("E MMM dd HH:mm:ss z yyyy",Locale.US).parse(strTime);
		// org.apache.log4j.Logger.getLogger("weibo").info(dt.toGMTString());
		//
//		NearByTweets nbtn = new NearByTweets("2.00XtOWhF0mdzcZf7fc933cffwqZxLE");
//		nbtn.getTweets();
		// long ts=new
		// SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/06/2013 00:00:00").getTime();
		// long te=new
		// SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/06/2014 00:00:00").getTime();
		// org.apache.log4j.Logger.getLogger("weibo").info(ts);
		// nbtn.getTweetsNum("800,1600","test",ts/1000,te/1000);

	}

	/**
	 * 
	 * @param ranges
	 * @param keys
	 * @param tag
	 * @return
	 */
	public int getTweetsNum(String ranges, String tag, long starttime,
			long endtime) {
		DbManager db = new DbManager();
		try {
			Statement st = db.getConnection().createStatement();

			ResultSet rs = st.executeQuery("select * from  wb_pois");

			while (rs.next()) {
				String pid = rs.getString("pid");
				double lat = rs.getDouble("lat");
				double lon = rs.getDouble("lon");
				String title = rs.getString("title");

				String strsA[] = ranges.split(",");

				for (int i = 0; i < strsA.length; i++) {
					int range = Integer.parseInt(strsA[i]);

					try {

						PoiEffectEntity pee = new PoiEffectEntity();
						pee.setDistance(range);

						pee.setPid(pid);
						pee.setTag(tag);
						if (!pee.isExist()) {
							String str = String.format("正在抓取%s %s %d...", pid,
									title, range);
							int num = getTweetNum(lat, lon, range, starttime,
									endtime);
							pee.setNum(num);
							org.apache.log4j.Logger.getLogger("weibo").info(str);
							pee.save2Db();
							Thread.sleep(25000);
						} else {
							String str = String.format("%s %s %d已经存在，不再抓取",
									pid, title, range);
							org.apache.log4j.Logger.getLogger("weibo").info(str);
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 0;
	}

	// public int getTweets(double lat,double lon, int range,long starttime,long
	// endtime) {
	// String url = "https://api.weibo.com/2/place/nearby_timeline.json?";
	// // source false string 采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
	// // access_token false string 采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
	// // lat true float 纬度，有效范围：-90.0到+90.0，+表示北纬。
	// // long true float 经度，有效范围：-180.0到+180.0，+表示东经。
	// // range false int 查询范围半径，默认为2000，最大为10000，单位米。
	// // q false string 查询的关键词，必须进行URLencode。
	// // category false string 查询的分类代码，取值范围见：分类代码对应表。
	// // count false int 单页返回的记录条数，默认为20，最大为50。
	// // page false int 返回结果的页码，默认为1。
	// // sort false int 排序方式，0：按权重，1：按距离，3：按签到人数。默认为0。
	// // offset false in
	// int n=0;
	// try {
	// url
	// =String.format("%slat=%.5f&long=%.5f062&count=50&range=%d",url,lat,lon,range);
	// url = addTime(starttime, endtime, url);
	// JSONObject obj = this.getJSon(url);
	// n = obj.getInt("total_number");
	// org.apache.log4j.Logger.getLogger("weibo").info(n);
	//
	//
	//
	// JSONArray statues = (JSONArray) obj.getJSONArray("statuses");
	// org.apache.log4j.Logger.getLogger("weibo").info(statues.toString());
	// org.apache.log4j.Logger.getLogger("weibo").info(statues.length());
	// for (int i = 0; i < statues.length(); i++) {
	// JSONObject tweet = (JSONObject) statues.get(i);
	// //"created_at":"Fri Aug 15 01:44:13 +0800 2014","id":"3743646128095463","mid":"3743646128095463",
	// //"idstr":"3743646128095463","text":"http://t.cn/RPjVXpH  http://t.cn/z8As31y",
	// //"source":"<a href=\"http://app.weibo.com/t/feed/3G5oUM\"
	// rel=\"nofollow\">
	// //iPhone
	// 5s</a>","favorited":false,"truncated":false,"in_reply_to_status_id":"",
	// //"in_reply_to_user_id":"","in_reply_to_screen_name":"",
	// //"pic_ids":[],"geo":{"type":"Point","coordinates":[30.515751,114.368088]},
	// //"user":{"id":"3220785707","idstr":"3220785707","class":1,"screen_name":"南山776",
	// //"name":"南山776","province":"42","city":"1","location":"湖北 武汉",
	// //"description":"只盼世事不要无常 就像现在这样不咸不淡 不痛不痒","url":"",
	// //"profile_image_url":"http://tp4.sinaimg.cn/3220785707/50/5702476938/1",
	// //"cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e8iktk4ohij20hs0hsmz6.jpg",
	// //"profile_url":"u/3220785707","domain":"","weihao":"","gender":"m","followers_count":91,
	// //"friends_count":65,"statuses_count":413,"favourites_count":0,
	// //"created_at":"Fri Jan 11 23:25:16 +0800 2013","following":false,"allow_all_act_msg":false,
	// //"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","ptype":0,
	// //"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3220785707/180/5702476938/1","avatar_hd":"http://ww4.sinaimg.cn/crop.0.0.640.640.1024/bff94a2bjw8ej5maklqlsj20hs0hswez.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":46,"lang":"zh-cn","star":0,"mbtype":2,"mbrank":1,"block_word":0,"block_app":0,"level":1,"type":1,"ulevel":0,"badge":{"uc_domain":0,"enterprise":0,"anniversary":0,"taobao":0,"travel2013":0,"gongyi":0,"gongyi_level":0,"bind_taobao":1,"hongbao_2014":1,"suishoupai_2014":1,"dailv":0,"zongyiji":0},"badge_top":"","extend":{"privacy":{"mobile":1},"mbprivilege":"0000000000000000000000000000000000000000000000000000000000000000"}},//
	// //"annotations":[{"place":{"poiid":"B2094655D56EA6F4459E","lon":114.368088,"title":"虎泉","type":"checkin","lat":30.515751},"client_mblogid":"iPhone-ECB36F9E-FC70-4BD3-8A47-F84F70AE591E"}],"reposts_count":0,"comments_count":4,"attitudes_count":0,"mlevel":0,"visible":{"type":0,"list_id":0},"darwin_tags":[],
	// //"distance":600}
	// TweetEntity te=new TweetEntity();
	// te.setMid(tweet.getString("id"));
	// te.setContent(tweet.getString("description"));
	// //te.setGeo(geo)
	// te.setTag(url);
	// //te.
	// String id = tweet.getString("id");
	// org.apache.log4j.Logger.getLogger("weibo").info(id);
	// }
	// //org.apache.log4j.Logger.getLogger("weibo").info(key);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return n;
	//
	// }
	public int getTweetNum(double lat, double lon, int range, long starttime,
			long endtime) {
		String url = "https://api.weibo.com/2/place/nearby_timeline.json?";
		// source false string 采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
		// access_token false string 采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
		// lat true float 纬度，有效范围：-90.0到+90.0，+表示北纬。
		// long true float 经度，有效范围：-180.0到+180.0，+表示东经。
		// range false int 查询范围半径，默认为2000，最大为10000，单位米。
		// q false string 查询的关键词，必须进行URLencode。
		// category false string 查询的分类代码，取值范围见：分类代码对应表。
		// count false int 单页返回的记录条数，默认为20，最大为50。
		// page false int 返回结果的页码，默认为1。
		// sort false int 排序方式，0：按权重，1：按距离，3：按签到人数。默认为0。
		// offset false in
		int n = 0;
		try {

			url = String.format("%s&lat=%.5f&long=%.5f062&count=1&range=%d",
					url, lat, lon, range);
			url = addTime(starttime, endtime, url);
			JSONObject obj = war.getJSon(url);
			org.apache.log4j.Logger.getLogger("weibo").info(obj.toString());
			n = obj.getInt("total_number");
			org.apache.log4j.Logger.getLogger("weibo").info(n);

			// n=num;
			// JSONArray statues = (JSONArray) obj.getJSONArray("statuses");
			// org.apache.log4j.Logger.getLogger("weibo").info(statues.toString());
			// org.apache.log4j.Logger.getLogger("weibo").info(statues.length());
			// for (int i = 0; i < statues.length(); i++) {
			// JSONObject tweet = (JSONObject) statues.get(i);
			// String id = tweet.getString("id");
			// org.apache.log4j.Logger.getLogger("weibo").info(id);
			// }
			// String key = (String) obj.optString("error");
			// org.apache.log4j.Logger.getLogger("weibo").info(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n;

	}

	int saveStatue(JSONObject obj, String tag) throws Exception {
	
			// if(obj.getBoolean(key))
			StatueEntity se = new StatueEntity();
			se.setMid(obj.getString("mid"));
			se.setUserid(obj.getJSONObject("user").getString("id"));
			se.setText(this.toUtf8(obj.getString("text")));
			JSONArray coor = obj.getJSONObject("geo").getJSONArray(
					"coordinates");
			se.setJson(obj.toString());
			se.setLat(coor.getDouble(0));// ("mid"));
			se.setLon(coor.getDouble(1));
			se.setTag(tag);
			String strTime = obj.getString("created_at");
			// Sat May 31 22:06:41 +0800 2014
			// new
			// SimpleDateFormat("E MMM dd yyyy HH:mm:ss z",Locale.US).parse(strTime);

			Date dt = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy",
					Locale.US).parse(strTime);
			se.setPosttime(new Timestamp(dt.getTime()));
			if (!se.isExist())
				se.save2Db();
			else
				return 0;
			return 1;
		

		
	}

	
/**
 * 是否已经抓取过
 * @param tag
 * @param time
 * @param lat
 * @param lon
 * @return
 */
	public boolean isCrawed(String tag, long time,double lat, double lon)
	{
		DbManager db=new DbManager();
		Statement st;
		try {
			
			if(lastTag==null)

				{String tmp=tag+":"+time+":"+lat+":"+lon;
//			org.apache.log4j.Logger.getLogger("weibo").info(tmp);
			st = db.getStatement();
			ResultSet rs;
	//		rs=st.executeQuery("select * from wb_statues where tag like '"+tmp+"%' order by uid desc");
//			if(!rs.next())
//				return false;
			
			 rs=st.executeQuery("select max(uid) from wb_statues where tag like '"+tag+":%' ");
			if(!rs.next())
				return false;
			String id=rs.getString(1);
			rs=st.executeQuery("select * from wb_statues where uid="+id);
			if(!rs.next())
				return false;
			 lastTag=rs.getString("tag");
				}
			String strs[]=lastTag.split(":");
			int dt1=Integer.parseInt(strs[1]);
			double lat1=Double.parseDouble(strs[2]);
			double lon1=Double.parseDouble(strs[3]);			

			if(time!=dt1)
				return time<dt1;
			else if(time>dt1)
				return false;
			//时间相等是进一步判断
			//当前lat明显小于
			else if( Math.abs(lat1-lat)>0.0001 )
				return lat<lat1;
			//前两者相同时
			//如果相差不大，认为没有
			else if(Math.abs(lon1-lon)<0.0001)
				return false;
			//如果
			else
				return lon<lon1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return false;
	}
	String lastTag=null;
	public int getTweets(double lat1,double lon1,double lat2,double lon2,String dateStart, int days,String tag  ) {
		// 30.35- 30.65
		// 114.15- 114.50
		// this.war.getAccessTokey("583ea7462780c60788f756e2e46f937d");
		int n = 0;	
		Date dtStart;	
		try {
			dtStart = new SimpleDateFormat("yyyy-MM-dd").parse(dateStart);//.getTime()/1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}  
		
		double r=0.04;
		int range=(int)(r*80000);
		long	ts=dtStart.getTime()/1000;
		lastTag=null;
		for( int i=1;i<days+1;i++)
		{	
			long	te=ts+24*60*60;
			double lat = lat1+r/2;
			for (; lat < lat2; lat += r) {
				double lon = lon1+r/2;
				for (; lon <lon2; lon += r) {
					if(isCrawed(tag,i,lat,lon))
					{
						org.apache.log4j.Logger.getLogger("weibo").info(String.format("%d day %f,%f已经抓取不再抓取",i,lat,lon));
					}
					else
					{
						int num = 0;
						num = this.getTweet(lat, lon, 50, range, ts , te ,tag+":"+i+":"+lat+":"+lon);
						n += num;
					}//end if 
						
				}//end for
	
			}//end for
			ts=te;
			
			try{
				DbManager db=new DbManager();
				Statement st=db.getStatement();
				String strSqlWhere =" where tag like '"+tag+":"+i+":%'";
				String sql="insert wb_statueswh	 select * from wb_statues "+strSqlWhere;
				st.execute(sql);
				sql="delete from wb_statues "+strSqlWhere;
				st.execute(sql);
				
			}catch(Exception ex)
			{
				ex.printStackTrace();
				
			}
		}//end for(int i
		return n;

	}

	public int getTweet(double lat, double lon, int count, int range,
			long starttime, long endtime,String tag) {
		String url = "https://api.weibo.com/2/place/nearby_timeline.json?";
		// source false string 采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
		// access_token false string 采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
		// lat true float 纬度，有效范围：-90.0到+90.0，+表示北纬。
		// long true float 经度，有效范围：-180.0到+180.0，+表示东经。
		// range false int 查询范围半径，默认为2000，最大为10000，单位米。
		// q false string 查询的关键词，必须进行URLencode。
		// category false string 查询的分类代码，取值范围见：分类代码对应表。
		// count false int 单页返回的记录条数，默认为20，最大为50。
		// page false int 返回结果的页码，默认为1。
		// sort false int 排序方式，0：按权重，1：按距离，3：按签到人数。默认为0。
		// offset false in
		int n = 0;
		try {

			url = String.format("%s?lat=%f&long=%f&count=%d&range=%d", baseUrl,
					lat, lon, count, range);

			url = addTime(starttime, endtime, url);
			org.apache.log4j.Logger.getLogger("weibo").info(url);
			int num = 0;
			for (int k = 1;; k++) {
				String tmp = url + "&page=" + k;
				org.apache.log4j.Logger.getLogger("weibo").info("Page " + k + "-");
				JSONObject obj = war.getJSon(tmp);
				if(obj==null)
				{
					k--;
					try	{
						Thread.sleep(25000);
					}catch(Exception e){}
					continue;
				}
				String strErr = obj.optString("error");

				if (strErr == null || strErr.length() == 0) {
					if (k == 1)
					{
						num = obj.getInt("total_number");
						org.apache.log4j.Logger.getLogger("weibo").info(tag+",total_number:"+num);
					}
					JSONArray statues = (JSONArray) obj
							.getJSONArray("statuses");

					int saved=0;
					org.apache.log4j.Logger.getLogger("weibo").info("本次获取到:"+statues.length()+"数据,");
					for (int i = 0; i < statues.length(); i++) {
						try
						{
							JSONObject tweet = (JSONObject) statues.get(i);
							int tmp1=	this.saveStatue(tweet,tag);
							saved+=tmp1;
						}
						catch(Exception ex)
						{
							ex.printStackTrace();
						}
					}				
					org.apache.log4j.Logger.getLogger("weibo").info("saved:"+saved);
					
					if (k * 50 > num) {
				//		org.apache.log4j.Logger.getLogger("weibo").info("break;k=" + k + ",num=" + num);
						Thread.sleep(25000);
						break;
					}
			//		org.apache.log4j.Logger.getLogger("weibo").info("k=" + k + ",num=" + num);
				} else {
					org.apache.log4j.Logger.getLogger("weibo").info("error:"+obj.toString());
					Thread.sleep(60000);
					k--;
				}
				Thread.sleep(25000);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n;

	}

	private String addTime(long starttime, long endtime, String url) {
		if (starttime != 0)
			url += "&starttime=" + starttime;
		if (endtime != 0)
			url += "&endtime=" + endtime;
		return url;
	}

//	private String genTag(double lat, double lon, long starttime, long endtime) {
//		return String.format("%s:%f,%f,%d-%d", tag, lat, lon, starttime,
//				endtime);
//	}

	public String toUtf8(String str) {
		return CharsetTranslate.unicodeToUtf8(str);
	}

}
