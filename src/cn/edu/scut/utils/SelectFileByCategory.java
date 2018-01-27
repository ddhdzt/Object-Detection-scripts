package cn.edu.scut.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;

import cn.edu.scut.domain.VocGoods;

public class SelectFileByCategory {
	private static Document document;
	private static Scanner input;
	public static void main(String[] args) {
		String XMLPath ;
		String imgPath ;
		String category;
		input = new Scanner(System.in);
		System.out.println("输入类别名：");
		category = input.nextLine();
		System.out.println("输入xml文件路径：");
		XMLPath = input.nextLine() + '/';
		System.out.println("输入图片文件路径：");
		imgPath = input.nextLine() + '/';
		
		System.out.println("输入选择类别xml文件路径：");
		String catXMLPath = input.nextLine() + '/';
		System.out.println("输入选择类别图片文件路径：");
		String catImgPath = input.nextLine() + '/';

		File xmldir = new File(XMLPath);
		if(!xmldir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!xmldir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		
		File[] files = xmldir.listFiles();
		File imgFile;
		
		//物体类别List
		List<String> needNames = new ArrayList<>();
		int lineCnt = 0;
		needNames.add(category);
		for(File xmlFile: files){
			lineCnt ++;
			if(lineCnt % 5000 == 0){
				System.out.println("lineCnt: " + lineCnt);
			}
			try {
				String fileName = xmlFile.getName();
				List<VocGoods> needGoodsList = new ArrayList<VocGoods>();
				SAXReader reader = new SAXReader();
				document = reader.read(xmlFile);
				Element root = document.getRootElement();
				List<Element> elements = root.selectNodes("object");	
				//1.获取文件size
				Element size = (Element)root.selectSingleNode("size");
				Element width = (Element)size.selectSingleNode("width");
				Element height = (Element)size.selectSingleNode("height");
				Element depth = (Element)size.selectSingleNode("depth");
				int widthNum = Integer.parseInt(width.getText());
				int heightNum = Integer.parseInt(height.getText());
				int depthNum = Integer.parseInt(depth.getText());
				
	            List<VocGoods> goodsList = getGoodsList(xmlFile);
	            //是否所需类别，执行复制
	            for(int i=0; i < goodsList.size(); i++){
	            	VocGoods tmpGoods = goodsList.get(i);
	            	if(tmpGoods.name.equals(category)){
	            		needGoodsList.add(tmpGoods);
	            	}
	            }
	            if(needGoodsList.size() > 0){
	            	//执行复制
	            	copyFile(imgPath+fileName.replace("xml", "jpg"), catImgPath+fileName.replace("xml", "jpg"));
	            	createXmlFile(catXMLPath+fileName, needGoodsList, widthNum, heightNum, depthNum, category, fileName.split("\\.")[0]);
	            }
	            
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

	private static void createXmlFile(String newXmlFileName,
			List<VocGoods> goodsList, int imgW, int imgH, int depNum, String category, String trueName) throws Exception, FileNotFoundException {
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
        depth.addText("" + depNum);
        
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
	
	/**读取xml文件，解析得到标定图中物体信息
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static List<VocGoods> getGoodsList(File xmlFile) throws Exception{
		List<VocGoods> goodsList = new ArrayList<VocGoods>();
		
		SAXReader reader = new SAXReader();
        document = reader.read(xmlFile);
        Element root = document.getRootElement();
        List<Element> elements = root.selectNodes("object");
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

		return goodsList;
	}
	
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
//					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}
	}
	
	@Test
	public void testDepth() throws Exception{
		String XMLPath ;
		Document document;
		Scanner input;
		input = new Scanner(System.in);
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
		
		File[] files = xmldir.listFiles();
		
		for(File xmlFile: files){
			String fileName = xmlFile.getName();
			List<VocGoods> needGoodsList = new ArrayList<VocGoods>();
			SAXReader reader = new SAXReader();
			document = reader.read(xmlFile);
			Element root = document.getRootElement();
			List<Element> elements = root.selectNodes("object");	
			//1.获取文件size
			Element size = (Element)root.selectSingleNode("size");
			Element width = (Element)size.selectSingleNode("width");
			Element height = (Element)size.selectSingleNode("height");
			Element depth = (Element)size.selectSingleNode("depth");
			int widthNum = Integer.parseInt(width.getText());
			int heightNum = Integer.parseInt(height.getText());
			int depthNum = Integer.parseInt(depth.getText());
			if(depthNum != 3){
				System.out.println(fileName);
			}
		}
	}
}
