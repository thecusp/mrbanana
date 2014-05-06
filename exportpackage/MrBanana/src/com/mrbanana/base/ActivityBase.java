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
		restoreLoginDataIfNeeded();
	}

	@Override
	protected void onResume() {
		super.onResume();
		restoreLoginDataIfNeeded();
	}

	public void restoreLoginDataIfNeeded() {
		if (AppBase.getmMuUser() == null) {
			AppBase.setmMuUser(new ModelUser(this));
		}
		if (AppBase.getmMuUser().getmStrLoginSucces()
				.equals(ModelUser.mStrValueLoginSuccessSuccess)) {

		} else {
			AppBase.setmMuUser(new ModelUser(this));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
