package com.hhxh.xijiu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

/**
 * @auth lijq
 * @date 2016/10/11
 */
public class BitmapUtil {
    /** 最大加载进内存大小 */
    public final static int MAX_SIZE_LOADINMEMORY = 480 * 480;
    /**
     * 获取最大加载进内存的大图对象
     *
     * @param context 上下文对象
     * @return bitmap
     */
    public static Bitmap getSuitableBigBitmap(Context context, int id) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int maxNumOfPixels = Math.min(MAX_SIZE_LOADINMEMORY,
                displayMetrics.widthPixels * displayMetrics.heightPixels);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(),id, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
        opts.inJustDecodeBounds = false;
        try {
           return BitmapFactory.decodeResource(context.getResources(),id, opts);
        } catch (OutOfMemoryError err) {
            return null;
        }
    }

    /**
     * 获取压缩值
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeSampleSize(BitmapFactory.Options options,
                                         int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
    /**
     * 计算初始采样大小
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
