package face;

import java.io.Serializable;

public class Suyuan implements Serializable{
	private int hospitalID;
	private long ID, preBlockIndex;
	private String Time1,Time2,Section;
	public Suyuan(long preBlockIndex, long ID,int hospitalID,String Section,String Time1,String Time2) {
		// TODO 自动生成的构造函数存根
		this.preBlockIndex = preBlockIndex;
		this.ID=ID;
		this.hospitalID=hospitalID;
		this.Time1=Time1;
		this.Time2=Time2;
		this.Section=Section;
	}
	public Suyuan(int hospitalID,String Time1,String Time2){
		this.hospitalID=hospitalID;
		this.Time1=Time1;
		this.Time2=Time2;
	}

	public long getPreBlockIndex(){
		return preBlockIndex;
	}
	public long getID(){
		return ID;
	}
	public int gethospitalID(){
		return hospitalID;
	}
	public String getSection(){
		return Section;
	}
	public String getTime1(){
		return Time1;
	}
	public String getTime2(){
		return Time2;
	}
	
	public static int random(int i){
		double floatNumber = Math.random();
		double number=1/(double)i;
		int j=(int) (floatNumber/number);
		return j;
		
	}
	public static void main(String[] args){
		System.out.println(random(5));
		
	}

}