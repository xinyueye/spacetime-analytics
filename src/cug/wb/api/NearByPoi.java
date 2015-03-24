package cug.wb.api;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import cug.crawer.RequestJson;
import cug.tools.CharsetTranslate;
import cug.wb.entity.PoiEntity;

public class NearByPoi {
	String baseUrl="https://api.weibo.com/2/place/nearby/pois.json";
	/**
	 * @param args
	 */
	
	String toUTF8(String str)
	{
		return CharsetTranslate.unicodeToUtf8(str);
	}
	RequestJson war;
	NearByPoi(String toke)
	{
		war=new WbApiRequest("config\\api.get.txt",toke);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	30.5110/114.3971
		//WbApiRequest war=new WbApiRequest("config\\api.get.txt","2.00XtOWhF0mdzcZf7fc933cffwqZxLE");
		NearByPoi nbp=new NearByPoi("2.00XtOWhF0mdzcZf7fc933cffwqZxLE");
		//30.5095/114.3994
		//int n=nbp.getPoi("肯德基",30.5095,114.3994,1,10000);
	nbp.getPois("肯德基");
	}
	public int getPois(String key)
	{
		//30.35-	30.65
		//	114.15-		114.50
	//	this.war.getAccessTokey("583ea7462780c60788f756e2e46f937d");
		int n=0;
		double  lat=30.35;
		
		for(;lat<30.66;lat+=0.05)
		{
			double lon=114.15;
			for(;lon<114.56;lon+=0.05)
			{
				String strs[]=key.split(",");	
				
					for(int i=0;i<strs.length;i++)
				{
						int num=0;
						n+=num;
				}
				getPoi(key,lat,lon,50,10000);
				try {
					Thread.sleep(25000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		return n;		
	}
	@SuppressWarnings("deprecation")
	public int getPoi(String key,double lat,double lon, int count,int range) {
		//String url = "https://api.weibo.com/2/place/nearby_timeline.json?";
		// source false string 采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
		// access_token false string 采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
		// lat true float 纬度，有效范围：-90.0到+90.0，+表示北纬。
		// long true float 经度，有效范围：-180.0到+180.0，+表示东经。
		// range false int 查询范围半径，默认为2000，最大为10000，单位米。
		// q false string 查询的关键词，必须进行URLencode。
		// category false string 查询的分类代码，取值范围见：分类代码对应表。
		// count false int 单页返回的记录条数，默认为20，最大为50。
		// page false int 返回结果的页码，默认为1。
		// sort false int 排序方式，0：按权重，1：按距离，3：按签到人数。默认为0。
		// offset false in
int n=0;
		try {
			String url =baseUrl+"?";
			if(key!=null &&key.length()>0)
			{
				url=url+"q="+URLEncoder.encode(key)+"&";
				
			}
			url =String.format("%slat=%.5f&long=%.5f062&count=%d&range=%d",url,lat,lon,count,range);
			JSONObject obj = war.getJSon(url);
			if(obj==null)
				return 0;
			org.apache.log4j.Logger.getLogger("weibo").info(obj.toString());
			int num = obj.getInt("total_number");
			org.apache.log4j.Logger.getLogger("weibo").info(num);
			n=num;
			JSONArray pois = (JSONArray) obj.getJSONArray("pois");
			org.apache.log4j.Logger.getLogger("weibo").info(pois.toString());
			//org.apache.log4j.Logger.getLogger("weibo").info(pois.length());
			for (int i = 0; i < pois.length(); i++) {
			//	PoiEntity
				JSONObject poi = (JSONObject) pois.get(i);
				PoiEntity pe=new PoiEntity();
				pe.setPid( poi.getString("poiid"));
				pe.setTitle(toUTF8( poi.getString("title")));
				pe.setCity( poi.getString("city"));
				pe.setCategorys( poi.getString("categorys"));
				pe.setJson(poi.toString());
				pe.setLat( poi.getDouble("lat"));
				pe.setLon( poi.getDouble("lon"));
				pe.setCheckin_num( poi.getInt("checkin_num"));
				pe.setTag( key);
				
				pe.save2Db();
			//	org.apache.log4j.Logger.getLogger("weibo").info(id);
			}
			//String key = (String) obj.optString("error");
		//	org.apache.log4j.Logger.getLogger("weibo").info(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return n;

	}

}
