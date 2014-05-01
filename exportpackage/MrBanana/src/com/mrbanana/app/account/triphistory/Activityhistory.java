package com.mrbanana.app.account.triphistory;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;

public class Activityhistory extends ActivityBase {

	TextView mTvBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ath_layout);
		findViewByIds();
		setOnClickListenrs();

		manipulateUi();
	}

	private void findViewByIds() {
		mTvBack = (TextView) findViewById(R.id.ath_lTvBack);

	}

	private void setOnClickListenrs() {

		mTvBack.setOnClickListener(this);

	}

	private void manipulateUi() {
		mTvBack.setText("< Accounts");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (v == mTvBack) {
			onBackPressed();
		}

	}
}
