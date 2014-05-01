package com.mrbanana.app.clienttour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.mrbanana.R;

public class FragmentClientTourPage7 extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static FragmentClientTourPage7 newInstance(int sectionNumber) {
		FragmentClientTourPage7 fragment = new FragmentClientTourPage7();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public FragmentClientTourPage7() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.act_frag_pageseven,
				container, false);

		manipulateUi(rootView);

		return rootView;
	}

	private void manipulateUi(View rootView) {
		Button mBtnGetStarted = (Button) rootView
				.findViewById(R.id.act_frag_pagesevenBtnGetStarted);
		
		mBtnGetStarted.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try {
					((ActivityClientTour)getActivity()).doOnCancelClicked();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}
}
