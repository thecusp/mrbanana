package com.mrbanana.app.account.carddetails;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;
import homemade.apps.framework.homerlibs.utils.Validation;

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
import android.widget.EditText;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityCardEdit extends ActivityBase {
	TextView mTvBack, mTvDone;
	String mStrCardUpdateResposne = "";
	String mStrHasCardResposne = "";
	String mStrRemoveCardResposne = "";

	EditText mEtCardNo, mEtCvv, mEtMonth, mEtYear;
	Button mBtnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ace_layout);

		findViewByIds();
		setOnClickListenrs();
		manipulateUi();
	}

	private void findViewByIds() {
		mEtCardNo = (EditText) findViewById(R.id.ace_lEtCardNo);
		mEtMonth = (EditText) findViewById(R.id.ace_lEtExpiryMonthy);
		mEtYear = (EditText) findViewById(R.id.ace_lEtExpiryYear);
		mEtCvv = (EditText) findViewById(R.id.ace_lEtCvv);

		mTvBack = (TextView) findViewById(R.id.ace_lTvBack);
		mTvDone = (TextView) findViewById(R.id.ace_lTvDone);

		mBtnSave = (Button) findViewById(R.id.ace_lBtnSave);
	}

	private void setOnClickListenrs() {
		// TODO Auto-generated method stub
		mEtCardNo.addTextChangedListener(new FourDigitCardFormatWatcher());
		mTvBack.setOnClickListener(this);
		mTvDone.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);

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
		if (v == mTvDone || v == mBtnSave) {
			if (Validation.hasText(mEtCardNo) && Validation.hasText(mEtCvv)
					&& Validation.hasText(mEtMonth)
					&& Validation.hasText(mEtYear)) {
				if (mEtCardNo.getText().toString().trim().length() == 16) {
					if (mEtCvv.getText().toString().trim().length() == 3) {
						if (Validation.isCreditCardNumber(mEtCardNo)) {
							AsyctaskUpdateCardDetils mAtucdUpdateCardTask = new AsyctaskUpdateCardDetils(
									this);
							mAtucdUpdateCardTask.execute();
						} else {
							AlertBoxUtils
									.getAlertDialogBox(this,
											"Please make sure you have entered a valid credit card no")
									.show();
						}
					} else {
						AlertBoxUtils
								.getAlertDialogBox(this,
										"Please make sure you have entered 3 digit cvv no ")
								.show();
					}
				} else {
					AlertBoxUtils
							.getAlertDialogBox(this,
									"Please make sure you have entered 16 digit credit card no ")
							.show();
				}
			} else {
				AlertBoxUtils.getAlertDialogBox(this,
						"Please fill in all the feilds before procedding")
						.show();
			}
		}

	}

	class AsyctaskUpdateCardDetils extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskUpdateCardDetils(Context context) {
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

					mNvp.add(new BasicNameValuePair("cc_number", mEtCardNo
							.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("cc_month", mEtMonth
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("cc_year", mEtYear
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("cc_cvv", mEtCvv.getText()
							.toString().trim()));

					// AppBase.getmMuUser().setmStrFirstName(
					// mEtFirstName.getText().toString().trim());
					//
					// AppBase.getmMuUser().setmStrLastName(
					// mEtLastName.getText().toString().trim());
					//
					// AppBase.getmMuUser().setmStrEmail(
					// mEtEmail.getText().toString().trim());
					//
					// AppBase.getmMuUser().setmStrPhoneNo(
					// mEtMobileNo.getText().toString().trim());
					//
					// AppBase.getmMuUser().setmStrPassword(
					// mEtPassword.getText().toString().trim());

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "updatecreditcard", mNvp);
					HomerLogger
							.d("updatecreditcard response ==" + mStsResponse);

					mStrCardUpdateResposne = mStsResponse;

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
					if (checkIfCardUpdationWorkedSuccessfully()) {
						finish();
						navigateToActivity(ActivityCard.class);

					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrCardUpdateResposne))
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

	private Boolean checkIfCardUpdationWorkedSuccessfully() {

		try {
			JSONObject jsonob = new JSONObject(mStrCardUpdateResposne);

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
