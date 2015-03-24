package cug.crawer;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 向服务器发起请求，获得Json对象，没有采用cookie（也就是没有考虑用户登录的问题）
 * 如果需要考虑用户登录，需要重写此部分
 * @author li
 *
 */
public class RequestJson {

	protected HttpClient client;
	protected String cfgFilePath;
	protected String keyValue;
	protected String keyname;
	


	public RequestJson(String cfgFile, String keyName,String accessToken) {
		client = new DefaultHttpClient();
		this.cfgFilePath = cfgFile;
		this.keyValue = accessToken;
		this.keyname=keyName;
	}


	public JSONObject getJSon(String url) {
		JSONObject obj =new JSONObject ();
		String content;
		try {
					
	
			CrawByCookie cbc = new CrawByCookie(cfgFilePath);
	
			if (this.keyValue!=null && this.keyValue.length()>1 &&url.indexOf("&"+keyname) == -1) {
				url += "&"+keyname+"=" + keyValue;
			}
	
			content = cbc.getContent(url);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		try
		{
			if (content == null || content.length() < 5) {
				return obj;
			}
			// org.apache.log4j.Logger.getLogger("weibo").info(content);
			// content = CharsetTranslate.unicodeToUtf8(content);
			// org.apache.log4j.Logger.getLogger("weibo").info(content);
			content=content.replace("http://", "http_//");
			JSONTokener token = new JSONTokener(content);
			 obj = new JSONObject(token);
			return obj;
		} catch (Exception ex) {
			System.out.println(content);
			ex.printStackTrace();
			
		}
		return null;
	}

}