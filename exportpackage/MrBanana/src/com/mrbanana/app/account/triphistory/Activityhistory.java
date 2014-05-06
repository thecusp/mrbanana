package com.mrbanana.app.account.triphistory;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;
import com.mrbanana.base.ModelJob;

public class Activityhistory extends ActivityBase {

	TextView mTvBack, mTvCleanerCount, mTvPlumberCount;
	RelativeLayout mRlCleaningWrapper, mRlPlumbingWrapper;

	String mStrWorkHistoryResponse = "";

	String mStrCleaningCount = "";

	String mStrPlumbingCount = "";

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
		mTvCleanerCount = (TextView) findViewById(R.id.ath_lTvCleaningJobValue);
		mTvPlumberCount = (TextView) findViewById(R.id.ath_lTvPlumbingJobValue);
		mRlCleaningWrapper = (RelativeLayout) findViewById(R.id.ath_lLlCleaningJobWrapper);
		mRlPlumbingWrapper = (RelativeLayout) findViewById(R.id.ath_lLlPlumbingJobWrapper);
	}

	private void setOnClickListenrs() {

		mTvBack.setOnClickListener(this);
		mRlCleaningWrapper.setOnClickListener(this);
		mRlPlumbingWrapper.setOnClickListener(this);

	}

	private void manipulateUi() {
		mTvBack.setText("< Accounts");

		AsyctaskWorkHistory mAtwhWorkHistoryTask = new AsyctaskWorkHistory(this);
		mAtwhWorkHistoryTask.execute();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (v == mTvBack) {
			onBackPressed();
		}

		if (v == mRlPlumbingWrapper) {

			ActivityWorkHistoryListing
					.setmStrTradesmanType(ActivityWorkHistoryListing.mStrValueTradesmanTypePlumber);
			navigateToActivity(ActivityWorkHistoryListing.class);
		}
		if (v == mRlCleaningWrapper) {

			ActivityWorkHistoryListing
					.setmStrTradesmanType(ActivityWorkHistoryListing.mStrValueTradesmanTypeCleaner);
			navigateToActivity(ActivityWorkHistoryListing.class);
		}

	}

	class AsyctaskWorkHistory extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskWorkHistory(Context context) {
			mWrContext = new WeakReference<Context>(context);
			mPdDialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {

			mPdDialog = ProgressDialog.show(mWrContext.get(), "Alert",
					"Loading please wait..");
			mPdDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				if (NetworkApis.isOnline()) {
					mBoolWasInternetPresentDuringDoInBackground = true;
					List<NameValuePair> mNvp = new ArrayList<NameValuePair>();
					mNvp.add(new BasicNameValuePair("user_id", AppBase
							.getmMuUser().getmStrId()));

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "workhistory", mNvp);
					HomerLogger.d("workhistory response ==" + mStsResponse);
					mStrWorkHistoryResponse = mStsResponse;

				} else {
					mBoolWasInternetPresentDuringDoInBackground = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void notUsed) {
			try {

				mPdDialog.dismiss();

				if (mBoolWasInternetPresentDuringDoInBackground) {
					if (checkIfWorkHistoryFethchedSuccessfully()) {

						parseWorkHistoryResponse();

					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrWorkHistoryResponse))
								.show();
						;
					}
				} else {
					AlertBoxUtils
							.getAlertDialogBox(mWrContext.get(),
									"Please check your internet connection and try again. ")
							.show();
					;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Boolean checkIfWorkHistoryFethchedSuccessfully() {

		try {
			JSONObject jsonob = new JSONObject(mStrWorkHistoryResponse);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public void parseWorkHistoryResponse() {

		try {

			JSONObject jsonSuccess = new JSONObject(mStrWorkHistoryResponse);

			JSONObject json = jsonSuccess.getJSONObject("SUCCESS");
			mStrCleaningCount = json.getString("cleaning_count");
			mStrPlumbingCount = json.getString(ModelJob.mStrKeyPlumbingCount);

			JSONArray jsonarrPlumbers = json
					.getJSONArray(ModelJob.mStrKeyPlumbingList);
			JSONArray jsonarrCleaners = json
					.getJSONArray(ModelJob.mStrKeyCleaningList);

			AppBase.setmArrLisOfMjJobListCleanerJobs(new ArrayList<ModelJob>());
			AppBase.setmArrLisOfMjJobListPlumberJobs(new ArrayList<ModelJob>());
			for (int i = 0; i < jsonarrPlumbers.length(); i++) {
				ModelJob job = new ModelJob();
				job.fromJsonOb(jsonarrPlumbers.getJSONObject(i));

				AppBase.getmArrLisOfMjJobListPlumberJobs().add(job);
			}

			for (int j = 0; j < jsonarrPlumbers.length(); j++) {
				ModelJob job = new ModelJob();
				job.fromJsonOb(jsonarrPlumbers.getJSONObject(j));

				AppBase.getmArrLisOfMjJobListCleanerJobs().add(job);
			}
			loadUi();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadUi() {

		mTvCleanerCount.setText(mStrCleaningCount);
		mTvPlumberCount.setText(mStrPlumbingCount);
	}
}
