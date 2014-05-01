package com.mrbanana.app.registration_and_login;

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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.mrbanana.base.ModelUser;

public class ActivityLogin extends ActivityBase implements OnClickListener {

	TextView mTvNewUser, mTvForgotPassword;
	EditText mEtUsername, mEtPassword;
	Button mBtnContinue;

	String mStrForgotPasswordresponse = "init";
	String mStrEmailForForgotPassword = "";
	private static AlertDialog alert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ali_layout);

		findViewByIds();
		setOnClickListeners();

	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		mTvNewUser = (TextView) findViewById(R.id.ali_lTvNewUser);
		mTvForgotPassword = (TextView) findViewById(R.id.ali_lTvForgotPassword);

		mEtUsername = (EditText) findViewById(R.id.ali_lEtName);
		mEtPassword = (EditText) findViewById(R.id.ali_lEtPassword);

		mBtnContinue = (Button) findViewById(R.id.ali_lBtnContinue);

	}

	private void setOnClickListeners() {
		mTvNewUser.setOnClickListener(this);
		mTvForgotPassword.setOnClickListener(this);
		mBtnContinue.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mTvNewUser) {
			navigateToActivity(ActivityRegistration.class);
			finish();
		}
		if (v == mTvForgotPassword) {
			getAlertDialogBoxForCommentBoxDefault(this).show();
		}
		if (v == mBtnContinue) {
			if (Utils.textHasBeenEnteredInTextBox(mEtPassword)
					&& Utils.textHasBeenEnteredInTextBox(mEtUsername)) {
				if (Validation.isEmailAddress(mEtUsername, true)) {

					if (Validation.hasAtleastSoManyCharecters(mEtPassword, 3)
							&& Validation.hasAtTheMaxSoManyCharecters(
									mEtPassword, 36)) {
						AsyctaskLogin mAtlLogintask = new AsyctaskLogin(this);
						mAtlLogintask.execute();

					} else {
						AlertBoxUtils
								.getAlertDialogBox(this,
										"Your password should be between 3 to 36 charecters.")
								.show();
					}
				} else {
					AlertBoxUtils.getAlertDialogBox(this,
							"Please enter a valid email address.").show();
				}
			} else {
				AlertBoxUtils.getAlertDialogBox(this,
						"Please fill in all the fields  before proceeding.")
						.show();
			}
		}

	}

	class AsyctaskLogin extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskLogin(Context context) {
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
					mNvp.add(new BasicNameValuePair("user_email", mEtUsername
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("user_password",
							mEtPassword.getText().toString().trim()));
					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "login", mNvp);

					HomerLogger.d("login response ==" + mStsResponse);
					HomerLogger.d("user_email =="
							+ mEtUsername.getText().toString().trim());

					HomerLogger.d("user_password =="
							+ mEtPassword.getText().toString().trim());

					parseLoginResponse(mStsResponse);

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
					if (AppBase.getmMuUser().getmStrLoginSucces()
							.equals(ModelUser.mStrValueLoginSuccessSuccess)) {
						if (AppBase.getmMuUser().getmStrStatus().trim()
								.equals("1")) {
							navigateToActivity(ActivityNearby.class);
						} else {
							ActivityVerification
									.setmBoolLoginCredentialsAvail(true);
							navigateToActivity(ActivityVerification.class);
						}
					} else {
						AlertBoxUtils
								.getAlertDialogBox(mWrContext.get(),
										"Login Was not Succesfull .Please check your credentials and Try Again ")
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

	private void parseLoginResponse(String mStrResponse) {
		AppBase.getmMuUser().setmStrLoginResponse(mStrResponse);

		AppBase.getmMuUser().fromString(
				AppBase.getmMuUser().getmStrLoginResponse());
	}

	class AsyctaskForgotPassword extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskForgotPassword(Context context) {
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
					mNvp.add(new BasicNameValuePair("user_email",
							mStrEmailForForgotPassword));

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "forgotpassword", mNvp);
					HomerLogger.d("forgotpassword response ==" + mStsResponse);
					mStrForgotPasswordresponse = mStsResponse;

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
					if (checkIfForgotPasswordWorkedSuccessfully()) {

						AlertBoxUtils.getAlertDialogBox(
								mWrContext.get(),
								"An email has been sent to your id "
										+ mStrEmailForForgotPassword).show();
						;

					} else {
						AlertBoxUtils.getAlertDialogBox(mWrContext.get(),
								mStrForgotPasswordresponse).show();
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

	private Boolean checkIfForgotPasswordWorkedSuccessfully() {

		try {
			JSONObject jsonob = new JSONObject(mStrForgotPasswordresponse);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public AlertDialog getAlertDialogBoxForCommentBoxDefault(
			ActivityLogin context) {
		final AsyctaskForgotPassword mAtfpForgotpasswordtask = new AsyctaskForgotPassword(
				this);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Comment Box");
		final EditText et = new EditText(context);
		et.setHint("Enter the email for the account whoes passwrd you have forgotten ");

		builder.setView(et)
				.setPositiveButton("Submit",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(final DialogInterface dialog,
									int which) {
								if (Validation.hasText(et)) {

									if (Validation.isEmailAddress(et, true))
										mStrEmailForForgotPassword = et
												.getText().toString().trim();
									mAtfpForgotpasswordtask.execute();

								} else {
									et.setHint("Please enter a valid email addres. ");
								}

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
