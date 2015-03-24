package cug.wb.lightcrawer;

//import org.las.crawler.Controller;
//import org.las.crawler.PageEntity;

public class TopicSearcher {
//
//
//	 /**
//	  * 抓取基础URL及其随后50页的微博数据
//	  * @param url 基础URL
//	  */
//	public  void TopicSearchPages(final String url)
//	{
//		// TODO Auto-generated method stub
//        Controller controller = new Controller(url, 1){
//                
//                protected void handlePage(final PageEntity page) {
//                   
//               	 byte []bytes=page.getContent();
//               	 
//               	 String s;
//					try {
//									
//						s = new String(bytes, "UTF-8");						
//						TweetParser ts=new TweetParser();
//						
//				//		 org.apache.log4j.Logger.getLogger("weibo").info(s);
//						int num=ts.getSearchResultNum(s);
//						
//						ts.handle(s);
//						
//						 
//						 //只能抓取50页数据
//						for(int i=1;i<num/19&& i<50;i++)
//						{
//							int nPage=i+1;
//							TopicSearchOnePage(url+"&page="+nPage);													
//						}
//							
//						
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					}               	 
//                }
//        };
//        controller.start(1);
//	}
//	
//	/***
//	 * 根据URL抓取其中的微博数据
//	 * @param url
//	 */
//	public  void  TopicSearchOnePage(final String url) {
//		// TODO Auto-generated method stub
//         Controller controller = new Controller(url, 1){
//                 
//                 protected void handlePage(final PageEntity page) {
//                    
//                	 byte []bytes=page.getContent();
//                	 
//                	 String s;
//					try {
//						s = new String(bytes, "UTF-8");						
//						TweetParser ts=new TweetParser();
//					//	org.apache.log4j.Logger.getLogger("weibo").info(s);
//						List<TweetEntity> ls=ts.handle(s);
//						org.apache.log4j.Logger.getLogger("weibo").info("find "+ls.size()+" tweets in "+url);
//						for(int i=0;i<ls.size();i++)
//						{
//							ls.get(i).save2Db();
//						}						
//					//	 org.apache.log4j.Logger.getLogger("weibo").info(s);
//					} catch (Exception  e) {
//						e.printStackTrace();
//					} 	 
//                         
//                 }
//         };
//         controller.start(1);
//	}
//	
//	/**
//	 * 根据开始时间，所在位置，搜索关键字获取搜索结果
//	 * @param dt  开始时间
//	 * @param hours  时间段
//	 * @param times 时间段次数
//	 * @param localtions  城市集合,以逗号分隔
//	 * @param keys  搜索关键字集合,以逗号分隔
//	 */
//	public void SearchByTimeLocation(Date dt, int hours,int times, String localtions,String keys)
//	{
//		final String baseUrl="http://s.weibo.com/wb/"; 
//		String []cities=localtions.split(",");
//		String []keyList=keys.split(",");
//		
//		
//		for(int i=0;i<cities.length;i++)
//		{			
//			for(int j=0;j<times;j++)
//			{
//				for(int k=0;i<keys.length();k++)
//				{
//				
//					Date dStart=new Date(dt.getTime()+60*60*1000*hours*j);//null;
//					Date dEnd=new Date(dStart.getTime()+60*60*1000*hours);
//					
//					searchByTimeLocation(dStart,dEnd,cities[i],baseUrl+keyList[k]);
//				}
//			}
//		}
//	}
//	/**
//	 * 抓取指定时间段内，指定地点中的微博数量
//	 * @param dtStart 开始时间
//	 * @param dtend 结束时间
//	 * @param location 位置
//	 * @param url 基础url
//	 */
//	public void searchByTimeLocation(Date dtStart,Date dtend,String location,String url)
//	{
//			
//		//&region=custom:42:1&timescope=custom:2014-07-15-10:2014-07-15-16
//		url=url+"&region=custom:"+location;		
//		
//		@SuppressWarnings("deprecation")
//		String datePara="";
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd-hh");
//		if(dtStart!=null)
//		{			
//			datePara=":"+sdf.format(dtStart);			
//		}
//		if(dtend!=null)
//		{
//			datePara+=":"+sdf.format(dtend);
//		}
//		
//		if(datePara.length()>0)
//		{
//			url=url+"&timescope=custom"+datePara;	
//		}
//		
//		
//        Controller controller = new Controller(url, 1){           
//        	
//                protected void handlePage(final PageEntity page) {
//                	
//               	 byte []bytes=page.getContent();
//               	 
//               	 String s;
//					try {
//						s = new String(bytes, "UTF-8");						
//						TweetParser ts=new TweetParser();
//						int nNum=ts.getSearchResultNum(s);
//						
//					} catch (UnsupportedEncodingException e) {
//						e.printStackTrace();
//					} 	 
//                        
//                }
//        };
//        controller.start(1);
//	}
}
