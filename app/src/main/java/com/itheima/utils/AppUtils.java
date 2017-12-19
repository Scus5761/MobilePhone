package com.itheima.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by ThinkPad on 2017-06-07.
 */

public class AppUtils {

    public static long getAvaiableSD() {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long blockCounts = stat.getBlockCount();
            long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }


    public static long getAvaiableROM() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long blockCounts = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();
        return blockSize * availableBlocks;
    }
}
