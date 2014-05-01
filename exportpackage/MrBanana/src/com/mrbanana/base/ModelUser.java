package com.mrbanana.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelUser {

	String mStrFirstName = "";
	String mStrLastName = "";
	String mStrEmail = "";
	String mStrPhoneNo = "";
	String mStrPassword = "";
	String mStrId = "";
	String mStrLoginResponse = "";
	String mStrStatus = "";
	String mStrLoginSucces = "notYet";

	public String getmStrStatus() {
		return mStrStatus;
	}

	public void setmStrStatus(String mStrStatus) {
		this.mStrStatus = mStrStatus;
	}

	public String getmStrLoginResponse() {
		return mStrLoginResponse;
	}

	public void setmStrLoginResponse(String mStrLoginResponse) {
		this.mStrLoginResponse = mStrLoginResponse;
	}

	public static final String mStrValueLoginSuccessNotYet = "Notyet";
	public static final String mStrValueLoginSuccessSuccess = "Success";

	public String getmStrLoginSucces() {
		return mStrLoginSucces;
	}

	public void setmStrLoginSucces(String mStrLoginSucces) {
		this.mStrLoginSucces = mStrLoginSucces;
	}

	public static final String mStrKeyFirstName = "first_name";
	public static final String mStrKeyLastName = "last_name";
	public static final String mStrKeyEmail = "user_email";
	public static final String mStrKeyPhoneNo = "user_mobile";
	public static final String mStrKeyPassword = "user_password";
	public static final String mStrKeyId = "user_id";

	public static final String mStrKeyStatus = "user_status";
	public static final String mStrKeyProfileImagePath = "profile_image_path";

	public String getmStrFirstName() {
		return mStrFirstName;
	}

	public void setmStrFirstName(String mStrFirstName) {
		this.mStrFirstName = mStrFirstName;
	}

	public String getmStrLastName() {
		return mStrLastName;
	}

	public void setmStrLastName(String mStrLastName) {
		this.mStrLastName = mStrLastName;
	}

	public String getmStrEmail() {
		return mStrEmail;
	}

	public void setmStrEmail(String mStrEmail) {
		this.mStrEmail = mStrEmail;
	}

	public String getmStrPhoneNo() {
		return mStrPhoneNo;
	}

	public void setmStrPhoneNo(String mStrPhoneNo) {
		this.mStrPhoneNo = mStrPhoneNo;
	}

	public String getmStrPassword() {
		return mStrPassword;
	}

	public void setmStrPassword(String mStrPassword) {
		this.mStrPassword = mStrPassword;
	}

	public String getmStrId() {
		return mStrId;
	}

	public void setmStrId(String mStrId) {
		this.mStrId = mStrId;
	}

	@Override
	public String toString() {
		super.toString();
		JSONObject json = new JSONObject();
		JSONObject Whole = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			json.put(mStrKeyFirstName, mStrFirstName);
			json.put(mStrKeyLastName, mStrLastName);
			json.put(mStrKeyEmail, mStrEmail);
			json.put(mStrKeyPhoneNo, mStrKeyPhoneNo);
			json.put(mStrKeyPassword, mStrPassword);
			json.put(mStrKeyId, mStrId);

			arr.put(json);
			Whole.put("SUCCESS", arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public void fromString(String mStr) {
		try {

			JSONObject json = new JSONObject(mStr);

			String strJsonarrSucess = "";
			JSONArray jsonarrSuccess = new JSONArray();
			if (json.has("SUCCESS")) {
				strJsonarrSucess = json.getString("SUCCESS");
				jsonarrSuccess = new JSONArray(strJsonarrSucess);
				JSONObject jsonObSuccessArrFirstKey = new JSONObject();
				jsonObSuccessArrFirstKey = jsonarrSuccess.getJSONObject(0);

				setmStrFirstName(jsonObSuccessArrFirstKey
						.getString(mStrKeyFirstName));

				setmStrLastName(jsonObSuccessArrFirstKey
						.getString(mStrKeyLastName));
				setmStrEmail(jsonObSuccessArrFirstKey.getString(mStrKeyEmail));
				setmStrPhoneNo(jsonObSuccessArrFirstKey
						.getString(mStrKeyPhoneNo));
				setmStrPassword(jsonObSuccessArrFirstKey
						.getString(mStrKeyPassword));
				setmStrId(jsonObSuccessArrFirstKey.getString(mStrKeyId));
				setmStrStatus(jsonObSuccessArrFirstKey.getString(mStrKeyStatus));

				setmStrLoginSucces(mStrValueLoginSuccessSuccess);
			} else {
				setmStrLoginSucces(mStrValueLoginSuccessNotYet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
