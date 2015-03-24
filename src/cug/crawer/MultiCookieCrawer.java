package cug.crawer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 采用多个cookie模仿多个用户抓取
 * 
 * @author li
 * 
 */
public class MultiCookieCrawer {

	private String configFiles;
	private int sleepTime = 45000;
	private long lastSleepTime = 0;

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	protected int num = 1;
	protected int errornum = 0;
	protected CrawByCookie[] cbcs = null;

	public int getCbcsSize() {
		if (cbcs == null)
			return 0;

		return cbcs.length;
	}

	public MultiCookieCrawer(String configFiles) {
		super();
		this.configFiles = configFiles;
		loadConfig();
	}

	private void loadConfig() {
		if (configFiles == null)
			configFiles = "";
		String strs[] = configFiles.split(",");
		cbcs = new CrawByCookie[strs.length];
		for (int i = 0; i < strs.length; i++)
			cbcs[i] = new CrawByCookie(strs[i]);
	}

	private void reloadConfig() {
		// if(configFiles==null)
		// configFiles="";
		// String strs[]=configFiles.split(",");
		// cbcs=new CrawByCookie[strs.length];
		for (int i = 0; i < cbcs.length; i++)
			cbcs[i].reloadConfig();// =new CrawByCookie(strs[i]);
	}

	public String curUrl = null;

	/**
	 * 重新读取配置文件，然后重新执行上一失败url
	 */
	public String retryGet() {
		errornum++;
		// num--;
		// cbcs=null;
		reloadConfig();
		return getContent(curUrl);
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public String getContent(String url) {
		curUrl = url;
		CrawByCookie cbc = cbcs[(this.num - 1) % cbcs.length];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

		org.apache.log4j.Logger.getLogger("weibo").info(
				df.format(new Date()) + "正在通过" + cbc.getConfigFile() + "进行第"
						+ num + "次抓取,错误" + errornum);
		num++;
		try {

			// if(num>1&& (num-1)%cbcs.length==0)
			if (num > cbcs.length + 1) {
				long toSleep = sleepTime / cbcs.length;
				long lNow = new Date().getTime();
				if (lNow - this.lastSleepTime < toSleep)
					toSleep -= (lNow - this.lastSleepTime);
				else
					toSleep = 0;
				lastSleepTime=lNow;
				org.apache.log4j.Logger.getLogger("weibo").info(
						"sleep " + toSleep);
				Thread.sleep(toSleep);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if(num%5==0)
		// cbc.postSome(url);
		String s = cbc.getContent(url);

		return s;

	}

	/**
	 * 通过Post方法获取数据，还没有实现
	 * 
	 * @param url
	 */
	public void post(String url) {

	}
}