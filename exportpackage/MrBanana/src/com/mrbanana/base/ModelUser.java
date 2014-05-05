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

	String mStrAddress1 = "";
	String mStrAddress2 = "";

	String mStrPostalCode = "";
	String mStrLocation = "";
	String mStrGender = "";

	String mStrCreditCardNo = "";

	String mStrProfileImagePath = "";

	public String getmStrProfileImagePath() {
		return mStrProfileImagePath;
	}

	public void setmStrProfileImagePath(String mStrProfileImagePath) {
		this.mStrProfileImagePath = mStrProfileImagePath;
	}

	public String getmStrCreditCardNo() {
		return mStrCreditCardNo;
	}

	public void setmStrCreditCardNo(String mStrCreditCardNo) {
		this.mStrCreditCardNo = mStrCreditCardNo;
	}

	public String getmStrCreditCardExpiryMonth() {
		return mStrCreditCardExpiryMonth;
	}

	public void setmStrCreditCardExpiryMonth(String mStrCreditCardExpiryMonth) {
		this.mStrCreditCardExpiryMonth = mStrCreditCardExpiryMonth;
	}

	public String getmStrCreditCardExpiryYear() {
		return mStrCreditCardExpiryYear;
	}

	public void setmStrCreditCardExpiryYear(String mStrCreditCardExpiryYear) {
		this.mStrCreditCardExpiryYear = mStrCreditCardExpiryYear;
	}

	public String getmStrCreditCardCvv() {
		return mStrCreditCardCvv;
	}

	public void setmStrCreditCardCvv(String mStrCreditCardCvv) {
		this.mStrCreditCardCvv = mStrCreditCardCvv;
	}

	String mStrCreditCardExpiryMonth = "";
	String mStrCreditCardExpiryYear = "";
	String mStrCreditCardCvv = "";

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

	public static final String mStrKeyAddress1 = "address1";
	public static final String mStrKeyAddress2 = "address2";
	public static final String mStrKeyPostalCode = "user_postcode";
	public static final String mStrKeyLocation = "user_location";
	public static final String mStrKeyGender = "user_gender";

	public static final String mStrKeyCcNo = "cc_number";
	public static final String mStrKeyCcExpiryMonth = "cc_month";
	public static final String mStrKeyCcExpiryYear = "cc_year";
	public static final String mStrKeyCcCvv = "cc_cvv";

	public String getmStrAddress1() {
		return mStrAddress1;
	}

	public void setmStrAddress1(String mStrAddress1) {
		this.mStrAddress1 = mStrAddress1;
	}

	public String getmStrAddress2() {
		return mStrAddress2;
	}

	public void setmStrAddress2(String mStrAddress2) {
		this.mStrAddress2 = mStrAddress2;
	}

	public String getmStrPostalCode() {
		return mStrPostalCode;
	}

	public void setmStrPostalCode(String mStrPostalCode) {
		this.mStrPostalCode = mStrPostalCode;
	}

	public String getmStrLocation() {
		return mStrLocation;
	}

	public void setmStrLocation(String mStrLocation) {
		this.mStrLocation = mStrLocation;
	}

	public String getmStrGender() {
		return mStrGender;
	}

	public void setmStrGender(String mStrGender) {
		this.mStrGender = mStrGender;
	}

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

				setmStrAddress1(jsonObSuccessArrFirstKey
						.getString(mStrKeyAddress1));
				setmStrAddress2(jsonObSuccessArrFirstKey
						.getString(mStrKeyAddress2));
				setmStrPostalCode(jsonObSuccessArrFirstKey
						.getString(mStrKeyPostalCode));
				setmStrLocation(jsonObSuccessArrFirstKey
						.getString(mStrKeyLocation));
				setmStrGender(jsonObSuccessArrFirstKey.getString(mStrKeyGender));

				setmStrCreditCardNo(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcNo));
				setmStrCreditCardExpiryMonth(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcExpiryMonth));
				setmStrCreditCardExpiryYear(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcExpiryYear));
				setmStrCreditCardCvv(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcCvv));

				setmStrLoginSucces(mStrValueLoginSuccessSuccess);
			} else {
				setmStrLoginSucces(mStrValueLoginSuccessNotYet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setProfileDetailsFromMyProfileResponse(String mStr) {
		try {

			JSONObject json = new JSONObject(mStr);

			if (json.has("SUCCESS")) {
				JSONObject jsonObSuccessArrFirstKey = json
						.getJSONObject("SUCCESS");

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

				setmStrAddress1(jsonObSuccessArrFirstKey
						.getString(mStrKeyAddress1));
				setmStrAddress2(jsonObSuccessArrFirstKey
						.getString(mStrKeyAddress2));
				setmStrPostalCode(jsonObSuccessArrFirstKey
						.getString(mStrKeyPostalCode));
				setmStrLocation(jsonObSuccessArrFirstKey
						.getString(mStrKeyLocation));
				setmStrGender(jsonObSuccessArrFirstKey.getString(mStrKeyGender));

				setmStrCreditCardNo(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcNo));
				setmStrCreditCardExpiryMonth(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcExpiryMonth));
				setmStrCreditCardExpiryYear(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcExpiryYear));
				setmStrCreditCardCvv(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcCvv));

				setmStrProfileImagePath(jsonObSuccessArrFirstKey
						.getString(mStrKeyProfileImagePath));

				setmStrLoginSucces(mStrValueLoginSuccessSuccess);
			} else {
				setmStrLoginSucces(mStrValueLoginSuccessNotYet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCCDetailsFromhasCreditCradResponse(String mStr) {
		try {

			JSONObject json = new JSONObject(mStr);

			if (json.has("SUCCESS")) {
				JSONObject jsonObSuccessArrFirstKey = json
						.getJSONObject("SUCCESS");

				setmStrCreditCardNo(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcNo));
				setmStrCreditCardExpiryMonth(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcExpiryMonth));
				setmStrCreditCardExpiryYear(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcExpiryYear));
				setmStrCreditCardCvv(jsonObSuccessArrFirstKey
						.getString(mStrKeyCcCvv));

				setmStrLoginSucces(mStrValueLoginSuccessSuccess);
			} else {
				setmStrLoginSucces(mStrValueLoginSuccessNotYet);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
