package com.mrbanana.app.account;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.app.account.carddetails.ActivityCardDetails;
import com.mrbanana.app.account.tellafrn.ActivityTellAFriend;
import com.mrbanana.app.account.triphistory.Activityhistory;
import com.mrbanana.base.ActivityBase;

public class ActivityAccountActionList extends ActivityBase implements
		OnClickListener {

	RelativeLayout mRlTellAFrnWrapper, mRlCardDetailsWrapper,
			mRlTripHistoryWrapper;
	TextView mTvBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aaal_layout);

		findViewByIds();
		setOnClickListeners();

	}

	private void findViewByIds() {

		mRlTellAFrnWrapper = (RelativeLayout) findViewById(R.id.aaal_lLlTellFriends);
		mRlCardDetailsWrapper = (RelativeLayout) findViewById(R.id.aaal_lLlCards);
		mRlTripHistoryWrapper = (RelativeLayout) findViewById(R.id.aaal_lLlTipHistory);

		mTvBack = (TextView) findViewById(R.id.aaal_lTvBack);
		
	}

	private void setOnClickListeners() {

		mRlTellAFrnWrapper.setOnClickListener(this);
		mRlCardDetailsWrapper.setOnClickListener(this);
		mRlTripHistoryWrapper.setOnClickListener(this);

		mTvBack.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v == mRlTellAFrnWrapper) {
			navigateToActivity(ActivityTellAFriend.class);
		}

		if (v == mRlCardDetailsWrapper) {
			navigateToActivity(ActivityCardDetails.class);
		}

		if (v == mRlTripHistoryWrapper) {
			navigateToActivity(Activityhistory.class);
		}

		if (v == mTvBack) {
			onBackPressed();
		}
	}

}
