package csci567.foodfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.Firebase;

/**
 * Created by bradley on 5/16/16.
 * modified from http://stackoverflow.com/questions/4139288/android-how-to-handle-right-to-left-swipe-gestures
 */
public class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;
    public static final String TAG = "Swipe Handling: ";
    private Restaurant m_rest;
    private Context m_context;

    public OnSwipeTouchListener(Context context, Restaurant rest) {
        gestureDetector = new GestureDetector(context, new GestureListener());
        m_rest = rest;
        m_context = context;
    }

    public void onSwipeDown() {
        Log.i(TAG, "SWIPED DOWN");

        new Database().add_dislike(m_rest);
    }

    public void onSwipeUp() {
        Log.i(TAG, "SWIPED UP");

        new Database().add_like(m_rest);

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr=" +
                        m_rest.getM_lat() + "," + m_rest.getM_lng()));
        m_context.startActivity(intent);

    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeUp();
                else
                    onSwipeDown();
                return true;
            }
            return false;
        }
    }
}