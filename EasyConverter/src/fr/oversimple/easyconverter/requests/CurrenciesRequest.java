package fr.oversimple.easyconverter.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class CurrenciesRequest extends StringRequest {

	public CurrenciesRequest(Response.Listener<String> stringListener,
			Response.ErrorListener errorListener) {
		super("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml",
				stringListener, errorListener);
	}
}
