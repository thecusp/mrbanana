package com.mrbanana.app.account.about;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;

public class ActivityAbout extends ActivityBase {
	TextView mTvAboutText, mTvBack;
	String mStrAbouttext = " Mr. Banana is a technology firm based in London. Consumers can get a trusted plumber or cleaner using the Mr. Banana mobile application with 2 clicks on their smartphone. We are not only solving a consumer problem but also increasing the efficiency of cleaners & plumbers.We are a team of 3 passionate entrepreneurs, a technology expert and a professional plumber.Mr. Banana is connecting you with a reliable plumber or cleaner in London with just 2 taps on your Smartphone.Dedicated to providing you with a trusted plumber or a cleaner within a few seconds.Mr. Banana wants to reduce the hassle of finding and booking a cleaner or tradesmen. We are digitizing your search by bringing the technology to your fingertips.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.aap_layout);
		findViewByIds();
		setOnClickListeners();
		manipulateUi();
	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		mTvAboutText = (TextView) findViewById(R.id.aap_lTvAboutText);
		mTvBack = (TextView) findViewById(R.id.aap_lTvBack);

	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub
		mTvBack.setOnClickListener(this);
	}

	private void manipulateUi() {
		// TODO Auto-generated method stub
		mTvAboutText.setText(mStrAbouttext);
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
