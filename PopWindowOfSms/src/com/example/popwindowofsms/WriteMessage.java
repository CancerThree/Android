package com.example.popwindowofsms;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class WriteMessage {
	static Context tmpContext;
	
	public static void writeMessage(String phoneNumber, ArrayList<String> text, Context context){
		tmpContext = context;
		String messageText = "";
		ContentValues messageValues = new ContentValues();
		
		for(String temp : text)
		{
			messageText += temp;
		}
		messageValues.put("date", System.currentTimeMillis());
		messageValues.put("read", "1");	//1�����Ѷ���0Ϊδ��
		messageValues.put("type", "2");	//1Ϊ�գ�2Ϊ��
		messageValues.put("address", phoneNumber);
		messageValues.put("body", messageText);
		context.getContentResolver().insert(Uri.parse("content://sms/sent"),messageValues);
	}
}
