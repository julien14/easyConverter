package fr.oversimple.easyconverter.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import fr.oversimple.easyconverter.EasyConverterApplication;
import fr.oversimple.easyconverter.R;
import fr.oversimple.easyconverter.activities.adapter.CurrenciesArrayAdapter;
import fr.oversimple.easyconverter.model.CurrenciesHelper;
import fr.oversimple.easyconverter.model.CurrenciesListParser;
import fr.oversimple.easyconverter.model.Currency;
import fr.oversimple.easyconverter.requests.CurrenciesRequest;

public class MainActivity extends Activity implements
		Response.Listener<String>, Response.ErrorListener,
		OnItemSelectedListener {

	private RequestQueue mVolleyRequestQueue;
	private CurrenciesHelper currenciesHelper;
	
	private EditText amountEditText;
	private Spinner leftCurrenciesSpinner;
	private Spinner rightCurrenciesSpinner;
	private Currency leftCurrency;
	private Currency rightCurrency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EasyConverterApplication application = (EasyConverterApplication) getApplication();
		mVolleyRequestQueue = application.getVolleyRequestQueue();
		
		currenciesHelper = CurrenciesHelper.getInstance();
		leftCurrenciesSpinner = (Spinner) findViewById(R.id.leftCurrenciesSpinner);
		leftCurrenciesSpinner.setOnItemSelectedListener(this);
		rightCurrenciesSpinner = (Spinner) findViewById(R.id.rightCurrenciesSpinner);
		rightCurrenciesSpinner.setOnItemSelectedListener(this);
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		
		//We look if a cache is available
		CurrenciesListParser listParser = new CurrenciesListParser(this);
		File cache = listParser.getCacheFile();
		
		if(cache.exists()) {
			try {
				String xmlContent = readFileAsString(cache);
				fillSpinner(xmlContent, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!currenciesHelper.isCurrenciesListAvailable()) {
			Toast.makeText(
					this,
					getString(R.string.internet_connection_needed),
					Toast.LENGTH_LONG).show();
			CurrenciesRequest request = new CurrenciesRequest(this, this);
			mVolleyRequestQueue.add(request);
		} else if(!currenciesHelper.hasListBeenUpdated()) {
			CurrenciesRequest request = new CurrenciesRequest(this, this);
			mVolleyRequestQueue.add(request);
		}
	}

	private String readFileAsString(File file)
            throws java.io.IOException {
        StringBuilder fileData = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            fileData.append(buf, 0, numRead);
        }
        reader.close();
        return fileData.toString();
    }
	
	public void leftButtonClicked(View v) {
		if(!checkEquationParameter()) {
			return;
		}
		TextView resulttextView = (TextView) findViewById(R.id.resultTextView);
		double result = (Double.parseDouble(amountEditText.getText().toString())
				* leftCurrency.getChangeRate()) / rightCurrency.getChangeRate();
		resulttextView.setText(result + " " + leftCurrency.getCode());
	}

	public void rightButtonClicked(View v) {
		if(!checkEquationParameter()) {
			return;
		}
		TextView resulttextView = (TextView) findViewById(R.id.resultTextView);		
		double result = (Double.parseDouble(amountEditText.getText().toString())
				* rightCurrency.getChangeRate()) / leftCurrency.getChangeRate();
		resulttextView.setText(result + " " + rightCurrency.getCode());
	}

	private boolean checkEquationParameter() {
		if(null == leftCurrency) {
			return false;
		} else if(null == rightCurrency) {
			return false;
		} else if(null == amountEditText) {
			return false;
		} else if(0 != amountEditText.getText().length()) {
			return true;
		} else {
			return false;
		}
		
	}

	@Override
	public void onErrorResponse(VolleyError arg0) {
		// TODO To handle the error
	}

	@Override
	public void onResponse(String xmlStringResponse) {
		fillSpinner(xmlStringResponse, false);
	}
	
	private void fillSpinner(String xmlStringResponse, boolean fromCache) {
		CurrenciesListParser parser = new CurrenciesListParser(this,xmlStringResponse);
		currenciesHelper.setCurrenciesList(parser.parse(), !fromCache);
		
		TextView dateTextView = (TextView) findViewById(R.id.dateTextBox);
		dateTextView.setText(parser.getSourceDate());
		
		CurrenciesArrayAdapter adapter = new CurrenciesArrayAdapter(this,
				currenciesHelper.getCurrenciesList());
		rightCurrenciesSpinner.setAdapter(adapter);
		leftCurrenciesSpinner.setAdapter(adapter);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if(parent.equals(leftCurrenciesSpinner)) {
			leftCurrency = currenciesHelper.getCurrenciesList().get(pos);
			Button leftButton = (Button) findViewById(R.id.leftButton);
			leftButton.setText(leftCurrency.getCode());
		} else if(parent.equals(rightCurrenciesSpinner)) {
			rightCurrency = currenciesHelper.getCurrenciesList().get(pos);
			Button rightButton = (Button) findViewById(R.id.rightButton);
			rightButton.setText(rightCurrency.getCode());
		} 
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// Do nothing
	}
}
