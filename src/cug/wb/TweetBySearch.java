package cug.wb;

import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cug.crawer.DbManager;
import cug.crawer.MultiCookieCrawer;
import cug.tools.Config;
import cug.wb.api.PoiAPI;
import cug.wb.api.WbApiRequest;
import cug.wb.entity.PoiEntity;
import cug.wb.entity.TweetCrawel;
import cug.wb.entity.TweetEntity;
import cug.wb.tools.TweetUpdateByAPI;

/**
 * 通过搜索获取tweet,结果数据存储在wb_tweet表中
 * 
 * @author li
 * 
 */
public class TweetBySearch extends MutiCrawSearcherByCookie {
	// CrawByCookie cbc = new CrawByCookie(null);

	private Date lastTweetTime=null;
	private int nCrawed=0;
	/**
	 * 测试函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TweetBySearch tbts = new TweetBySearch(
				"config\\com.weibo.txt,config\\wb.lsw40001.txt,config\\wb.lsw40002.txt,config\\wb.lsw40003");
		tbts.TopicSearchOnePage(
				"http://place.weibo.com/weibo/B2094757D065AAFF409D/2", "test");
	}

	/**
	 * 获取数据结果的页数，最多是50页
	 * 
	 * @param s
	 * @return
	 */
	public int getPageNum(String s) {
		int n = 1;
		int nStart = 0;
		while (nStart != -1) {
			nStart = s.indexOf("key=tblog_search_v4.1&amp;value=weibo_page");
			if (n == -1)
				return -1;
			nStart = s.indexOf(">");
			String strNum = s.substring(nStart + 1, s.indexOf("<"));
			try {
				n = Integer.parseInt(strNum);
			} catch (Exception e) {
			}
		}
		return n;

	}

	public TweetBySearch(String configFiles) {
		mcc = new MultiCookieCrawer(configFiles);
	}

	/**
	 * 抓取基础URL及其随后50页的微博数据
	 * 
	 * @param url
	 *            基础URL
	 */
	public long TopicSearchPages(String url, String tag, String key,
			String location) {

		// url=URLEncoder.encode(url);

		TweetCrawel wtc = new TweetCrawel();
		wtc.setUrl(url);
		wtc.setTag(tag);
		wtc.setKey(key);
		wtc.setCity(location);
		wtc.setPage(0);

		// first page
		long n = 0;//需要抓取的总数
		long nTotal=0;//成功抓取的数目
		if (wtc.isExist()&& wtc.getIsOk() == 1) {
			n = wtc.getTweetNum();
			org.apache.log4j.Logger.getLogger("weibo").info(					"Ignore the crawled:" + url);
		} else {
			String s = this.getValidContent(url, 50);
			if (s == null)
				wtc.setIsOk(0);
			else
				wtc.setIsOk(1);
			n = new TweetParser().getSearchResultNum(s);
			wtc.setTweetNum(n);
			int x = ParsePage(s, tag);
			nTotal+=x;
			wtc.save2Db();
			org.apache.log4j.Logger.getLogger("weibo").info("find " + x + " tweets in Page 1");
			
			
		}

		// page 1 to 50
		if (n == 0)
			return 0;
		long nPage = ((n - 1) / 20 + 1);
		

		// 只能抓取50页数据
		for (int i = 1; i < nPage && i < 50; i++) {
			// int nPage = i + 1;
			String newUrl = url + "&page=" + (i + 1);
			
			wtc.setUrl(newUrl);
			
			//最后一页一定要抓取
			if (i!=49 && wtc.isExist() && wtc.getIsOk() == 1) {
//				if(wtc.getTweetNum()>1000)
//				{
//				//	i=48;
//				}
//				else
//				{
				org.apache.log4j.Logger.getLogger("weibo").info(
						"Ignore the crawled:" + newUrl);
				//}
				continue;
			}
			
			int x = TopicSearchOnePage(newUrl, tag);
			nTotal = nTotal + x;
			//nTotal
			wtc.setUrl(newUrl);
			wtc.setPage(i);
			wtc.setTweetNum(n);

			if (x > 0) {
				wtc.setIsOk(1);
			} else
				wtc.setIsOk(0);
			wtc.save2Db();
			
		

		}
		org.apache.log4j.Logger.getLogger("weibo").info(
				"find total " + nTotal + " tweets in " + url + " and its links!");
		return n;
	}

	/***
	 * 根据URL抓取其中的微博数据
	 * 
	 * @param url
	 */
	public int TopicSearchOnePage(final String url, String tag) {

		int n = 0;
		String s = this.getValidContent(url, 50);// mcc.getContent(url);
		n = ParsePage(s, tag);
		org.apache.log4j.Logger.getLogger("weibo").info(
				"find " + n + " tweets ");

		return n;
	}

	/**
	 * 分析内容一个页面中的内容，并存入数据库
	 * 
	 * @param content
	 * @param tag
	 * @return 成功存储的数据
	 */
	private int ParsePage(String content, String tag) {
		TweetParser ts = new TweetParser();
		List<TweetEntity> ls = ts.handle(content);

		try {
			for (int i = 0; i < ls.size(); i++) {
				ls.get(i).setTag(tag);
				// org.apache.log4j.Logger.getLogger("weibo").info(ls.get(i).getContent());
				int n = tag.lastIndexOf("_");

				if (n > 0) {
					String city = tag.substring(n + 1);
					ls.get(i).setCity(city);
				}
				
				ls.get(i).save2Db();
				
				if(lastTweetTime.after(ls.get(i).getDate()))
					this.lastTweetTime=ls.get(i).getDate();
				this.nCrawed++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ls.size();
	}

	@Override
	/**
	 * 搜索指定时间段内所有的微博
	 */
	public void searchByTimeLocation(String key, String location, Date dtStart,
			Date dtend, String tag) {
		// 记录抓取的最后一条微博，也就是时间最靠前的微博（因为微博显示是逆序的）
	
		
			try {
				if(dtStart==null)
				dtStart=new SimpleDateFormat("yyyyMMdd").parse("20000101");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(dtend==null)
			dtend=new Date();
		
		
		this.lastTweetTime=dtend;;	
		
		
		long n=1001;
		for(;n>1000&&!dtend.before(dtStart);)
		{
			SimpleDateFormat  sdf=new SimpleDateFormat("yyyyMMddHH");
			String newTag = String.format("%s_%s_%s_%s", tag, key, sdf.format(dtStart),location);
			String url = this.CombinUrl(key, location, dtStart, lastTweetTime, newTag);
			 n=TopicSearchPages(url, newTag, key, location);
			
			 //如果最后一小时的内容都超过1000条了，只有放弃多余1000条的那条数据
			 if(dtend.getTime()-lastTweetTime.getTime()<60*60*1000)
			 {
				 dtend.setTime(dtend.getTime()-60*60*1000);
			 }
			//否则，抓取前面的数据
			 else
			 {				 
				 dtend= lastTweetTime;
			 }
				 
		}

	}

	/**
	 * http://weibo.com/p/100101B209475DD765A2FF429A
	 * 
	 * @param tableName
	 * @return
	 */
	public int geo2LonLat(String tableName, String setting) {
		DbManager db = new DbManager();
		String access_token = Config.getStringProperty(
				setting + "access_token", "");

		// war=new
		// GetPoi(Config.getStringProperty(setting+"apiCfgfile","config\\api.get.txt"),access_token
		// );

		PoiAPI gp = new PoiAPI(Config.getStringProperty(setting + "apiCfgfile",
				"config\\api.get.txt"), access_token);
		ResultSet rs = db.getResult("select * from " + tableName
				+ " where geo like '%/p/1001%' and lat<0.1");

		for (;;) {
			try {
				if (!rs.next())
					break;
				String geo = rs.getString("geo");
				int n = geo.indexOf("/p/100101");
				//100101
				if(n>0)
				{
					String uid = rs.getString("uid");
					
					String geo2 = geo.substring("/p/100101".length() + n);					
					if (geo2.contains("_")) {
							TweetUpdateByAPI h=new TweetUpdateByAPI();
							h.addLatLonByUrl(db,geo,tableName,uid);
						}
						else
						{
						PoiEntity pe = gp.GetPoiById(geo2);					
						String strSql = "update " + tableName + " set lat="
								+ pe.getLat() + ",lon=" + pe.getLon()
								+ " where uid=" + uid;
						db.ExecuteUpdate(strSql);
						try {
							Thread.sleep(25000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
				
				n = geo.indexOf("/p/100135");
				//100101
				if(n>0)
				{
					String str=this.mcc.getContent(geo);
					n=str.indexOf("poiid=");
					
					 
				
				}
				
				

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 将wb_tweet表中的city编码变换为cityname
	 * @param citiesName
	 * @param pots
	 */
	public void cityCode2Name(String tableName,String citiesName, String[] pots) {
		try {
		String []cities=citiesName.split(",");
		Statement st = new DbManager().getStatement();
		for (int i = 0; i < pots.length; i++) {		
		String strSql = String
				.format("update "+tableName+" set city='%s' where city like '%s' ",
						cities[i], pots[i]);
		
			st.execute(strSql);
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 已经废弃。从Content字段获取数据到table字段
	 * @param tableName
	 * 
	 */
	@Deprecated
	public void addGeo(String tableName){
		
		return ;
	}
	/**
	 * 添加地理坐标到表中
	 * @param tableName
	 * @param setting
	 */
	public void UpdateByAPI(String tableName,String setting)
	{
		String access_token = Config.getStringProperty(
				setting + "access_token", "");

		// war=new
		// GetPoi(Config.getStringProperty(setting+"apiCfgfile","config\\api.get.txt"),access_token
		// );

//		GetPoi gp = new GetPoi(Config.getStringProperty(setting + "apiCfgfile",
//				"config\\api.get.txt"), access_token);
		
		new TweetUpdateByAPI().AddUserInfo(tableName, 
				Config.getStringProperty(setting + "apiCfgfile","config\\api.get.txt"),
				access_token);
	}
	
	public int deleteLaterDuplicateTweetByMid()
	{
		int n=0;
		DbManager db=new DbManager();
		
		try
		{
			db.ExecuteUpdate("drop table tweettmp");
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		try
		{
			n=   db.ExecuteUpdate("create table tweettmp select min(uid) as uid from wb_tweet group by mid");
			int x=db.ExecuteUpdate("delete from wb_tweet where uid not in (select uid from tweettmp)");
			System.out.println("删除:"+x+",保留:"+n+"条数据");
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return n;
	}
}
