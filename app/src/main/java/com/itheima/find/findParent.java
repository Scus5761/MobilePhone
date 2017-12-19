package com.itheima.find;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by ThinkPad on 2017-05-23.
 */

    public abstract class findParent extends Activity{
    public  GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        detector = new GestureDetector(new MyGestureListener());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startX = e1.getRawX();
            float endX = e2.getRawX();
            float startY = e1.getRawY();
            float endY = e2.getRawY();
            if (Math.abs(startY - endY) > 50) {
                Toast.makeText(getApplicationContext(), "小主不要乱滑了...", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (startX - endX > 100) {
                next_activity();
            } else if (endX - startX > 100) {
                pre_activity();
            }
            return true;
        }
    }

    public void next(View v) {
        next_activity();
    }

    public void previous(View view) {
        pre_activity();
    }

    public abstract void next_activity();

    public abstract void pre_activity();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pre_activity();

        }
        return super.onKeyDown(keyCode, event);
    }

}
