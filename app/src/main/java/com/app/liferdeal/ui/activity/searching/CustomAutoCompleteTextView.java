package com.app.liferdeal.ui.activity.searching;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import java.util.HashMap;


/** Customizing AutoCompleteTextView to return Place Description
 *  corresponding to the selected item
 */
@SuppressLint("AppCompatCustomView")
public class CustomAutoCompleteTextView extends AutoCompleteTextView
{
	
	public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** Returns the place description corresponding to the selected item */
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem)
	{
		/** Each item in the autocompetetextview suggestion list is a hashmap object */
//		Utility.printLog("selectedItem="+selectedItem);
		Log.e("Onselected item", ""+selectedItem);
		HashMap<String, String> hm = (HashMap<String, String>) selectedItem;
		return hm.get("description");
	}
	
}
