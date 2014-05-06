package com.mrbanana.app.registration_and_login;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.app.nearby.ActivityNearby;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;
import com.mrbanana.base.ModelUser;
import com.mrbanana.base.PlacesAutoCompleteAdapter;

public class ActivityRegistration extends ActivityBase implements
		OnClickListener {

	String mStrRegisterResponse = "init";

	RelativeLayout mRlLoginBtn;

	EditText mEtFirstName, mEtLastName, mEtPassword, mEtMobileNo, mEtEmail,
			mEtAddress1, mEtAddress2;
	Button mBtnContinue;

	AutoCompleteTextView mEtLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.areg_layout);

		findViewByIds();
		setOnClickListeners();
		manipulateUi();

	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		mRlLoginBtn = (RelativeLayout) findViewById(R.id.areg_lRlLoginBtnWraapper2);

		mEtFirstName = (EditText) findViewById(R.id.areg_lEtFirstName);
		mEtLastName = (EditText) findViewById(R.id.areg_lEtLastName);
		mEtPassword = (EditText) findViewById(R.id.areg_lEtPassword);
		mEtMobileNo = (EditText) findViewById(R.id.areg_lEtMobile);
		mEtEmail = (EditText) findViewById(R.id.areg_lEtEmail);

		mEtAddress1 = (EditText) findViewById(R.id.areg_lEtAddress1);
		mEtAddress2 = (EditText) findViewById(R.id.areg_lEtAddress2);
		mEtLocation = (AutoCompleteTextView) findViewById(R.id.areg_lEtLocation);

		mBtnContinue = (Button) findViewById(R.id.areg_lBtnContinue);

	}

	private void setOnClickListeners() {

		mRlLoginBtn.setOnClickListener(this);

		mBtnContinue.setOnClickListener(this);

	}

	private void manipulateUi() {
		// TODO Auto-generated method stub
		
		
		mEtLocation.setAdapter(new PlacesAutoCompleteAdapter(this,
				R.layout.areg_location_actv_item));
		setKeyListenersToInputFeilds(mEtFirstName, mEtEmail);
		setKeyListenersToInputFeilds(mEtEmail, mEtMobileNo);
		setKeyListenersToInputFeilds(mEtMobileNo, mEtPassword);
		setKeyListenersToInputFeilds(mEtPassword, mEtAddress1);
		setKeyListenersToInputFeilds(mEtAddress1, mEtAddress2);
		setKeyListenersToInputFeilds(mEtAddress2, mEtLocation);

	}

	public void setKeyListenersToInputFeilds(final EditText mEtClearFocus,
			final EditText mEtRequestFocus) {
		mEtClearFocus.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on Enter key press
					mEtClearFocus.clearFocus();
					mEtRequestFocus.requestFocus();
					return true;
				}
				return false;
			}

		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mRlLoginBtn) {
			navigateToActivity(ActivityLogin.class);
			finish();
		}

		if (v == mBtnContinue) {
			if (Utils.textHasBeenEnteredInTextBox(mEtEmail)
					&& Utils.textHasBeenEnteredInTextBox(mEtMobileNo)
					&& Utils.textHasBeenEnteredInTextBox(mEtPassword)
					&& Utils.textHasBeenEnteredInTextBox(mEtFirstName)
					&& Utils.textHasBeenEnteredInTextBox(mEtLastName)
					&& Utils.textHasBeenEnteredInTextBox(mEtAddress1)
					&& Utils.textHasBeenEnteredInTextBox(mEtLocation)) {
				if (Validation.isEmailAddress(mEtEmail, true)) {

					if (Validation.hasExactlySoManyCharecters(mEtMobileNo, 10)) {

						if (Validation.hasAtleastSoManyCharecters(mEtPassword,
								3)
								&& Validation.hasAtTheMaxSoManyCharecters(
										mEtPassword, 36)) {

							AsyctaskRegister mAtrRegistertask = new AsyctaskRegister(
									this);
							mAtrRegistertask.execute();

						} else {
							AlertBoxUtils
									.getAlertDialogBox(this,
											"Your password should be between 6 to 36 charecters.")
									.show();
						}
					} else {
						AlertBoxUtils.getAlertDialogBox(this,
								"Please enter a valid phone no").show();
					}

				} else {
					AlertBoxUtils.getAlertDialogBox(this,
							"Please enter a valid email address").show();
				}
			} else {
				AlertBoxUtils.getAlertDialogBox(this,
						"Please fill in all the fields  before proceeding")
						.show();
			}
		}

	}

	class AsyctaskRegister extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskRegister(Context context) {
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
					mNvp.add(new BasicNameValuePair("first_name", mEtFirstName
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("last_name", mEtLastName
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("user_email", mEtEmail
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("user_password",
							mEtPassword.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("mobile_number",
							mEtMobileNo.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("address1", mEtAddress1
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("address2", mEtAddress2
							.getText().toString().trim()));
					mNvp.add(new BasicNameValuePair("user_location",
							mEtLocation.getText().toString().trim()));

					AppBase.getmMuUser().setmStrFirstName(
							mEtFirstName.getText().toString().trim());

					AppBase.getmMuUser().setmStrLastName(
							mEtLastName.getText().toString().trim());

					AppBase.getmMuUser().setmStrEmail(
							mEtEmail.getText().toString().trim());

					AppBase.getmMuUser().setmStrPhoneNo(
							mEtMobileNo.getText().toString().trim());

					AppBase.getmMuUser().setmStrPassword(
							mEtPassword.getText().toString().trim());

					AppBase.getmMuUser().setmStrAddress1(
							mEtAddress1.getText().toString().trim());
					AppBase.getmMuUser().setmStrAddress2(
							mEtAddress2.getText().toString().trim());
					AppBase.getmMuUser().setmStrLocation(
							mEtLocation.getText().toString().trim());

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "register", mNvp);
					HomerLogger.d("register response ==" + mStsResponse);

					mStrRegisterResponse = mStsResponse;

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
					if (checkIfRegisterationWasSuccess()) {
						ActivityVerification
								.setmBoolLoginCredentialsAvail(false);
						navigateToActivity(ActivityVerification.class);

					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrRegisterResponse))
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

	private Boolean checkIfRegisterationWasSuccess() {

		try {
			JSONObject jsonob = new JSONObject(mStrRegisterResponse);

			if (jsonob.has("SUCCESS")) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public void onBackPressed() {

	}
}
