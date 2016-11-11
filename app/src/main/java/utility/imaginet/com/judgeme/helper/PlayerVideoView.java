package utility.imaginet.com.judgeme.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by IM0033 on 2/15/2016.
 */
public class PlayerVideoView extends VideoView {
    private int mForceHeight = 0;
    private int mForceWidth = 0;
    public PlayerVideoView(Context context) {
        super(context);
    }

    public PlayerVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayerVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDimensions(int w, int h) {
        this.mForceHeight = h;
        this.mForceWidth = w;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mForceWidth, mForceHeight);
    }
}
