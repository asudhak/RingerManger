package edu.ncsu.soc.rms;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MissedCallService extends Service{
	
	TelephonyManager telephonyManager;
	PhoneStateListener listener;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "E-Mail Service Stopped. Missed calls will not be monitored", Toast.LENGTH_LONG).show();
		Log.d("Mail", "onDestroy");
		stopSelf();
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		
		
		super.onDestroy();
		
	}
	
	public void onStart(Intent intent, int startid){
		    telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

	    listener = new PhoneStateListener() {
	    
	    	String lastKnownState="";
	    	int flag=0;
	    	@Override
	      public void onCallStateChanged(int state, String incomingNumber) {
	    	    String stateString = "N/A";
	        switch (state) {
	        case TelephonyManager.CALL_STATE_IDLE:
	          stateString = "Idle";
	          if(lastKnownState=="Ringing")
	          {
//	        	  Toast.makeText(getBaseContext(), "Missed CALL", 4);  
	          }
	        	  
	          break;
	        case TelephonyManager.CALL_STATE_OFFHOOK:
	          stateString = "Off Hook";
	          flag=0;
	          break;
	        case TelephonyManager.CALL_STATE_RINGING:
	          stateString = "Ringing";
	          flag=1;
	          break;
	           
	        }
	        lastKnownState=stateString;
	        Log.d("Flag"," "+ flag);
	        if(flag==1 && stateString=="Idle"){
//	        	Toast.makeText(getBaseContext(), "Missed CALL", 4).show();
	        	
	        	      	
	        	String email=getemail(incomingNumber);
	        	
	        	if(updateRingerService.getGlobalallowemail()==1)
	        	sendmail(email);
	        }

	        Log.d("PhoneState", stateString);
	      }
	    };

	      telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	  }
		
		
	public void sendmail(String email)
		{
		String[] fields=email.split(":");
		
		email=fields[0];
		String name=fields[1];
		String place=updateRingerService.getGlobalPlace();

		Log.d("Place",place);
		
		Intent i = new Intent(Intent.ACTION_SEND);
		
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
		i.putExtra(Intent.EXTRA_SUBJECT, "Will Call you Back");
		i.putExtra(Intent.EXTRA_TEXT   , "Hi "+ name + ",\nI'm at "+ place+ " now. " + " \n I am currently busy and couldnt take your call."+ "I will you call back in sometime\n ");

		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
//		    Intent.createChooser(i, "Auto Notify Caller");
			startActivity(i);
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(getBaseContext(), "No Emails setup", Toast.LENGTH_SHORT).show();
		}

	}
	
	
	public String getemail(String phNo)
	{
		
		ContentResolver cr = getContentResolver();
		
		String name="";
		String email="";
		
		
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
	    
        	while (cur.moveToNext()) {
	    	StringBuffer phone_number=new StringBuffer();
	    	StringBuffer phone=new StringBuffer();
	        String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
		name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
 		if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
 		    
 			Cursor pCur = cr.query(
 		 		    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
 		 		    null, 
 		 		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
 		 		    new String[]{id}, null);
 		 	        while (pCur.moveToNext()) {
 		 	
 		 	        	phone_number.append(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
 		 	        	
 		 	        	for(int i=0;i<phone_number.length();i++)
 		 	        	{
 		 	        		if(phone_number.charAt(i)!='-')
 		 	        		phone.append(phone_number.charAt(i));
 		 	        	}
 		 	        	
 		 	        } 
 		 	        pCur.close();
 		 	        
 		 	      Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
 		 	    		new String[]{id}, null); 
 		 	      
 		 	    	while (emailCur.moveToNext()) { 
 		 	    	        	    email = emailCur.getString(
 		 	                          emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
 		 	     	} 
 		 	     
 		 	    	emailCur.close();
 		}
 		
 		if(phone.toString().equalsIgnoreCase(phNo))
     	{
     		break;
     	}
	    
	  }
 	
    }
		return email+":"+ name;	
		
	}
	
	
	
	
}


