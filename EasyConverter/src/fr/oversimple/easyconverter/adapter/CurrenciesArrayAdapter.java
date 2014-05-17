package fr.oversimple.easyconverter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import fr.oversimple.easyconverter.R;
import fr.oversimple.easyconverter.model.Currency;

public class CurrenciesArrayAdapter extends ArrayAdapter<Currency> {
	
	public CurrenciesArrayAdapter(Context context, List<Currency> currenciesList) {
		super(context, R.layout.currency_item_layout,currenciesList);
	}

	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
		return getCustomView(position, cnvtView, prnt);
	}

	@Override
	public View getView(int pos, View cnvtView, ViewGroup prnt) {
		return getCustomView(pos, cnvtView, prnt);
	}

	public View getCustomView(int position, View convertView, ViewGroup parent) {
		
		 LayoutInflater inflater = (LayoutInflater) getContext()
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    View rowView = inflater.inflate(R.layout.currency_item_layout, parent, false);
			    TextView textView = (TextView) rowView.findViewById(R.id.currency_title);
			    
			    textView.setText(getItem(position).getName());
			 

			    return rowView;
	}

}
