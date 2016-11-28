package com.webCrawler.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class Util {

	public static String getCurrentData() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

		return sdf.format(new Date());

	}
	
	// ������ ���� ����
	public static String specialWord(String word)
	{
		if(word == null || "".equals(word))
		{
			return "";
		}
		
		String[] specialSymbol = {"/","\\.","\\?","\\!","\\@","\\#","\\$","\\%","\\^","\\&","\\*","\\(","\\)","\\_","\\+","\\=","\\|","\\\\","\\}","\\]","\\{","\\[","\\\"","\\'","\\:","\\;","\\<","\\,","\\>","\\.","\\?","\\/"};
		String fullWord = word;
		String conversion = "";	
			
		for (String symbol : specialSymbol) 
		{
			conversion = fullWord.replace(symbol, "_");
			
			fullWord = conversion;
		}	
			
		System.out.println(fullWord);
	
		
		return fullWord;
	}
	
	public static void mapLog(HashMap pageLink)
	{
		System.out.println("=============================================================");
		
		for (Iterator iterator = pageLink.keySet().iterator(); iterator.hasNext();) {
			
			Object key = iterator.next();
			
			System.out.println(getCurrentData() + " :: " + key + " : " + pageLink.get(key));
		}
		
		System.out.println("MAP SIZE : " + pageLink.size());
		
	}
	
	
	//10���� �������� �ƴ��� üũ
	public static boolean isNum(String num)
	{
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
}
