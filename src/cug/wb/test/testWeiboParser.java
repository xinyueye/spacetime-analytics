package cug.wb.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


import org.junit.Test;

import cug.wb.TweetParser;

public class testWeiboParser {

	@Test
	public void test() throws Exception {
		//fail("Not yet implemented");
		File f=new File("E:\\sns\\lightCrawler\\src\\Pm25.htm");
		
		StringBuffer sb=new StringBuffer();
		Scanner sc=new Scanner(f);
		while(sc.hasNext())
		{
			String str=sc.nextLine();
			sb.append(str);
		
		}
		TweetParser wp=new TweetParser();
		wp.handle(sb.toString());
		
	}

}
