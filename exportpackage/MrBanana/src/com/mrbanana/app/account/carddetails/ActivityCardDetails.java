package com.mrbanana.app.account.carddetails;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;

public class ActivityCardDetails extends ActivityBase {
	TextView mTvBack, mTvDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acd_layout);

		findViewByIds();
		setOnClickListenrs();
		manipulateUi();
	}

	private void findViewByIds() {
		mTvBack = (TextView) findViewById(R.id.acd_lTvBack);
		mTvDone = (TextView) findViewById(R.id.acd_lTvDone);
	}

	private void setOnClickListenrs() {
		// TODO Auto-generated method stub
		mTvBack.setOnClickListener(this);
		mTvDone.setOnClickListener(this);

	}

	private void manipulateUi() {
		mTvBack.setText("< Accounts");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (v == mTvBack || v == mTvDone) {
			onBackPressed();
		}

	}

}
