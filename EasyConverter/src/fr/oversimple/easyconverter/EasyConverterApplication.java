package fr.oversimple.easyconverter;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class EasyConverterApplication extends Application {
	
    private RequestQueue volleyRequestQueue;
 
    @Override
    public void onCreate() {
        super.onCreate();
        volleyRequestQueue = Volley.newRequestQueue(this);
        volleyRequestQueue.start();
    }
 
    @Override
    public void onTerminate() {
        volleyRequestQueue.stop();
        super.onTerminate();
    }
 
    public RequestQueue getVolleyRequestQueue() {
        return volleyRequestQueue;
    }
}
