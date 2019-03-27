package widget.com.example.kanatip.applicationproj;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class OverlayService extends Service {

    public OverlayService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }

    RelativeLayout oView;
    WindowManager wm;

    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }


    @SuppressLint("ClickableViewAccessibility")
    public void onCreate() {
        super.onCreate();


        oView = new RelativeLayout(this);
        oView.setBackgroundColor(0xA6000000); // The translucent black color

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                0 | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);


        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        wm.addView(oView, params);

        oView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("thhh", "TOUCHHH");

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:

                        wm.removeView(oView);
//                        wm.removeView(textv);

                        break;
                    default:
                        break;
                }
                return true;

            }

        });



    }

    public void onDestroy() {
        super.onDestroy();
//        if(oView!=null){
//            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//            wm.removeView(oView);
//            wm.removeView(textv);
//        }
    }
}
