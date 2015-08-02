package com.steveroggenkamp.android;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class  ItemListAdapter extends BaseAdapter {

	private final List<Item> mItems = new ArrayList<Item>();
	private final Context mContext;

	private static final String TAG = "Shopping-UI-ILA";

	private static class ViewHolder {
		CheckBox statusCheckBox;
		EditText titleView;

	}

	ViewHolder itemViewholder;

	public ItemListAdapter(Context context) {

		mContext = context;

	}

	// Add a item to the adapter
	// Notify observers that the data set has changed

	public void add(Item item) {

		mItems.add(item);
		notifyDataSetChanged();

	}

	// Clears the list adapter of all items.

	public void clear() {

		mItems.clear();
		notifyDataSetChanged();

	}

	// Returns the number of items

	@Override
	public int getCount() {

		return mItems.size();

	}

	// Retrieve the number of items

	@Override
	public Object getItem(int pos) {

		return mItems.get(pos);

	}

	// Get the ID for the item
	// In this case it's just the position

	@Override
	public long getItemId(int pos) {

		return pos;

	}

	// Create a View for the item at specified position
	// Remember to check whether convertView holds an already allocated View
	// before created a new View.
	// Consider using the ViewHolder pattern to make scrolling more efficient
	// See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// TOD - Get the current item
		final Item item = mItems.get(position);


		// TOD - Inflate the View for this item
		// from item.xml
		RelativeLayout itemLayout = (RelativeLayout) convertView;

		if ( itemLayout == null ) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (RelativeLayout)inflater.inflate(R.layout.item,parent,false);
		};

		// Fill in specific item data
		// Remember that the data that goes in this View
		// corresponds to the user interface elements defined
		// in the layout file

		// TODO - Fix Title in EditText
		final EditText titleView = (EditText) itemLayout.getChildAt(1);
		titleView.setText(item.getTitle());
		titleView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					item.setTitle(titleView.getText().toString());
					v.clearFocus();
					handled = true;
				}
				return handled;
			}
		});


		// TOD - Set up Status CheckBox
		final CheckBox statusView = (CheckBox) itemLayout.getChildAt(0);
		boolean isDone = item.getStatus() == Item.Status.DONE ? true : false;
		statusView.setChecked(isDone);


		statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				buttonView.setChecked(isChecked);
				statusView.setChecked(isChecked);
				if (isChecked) {
					item.setStatus(Item.Status.DONE);
				} else {
					item.setStatus(Item.Status.NOTDONE);
				}
				// statusView.setText(item.getStatus().toString());
				statusView.setChecked(item.getStatus() == Item.Status.DONE ? true : false);
					}
				});

		return itemLayout;
	}
}
