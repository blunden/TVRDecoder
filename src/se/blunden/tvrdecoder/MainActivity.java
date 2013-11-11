package se.blunden.tvrdecoder;

import java.util.ArrayList;

import se.blunden.tvrdecoder.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "TVRDecoder";
	
	private static final int ABOUT_ID = Menu.FIRST;
	private static final int CLEAR_ALL_ID = Menu.FIRST + 1;
	
	private static String aboutMessage = null;
	private AlertDialog mAboutDialog;
	
	private Decoder decoder;
	private EditText tvrInputField;
	private EditText tsiInputField;
	private ImageButton decodeButton;
	
	private ArrayList<String> tvrResult;
	private ArrayList<String> tsiResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		prepareAboutDialog();
		
		decoder = new Decoder();
		
        tvrInputField = (EditText) findViewById(R.id.input_tvr);
        tsiInputField = (EditText) findViewById(R.id.input_tsi);

        decodeButton = (ImageButton) findViewById(R.id.button_decode);
        decodeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                	// Valid input characters enforced by input_tvr and input_tsi
                	String tvr = tvrInputField.getText().toString();
                	String tsi = tsiInputField.getText().toString();
                	
                	tvrResult = decoder.decodeTVR(tvr);
                	tsiResult = decoder.decodeTSI(tsi);
                	
                	Log.d(TAG, "input tvr: " + tvr);
                	Log.d(TAG, "input tsi: " + tsi);
                	
                	tvrInputField.setText("");
                	tsiInputField.setText("");
                	
                	displayOutput(buildResultString());
                }
            });
        
	}
	
	private String buildResultString() {
		String formattedString = "";
		if(!tvrResult.isEmpty()) {
			formattedString = BulletListBuilder.getBulletList("TVR Results", tvrResult);
		}
		if(!tsiResult.isEmpty()) {
			if(!tvrResult.isEmpty()) {
				formattedString += "\n";
			}
			formattedString += BulletListBuilder.getBulletList("TSI Results", tsiResult);
		}
		return formattedString;
	}
	
	private void displayOutput(String formattedString) {
		if(formattedString.equals("")) {
			Log.d(TAG, "displayOutput received empty string. No output shown.");
			return;
		}
		final LinearLayout layout = (LinearLayout) findViewById(R.id.now_layout);
		
		final TextView resultView = new CardTextView(new ContextThemeWrapper(this, R.style.nowCardStyle));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 20, 0, 0);
		
		resultView.setText(formattedString);
		resultView.setLayoutParams(lp);
		
        // Create a generic swipe-to-dismiss touch listener.
        resultView.setOnTouchListener(new SwipeDismissTouchListener(
                resultView,
                null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                    	Log.d(TAG, "in onDismiss()");
                    	layout.removeView(resultView);
                    }
                }));
        
        layout.addView(resultView);
	}
	
	private void prepareAboutDialog() {
		if (aboutMessage == null) {
			aboutMessage = getString(R.string.about_message);
		}
		
		mAboutDialog = new AlertDialog.Builder(this)
		.setTitle(R.string.menu_about)
		.setMessage(aboutMessage)
		.setNeutralButton(R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		})
		.create();
	}
	
	private void clearAllCards() {
		ViewGroup group = (ViewGroup) findViewById(R.id.now_layout);
		for (int i = 0, count = group.getChildCount(); i < count; i++) {
	        View view = group.getChildAt(i);
	        if (view instanceof CardTextView) {
	        	Log.d(TAG, "clearAllCards i: " + i);
	        	group.removeView(view);
	        }
	    }
		// The loop above does not remove the first card if 3 or more have been added
		// For now, remove the card at index 2 (index 0 and 1 are the input fields) if it is indeed a card
		View view = group.getChildAt(2);
		if(view instanceof CardTextView) {
			Log.d(TAG, "clearAllCards removing remaining index 2");
        	group.removeView(view);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    // Store all formatted card strings to be able to restore on configuration change
	    ArrayList<String> savedStrings = new ArrayList<String>();
	    ViewGroup group = (ViewGroup) findViewById(R.id.now_layout);
	    for (int i = 0, count = group.getChildCount(); i < count; ++i) {
	        View view = group.getChildAt(i);
	        if (view instanceof CardTextView) {
	            savedStrings.add(((CardTextView)view).getText().toString());
	        }
	    }
	    outState.putStringArrayList("savedCardText", savedStrings);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// Retrieve saved strings and add the cards back
		ArrayList<String> savedStrings = savedInstanceState.getStringArrayList("savedCardText");
		if(!(savedStrings == null)) {
	    	for(String text : savedStrings) {
	    		displayOutput(text);
	    	}
	    }
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ABOUT_ID, 0, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(0, CLEAR_ALL_ID, 0, R.string.menu_clear_all).setIcon(android.R.drawable.ic_menu_close_clear_cancel).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case ABOUT_ID:
			mAboutDialog.show();
			return true;
		
		case CLEAR_ALL_ID:
			clearAllCards();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
}
