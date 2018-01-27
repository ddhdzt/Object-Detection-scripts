package cn.edu.scut.dataprocessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GetDirFileNames {
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(System.in);
		input = new Scanner(System.in);
		System.out.println("输入图片文件路径：");
		String imgPath = input.nextLine() + '/';
		
		File imgDir = new File(imgPath);
		if(!imgDir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgDir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		
		File[] imgFiles = imgDir.listFiles();
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/filenames.txt"));
		for(File file: imgFiles){
			String fileName = imgPath + file.getName();
			bw.write(fileName + "\n");
		}
		bw.close();
	}
}
