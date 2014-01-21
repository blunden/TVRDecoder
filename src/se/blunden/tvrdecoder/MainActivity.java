package se.blunden.tvrdecoder;

import java.util.ArrayList;

import se.blunden.tvrdecoder.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
                	
                	Log.d(TAG, "input tvr: " + tvr);
                	Log.d(TAG, "input tsi: " + tsi);
                	
                	ArrayList<String> tvrResult = decoder.decodeTVR(tvr);
                	ArrayList<String> tsiResult = decoder.decodeTSI(tsi);
                	
                	tvrInputField.setText("");
                	tsiInputField.setText("");
                	
                	displayOutput(BulletListBuilder.getBulletList(null, tvrResult), BulletListBuilder.getBulletList(null, tsiResult));
                }
            });
        
	}
	
	private void displayOutput(String tvr, String tsi) {
		// Get a reference to the layout where the card will be displayed
		final LinearLayout layout = (LinearLayout) findViewById(R.id.now_layout);
		
		// Create the View for the card 
		final CardView card = new CardView(this);
		
		// Specify layout parameters to be applied
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 19, 0, 0);
		
		card.setTvrHeaderText("TVR Results");
		card.setTsiHeaderText("TSI Results");
		
		card.setTvrText(tvr);
		card.setTsiText(tsi);
		card.setLayoutParams(lp);
		
        // Create a generic swipe-to-dismiss touch listener.
        card.setOnTouchListener(new SwipeDismissTouchListener(
                card,
                null,
                new SwipeDismissTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(Object token) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view, Object token) {
                    	Log.d(TAG, "in onDismiss()");
                    	layout.removeView(card);
                    }
                }));
        
        layout.addView(card);
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
	        if (view instanceof CardView) {
	        	Log.d(TAG, "clearAllCards i: " + i);
	        	group.removeView(view);
	        }
	    }
		// The loop above does not remove the first card if 3 or more have been added
		// For now, remove the card at index 2 (index 0 and 1 are the input fields) if it is indeed a card
		View view = group.getChildAt(2);
		if(view instanceof CardView) {
			Log.d(TAG, "clearAllCards removing remaining card at index 2");
        	group.removeView(view);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    // Store all formatted card strings to be able to restore on configuration change
	    ArrayList<String> savedTvrStrings = new ArrayList<String>();
	    ArrayList<String> savedTsiStrings = new ArrayList<String>();
	    ViewGroup group = (ViewGroup) findViewById(R.id.now_layout);
	    for (int i = 0, count = group.getChildCount(); i < count; ++i) {
	        View view = group.getChildAt(i);
	        if (view instanceof CardView) {
	            savedTvrStrings.add(((CardView)view).getTvrView().getText().toString());
	            savedTsiStrings.add(((CardView)view).getTsiView().getText().toString());
	        }
	    }
	    outState.putStringArrayList("savedTvrText", savedTvrStrings);
	    outState.putStringArrayList("savedTsiText", savedTsiStrings);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// Retrieve saved strings
		ArrayList<String> savedTvrStrings = savedInstanceState.getStringArrayList("savedTvrText");
		ArrayList<String> savedTsiStrings = savedInstanceState.getStringArrayList("savedTsiText");
		Log.d(TAG, "restored savedTvrStrings size: " + savedTvrStrings.size());
		Log.d(TAG, "restored savedTsiStrings size: " + savedTsiStrings.size());
		
		// Add the cards back
		if(savedTvrStrings != null && savedTsiStrings != null) {
	    	for(int i = 0; i < Math.max(savedTvrStrings.size(), savedTsiStrings.size()); i++) {
	    		displayOutput(savedTvrStrings.get(i), savedTsiStrings.get(i));
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
