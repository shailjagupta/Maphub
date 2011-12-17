package com.maphub.Activity;

import com.maphub.Service.*;
import java.util.List;
import java.util.Vector;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.ClipData.Item;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Shailja
 * This activity will display the selected map on full screen
 *
 */

public class WebAndroid extends Activity {
	private static final String TAG = WebAndroid.class.getSimpleName();
	private static final String LOG_TAG = "WebViewDemo";
	private WebView mWebView;
	EditText a;
	String urll;
	int width,height;


	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main2);
		mWebView = (WebView) findViewById(R.id.webview);
		Bundle b =getIntent().getExtras();
		urll = b.getString("imageurl");
		width = b.getInt("width");
		height = b.getInt("height");
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("file:///android_asset/maphub.html");

	}

	/**
	 * Provides a hook for calling "alert" from javascript. Useful for
	 * debugging your javascript.
	 */
	final class MyWebChromeClient extends WebChromeClient {

		@Override
		public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
			Log.d(LOG_TAG, message);
			result.confirm();
			return true;
		}
	}


	public class JavaScriptInterface {
		Context mContext;

		JavaScriptInterface(Context c) {
			mContext = c;
		}

		public void showToast(String toast){
			Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
		}

		/**
		 * This method provides the url to the page of the map
		 * @return
		 * url of the map
		 */
		public String getur(){
			//String ur ="http://europeana.mminf.univie.ac.at/maps/g3190.ct003131/";
			String tem = urll.substring(0,56);
			//Toast.makeText(getApplicationContext(), tem, Toast.LENGTH_SHORT).show();
			return(urll);
		}
		public int getwidth(){
			//int width =15551;
			return(width);
		}
		public int getheight(){
			//int height = 8640;
			return(height);
		}
		public void er(String msg){
			//Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();    	
			AlertDialog.Builder myDialog
			= new AlertDialog.Builder(WebAndroid.this);        	    
			myDialog.setMessage(msg);
			myDialog.setPositiveButton("OK", null);
			myDialog.show();
		}
		public void mesg(String msg){
			Toast.makeText(getApplicationContext(), msg,Toast.LENGTH_LONG).show();    
		}

		public String addAnnotation(String wkt_data){
			return ("1");

		}

	}
}
