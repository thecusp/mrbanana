package com.mrbanana.app.account.triphistory;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;
import com.mrbanana.base.ModelJob;

public class ActivityWorkHistoryListing extends ActivityBase {
	ListView mlv;

	TextView mTvBack;
	private static String mStrTradesmanType = "";
	public static final String mStrValueTradesmanTypeCleaner = "cleaner";
	public static final String mStrValueTradesmanTypePlumber = "Plumber";

	public static String getmStrTradesmanType() {
		return mStrTradesmanType;
	}

	public static void setmStrTradesmanType(String mStrTradesmanType) {
		ActivityWorkHistoryListing.mStrTradesmanType = mStrTradesmanType;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ath_list_layout);

		findViewByIds();
		setOnClickListners();
		manipulateUi();
	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		mlv = (ListView) findViewById(R.id.ath_l_lLv);

		mTvBack = (TextView) findViewById(R.id.ath_l_lTvBack);
	}

	private void setOnClickListners() {
		// TODO Auto-generated method stub
		mTvBack.setOnClickListener(this);
	}

	private void manipulateUi() {
		// TODO Auto-generated method stub

		mTvBack.setText("< Account");
		mlv.setAdapter(new ListAdapterWorkHistory(this, getApproWorkerarray(),
				this));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v.getId() == R.id.ath_l_l_orRlScreenParentLayout) {
			ModelJob job = (ModelJob) v.getTag();
			ActivityWorkHistoryDetails.setmModelJob(job);
			navigateToActivity(ActivityWorkHistoryDetails.class);
		}
		if (v == mTvBack) {
			onBackPressed();
		}
	}

	public ArrayList<ModelJob> getApproWorkerarray() {
		if (mStrTradesmanType.equals(mStrValueTradesmanTypeCleaner)) {
			return AppBase.getmArrLisOfMjJobListCleanerJobs();

		} else {
			return AppBase.getmArrLisOfMjJobListPlumberJobs();
		}
	}

	public static String getJobStatusforNo(String str) {
		int i = 32;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		switch (i) {
		case 0:
			return "Rejected";
		case 1:

			return "Accepted";
		case 2:

			return "Awaiting";
		case 3:

			return "Canceled";
		case 4:

			return "Completed";
		case 5:

			return "On The Job";
		default:
			return "";
		}

	}
}