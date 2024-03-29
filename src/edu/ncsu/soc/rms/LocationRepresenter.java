package edu.ncsu.soc.rms;


import java.util.List;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class LocationRepresenter extends MapActivity {
    /** Called when the activity is first created. */
	static LocationManager locationManager;
	public static Location location;
	public static int profile;
	static MapController mapController;
	static LocPositionOverlay positionOverlay;
	public static Double geoLat, geoLong;
	 
	 @Override
	  protected boolean isRouteDisplayed() {
	    return false;
	  }
	
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2);
     
        //MAP CODE=================================================
        final MapView myMapView = (MapView) findViewById(R.id.myMapView);
        mapController = myMapView.getController();

        // Configure the map display options
        myMapView.setSatellite(true);

        // Zoom in
        mapController.setZoom(17);

        // Add the AlertsPositionOverlay
        positionOverlay = new LocPositionOverlay(this);
        List<Overlay> overlays = myMapView.getOverlays();
        overlays.add(positionOverlay);
        
        
        final Button button=(Button) findViewById(R.id.button1);
    	
        button.setOnClickListener(new View.OnClickListener()
     	{
     		public void onClick(View v)
     		{
     	
     			addOverlays(myMapView);
     			updateRingerService.refresh();
     			

     		}
     	});
     	
        addOverlays(myMapView);
       //CODE TO REFRESH MAP 
        mainUITabs.mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
			if(tabId.compareTo(("Location"))==0)
			{
			
				addOverlays(myMapView);
				updateRingerService.refresh();
			}
			
			}
	    	
	    });
        
    }
    
    public static void updateOverlays()
    {
    
    }
    
    public void addOverlays(MapView myMapView){
    	 int lat,lng;
    	 
    	 
//    	 Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
    	 Drawable drawable1 = this.getResources().getDrawable(R.drawable.soundicon);
       	circleOverlays itemizedoverlay = new circleOverlays(drawable1, this);
    	 
    	 List<Overlay> mapOverlays = myMapView.getOverlays();
    	 
    	 for(int i=1;i<mapOverlays.size();i++)
    	 mapOverlays.remove(i);
    	 
    	 
    	 
//    	 Log.d("COunt overlays itesm", "" + itemizedoverlay.size());   
    	 
         String[] projection = new String[] {
                 LocationDB.KEY_ID,
                 LocationDB.KEY_PROFILE,
                 LocationDB.KEY_PLACE_LAT,
                 LocationDB.KEY_PLACE_LNG,
                 LocationDB.KEY_PROFILE,
                 LocationDB.KEY_LOCATION
                                             };

      Uri alerts =  LocationDB.CONTENT_URI;

      //Query the DB 
      Cursor cur = managedQuery(alerts,
              projection,  
              null,       
              null,       
              LocationDB.KEY_ID + " ASC");

      	//CODE TO actually read the data from the returned CURSOR
      
      if(cur.moveToFirst()){
      	 	   
      	   do{
      	   
      	   
      	   lat=Integer.parseInt(cur.getString(LocationDB.LATITUDE_COLUMN));
      	   lng=Integer.parseInt(cur.getString(LocationDB.LONGITUDE_COLUMN));
      	   
      	 String Profile=cur.getString(LocationDB.PROFILE_COLUMN);
      	 String Location=cur.getString(LocationDB.LOCATION_COLUMN);
      
      	 GeoPoint point = new GeoPoint(lat,lng);
      	    OverlayItem overlayitem = new OverlayItem(point, "Profile: " + Profile , "Place :" + Location);
      	    
      	    
      	    itemizedoverlay.addOverlay(overlayitem);
      	  
      	   }while(cur.moveToNext());
      	    
      	   mapOverlays.add(itemizedoverlay);
      	    
      	 Log.d("Overaly 2",mapOverlays.get(0).toString());  
      	Log.d("Overaly 2",mapOverlays.size()+ "" );
  //END OF MY CODE
      }
   }
    
       
    public static void addtoDB(String location_string, String profile, int dia, int email, Context context, GeoPoint point){
        final int lat = point.getLatitudeE6();
        final int lng = point.getLongitudeE6();
     
        ContentValues values=new ContentValues();
        values.put(LocationDB.KEY_LOCATION,location_string);
        values.put(LocationDB.KEY_PLACE_LAT,lat);
        values.put(LocationDB.KEY_PLACE_LNG,lng);
        values.put(LocationDB.KEY_LOCATION, location_string);
        values.put(LocationDB.KEY_PROFILE, profile);
        values.put(LocationDB.KEY_DIA, dia);
        values.put(LocationDB.KEY_EMAIL, email);
        
        @SuppressWarnings("unused")
		Uri uri=context.getContentResolver().insert(LocationDB.CONTENT_URI, values);
        Toast.makeText(context, "Profile added", 2).show();
        	
 
       
    	
    }
        
    public static void show_custom_dialog(Context c, final GeoPoint point)
    {
    	final Context toastContext=c;
    	   final Dialog dialog = new Dialog(c);
           dialog.setContentView(R.layout.alert_dialog_layout);
           dialog.setTitle("Add new profile at this location");
           dialog.setCancelable(true);
           //there are a lot of settings, for dialog, check them all out!

           //set up text
           TextView text = (TextView) dialog.findViewById(R.id.text1);
           text.setText("Name of Location");
           
         final RadioGroup mRadioGroup = (RadioGroup) dialog.findViewById(R.id.group1);
         mRadioGroup.check(R.id.vib);
         final SeekBar dia = (SeekBar)dialog.findViewById(R.id.seekbar);
         final CheckBox email=(CheckBox)dialog.findViewById(R.id.allowemail);
         email.setChecked(true);
         Button button_add = (Button) dialog.findViewById(R.id.add);
         Button button_cancel=(Button) dialog.findViewById(R.id.dismiss);
         button_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			dialog.dismiss();	
			}
		});
         button_add.setOnClickListener(new OnClickListener() {
              
        	   public void onClick(View v) {
//CALL ADDTO DB HERE
        		   
            	   
                      EditText edit=(EditText)dialog.findViewById(R.id.Location);
                      String location_text=edit.getText().toString();
                      int selected_profile=R.id.loud;
                      try{selected_profile=mRadioGroup.getCheckedRadioButtonId();}
                      catch(NullPointerException e){
                    	  selected_profile=R.id.loud;
                    	  Toast.makeText(toastContext, "No Profile Selected ! Default Profile Set", 2);
                    	
                      };
                      int dia_location=dia.getProgress();
                      Boolean allowemail=email.isChecked();
                      int email=0;
                      if(allowemail.booleanValue())
                    	  email=1;
                   
                      RadioButton checkedRadioButton = (RadioButton) mRadioGroup.findViewById(selected_profile);
                      String selected_profile_String=checkedRadioButton.getText().toString();
                      //Toast.makeText(toastContext, "The Choice is: "+checkedRadioButton.getText().toString()+" with dia:" + dia_location, 5).show();
                 
                      addtoDB(location_text,selected_profile_String, dia_location, email, toastContext, point);
                      
                      dialog.dismiss();
                 }
           });   
           
               
           dialog.show();
       }
   
    
	
	
    }


