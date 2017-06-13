package contactsSearch;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

public class Search {
	String name;
	boolean exist;
	Bitmap contactImage;
	
	public Search(String phoneNumber, Context context){
		name = "";
		exist = false;
		searchByPhoneNumer(phoneNumber, context);
	}
	
	public void searchByPhoneNumer(String phoneNumber,Context context){
		Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/" + phoneNumber);
		
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, null, null, null, null);
		if(cursor.getCount()>0)
		{
			exist = true;
			if(cursor.moveToFirst())
			{
				name = cursor.getString(cursor.getColumnIndex("display_name"));
			}
			cursor.moveToFirst();
			long contactId = cursor.getLong(cursor.getColumnIndex("contact_id"));
			Uri imageUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
					contactId);
			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream
					(context.getContentResolver(), imageUri);
			contactImage = BitmapFactory.decodeStream(input);
		}
		else
			exist = false;
	}
	
	public String getName()
	{
		return name;
	}
	public Bitmap getImage()
	{
		return contactImage;
	}
	public boolean contactIsExist()
	{
		return exist;
	}
}
