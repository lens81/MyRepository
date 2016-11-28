package com.webCrawler.folderTheorem;

public class main {

	public static void main(String[] args) {


		String targetFoler = "D:\\DownLoad";
		
		FolderTheorem startMain = new FolderTheorem(targetFoler);
		
		startMain.setFileNumCnt(3);
		
		startMain.getFolderList();
		
		
		
	}
	
	
	

}
