package widget.com.example.kanatip.applicationproj;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.IBinder;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.nio.ByteBuffer;


public class FloatingWidgetShowService extends Service  {


    WindowManager windowManager;
    View floatingView, collapsedView, expandedView;
    WindowManager.LayoutParams params ;
    public static int REQUEST_MEDIA_PROJECTION = 4567;
    private MediaProjectionManager mMediaProjectionManager;
    private boolean isDetecting = false;
    public boolean isOverlay = false;
    private int displayWidth;
    private int mDisplayHeight;
    private int mDensityDpi;
    public int mResultCode;
    public Intent mResultData;
    public MediaProjection mMediaProjection;
    private ImageReader mImageReader;
    public VirtualDisplay mVirtualDisplay;

//    private TextView textHelp;
//
//    private RelativeLayout rlOverlay;

    public FloatingWidgetShowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        throw new UnsupportedOperationException("Not yet implemented");
    }



    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

        try {
            mResultCode = intent.getIntExtra("mResultCode", 0);
            mResultData = intent;
        }catch (Exception e){
            stopService(new Intent(FloatingWidgetShowService.this, FloatingWidgetShowService.class));
        }

        Log.d("test","on start");
        if(startId == Service.START_STICKY) {
            onCreate();
            return super.onStartCommand(intent, flags, startId);
        }else{
            return  Service.START_NOT_STICKY;

        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("test", "onCreate()");

        floatingView = LayoutInflater.from(this).inflate(R.layout.floating_widget_layout, null);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        windowManager.addView(floatingView, params);

        expandedView = floatingView.findViewById(R.id.Layout_Expended);

        collapsedView = floatingView.findViewById(R.id.Layout_Collapsed);


        floatingView.findViewById(R.id.Widget_Close_Icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopSelf();

            }
        });

        expandedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
//                rlOverlay.setVisibility(View.VISIBLE);


            }
        });

        floatingView.findViewById(R.id.MainParentRelativeLayout).setOnTouchListener(new View.OnTouchListener() {
            int X_Axis, Y_Axis;
            float TouchX, TouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        X_Axis = params.x;
                        Y_Axis = params.y;

                        //get the touch location
                        TouchX = event.getRawX();
                        TouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:

                        collapsedView.setVisibility(View.GONE);
                        expandedView.setVisibility(View.VISIBLE);
                        floatwidget_click();
                        return true;

                    case MotionEvent.ACTION_MOVE:

                        params.x = X_Axis + (int) (event.getRawX() - TouchX);
                        params.y = Y_Axis + (int) (event.getRawY() - TouchY);
                        windowManager.updateViewLayout(floatingView, params);
                        return true;
                }
                return false;
            }
        });
    }


    private void floatwidget_click(){
        Log.d("test","click on floating widget"+" Overlay: "+isOverlay+" Detect: "+isDetecting);

        if(isOverlay){
            isOverlay = false;

            //just added and haven't test it yet
            if (isDetecting) {
                isDetecting = false;
            }

            //change from floating widget to overlay Class
            stopService(new Intent(FloatingWidgetShowService.this, OverlayService.class));

        }


//            DisplayMetrics metrics = getResources().getDisplayMetrics();
//            mDensityDpi = metrics.densityDpi;
//            displayWidth = metrics.widthPixels;
//            mDisplayHeight = metrics.heightPixels;
//            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//            mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//            mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
//            mImageReader = ImageReader.newInstance(displayWidth, mDisplayHeight, ImageFormat.RGB_565, 2);
//            mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture", displayWidth, mDisplayHeight, mDensityDpi,
//                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
//            android.media.Image image = null;
//
//
//            while (image == null) {
//                image = mImageReader.acquireLatestImage();
//            }
//
//            final android.media.Image.Plane[] planes = image.getPlanes();
//            final ByteBuffer buffer = planes[0].getBuffer();
//            int offset = 0;
//            int pixelStride = planes[0].getPixelStride();
//            int rowStride = planes[0].getRowStride();
//            int rowPadding = rowStride - pixelStride * displayWidth;
//            String ImagePath;
//            Uri URI;
//
//            Bitmap bmp = Bitmap.createBitmap(displayWidth + rowPadding / pixelStride, mDisplayHeight, Bitmap.Config.RGB_565);
//            bmp.copyPixelsFromBuffer(buffer);
//
//            image.close();
//            mVirtualDisplay.release();
//            mVirtualDisplay = null;
//            mMediaProjection.stop();
//
//
////          feature = new Feature();
////          feature.setType(visionAPI[0]);
////          feature.setMaxResults(50);
//
//            Bitmap sc_bmp = Bitmap.createScaledBitmap(bmp,bmp.getWidth()/2,bmp.getHeight()/2,false);
//
////          callCloudVision(sc_bmp, feature);
//
//            image = null;





    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) windowManager.removeView(floatingView);
    }


}
