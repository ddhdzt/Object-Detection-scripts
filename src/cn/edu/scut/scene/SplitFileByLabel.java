package cn.edu.scut.scene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class SplitFileByLabel {
	public static void main(String[] args) throws Exception{
		String filePath = "K:/子任务一/1.场景识别-数据-模型-实验结果/模型和实验结果/道路-工厂车间-工地/数据/val.txt";
		int classNum = 3;
		List<BufferedWriter> bwList = new ArrayList<>();
		for(int i=0; i < classNum; i++){
			BufferedWriter tmpBw = new BufferedWriter(new FileWriter(filePath+"_"+i));
			bwList.add(tmpBw);
		}
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = "";
		while((line = br.readLine()) != null){
			String[] ss = line.split(" ");
			int labelId = Integer.parseInt(ss[1]);
			bwList.get(labelId).write(line+"\n");
		}
		for(int i=0; i < classNum; i++){
			bwList.get(i).close();
		}
		br.close();
	}
}
