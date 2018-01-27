package cn.edu.scut.scene;

public class TestSplit {
	public static void main(String[] args) {
		String str = "abcdefg.jpg";
		String[] ss = str.split("\\.");
		System.out.println(ss[0]+ " "+ ss[1]);
	}
}
