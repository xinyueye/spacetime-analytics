package cug.wb;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cug.crawer.MultiCookieCrawer;
import cug.wb.entity.LSTNumEntity;

/**
 * 根据时间、地点、内容进行微博内容统计 。数据存储在数据库中
 * @author li
 * 
 */
public class StaticsBySearch extends MutiCrawSearcherByCookie {
	//MultiCookieCrawer mcc = null;
	private String strSQL;

	
	/**
	 * 以逗号分开如 config/com.weibo,config\com.weibo.360
	 * 
	 * @param configFiles
	 */
	public StaticsBySearch(String configFiles) {
		mcc = new MultiCookieCrawer(configFiles);
		// mutiCookieCrawer

	}
 /**
	 * 抓取指定月份内，指定地点中的微博数量
	 * 
	 * @param keys 关键字列表
	 * 
	 * @param location
	 *            位置
	 * @param year
	 *            开始年份 (1-2099)
	 * @param month
	 *            开始月份 (1-12)
	 *@param day 开始天（一般为1)
	 *
	 *   @param times 次数 
	 *    
	 * @param tag ，标记
	 *            
	 */
 
	public void SearchByMonthLocation(String []keys, String []localtions, int year,int month,int days, int times, String tag) {
		
		int months=year*12+month-1;	
		
		for(int i=0;i<times;i++)		
		{
			Calendar c=Calendar.getInstance();
			c.clear();
			c.set(months/12,months%12,1);
			Date dta=c.getTime();
			
			months++;
			c.set(months/12,months%12,1);
			Date		 dtb=c.getTime();
			
			long nhours=(dtb.getTime()-dta.getTime())/(1000*60*60);
	
						SearchByTimeLocation(keys, localtions,dta, (int)nhours, 1,tag);
		}

	}

	/**
	 * 抓取指定时间段内，指定地点中的微博数量
	 * 
	 * @param location
	 *            位置
	 * @param dtStart
	 *            开始时间
	 * @param dtend
	 *            结束时间
	 * @param url
	 *            基础url
	 */
	public void searchByTimeLocation(String key, String location, Date dtStart,
			Date dtend, String tag) {

		LSTNumEntity lstn = new LSTNumEntity();
		lstn.setLocation(location);
		lstn.setDtEnd(dtend);
		lstn.setDtStart(dtStart);
		lstn.setTopic(key);

		lstn.setTag(tag);

		if (lstn.find(tag.split(":")[0])) {
			System.out
					.println(String.format("已经抓取%s,%s,%s-%s，不再重复抓取", key,
							location, dtStart.toLocaleString(),
							dtend.toLocaleString()));
			return;
		}

		// &region=custom:42:1&timescope=custom:2014-07-15-10:2014-07-15-16
		String url = this.CombinUrl(key, location, dtStart, dtend, tag);
		String str=getValidContent(url,50);
		long nNum=new TweetParser().getSearchResultNum(str);
		System.out.println(nNum);
		lstn.setNum(nNum);
		try {
			lstn.saveToDb();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

}

/**
 * Main函数用来做测试
 * @param args
 * @throws ParseException
 */
	static public void main(String args[]) throws ParseException {
		StaticsBySearch sbs = new StaticsBySearch(
				"config\\com.weibo,config\\wb.lsw40001,config\\wb.lsw40002,config\\wb.lsw40003");

		Date dtStart = null;
		dtStart = new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.parse("2014-07-20 12:00");
		String cities = "42:1,42:2,42:3";
		String topics = "pm2.5,污染,雾霭";
		sbs.SearchByTimeLocation(topics.split(",")
				, cities.split(","), dtStart, 1, 48, "test");

	}

}
