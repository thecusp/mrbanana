package com.mrbanana.app.account.triphistory;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.ModelJob;

public class ActivityWorkHistoryDetails extends ActivityBase {

	TextView mTvBack, mTvFirstName, mTvLastName, mTvTradesmanType,
			mTvTradesmanLevel, mTvJobStatus;

	private static ModelJob mModelJob = new ModelJob();

	public static ModelJob getmModelJob() {
		return mModelJob;
	}

	public static void setmModelJob(ModelJob mModelJob) {
		ActivityWorkHistoryDetails.mModelJob = mModelJob;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ath_details_layout);

		findViewByIds();
		setOnClickListeners();
		manipulateUi();

	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		try {
			mTvBack = (TextView) findViewById(R.id.ath_d_lTvBack);

			mTvFirstName = (TextView) findViewById(R.id.ath_d_lTvFirstName);
			mTvLastName = (TextView) findViewById(R.id.ath_d_lTvLastName);
			mTvTradesmanLevel = (TextView) findViewById(R.id.ath_d_lTvTradesmanLevel);
			mTvTradesmanType = (TextView) findViewById(R.id.ath_d_lTvTradesmanType);
			mTvJobStatus = (TextView) findViewById(R.id.ath_d_lTvJobStatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub
		mTvBack.setOnClickListener(this);
	}

	private void manipulateUi() {

		mTvBack.setText("< Listing");
		try {
			mTvFirstName.setText("First Name :" + mModelJob.getmStrFirstName());
			mTvLastName.setText("Last Name :" + mModelJob.getmStrLastName());
			mTvTradesmanLevel.setText("Tradesman Level :"
					+ mModelJob.getmStrTradesmanLevel());
			mTvTradesmanType.setText("Tradesman Level :"
					+ mModelJob.getmStrTradesmanType());
			mTvJobStatus.setText("Job Status :"
					+ ActivityWorkHistoryListing.getJobStatusforNo(mModelJob
							.getmStrJobStatus()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v == mTvBack) {
			onBackPressed();
		}
	}
}
