package com.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;
import android.util.Log;

/**
 * 
 * @author Shailja
 * This is an intent service of Android which will fetch the list of the maps from server
 * and parse the xml to get maps url and other parameters required
 *
 */
public class imageService extends IntentService{
	StatusLine statusLine;

	public imageService() {
		super("In the image Service");
	}

	public imageService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		double latitude = intent.getDoubleExtra("latitude", 0);
		double longitude = intent.getDoubleExtra("longitude", 0);
		String responseString="";
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpHost targetHost = new HttpHost("localhost:8080/de.vogella.jersey.first/rest/MapHub/Maps", 8080);
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet("localhost:8080/de.vogella.jersey.first/rest/MapHub/Maps"));		
			statusLine   = response.getStatusLine();		
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);			
				out.close();
				responseString = out.toString();
				
			} else{
				//Closes the connection.
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}System.out.println(responseString);

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			statusLine.getReasonPhrase();
		}

	}


}
