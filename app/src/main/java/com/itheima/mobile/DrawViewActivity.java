package com.itheima.mobile;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ThinkPad on 2017-05-31.
 */

public class DrawViewActivity extends Activity {

    private LinearLayout ll_draw_toast;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private WindowManager manager;
    private int widthPixels;
    private int heightPixels;
    private long[] mHits;
    private TextView draw_toast_bottom;
    private TextView draw_toast_top;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawview);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        ll_draw_toast = (LinearLayout) findViewById(R.id.ll_draw_toast);
        draw_toast_top = (TextView) findViewById(R.id.draw_toast_top);
        draw_toast_bottom = (TextView) findViewById(R.id.draw_toast_bottom);

        int x = sp.getInt("X", 60);
        int y= sp.getInt("Y", 60);
       /* int r = x + ll_draw_toast.getWidth();
        int b = y + ll_draw_toast.getHeight();
        ll_draw_toast.layout(x,y,r,b);*/

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_draw_toast.getLayoutParams();
        params.leftMargin = x;
        params.topMargin=y;
        ll_draw_toast.setLayoutParams(params);
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        heightPixels = metrics.heightPixels;
        widthPixels = metrics.widthPixels;
        if (y > heightPixels / 2) {
            draw_toast_bottom.setVisibility(View.INVISIBLE);
            draw_toast_top.setVisibility(View.VISIBLE);
        } else {
            draw_toast_bottom.setVisibility(View.VISIBLE);
            draw_toast_top.setVisibility(View.INVISIBLE);
        }
        onTouch();
        DoubleClick();
        mHits = new long[2];

    }

    private void DoubleClick() {
        ll_draw_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - 300)) {

                    int t = (heightPixels - ll_draw_toast.getHeight() - 25) / 2;
                    int l = (widthPixels - ll_draw_toast.getWidth()) / 2;
                    ll_draw_toast.layout(l, t, l + ll_draw_toast.getWidth(), t + ll_draw_toast.getHeight());
                    editor.putInt("X", l);
                    editor.putInt("Y", t);
                    editor.commit();
                }
            }
        });

    }

    private void onTouch() {
        ll_draw_toast.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                        case MotionEvent.ACTION_MOVE:
                            int newX = (int) event.getRawX();
                            int newY = (int) event.getRawY();
                            int dX = newX - startX;
                            int dY = newY - startY;
                            int l = ll_draw_toast.getLeft();
                            int t = ll_draw_toast.getTop();
                            l += dX;
                            t += dY;
                            int r = l + ll_draw_toast.getWidth();
                            int b = t + ll_draw_toast.getHeight();

                            if (l < 0 || r > widthPixels || t < 0 || b > heightPixels - 25) {
                                break;
                            }
                            ll_draw_toast.layout(l, t, r, b);
                            int top = ll_draw_toast.getTop();
                            if (top > heightPixels / 2) {
                                draw_toast_bottom.setVisibility(View.INVISIBLE);
                                draw_toast_top.setVisibility(View.VISIBLE);
                            } else {
                                draw_toast_bottom.setVisibility(View.VISIBLE);
                                draw_toast_top.setVisibility(View.INVISIBLE);
                            }
                            startX = newX;
                            startY=newY;
                            break;

                        case MotionEvent.ACTION_UP:
                            int X = ll_draw_toast.getLeft();
                            int Y= ll_draw_toast.getTop();
                            editor = sp.edit();
                            editor.putInt("X", X);
                            editor.putInt("Y", Y);
                            editor.commit();
                            break;
                }
                return false;
            }

        });

    }
}
