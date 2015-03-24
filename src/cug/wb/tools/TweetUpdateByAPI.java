package cug.wb.tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import cug.crawer.CrawByCookie;
import cug.crawer.DbManager;
import cug.tools.Config;
import cug.wb.api.WeiboTweetsAPI;

public class TweetUpdateByAPI {

	protected HttpClient client;

	/**
	 * @param args
	 *            从html中发现内容 <a class=\"W_btn_c\" href=\"http://t.cn/R7c4VCm\" >
	 *            <a class=\"W_btn_c\" href=\"http://t.cn/R7IXcXQ\" ><span
	 *            class=\"W_btn_tag\">
	 *            <em class=\"W_btn_icon\"><i class=\"W_ico12 icon_cd_place\"></i>
	 *            <i class=\"W_vline S_line1_c\">|</i></em>
	 *            <em class=\"W_autocut S_link1\">中華四路</em></span></a></em>
	 */
	// ////
	public static void main(String[] args) {
		
		String setting="wb.api.lsw4000.";
		TweetUpdateByAPI hfg = new TweetUpdateByAPI();
		
		String access_token = Config.getStringProperty(
				setting + "access_token", "");

	
		hfg.AddUserInfo("wb_tweet", 
				Config.getStringProperty(setting + "apiCfgfile","config\\api.get.txt"),
				access_token);
		

	}

	/**
	 * 从content中获取地理位置隐含信息，添加数据到geo，lat,lon中
	 * 
	 * @param tableName
	 * @return
	 */
	@Deprecated
	public int AddGeoFields(String tableName) {

		int n = 0;
		DbManager db = new DbManager();

		ResultSet rs = db
				.getResult("select uid, content from "
						+ tableName
						+ " where content like '%W_btn_c%'  and content like '%place%' and   isnull(geo) && lat<0.1");

		try {
			while (rs.next()) {
				try {
					String uid = rs.getString(1);
					String content = rs.getString(2);
					String url = findUrl(content);

					// if(url.length()>0)
					// geo= getGeoFromShortUrl(url);
					// else
					// continue;
					url = getRedirectURL(url);
					if (url.length() < 0)
						continue;

					// String geo=findGeoFromHtml(content);
					String strSql = String.format(
							"update %s set geo='%s' where uid=%s", tableName,
							url, uid);
					db.ExecuteUpdate(strSql);
					if (addLatLonByUrl(db, url, tableName, uid) > 0) {
						org.apache.log4j.Logger.getLogger("weibo").info(
								"No." + n++ + " AddGeoFields finished");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return n;
	}

	/**
	 * 从content中获取地理位置信息、评论数等、用户信息等
	 * 
	 * @param tableName
	 * @return
	 */
	public int AddUserInfo(String tableName, String cfgFile, String token) {

		int n = 0;
		DbManager db = new DbManager();
		WeiboTweetsAPI gt = new WeiboTweetsAPI(cfgFile, token);

			try{
		ResultSet rs = db.getResult("select  count( mid) from " + tableName
				+ " where isnull(geo) or  geo not like '%API%'");
		rs.next();
		int total=rs.getInt(1);
	rs=	db.getResult("select  distinct mid from " + tableName+ " where isnull(geo) or  geo not like '%API%'");
	
	    int i=0;
	    long tSt=new Date().getTime();
			while (rs.next()) {
				try {
					i++;
					String mid = rs.getString(1);

					gt.updateById(tableName, mid, true);
					// System.out.println("Sleep 25000.....");
					// Thread.sleep(25000);
					
					  long tNow=new Date().getTime();
					  long tm=(total-i)*(tNow-tSt)/1000/60/i;
					  
					  
					System.out.println(new Date().toLocaleString()+" " +i+"/"+total+",left:"+tm/60+" Hours "+tm%60+" Minutes");

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return n;
	}

	/**
	 * 从content中获取地理位置隐含信息，添加数据到geo，lat,lon中
	 * 
	 * @param tableName
	 * @return
	 */
	public int AddLocations(String tableName, String cfgFile, String token) {

		int n = 0;
		DbManager db = new DbManager();
		WeiboTweetsAPI gt = new WeiboTweetsAPI(cfgFile, token);

		ResultSet rs = db
				.getResult("select uid, mid,content from "
						+ tableName
						+ " where content like '%W_btn_c%'  and content like '%place%' and   isnull(geo) && lat<0.1");

		try {
			while (rs.next()) {
				try {
					String mid = rs.getString(2);

					gt.updateById(tableName, mid);
					// System.out.println("Sleep 25000.....");
					// Thread.sleep(25000);

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return n;
	}

	/**
	 * 直接添加
	 * 
	 * @param db
	 * @param url
	 * @param tableName
	 * @param uid
	 */
	public int addLatLonByUrl(DbManager db, String url, String tableName,
			String uid) {
		String geo = "";
		int n = url.indexOf("p/100101");
		if (n > 0)
			geo = url.substring(n + "p/100101".length());
		if (geo != null & geo.length() > 0 && geo.indexOf("_") > -1) {
			String[] locs = geo.split("_");
			if (locs[0].contains(".") && locs[1].contains(".")) {
				String str = String.format(
						"update %s set lon=%s,lat=%s where uid=%s", tableName,
						locs[0], locs[1], uid);
				db.ExecuteUpdate(str);
				return 1;
			}
			// n++;
		}
		return 0;

	}

	/**
	 * 
	 * @param content
	 * @return
	 */
	public String findGeoFromHtml(String content) {
		String url = findUrl(content);
		if (url.length() > 0)
			return getGeoFromShortUrl(url);
		return "";
	}

	/**
	 * 从html中发现内容 <a class=\"W_btn_c\" href=\"http://t.cn/R7c4VCm\" >
	 * 
	 * @param content
	 * @return
	 */
	public String findUrl(String content) {

		int n = content.indexOf("W_btn_c");
		if (n < 0)
			return "";
		n = content.indexOf("http://t.cn", n);
		if (n < 0)
			return "";
		int nEnd = content.indexOf("\\\"", n);
		if (n > nEnd)
			return "";
		return content.substring(n, nEnd);
		// return "http://t.cn/R7c4VCm";
	}

	/**
	 * http://weibo.com/p/100101113.07319_23.71965
	 * 
	 * @return
	 */
	public String getGeoFromShortUrl(String url) {
		// CrawByCookie cbc = new CrawByCookie("config\\wb.lsw4000.txt");
		// String url=" http://weibo.com/p/100101113.07319_23.71965";
		url = getRedirectURL(url);
		int n = url.indexOf("p/100101");
		if (n == -1)
			return "";
		return url.substring(n + "p/100101".length());
	}

	/**
	 * 获得重定向的URL
	 * 
	 * @param url
	 * @return
	 */

	public String getRedirectURL(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String location = null;
		int responseCode = 0;
		try {
			final HttpGet request = new HttpGet(url);
			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects", false); // 默认不让重定向
			// 这样就能拿到Location头了
			request.setParams(params);
			HttpResponse response = httpclient.execute(request);
			responseCode = response.getStatusLine().getStatusCode();
			Header[] headers = response.getAllHeaders();
			if (responseCode == 302) {
				Header locationHeader = response.getFirstHeader("Location");
				if (locationHeader != null) {
					location = locationHeader.getValue();
					SimpleDateFormat df = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");// 设置日期格式
					org.apache.log4j.Logger.getLogger("weibo").info(
							df.format(new Date()) + "No." + "解析" + url + "->"
									+ location);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

}
