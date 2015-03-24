package cug.wb.api;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import cug.wb.entity.PoiEntity;

//https://api.weibo.com/2/place/pois/show.json?access_token=00XtOWhF0KpOPYd774f395fezZ4o8E&poiid=100135SQ100014

public class PoiAPI {
	WbApiRequest war;



	String baseUrl = "https://api.weibo.com/2/place/pois/show.json";

	public PoiAPI(String cfgFile, String toke) {
		war = new WbApiRequest(cfgFile, toke);
	}

	public PoiEntity GetPoiById(String id) {
		String url = String.format("%s?poiid=%s", baseUrl, id);
		org.apache.log4j.Logger.getLogger("weibo").info(url);
		JSONObject obj = war.getJSon(url);
		if (obj == null)
			return null;
//		org.apache.log4j.Logger.getLogger("weibo").info(obj.toString());
//		// n = obj.getInt("total_number");
//		JSONArray pois = (JSONArray) obj.getJSONArray("pois");
//		org.apache.log4j.Logger.getLogger("weibo").info(pois.toString());
		// org.apache.log4j.Logger.getLogger("weibo").info(pois.length());
		
		//for (int i = 0; i < pois.length(); i++) {
			// PoiEntity
			JSONObject poi =obj;// (JSONObject) pois.get(0);
	//		PoiEntity pe = new PoiEntity();
			
			PoiEntity pe = new PoiEntity();
		pe.setPid(poi.getString("poiid"));
		String title = poi.getString("title");

		pe.setTitle(poi.getString("title"));
		pe.setCity(poi.getString("city"));
		pe.setCategorys(poi.getString("categorys"));
		pe.setJson(poi.toString());
		pe.setLat(poi.getDouble("lat"));
		pe.setLon(poi.getDouble("lon"));
		pe.setCheckin_num(poi.getInt("checkin_num"));
		// pe.setTag(key);
		return pe;
	}

}
