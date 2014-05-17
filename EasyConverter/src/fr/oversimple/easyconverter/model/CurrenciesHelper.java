package fr.oversimple.easyconverter.model;

import java.util.List;

public class CurrenciesHelper {
	
	/* Static definition */
	private static CurrenciesHelper helper = new CurrenciesHelper();
	
	public static synchronized CurrenciesHelper getInstance() {
		return helper;
	}
	
	/* Object definition */
	
	private List<Currency> currenciesList;
	private boolean updated;
	
	private CurrenciesHelper() {
		updated = false;
		
	}
	
	public boolean isCurrenciesListAvailable() {
		if(null != currenciesList) {
			return true;
		} else {
			return false;
		}
	}
	
	public void setCurrenciesList(List<Currency> currenciesList, boolean updated) {
		this.updated = updated;
		this.currenciesList = currenciesList;
	}
	
	public boolean hasListBeenUpdated() {
		return updated;
	}
	
	public List<Currency> getCurrenciesList() {
		return currenciesList;
	}
	
}
