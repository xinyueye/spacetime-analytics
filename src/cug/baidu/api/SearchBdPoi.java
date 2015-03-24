package cug.baidu.api;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import cug.crawer.MutiApiRequest;
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
public class SearchBdPoi {
	
	//http://api.map.baidu.com/place/v2/search?q=饭店&region=北京&output=json&ak=E4805d16520de693a3fe707cdc962045
	int nPageSize=20;
	String baseUrl = "http://api.map.baidu.com/place/v2/search";

	/**
	 * @param args
	 */

	String toUTF8(String str) {
		return CharsetTranslate.unicodeToUtf8(str);
	}

	MutiApiRequest war;

	public SearchBdPoi(String cfgFile,String toke) {
		war = new MutiBdApiRequest(cfgFile, toke);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 30.5110/114.3971
		// WbApiRequest war=new
		// WbApiRequest("config\\api.get.txt","2.00XtOWhF0mdzcZf7fc933cffwqZxLE");
		SearchBdPoi nbp = new SearchBdPoi("config\\api.baidu.txt","9DPszZlYeCr5Dj0tRshXl8As");
		// 30.5095/114.3994
		// int n=nbp.getPoi("肯德基",30.5095,114.3994,1,10000);
		 nbp.getPois("医院","武汉");;
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
				for (int k = 0;; k++) {
					num = getPoi(strsK[j], strsC[i], 38, k);
//					if(k<10)
//						return 0;
//					try {
//						
//						Thread.sleep(25000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					if (k * nPageSize > num)
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
			//q=饭店&region=北京&output=json&page_size=20&page_num=1&scope=2
			String url = String.format("%s?q=%s&region=%s&page_size=%d&page_num=%d&scope=2&output=json", baseUrl,
					URLEncoder.encode(key), city,nPageSize, page);
			org.apache.log4j.Logger.getLogger("baidu").info(url);
			JSONObject obj = war.getJSon(url);
			if (obj == null)
				return 0;
			org.apache.log4j.Logger.getLogger("baidu").info(obj.toString());
			
			n = obj.getInt("total");

			JSONArray pois = (JSONArray) obj.getJSONArray("results");
			org.apache.log4j.Logger.getLogger("baidu").info(pois.toString());
			// org.apache.log4j.Logger.getLogger("weibo").info(pois.length());
//			  "name":"武汉市第六医院",
//	            "location":{
//	                "lat":30.605831,
//	                "lng":114.296186
//	            },
//	            "address":"香港路168号",
//	            "telephone":"(027)82419006",
//	            "uid":"cc0849a032141a499403194e",
//	            "detail_info":{
//	                "tag":"医疗;综合医院",
//	                "type":"hospital",
//	                "detail_url":"http://api.map.baidu.com/place/detail?uid=cc0849a032141a499403194e&output=html&source=placeapi_v2",
//	                "price":"0",
//	                "overall_rating":"3.0",
//	                "service_rating":"0",
//	                "technology_rating":"0",
//	                "image_num":"10",
//	                "comment_num":"10"
//	            }
			for (int i = 0; i < pois.length(); i++) {
				// PoiEntity
				JSONObject poi = (JSONObject) pois.get(i);
				PoiEntity pe = new PoiEntity();
				pe.setPid(poi.getString("uid"));
			String title=poi.getString("name");
			pe.setTitle(toUTF8(title));
			JSONObject pos=(JSONObject) poi.getJSONObject("location");
			pe.setLat(pos.getDouble("lat"));
			pe.setLon(pos.getDouble("lng"));
			pe.setCity(city);
			
			
//			if(title.indexOf(key)<0)
//				continue;
			
		//		pe.setCity(poi.getString("city"));
			
			JSONObject detail_info=(JSONObject) poi.getJSONObject("detail_info");
			
				pe.setCategorys(detail_info.optString("type"));
				pe.setJson(poi.toString());
			
				pe.setCheckin_num(detail_info.optInt("comment_num"));
				pe.setTag("bd_"+key);

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
