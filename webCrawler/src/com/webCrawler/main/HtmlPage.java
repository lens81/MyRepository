package com.webCrawler.main;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlPage {
	
	public void HtmlPage()
	{
		
	}
	
	public Elements pageDom(String url, String selectType) throws IOException
	{
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
		
		Document jsoupDoc = Jsoup.connect(url).userAgent(userAgent).get();
		
		//System.out.println(jsoupDoc);
		
		Elements element = jsoupDoc.select(selectType);
		
		return element;
	}
	
	public HashMap<String, String> nayaaLinkTitleMap(Elements page, String gubun)
	{	
		HashMap<String, String> linkIdTitle = new HashMap<String, String>();
		
		int i = 0;
		
		for (Element element : page) {
			
			String linkUrl = element.attr("abs:href");
			String linkId = "";
			
			System.out.println(element);
			
			if(gubun.equals("title"))
			{
				if(!element.attr("title").equals("Download"))
				{	
					if(linkUrl.indexOf("tid=") > 0)
					{	
						linkId = linkUrl.substring(linkUrl.indexOf("tid=")+4, linkUrl.length());
			
						String changeValue = Util.specialWord(element.text());
						
						linkIdTitle.put(linkId, changeValue);
					}	
				}
			}
			
			if(gubun.equals("Download"))
			{
				if(element.attr("title").equals("Download"))
				{	
					if(linkUrl.indexOf("tid=") > 0)
					{	
						linkId = linkUrl.substring(linkUrl.indexOf("tid=")+4, linkUrl.length());			
						
						linkIdTitle.put(linkId, linkUrl);
					}	
				}
			}
		}

		if(gubun.equals("Download"))
		{
			Util.mapLog(linkIdTitle);
		}
		
		return linkIdTitle;
	}
	
	public HashMap<String, String> nayaaPageLink(Elements page, String url)
	{
		HashMap<String, String> pageLink = new HashMap<String, String>();
		
		for (Element element : page) {
			
			if(element.className().equals("page pagelink"))
			{
				if(Util.isNum(element.text()))
				{
					if(!pageLink.containsKey(element.text()))
					{
						pageLink.put(element.text(), element.attr("abs:href"));
					}
				}
			}
		}
		
		// 자신은 누락이 발생 하여 따로 넣어줌
		pageLink.put("0", url);
		
		Util.mapLog(pageLink);
		
		return pageLink;
	}
	



}
