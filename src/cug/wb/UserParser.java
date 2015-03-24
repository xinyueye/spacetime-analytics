package cug.wb;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 分析用户页面内容，获取用户的信息
 * @author li
 *
 */
public class UserParser {
	String mUserId;
	int mnFollowed;
	int mnFans;
	int mnBlogs;
	String mLocation;

	public boolean handle(String string) {

		// 关注
		// <strong node-type="follow">107</strong>
		// int nStart="<strong node-type=\"follow\">";
		String pat = "<strong node-type=\"follow\">(\\d*)</strong>";
		Matcher m = Pattern.compile(pat).matcher(string);

		if (!m.find()) {
			return false;
		}
		org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));

		pat = "<strong node-type=\"fans\">(\\d*)</strong>";
		m = Pattern.compile(pat).matcher(string);

		if (!m.find()) {
			return false;
		}
		org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));

		pat = "<strong node-type=\"weibo\">(\\d*)</strong>";
		m = Pattern.compile(pat).matcher(string);

		if (!m.find()) {
			return false;
		}
		org.apache.log4j.Logger.getLogger("weibo").info(m.group(1));
		

		// <em class="S_txt2"><a title="北京 海淀区"
		// href="/find/f?type=1&amp;search=1&amp;sex=m&amp;tag1=%E5%8F%8C%E9%B1%BC%E5%BA%A7&amp;prov=11&amp;from=profile&amp;wvr=5&amp;loc=infplace">北京
		// 海淀区</a></em>
		final String tagLocal="<em class=\"S_txt2\">";
		int ni=string.indexOf(tagLocal);
		
		ni=string.indexOf("title=\"",ni+tagLocal.length());
		int ne=string.indexOf("\"",ni+7);
		this.mLocation=string.substring(ni+7,ne);
		
		org.apache.log4j.Logger.getLogger("weibo").info(mLocation);
		
		return true;
	}

}
