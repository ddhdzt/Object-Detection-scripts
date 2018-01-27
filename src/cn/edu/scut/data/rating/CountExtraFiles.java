package cn.edu.scut.data.rating;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import cn.edu.scut.domain.*;

public class CountExtraFiles {
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(System.in);
		input = new Scanner(System.in);
		System.out.println("输入xml文件路径：");
		String XMLPath = input.nextLine() + '/';
		System.out.println("输入图片文件路径："); 
		String imgPath = input.nextLine() + '/';
		BufferedWriter bw = new BufferedWriter(new FileWriter("data/delete.txt"));
		File xmldir = new File(XMLPath);
		if(!xmldir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!xmldir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		
		File imgDir = new File(imgPath);
		if(!imgDir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgDir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		
		File[] xmlFiles = xmldir.listFiles();
		File[] imgFiles = imgDir.listFiles();
		Set<String> fileNames = new HashSet<String>();
		for(File file: xmlFiles){
			String fileName = file.getName();

			String newName = fileName.substring(0, fileName.length()-4); //去掉后缀
			if(fileNames.contains(newName)){
				fileNames.remove(newName);
			}else{
				fileNames.add(newName);
			}
		}
		for(File file: imgFiles){
			String fileName = file.getName();
			String newName = fileName.substring(0, fileName.length()-4); //去掉后缀
			if(fileNames.contains(newName)){
				fileNames.remove(newName);
			}else{
				fileNames.add(newName);
			}
		}
		
		for(String str: fileNames){
			bw.write(str + "\n");
		}
		bw.close();
	}
}
