package cn.edu.scut.data.rating;

import java.io.File;
import java.util.*;

import cn.edu.scut.domain.*;

public class ChangeNameSameTime {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		input = new Scanner(System.in);
		System.out.println("输入xml文件路径：");
		String XMLPath = input.nextLine() + '/';
		System.out.println("输入图片文件路径：");
		String imgPath = input.nextLine() + '/';
		
		File xmldir = new File(XMLPath);
		if(!xmldir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!xmldir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		
		File imgDir = new File(imgPath);
		if(!imgDir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgDir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		String category = "face";
		File[] xmlFiles = xmldir.listFiles();
		File[] imgFiles = imgDir.listFiles();
		int index = 1;
		Set<String> fileNames = new HashSet<String>();
		for(File file: xmlFiles){
			String fileName = file.getName();
			String newName = fileName.substring(0, fileName.length()-4); //去掉后缀
			String imgName = newName + ".jpg";
			File imgFile = new File(imgPath+"/"+imgName);
			String str = category + changeInt(index);
			if(imgFile.exists()){
				file.renameTo(new File(XMLPath+str+".xml"));
				imgFile.renameTo(new File(imgPath+str+".jpg"));
			}else{
				System.out.println("it is wrong .. ");
				return;
			}
			index++;
		}	
	}
	
	//改数字格式
	private static String changeInt(int init){
		StringBuilder tem = new StringBuilder(""+init);
		tem = tem.reverse();
		while(tem.length()<5) tem.append('0');
		tem = tem.reverse();
		return tem.toString();
	}
}
