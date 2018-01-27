package cn.edu.scut.dataprocessing;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class CreateTrainVal {
	private static Scanner input;
	public static void main(String[] args) throws Exception {
		String imgPath ;  //0
		input = new Scanner(System.in);
		System.out.println("输入图片文件路径：");
		imgPath = input.nextLine() + '/';

		File imgdir = new File(imgPath);
		if(!imgdir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgdir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		List<String> nameList = new ArrayList<>();
		File[] files = imgdir.listFiles();
		int cnt = 0;
		for(File nowFile: files){
			String prefix = nowFile.getName().split("\\.")[0];
			//if(prefix.contains("person"))
			System.out.println(prefix); 
			nameList.add(prefix);
			cnt ++;
		}
		System.out.println("files count: " + cnt);
		
		Collections.shuffle(nameList);
		FileWriter fr = new FileWriter("data/train.txt");
		FileWriter fr2 = new FileWriter("data/test.txt");
		FileWriter fr3 = new FileWriter("data/trainval.txt");
		FileWriter fr4 = new FileWriter("data/val.txt");
		
		int trainCnt = (int)(cnt * 0.1);
		int trainValCnt = (int)(cnt * 0.77);
		
		for(int i=0; i < trainCnt && i < nameList.size(); i++){
			fr.write(nameList.get(i)+"\n");
			fr3.write(nameList.get(i)+"\n");
		}
		
		for(int i=trainCnt; i < trainValCnt; i++){
			fr3.write(nameList.get(i)+"\n");
			fr4.write(nameList.get(i)+"\n");
		}
		for(int i=trainValCnt; i < nameList.size(); i++){
			fr2.write(nameList.get(i)+"\n");
		}
		
		fr.close();
		fr2.close();
		fr3.close();
		fr4.close();
    }
}
