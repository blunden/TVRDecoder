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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "TVRDecoder";
	
	private static final int ABOUT_ID = Menu.FIRST;
	
	private static String aboutMessage = null;
	private AlertDialog mAboutDialog;
	
	private Decoder decoder;
	private EditText tvrInputField;
	private EditText tsiInputField;
	private Button decodeButton;
	
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

        decodeButton = (Button) findViewById(R.id.button_decode);
        decodeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                	// TODO: Display error to the user if invalid
                	// ie. check the return value and create alert dialog
                	String tvr = tvrInputField.getText().toString();
                	String tsi = tsiInputField.getText().toString();
                	
                	tvrResult = decoder.decodeTVR(tvr);
                	tsiResult = decoder.decodeTSI(tsi);
                	
                	Log.d(TAG, "input tvr: " + tvr);
                	Log.d(TAG, "input tsi: " + tsi);
                	
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
		/*String formattedString = BulletListBuilder.getBulletList("TVR Results", tvrResult) + "\n" 
				+ BulletListBuilder.getBulletList("TSI Results", tsiResult);*/
		
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
        
		/*resultView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "touch detected!");
				return false;
			}
		});*/
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ABOUT_ID, 0, R.string.menu_about).setIcon(android.R.drawable.ic_menu_info_details).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()) {
		case ABOUT_ID:
			mAboutDialog.show();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
}
