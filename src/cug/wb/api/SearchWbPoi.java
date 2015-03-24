package cug.wb.api;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import cug.crawer.RequestJson;
import cug.tools.CharsetTranslate;
import cug.wb.entity.PoiEntity;

//source	false	string	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//access_token	false	string	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//keyword	true	string	查询的关键词，必须进行URLencode。
//city	false	string	城市代码，默认为全国搜索。
//category	false	string	查询的分类代码，取值范围见：分类代码对应表。
//page	false	int	返回结果的页码，默认为1。
//count	false	int	单页返回的记录条数，默认为20，最大为50
public class SearchWbPoi {
	String baseUrl = "https://api.weibo.com/2/place/pois/search.json";

	/**
	 * @param args
	 */

	String toUTF8(String str) {
		return CharsetTranslate.unicodeToUtf8(str);
	}

	MutiWbApiRequest war;

	public SearchWbPoi(String cfgFile,String toke) {
		war = new MutiWbApiRequest(cfgFile, toke);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 30.5110/114.3971
		// WbApiRequest war=new
		// WbApiRequest("config\\api.get.txt","2.00XtOWhF0mdzcZf7fc933cffwqZxLE");
		SearchWbPoi nbp = new SearchWbPoi("config\\api.get.lsw4000.txt","2.00XtOWhF0KpOPYd774f395fezZ4o8E;2.00X1FBiFK4Nv1B7a050fe7f04pcxvB;2.00X1FBiFji8FrC72c564f871kGkRzB;2.00X1FBiFQBEBbEc747070439uPPTLB;2.00X1FBiFoVb5nC10d1b7cd4bxFbBnC;2.00X1FBiF0CoPbS40d420d12dRqUisB");
		// 30.5095/114.3994
		// int n=nbp.getPoi("肯德基",30.5095,114.3994,1,10000);
		 nbp.getPois("医院","0027");;
	}

	public int getPois(String keys, String citys) {
		// 30.35- 30.65
		// 114.15- 114.50
		// this.war.getAccessTokey("583ea7462780c60788f756e2e46f937d");
		int n = 0;
		//double lat = 30.35;

		String strsC[] = citys.split(",");

		for (int i = 0; i < strsC.length; i++) {

			String strsK[] = keys.split(",");

			for (int j = 0; j < strsK.length; j++) {
				int num = 0;
				n += num;
				for (int k = 1;; k++) {
					num = getPoi(strsK[j], strsC[i], 50, k);
//					if(k<10)
////						return 0;
//					try {
//						
//					//	Thread.sleep(25000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					if (k * 50 > num)
						break;
				}
			}
		}

		return n;
	}

	@SuppressWarnings("deprecation")
	public int getPoi(String key, String city, int count, int page) {
		int n = 0;
		try {
	//		String url = baseUrl + "?";
//			if (key != null && key.length() > 0) {
//				url = url + "q=" + URLEncoder.encode(key) + "&";
//
//			}
			String url = String.format("%s?keyword=%s&city=%s&count=%d&page=%d", baseUrl,
					URLEncoder.encode(key), city,count, page);
			org.apache.log4j.Logger.getLogger("weibo").info(url);
			JSONObject obj = war.getJSon(url);
			if (obj == null)
				return 0;
			org.apache.log4j.Logger.getLogger("weibo").info(obj.toString());
			
			n = obj.getInt("total_number");

			JSONArray pois = (JSONArray) obj.getJSONArray("pois");
			org.apache.log4j.Logger.getLogger("weibo").info(pois.toString());
			// org.apache.log4j.Logger.getLogger("weibo").info(pois.length());
			for (int i = 0; i < pois.length(); i++) {
				// PoiEntity
				JSONObject poi = (JSONObject) pois.get(i);
				PoiEntity pe = new PoiEntity();
				pe.setPid(poi.getString("poiid"));
			String title=poi.getString("title");
			if(title.indexOf(key)<0)
				continue;
				pe.setTitle(toUTF8(poi.getString("title")));
				pe.setCity(poi.getString("city"));
				pe.setCategorys(poi.getString("categorys"));
				pe.setJson(poi.toString());
				pe.setLat(poi.getDouble("lat"));
				pe.setLon(poi.getDouble("lon"));
				pe.setCheckin_num(poi.getInt("checkin_num"));
				pe.setTag("wb_"+key);

				if (!pe.isExist())
					pe.save2Db();
				// org.apache.log4j.Logger.getLogger("weibo").info(id);
			}
			// String key = (String) obj.optString("error");
			// org.apache.log4j.Logger.getLogger("weibo").info(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n;

	}

}
