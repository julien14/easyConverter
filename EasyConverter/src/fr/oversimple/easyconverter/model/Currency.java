package fr.oversimple.easyconverter.model;

public class Currency {

	private String code;
	private String name;
	private double changeRate;
	
	public Currency(String code, String name, double changeRate) {
		this.code = code;
		this.name = name;
		this.changeRate = changeRate;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
		return "Currency [code=" + code + ", name=" + name + ", changeRate="
				+ changeRate + "]";
	}

	
	
}
