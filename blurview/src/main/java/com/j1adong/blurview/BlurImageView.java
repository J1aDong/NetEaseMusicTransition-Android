package com.j1adong.blurview;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by J1aDong on 16/7/21.
 */
public class BlurImageView extends ImageView {

    private final DrawableFadeDisplayer drawableFadeDisplayer;

    int scaleRatio = 10;
    int blurRadius = 8;
    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;
    private Allocation mBlurInput;
    private Allocation mBlurOutput;

    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlurImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawableFadeDisplayer = new DrawableFadeDisplayer(500);
    }

    public void blur(Bitmap originBitmap) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap, originBitmap.getWidth() / scaleRatio, originBitmap.getHeight() / scaleRatio, false);
        Bitmap mBitmapToBlur = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap mBlurredBitmap = Bitmap.createBitmap(mBitmapToBlur.getWidth(), mBitmapToBlur.getHeight(),
                Bitmap.Config.ARGB_8888);

        mRenderScript = RenderScript.create(getContext());
        mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));

        mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());

        mBlurInput.copyFrom(mBitmapToBlur);
        mBlurScript.setRadius(blurRadius);
        mBlurScript.setInput(mBlurInput);
        mBlurScript.forEach(mBlurOutput);
        mBlurOutput.copyTo(mBlurredBitmap);

        drawableFadeDisplayer.display(mBlurredBitmap, this);
        scaledBitmap.recycle();
        mBitmapToBlur.recycle();
        mRenderScript.destroy();
    }
}
