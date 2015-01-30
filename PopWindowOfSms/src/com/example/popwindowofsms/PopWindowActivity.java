package com.example.popwindowofsms;

import java.util.ArrayList;

import contactsSearch.Search;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class PopWindowActivity extends Activity {
	
	final String destinationAddress = SMSReceiver.getAddress();
	final Context context = this;
	private static Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop_window);
		setFinishOnTouchOutside(false);

		ImageButton sendMessage = (ImageButton)this.findViewById(R.id.sendButton);
		ImageButton windowClose = (ImageButton)this.findViewById(R.id.closeButton);
		final ImageButton contactorButton = (ImageButton)this.findViewById(R.id.contactorButton);
		final TextView address = (TextView)this.findViewById(R.id.address);
		TextView message = (TextView)this.findViewById(R.id.Message);
		TextView time = (TextView)this.findViewById(R.id.time);
		final EditText send_Text = (EditText)this.findViewById(R.id.sendtext);
		
		//address.setText(destinationAddress);
		message.setText(SMSReceiver.getMessage());
		time.setText(SMSReceiver.getTime());
		
		Thread contactSetting = new Thread(new Runnable(){
			@Override
			public void run(){
				final Search contactSearch = new Search(destinationAddress, context);
				
				if(contactSearch.contactIsExist())
				{
					handler.post(new Runnable(){
						@Override
						public void run(){
							address.setText(contactSearch.getName());
							contactorButton.setImageBitmap(contactSearch.getImage());
						}
					});
				}
				else
					handler.post(new Runnable(){
						@Override
						public void run(){
							address.setText(destinationAddress);
						}
					});
			}
		});
		contactSetting.start();
		
		sendMessage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO sendMessage
				final String sendText = send_Text.getText().toString();
				if(sendText.length()>0)
				{
					Thread sendThread = new Thread(new Runnable(){
						@Override
						public void run(){
							SmsManager smsManager = SmsManager.getDefault();
							ArrayList<String> dividedMessage = smsManager.divideMessage(sendText);
							smsManager.sendMultipartTextMessage(destinationAddress, null,
									dividedMessage, null, null);
							WriteMessage.writeMessage(destinationAddress, dividedMessage,
									context);
						}
					});
					sendThread.start();
					
					finish();
				}
			}
		});
		windowClose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//TODO close the window
				finish();
			}
		});
		contactorButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				// TODO call
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+destinationAddress));
				startActivity(intent);
				finish();
			}
		});
	}
}
