package edu.ncsu.soc.rms;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;




public class MyProfiles extends Activity {
    private static final String TAG = "ContentUserDemo";
    public Context context_this;    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.tab3);
      context_this=MyProfiles.this;
      
      
    
      // Get content provider and cursor
      ContentResolver cr = getContentResolver();
      Cursor cursor = cr.query(LocationDB.CONTENT_URI, null, null, null, null);
    
      // Let activity manage the cursor
      startManagingCursor(cursor);
      Log.d(TAG, "cursor.getCount()=" + cursor.getCount());
    
      // Get the list view
      final ListView listView = (ListView) findViewById(R.id.listView);
      
      String[] from = { LocationDB.KEY_LOCATION, LocationDB.KEY_PROFILE, LocationDB.KEY_EMAIL, LocationDB.KEY_ID, LocationDB.KEY_DIA};
      int[] to = { R.id.textName, R.id.textValue , R.id.textEmail , R.id.lngValue ,R.id.diaValue};
      SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);
      listView.setAdapter(adapter);
      listView.setTextFilterEnabled(true);
      
      listView.setOnItemClickListener(new OnItemClickListener()
      {
    	   
      public void onItemClick(AdapterView<?> arg0, View v, int position, long id)
      {
//    	  Toast.makeText(getBaseContext(), ((TextView)v.findViewById(R.id.lngValue)).getText().toString(),4).show();  	  
    	  
    	  final String[] toDel= {((TextView)v.findViewById(R.id.lngValue)).getText().toString()};
    	  final String[] toUpd={((TextView)v.findViewById(R.id.textName)).getText().toString(),
    			  				((TextView)v.findViewById(R.id.textValue)).getText().toString(),
    			  				((TextView)v.findViewById(R.id.textEmail)).getText().toString(),
    			  				((TextView)v.findViewById(R.id.lngValue)).getText().toString(),
    			  				((TextView)v.findViewById(R.id.diaValue)).getText().toString()
    	  };
//    	  Toast.makeText(getBaseContext(), arg0.findViewById(R.id.lngValue).getResources().toString(),4).show();
    	  
      AlertDialog.Builder adb = new AlertDialog.Builder(MyProfiles.this);
      adb.setCancelable(true);
      
      adb.setNeutralButton("Update", new OnClickListener(){
    	  @Override
  		public void onClick(DialogInterface arg0, int arg1) {
  			// TODO Auto-generated method stub
  			show_update_dialog(context_this, toUpd);
    		  
  		}
      });
      adb.setMessage("Select an Option");
      
      adb.setPositiveButton("Delete", new OnClickListener(){
    	
		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			// TODO Auto-generated method stub
			//DELETE
			
			getBaseContext().getContentResolver().delete(LocationDB.CONTENT_URI, "_id=?", toDel);	
		}
    	  
      });
      
      adb.setNegativeButton("Cancel", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
		
		
		}
	});
      
      adb.show();
                                                      }

	
                                      });
      
      
      
                      }
    
    public static void show_update_dialog(Context c, String[] toUpd)
    {
    	final Context toastContext=c;
    	
    	  
    	   final Dialog dialog = new Dialog(c);
           dialog.setContentView(R.layout.alert_dialog_layout);
           dialog.setTitle("Edit profile at this location");
           dialog.setCancelable(true);
           
           
           
           for(int i=0;i<toUpd.length;i++)
           {
        	   Log.d("String "+ i ,toUpd[i]);
           }
           
           TextView text = (TextView) dialog.findViewById(R.id.text1);
           text.setText("Name of Location");
           
           
           
           
           
         final RadioGroup mRadioGroup = (RadioGroup) dialog.findViewById(R.id.group1);
         final SeekBar dia = (SeekBar)dialog.findViewById(R.id.seekbar);
         final CheckBox email=(CheckBox)dialog.findViewById(R.id.allowemail);
         
         Button button_add = (Button) dialog.findViewById(R.id.add);
         button_add.setText("Update");
         Button button_cancel=(Button) dialog.findViewById(R.id.dismiss);
         
         
         int diaValue=Integer.parseInt(toUpd[4]);
         
         dia.setProgress(diaValue);
         
         
         final EditText edit=(EditText)dialog.findViewById(R.id.Location);
                      edit.setText(toUpd[0]);
         
         final String pkey=toUpd[3];
                                           
         if(toUpd[2].compareTo("1")==0)
        		        	 email.setChecked(true);
         
         int check_id=0;
         
         if(toUpd[1].compareTo("SILENT")==0)
        	 check_id=R.id.silent;
         else if(toUpd[1].compareTo("VIBRATING")==0)
        	 check_id=R.id.vib;
         else if(toUpd[1].compareTo("LOUD")==0)
        	 check_id=R.id.loud;
         
         mRadioGroup.check(check_id);
         
         
         button_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			dialog.dismiss();	
			}
		});
         
         button_add.setOnClickListener(new View.OnClickListener() {
              
        	   public void onClick(View v) {
//CALL ADDTO DB HERE       		   
            	   
                      int selected_profile=mRadioGroup.getCheckedRadioButtonId();
                      int dia_location=dia.getProgress();
                      Boolean allowemail=email.isChecked();
                      int email=0;
                      if(allowemail.booleanValue())
                    	  email=1;
                 
                      RadioButton checkedRadioButton = (RadioButton) mRadioGroup.findViewById(selected_profile);
                      String selected_profile_String=checkedRadioButton.getText().toString();
                      String location_text=edit.getText().toString();
                      //Toast.makeText(toastContext, "The Choice is: "+checkedRadioButton.getText().toString()+" with dia:" + dia_location, 5).show();
                 
                      updateDB(location_text,selected_profile_String, dia_location, email, pkey, toastContext);
                      
                      Log.d("location_text", location_text);
                      Log.d("profile", selected_profile_String);
                      Log.d("Email", ""+email);
                      
                      dialog.dismiss();
                 }
           });   
                         
         dialog.show();       
       }
    public static void updateDB(String location_string, String profile, int dia, int email, String pkey, Context context){
        
    	ContentValues values=new ContentValues();
        values.put(LocationDB.KEY_LOCATION,location_string);
        values.put(LocationDB.KEY_LOCATION, location_string);
        values.put(LocationDB.KEY_PROFILE, profile);
        values.put(LocationDB.KEY_DIA, dia);
        values.put(LocationDB.KEY_EMAIL, email);
        String[] selection={pkey};
        
        context.getContentResolver().update(LocationDB.CONTENT_URI, values, "_id=?", selection);
        Toast.makeText(context, "Profile Updated",	2).show();
        	
   //END OF MY CODE     
        
    	
    } 
       
}
