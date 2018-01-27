package cn.edu.scut.data.rating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

import org.junit.Test;

public class SelectWrongImages {
	/**
	 * 将标定错误的图片挑选出来
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String wrongPath = "H:/中国移动项目任务一所有资料/5车辆检测/车辆3/wrong.txt";
		String filePath = "H:/中国移动项目任务一所有资料/5车辆检测/车辆3/val";
		String wrongFilePath = "H:/中国移动项目任务一所有资料/5车辆检测/车辆3/wrong_val";
		Set<Integer> wrongNos = new HashSet<>();
		BufferedReader wrongBR = new BufferedReader(new FileReader(wrongPath));
		String line = "";
		while((line = wrongBR.readLine()) != null){
			if(!line.startsWith("#")){
				System.out.println(line);
				wrongNos.add(Integer.parseInt(line.split(" ")[0]));
			}
		}
		File fileDir = new File(filePath);
		File[] files = fileDir.listFiles();
		for(File file: files){
			String fileName = file.getName();System.out.println(fileName.substring(7, 12));
			int fileNo = Integer.parseInt(fileName.substring(7, 12));
			if(wrongNos.contains(fileNo)){
				file.renameTo(new File(wrongFilePath+"/"+fileName));
			}
		}
	}
	
	@Test
	public void main2() throws Exception{
		String wrongPath = "G:/三轮车/filenames.txt";
		String filePath = "G:/三轮车/xml";
		String wrongFilePath = "G:/三轮车/xml-select";
		Set<String> wrongNames = new HashSet<String>();
		BufferedReader wrongBR = new BufferedReader(new FileReader(wrongPath));
		String line = "";
		while((line = wrongBR.readLine()) != null){
			wrongNames.add(line);
		}
		File fileDir = new File(filePath);
		File[] files = fileDir.listFiles();
		for(File file: files){
			String fileName = file.getName();
			int len = fileName.length();
			String fileNo = fileName.substring(0, len-4);
			if(wrongNames.contains(fileNo)){
				System.out.println("rename .. ");
				file.renameTo(new File(wrongFilePath+"/"+fileName));
			}
		}
		wrongBR.close();
	}
}
