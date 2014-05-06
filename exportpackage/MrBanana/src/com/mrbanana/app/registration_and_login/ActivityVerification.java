package com.mrbanana.app.registration_and_login;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;
import homemade.apps.framework.homerlibs.utils.Validation;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.app.nearby.ActivityNearby;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityVerification extends ActivityBase implements
		OnClickListener {

	TextView mTvBack, mTvSmsSentTo, mTvResendSms;
	EditText mEtCode;
	Button mBtnContinue, mBtnSkip;
	String mStrSmsActivationResponse = "";
	String mStrResendSmsResponse = "";

	private static boolean mBoolLoginCredentialsAvail = true;

	public static boolean ismBoolLoginCredentialsAvail() {
		return mBoolLoginCredentialsAvail;
	}

	public static void setmBoolLoginCredentialsAvail(
			boolean mBoolLoginCredentialsAvail) {
		ActivityVerification.mBoolLoginCredentialsAvail = mBoolLoginCredentialsAvail;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.av_layout);

		findViewByIds();
		setOnClickListeners();
		manipulateUi();

	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		mTvBack = (TextView) findViewById(R.id.av_lTvBack);
		mTvSmsSentTo = (TextView) findViewById(R.id.av_lTvSmsSentToText);
		mTvResendSms = (TextView) findViewById(R.id.av_lTvResendSms);

		mEtCode = (EditText) findViewById(R.id.av_lEtCode);

		mBtnContinue = (Button) findViewById(R.id.av_lBtnContinue);

		mBtnSkip = (Button) findViewById(R.id.av_lBtnSkip);

	}

	private void setOnClickListeners() {
		try {
			mBtnContinue.setOnClickListener(this);
			mBtnSkip.setOnClickListener(this);
			mTvBack.setOnClickListener(this);

			mTvResendSms.setOnClickListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void manipulateUi() {
		// TODO Auto-generated method stub
		mTvBack.setText("< Back");
		mTvSmsSentTo.setText("SMS sent to "
				+ AppBase.getmMuUser().getmStrPhoneNo());

	}

	@Override
	public void onClick(View v) {
		if (v == mTvBack) {
			onBackPressed();
		}
		if (v == mBtnContinue) {
			// if (Validation.hasExactlySoManyCharecters(mEtCode, 5)) {

			AsyctaskSmsActivation mAtsaSmsActivateTask = new AsyctaskSmsActivation(
					this);
			mAtsaSmsActivateTask.execute();

			// } else {
			// AlertBoxUtils.getAlertDialogBox(this,
			// "Enter a valid verification code").show();
			//
			// }
		}
		if (v == mTvResendSms) {
			if (AppBase.getmBoolCanResendSms()) {
				AsyctaskResendSms mAtrsResendSmsTask = new AsyctaskResendSms(
						this);
				mAtrsResendSmsTask.execute();
			} else {
				AlertBoxUtils.getAlertDialogBox(this,
						"Wait 3 minutes before resending the sms").show();

			}
		}
		if (v == mBtnSkip) {
			navigateToActivity(ActivityNearby.class);
		}

	}

	class AsyctaskSmsActivation extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskSmsActivation(Context context) {
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
					mNvp.add(new BasicNameValuePair("sms_code", mEtCode
							.getText().toString().trim()));

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "smsactivation", mNvp);
					HomerLogger.d("smsactivation response ==" + mStsResponse);
					mStrSmsActivationResponse = mStsResponse;

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
					if (checkIfSmsactivationWorkedSuccessfully()) {

						if (mBoolLoginCredentialsAvail) {
							navigateToActivity(ActivityNearby.class);
						} else {
							navigateToActivity(ActivityLogin.class);
						}

					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrSmsActivationResponse))
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

	private Boolean checkIfSmsactivationWorkedSuccessfully() {

		try {
			JSONObject jsonob = new JSONObject(mStrSmsActivationResponse);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	class AsyctaskResendSms extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskResendSms(Context context) {
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
					mNvp.add(new BasicNameValuePair("user_email", AppBase
							.getmMuUser().getmStrEmail()));

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "resendsms", mNvp);
					HomerLogger.d("resendsms response ==" + mStsResponse);
					mStrResendSmsResponse = mStsResponse;

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
					if (checkIfResendSmsWorkedSuccessfully()) {

						AlertBoxUtils.getAlertDialogBox(mWrContext.get(),
								"Sms has been resent successfully. ").show();
						AppBase.setmBoolCanResendSmsValueToFalse();

					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrResendSmsResponse))
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

	private Boolean checkIfResendSmsWorkedSuccessfully() {

		try {
			JSONObject jsonob = new JSONObject(mStrResendSmsResponse);

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
