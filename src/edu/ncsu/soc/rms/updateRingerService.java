package edu.ncsu.soc.rms;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

import edu.ncsu.soc.rms.myLocation.LocationResult;

public class updateRingerService extends Service {
	static String globalPlace="";
	static int globalAllowemail=0;
	
	LocationListener locationListener;
	LocationListener GPSListener;
	
	myLocation myLocation = new myLocation();

	public void locationClick() {
	    myLocation.getLocation(this, locationResult);
	    
	}

	public LocationResult locationResult = new LocationResult(){
	    @Override
	    public void gotLocation(final Location location){
	        LocationRepresenter.location=location;
//	        Toast.makeText(getBaseContext(), "your location is" + location.getLatitude(), 4).show();
//	        updateWithNewLocation(location);
	        }
	};

//	LocationManager locationManager;
//	public static Location location;
	
		private static final String TAG = "RingerService";
		public AudioManager myAM;
		
		@Override
		public IBinder onBind(Intent intent) {
			return null;
		}
	
		@Override
		public void onCreate() {
//			Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
			Log.d(TAG, "onCreate");
			}

		@Override
		public void onDestroy() {
			Toast.makeText(this, "Location Services Turned off. Profiles will not be updated", Toast.LENGTH_LONG).show();
			Log.d(TAG, "onDestroy");
			
			LocationRepresenter.locationManager.removeUpdates(GPSListener);
			LocationRepresenter.locationManager.removeUpdates(locationListener);
			
			stopSelf();
			super.onDestroy();
			stopSelf();
			
		}
		
		
		
		@Override
		public void onStart(Intent intent, int startid) {
//			Toast.makeText(this, "Now Listening", Toast.LENGTH_LONG).show();
			Log.d(TAG, "onStart");
//			locationClick();
			
			final class MyLocationListener implements LocationListener {

			      @Override
			      public void onLocationChanged(Location location1) {
//			    	  locationClick();
//			    	  updateProvider(this, "best");
			    	  try{updateWithNewLocation(location1);}catch(Exception NullPointerException){Log.d("NULL","Exception");};
			    	  		          
			      }

			      @Override
			      public void onProviderDisabled(String provider) {
			         //GPSstatus("GPS DISABLED");//Added for Debugging purposes
			      }

			      @Override
			      public void onProviderEnabled(String provider) {
			         //UNUSED
			    	  updateProvider(this, provider);
			      }

			      @Override
			      public void onStatusChanged(String provider, int status, Bundle extras) {
			        //UNUSED
			      }
			}
//			updateProvider(locationListener ,"best");
						
			locationListener=new MyLocationListener();	
			GPSListener=new MyLocationListener();
			
			
			
			
			updateProvider(locationListener, "network");
			updateProvider(GPSListener, "gps");
			
		}
		
		public static void refresh()	
		{
			try{
			LocationRepresenter.location = LocationRepresenter.locationManager.getLastKnownLocation("network");}
			catch(Exception NullPointerException)
			{
				
			}
		}
		
		
		public void updateProvider(LocationListener locationListener, String provider)
		{
			
	        LocationRepresenter.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
	        /*
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
//	        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
	        */
	        int interval=0;
	        
	        if(provider.compareTo("gps")==0)
	        	interval=60000;
	        
	        
	        LocationRepresenter.location = LocationRepresenter.locationManager.getLastKnownLocation(provider);
	        
	        
	        
	        LocationRepresenter.locationManager.requestLocationUpdates(provider, interval, 0, locationListener); 
//	        Toast.makeText(getBaseContext(), "Provider: " + provider, 3).show();
		}
		

		public void updateWithNewLocation(Location location1) {   
	    	
			@SuppressWarnings("unused")
			String latLongString = null;
		    if (location1 != null) {
		      
		    	LocationRepresenter.location=location1;
		    	
		      // Update the map location.
		    	LocationRepresenter.geoLat = LocationRepresenter.location.getLatitude() * 1E6;
		        LocationRepresenter.geoLong = LocationRepresenter.location.getLongitude() * 1E6;
		        GeoPoint point = new GeoPoint(LocationRepresenter.geoLat.intValue(), LocationRepresenter.geoLong.intValue());

		        LocationRepresenter.mapController.animateTo(point);

		        // update my position marker
		        LocationRepresenter.positionOverlay.setLocation(LocationRepresenter.location);

		      double lat = LocationRepresenter.location.getLatitude();
		      double lng = LocationRepresenter.location.getLongitude();
		      
		      latLongString=lat+" "+lng;
		      Log.d("Lat","lat"+LocationRepresenter.geoLat);
		      Log.d("Lng","lng"+LocationRepresenter.geoLong);
		           
		      
//		      Toast.makeText(getBaseContext(),"Your Location is: "+ latLongString, 3).show();
		      
		      //CODE TO UPDATE THE RINGER
		      
		    
		    updateRinger();
		     
		      //END OF CODE TO UPDATE RINGER
		      
		      
		      }
		    }

		
private void updateRinger()
{
	String[] projection = new String[] {
            LocationDB.KEY_ID,
            LocationDB.KEY_PROFILE,
            LocationDB.KEY_PLACE_LAT,
            LocationDB.KEY_PLACE_LNG,
            LocationDB.KEY_EMAIL,
            LocationDB.KEY_LOCATION,
            LocationDB.KEY_DIA
                                        };

  Uri alerts =  LocationDB.CONTENT_URI;

  Cursor cur=getBaseContext().getContentResolver().query(alerts,
		  projection,  
		  null,       
		  null,       
		  LocationDB.KEY_ID + " ASC");

  int lat, lng;
  double minDiffDistance=Integer.MAX_VALUE;
  Boolean set=false;  
  if(cur.moveToFirst()){
  	 	   
  	   do{
  	   lat=Integer.parseInt(cur.getString(cur.getColumnIndex(LocationDB.KEY_PLACE_LAT)));
  	   lng=Integer.parseInt(cur.getString(cur.getColumnIndex(LocationDB.KEY_PLACE_LNG)));
  	   int dia=Integer.parseInt(cur.getString(cur.getColumnIndex(LocationDB.KEY_DIA)));
  	  String Place=cur.getString(cur.getColumnIndex(LocationDB.KEY_LOCATION));
  	 String Profile=cur.getString(cur.getColumnIndex(LocationDB.KEY_PROFILE));
  	 
  	 int allowemail=Integer.parseInt(cur.getString(cur.getColumnIndex(LocationDB.KEY_EMAIL)));
  	 
  	 double diffDistance=getDistance(LocationRepresenter.location.getLatitude(),LocationRepresenter.location.getLongitude(),lat/1E6,lng/1E6);
  
  	 
  	 Log.d("Place", Place);
  	 Log.d("Profiles", Profile);
  	 Log.d("e-mail","" + allowemail);
  	 Log.d("diffDistance","" + diffDistance);
  	 
  	 
  	 int RingerMode = 1; 
  	int minRingerMode=1;
  	  	if(minDiffDistance>diffDistance)
  		minDiffDistance=diffDistance;
  	
  	 if(diffDistance<dia)
  	 {
  		 //Default Ringer set to Vibrate
  		 
  		 if(Profile.equalsIgnoreCase("SILENT"))
  			 RingerMode=0;
  		 else if(Profile.equalsIgnoreCase("VIBRATING"))
  			 RingerMode=1;
  		 else if(Profile.equalsIgnoreCase("LOUD"))
  			 RingerMode=2;
  		 
  		 Log.d("minDiff",""+ minDiffDistance);
  		 if(diffDistance==minDiffDistance){
  			 minRingerMode=RingerMode;
  			 set=true;
  			 			 
  		 }
  		myAM = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE); 
  		myAM.setRingerMode(minRingerMode);
//  		Toast.makeText(getBaseContext(), "Succesfully Changed Ringer to" + minRingerMode, 4).show();
  		set=true;
  		setGlobalPlace(Place);
  		setGlobalallowemail(allowemail);
  		 
  	 }
  
  	   }while(cur.moveToNext());
  	 
  }
  if(!set)
  {
	  myAM = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE); 
		myAM.setRingerMode(2);
//		Toast.makeText(getBaseContext(), "Default Ringer SEt" + 1, 4).show();
		setGlobalPlace("");
		setGlobalallowemail(0);
  }
  
}

public static double getDistance(double lat1, double lon1, double lat2, double lon2) {

    final double Radius = 6371 * 1E3; // Earth's mean radius

    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
        * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return Radius * c;
  }

public void setGlobalPlace(String place)
{
	globalPlace=place;
}

public static String getGlobalPlace()
{
	return globalPlace;
}

public void setGlobalallowemail(int email)
{
	globalAllowemail=email;
}

public static int getGlobalallowemail()
{
	return globalAllowemail;
}


}
	
	
	
	
	
	
	
	
	
	
	
	

