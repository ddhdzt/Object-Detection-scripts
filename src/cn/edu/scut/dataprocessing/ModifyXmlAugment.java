package cn.edu.scut.dataprocessing;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import cn.edu.scut.domain.VocGoods;

public class ModifyXmlAugment {
	private static Document document;
	private static Scanner input;
	
	public static void main(String[] args) throws Exception {
		input = new Scanner(System.in);
		System.out.println("输入xml文件路径：");
		String XMLPath = input.nextLine() + '/';
		
		System.out.println("输入新的xml文件路径：");
		String newPath = input.nextLine() + '/';
		
		System.out.println("输入img文件路径：");
		String imgPath = input.nextLine() + '/';
		
		File dir = new File(XMLPath);
		if(!dir.exists() || !dir.isDirectory()){
			System.out.println("it is wrong ... ");
			return;
		}
		File[] files = dir.listFiles();
		for(File xmlFile: files){	
			String fileName = xmlFile.getName();
			System.out.println(fileName);
			List<VocGoods> goodsList = new ArrayList<VocGoods>();
			SAXReader reader = new SAXReader();
			document = reader.read(xmlFile);
			Element root = document.getRootElement();
			List<Element> elements = root.selectNodes("object");
			
			//1.获取文件size
			Element size = (Element)root.selectSingleNode("size");
			Element width = (Element)size.selectSingleNode("width");
			Element height = (Element)size.selectSingleNode("height");
			Element depth = (Element)size.selectSingleNode("depth");
			
			File img = new File(imgPath + fileName.replace("xml", "jpg"));
			Image src = ImageIO.read(img);
			List<String> wrongFileNames = new ArrayList<>();
			if(src == null){
				//System.out.println(oldImageFile + " is wrong ... ");
				wrongFileNames.add(fileName.replace("xml", "jpg"));
				continue ;
			}
			
			int widthNum = src.getWidth(null);
			int heightNum = src.getHeight(null);
			
			//int widthNum = Integer.parseInt(width.getText());
			//int heightNum = Integer.parseInt(height.getText());
			int depthNum = Integer.parseInt(depth.getText());
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
			//copy一份xml文件
			String category = "text";
			rotated180XmlFile(goodsList, widthNum, heightNum);
			String[] ss = fileName.split("\\.");
			createXmlFile(newPath + ss[0]+ "180."+ ss[1], XMLPath + fileName, goodsList, widthNum, heightNum, depthNum, category, fileName.split("\\.")[0]);
		}
	}
	
	private static void rotated180XmlFile(List<VocGoods> goodsList,
			int widthNum, int heightNum) {
		for(VocGoods goods: goodsList){
			int xmin = goods.xmin;
			int ymin = goods.ymin;
			int xmax = goods.xmax;
			int ymax = goods.ymax;
			//生成新的x,y坐标值
			goods.xmin = widthNum - xmax;
			goods.ymin = heightNum - ymax;
			goods.xmax = widthNum - xmin;
			goods.ymax = heightNum - ymin;
			
		}
		
	}

	private static void createXmlFile(String newXmlFileName,
			String nowFilename, List<VocGoods> goodsList, int imgW, int imgH, int depthNum, String category, String trueName) throws Exception, FileNotFoundException {
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
        depth.addText("" + depthNum);
        
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
        	trueObjectName = goods.name;
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


}
