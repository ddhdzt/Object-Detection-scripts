package cn.edu.scut.dataprocessing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import cn.edu.scut.domain.VocGoods;



public class DetectXmlFiles {
	private static Document document;
	private static Scanner input;
	
	public static void main(String[] args) throws Exception{
		input = new Scanner(System.in);
		System.out.println("输入xml文件路径：");
		String XMLPath = input.nextLine() + '/';
		System.out.println("输入图片文件路径：");
		String imgPath = input.nextLine() + '/';
		File dir = new File(XMLPath);
		if(!dir.exists() || !dir.isDirectory()){
			System.out.println("it is wrong ... ");
			return;
		}
		File[] files = dir.listFiles();
		//添加类别
		Set<String> wrongFileNames = new HashSet<String>();
		List<String> definedNames = new ArrayList<String>();
		definedNames.add("terrorism");
//		definedNames.add("woman");
//		definedNames.add("child");
		//definedNames.add("truck");
		//definedNames.add("bicycle");
		//definedNames.add("car");
		//definedNames.add("motorbike");
		//definedNames.add("tricycle");
		//definedNames.add("bus");
		
		int fileCnt = 0;
		
		for(File xmlFile: files){	
			fileCnt ++;
			String fileName = xmlFile.getName();
			List<VocGoods> goodsList = new ArrayList<VocGoods>();
			SAXReader reader = new SAXReader();
			document = reader.read(xmlFile);
			Element root = document.getRootElement();
			List<Element> elements = root.selectNodes("object");
			
			//1.获取文件size
			Element size = (Element)root.selectSingleNode("size");
			Element width = (Element)size.selectSingleNode("width");
			Element height = (Element)size.selectSingleNode("height");
			
			int widthNum = Integer.parseInt(width.getText());
			int heightNum = Integer.parseInt(height.getText());
			for(Element element: elements){
				String name = element.selectSingleNode("name").getText();
				Element bndbox = (Element) element.selectSingleNode("bndbox");
				String xmin = bndbox.selectSingleNode("xmin").getText();
				String ymin = bndbox.selectSingleNode("ymin").getText();
				String xmax = bndbox.selectSingleNode("xmax").getText();
				String ymax = bndbox.selectSingleNode("ymax").getText();
				VocGoods goods = new VocGoods(name, Integer.parseInt(xmin), Integer.parseInt(ymin), 
						Integer.parseInt(xmax), Integer.parseInt(ymax));
				goodsList.add(goods);
			}
			
			String imgFileName = imgPath + fileName.replace("xml", "jpg");
			BufferedImage sourceImg = null;
			try {
				sourceImg =ImageIO.read(new FileInputStream(imgPath + fileName.replace("xml", "jpg")));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("img file wrong" + imgFileName);
			}
			
			if(fileCnt % 1000 == 0){
				System.out.println("file cnt: " + fileCnt);
			}
			
			int trueW = 0;
			int trueH = 0;
			if(sourceImg != null){
				trueW = sourceImg.getWidth();
				trueH = sourceImg.getHeight();
			}else{
				wrongFileNames.add(fileName.replace("xml", "jpg"));
			}
			
			//1.width 或者 height <=0
			if(widthNum !=trueW || heightNum != trueH){
				System.out.println("heightNum: " + heightNum);
				System.out.println("trueH: " + trueH);
				System.out.println("widthNum: " + widthNum);
				System.out.println("trueW: " + trueW);
				System.out.println(fileName + ", it is a size problem");
				wrongFileNames.add(fileName);
				width.setText(trueW+"");
				height.setText(trueH+"");
				writeXML(xmlFile);
			}
			
			//2.出现了不属于这8类的name
			boolean isWrongFile = false;
			for(VocGoods goods: goodsList){
				
				if(!definedNames.contains(goods.name)){
					System.out.println(fileName + ", it is a object name problem");
					isWrongFile = true;
					break;
				}
				
				//3.超出边界
				if(goods.xmin < 0 || goods.ymin <0 || goods.xmax > widthNum || goods.ymax > heightNum){
					System.out.println(fileName + ", it is a object size problem");
					isWrongFile = true;
					break;
				}
			}
			if(isWrongFile){
				wrongFileNames.add(fileName);
			}
			
				
		}
		System.out.println("wrong file size = " + wrongFileNames.size());
		System.out.println(wrongFileNames);
		
		System.out.println("****************************");
		for(String wrongName: wrongFileNames){
			System.out.println(wrongName);
		}
		
		//delete file
		/*
		for(File xmlFile: files){
			String xmlName = xmlFile.getName();
			if(!wrongFileNames.contains(xmlName)){
				System.out.println("delete xml file : " + xmlName);
				xmlFile.delete();
			}
		}
		*/
	}
	
	public static  void deleteWrongNameObjectFromXmlFile() throws Exception{
		File dir = new File("D:/Annotations-wrong");
		if(!dir.exists() || !dir.isDirectory()){
			System.out.println("it is wrong ... ");
			return;
		}
		File[] files = dir.listFiles();
		
		List<String> definedNames = new ArrayList<String>();
		definedNames.add("person");
		definedNames.add("vehicle");
		definedNames.add("road_sign");
		definedNames.add("traffic_signal_lamp");
		definedNames.add("suitcase");
		definedNames.add("bag");
		definedNames.add("knife");
		definedNames.add("gun");
		
		
		for(File xmlFile: files){	
			String fileName = xmlFile.getName();
			List<VocGoods> goodsList = new ArrayList<VocGoods>();
			SAXReader reader = new SAXReader();
			document = reader.read(xmlFile);
			Element root = document.getRootElement();
			List<Element> elements = root.selectNodes("object");
			
			//1.获取文件size
			Element size = (Element)root.selectSingleNode("size");
			Element width = (Element)size.selectSingleNode("width");
			Element height = (Element)size.selectSingleNode("height");
			
			int widthNum = Integer.parseInt(width.getText());
			int heightNum = Integer.parseInt(height.getText());
			for(Element element: elements){
				String name = element.selectSingleNode("name").getText();
				if(!name.equals("face") && !name.equals("test")){			//剔除 face 和 test object		
					Element bndbox = (Element) element.selectSingleNode("bndbox");
					String xmin = bndbox.selectSingleNode("xmin").getText();
					String ymin = bndbox.selectSingleNode("ymin").getText();
					String xmax = bndbox.selectSingleNode("xmax").getText();
					String ymax = bndbox.selectSingleNode("ymax").getText();
					VocGoods goods = new VocGoods(name, Integer.parseInt(xmin), Integer.parseInt(ymin), 
							Integer.parseInt(xmax), Integer.parseInt(ymax));
					goodsList.add(goods);
				}
			}
			//copy一份xml文件
			createXmlFile("D:/Annotations-new/" + fileName, "D:/Annotations-wrong/" + fileName, goodsList, widthNum, heightNum, "object", fileName.split("\\.")[0]);
		}
	}
	
	private static void createXmlFile(String newXmlFileName,
			String nowFilename, List<VocGoods> goodsList, int imgW, int imgH, String category, String trueName) throws Exception, FileNotFoundException {
        //2.第二种 创建文档及设置根元素节点的方式
        Element root = DocumentHelper.createElement("annotation");
        Document document = DocumentHelper.createDocument(root);
        //给根节点添加属性
        root.addAttribute("verified", "no");
        
        //给根节点添加孩子节点
        Element folder = root.addElement("folder");
        folder.addText(category);
        
        Element filename = root.addElement("filename");
        filename.addText(trueName);
        
        Element path = root.addElement("path");
        path.addText("chinaMobilePrg/" + category);
        
        //添加size节点
        Element size = root.addElement("size");
        Element width = size.addElement("width");
        Element height = size.addElement("height");
        Element depth = size.addElement("depth");
        width.addText("" + imgW);
        height.addText("" + imgH);
        depth.addText("3");
        
        //添加segmented
        Element segmented = root.addElement("segmented");
        segmented.addText("0");
        
        //依次添加object
        for(VocGoods goods: goodsList){
        	Element object = root.addElement("object");
        	Element name = object.addElement("name");
        	Element pose = object.addElement("pose");
        	Element truncated = object.addElement("truncated");
        	Element difficult = object.addElement("difficult");
        	String trueObjectName = "";
        	if(goods.name.equals("vihicle")){
        		trueObjectName = "vehicle";
        	}else if(goods.name.equals("road_vehicle")){
        		trueObjectName = "road_sign";
        	}else if(goods.name.equals("traffic-signal-lamp")){
        		trueObjectName = "traffic_signal_lamp";
        	}else if(goods.name.equals("road-sign")){
        		trueObjectName = "road_sign";
        	}else if(goods.name.equals("traffic_signal_light")){
        		trueObjectName = "traffic_signal_lamp";
        	}else if(goods.name.equals("traffic_singal_lamp")){
        		trueObjectName = "traffic_signal_lamp";
        	}else if(goods.name.equals("car")){
        		trueObjectName = "vehicle";
        	}else{
        		trueObjectName = goods.name;
        	}
        	name.addText(trueObjectName);        		
        	pose.addText("Unspecified");
        	truncated.addText("0");
        	difficult.addText("0");
        	
        	//bndbox
        	Element bndbox = object.addElement("bndbox");
        	Element xmin = bndbox.addElement("xmin");
        	Element ymin = bndbox.addElement("ymin");
        	Element xmax = bndbox.addElement("xmax");
        	Element ymax = bndbox.addElement("ymax");
        	xmin.addText(""+ goods.xmin);
        	xmax.addText("" + goods.xmax);
        	ymin.addText("" + goods.ymin);
        	ymax.addText("" + goods.ymax);
        }
        //把生成的xml文档存放在硬盘上  true代表是否换行
        OutputFormat format = new OutputFormat("    ",true);
        format.setEncoding("UTF-8");//设置编码格式
        XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(newXmlFileName),format);
        xmlWriter.write(document);
        xmlWriter.close();
	}
	
	@Test
	public void modifyXMLFileCategoryName() {
			String XMLPath ;
			String category;
			input = new Scanner(System.in);
			System.out.println("输入类别名：");
			category = input.nextLine();
			System.out.println("输入xml文件路径：");
			XMLPath = input.nextLine() + '/';
			File xmldir = new File(XMLPath);
			if(!xmldir.exists()){
				System.out.println("File not exit");
				return;
			}
			else if(!xmldir.isDirectory()){
				System.out.println("Not a diretory");
				return;
			}
			
			SAXReader reader = new SAXReader();	
			File[] files = xmldir.listFiles();
			for(File nowFile: files){
				try {
					System.out.println(nowFile.getName());
		            document = reader.read(nowFile);
		            
		            Element root = document.getRootElement();
		            Element ffolder = (Element) root.selectSingleNode("folder");
		            Element fname = (Element) root.selectSingleNode("filename");
		            //Element fpath = (Element) root.selectSingleNode("path");
		            List<Element> objectList = root.selectNodes("object");
		            //新的文件名
		            String nowFilename = nowFile.getName().split("\\.")[0];
		            
			        //改xml
			        fname.setText(nowFilename);
			        ffolder.setText(category);
			        //fpath.setText("chinaMobilePrg/"+category);
			        for(Element ele: objectList){
			        	Element nameEle = (Element)ele.selectSingleNode("name");
			        	String catName = nameEle.getText();
			        	System.out.println(catName);
			        	if(catName.equalsIgnoreCase("motorcycle")){
			        		nameEle.setText("motorbike");
			        	}
			        }
			        //写入xml
			        writeXML(nowFile);

		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
	    }
		
		//pretty write XML
		private static void writeXML(File file) throws IOException{
			FileOutputStream out = new FileOutputStream(file);
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter writer;
	        writer = new XMLWriter(out, format);
	        writer.write(document);
	        writer.close();
	        out.close();
		}
		 
		//改数字格式
		private static String changeInt(int init){
			StringBuilder tem = new StringBuilder(""+init);
			tem = tem.reverse();
			while(tem.length()<6) tem.append('0');
			tem = tem.reverse();
			return tem.toString();
		}
}
