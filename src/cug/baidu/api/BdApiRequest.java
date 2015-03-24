package cug.baidu.api;

import cug.crawer.RequestJson;
import cug.wb.api.WbApiRequest;

public class BdApiRequest extends RequestJson {
	

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
	public BdApiRequest(String cfgFile, String accessToken) {
			super(cfgFile,"ak",accessToken);
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
	

}
