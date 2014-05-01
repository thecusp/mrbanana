package com.mrbanana.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import homemade.apps.framework.homerlibs.activities.ActionBarActivityHomerBase;

public class ActivityBase extends ActionBarActivityHomerBase implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
