package cn.edu.scut.dataprocessing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * 修改xml文件中的类别名
 *
 * @author Administrator
 *
 */
public class ModifyXmlCategoryName {
	private static Document document;
	private static Scanner input;
	
	public static void main(String[] args) {
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
		        
		        //修改类名部分
		        /*
		        for(Element ele: objectList){
		        	Element nameEle = (Element)ele.selectSingleNode("name");
		        	String catName = nameEle.getText();
		        	System.out.println(catName);
		        	if(catName.equalsIgnoreCase("motorcycle")){
		        		nameEle.setText("motorbike");
		        	}
		        }
		        */
		        for(Element ele: objectList){
		        	Element nameEle = (Element)ele.selectSingleNode("name");
		        	String catName = nameEle.getText();
		        	System.out.println(catName);
		        	if(catName.equalsIgnoreCase("motorbike")){
		        		nameEle.setText("vehicle");
		        	}
		        	if(catName.equalsIgnoreCase("bus")){
		        		nameEle.setText("vehicle");
		        	}
		        	if(catName.equalsIgnoreCase("car")){
		        		nameEle.setText("vehicle");
		        	}
		        	if(catName.equalsIgnoreCase("truck")){
		        		nameEle.setText("vehicle");
		        	}
		        	if(catName.equalsIgnoreCase("tricycle")){
		        		nameEle.setText("vehicle");
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
