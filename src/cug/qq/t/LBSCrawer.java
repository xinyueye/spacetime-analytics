package cug.qq.t;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import cug.tools.RequestParams;

public class LBSCrawer {

	
		private HttpClient client=new DefaultHttpClient();
		private String configFile="config\\t.qq";
		RequestParams rp = null;
		public String getConfigFile() {
			return configFile;
		}
		public String getContent(String url)
		{
			
			HttpGet post = new HttpGet(url);
			if (rp == null) {
				rp = new RequestParams();
				rp.getFromConfig("config\\t.qq");
			}
			rp.setHeader(post);
			try {
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String content = EntityUtils.toString(entity, "UTF-8");

				org.apache.log4j.Logger.getLogger("weibo").info(content);
			
				return content;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}
	public String getAccessToken(String appkey)
	{
		String strUrl="https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id=%s&response_type=token&redirect_uri=http://59.71.152.192&forcelogin=false";
		strUrl=String.format(strUrl,appkey);
		org.apache.log4j.Logger.getLogger("weibo").info(strUrl);
		return this.getContent(strUrl);
	}
	public String getC(String appkey,String token,String openid)
	{
				
		//https://open.t.qq.com/api/t/show?oauth_consumer_key=801529316&access_token=3d2c4f873773a93a05dcc283c2a363db&expires_in=8035200&openid=F0BD96132B0C40A326C8196E03E7512F&openkey=51C9A2715D4BDD2E3C3002C93CE2034E&clientip=219.140.235.201&oauth_version=2.a&scope=all&format=json&id=426257016881475
		String auth="&oauth_consumer_key=%s&access_token=%s&openid=%s&oauth_version=2.a";
		String strUrl="https://open.t.qq.com/api/t/show?scope=all&id=426257016881475"+auth;
		strUrl=String.format(strUrl,appkey,token,openid);
		org.apache.log4j.Logger.getLogger("weibo").info(strUrl);
		return this.getContent(strUrl);
	}
//	format	 返回数据的格式（json或xml）
//	keyword	 搜索关键字（1-128字节）
//	pagesize	 每页大小（1-30个）
//	page	 页码
//	contenttype	 消息的正文类型（按位使用）
//	0-所有，0x01-纯文本，0x02-包含url，0x04-包含图片，0x08-包含视频，0x10-包含音频
//
//	sorttype	 排序方式
//	0-表示按默认方式排序(即时间排序(最新))
//
//	msgtype	 消息的类型（按位使用）
//	0-所有，1-原创发表，2 转载，8-回复(针对一个消息，进行对话)，0x10-空回(点击客人页，进行对话)
//
//	searchtype	 搜索类型
//	0-默认搜索类型（现在为模糊搜索）
//	1-模糊搜索：时间参数starttime和endtime间隔小于一小时，时间参数会调整为starttime前endtime后的整点，即调整间隔为1小时
//	8-实时搜索：选择实时搜索，只返回最近几分钟的微博，时间参数需要设置为最近的几分钟范围内才生效，并且不会调整参数间隔
//	starttime	 开始时间，用UNIX时间表示（从1970年1月1日0时0分0秒起至现在的总秒数）
//	endtime	 结束时间，与starttime一起使用（必须大于starttime）
//	province	 省编码（不填表示忽略地点搜索）
//	city	 市编码（不填表示按省搜索）
//	longitude	 经度，（实数）*1000000，需与latitude、radius配合使用
//	latitude	 纬度，（实数）*1000000，需与longitude、radius配合使用
//	radius	 半径（整数，单位米，不大于20000）,需与longitude、latitude配合使用
//	needdup	 用于结果过滤，0-精简后结果，1-全量结果，默认为0
	public String getSearchResult()
	{
		String url="http://open.t.qq.com/api/search/t";
		url+="?access_token=3d2c4f873773a93a05dcc283c2a363db&expires_in=8035200&openid=F0BD96132B0C40A326C8196E03E7512F&openkey=BFFDB06B9F8337FC7210AF67BF36E606";
//		url+="&keyword=kfc";
//		url+="&longitude=114070000";
//		url+="&latitude=22330000";
//		url+="&radius=20000";
		url+="&format=json&keyword=123&pagesize=10&page=1&city=1&province=44&contenttype=0&sorttype=0&msgtype=0&searchtype=0&starttime=1321920000&endtime=1321929902&longitude=114070000&latitude=22330000&radius=20000";
		//url+="&format=json&keyword=123&pagesize=10&page=1&city=1&province=44&contenttype=0&sorttype=0&msgtype=0&searchtype=0&starttime=1321920000&endtime=1321929902&longitude=114070000&latitude=22330000&radius=20000";
		//url=String.format(url,appkey,token,openid);
		
		org.apache.log4j.Logger.getLogger("weibo").info(url);
		return this.getContent(url);
	}
	/**
	 * @param args
	 */
	//http://59.71.152.192/#access_token=3d2c4f873773a93a05dcc283c2a363db&expires_in=8035200&openid=F0BD96132B0C40A326C8196E03E7512F&openkey=BFFDB06B9F8337FC7210AF67BF36E606&refresh_token=13e07226ab18719075fc451358f3ab17&state=&name=lsw4000&nick=abc
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LBSCrawer lbs=new LBSCrawer();
//		lbs.getC("801529316",
//				"3d2c4f873773a93a05dcc283c2a363db",
//								"F0BD96132B0C40A326C8196E03E7512F");
		lbs.getSearchResult();
	}

}
