package com.j1adong.neteasemusictransition;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

/**
 * Created by J1aDong on 16/7/19.
 */
public class DrawableFadeDisplayer {

    private final int durationMillis;

    public DrawableFadeDisplayer(int durationMillis) {
        this.durationMillis = durationMillis;
    }

    /**
     * @param bitmap
     * @param imageView
     */
    public void display(Bitmap bitmap, ImageView imageView) {
        Drawable oldDrawable = imageView.getDrawable();
        Drawable oldBitmapDrawable = null;
        //如果原先的imageView没drawable就创建一个透明的drawable
        if (null == oldDrawable) {
            oldBitmapDrawable = new ColorDrawable(Color.TRANSPARENT);
        }
        //如果原先就是TransitionDrawable,就获得第二张图片
        else if (oldDrawable instanceof TransitionDrawable) {
            oldBitmapDrawable = ((TransitionDrawable) oldDrawable).getDrawable(1);
        } else {
            oldBitmapDrawable = oldDrawable;
        }
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                oldBitmapDrawable,
                new BitmapDrawable(Resources.getSystem(), bitmap)
        });
        imageView.setImageDrawable(td);
        td.startTransition(durationMillis);
    }
}
