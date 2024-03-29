package edu.ncsu.soc.rms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class RingerManagerActivity extends Activity {
    /** Called when the activity is first created. */
	private Button tab1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);
        final TextView status=(TextView) this.findViewById(R.id.status);    
        
        startServices1();
        status.setText("Your Ringer is now Managed");
        
       this.tab1=(Button) this.findViewById(R.id.button1);
        this.tab1.setOnClickListener(new OnClickListener() {
        	@Override
            public void onClick(View v) {
        		startServices();
        			
            }
        });
       
//        startService(new Intent(this, RingerManagerService.class));
    }
	
	public void stopProgram(View v)
	{
		stopServices();
		finish();
		
	}
	
	public void startProgram(View v)
	{
		startServices();
		finish();
		
	}
	
	public void onToggleClicked(View v) {
	    // Perform action on clicks
	    if (((ToggleButton) v).isChecked()) {
        	startServices();
	    } else {
	    	
	    	stopServices();
	    }
	}
	public ServiceConnection mConnection;
public void startServices()

{
	this.startService(new Intent(this, updateRingerService.class));
	
	
	
    this.startService(new Intent(this, MissedCallService.class));
}

public void stopServices()

{
	stopService(new Intent(this, updateRingerService.class));
	
    Log.d("Service","" +stopService(new Intent(this, MissedCallService.class)));
}
public void releaseBind(){
    unbindService((ServiceConnection) this);
  }

public void startServices1()

{
	startService(new Intent(this, updateRingerService.class));
	
	bindService(new Intent(this, updateRingerService.class), mConnection, Context.BIND_AUTO_CREATE);
	
    startService(new Intent(this, MissedCallService.class));
}

}