package utility.imaginet.com.judgeme.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by Sanjay on 12/9/15.
 */
public class Upload_CircleSurfaceView extends SurfaceView {

    private Path clipPath;

    public Upload_CircleSurfaceView(Context context) {
        super(context);
        inside();
    }

    public Upload_CircleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inside();
    }

    public Upload_CircleSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inside();
    }

    private void inside(){
        clipPath = new Path();
        clipPath.addCircle(180, 180, 185, Path.Direction.CCW);
       //clipPath.addCircle(250, 300, 250, 300);
        clipPath.setFillType(Path.FillType.WINDING);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.clipPath(clipPath);
        super.dispatchDraw(canvas);
    }
}
