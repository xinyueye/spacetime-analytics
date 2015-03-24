package cug.wb.api;

import java.sql.SQLException;

import org.json.JSONObject;

import cug.wb.entity.WbUser;

/**
 * 
 * @author li
 *
 */
public class UserAPI {
	/**
	 * 
	 * @return
	 
	{
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
	}
	*/
	boolean parseJson(JSONObject json)
	{
		JSONObject obj=json;//new JSONObject(json);
		
		WbUser wu=new WbUser();
		wu.setUserId(obj.getString("idstr"));
		wu.setCity(obj.getInt("city"));
		wu.setFavourites_count(obj.getInt("favourites_count"));
		wu.setFollowers_count(obj.getInt("followers_count"));
		wu.setFriends_count(obj.getInt("friends_count"));
		wu.setJson(json.toString());
		wu.setName(obj.getString("name"));
		wu.setProvince(obj.getInt("province"));
		wu.setScreen_name(obj.getString("screen_name"));
		wu.setStatuses_count(obj.getInt("statuses_count"));
		wu.setUclass(String.valueOf(obj.getInt("class")));
		if(!wu.isExist())
			try {
				wu.save2Db();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return true;
	}

}
