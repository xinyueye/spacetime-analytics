package cug.lda;

import cug.wb.TweetParser;

public class Tweet2Txt {

	public static void main(String[] args) {

		TweetParser tp = new TweetParser();
		// int n=tp.clearAllContent();
		int n = tp.Content2Txt("E:\\sns\\lda\\test_data\\vdengeu_txt.txt", "",
				"http;北京;cn;rp;cr;id;quot;gt;zaker;省;市;登革热;分享 ");
		org.apache.log4j.Logger.getLogger("weibo").info("共处理微博" + n + "条");

	}
}
