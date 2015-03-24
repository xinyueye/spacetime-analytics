package cug.wb.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import org.json.*;

import cug.crawer.RequestJson;
import cug.tools.CharsetTranslate;
import cug.wb.login.HttpTools;

public class WbApiRequest extends RequestJson {
	

	// private String url =
	// "https://api.weibo.com/2/place/nearby_timeline.json?";

	// public WbApiRequest(String cfgFile, String redirect_uri) {
	// client = new DefaultHttpClient();
	// this.cfgFilePath=cfgFile;
	// this.access_token=accessToken;
	// this.redirect_uri=redirect_uri;
	//
	// }
	/**
	 * 
	 * @param cfgFile
	 * @param accessToken
	 */
	public WbApiRequest(String cfgFile, String accessToken) {
	super(cfgFile,"access_token",accessToken);
	}

	public static     void main(String []args)
	{
		WbApiRequest apr=new WbApiRequest("", null);
		//键	值
	//	Location	http://?code=ef44c07ce21dd54a044436f03f668a4d
		apr.getAccessTokey("b9132232d38d94e422705faafd53b1ab",
				"http://",
				"1225295656",
				"e3f1e7ccc22d3d4ccffb9a313fcede57");
		
	}
	/**
	 * 采用IE打开：https://api.weibo.com/oauth2/authorize?client_id=2616709673&redirect_uri=http://&response_type=code
	 * 使用F12看返回的code，然后调用这个方法
	 * @param code
	 * @param keyValue
	 * @param redirect_uri
	 * @param client_id
	 * @param client_secret
	 * @return
	 */
	public String getAccessTokey(String code,

	String redirect_uri, String client_id, String client_secret) {

		String url = "";
		List<NameValuePair> parms = new ArrayList<NameValuePair>();
		parms.add(new BasicNameValuePair("client_id", client_id));
		parms.add(new BasicNameValuePair("client_secret", client_secret));

		parms.add(new BasicNameValuePair("grant_type", "authorization_code"));
		parms.add(new BasicNameValuePair("code", code));
		parms.add(new BasicNameValuePair("redirect_uri", redirect_uri));

		try {
			String content = HttpTools.postRequest(client,
					"https://api.weibo.com/oauth2/access_token", parms);
			org.apache.log4j.Logger.getLogger("weibo").info("content----------" + content);
			content = CharsetTranslate.unicodeToUtf8(content);
			// org.apache.log4j.Logger.getLogger("weibo").info("content----------" + content);
			JSONTokener token = new JSONTokener(content);
			JSONObject obj = new JSONObject(token);

			String at = obj.optString("access_token");
			if (at != null) {
				this.keyValue = at;
			}
			return at;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

}
