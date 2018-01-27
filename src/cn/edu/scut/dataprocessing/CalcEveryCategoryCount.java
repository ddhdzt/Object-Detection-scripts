package cn.edu.scut.dataprocessing;

import java.util.*;
import java.io.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class CalcEveryCategoryCount {
	
	public static Document document;
	
	public static void main(String[] args) throws Exception{
		Scanner input = new Scanner(System.in);
		input = new Scanner(System.in);
		System.out.println("输入xml文件路径：");
		String xmlPath = input.nextLine();
		String path = "data";		
		BufferedWriter brVeh = new BufferedWriter(new FileWriter(path+"/bicycle.txt"));
//		BufferedWriter brPer = new BufferedWriter(new FileWriter(path+"/person.txt"));
		BufferedWriter brBag = new BufferedWriter(new FileWriter(path+"/bus.txt"));
		BufferedWriter brSuit = new BufferedWriter(new FileWriter(path+"/car.txt"));
		BufferedWriter brTraffic = new BufferedWriter(new FileWriter(path+"/motorbike.txt"));
		BufferedWriter brTruck = new BufferedWriter(new FileWriter(path+"/truck.txt"));
		BufferedWriter brTricycle = new BufferedWriter(new FileWriter(path+"/tricycle.txt"));
		BufferedWriter brTotal = new BufferedWriter(new FileWriter(path+"/total.txt"));
//		BufferedWriter brKnife = new BufferedWriter(new FileWriter(path+"/knife.txt"));
//		BufferedWriter brGun = new BufferedWriter(new FileWriter(path+"/gun.txt"));
//		BufferedWriter brRoad = new BufferedWriter(new FileWriter(path+"/road_sign.txt"));
		
		
		File xmlDir = new File(xmlPath);
		//1.读取xml目录
		if(!xmlDir.isDirectory()){
			System.out.println("it is not a directory ... ");
			return;
		}
		
		File[] xmlFiles = xmlDir.listFiles();
		int cnt = 0;
		for(File xmlFile: xmlFiles){
			cnt ++;
			String fileName = xmlFile.getName().replace(".xml", "");
			System.out.println(fileName);
			Set<String> nameSet = getNameSet(xmlFile);
			boolean flag = true;
			//1=vehicle
			//if(nameSet.contains("bicycle")){
			if(nameSet.contains("man")){
				brVeh.write(fileName + "\n");
				flag = false;
			}
			//2=text
			//3=person
//			if(nameSet.contains("person")){
//				brPer.write(fileName + "\n");
//			}
			//4=bag
			if(nameSet.contains("woman")){
			//if(nameSet.contains("bus")){
				brBag.write(fileName + "\n");
				flag = false;
			}
			
			//5=suitcase
			//if(nameSet.contains("car")){
			if(nameSet.contains("child")){
				brSuit.write(fileName + "\n");
			}
			
			//6=traffic_signal_lamp
			if(nameSet.contains("motorbike")){
				brTraffic.write(fileName + "\n");
				flag = false;
			}
			
			if(nameSet.contains("truck")){
				brTruck.write(fileName + "\n");
			}
			
			if(nameSet.contains("tricycle")){
				brTricycle.write(fileName + "\n");
				flag = false;
			}
			
			if(!flag){
				brTotal.write(fileName + "\n");
			}

			//7=road_sign
//			if(nameSet.contains("road_sign")){
//				brRoad.write(fileName + "\n");
//				brRoad.flush();
//			}
//			
//			//8=knife
//			if(nameSet.contains("knife")){
//				brKnife.write(fileName + "\n");
//				brKnife.flush();
//			}
//			
//			//9=gun
//			if(nameSet.contains("gun")){
//				brGun.write(fileName + "\n");
//				brGun.flush();
//			}
		}
		System.out.println("the total cnt: " + cnt);
		
		brBag.close();
		brVeh.close();
//		brPer.close();
		//brKnife.close();
		brTraffic.close();
		brSuit.close();
		brTruck.close();
		brTricycle.close();
		brTotal.close();
		//brGun.close();
		//brRoad.close();
	}
	
	public static Set<String> getNameSet(File xmlFile) throws Exception{
		Set<String> nameSet = new HashSet<String>();
		
		SAXReader reader = new SAXReader();
        document = reader.read(xmlFile);
        Element root = document.getRootElement();
        List<Element> elements = root.selectNodes("object");
        for(Element element: elements){
        	String name = element.selectSingleNode("name").getText();
        	nameSet.add(name);
        }
		return nameSet;
	}
	
	
}
