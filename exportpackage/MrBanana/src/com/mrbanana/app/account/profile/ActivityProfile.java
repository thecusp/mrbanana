package com.mrbanana.app.account.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;

public class ActivityProfile extends ActivityBase {

	EditText mEtFirstName, mEtLastName, mEtPhone, mEtAddress1, mEtAddress2,
			mEtPostCode, mEtLocation;
	RelativeLayout mRlGenderWrapper;
	TextView mTvGenderValue, mTvBack, mTvDone;

	ImageView mIvProfilePic;
	ProgressBar mPbProfilePicProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ap_layout);

		findViewByIds();
		setOnClickListeners();
		manipulateUi();
	}

	private void findViewByIds() {
		// TODO Auto-generated method stub

		mEtFirstName = (EditText) findViewById(R.id.ap_lEtFirstName);
		mEtLastName = (EditText) findViewById(R.id.ap_lEtLastName);
		mEtPhone = (EditText) findViewById(R.id.ap_lEtMobile);
		mEtAddress1 = (EditText) findViewById(R.id.ap_lEtAddress1);
		mEtAddress2 = (EditText) findViewById(R.id.ap_lEtAddress2);
		mEtLocation = (EditText) findViewById(R.id.ap_lEtLocation);
		mEtPostCode = (EditText) findViewById(R.id.ap_lEtPostcode);

	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub

	}

	private void manipulateUi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

	}
}
