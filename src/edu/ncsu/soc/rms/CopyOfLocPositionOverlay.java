package edu.ncsu.soc.rms;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CopyOfLocPositionOverlay extends Overlay {
  Context context;

  public CopyOfLocPositionOverlay(Context _context) {
    this.context = _context;
  }

  /** Get the position location */
  public Location getLocation() {
    return location;
  }

  /** Set the position location */
  public void setLocation(Location location) {
    this.location = location;
  }

  Location location;

  private final int mRadius = 5;

  @Override
  public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    Projection projection = mapView.getProjection();

    if (location == null)
      return;

    if (shadow == false) {
      // Get the current location
      Double latitude = location.getLatitude() * 1E6;
      Double longitude = location.getLongitude() * 1E6;
      GeoPoint geoPoint = new GeoPoint(latitude.intValue(), longitude.intValue());

      // Convert the location to screen pixels
      Point point = new Point();
      projection.toPixels(geoPoint, point);

      RectF oval = new RectF(point.x - mRadius, point.y - mRadius, point.x + mRadius, point.y
          + mRadius);

      // Setup the paint
      Paint paint = new Paint();
      paint.setARGB(255, 255, 255, 255);
      paint.setAntiAlias(true);
      paint.setFakeBoldText(true);

      Paint backPaint = new Paint();
      backPaint.setARGB(180, 50, 50, 50);
      backPaint.setAntiAlias(true);

      RectF backRect = new RectF(point.x + 2 + mRadius, point.y - 3 * mRadius, point.x + 65,
          point.y + mRadius);

      // Draw the marker
      canvas.drawOval(oval, paint);
      canvas.drawRoundRect(backRect, 5, 5, backPaint);
      canvas.drawText("Here I Am", point.x + 2 * mRadius, point.y, paint);
    }
    super.draw(canvas, mapView, shadow);
  }
  
  
  public void draw_location(Location location, Canvas canvas, MapView mapView, boolean shadow, int size) {
    Projection projection = mapView.getProjection();

    if (location == null)
      return;

    if (shadow == false) {
      // Get the current location
      Double latitude = location.getLatitude() * 1E6;
      Double longitude = location.getLongitude() * 1E6;
      GeoPoint geoPoint = new GeoPoint(latitude.intValue(), longitude.intValue());

      
      
      // Convert the location to screen pixels
      Point point = new Point();
      projection.toPixels(geoPoint, point);
      
      Float sizeF=(float) size;
      

      RectF oval = new RectF(point.x - mRadius, point.y - mRadius, point.x + mRadius, point.y
          + mRadius);

      // Setup the paint
      Paint paint = new Paint();
      paint.setARGB(255, 255, 255, 255);
      paint.setAntiAlias(true);
      paint.setFakeBoldText(true);

      Paint backPaint = new Paint();
      backPaint.setARGB(180, 50, 50, 50);
      backPaint.setAntiAlias(true);

      int mRadius=size;
      RectF backRect = new RectF(point.x + 2 + mRadius, point.y - 3 * mRadius, point.x + 65,
          point.y + mRadius);

      // Draw the marker
      canvas.drawOval(oval, paint);
      canvas.drawRoundRect(backRect, 5, 5, backPaint);
      
      
      
      
      canvas.drawText("Here I Am", point.x + 2 * mRadius, point.y, paint);
    }
    super.draw(canvas, mapView, shadow);
  }

  @Override
  public boolean onTap(GeoPoint point, MapView mapView) {
    final int latitude = point.getLatitudeE6();
    final int longitude = point.getLongitudeE6();

//    final FrameLayout fl = new FrameLayout(context);
//    final EditText input = new EditText(context);
//    input.setGravity(Gravity.CENTER);

//    fl.addView(input, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
//        FrameLayout.LayoutParams.WRAP_CONTENT));

//   show_alert();
   LocationRepresenter.show_custom_dialog(context);
    

    return true;
  }
  
  //SHOW ALERT CODE
  public String show_alert() {
  	// TODO Auto-generated method stub
  	final EditText input=new EditText(context);
  	final EditText input_rad=new EditText(context);
  	
  	 AlertDialog.Builder alert_box=new AlertDialog.Builder(context);
  	 alert_box.setMessage("Do you want to add your current location?");
  	 alert_box.setView(input);
  	 
  	 alert_box.setPositiveButton("ADD",new DialogInterface.OnClickListener() {
  	 @Override
  	 public void onClick(DialogInterface dialog, int which) {
  	 // Close application
  		 String Location=input.getText().toString().trim();
  		dialog.dismiss(); 
  		
  		
  		 
  		 Toast.makeText(context, "ADDED: "+ Location, 4).show();
  		
  		
  		  		 
//  		 LocationRepresenter.addtoDB(Location, context);
  		 
  		}
  	 });
  	 alert_box.setNegativeButton("No", new DialogInterface.OnClickListener() {
  	 @Override
  	 public void onClick(DialogInterface dialog, int which) {
  	 // Do whatever you wish, i am show Toast
  	 Toast.makeText(context, "No Button Clicked", Toast.LENGTH_LONG).show();
  	 }
  	 });

  	 alert_box.show();
  	 return input.getText().toString();
  }
  
  //END OF SHOW ALERT CODE
  
  
  
  
  
}

