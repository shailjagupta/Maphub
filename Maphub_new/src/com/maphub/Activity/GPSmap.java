package com.maphub.Activity;

import android.app.Activity;
import com.maphub.Service.*; 
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Shailja Gupta
 * This page finds the current latitude and longitude of the location and if GPS is enabled
 * on the device then fetch a list of maps to be displayed.
 *
 */
public class GPSmap extends Activity implements GenericResultReceiver.Receiver{

	LocationManager locManager;
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
	ListView list;
	LazyAdapter adapter;
	public GenericResultReceiver mReceiver;
	double latitude,longitude;


	@Override
	protected void onPause() {
		super.onPause();
		System.exit(0);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mReceiver = new GenericResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000L,500.0f,locationListener);
		Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location != null)                                
		{
			latitude = location.getLatitude();
			System.out.println("latitude"+latitude);
			longitude = location.getLongitude();
			System.out.println(longitude);
			Toast.makeText(getApplicationContext(), " Current Lat:  "+latitude+ " Current Long: "+ longitude,Toast.LENGTH_LONG ).show();
		}  

		/*
		 * Based on latitude and longitude, server request is made to fetch 
		 * list of maps containing this location
		 */
		final Intent intent = new Intent(Intent.ACTION_SYNC, null, getApplicationContext(), imageService.class);
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		startService(intent);

		/**
		 *  the activity will be displayed only when GPS is enabled
		 */

		if ( locManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
			final String[] mStrings={
					"http://europeana.mminf.univie.ac.at/maps/g3170.ct001892/TileGroup0/0-0-0.jpg",
					"http://europeana.mminf.univie.ac.at/maps/g3200.ct000951/TileGroup0/0-0-0.jpg",
					"http://europeana.mminf.univie.ac.at/maps/g3196a.ct002487/TileGroup0/0-0-0.jpg",
					"http://europeana.mminf.univie.ac.at/maps/g3200.ct000382/TileGroup0/0-0-0.jpg",
					"http://europeana.mminf.univie.ac.at/maps/g3200.ct000270/TileGroup0/0-0-0.jpg",
					"http://europeana.mminf.univie.ac.at/maps/g3200.ct000575/TileGroup0/0-0-0.jpg"
			};
			final int width=200;
			final int height = 300;
			list=(ListView)findViewById(R.id.list);
			adapter=new LazyAdapter(this, mStrings);
			list.setAdapter(adapter);
			list.setClickable(true);
			/*
			 * to display the images of map on the page
			 */
			list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

					Object o = list.getItemAtPosition(position);
					int p = position+1;
					Bundle b = new Bundle();           
					b.putString("imageurl", mStrings[position]);
					b.putInt("width", width);
					b.putInt("height", height);
					Intent meetingIntent = new Intent(getApplicationContext(), WebAndroid.class);
					meetingIntent.putExtras(b);
					startActivity(meetingIntent);
					
				}
			});
		}

	}

	private void updateWithNewLocation(Location location) {
			String latLongString = "";
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			latLongString = "Lat:" + lat + "\nLong:" + lng;


		} else {
			latLongString = "No location found";
		}
		Toast.makeText(getApplicationContext(), "Lat"+latLongString,Toast.LENGTH_LONG ).show();


	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}

		public void onProviderDisabled(String provider) {

			AlertDialog.Builder alertbox = new AlertDialog.Builder(GPSmap.this);
			alertbox.setMessage("GPS is disabled. Cannot fetch current location");
			alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) { 
					System.exit(0);
				}
			});
			alertbox.create();
			alertbox.show();
			//	updateWithNewLocation(null);
		}

		public void onProviderEnabled(String provider) {
			Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	@Override
	public void onDestroy()
	{
		adapter.imageLoader.stopThread();
		list.setAdapter(null);
		super.onDestroy();
	}



	public OnClickListener myClickListener=new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Toast.makeText(getApplicationContext(), "You have selected "
					, Toast.LENGTH_SHORT).show();

		}
	};


	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub

	}

}