package se.blunden.tvrdecoder;

import java.util.ArrayList;

import se.blunden.tvrdecoder.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "TVRDecoder";
	
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
                	
                	displayOutput();
                }
            });
        
	}
	
	public void displayOutput() {

		final LinearLayout layout = (LinearLayout) findViewById(R.id.now_layout);
		
		final TextView resultView = new CardTextView(new ContextThemeWrapper(this, R.style.nowCardStyle));
		String formattedString = BulletListBuilder.getBulletList("TVR Results", tvrResult) + "\n" 
				+ BulletListBuilder.getBulletList("TSI Results", tsiResult);
		
		resultView.setText(formattedString);
		resultView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
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
		
		//-----
		/*View layout = findViewById(R.id.now_layout);
		
		TextView resultView = new CardTextView(new ContextThemeWrapper(this, R.style.nowCardStyle));
		String formattedString = BulletListBuilder.getBulletList("TVR Results", tvrResult) + "\n" 
				+ BulletListBuilder.getBulletList("TSI Results", tsiResult);
		
		resultView.setText(formattedString);
		resultView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		((LinearLayout) layout).addView(resultView);*/
	}

}
