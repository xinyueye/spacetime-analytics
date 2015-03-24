package cug.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

public class RequestParams {
	 public  List<NameValuePair> headParms = new ArrayList<NameValuePair>();
	  public List<NameValuePair> formData = new ArrayList<NameValuePair>();
	  
	  /**
	   * 
	   * @param filePath
	   * @return
	   */
	  public boolean getFromConfig(String filePath)
	  {
		  File file=new File(filePath);
		  List<NameValuePair>params =headParms;
		  
		  	Scanner sc;
			try {
				sc = new Scanner(file);
				while(sc.hasNextLine())
			  	{
			  		String sb=sc.nextLine();
			  		if(sb.length()<3)
			  		{
			  			params=formData;
			  			continue;
			  		}
			  		int n=sb.indexOf(":");
			  		if(n<1)
			  		{
			  			for(n=0;n<sb.length();n++)
			  			{
			  				char c=sb.charAt(n);
			  				if(c=='-' ||
			  						(c>='a' && c<='z')
			  						|| (c>='A' && c<='Z'))
			  						{}
			  				else 
			  					break;
			  				}
			  			//n--;
//			  			
//				  		String pat ="[\\w-]*";
//	//					// String pat = ".{0,5}([0-9]{0,32})";
//				  		Matcher m = Pattern.compile(pat).matcher(sb);
//				  		
//				  		n=m.group().length();
//			  		
			  		}
			  		
			  		if(n<1		)
			  		{
			  			org.apache.log4j.Logger.getLogger("weibo").info("error-RequestParams.getFromConfig:"+sb);
			  			continue;
			  		}
			  		String strName=sb.substring(0,n).trim();
			  		String strValue=sb.substring(n+1).trim();
			  		if (!strName.toLowerCase().equals("Content-Length".toLowerCase())
			  				&&!strName.toLowerCase().equals("Connection".toLowerCase()))
			  		params.add(new BasicNameValuePair(strName,strValue));		
			  	
			  	}
				
					
								
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		  	return true;
	  }
	  
	  public boolean setHeader(HttpPost post)
	  {
		//  HttpPost post = new HttpPost(url);
		  //getFromConfig(filePath);
		  for(int i=0;i<this.headParms.size();i++)
		  {
			  NameValuePair nvp=headParms.get(i);
			  post.addHeader( nvp.getName(),nvp.getValue());			  
		  }	  
	    
	      return true;
	  }
	  public boolean setHeader(HttpGet get)
	  {
		//  HttpPost post = new HttpPost(url);
		  //getFromConfig(filePath);
		  for(int i=0;i<this.headParms.size();i++)
		  {
			  NameValuePair nvp=headParms.get(i);
			  get.addHeader( nvp.getName(),nvp.getValue());			
			 
		  }	  
	    
	      return true;
	  }
	  public boolean setpostEntiy(HttpPost post,List<NameValuePair>parms)
	  {
		  if(parms==null)
			  parms=this.formData;
		  UrlEncodedFormEntity postEntity;
		try {
			postEntity = new UrlEncodedFormEntity(parms, "UTF-8");
			   post.setEntity(postEntity);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	      return true;
	  }
}
