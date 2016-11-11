package utility.imaginet.com.judgeme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.SimpleGestureFilter;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class HelperActivity extends FragmentActivity implements SimpleGestureFilter.SimpleGestureListener{
    private SimpleGestureFilter detector;
   private Button nextupcloseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.helper_activity);
        detector = new SimpleGestureFilter(HelperActivity.this, this);
        nextupcloseButton= (Button) findViewById(R.id.nextupcloseButton);
        nextupcloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";
        switch (direction){
            case SimpleGestureFilter.SWIPE_RIGHT:
                str = "Swipe Right";
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP:
                str = "Swipe Up";
                break;
        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {

    }
}
