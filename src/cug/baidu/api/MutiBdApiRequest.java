package cug.baidu.api;



import org.json.JSONObject;

import cug.crawer.MutiApiRequest;
import cug.wb.api.WbApiRequest;

public class MutiBdApiRequest extends MutiApiRequest {
	/**
	 * 多个accessToken间采用;分隔，不能超过6个
	 * 
	 * @param cfgFile
	 * @param accessToken
	 */
	public MutiBdApiRequest(String cfgFile, String accessToken) {
		this.cfgFile = cfgFile;
		this.accessToken = accessToken;
		String[] tokens = accessToken.split(";");
		requests = new BdApiRequest[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			requests[i] = new BdApiRequest(cfgFile, tokens[i]);
		}
	//	nMaxPage = 37;
		
		long nsleep = 60*60*24 * 1000/100000/ requests.length ;
	}
	
	public boolean isValid(JSONObject obj)
	{
		
		String strErr=obj.optString("message");
		
		if (strErr != null && strErr.length() > 0 && !strErr.equals("ok"))
		{
			System.out.println("Error:" + strErr);	
			return false;
		}
		else
			return true;
		
			
	
	}
}

