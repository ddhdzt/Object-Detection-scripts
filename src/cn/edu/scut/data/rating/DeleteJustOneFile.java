package cn.edu.scut.data.rating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class DeleteJustOneFile {
	public static void main(String[] args) throws Exception{
		String deleteFile = "K:/子任务一/1.场景识别-数据-模型-实验结果/模型和实验结果/东突暴恐分子/数据/delete.txt";
		BufferedReader br = new BufferedReader(new FileReader(deleteFile));
		String xmlDir = "K:/子任务一/1.场景识别-数据-模型-实验结果/模型和实验结果/东突暴恐分子/数据/xml/";
		String line = "";
		while((line = br.readLine()) != null){
			System.out.println(line);
			String xml = xmlDir + line + ".xml";
			System.out.println(xml);
			File xmlFile = new File(xml);
			xmlFile.delete();
		}
		br.close();
	}
}
