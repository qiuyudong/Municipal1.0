package com.example.municipalmanage;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact> {
   private int resourceId;
	public ContactAdapter(Context context, int textViewResourceId,List<Contact> objects) {
		super(context, textViewResourceId,objects);
		resourceId=textViewResourceId;
		// TODO Auto-generated constructor stub
	}
 @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Contact contact=getItem(position);
		View view=LayoutInflater.from(getContext()).inflate(resourceId, null);
		TextView name=(TextView)view.findViewById(R.id.name);
		TextView phone_number=(TextView)view.findViewById(R.id.phone_number);
		name.setText(contact.getName());
		phone_number.setText(contact.getPhone_numeber());
		return view;
	}
}
