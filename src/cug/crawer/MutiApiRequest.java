package cug.crawer;

import java.util.Date;

import org.json.JSONObject;


public abstract class MutiApiRequest {

	/**
	 * 
	 * @param cfgFile
	 * @param accessToken
	 */
	protected String cfgFile;
	protected String accessToken;
	protected RequestJson[] requests;
	protected long nsleep;// = 60*60*24 * 1000/100000/ requests.length ;
	int times = 0;

	long lastTime = 0;

	public MutiApiRequest() {
		super();
	}

	/**
	 * 轮流使用多个accessToken进行抓取，会先sleep.
	 * 
	 * @param url
	 * @return
	 */
	public JSONObject getJSon(String url) {
		
		JSONObject obj = null;
	//	;
		String strErr ="";
		while (true) {
			if (times > requests.length) {
				
				try {
					long tNow=new Date().getTime();
					long toSleep=0;
					if(tNow-this.lastTime>nsleep)
						toSleep=0;
					
					
					else
						toSleep=nsleep-(tNow-this.lastTime);
					lastTime=tNow;
					System.out.println(String.format("正在进行%d抓取%s,sleep %d", times,
							url, toSleep));
					Thread.sleep(toSleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			}
			
			RequestJson req = requests[times % requests.length];
			times++;
			obj = req.getJSon(url);
			if(this.isValid(obj))
				break;
			else
				System.out.println("retry:" + url);		
			
		}
	
		return obj;// req.getJSon(url);
	}
	
	/**
	 * 判定抓取的数据是否有效，是否需要重试
	 * @param obj
	 * @return
	 */
	public abstract boolean isValid(JSONObject obj);
	
	
	

}