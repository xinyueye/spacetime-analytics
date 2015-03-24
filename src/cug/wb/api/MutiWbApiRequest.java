package cug.wb.api;

import org.json.JSONObject;

import cug.crawer.MutiApiRequest;

/**
 * 使用多个accessToken进行抓取，不能超过6个
 * 
 * @author li
 * 
 */
public class MutiWbApiRequest  extends MutiApiRequest{
		

	/**
	 * 多个accessToken间采用;分隔，不能超过6个
	 * 
	 * @param cfgFile
	 * @param accessToken
	 */
	public MutiWbApiRequest(String cfgFile, String accessToken) {
		this.cfgFile = cfgFile;
		this.accessToken = accessToken;
		String[] tokens = accessToken.split(";");
		requests = new WbApiRequest[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			requests[i] = new WbApiRequest(cfgFile, tokens[i]);
		}
		
		nsleep = 3600 / 50 / requests.length * 1000;
	//	maxPage=50;
		
	}

	/**
	 * 轮流使用多个accessToken进行抓取，会先sleep.
	 * 
	 * @param url
	 * @return
	 */

	
	public boolean isValid(JSONObject obj)
	{
		
		String strErr=obj.optString("message");
		
		strErr=obj.optString("error");

		if (strErr != null && strErr.length() > 0 && !strErr.contains("target weibo does not exist!"))
		{
			System.out.println("Error:" + strErr);
			return false;
		}
		else
			return true;
	}
}
