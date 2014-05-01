package com.mrbanana.app.account.tellafrn;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;

public class ActivityTellAFriend extends ActivityBase implements
		OnClickListener {

	RelativeLayout mRlEmailWrapper, mRlSmsWrapper, mRlTwitter;
	TextView mTvBack;
	String mStrMssageToBeShared = "Check out Mr Banana the android and ios app that lets you call a plumber or cleaner in just 2 taps,pay by cash or card.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ataf_layout);

		findViewByIds();
		setOnClickListenrs();

		manipulateUi();
	}

	private void findViewByIds() {

		mRlEmailWrapper = (RelativeLayout) findViewById(R.id.ataf_lLlEmail);
		mRlSmsWrapper = (RelativeLayout) findViewById(R.id.ataf_lLlSMS);
		mRlTwitter = (RelativeLayout) findViewById(R.id.ataf_lLlTwitter);

		mTvBack = (TextView) findViewById(R.id.ataf_lTvBack);

	}

	private void setOnClickListenrs() {
		mRlEmailWrapper.setOnClickListener(this);
		mRlSmsWrapper.setOnClickListener(this);
		mRlTwitter.setOnClickListener(this);

		mTvBack.setOnClickListener(this);
	}

	private void manipulateUi() {

		mTvBack.setText("< Account ");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v == mRlEmailWrapper) {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "recipient@example.com" });
			i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
			i.putExtra(Intent.EXTRA_TEXT, mStrMssageToBeShared);
			try {
				startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this, "There are no email clients installed.",
						Toast.LENGTH_SHORT).show();
			}
		}

		if (v == mRlSmsWrapper) {

			Intent smsIntent = new Intent(Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address", "12125551212");
			smsIntent.putExtra("sms_body", mStrMssageToBeShared);
			startActivity(smsIntent);

		}

		if (v == mRlTwitter) {
			AlertBoxUtils.getAlertDialogBox(this, "coming soon").show();
		}

		if (v == mTvBack) {
			onBackPressed();
		}
	}
}