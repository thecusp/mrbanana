package com.mrbanana.app.account.profile;

import homemade.apps.framework.homerlibs.utils.AlertBoxUtils;
import homemade.apps.framework.homerlibs.utils.HomerLogger;
import homemade.apps.framework.homerlibs.utils.NetworkApis;
import homemade.apps.framework.homerlibs.utils.Utils;
import homemade.apps.framework.homerlibs.utils.Validation;
import homemade.apps.framework.homerlibs.utils.imageloader.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mrbanana.R;
import com.mrbanana.base.ActivityBase;
import com.mrbanana.base.AppBase;

public class ActivityProfile extends ActivityBase {

	EditText mEtFirstName, mEtLastName, mEtPhone, mEtAddress1, mEtAddress2,
			mEtPostCode, mEtLocation;
	RelativeLayout mRlGenderWrapper;
	TextView mTvGenderValue, mTvBack, mTvDone;

	ImageView mIvProfilePic;
	ProgressBar mPbProfilePicProgress;

	private Uri mImageCaptureUri;
	String mStrMyProfileResposne = "";
	String mStrUpdateProfileResposne = "";

	String mStrUploadImageResposne = "";

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 3;
	private static final int SELECT_PICTURE = 0;

	public static final String mStrValueGenderMale = "Male";
	public static final String mStrValueGenderFeMale = "FeMale";
	public static final String[] mStrArrValuesDropDownGenders = {
			mStrValueGenderMale, mStrValueGenderFeMale };

	ImageLoader mIlImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ap_layout);

		mIlImageLoader = new ImageLoader(this);

		findViewByIds();
		setOnClickListeners();
		manipulateUi();
	}

	private void findViewByIds() {
		// TODO Auto-generated method stub

		mEtFirstName = (EditText) findViewById(R.id.ap_lEtFirstName);
		mEtLastName = (EditText) findViewById(R.id.ap_lEtLastName);
		mEtPhone = (EditText) findViewById(R.id.ap_lEtMobile);
		mEtAddress1 = (EditText) findViewById(R.id.ap_lEtAddress1);
		mEtAddress2 = (EditText) findViewById(R.id.ap_lEtAddress2);
		mEtLocation = (EditText) findViewById(R.id.ap_lEtLocation);
		mEtPostCode = (EditText) findViewById(R.id.ap_lEtPostcode);

		mIvProfilePic = (ImageView) findViewById(R.id.ap_lIvProfileImage);

		mRlGenderWrapper = (RelativeLayout) findViewById(R.id.ap_lRlGenderWrapper);
		mTvBack = (TextView) findViewById(R.id.ap_lTvBack);
		mTvDone = (TextView) findViewById(R.id.ap_lTvDone);
		mTvGenderValue = (TextView) findViewById(R.id.ap_lTvGenderValue);
	}

	private void setOnClickListeners() {
		// TODO Auto-generated method stub

		mIvProfilePic.setOnClickListener(this);
		mRlGenderWrapper.setOnClickListener(this);
		mTvBack.setOnClickListener(this);
		mTvDone.setOnClickListener(this);
	}

	private void manipulateUi() {
		// TODO Auto-generated method stub
		mTvBack.setText("< Accounts");
		doOnGenderSelected(0);
		AsyctaskMyProfile mAtmpMyProfileTask = new AsyctaskMyProfile(this);
		mAtmpMyProfileTask.execute();
	}

	private void loadUi() {
		mEtFirstName.setText(AppBase.getmMuUser().getmStrFirstName());
		mEtLastName.setText(AppBase.getmMuUser().getmStrLastName());
		mEtPhone.setText(AppBase.getmMuUser().getmStrPhoneNo());
		mEtAddress1.setText(AppBase.getmMuUser().getmStrAddress1());
		mEtAddress2.setText(AppBase.getmMuUser().getmStrAddress2());
		mEtLocation.setText(AppBase.getmMuUser().getmStrLocation());
		mEtPostCode.setText(AppBase.getmMuUser().getmStrPostalCode());

		loadGender();

		mIlImageLoader.DisplayImage(AppBase.getmMuUser()
				.getmStrProfileImagePath(), mIvProfilePic);
	}

	private void loadGender() {
		if (AppBase.getmMuUser().getmStrGender().toLowerCase().trim()
				.equals(mStrValueGenderMale.toLowerCase().trim())) {
			doOnGenderSelected(0);

		} else if (AppBase.getmMuUser().getmStrGender().toLowerCase().trim()
				.equals(mStrValueGenderFeMale.toLowerCase().trim())) {
			doOnGenderSelected(1);

		} else {
			doOnGenderSelected(0);
		}

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v == mRlGenderWrapper) {
			showSelectGenderDialog();
		}
		if (v == mTvBack) {
			onBackPressed();
		}
		if (v == mTvDone) {

			if (Validation.hasText(mEtAddress1)) {
				doOnDonePressed();
			} else {
				AlertBoxUtils.getAlertDialogBox(this,
						"Please fill in your address before proceeding");
			}
		}

		if (v == mIvProfilePic) {
			// pickPhoto();
			pickPhoto2();
		}
	}

	private void doOnDonePressed() {

		onBackPressed();
		AsyctaskUpdateProfile mAtupUpdateProfileTask = new AsyctaskUpdateProfile(
				this);
		mAtupUpdateProfileTask.execute();

	}

	protected void showSelectGenderDialog() {

		try {
			int count = mStrArrValuesDropDownGenders.length;
			int checkedpos = -1;
			for (int i = 0; i < count; i++) {
				if (mStrArrValuesDropDownGenders[i].equals(""
						+ mTvGenderValue.getText().toString().trim())) {
					checkedpos = i;
				}
			}
			if (checkedpos == -1) {
				checkedpos = 0;
			}

			DialogInterface.OnClickListener fontDialogListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int pos) {
					doOnGenderSelected(pos);
					dialog.dismiss();
				}

			};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Gender");
			builder.setSingleChoiceItems(mStrArrValuesDropDownGenders,
					checkedpos, fontDialogListener);

			AlertDialog dialog = builder.create();
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doOnGenderSelected(int pos) {
		mTvGenderValue.setText(mStrArrValuesDropDownGenders[pos]);
	}

	public void pickPhoto() {
		// TODO: launch the photo picker
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				SELECT_PICTURE);
	}

	public void pickPhoto2() {
		final String[] items = new String[] { "Take from camera",
				"Select from gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
																	// camera
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					mImageCaptureUri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "tmp_avatar_"
							+ String.valueOf(System.currentTimeMillis())
							+ ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
							mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { // pick from file
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_FILE);
				}
			}
		});

		final AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//
		try {
			Bitmap bitmap = null;
			if (resultCode == Activity.RESULT_OK
					&& requestCode == PICK_FROM_FILE) {
				if (data.getData() != null) {
					try {
						if (bitmap != null) {
							bitmap.recycle();
						}
						mImageCaptureUri = data.getData();
						InputStream stream = getContentResolver()
								.openInputStream(mImageCaptureUri);
						bitmap = BitmapFactory.decodeStream(stream);
						stream.close();

						int nh = (int) (bitmap.getHeight() * (512.0 / bitmap
								.getWidth()));
						Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512,
								nh, true);

						mIvProfilePic.setImageBitmap(scaled);
					}

					catch (Exception e) {
						e.printStackTrace();
					}

				}

				else {
					bitmap = (Bitmap) data.getExtras().get("data");

					int nh = (int) (bitmap.getHeight() * (512.0 / bitmap
							.getWidth()));
					Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh,
							true);

					mIvProfilePic.setImageBitmap(scaled);
				}

			} else if (resultCode == Activity.RESULT_OK
					&& requestCode == PICK_FROM_CAMERA) {

				InputStream stream = getContentResolver().openInputStream(
						mImageCaptureUri);
				bitmap = BitmapFactory.decodeStream(stream);
				stream.close();

				int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
				Bitmap scaled = Bitmap
						.createScaledBitmap(bitmap, 512, nh, true);

				mIvProfilePic.setImageBitmap(scaled);

			}
			AsyctaskUploadProfileImage mAtUphoUploadPhototask = new AsyctaskUploadProfileImage(
					this);
			mAtUphoUploadPhototask.execute();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getPath(Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String filePath = cursor.getString(column_index);
		cursor.close();

		return filePath;
	}

	/**
	 * this one atleast returns something
	 * 
	 * @param uri
	 * @return
	 */
	public String getImgPath(Uri uri) {
		String[] largeFileProjection = { MediaStore.Images.ImageColumns._ID,
				MediaStore.Images.ImageColumns.DATA };
		String largeFileSort = MediaStore.Images.ImageColumns._ID + " DESC";
		Cursor myCursor = this.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				largeFileProjection, null, null, largeFileSort);
		String largeImagePath = "";
		try {
			myCursor.moveToFirst();
			largeImagePath = myCursor
					.getString(myCursor
							.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));
		} finally {
			myCursor.close();
		}

		HomerLogger.d("imagepath =" + largeImagePath);
		return largeImagePath;
	}

	public String fetchPathFromUri(Uri uri) {
		String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, filePathColumn, null,
				null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);

		HomerLogger.d("imagepath =" + picturePath);
		return picturePath;
	}

	class AsyctaskMyProfile extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskMyProfile(Context context) {
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
							+ "myprofile", mNvp);

					HomerLogger.d("myprofile response ==" + mStsResponse);

					parseMyProfileResponse(mStsResponse);

					mStrMyProfileResposne = mStsResponse;

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
					if (checkIfMyProfileWorkedSuccessfully()) {
						loadUi();
					} else {
						AlertBoxUtils
								.getAlertDialogBox(mWrContext.get(),
										"Opps .!! Could not load your profile Please try again later.")
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

	private void parseMyProfileResponse(String mStrResponse) {
		// AppBase.getmMuUser().setmStrLoginResponse(mStrResponse);

		AppBase.getmMuUser().setProfileDetailsFromMyProfileResponse(
				mStrResponse);
	}

	private boolean checkIfMyProfileWorkedSuccessfully() {
		try {
			JSONObject jsonob = new JSONObject(mStrMyProfileResposne);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	class AsyctaskUpdateProfile extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskUpdateProfile(Context context) {
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

					mNvp.add(new BasicNameValuePair("first_name", mEtFirstName
							.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("last_name", mEtLastName
							.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("user_gender",
							mTvGenderValue.getText().toString().trim()
									.toLowerCase()));

					mNvp.add(new BasicNameValuePair("user_mobile", mEtPhone
							.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("address1", mEtAddress1
							.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("address2", mEtAddress2
							.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("user_location",
							mEtLocation.getText().toString().trim()));

					mNvp.add(new BasicNameValuePair("user_postcode",
							mEtPostCode.getText().toString().trim()));

					// mNvp.add(new BasicNameValuePair("user_id",mEt));

					String mStsResponse = Utils.postData(AppBase.mStrBaseUrl
							+ "updateprofile", mNvp);

					HomerLogger.d("updateprofile response ==" + mStsResponse);

					mStrUpdateProfileResposne = mStsResponse;

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
					if (checkIfMyProfileWorkedSuccessfully()) {
						AsyctaskMyProfile mAtmpMyProfiletask = new AsyctaskMyProfile(
								mWrContext.get());
						mAtmpMyProfiletask.execute();
					} else {
						AlertBoxUtils.getAlertDialogBox(mWrContext.get(),
								mStrUpdateProfileResposne).show();
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

	private boolean checkIfUpdateProfileWorkedSuccessfully() {
		try {
			JSONObject jsonob = new JSONObject(mStrUpdateProfileResposne);

			if (jsonob.has("ERROR")) {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	class AsyctaskUploadProfileImage extends AsyncTask<Void, Void, Void> {
		ProgressDialog mPdDialog;
		boolean mBoolWasInternetPresentDuringDoInBackground = false;
		WeakReference<Context> mWrContext;

		public AsyctaskUploadProfileImage(Context context) {
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
				Looper.prepare();
				if (NetworkApis.isOnline()) {

					mBoolWasInternetPresentDuringDoInBackground = true;
					List<NameValuePair> mNvp = new ArrayList<NameValuePair>();

					mNvp.add(new BasicNameValuePair("user_id", AppBase
							.getmMuUser().getmStrId()));

					String mStsResponse = Utils
							.executeHttpPostWithMultiPartEntity(
									AppBase.mStrBaseUrl + "uploadimage", mNvp,
									fetchPathFromUri(mImageCaptureUri),
									"profilepic.jpg", "user_img");

					HomerLogger.d("uploadimage response ==" + mStsResponse);

					mStrUploadImageResposne = mStsResponse;

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
					if (checkIfUploadImageWorkedSuccessfully()) {
						AsyctaskMyProfile mAtmpMyProfiletask = new AsyctaskMyProfile(
								mWrContext.get());
						mAtmpMyProfiletask.execute();
					} else {
						AlertBoxUtils.getAlertDialogBox(mWrContext.get(),
								mStrUploadImageResposne).show();
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

	private boolean checkIfUploadImageWorkedSuccessfully() {
		try {
			JSONObject jsonob = new JSONObject(mStrUploadImageResposne);

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
