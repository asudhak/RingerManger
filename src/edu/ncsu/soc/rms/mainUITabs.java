package edu.ncsu.soc.rms;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;


public class mainUITabs extends TabActivity {
	public static TabHost mTabHost;
	

	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, RingerManagerActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("Main").setIndicator("Main",
	                      res.getDrawable(R.drawable.tab1))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, LocationRepresenter.class);
	    spec = tabHost.newTabSpec("Location").setIndicator("Location",
	                      res.getDrawable(R.drawable.tab2)).setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, MyProfiles.class);
	    spec = tabHost.newTabSpec("MyProfiles").setIndicator("My Profiles",
	                      res.getDrawable(R.drawable.tab3)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    mTabHost=getTabHost();
	    
	    getTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
			if(tabId.compareTo(("Location"))==0)
			{
				
			}
			Log.d("TAB", tabId);
			}
	    	
	    });
//	    tabHost.setCurrentTab(1);
	}
}
