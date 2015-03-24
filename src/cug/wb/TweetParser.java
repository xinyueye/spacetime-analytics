package cug.wb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cug.crawer.DbManager;
import cug.tools.CharsetTranslate;
import cug.wb.entity.TweetEntity;

/**
 * Weibo搜索结果的解析类
 * 
 * @author li
 * 
 */
public class TweetParser {
	String mstr;

	public TweetParser() {

	}

	List<String> tweets;

	/**
	 * 获取搜索结果的数目
	 * 
	 * @param str
	 *            网页HTML
	 * @return
	 */
	public long getSearchResultNum(String str) {

		long nNum = -1;

		if (str.equals("File not found.\n")
				|| str.indexOf("$CONFIG['islogin'] = '1'") == -1) {
			org.apache.log4j.Logger.getLogger("weibo").error(
					"--------抓取过程失败！原因：认证信息过期！");
			// throw new RuntimeException("--------抓取过程失败！原因：认证信息过期！");
		}
		String strNum = "-1";
		int nStart = str.indexOf("noresult_tit");
		if (nStart > 0) 
			return 0;

			nStart = str.lastIndexOf("search_num");
			if (nStart < 0) {// 不含有 Search_Num
				// org.apache.log4j.Logger.getLogger("weibo").info(str);
				String[] strs = splitTweets(str);
				if (strs.length > 0)
					nNum = strs.length - 1;
				else
					nNum=-2;
			} else { // 含有Search_Num
				nStart = str.indexOf("u5230", nStart + 10);

				int nEnd = str.indexOf("\\", nStart + 5);
				strNum = str.substring(nStart + 5, nEnd);

				strNum = strNum.trim();
				if (strNum != null && strNum.length() > 0)
					nNum = Long.parseLong(strNum);
		}
		org.apache.log4j.Logger.getLogger("weibo").info("查询结果：" + nNum);

		return nNum;

	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public String[] splitTweets(String str) {
		return str.split("feed_list feed_list_new W_linecolor");// <dl\\s*class.{0,6}feed_list\\s*W_linecolor");
	}

	/**
	 * 分析搜索结果
	 * 
	 * @param str
	 * @return
	 */
	public List<TweetEntity> handle(String str) {

		List<TweetEntity> twList = new ArrayList<TweetEntity>();
		String[] strTweets = splitTweets(str);
		if (strTweets.length < 3)
			org.apache.log4j.Logger.getLogger("weibo").info(
					"can't find tweets:" + str);

		for (int i = 1; i < strTweets.length; i++) {
			TweetEntity te = ParserOneTweet(strTweets[i]);
			if (te != null)
				twList.add(te);
			// org.apache.log4j.Logger.getLogger("weibo").info();
		}
		// org.apache.log4j.Logger.getLogger("weibo").info(strTweets.length -
		// 1);

		return twList;
	}

	/**
	 * 清理微博html格式到文本格式
	 * 
	 * @param s
	 * @return
	 */
	public String clearContent(String s) {
		StringBuffer sb = new StringBuffer();
		String[] tokens = { "a" };

		int nIndex = 0;
		// 去除某些tag及其中间的内容
		do {
			int nStart = s.length();
			int nEnd = 0;

			int n1 = 0;
			nIndex = -1;
			int n2 = 0;
			for (int i = 0; i < tokens.length; i++) {
				n1 = s.indexOf("<" + tokens[i]);
				n2 = s.indexOf(tokens[i] + ">");
				if (n1 > -1 && n1 < nStart && n2 > -1 && n2 > n1) {
					nIndex = i;
					nStart = n1;
					nEnd = n2;
					// nStart<
				}
			}

			// 去除某些标签
			if (nIndex > -1) {
				sb.append(s.substring(0, nStart));
				sb.append(s.substring(nEnd + 2));
				s = sb.toString();
			}

			sb = new StringBuffer();
		} while (nIndex > -1);

		do {
			nIndex = -1;
			if (s.indexOf("<") > -1 && s.indexOf(">") > -1
					&& s.indexOf(">") > s.indexOf("<")) {
				sb.append(s.substring(0, s.indexOf("<")));
				sb.append(s.substring(s.indexOf(">") + 1));
				nIndex = 0;
				s = sb.toString();
			}

			sb = new StringBuffer();
		} while (nIndex > -1);

		return s;
	}

	public String oldClearConten(String s) {
		int n = s.indexOf("<em>");
		if (n < 0)
			return "";

		int nEnd = s.indexOf("</em>", n);
		if (n < 0)
			n = s.length();
		s = s.substring(n + 4, nEnd);
		StringBuffer sb = new StringBuffer();
		int nLeft = 0, nRight = 0;// nEnd=0;

		//
		nLeft = s.indexOf("<", nRight);
		for (; nLeft > -1 && nRight > -1;) {
			// nRight=s.indexOf(">",nLeft);
			String str = s.substring(nRight, nLeft);
			sb.append(str);
			nRight = s.indexOf(">", nLeft);
			nRight++;
			nLeft = s.indexOf("<", nRight);
		}
		sb.append(s.substring(nRight));
		return sb.toString();
	}

	/**
	 * 分析一条微博内容
	 * 
	 * @param string
	 * @return
	 */
	public TweetEntity ParserOneTweet(String string) {
		String strTweet = string;
		TweetEntity te = new TweetEntity();
		// mid
		String pat = "mid[^0-9]{0,5}([0-9]*)";
		Matcher m = Pattern.compile(pat).matcher(string);
		int n = 0;
		if (!m.find()) {
			org.apache.log4j.Logger.getLogger("weibo").info(
					"can't find mid:" + string);
			return null;
		}

		te.setMid(m.group(1));
		// org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));

		string = CharsetTranslate.unicodeToUtf8(string);
		string = string.replace("\n", "");
		string = string.replace("\r", "");

		// date
		// title="2014-07-09 18:14" date="1404900850000" class="date"
		pat = "title=.{0,5}([0-9]{4}-[0-9]{2}-[0-9]{2}.[0-9]{2}:[0-9]{2})";// title=\"([^\"]{1,32})\".{1,16}date=";
		m = Pattern.compile(pat).matcher(string);
		if (!m.find()) {
			org.apache.log4j.Logger.getLogger("weibo").info(
					"can't find date:" + string);
			return null;
		}

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		// String str = formatDate.format(date);
		try {
			Date dt = formatDate.parse(m.group(1));
			te.setDate(dt);
		} catch (ParseException e) {
			org.apache.log4j.Logger.getLogger("weibo").info(
					"formatDate.parse error:" + m.group());
			e.printStackTrace();
		}

		// org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));

		// user href
		// href="http://..."
		pat = "href.{0,32}\\\"(http[^\"]{1,64})";
		m = Pattern.compile(pat).matcher(string);
		if (!m.find()) {
			org.apache.log4j.Logger.getLogger("weibo").info(
					"can't find url:" + string);
			return null;
		}
		te.setUrl(m.group(1));
		// org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));

		// title
		// title="..."
		pat = "title[^\"]{1,128}\"([^\"]{1,128})";
		m = Pattern.compile(pat).matcher(string);
		if (!m.find()) {
			org.apache.log4j.Logger.getLogger("weibo").info(
					"can't find title:" + string);
			return null;
		}
		String str = m.group(1);
		te.setTitle(str.substring(0, str.length() - 1));
		// org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));

		// feed_list_content
		// <p node-type="feed_list_content">..</p>
		int nStart = string.indexOf("feed_list_content");
		nStart = string.indexOf("<", nStart);
		int nEnd = string.indexOf("</p>", nStart);
		string = string.substring(nStart, nEnd);
		if (string.length() < 16) {
			org.apache.log4j.Logger.getLogger("weibo").info(
					"can't find content:" + string);
			return null;
		}
		te.setContent(string);
		string = clearContent(string);
		te.setTxt(string);

		// 转发数
		// <span class="line S_line1">转发<em>11</em></span>
		// if(nStart>0)
		nStart = strTweet.indexOf("line S_line1", nStart);
		if (nStart > 0)
			nStart = strTweet.indexOf("em>", nStart);
		if (nStart > 0) {
			int tmp = strTweet.indexOf("<", nStart);
			if (tmp > 0) {
				String tmp5 = strTweet.substring(nStart + 3, tmp).trim();

				int x = 0;
				if (tmp5.length() > 0)
					x = Integer.parseInt(tmp5);
				te.setReposts_count(x);

			}
		}
		// 评论数
		if (nStart > 0)
			nStart = strTweet.indexOf("line S_line1", nStart);
		if (nStart > 0)
			nStart = strTweet.indexOf("em>", nStart);
		if (nStart > 0) {
			int tmp = strTweet.indexOf("<", nStart);
			if (tmp > 0) {
				String tmp5 = strTweet.substring(nStart + 3, tmp).trim();
				int x = 0;
				if (tmp5.length() > 0)
					x = Integer.parseInt(tmp5);
				te.setComments_count(x);
			}
		}
		// 赞
		if (nStart > 0)
			nStart = strTweet.indexOf("W_ico12 icon_praised_b", nStart);
		if (nStart > 0)
			nStart = strTweet.indexOf("em>", nStart);
		if (nStart > 0) {
			int tmp = strTweet.indexOf("<", nStart);
			if (tmp > 0) {
				String tmp5 = strTweet.substring(nStart + 3, tmp).trim();
				int x = 0;
				if (tmp5.length() > 0)
					x = Integer.parseInt(tmp5);
				te.setAttitudes_count(x);
			}
		}

		// 地理坐标
		// <a href="javascript:void(0);" action-type="feed_list_geo_info"
		// action-data="geo=121.48231,31.2304&amp;head=http://tp2.sinaimg.cn/1067104425/50/40038773763/1&amp;title=王仙客929-上海市,黄浦区,延安东路">显示地图</a>
		nStart = strTweet.indexOf("feed_list_geo_info");
		if (nStart > 0) {
			nStart = strTweet.indexOf("geo=", nStart);
			nEnd = strTweet.indexOf("&", nStart);
			if (nEnd > nStart + 4) {
				string = strTweet.substring(nStart + 4, nEnd);
				String[] strs = string.split(",");
				if (strs.length == 2) {
					org.apache.log4j.Logger.getLogger("weibo").info(
							" find geo:" + string);
					te.setGeo(string);
					try {
						te.setLon(Double.parseDouble(strs[0]));
						te.setLat(Double.parseDouble(strs[1]));
					} catch (Exception ex) {
					}
				}
			}
		}

		// org.apache.log4j.Logger.getLogger("weibo").info(string);

		// pat = "<p.{1,16}feed_list_content.*(</p>)";
		// m = Pattern.compile(pat).matcher(string);
		// if (!m.find()) {
		// return false;
		// }
		// org.apache.log4j.Logger.getLogger("weibo").info("content="+m.group(0));

		//
		// // title
		// pat = "title[^\"]{1,100}\"([^\"]{1,100})";
		// m = Pattern.compile(pat).matcher(string);
		// if (!m.find()) {
		// return false;
		// }
		// org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));

		return te;
	}

	/**
	 * 将数据库中数据形成文本文件
	 * 
	 * @return
	 */
	public int Content2Txt(String fileName, String filter, String stopWord) {
		DbManager db = new DbManager();
		try {

			FileWriter fw = new FileWriter(new File(fileName));
			Statement st = db.getStatement();
			if (filter == null)
				filter = "";
			ResultSet rs = st
					.executeQuery("select distinct( txt) from wb_tweet"
							+ filter);
			int n = 0;
			if (stopWord == null)
				stopWord = "";
			String[] strs = stopWord.split(";");
			while (rs.next()) {
				String s = rs.getString(1);

				for (int i = 0; i < strs.length; i++)
					s = s.replace(strs[i], "");
				// 去掉小于14个字的微博
				if (s.length() > 14) {
					fw.append(s);
					fw.append("\r\n");
					n++;
				}
			}
			fw.flush();
			fw.close();
			return n;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		return 0;

	}

	/**
	 * 数据库中的所有tweet内容都做重新清理,记录文本
	 * 
	 * @return
	 */
	public int clearAllContent() {
		DbManager db = new DbManager();
		try {
			Statement st = db.getStatement();
			Connection con = db.getConnection();
			PreparedStatement ps = con
					.prepareStatement("update wb_tweet set txt=? where uid=?");
			ResultSet rs = st.executeQuery("select * from wb_tweet");
			int n = 0;
			while (rs.next()) {
				int uid = rs.getInt("uid");
				String s = rs.getString("content");
				s = this.clearContent(s);
				ps.setInt(2, uid);
				ps.setString(1, s);
				ps.execute();
				n++;
			}
			return n;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		return 0;
	}

}
