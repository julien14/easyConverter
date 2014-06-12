package fr.oversimple.easyconverter.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import android.content.Context;

public class CurrenciesListParser {

	/* Static definition */
	private final static String CURRENCIES_CACHE_URI = "cache.xml";
	private static final String CUBE_NAME = "Cube";

	private static HashMap<String, String> currenciesCodeHashMap = null;

	private HashMap<String, String> extractCurrenciesCode(Context context) {
		Document document;
		SAXBuilder sxb = new SAXBuilder();
		HashMap<String, String> cHashMap = new HashMap<String, String>();

		try {
			InputStream currenciesCodeFile = context.getAssets().open(
					"currencies_codes.xml");
			document = sxb.build(currenciesCodeFile);
			List<Element> codes = document.getRootElement().getChildren();

			Iterator<Element> it = codes.iterator();

			while (it.hasNext()) {
				Element currencyElement = it.next();
				cHashMap.put(currencyElement.getAttributeValue("code"),
						currencyElement.getValue());
			}

		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}

		return cHashMap;
	}

	/* object definitions */

	private Document document;
	private SAXBuilder sxb;
	private Context context;
	private String sourceDate;

	public CurrenciesListParser(Context context) {

		if (null == currenciesCodeHashMap) {
			currenciesCodeHashMap = extractCurrenciesCode(context);
		}

		sxb = new SAXBuilder();
		this.context = context;
	}

	public CurrenciesListParser(Context context, String xmlDataSet) {
		this(context);
		try {
			Reader in = new StringReader(xmlDataSet);
			document = sxb.build(in);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

	public List<Currency> parse(String xmlDataSet) {
		try {
			Reader in = new StringReader(xmlDataSet);
			document = sxb.build(in);
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return parse();
	}

	public File getCacheFile() {
		return new File(context.getCacheDir(), CURRENCIES_CACHE_URI);
	}

	public List<Currency> parse() {
		// The first step is to create a cache
		File cache = getCacheFile();
		if (!cache.exists()) {
			try {
				cache.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		try {
			out.output(document, new FileOutputStream(cache));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Currency> currenciesList = new ArrayList<Currency>();
		/*
		 * The EUR value is not present in the list everything is function of
		 * the EUR. So EUR has a constant change rate of 1
		 */
		currenciesList.add(new Currency("EUR",currenciesCodeHashMap.get("EUR"), 1));

		Element root = document.getRootElement();
		Namespace ns = Namespace.getNamespace("",
				"http://www.ecb.int/vocabulary/2002-08-01/eurofxref");

		Element mainCube = root.getChild(CUBE_NAME, ns);
		Element subCube = mainCube.getChild(CUBE_NAME, ns);
		sourceDate = subCube.getAttributeValue("time");

		List<Element> currenciesCube = subCube.getChildren(CUBE_NAME, ns);
		Iterator<Element> it = currenciesCube.iterator();

		while (it.hasNext()) {
			Element currencyCube = it.next();
			String currencyCode = currencyCube.getAttributeValue("currency");
			currenciesList.add(new Currency(currencyCode, currenciesCodeHashMap
					.get(currencyCode), Double.parseDouble(currencyCube
					.getAttributeValue("rate"))));
		}

		return currenciesList;
	}

	public String getSourceDate() {
		return sourceDate;
	}

}
