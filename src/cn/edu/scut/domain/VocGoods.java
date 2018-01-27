package cn.edu.scut.domain;

public class VocGoods {
	public String name;
	public int xmin;
	public int ymin;
	public int xmax;
	public int ymax;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getXmin() {
		return xmin;
	}
	public void setXmin(int xmin) {
		this.xmin = xmin;
	}
	public int getYmin() {
		return ymin;
	}
	public void setYmin(int ymin) {
		this.ymin = ymin;
	}
	public int getXmax() {
		return xmax;
	}
	public void setXmax(int xmax) {
		this.xmax = xmax;
	}
	public int getYmax() {
		return ymax;
	}
	public void setYmax(int ymax) {
		this.ymax = ymax;
	}
	@Override
	public String toString() {
		return "Goods [name=" + name + ", xmin=" + xmin + ", ymin=" + ymin
				+ ", xmax=" + xmax + ", ymax=" + ymax + "]";
	}
	public VocGoods() {
		super();
		// TODO Auto-generated constructor stub
	}
	public VocGoods(String name, int xmin, int ymin, int xmax, int ymax) {
		super();
		this.name = name;
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}
	
}
