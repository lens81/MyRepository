package com.webCrawler.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		System.out.println("Start sample");

		//nyaaFileDownLoad("https://sukebei.nyaa.se/?cats=8_30&filter=2");
		
		naverComicFileDownLoad("http://comic.naver.com/webtoon/detail.nhn?titleId=648419&no=1&weekday=mon");
		
		
		// fileDownLoad("http://marumaru.in/");

		System.out.println("End");
	}

	public static void naverComicFileDownLoad(String url) throws IOException 
	{
		HtmlPage htmlPage = new HtmlPage();

		Elements element = htmlPage.pageDom(url, "img");
		
		System.out.println(element);
		
		for (Element element2 : element) {
			
			String pageLink = element2.attr("abs:src");
			
			System.out.println(pageLink);
			
		}
		
		UrlConnection urlConnection = new UrlConnection();
		
		
		try {
			urlConnection.getFile("http://comic.naver.com/webtoon/detail.nhn?titleId=648419&no=1&weekday=mon", "test111");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// 대표 url 따라 30 페이지만 한다.
	public static void nyaaFileDownLoad(String url) throws IOException,
			InterruptedException {
		// 최초 대표 url 로 검색
		HashMap<String, String> htmlTileInfo = new HashMap<String, String>();
		HashMap<String, String> downLoadLinkInfo = new HashMap<String, String>();
		HashMap<String, String> linkInfo = new HashMap<String, String>();

		HtmlPage htmlPage = new HtmlPage();

		Elements element = htmlPage.pageDom(url, "a");		
		
		linkInfo = htmlPage.nayaaPageLink(element, url);

		for (Iterator iterator = linkInfo.keySet().iterator(); iterator.hasNext();) {
			String keyName = (String) iterator.next();
			String link = linkInfo.get(keyName);

			element = htmlPage.pageDom(link, "a");

			System.out.println(link);
			
			htmlTileInfo = htmlPage.nayaaLinkTitleMap(element, "title");
			downLoadLinkInfo = htmlPage.nayaaLinkTitleMap(element, "Download");

			//Thread.sleep(60000);

			
			
			Iterator htmlTileInfoIterator = htmlTileInfo.keySet().iterator();
			
			while (htmlTileInfoIterator.hasNext()) {
				String keyId  = (String) htmlTileInfoIterator.next();
				
				if(downLoadLinkInfo.containsKey(keyId))
				{
				
					System.out.println(downLoadLinkInfo.get(keyId) + "" + htmlTileInfo.get(keyId));
					
					Thread.sleep(4000);
					
				}
			}
			
			Thread.sleep(3000);
			
//			for (Iterator iterator1 = htmlTileInfo.keySet().iterator(); iterator.hasNext();) {
//				
//				if(iterator1.hasNext())
//				{
//					String keyName1 = (String) iterator1.next();
//					String link1 = downLoadLinkInfo.get(keyName1);
//					String fileName1 = htmlTileInfo.get(keyName1);
//
//					System.out.println("link1 : " + link1 + "  fileName1 : " + fileName1);
//					
////					if ("".equals(link1) || link1 == null) {
////						continue;
////					}
//
//					// urlFile.getFile(link, fileName);
//
//					//Thread.sleep(30000);					
//				}
//
//
//			}
			
			System.out.println("for end");
			
			
		}

	}

}
