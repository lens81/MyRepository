package com.webCrawler.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
public class UrlConnection {
	
	public void getFile(String urlName, String FileName) throws Exception
	{	
		String s = urlName;
		
		URL url = new URL(s);
		URL comic = new URL("http://imgcomic.naver.net/webtoon/648419/1/20150303105508_b5269b9eed9adea8e9572f14e64339b5_IMAG01_1.jpg");
		
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		HttpURLConnection comicURL = (HttpURLConnection) comic.openConnection();
		comicURL.setRequestMethod("GET"); 
		
		comicURL.setRequestProperty("Accept-Encoding", 	"gzip, deflate");
		comicURL.setRequestProperty("Accept-Language", 	"ko");
		comicURL.setRequestProperty("Connection", 		"Keep-Alive");
		comicURL.setRequestProperty("Referer", 		"http://comic.naver.com/webtoon/detail.nhn?titleId=648419&no=1&weekday=mon");
		comicURL.setRequestProperty("Host", 			"comic.naver.com");
		comicURL.setRequestProperty("User-Agent", 		"Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");
		
		
		Map<String, List<String>> hearderMap = urlConnection.getHeaderFields();
		
		for (Iterator<String> hearderKey = hearderMap.keySet().iterator(); hearderKey.hasNext();) 
		{
			String key = (String) hearderKey.next();

			List type = hearderMap.get(key);

			for (int i = 0; i < type.size(); i++) {

				System.out.println(key + " :: " + type.get(i));

				
			}
		}

//		url = new URL();
//
//		urlConnection = (HttpURLConnection) url.openConnection();
//		
//		urlConnection.setConnectTimeout(2000);
//		urlConnection.setReadTimeout(2000);
//		
		System.out.println("음답 요청 : " + comicURL.getResponseCode());
		
		
		if(comicURL.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			InputStream is = comicURL.getInputStream();
			
			String reName = FileName.replaceAll(".", "_");
			File torrrentFile = new File("D:\\DownLoad\\" + FileName + ".torrent");
			
		    //응답 코드를 C:\1.jpg에 저장
            FileOutputStream fos = new FileOutputStream(torrrentFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
             
            
            
            //응답 코드를 1024바이트 단위로 저장
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf, 0, 1024)) != -1) {
                bos.write(buf, 0, len);
                bos.flush();
            }
            
            bos.close();
            bis.close();
            fos.close();
            
            System.out.println(Util.getCurrentData()+ " make file : " + "D:\\DownLoad\\" + FileName + ".torrent");
		}
		
		urlConnection.disconnect();
		
	}
	


}
