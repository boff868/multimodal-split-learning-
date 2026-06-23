

public class Entry {
	private String name;
	private int number;
	
	public Entry(String name, int number){
		this.name = name;
		this.number = number;
	}
	
	public String getName(){
		return name;
	}
	
	public int getNumber(){
		return number;
	}
	
	public void setNumber(int number){
		this.number = number;
	}
	
	public String toString(){
		return name + " : " + number;
	}
}
