package com.mrbanana.app;

import com.mrbanana.R;
import com.mrbanana.R.id;
import com.mrbanana.R.layout;
import com.mrbanana.R.menu;
import com.mrbanana.app.clienttour.ActivityClientTour;
import com.mrbanana.app.registration_and_login.ActivityRegistration;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Skip Tour If it has been Already Shown
		
//		if(AppBase.getmBoolTourHasBeenShown(this))
//			navigateToActivity(ActivityRegistration.class);
//		else
			navigateToActivity(ActivityClientTour.class);
		
		finish();
			
	}

}
