package se.blunden.tvrdecoder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

/**
 * Custom TextView that animates into view when added.
 */
public class CardTextView extends TextView {

	public CardTextView(Context context) {
		super(context);
		
	}
	
	public CardTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CardTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
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
}
