package com.mrbanana.app.account;

import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbanana.R;
import com.mrbanana.app.account.about.ActivityAbout;
import com.mrbanana.app.account.carddetails.ActivityCard;
import com.mrbanana.app.account.changepassword.ActivityChangePassword;
import com.mrbanana.app.account.feedback.ActivitySendFeedBack;
import com.mrbanana.app.account.profile.ActivityProfile;
import com.mrbanana.app.account.tellafrn.ActivityTellAFriend;
import com.mrbanana.app.account.triphistory.Activityhistory;
import com.mrbanana.app.registration_and_login.ActivityLogin;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityAccountActionList extends ActivityBase implements
		OnClickListener {

	RelativeLayout mRlTellAFrnWrapper, mRlCardDetailsWrapper,
			mRlTripHistoryWrapper, mRlAbout, mRlProfile, mRlFeedback,
			mRlLogout, mRlChangePassword;
	TextView mTvBack;
	public String mStrLogoutResponse = "";

	private static AlertDialog alert;

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

		mRlAbout = (RelativeLayout) findViewById(R.id.aaal_lLlAbout);
		mRlFeedback = (RelativeLayout) findViewById(R.id.aaal_lLlFeedback);
		mRlProfile = (RelativeLayout) findViewById(R.id.aaal_lLlProfile);

		mRlLogout = (RelativeLayout) findViewById(R.id.aaal_lLlLogout);

		mTvBack = (TextView) findViewById(R.id.aaal_lTvBack);

		mRlChangePassword = (RelativeLayout) findViewById(R.id.aaal_lLlChangePassword);

	}

	private void setOnClickListeners() {

		mRlTellAFrnWrapper.setOnClickListener(this);
		mRlCardDetailsWrapper.setOnClickListener(this);
		mRlTripHistoryWrapper.setOnClickListener(this);
		mRlAbout.setOnClickListener(this);
		mRlFeedback.setOnClickListener(this);
		mRlProfile.setOnClickListener(this);
		mRlLogout.setOnClickListener(this);
		mRlChangePassword.setOnClickListener(this);
		mTvBack.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v == mRlTellAFrnWrapper) {
			navigateToActivity(ActivityTellAFriend.class);
		}

		if (v == mRlCardDetailsWrapper) {
			navigateToActivity(ActivityCard.class);
		}

		if (v == mRlTripHistoryWrapper) {
			navigateToActivity(Activityhistory.class);
		}

		if (v == mRlLogout) {
			getAlertDialogBoxForLogout(this).show();
		}

		if (v == mRlAbout) {
			navigateToActivity(ActivityAbout.class);
		}
		if (v == mRlProfile) {
			navigateToActivity(ActivityProfile.class);
		}
		if (v == mRlFeedback) {
			navigateToActivity(ActivitySendFeedBack.class);
		}

		if (v == mTvBack) {
			onBackPressed();
		}

		if (v == mRlChangePassword) {
			navigateToActivity(ActivityChangePassword.class);
		}
	}

	class AsyctaskLogout extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskLogout(Context context) {
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
							+ "logout", mNvp);
					HomerLogger.d("logout response ==" + mStsResponse);
					mStrLogoutResponse = mStsResponse;

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

				} else {
					Toast.makeText(mWrContext.get(),
							"could not logout from webservice ",
							Toast.LENGTH_SHORT).show();
				}

				if (checkIfLogoutWasSuccessfull()) {

				} else {
					Toast.makeText(
							mWrContext.get(),
							"could not logout from webservice as no internet was present ",
							Toast.LENGTH_SHORT).show();
				}

				Intent intent = new Intent(mWrContext.get(),
						ActivityLogin.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("EXIT", true);
				startActivity(intent);
				finish();

				// navigateToActivity(ActivityLogin.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Boolean checkIfLogoutWasSuccessfull() {

		try {
			JSONObject jsonob = new JSONObject(mStrLogoutResponse);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public AlertDialog getAlertDialogBoxForLogout(
			ActivityAccountActionList context) {
		final AsyctaskLogout mAtfpLogouttask = new AsyctaskLogout(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Alert Box");

		// set dialog message
		builder.setMessage("Are you sure you want to Logout ?")
				.setCancelable(false)
				.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								mAtfpLogouttask.execute();

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();

							}
						});

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}

		});

		alert = builder.create();

		return alert;
	}
}
