package com.mrbanana.app.account.changepassword;

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
import android.widget.EditText;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityChangePassword extends ActivityBase {

	String mStrChangePasswordResponse = "";
	EditText mEtOldPassword, mEtNewPassword, mEtConfirmPassword;
	TextView mTvBack, mTvDone;
	Button mBtnChange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acp_layout);
		findViewByIds();
		setOnClickListeners();
		manipulateUi();
	}

	private void findViewByIds() {
		// TODO Auto-generated method stub
		mTvBack = (TextView) findViewById(R.id.acp_lTvBack);
		mTvDone = (TextView) findViewById(R.id.acp_lTvDone);

		mBtnChange = (Button) findViewById(R.id.acp_lBtnChange);

		mEtOldPassword = (EditText) findViewById(R.id.acp_lEtOldPassword);
		mEtNewPassword = (EditText) findViewById(R.id.acp_lEtNewPassword);
		mEtConfirmPassword = (EditText) findViewById(R.id.acp_lEtConfirmPassword);
	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub
		mTvBack.setOnClickListener(this);
		mTvDone.setOnClickListener(this);
		mBtnChange.setOnClickListener(this);
	}

	private void manipulateUi() {
		// TODO Auto-generated method stub
		mTvBack.setText("< Account");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		if (v == mTvBack) {
			onBackPressed();
		}
		if (v == mTvDone || v == mBtnChange) {
			// if (Validation.hasText(mEtOldPassword)
			// && Validation.hasText(mEtNewPassword)
			// && Validation.hasText(mEtConfirmPassword)) {
			// if (mEtOldPassword.getText().toString().trim()
			// .equals(AppBase.getmMuUser().getmStrPassword())) {
			// if (mEtNewPassword
			// .getText()
			// .toString()
			// .trim()
			// .equals(mEtConfirmPassword.getText().toString()
			// .trim())) {
			doOnChangePasswordClicked();
			// } else {
			// AlertBoxUtils
			// .getAlertDialogBox(this,
			// "your new password and confirm password do not match.")
			// .show();
			// }
			// } else {
			// AlertBoxUtils.getAlertDialogBox(this,
			// "Your current password is not correct").show();
			// }
			// } else {
			// AlertBoxUtils.getAlertDialogBox(this,
			// "Please fill in all the fields before Procedding.")
			// .show();
			// }
		}
	}

	private void doOnChangePasswordClicked() {
		AsyctaskChangePassword mAtcpChangepassTask = new AsyctaskChangePassword(
				this);
		mAtcpChangepassTask.execute();
	}

	class AsyctaskChangePassword extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskChangePassword(Context context) {
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

					mNvp.add(new BasicNameValuePair("old_password",
							mEtOldPassword.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("new_password",
							mEtNewPassword.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("confirm_password",
							mEtConfirmPassword.getText().toString().trim()));

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "changepassword", mNvp);

					HomerLogger.d("changepassword response ==" + mStsResponse);

					mStrChangePasswordResponse = mStsResponse;

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
					if (checkIfPasswordChangedSuccesfully(mStrChangePasswordResponse)) {
						finish();
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrChangePasswordResponse))
								.show();
						;
						// navigateToActivity(ActivityAccountActionList.class);
					} else {
						AlertBoxUtils
								.getAlertDialogBox(
										mWrContext.get(),
										AppBase.retriveMsgsfromResponse(mStrChangePasswordResponse))
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
				e.printStackTrace();
			}

		}
	}

	public Boolean checkIfPasswordChangedSuccesfully(String mStsResponse) {

		try {
			JSONObject jsonob = new JSONObject(mStrChangePasswordResponse);

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
