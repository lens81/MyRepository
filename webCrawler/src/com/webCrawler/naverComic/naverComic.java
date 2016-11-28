package com.webCrawler.naverComic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.sanselan.ImageReadException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.webCrawler.main.Util;

public class naverComic {

	private static HashMap sizsMap = new HashMap();
	
	public static void main(String[] args) throws InterruptedException, IOException, ImageReadException {
		// TODO Auto-generated method stub

		naverComic nComic = new naverComic();
		
		String sName = "고수";
		//String sName = "마음의소리";
		//String sName = "뷰티풀_군바리";
		
		//String sName = "아일랜드 1부";
		
		// 다운 로드 
		//downloadComin(sName, 0);

		reNameFile("D:\\DownLoad\\" + sName);
		
		//합체 
		File folder = new File("D:\\DownLoad\\" + sName);
		
		File[] folderList = folder.listFiles();
		
		for (int i = 0; i < folderList.length; i++) {
	
			try {
				String folderName = folderList[i].getPath();
				
				System.out.println("합체 폴더명 : " + folderName);
				
				mergeImage(folderName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println("실행 완료");
		
	}
	
	public static void downloadComin(String title, int epNum)
	{
		naverComic nComic = new naverComic();
		
		HashMap titleByNameMap = nComic.titleMap(title);
		
		String sTitleId =  (String)titleByNameMap.get(title);
		
		System.out.println(title + " : " + sTitleId);
		
		//ID 를 찾았으니 링크 를 만들어서  연결 할 준비를 한다. 
		//ex)648419
		// 여기서 젤 마지막 화수를 찾아 낸다. 
		
		HashMap pageMap = new HashMap();
		HashMap pageEpisode = new HashMap();
		
		pageMap = nComic.maxPageURL(sTitleId, 1, pageMap);
		
		for (int i = 1; i <= pageMap.size(); i++) {
			String sUrl = (String)pageMap.get(i);
			
			System.out.println("전체 페이지  링크 주소 : " + sUrl);
			
			pageEpisode = nComic.pageEpisode(sTitleId, sUrl, pageEpisode);
		}
		
		if(epNum > 0)
		{
			PageInfo pageInfo = (PageInfo)pageEpisode.get(epNum);
			
			nComic.makeImageUrl(pageInfo.getPAGE_URL(), pageInfo.getEPISODE_TITEL(), pageInfo.getEPISODE_NO(), title);
		}
		else
		{
			for (Iterator iterator = pageEpisode.keySet().iterator(); iterator.hasNext();) {
				
				int epNo = (int)iterator.next();
				
				PageInfo pageInfo = (PageInfo)pageEpisode.get(epNo);
				
				nComic.makeImageUrl(pageInfo.getPAGE_URL(), pageInfo.getEPISODE_TITEL(), pageInfo.getEPISODE_NO(), title);
				
				//Thread.sleep(60000);

			}	
		}
		

	}
	

	// id 를 가져온다 
	public HashMap<String, String> titleMap(String name)
	{	
		HashMap<String, String> titleMap = null;
		
		try {
			
			String sSearchUrl = "http://comic.naver.com/search.nhn?keyword=";
			String sSearchName = URLEncoder.encode(name, "UTF-8");
			
			String sFullUrl = sSearchUrl + sSearchName;
			
			Elements html =  this.pageDom(sFullUrl, "a");
			
			titleMap = new HashMap<String, String>();
			
			for (Element element : html) 
			{
				String sLink 	= element.attr("abs:href");
				String sTitle 	= element.text();
				String sTitleId = "";
				
				System.out.println(sLink + " // " + sTitle);
				
				// 링크에서 webtoon 이고  title id 뽑아낸다.
				if(sLink.indexOf("titleId") > 0 && sLink.indexOf("webtoon") > 0 && (sTitle != null && !"".equals(sTitle)))
				{	
					sTitleId = sLink.substring(sLink.indexOf("titleId=")+8, sLink.length());
					
					System.out.println(sTitleId);
					
					//다시한번 링크를 걸러준다
					if(sTitleId.indexOf("&") == -1)
					{
						titleMap.put(sTitle, sTitleId);
					}
				}
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return titleMap;
		
	}
	
	// 이 만화의 전체 페이즈를 가져온다
	public HashMap maxPageURL(String titleId, int pageUrl, HashMap pageMap)
	{		
		try {
			if(pageMap == null)
			{
				pageMap = new HashMap();
			}
			
			if(pageUrl == 0)
			{
				pageUrl = 1;
			}
			
			String titleUrl =  "http://comic.naver.com/webtoon/list.nhn?titleId=" + titleId + "&page=" + Integer.toString(pageUrl);
			
			Elements html = this.pageDom(titleUrl, "a.page");
			
			HashMap<Integer, String> nowPage = new HashMap<Integer, String>();
			
			// 링크 페이지를 다 확보한다.
			for (Element element : html) {
				
				// 검증을 위해 page 가 들어가가 확인한다. 
				if(element.attr("abs:href").indexOf(titleId) > 0 && element.attr("abs:href").indexOf("page=") > 0)
				{
					if(!pageMap.containsKey(Integer.parseInt(element.text())))
					{
						// 체크 맵
						nowPage.put(Integer.parseInt(element.text()), element.attr("abs:href"));
						
						// 실 사용 맵
						pageMap.put(Integer.parseInt(element.text()), element.attr("abs:href"));
					}	
				}
			}
			
			if(nowPage.size() > 0)
			{	
				for (Iterator iterator = nowPage.keySet().iterator(); iterator.hasNext();) {
					int page = (Integer)iterator.next();		
					this.maxPageURL(titleId, page, pageMap);
				}
			}
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pageMap;
		
	}
	
	// 전체 페이지를 바탕으로 전체 에피 소드를 들고 온다.
	public HashMap pageEpisode(String titleId, String url, HashMap pageEpisode)
	{
		String titleUrl =  url;
		
		try {
			Elements html = this.pageDom(titleUrl, "a");
			
			HashMap<Integer, String> nowPage = new HashMap<Integer, String>();
			
			// 링크 페이지를 다 확보한다.
			for (Element element : html) {
				
				String pageUrl = (String)element.attr("abs:href");
				String sETitle = (String)element.text();
				
				if(pageUrl.indexOf(titleId) > 0 && pageUrl.indexOf("no") >0 && !"".equals(sETitle))
				{	
					String[] epUrl = pageUrl.split("&");
					int eNo = 0;
					
					for (int i = 0; i < epUrl.length; i++) {
						if(epUrl[i].indexOf("no=") > -1)
						{
							eNo = Integer.parseInt(epUrl[i].replaceAll("no=", ""));
						}
					}
					
					PageInfo pageInfo = new PageInfo();
					
					pageInfo.setEPISODE_NO(eNo);
					pageInfo.setEPISODE_TITEL(sETitle);
					pageInfo.setPAGE_URL(pageUrl);
					
					pageEpisode.put(eNo, pageInfo);
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pageEpisode;
	}
	
	public void makeImageUrl(String url, String title, int epsoideNum, String mangaTitle)
	{
		
		
		try {
			
			System.out.println(url + " // " + title + "  " + epsoideNum );
			
			Elements html = this.pageDom(url, "div.wt_viewer > img");
			
			for (Element element : html) {
				
				String imageUrl = element.attr("src");
				
				this.makeFile(imageUrl, title, epsoideNum, url, mangaTitle);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void makeFile(String imageUrl, String title, int epsoideNum, String webUrl, String mangaTitle) {		
		
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			
			urlConnection.setRequestProperty("Accept-Encoding", 	"gzip, deflate");
			urlConnection.setRequestProperty("Accept-Language", 	"ko");
			urlConnection.setRequestProperty("Connection", 			"Keep-Alive");
			urlConnection.setRequestProperty("Referer", 			webUrl);
			urlConnection.setRequestProperty("Host", 				"comic.naver.com");
			urlConnection.setRequestProperty("User-Agent", 			"Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");

			System.out.println("음답 요청 : " + imageUrl + "  " + urlConnection.getResponseCode());
			
			String[] arrFileName = imageUrl.split("/");
			String fileName = "";
			
			for (int i = 0; i < arrFileName.length; i++) {
				
				if(arrFileName[i].indexOf(".jpg") > 0)
				{
					fileName = arrFileName[i];
				}
				else 
				{
					fileName = "empty_" + Integer.toString(i)+ ".jpg";
				}
			}
			
			
			if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				InputStream is = urlConnection.getInputStream();
				
				title = title.replaceAll(">", "");
				title = title.replaceAll("<", "_");
				title = title.replaceAll(" ", "_");
				title = title.replaceAll("\\.", "_");
				
				mangaTitle = mangaTitle.replace(" ", "_");
				
				fileName = Util.specialWord(fileName);
				
				// 디렉 토리 작성
				//File saveDir = new File("D:\\DownLoad\\" + mangaTitle + "\\" + title);
				
				File saveDir = new File("D:\\DownLoad\\" + mangaTitle);
				
				
				if(!saveDir.isDirectory())
				{
					System.out.println("디렉토리 생성  : " + saveDir.getPath());
					saveDir.mkdir();
				}
				
				//한번더 생성함 
				File saveDir2 = new File(saveDir.getPath() + "\\" + title);
				
				if(!saveDir2.isDirectory())
				{
					System.out.println("디렉토리 생성  : " + saveDir.getPath());
					saveDir2.mkdir();
				}
				
				String filePath = saveDir2.getPath()+ "\\" + fileName;
				
				File imageFile = new File(filePath);
				
				if(imageFile.isFile())
				{
					System.out.println("파일이 존재함 : " + filePath);
					
					return;
				}

			    FileOutputStream fos = new FileOutputStream(imageFile);
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
			    
			    System.out.println(Util.getCurrentData()+ " make file : " + filePath);
			}
			
			
			urlConnection.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}

	public Elements pageDom(String url, String selectType) throws IOException
	{
		String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36";
		
		Document jsoupDoc = Jsoup.connect(url).userAgent(userAgent).get();
		
		//System.out.println(jsoupDoc);
		
		Elements element = jsoupDoc.select(selectType);
		
		return element;
	}
	
	public static void reNameFile(String baseFolder) throws IOException
	{
		File folder = new File(baseFolder);
		
		StringBuffer reFileName = new StringBuffer();
		
		for (int i = 0; i < folder.listFiles().length; i++) {
			if(folder.listFiles()[i].isDirectory())
			{
				reNameFile(folder.listFiles()[i].getPath());
			}
			else
			{
				System.out.println("FileName : " + folder.listFiles()[i].getName());
				
				File file = folder.listFiles()[i];
				
				String fileName = file.getName();			
				String[] arrayName = fileName.split("_");
				reFileName = new StringBuffer();
				
				for (int j = 0; j < arrayName.length; j++) {
					
					// 3 자리 수를 맞춘다.
					if(j == arrayName.length-1)
					{
						String fileNum = arrayName[j].replace(".jpg", "");
						
						if(fileNum.length() < 3)
						{						
							if(fileNum.length() == 1)
							{	
								fileNum = "00" + fileNum;
							}
							else if(fileNum.length() == 2)
							{
								fileNum = "0" + fileNum;
							}

//							System.out.println(fileNum);
						}
						
						reFileName.append( "_" + fileNum + ".jpg");
					}
					else
					{
						reFileName.append(arrayName[j]);					
					}				
				}
				
				if(file.isFile())
				{
					File reFile = new File(baseFolder + "//" + reFileName);
					
					if(file.renameTo(reFile))
					{
						System.out.println("성공 하였습니다. : " + reFile.getName());
					}
					
					reFile 	= null;
				}
				
				file	= null;				
			}
		}
	}
	
	public static void mergeImage(String folder) throws IOException, ImageReadException
	{
		String targetFolder = folder;
		
		//reNameFile(targetFolder);
		
		//65500 pixels
		int MAX_PIXELS = 65500;
		
		// 폴더에서 파일 리스트를 가져옴 
		File baseFolder = new File(targetFolder);
		
		File[] fileList = baseFolder.listFiles();
		
		int maxH 	= 0;
		int maxW 	= 0;
		int pageCnt = 1;
		
		
		// 이미지 전체 길이를 가져옴 
		for (int i = 0; i < fileList.length; i++) {
			
			File imageFile = fileList[i];
			
			BufferedImage bImage = ImageIO.read(imageFile);
			
			maxH = maxH + bImage.getHeight();

			if(maxW < bImage.getWidth() )
			{
				maxW = bImage.getWidth();
			}	
		}		


		BufferedImage meargeImage = new BufferedImage(maxW, maxH, BufferedImage.TYPE_INT_BGR);
		
		Graphics2D g2d = (Graphics2D) meargeImage.getGraphics();
		JpegReader jpegReader = new JpegReader(); //위에서 만든 Class
		
		g2d.setBackground(Color.BLACK);
		
		int fHeight = 0;
		int fWidth  = 0;
		
		for (int i = 0; i < fileList.length; i++) {
			
			File imageFile = fileList[i];
			
			BufferedImage bImage = jpegReader.readImage(imageFile);
			
			if( i == 0 )
			{
				g2d.drawImage(bImage, 0, 0, null);	
				fHeight = bImage.getHeight();
			}
			else
			{
				g2d.drawImage(bImage, 0, fHeight, null);	
				fHeight = fHeight + bImage.getHeight();
			}
		}
		
		if(ImageIO.write(meargeImage, "png", new File(targetFolder + "//allPage.png" )))
		{
			System.out.println("파일이 정상적으로 생성되었습니다. ");
			
			// 생성후 나머지 파일 삭제 
			
			for (int i = 0; i < fileList.length; i++) {
				
				File imageFile = fileList[i];
				
				if(imageFile.delete())
				{
					System.out.println(imageFile.getName() + " : 정상적으로 삭제 되었습니다.");
				}
				else
				{
					System.out.println(imageFile.getName() + " : 실패 하였습니다.");
				}

			}
		}
		else
		{
			System.out.println(targetFolder + "파일이 정상적으로 생성 실패 하였습니다. ");
		}
	}
}
