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
import android.view.ViewParent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class  ItemListAdapter extends ArrayAdapter {

	private final List<Item> mItems = new ArrayList<Item>();
	private final Context mContext;

	private static final String TAG = "Shopping-UI-ILA";

	private static class ViewHolder {
		CheckBox statusCheckBox;
		EditText titleView;
		Button  deleteView;
	}

	public ItemListAdapter(Context context) {
		super(context,R.layout.item,R.id.titleView);
		mContext = context;
		setNotifyOnChange(true);
	}

	// Add a item to the adapter
	// Notify observers that the data set has changed

	public void add(Item item) {
		mItems.add(item);
		notifyDataSetChanged();
	}

	public void remove(Item item) {
		mItems.remove(item);
		notifyDataSetChanged();
	}

	public void remove(int child) {
		mItems.remove( child );
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
		final Item    item   = mItems.get(position);
		final boolean isDone = item.getStatus() == Item.Status.DONE ? true : false;
		final String  title  = item.getTitle();
		ViewHolder itemViewHolder;

		// TOD - Inflate the View for this item
		// from item.xml
		RelativeLayout itemLayout = (RelativeLayout) convertView;

		if ( itemLayout == null ) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemLayout = (RelativeLayout)inflater.inflate(R.layout.item,parent,false);
			itemViewHolder = new ViewHolder();

			final CheckBox statusView = (CheckBox) itemLayout.findViewById(R.id.statusCheckBox);
			itemViewHolder.statusCheckBox = statusView;
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

			final EditText titleView = (EditText) itemLayout.findViewById(R.id.titleView);
			itemViewHolder.titleView = titleView;
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

			final Button deleteButton = (Button) itemLayout.findViewById(R.id.deleteButton);
			itemViewHolder.deleteView = deleteButton;
			deleteButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					RelativeLayout itemView = (RelativeLayout) v.getParent();
					ListView listView = (ListView) itemView.getParent();
					int itemNum = -1;
					for (int i = 0; i < mItems.size(); i++) {
						if (listView.getChildAt(i) == itemView) {
							itemNum = i;
							break;
						}
					}
					if (itemNum != -1) {
						remove(itemNum);
					}
				}
			});

			itemLayout.setTag(itemViewHolder);
		} else {
			itemViewHolder = (ViewHolder) itemLayout.getTag();
			itemViewHolder.statusCheckBox.setChecked(isDone);
			itemViewHolder.titleView.setText(title);
		}

		return itemLayout;
	}
}
