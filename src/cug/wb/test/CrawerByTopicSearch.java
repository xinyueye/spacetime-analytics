package cug.wb.test;

import java.net.CookieStore;
import java.net.HttpCookie;

import cug.wb.TweetBySearch;

public class CrawerByTopicSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 //  CookieStore cs=new CookieStore();
		  // HttpCookie hc;
		   
           //httpclient.setCookieStore(cookieStore);
		   
		   
		  new TweetBySearch(null).TopicSearchPages("http://s.weibo.com/wb/pm2.5&xsort=time&timescope=custom::2014-07-15-0&nodup=1","test","","");
	}

}
