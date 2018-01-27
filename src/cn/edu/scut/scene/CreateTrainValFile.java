package cn.edu.scut.scene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class CreateTrainValFile {
	private static Scanner input;
	public static void main(String[] args) throws Exception {
		String basePath = "K:/子任务一/1.场景识别-数据-模型-实验结果/模型和实验结果/建筑/数据/";
		String imgPath = basePath + "img" ;  //0
		String negPath = basePath + "neg";
				
		File imgdir = new File(imgPath);
		if(!imgdir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgdir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		List<String> imgList = new ArrayList<>();
		File[] files = imgdir.listFiles();
		for(File nowFile: files){
			imgList.add(nowFile.getName());
		}
		
		File negdir = new File(negPath);
		if(!negdir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!negdir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		List<String> negList = new ArrayList<>();
		File[] negFiles = negdir.listFiles();
		for(File nowFile: negFiles){
			negList.add(nowFile.getName());
		}
		//鎵撲贡
		Collections.shuffle(imgList);
		Collections.shuffle(negList);
		Thread.sleep(235);
		//鎵撲贡
		Collections.shuffle(imgList);
		Collections.shuffle(negList);
		
		//鎵撲贡
		Collections.shuffle(imgList);
		Collections.shuffle(negList);
		Thread.sleep(335);
		//鎵撲贡
		Collections.shuffle(imgList);
		Collections.shuffle(negList);
		//閰嶆垚4锛�
		List<String> trainList = new ArrayList<>();
		List<String> valList = new ArrayList<>();
		
		
		for(int i=0; i < imgList.size(); i++){
			if(i < (int)(imgList.size() * 0.75)){
				trainList.add(imgList.get(i));
			}else{
				valList.add(imgList.get(i));
			}
		}
		
		for(int i=0; i < negList.size(); i++){
			if(i < (int)(negList.size() * 0.75)){
				trainList.add(negList.get(i));
			}else{
				valList.add(negList.get(i));
			}
		}
		

		
		FileWriter trainFw = new FileWriter(basePath + "train.txt");
		BufferedWriter trainBw = new BufferedWriter(trainFw);
		
		//鎵撲贡
		Collections.shuffle(trainList);
		Thread.sleep(100);
		Collections.shuffle(trainList);
		Thread.sleep(100);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		Thread.sleep(20);
		Collections.shuffle(valList);
		Thread.sleep(300);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		//鎵撲贡
		Collections.shuffle(trainList);
		Thread.sleep(100);
		Collections.shuffle(trainList);
		Thread.sleep(100);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		Thread.sleep(20);
		Collections.shuffle(valList);
		Thread.sleep(300);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		//鎵撲贡
		Collections.shuffle(trainList);
		Thread.sleep(100);
		Collections.shuffle(trainList);
		Thread.sleep(320);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		Thread.sleep(20);
		Collections.shuffle(valList);
		Thread.sleep(300);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		//鎵撲贡
		Collections.shuffle(trainList);
		Thread.sleep(100);
		Collections.shuffle(trainList);
		Thread.sleep(180);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		Thread.sleep(200);
		Collections.shuffle(valList);
		Thread.sleep(300);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		//鎵撲贡
		Collections.shuffle(trainList);
		Thread.sleep(150);
		Collections.shuffle(trainList);
		Thread.sleep(100);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		Thread.sleep(20);
		Collections.shuffle(valList);
		Thread.sleep(300);
		Collections.shuffle(valList);
		Collections.shuffle(trainList);
		
		/*
		for(String fileName: trainList){
			if(fileName.contains("foghaze")){
				trainBw.write(fileName + " 1\n");
			}else if(fileName.contains("sandstorm")){
				trainBw.write(fileName + " 2\n");
			}else if(fileName.contains("rain")){
				trainBw.write(fileName + " 3\n");
			}else if(fileName.contains("snow")){
				trainBw.write(fileName + " 4\n");
			}
			else {
				trainBw.write(fileName + " 0\n");
			}
		}
		*/
		for(String fileName: trainList){
			if(fileName.contains("building")){
				trainBw.write(fileName + " 1\n");
			}else if(fileName.contains("construction_site")){
				trainBw.write(fileName + " 2\n");
			}else if(fileName.contains("adwerasfb")){
				trainBw.write(fileName + " 3\n");
			}else if(fileName.contains("asqewrdf")){
				trainBw.write(fileName + " 4\n");
			}
			else {
				trainBw.write(fileName + " 0\n");
			}
		}
		
		FileWriter valFw = new FileWriter(basePath + "val.txt");
		BufferedWriter valBw = new BufferedWriter(valFw);
		/*for(String fileName: valList){
			if(fileName.contains("foghaze")){
				valBw.write(fileName + " 1\n");
			}else if(fileName.contains("sandstorm")){
				valBw.write(fileName + " 2\n");
			}else if(fileName.contains("rain")){
				valBw.write(fileName + " 3\n");
			}else if(fileName.contains("snow")){
				valBw.write(fileName + " 4\n");
			}else {
				valBw.write(fileName + " 0\n");
			}
		}*/
		for(String fileName: valList){
			if(fileName.contains("building")){
				valFw.write(fileName + " 1\n");
			}else if(fileName.contains("construction_site")){
				valFw.write(fileName + " 2\n");
			}else if(fileName.contains("asdfwe")){
				valFw.write(fileName + " 3\n");
			}else if(fileName.contains("asdf")){
				valFw.write(fileName + " 4\n");
			}
			else {
				valFw.write(fileName + " 0\n");
			}
		}
		trainBw.flush();
		valBw.flush();
		trainBw.close();
		valBw.close();
		
    }
	
	
	//fire water smog ub fusw
	private static String changeInt(int init){
		StringBuilder tem = new StringBuilder(""+init);
		tem = tem.reverse();
		while(tem.length()<5) tem.append('0');
		tem = tem.reverse();
		return tem.toString();
	}
	
}