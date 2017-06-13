package com.example.popwindowofsms;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
	static String messageBytes="";
	static String address;
	static Date time;
	static String hourMin; 

	public SMSReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		final Context tContext = context;
		final Intent tIntent = intent;
		
		Thread everyMessage = new Thread(new Runnable() {
			@Override
			public void run(){
				messageBytes="";
				if("android.provider.Telephony.SMS_RECEIVED".equals(tIntent.getAction()))
				{
					Bundle bundle = tIntent.getExtras();
					if(bundle != null)
					{
						Object[] objArray = (Object[])bundle.get("pdus");
						SmsMessage[] Sms = new SmsMessage[objArray.length];
						for(int i=0; i<objArray.length; i++)
						{
							Sms[i] = SmsMessage.createFromPdu((byte[]) objArray[i]);
						}
						for(SmsMessage currentMessage : Sms)
						{
							address = currentMessage.getDisplayOriginatingAddress();
							messageBytes += currentMessage.getDisplayMessageBody();
							time = new Date(currentMessage.getTimestampMillis());
							hourMin = getHourMin(time);
						}
					}
					//Æô¶¯µ¯´°µÄActivity
					Intent popWindowIntent = new Intent(tContext, PopWindowActivity.class);
					popWindowIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					tContext.startActivity(popWindowIntent);
				}
		}});
		everyMessage.start();
	}
	
	public static String getMessage(){
		return messageBytes;
	}
	public static String getAddress(){
		return address;
	}
	public static String getTime(){
		return hourMin;
	}
	public static String getHourMin(Date date){
		String tmp = date.toString();
		int position = tmp.indexOf(":");
		return tmp.substring(position-2, position+3);
	}
}
