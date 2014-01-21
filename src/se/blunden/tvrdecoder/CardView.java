package se.blunden.tvrdecoder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Custom ViewGroup that animates into view when added.
 */
public class CardView extends LinearLayout {

	private TextView tvrHeaderView;
	private TextView tvrView;
	private TextView tsiHeaderView;
	private TextView tsiView;
	
	public CardView(Context context) {
		super(context);
		initialize(context);
	}
	
	public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}
	
	public CardView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	@Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_left);
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setFillAfter(true);
        this.startAnimation(anim);
    }
	
	public void setTvrHeaderText(String tvrheader) {
		if(tvrheader.equals("")) {
			tvrHeaderView.setVisibility(GONE); // Should the view be removed instead?
		} else {
			tvrHeaderView.setText(tvrheader);
		}
	}
	
	public void setTvrText(String tvr) {
		
		if(tvr.equals("")) {
			tvrHeaderView.setVisibility(GONE);
			tvrView.setVisibility(GONE); // Should the view be removed instead?
		} else {
			tvrView.setText(tvr);
		}
	}
	
	public void setTsiHeaderText(String tsiheader) {
		if(tsiheader.equals("")) {
			tsiHeaderView.setVisibility(GONE); // Should the view be removed instead?
		} else {
			tsiHeaderView.setText(tsiheader);
		}
	}
	
	public void setTsiText(String tsi) {
		if(tsi.equals("")) {
			tsiHeaderView.setVisibility(GONE);
			tsiView.setVisibility(GONE); // Should the view be removed instead?
		} else {
			tsiView.setText(tsi);
		}
	}

	public TextView getTvrView() {
		return tvrView;
	}

	public TextView getTsiView() {
		return tsiView;
	}
	
	private void initialize(Context context) {
		// Inflate the card layout
		LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(R.layout.now_card, this, true);
		
		// Save a reference to the different TextViews
		tvrHeaderView = (TextView)findViewById(R.id.card_tvr_header);
		tvrView = (TextView)findViewById(R.id.card_tvr_text);
		tsiHeaderView = (TextView)findViewById(R.id.card_tsi_header);
		tsiView = (TextView)findViewById(R.id.card_tsi_text);
	}
}
