package cug.crawer;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import cug.tools.CharsetTranslate;


public class RequestJsonJackson {

	protected HttpClient client;
	protected String cfgFilePath;
	protected String access_token;


	public RequestJsonJackson(String cfgFile, String accessToken) {
		client = new DefaultHttpClient();
		this.cfgFilePath = cfgFile;
		this.access_token = accessToken;
	}


	public JsonNode  getJSon(String url) {
		// =new ObjectNode ();
		ObjectMapper m = new ObjectMapper();
		m.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true); 
		String content;
		try {
					
	
			CrawByCookie cbc = new CrawByCookie(cfgFilePath);
	
			if (this.access_token!=null && this.access_token.length()>1 &&url.indexOf("access_toke") == -1) {
				url += "&access_token=" + access_token;
			}
	
			content = cbc.getContent(url);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		try
		{
			if (content == null || content.length() < 5) {
				return null;
			}
			// org.apache.log4j.Logger.getLogger("weibo").info(content);
			 content = CharsetTranslate.unicodeToUtf8(content);
			// org.apache.log4j.Logger.getLogger("weibo").info(content);
			content=content.replace("http://", "http_//");
//			JSONTokener token = new JSONTokener(content);
			JsonNode obj=(ObjectNode) m.readTree(content);
		//	 obj = new JSONObject(token);
			return obj;
		} catch (Exception ex) {
			System.out.println(content);
			ex.printStackTrace();
			
		}
		return null;
	}

}