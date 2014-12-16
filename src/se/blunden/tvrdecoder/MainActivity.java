package se.blunden.tvrdecoder;

import java.util.ArrayList;

import se.blunden.tvrdecoder.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MainActivity extends ActionBarActivity {
	private static final String TAG = "TVRDecoder";
	
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
		final ResultCardView card = new ResultCardView(this);
		
		// Specify layout parameters to be applied
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 20, 0, 0);
		
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
		boolean finished = false;
		while(!finished) {
			ViewGroup group = (ViewGroup) findViewById(R.id.now_layout);
			int count = group.getChildCount();
			int i;
			for (i = 0; i < count; i++) {
				View view = group.getChildAt(i);
		        if (view instanceof ResultCardView) {
		        	group.removeView(view);
		        	break;
		        }
		    }
			if(i == count) {
				finished = true;
			}
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
	        if (view instanceof ResultCardView) {
	            savedTvrStrings.add(((ResultCardView)view).getTvrView().getText().toString());
	            savedTsiStrings.add(((ResultCardView)view).getTsiView().getText().toString());
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
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_about:
			mAboutDialog.show();
			return true;
		
		case R.id.action_clear_all:
			// Remove all the cards
        	clearAllCards();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
