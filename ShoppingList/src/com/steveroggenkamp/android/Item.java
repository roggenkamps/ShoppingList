package com.steveroggenkamp.android;

import java.text.ParseException;

import java.util.Locale;

import android.content.Intent;

public class Item {

	public static final String ITEM_SEP = System.getProperty("line.separator");

	public enum Status {
		NOTDONE, DONE
	};

	public final static String TITLE = "title";
	public final static String STATUS = "status";
	public final static String FILENAME = "filename";

	private String mTitle = new String();
	private Status mStatus = Status.NOTDONE;

	Item() {
		this.mTitle = "";
		this.mStatus = Status.NOTDONE;
	}

	Item(String title, Status status ) {
		this.mTitle = title;
		this.mStatus = status;
	}

	// Create a new ToDoItem from data packaged in an Intent

	Item(Intent intent) {
		mTitle = intent.getStringExtra(Item.TITLE);
		mStatus = Status.valueOf(intent.getStringExtra(Item.STATUS));
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public Status getStatus() {
		return mStatus;
	}

	public void setStatus(Status status) {
		mStatus = status;
	}

	public static void packageIntent(Intent intent, String title, Status status ) {
		intent.putExtra(Item.TITLE, title);
		intent.putExtra(Item.STATUS, status.toString());
	}

	public String toString() {
		return mTitle + ITEM_SEP + mStatus;
	}

	public String toLog() {
		return "Title:" + mTitle + ITEM_SEP + "Status:" + mStatus + "\n";
	}
}
