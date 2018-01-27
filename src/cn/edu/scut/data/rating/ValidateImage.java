package cn.edu.scut.data.rating;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

public class ValidateImage {
	private static Document document;
	private static Scanner input;
	
	private static List<String> wrongFileNames = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		input = new Scanner(System.in);
		System.out.println("输入xml文件路径：");
		String XMLPath = input.nextLine() + '/';
		System.out.println("输入图片文件路径：");
		String imgPath = input.nextLine() + '/';
		System.out.println("输入验证图片保存路径：");
		String valImgPath = input.nextLine() + '/';
		
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
		for(File nowFile: files){
            //旧的文件名
            String oldFilename = nowFile.getName();
            //判断jpg文件是否存在
            String imgAbsPath = imgPath+oldFilename.replace("xml", "jpg");
            System.out.println("imgAbsPath = " + imgAbsPath);
            String valImg = valImgPath + oldFilename.replace("xml", "jpg");
            imgFile = new File(imgAbsPath);  
            if(imgFile.exists()){
            	List<VocGoods> goodsList = getGoodsList(nowFile);
            	//System.out.println(goodsList);
            	if(goodsList.size() > 0){
            		drawRectToImage(imgAbsPath, valImg, goodsList);
            	}
            }
		}
		for(String wrongFileName: wrongFileNames){
			System.out.println(wrongFileName);
		}
		
		//deleteWrongNameObjectFromXmlFile();
		//detectSizeWrongXmlFiles();
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
	
	@Test
	public  void detectXmlFiles() throws Exception{
		File dir = new File("H:/中国移动项目任务一所有资料/3人体检测/person/Annotations");
		if(!dir.exists() || !dir.isDirectory()){
			System.out.println("it is wrong ... ");
			return;
		}
		File[] files = dir.listFiles();
		
		Set<String> wrongFileNames = new HashSet<String>();
		List<String> definedNames = new ArrayList<String>();
		definedNames.add("man");
		definedNames.add("woman");
		definedNames.add("child");
//		definedNames.add("vehicle");
//		definedNames.add("road_sign");
//		definedNames.add("traffic_signal_lamp");
//		definedNames.add("suitcase");
//		definedNames.add("bag");
//		definedNames.add("knife");
//		definedNames.add("gun");
		
		
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
				Element bndbox = (Element) element.selectSingleNode("bndbox");
				String xmin = bndbox.selectSingleNode("xmin").getText();
				String ymin = bndbox.selectSingleNode("ymin").getText();
				String xmax = bndbox.selectSingleNode("xmax").getText();
				String ymax = bndbox.selectSingleNode("ymax").getText();
				VocGoods goods = new VocGoods(name, Integer.parseInt(xmin), Integer.parseInt(ymin), 
						Integer.parseInt(xmax), Integer.parseInt(ymax));
				goodsList.add(goods);
			}
			//1.width 或者 height <=0
			if(widthNum <=0 || heightNum <= 0){
				System.out.println(fileName + ", it is a size problem");
				wrongFileNames.add(fileName);
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
		
		//delete file
//		for(File xmlFile: files){
//			String xmlName = xmlFile.getName();
//			if(!wrongFileNames.contains(xmlName)){
//				System.out.println("delete xml file : " + xmlName);
//				//xmlFile.delete();
//			}
//		}
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
	
	
	public static  void detectSizeWrongXmlFiles() throws Exception{
		String xmlPath = "D:/Annotations-bak/";
		File dir = new File(xmlPath);
		if(!dir.exists() || !dir.isDirectory()){
			System.out.println("it is wrong ... ");
			return;
		}
		File[] files = dir.listFiles();
		
		Set<String> wrongSizeFileNames = new HashSet<String>();
		Set<String> wrongNameFileNames = new HashSet<String>();
		Set<String> wrongDepthFileNames = new HashSet<String>();
		Set<String> wrongWHFileNames = new HashSet<String>();
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
			
			int widthNum = Integer.parseInt(width.getText());
			int heightNum = Integer.parseInt(height.getText());
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
			
			BufferedImage sourceImg =ImageIO.read(new FileInputStream("G:/coco/JPEGImages/" + fileName.replace("xml", "jpg")));
			if(sourceImg != null){
				int trueW = sourceImg.getWidth();
				int trueH = sourceImg.getHeight();
				if(trueH != heightNum || trueW != widthNum){
					wrongWHFileNames.add(fileName);
				}
				
			}
			
			//0. depth != 3
			if(depthNum != 3){
				System.out.println(fileName+", it is a depth problem ... ");
				wrongDepthFileNames.add(fileName);
				//createXmlFile("D:/Annotations-depth/" + fileName, xmlPath + fileName, goodsList, widthNum, heightNum, "object", fileName.split("\\.")[0]);
			}
			
			//1.width 或者 height <=0
			if(widthNum <=0 || heightNum <= 0){
				System.out.println(fileName + ", it is a size problem");
				wrongSizeFileNames.add(fileName);
			}
			
			//1.width 或者 height <=0
			if(widthNum <=0 || heightNum <= 0){
				System.out.println(fileName + ", it is a size problem");
				wrongSizeFileNames.add(fileName);
			}
			
			
			
			//2.出现了不属于这8类的name
			boolean isWrongFile = false;
			for(VocGoods goods: goodsList){
				if(!definedNames.contains(goods.name)){
					System.out.println(fileName + ", it is a object name problem");
					wrongNameFileNames.add(fileName);
					break;
				}
				//3.超出边界
				if(goods.xmin < 0 || goods.ymin <0 || goods.xmax > widthNum || goods.ymax > heightNum){
					System.out.println(fileName + ", it is a object size problem");
					wrongSizeFileNames.add(fileName);
					break;
				}
			}
//			if(isWrongFile){
//				wrongFileNames.add(fileName);
//			}	
			
			
			
		}
		System.out.println("wrong file size = " + wrongWHFileNames.size());
		System.out.println(wrongNameFileNames);
		System.out.println(wrongSizeFileNames);
		System.out.println(wrongDepthFileNames);
		System.out.println(wrongWHFileNames);
	}
	
	public static void drawRectToImage(String oldImageFile, String destImageFile, List<VocGoods> goodsList) throws Exception {
		int fontSize = 24;

		File img = new File(oldImageFile);

		Image src = ImageIO.read(img);
		
		if(src == null){
			//System.out.println(oldImageFile + " is wrong ... ");
			wrongFileNames.add(oldImageFile);
			return ;
		}
		
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		
		
		//System.out.println("width = " + width);
		//System.out.println("height = " + height);
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.drawImage(src, 0, 0, width, height, null);
		
		for(VocGoods good: goodsList){
			String pressText = good.name;
			int x = good.xmin;
			int y = good.ymin;
			int xEnd = good.xmax;
			int yEnd = good.ymax;
			int rectWidth = xEnd - x;
			int rectHeight = yEnd - y;
			g.setColor(Color.red);
			g.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP));
			// 在指定坐标绘制水印文字
			g.drawString(pressText, x+10, y+36);
			g.setStroke(new BasicStroke(2.0f));
			g.drawRect(x, y, rectWidth, rectHeight);
		}
		g.dispose();
		ImageIO.write((BufferedImage) image, "JPEG", new File(destImageFile));// 输出到文件流
	}

}

