package com.webCrawler.folderTheorem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FolderTheorem {
	
	private static String TARGET_FOLDER = "";
	private static int fileNumCnt;
	private static HashMap aa = null;

	public FolderTheorem()
	{	
	}
	
	public FolderTheorem(String targetFolder)
	{
		this.TARGET_FOLDER = targetFolder;
	}
	
	public static String getTARGET_FOLDER() {
		return TARGET_FOLDER;
	}
	
	public static int getFileNumCnt() {
		return fileNumCnt;
	}

	public static void setFileNumCnt(int fileNumCnt) {
		FolderTheorem.fileNumCnt = fileNumCnt;
	}

	public static void setTARGET_FOLDER(String tARGET_FOLDER) {
		TARGET_FOLDER = tARGET_FOLDER;
	}
	
	public void getFolderList()
	{
		getFolderList("");
	}
	
	
	// 폴더 리스트를 가져온다.
	public ArrayList getFolderList(String targetFolder)
	{
		// 폴더 리스트를 가져온다.
		String fName = "";
		String fDName = "";
		ArrayList fileNameList = null;
		
		try {
			if("".equals(targetFolder))
			{
				fName = this.getTARGET_FOLDER();
			}
			else
			{
				fName = targetFolder;
			}
			
			File folder 			= new File(fName);			
			File[] folderList 		= folder.listFiles();
			
			fileNameList 			= new ArrayList();
			HashMap dirMap          = new HashMap();
			
			String fullPathName 	= "";
			
			
			for (int i = 0; i < folderList.length; i++) {
				
				if(folderList[i].isDirectory())
				{
					fDName = folderList[i].getPath();
					
					dirMap.put(folderList[i].getName(), getFolderList(fDName));
				}
				else 
				{	
					fullPathName = folderList[i].getPath() + "\\" + folderList[i].getName();
					
					fileNameList.add(fullPathName);
				}
			}
			
			System.out.println(dirMap);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileNameList;
	}




}


