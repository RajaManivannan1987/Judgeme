package utility.imaginet.com.judgeme.helper;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by IM0033 on 3/16/2016.
 */
public class Utils {
    private static Handler mainHandler;

    static {
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUiThread(Runnable runnable) {
        if (isUiThread()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }

    public static boolean isUiThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return true;
        }
        return false;
    }
}
