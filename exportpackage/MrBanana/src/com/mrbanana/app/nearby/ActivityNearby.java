package com.mrbanana.app.nearby;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.app.account.ActivityAccountActionList;
import com.mrbanana.base.ActivityBase;

public class ActivityNearby extends ActivityBase implements OnClickListener {
	TextView mTvProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.anb_layout);

		findViewByIds();
		setOnClickListeners();

	}

	private void findViewByIds() {

		mTvProfile = (TextView) findViewById(R.id.anb_TvProfile);

	}

	private void setOnClickListeners() {
		mTvProfile.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == mTvProfile) {
			navigateToActivity(ActivityAccountActionList.class);
		}
	}

}
