package com.steveroggenkamp.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.steveroggenkamp.android.Item.Status;

public class ItemManagerActivity extends ListActivity {

	private static final int ADD_ITEM_REQUEST = 0;
	private static final String FILE_NAME = "ItemManagerActivityData.txt";
	private static final String TAG = "Shopping-UI-IMA";

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;

	ItemListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create a new ItemListAdapter for this ListActivity's ListView
		mAdapter = new ItemListAdapter(getApplicationContext());

		// Put divider between ToDoItems and FooterView
		getListView().setFooterDividersEnabled(true);

		// TOD - Inflate footerView for footer_view.xml file
		TextView footerView = (TextView)getListView()
				                            .inflate(getApplicationContext(),
											         R.layout.footer_view,
											         null);

		// TOD - Add footerView to ListView

		getListView().addFooterView(footerView);
        
		// TOD - Attach Listener to FooterView
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Item item = new Item();
				mAdapter.add(item);

				/*
				Intent explicitIntent = new Intent(ItemManagerActivity.this, AddItemActivity.class);
			    startActivityForResult(explicitIntent, ADD_ITEM_REQUEST);
				*/
			}
		});

		// TOD - Attach the adapter to this ListActivity's ListView
		setListAdapter(mAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "Entered onActivityResult()");

		// TOD - Check result code and request code
		// if user submitted a new ToDoItem
		// Create a new ToDoItem from the data Intent
		// and then add it to the adapter

		if ( resultCode == RESULT_CANCELED ) {
			return;
		} else if (resultCode != RESULT_OK) {
			Log.i(TAG, "Bad result code:" + resultCode);
			return;
		} else if (requestCode == ADD_ITEM_REQUEST) {
			Log.i(TAG, "Adding new item");
			if ( data != null ) {
				Item item = new Item(data);
				mAdapter.add(item);
			}
		}
	}

	// Do not modify below here

	@Override
	public void onResume() {
		super.onResume();

		// Load saved ToDoItems, if necessary

		if (mAdapter.getCount() == 0)
			loadItems();
	}

	@Override
	protected void onPause() {
		super.onPause();

		saveItems();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
			mAdapter.clear();
			return true;
		case MENU_DUMP:
			dump();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void dump() {

		for (int i = 0; i < mAdapter.getCount(); i++) {
			String data = ((Item) mAdapter.getItem(i)).toLog();
			Log.i(TAG,	"Item " + i + ": " + data.replace(Item.ITEM_SEP, ","));
		}

	}

	// Load stored ToDoItems
	private void loadItems() {
		BufferedReader reader = null;
		try {
			FileInputStream fis = openFileInput(FILE_NAME);
			reader = new BufferedReader(new InputStreamReader(fis));

			String title = null;
			String status = null;

			while (null != (title = reader.readLine())) {
				status = reader.readLine();
				mAdapter.add(new Item(title, Status.valueOf(status)));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Save ToDoItems to file
	private void saveItems() {
		PrintWriter writer = null;
		try {
			FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
					fos)));

			for (int idx = 0; idx < mAdapter.getCount(); idx++) {

				writer.println(mAdapter.getItem(idx));

			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != writer) {
				writer.close();
			}
		}
	}
}
