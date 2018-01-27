package cn.edu.scut.data.rating;

import java.util.*;
import java.io.*;

public class deleteFiles {
	public static void main(String[] args) throws Exception{
		String deleteFile = "K:/子任务一/1.场景识别-数据-模型-实验结果/模型和实验结果/东突暴恐分子/数据/delete.txt";
		BufferedReader br = new BufferedReader(new FileReader(deleteFile));
		String line = "";
		while((line = br.readLine()) != null){
			System.out.println(line);
			String xml = line.replace("jpg", "xml");
			xml = xml.replace("img", "xml");
			System.out.println(xml);
			File xmlFile = new File(xml);
			File jpgFile = new File(line);
			xmlFile.delete();
			jpgFile.delete();
		}
		br.close();
	}
}
