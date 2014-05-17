package fr.oversimple.easyconverter.model;

public class Currency {

	private String name;
	private double changeRate;
	
	public Currency(String name, double changeRate) {
		this.name = name;
		this.changeRate = changeRate;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getChangeRate() {
		return changeRate;
	}
	public void setChangeRate(double changeRate) {
		this.changeRate = changeRate;
	}

	@Override
	public String toString() {
		return "Currency [name=" + name + ", changeRate=" + changeRate + "]";
	}
	
	
}
