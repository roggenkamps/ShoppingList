package com.steveroggenkamp.android;


import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.steveroggenkamp.android.Item.Status;

public class AddItemActivity extends Activity {

	// 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
	private static final int SEVEN_DAYS = 604800000;

	private static final String TAG = "Shopping-UI-AIA";
	private EditText mItemText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_item);

		mItemText = (EditText) findViewById(R.id.title);

		// OnClickListener for the Cancel Button,

		final Button cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TOD - Indicate result and finish

                setResult(RESULT_CANCELED);
                finish();
			}
		});

		// TOD - Set up OnClickListener for the Reset Button
		final Button resetButton = (Button) findViewById(R.id.resetButton);
		resetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// TOD - Reset data to default values
				mItemText.setText("");
			}
		});

		// Set up OnClickListener for the Submit Button

		final Button submitButton = (Button) findViewById(R.id.submitButton);
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				// gather Item data

				String itemString = mItemText.getText().toString();
				
				// Package ToDoItem data into an Intent
				Intent data = new Intent();
				Item.packageIntent(data, itemString, Status.NOTDONE );

				// TOD - return data Intent and finish
				setResult(RESULT_OK,data);
				finish();
			}
		    });
	}

	private String getItemTitle() {
		return mItemText.getText().toString();
	}
}
