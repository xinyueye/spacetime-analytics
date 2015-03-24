package cug.wb.api;

import org.json.JSONArray;
import org.json.JSONObject;

import cug.wb.entity.PoiEntity;
import cug.wb.entity.TweetEntity;

//https://api.weibo.com/2/statuses/show.json?id=3717140001836370&access_token=2.00XtOWhF0mdzcZf7fc933cffwqZxLE
/**
 * 
 * @author li 
{
    "created_at": "Sun Oct 19 09:35:01 +0800 2014",
    "id": 3767319819836343,
    "mid": "3767319819836343",
    "idstr": "3767319819836343",
    "text": "【广州现埃博拉？谣言！】17日晚间，一条“埃博拉病人在广交会上出现”的消息在微信朋友圈传播，而且声称“珠江台新闻报道”，在市民中引起紧张。昨日上午，省卫生计生委出面辟谣：广州现埃博拉？谣言！http://t.cn/R7b1FEW",
    "source_type": 1,
    "source": "<a href=\"http://app.weibo.com/t/feed/6ghA0p\" rel=\"nofollow\">搜狗高速浏览器</a>",
    "favorited": false,
    "truncated": false,
    "in_reply_to_status_id": "",
    "in_reply_to_user_id": "",
    "in_reply_to_screen_name": "",
    "pic_urls": [
        {
            "thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/81b6b901gw1elg7t7wapjj208204174p.jpg"
        }
    ],
    "thumbnail_pic": "http://ww2.sinaimg.cn/thumbnail/81b6b901gw1elg7t7wapjj208204174p.jpg",
    "bmiddle_pic": "http://ww2.sinaimg.cn/bmiddle/81b6b901gw1elg7t7wapjj208204174p.jpg",
    "original_pic": "http://ww2.sinaimg.cn/large/81b6b901gw1elg7t7wapjj208204174p.jpg",
    "geo": null,
    "user": {
        "id": 2176235777,
        "idstr": "2176235777",
        "class": 1,
        "screen_name": "广东政法",
        "name": "广东政法",
        "province": "44",
        "city": "1",
        "location": "广东 广州",
        "description": "秉持改革开放、民主法治的理念，为民依法公正办事。",
        "url": "http://www.gdzf.org.cn/",
        "profile_image_url": "http://tp2.sinaimg.cn/2176235777/50/40021836543/1",
        "profile_url": "gdzf",
        "domain": "gdzf",
        "weihao": "",
        "gender": "m",
        "followers_count": 854869,
        "friends_count": 944,
        "pagefriends_count": 0,
        "statuses_count": 7200,
        "favourites_count": 50,
        "created_at": "Mon Jun 13 09:37:44 +0800 2011",
        "following": false,
        "allow_all_act_msg": false,
        "geo_enabled": true,
        "verified": true,
        "verified_type": 1,
        "remark": "",
        "ptype": 0,
        "allow_all_comment": true,
        "avatar_large": "http://tp2.sinaimg.cn/2176235777/180/40021836543/1",
        "avatar_hd": "http://tp2.sinaimg.cn/2176235777/180/40021836543/1",
        "verified_reason": "广东省政法委、省平安办、省综治办、省维稳办官方微博",
        "verified_trade": "",
        "verified_reason_url": "",
        "verified_source": "",
        "verified_source_url": "",
        "verified_state": 0,
        "verified_level": 3,
        "verified_reason_modified": "",
        "verified_contact_name": "",
        "verified_contact_email": "",
        "verified_contact_mobile": "",
        "follow_me": false,
        "online_status": 0,
        "bi_followers_count": 689,
        "lang": "zh-cn",
        "star": 0,
        "mbtype": 0,
        "mbrank": 0,
        "block_word": 0,
        "block_app": 0,
        "credit_score": 80,
        "urank": 0
    },
    "reposts_count": 26,
    "comments_count": 19,
    "attitudes_count": 6,
    "mlevel": 0,
    "visible": {
        "type": 0,
        "list_id": 0
    },
    "darwin_tags": []
}
 */
public class WeiboTweetsAPI {
	MutiWbApiRequest war;

	String baseUrl = "https://api.weibo.com/2/statuses/show.json";

	public WeiboTweetsAPI(String cfgFile, String toke) {
		war = new MutiWbApiRequest(cfgFile, toke);
	}

	/**
	 * 添加到
	 * 
	 * @param tableName
	 * @param id
	 * @return
	 */

	public boolean updateById(String tableName, String id) {
		return this.updateById(tableName, id,false);
	}
	/**
	 * 
	 * @param tableName
	 * @param id
	 * @param updateUser 是否同时更新用户信息
	 * @return
	 */
	public boolean updateById(String tableName, String id,boolean updateUser) {
		TweetEntity te = new TweetEntity();
		te.setMid(id);
		JSONObject obj = getJsonById(id);
		JSONObject objTweet=obj;
		// obj.get
		if (obj == null) {
			te.setGeo("No found the weibo");
			return false;
		}

		if(updateUser)
		{
			JSONObject user=obj.getJSONObject("user");
			String id1=user.getString("idstr");
			te.setUserid(id1);
		}
		te.setReposts_count(obj.getInt("reposts_count"));
		te.setAttitudes_count(obj.getInt("attitudes_count"));
		te.setComments_count(obj.getInt("comments_count"));
		te.setContent(obj.toString());
		te.setTxt(obj.getString("text"));
		// obj.get
		if (obj != null) {
			// te.setTxt(geo)
			if (!obj.get("geo").toString().equals("null"))// obj.has("geo") )

			{
				// obj.get("geo").toString().equals("null")

				obj = obj.getJSONObject("geo");

				// geo":{"type":"Point","coordinates":[34.78422,113.66345]}
				if (obj != null) {
					JSONArray arr = obj.getJSONArray("coordinates");
					te.setLat(arr.getDouble(0));
					te.setLon(arr.getDouble(1));

				}
			}
		}
		
		boolean isok=false;
		 isok=te.updateFromApi(tableName);
		 
		 UserAPI gu=new UserAPI();
		 
		 try
		 {
		if( updateUser) 
			{
				JSONObject user=objTweet.getJSONObject("user");
				if(user!=null)
				 return 	gu.parseJson(user);
				else return false;
			}}
		 catch(Exception ex)
		 {
			  ex.printStackTrace();
			  System.out.println(obj.toString());
		 }
				return isok;
	
		 

	}

	/**
	 * Unfinished
	 * 
	 * @param id
	 * @return
	 */
	public TweetEntity GetTweetById(String id) {
		JSONObject obj = getJsonById(id);
		if (obj == null)
			return null;
		// org.apache.log4j.Logger.getLogger("weibo").info(obj.toString());
		// // n = obj.getInt("total_number");
		// JSONArray pois = (JSONArray) obj.getJSONArray("pois");
		// org.apache.log4j.Logger.getLogger("weibo").info(pois.toString());
		// org.apache.log4j.Logger.getLogger("weibo").info(pois.length());

		// for (int i = 0; i < pois.length(); i++) {
		// PoiEntity
		JSONObject tweet = obj;// (JSONObject) pois.get(0);
		// PoiEntity pe = new PoiEntity();

		TweetEntity te = new TweetEntity();
		te.setTxt(tweet.getString("text"));
		// te.set)

		return te;
	}

	private JSONObject getJsonById(String id) {
		String url = String.format("%s?id=%s", baseUrl, id);
		org.apache.log4j.Logger.getLogger("weibo").info(url);
		JSONObject obj = war.getJSon(url);
		String strErr=obj.optString("error");
		if(strErr!=null && strErr.length()>1)
		{
			System.out.println(strErr);
			return null;		
		}
		return obj;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
