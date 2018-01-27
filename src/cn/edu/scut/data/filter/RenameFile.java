package cn.edu.scut.data.filter;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;

import org.junit.Test;

/**
 * 给文件夹下的文件进行重命名
 * @author Administrator
 *
 */
public class RenameFile {
	private static Scanner input;
	public static void main(String[] args) {
		String imgPath ;
		String category;
		input = new Scanner(System.in);
		System.out.println("输入类别名：");
		category = input.nextLine();
		System.out.println("输入图片文件路径：");
		imgPath = input.nextLine() + '/';
		
		
		File imgdir = new File(imgPath);
		if(!imgdir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgdir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		int index = 1;
		File[] files = imgdir.listFiles();
		for(File nowFile: files){
			File newFile = new File(imgPath + category + "_" +changeInt(index) + ".jpg");
			nowFile.renameTo(newFile);
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
	
	@Test
	public void main2() throws IOException {
		String imgPath ;
		input = new Scanner(System.in);
		System.out.println("输入图片文件路径：");
		imgPath = input.nextLine() + '/';
		
		
		File imgdir = new File(imgPath);
		if(!imgdir.exists()){
			System.out.println("File not exit");
			return;
		}
		else if(!imgdir.isDirectory()){
			System.out.println("Not a diretory");
			return;
		}
		int index = 1;
		File[] files = imgdir.listFiles();
		Set<String> wrongFileNames = new HashSet<>();
		for(File nowFile: files){
			Image src = ImageIO.read(nowFile);
			
			if(src == null){
				//System.out.println(oldImageFile + " is wrong ... ");
				wrongFileNames.add(nowFile.getName());
				return ;
			}
		}
		System.out.println(wrongFileNames);
    }
}

