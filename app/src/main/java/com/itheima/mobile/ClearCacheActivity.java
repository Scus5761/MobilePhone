package com.itheima.mobile;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.itheima.fragment.CacheFragment;
import com.itheima.fragment.SDFragment;


/**
 * Created by ThinkPad on 2017-06-14.
 */

public class ClearCacheActivity extends FragmentActivity {
    private android.support.v4.app.FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clearcache);

        CacheFragment cacheFragment = new CacheFragment();
        SDFragment sdFragment = new SDFragment();
        fragmentManager = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction fragmentTransaction; fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frag_cache_fragment, cacheFragment);
        fragmentTransaction.commit();

    }

    public void cache(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_cache_fragment, new CacheFragment());
        fragmentTransaction.commit();
    }

    public void sd(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frag_cache_fragment, new SDFragment());
        fragmentTransaction.commit();
    }
}
