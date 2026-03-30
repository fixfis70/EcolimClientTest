package dev.fixfis.server.whatsapp;

import com.google.gson.annotations.SerializedName;

public class SendMessage {

	@SerializedName("phone")
	private String phone;

	@SerializedName("is_forwarded")
	private boolean isForwarded = false;

	@SerializedName("message")
	private String message;

	public void setPhone(String phone){
		this.phone = phone.replace(" ","")+"@s.whatsapp.net";
	}

	public String getPhone(){
		return phone;
	}

	public void setIsForwarded(boolean isForwarded){
		this.isForwarded = isForwarded;
	}

	public boolean isIsForwarded(){
		return isForwarded;
	}

	public void setMessage(String message){

		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}