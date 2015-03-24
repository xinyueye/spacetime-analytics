package cug.wb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import cug.crawer.MultiCookieCrawer;

/**
 * 抽象类，用于多cookie的搜索
 * 
 * @author li
 * 
 */
public abstract class MutiCrawSearcherByCookie {

	protected MultiCookieCrawer mcc;
	protected String baseUrl = "http://s.weibo.com/wb/";

	/**
	 * 合并URL
	 * 
	 * @param key
	 * @param location
	 * @param dtStart
	 * @param dtend
	 * @param tag
	 * @return
	 */
	public String CombinUrl(String key, String location, Date dtStart,
			Date dtend, String tag) {
		// &region=custom:42:1&timescope=custom:2014-07-15-10:2014-07-15-16

		String url = null;
		try {
			url = this.baseUrl + URLEncoder.encode(key, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (location != null && location.length() > 0)
			url += "&region=custom:" + location;

		String datePara = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
		if (dtStart != null) {
			datePara = ":" + sdf.format(dtStart);
		}
		if (dtend != null) {
			datePara += ":" + sdf.format(dtend);
		}

		if (datePara.length() > 0) {
			url = url + "&timescope=custom" + datePara;
		}
		url = url + "&scope=ori&nodup=1";

		return url;
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
	abstract public void searchByTimeLocation(String key, String location,
			Date dtStart, Date dtend, String tag);

	/**
	 * 根据开始时间，所在位置，搜索关键字获取搜索结果 * @param keys 搜索关键字集合,以逗号分隔 * @param localtions
	 * 城市集合,以逗号分隔
	 * 
	 * @param dt
	 *            开始时间
	 * @param hours
	 *            时间段
	 * @param times
	 *            时间段次数
	 * @param tag
	 *            标识
	 */
	// public void SearchByTimeLocation(String keys, String locations, Date dt,
	// int hours, int times, String tag) {
	// SearchByTimeLocation(keys, locations, dt, hours, times, tag, ",");
	// }

	/**
	 * 根据开始时间，所在位置，搜索关键字获取搜索结果 * @param keys 搜索关键字集合,以逗号分隔 * @param localtions
	 * 城市集合,以逗号分隔
	 * 
	 * @param keys
	 *            搜索的关键字列表
	 * @param localtions
	 *            搜索的位置列表
	 * @param dt
	 *            开始时间
	 * @param hours
	 *            时间段
	 * @param times
	 *            时间段次数
	 * @param tag
	 *            标识
	 */
	public void SearchByTimeLocation(String[] keys, String[] localtions,
			Date dt, int hours, int times, String tag) {

		// int retryTimes = 0;// isRetry=false;

		String[] keyList = keys;// .split(token);
		for (int j = 0; j < times; j++) {
			for (int k = 0; k < keyList.length; k++) {
				String[] cities = new String[1];

				if (localtions != null
						&& (localtions.length > 1 || (localtions[0] != null && localtions[0]
								.length() > 0)))
					cities = localtions;// .split(token);
				for (int i = 0; i < cities.length; i++) {
					if (dt == null)
						times = 1;
					// if (retryTimes > 1) {
					//
					// }

					Date dStart = null;
					Date dEnd = null;
					if (dt != null) {
						dStart = new Date(dt.getTime() + ((long) 60) * 60
								* 1000 * hours * j);// null;
						dEnd = new Date(dStart.getTime() + ((long) 60) * 60
								* 1000 * (hours - 1));
					}
					// try {
					searchByTimeLocation(keyList[k], cities[i], dStart, dEnd,
							tag);
					// retryTimes = 0;
					// } catch (Exception ex) {
					// ex.printStackTrace();
					// // j--;
					// //retryTimes++;
					// }

					// try {
					// Thread.sleep(40000);
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					//
					// }
				}
			}
		}
	}

	public boolean isValid(String str) {
		if (str.indexOf("404 Not Found") > 0)
			return false;
		if (str.indexOf("signup.php") > 0)
			return false;
		if (str.indexOf("login.php") > 0)
			return false;
		if (str.equals("File not found.\n")
				|| str.indexOf("$CONFIG['islogin'] = '1'") == -1) {
			org.apache.log4j.Logger.getLogger("weibo").error(
					"--------抓取过程失败！原因：认证信息过期！");
			// throw new RuntimeException("--------抓取过程失败！原因：认证信息过期！");
			return false;
		}
		return true;
	}

//	public String tryGetValidContent(String url ,int minLength)
//	{
//		String s = mcc.getContent(url);
//		if (s.length() < minLength) {
//			//nNumFailed++;
//			org.apache.log4j.Logger.getLogger("weibo").info(
//					s + "-小于最少长度:" + minLength);
//		//	nNumFailed++;
//		}
//		return s;
//	}
	/***
	 * 获取有效的内容，如果没有效，则重复获取
	 * 
	 * @param url
	 * @return
	 */

	public String getValidContent(String url, int minLength) {
		int nNumFailed = 0;
		// boolean isFailed = false;
		String s = "";
		while (nNumFailed > -1) {
			
			// 试着获取数据
			try {
			
				if (nNumFailed == 0) {
					s = mcc.getContent(url);				
				}
				// 重试
				else {
					s = mcc.retryGet();
				}
				if (s.length() < minLength|| !isValid(s)) 
					nNumFailed++;
					
			} catch (Exception ex) {
				ex.printStackTrace();
				nNumFailed++;
			}
			//休眠
			try {
				int nSecond = 15;
				if (nNumFailed > 0) {
					org.apache.log4j.Logger.getLogger("weibo").info(
							nNumFailed + " retry...");
				} else if (nNumFailed > mcc.getCbcsSize()) {
					org.apache.log4j.Logger.getLogger("weibo").info(
							nNumFailed + " retry...sleep 2 minutes");
					Thread.sleep(2 * 60 * 1000);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block				
				e.printStackTrace();
				continue;
			}

			//试着获取数据
			TweetParser ts = new TweetParser();
			try {
			
				long n=ts.getSearchResultNum(s);
				if(n<0)
					nNumFailed++;		
				else
				{
					nNumFailed = -1;
					return s;
				}
			
				//return s;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				nNumFailed++;				
				e.printStackTrace();			
				}

		}

		return null;
	}

	public MutiCrawSearcherByCookie() {
		super();
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

}