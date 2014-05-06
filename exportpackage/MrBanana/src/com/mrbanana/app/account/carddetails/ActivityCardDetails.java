package com.mrbanana.app.account.carddetails;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.app.account.ActivityAccountActionList;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityCardDetails extends ActivityBase {
	TextView mTvBack, mTvDone;

	String mStrRemoveCardResposne = "";

	TextView mTvCardNo, mTvCvv, mTvMonth, mTvYear;
	Button mBtnRemove, mBtnEdit;

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
		mTvCardNo = (TextView) findViewById(R.id.acd_lEtCardNo);
		mTvMonth = (TextView) findViewById(R.id.acd_lEtExpiryMonthy);
		mTvYear = (TextView) findViewById(R.id.acd_lEtExpiryYear);
		mTvCvv = (TextView) findViewById(R.id.acd_lEtCvv);

		mTvBack = (TextView) findViewById(R.id.acd_lTvBack);
		mTvDone = (TextView) findViewById(R.id.acd_lTvEdit);

		mBtnRemove = (Button) findViewById(R.id.acd_lBtnRemove);
		mBtnEdit = (Button) findViewById(R.id.acd_lBtnEdit);
	}

	private void setOnClickListenrs() {
		// TODO Auto-generated method stub
		mTvCardNo.addTextChangedListener(new FourDigitCardFormatWatcher());
		mTvBack.setOnClickListener(this);
		mTvDone.setOnClickListener(this);
		mBtnRemove.setOnClickListener(this);
		mBtnEdit.setOnClickListener(this);

	}

	private void manipulateUi() {
		mTvBack.setText("< Accounts");
		loadUi();
	}

	private void loadUi() {
		loadCreditCardNo();
		loadCvvNo();
		loadExpiryMonth();
		loadExpiryYear();
	}

	private void loadCreditCardNo() {

		String mStrStarts = "**** **** **** ";
		String mStrLast4Digits = AppBase
				.getmMuUser()
				.getmStrCreditCardNo()
				.substring(
						AppBase.getmMuUser().getmStrCreditCardNo().length() - 4);

		mTvCardNo.setText(mStrStarts + mStrLast4Digits);

	}

	private void loadCvvNo() {

		String mStrStarts = "**";
		String mStrLast1Digits = AppBase
				.getmMuUser()
				.getmStrCreditCardCvv()
				.substring(
						AppBase.getmMuUser().getmStrCreditCardCvv().length() - 1,
						AppBase.getmMuUser().getmStrCreditCardCvv().length());

		mTvCvv.setText(mStrStarts + mStrLast1Digits);

	}

	private void loadExpiryMonth() {

		mTvMonth.setText(AppBase.getmMuUser().getmStrCreditCardExpiryMonth());

	}

	private void loadExpiryYear() {

		mTvYear.setText(AppBase.getmMuUser().getmStrCreditCardExpiryYear());

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		if (v == mTvBack) {
			onBackPressed();
		}
		if (v == mTvDone || v == mBtnEdit) {
			navigateToActivity(ActivityCardEdit.class);
		}
		if (v == mBtnRemove) {
			doOnRemoveCardButtonClicked();
		}

	}

	private void doOnRemoveCardButtonClicked() {
		AsyctaskRemoveCard mAtRcRemoveCardTask = new AsyctaskRemoveCard(this);
		mAtRcRemoveCardTask.execute();
	}

	class AsyctaskRemoveCard extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskRemoveCard(Context context) {
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
							+ "removecreditcard", mNvp);
					HomerLogger
							.d("removecreditcard response ==" + mStsResponse);

					mStrRemoveCardResposne = mStsResponse;

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
					if (checkIfRemovedCardWorkedSuccessfully()) {
						finish();
						navigateToActivity(ActivityCard.class);

					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrRemoveCardResposne))
								.show();
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

	private Boolean checkIfRemovedCardWorkedSuccessfully() {

		try {
			JSONObject jsonob = new JSONObject(mStrRemoveCardResposne);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

}
