package com.mrbanana.app.clienttour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mrbanana.R;
import com.mrbanana.app.registration_and_login.ActivityLogin;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityClientTour extends ActivityBase implements OnClickListener {

	ImageView mIvCancel, mIVDotScrollBarDot1, mIVDotScrollBarDot2,
			mIVDotScrollBarDot3, mIVDotScrollBarDot4, mIVDotScrollBarDot5,
			mIVDotScrollBarDot6, mIVDotScrollBarDot7;

	ViewPager mVp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_layout);

		findViewByIds();

		setOnClickListeners();

		manipulateUi();

	}

	private void findViewByIds() {

		mIvCancel = (ImageView) findViewById(R.id.act_lIvCancel);

		mIVDotScrollBarDot1 = (ImageView) findViewById(R.id.act_lIvDot1);
		mIVDotScrollBarDot2 = (ImageView) findViewById(R.id.act_lIvDot2);
		mIVDotScrollBarDot3 = (ImageView) findViewById(R.id.act_lIvDot3);
		mIVDotScrollBarDot4 = (ImageView) findViewById(R.id.act_lIvDot4);
		mIVDotScrollBarDot5 = (ImageView) findViewById(R.id.act_lIvDot5);
		mIVDotScrollBarDot6 = (ImageView) findViewById(R.id.act_lIvDot6);
		mIVDotScrollBarDot7 = (ImageView) findViewById(R.id.act_lIvDot7);

		mVp = (ViewPager) findViewById(R.id.act_lVpContent);
	}

	private void setOnClickListeners() {

		mIvCancel.setOnClickListener(this);

		mIVDotScrollBarDot1.setOnClickListener(this);
		mIVDotScrollBarDot2.setOnClickListener(this);
		mIVDotScrollBarDot3.setOnClickListener(this);
		mIVDotScrollBarDot4.setOnClickListener(this);
		mIVDotScrollBarDot5.setOnClickListener(this);
		mIVDotScrollBarDot6.setOnClickListener(this);
		mIVDotScrollBarDot7.setOnClickListener(this);
	}

	private void manipulateUi() {

		setUpViewPager();
		setDotScrollBarSelection(0);

	}

	private void setUpViewPager() {
		mVp.setAdapter(new PagerAdapterClientTour(getSupportFragmentManager()));
		mVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				setDotScrollBarSelection(pos);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void setDotScrollBarSelection(int mIntSelectedDotNumber) {

		try {
			switch (mIntSelectedDotNumber) {
			case 0:
				mIVDotScrollBarDot1.setSelected(true);
				mIVDotScrollBarDot2.setSelected(false);
				mIVDotScrollBarDot3.setSelected(false);
				mIVDotScrollBarDot4.setSelected(false);
				mIVDotScrollBarDot5.setSelected(false);
				mIVDotScrollBarDot6.setSelected(false);
				mIVDotScrollBarDot7.setSelected(false);

				break;
			case 1:

				mIVDotScrollBarDot1.setSelected(false);
				mIVDotScrollBarDot2.setSelected(true);
				mIVDotScrollBarDot3.setSelected(false);
				mIVDotScrollBarDot4.setSelected(false);
				mIVDotScrollBarDot5.setSelected(false);
				mIVDotScrollBarDot6.setSelected(false);
				mIVDotScrollBarDot7.setSelected(false);

				break;
			case 2:
				mIVDotScrollBarDot1.setSelected(false);
				mIVDotScrollBarDot2.setSelected(false);
				mIVDotScrollBarDot3.setSelected(true);
				mIVDotScrollBarDot4.setSelected(false);
				mIVDotScrollBarDot5.setSelected(false);
				mIVDotScrollBarDot6.setSelected(false);
				mIVDotScrollBarDot7.setSelected(false);

				break;
			case 3:
				mIVDotScrollBarDot1.setSelected(false);
				mIVDotScrollBarDot2.setSelected(false);
				mIVDotScrollBarDot3.setSelected(false);
				mIVDotScrollBarDot4.setSelected(true);
				mIVDotScrollBarDot5.setSelected(false);
				mIVDotScrollBarDot6.setSelected(false);
				mIVDotScrollBarDot7.setSelected(false);

				break;
			case 4:
				mIVDotScrollBarDot1.setSelected(false);
				mIVDotScrollBarDot2.setSelected(false);
				mIVDotScrollBarDot3.setSelected(false);
				mIVDotScrollBarDot4.setSelected(false);
				mIVDotScrollBarDot5.setSelected(true);
				mIVDotScrollBarDot6.setSelected(false);
				mIVDotScrollBarDot7.setSelected(false);

				break;
			case 5:
				mIVDotScrollBarDot1.setSelected(false);
				mIVDotScrollBarDot2.setSelected(false);
				mIVDotScrollBarDot3.setSelected(false);
				mIVDotScrollBarDot4.setSelected(false);
				mIVDotScrollBarDot5.setSelected(false);
				mIVDotScrollBarDot6.setSelected(true);
				mIVDotScrollBarDot7.setSelected(false);

				break;
			case 6:
				mIVDotScrollBarDot1.setSelected(false);
				mIVDotScrollBarDot2.setSelected(false);
				mIVDotScrollBarDot3.setSelected(false);
				mIVDotScrollBarDot4.setSelected(false);
				mIVDotScrollBarDot5.setSelected(false);
				mIVDotScrollBarDot6.setSelected(false);
				mIVDotScrollBarDot7.setSelected(true);

				break;

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {

		if (v == mIvCancel) {

			// on cancel clicked set tour has been shown and navigate to
			// registeration activity
			doOnCancelClicked();

		} else if (v == mIVDotScrollBarDot1) {
			doOnDotScrollBarClicked(0);

		} else if (v == mIVDotScrollBarDot2) {
			doOnDotScrollBarClicked(1);

		} else if (v == mIVDotScrollBarDot3) {
			doOnDotScrollBarClicked(2);

		} else if (v == mIVDotScrollBarDot4) {
			doOnDotScrollBarClicked(3);

		} else if (v == mIVDotScrollBarDot5) {
			doOnDotScrollBarClicked(4);

		} else if (v == mIVDotScrollBarDot6) {
			doOnDotScrollBarClicked(5);

		} else if (v == mIVDotScrollBarDot7) {
			doOnDotScrollBarClicked(6);

		}
	}

	public void doOnCancelClicked() {
		AppBase.setmBoolTourHasBeenShown(true, this);
		navigateToActivity(ActivityLogin.class);
		finish();

	}

	private void doOnDotScrollBarClicked(int mIntClickedDotNumber) {

		setDotScrollBarSelection(mIntClickedDotNumber);
		mVp.setCurrentItem(mIntClickedDotNumber);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class PagerAdapterClientTour extends FragmentPagerAdapter {

		public PagerAdapterClientTour(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = FragmentClientTourPage1.newInstance(position + 1);
				break;
			case 1:
				fragment = FragmentClientTourPage2.newInstance(position + 1);
				break;
			case 2:
				fragment = FragmentClientTourPage3.newInstance(position + 1);
				break;
			case 3:
				fragment = FragmentClientTourPage4.newInstance(position + 1);
				break;
			case 4:
				fragment = FragmentClientTourPage5.newInstance(position + 1);
				break;
			case 5:
				fragment = FragmentClientTourPage6.newInstance(position + 1);
				break;

			case 6:
				fragment = FragmentClientTourPage7.newInstance(position + 1);
				break;
			default:
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 7 total pages.
			return 7;
		}

	}

}
