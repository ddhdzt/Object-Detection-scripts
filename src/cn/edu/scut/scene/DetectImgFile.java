package cn.edu.scut.scene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class DetectImgFile {
	private static Scanner input;
	
	public static void main(String[] args) {
		input = new Scanner(System.in);
		System.out.println("输入图片文件路径：");
		String imgPath = input.nextLine() + '/';
		File imgdir = new File(imgPath);
		if(!imgdir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgdir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		
		File[] files = imgdir.listFiles();
		int fileCnt = 0;
		for(File file: files){
			fileCnt ++;
			String fileName = file.getName();
			String imgFileName = imgPath + fileName;
			BufferedImage sourceImg = null;
			try {
				sourceImg =ImageIO.read(new FileInputStream(imgFileName));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("img file wrong" + imgFileName);
			}
			if(sourceImg == null){
				System.out.println("img file wrong" + imgFileName);
			}
			
			if(fileCnt % 1000 == 0){
				System.out.println("file cnt: " + fileCnt);
			}
		}
	}
}
