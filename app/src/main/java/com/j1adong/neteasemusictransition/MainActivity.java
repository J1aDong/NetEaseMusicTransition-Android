package com.j1adong.neteasemusictransition;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawableFadeDisplayer drawableFadeDisplayer;
    private ImageView ivRoot;
    private Button btnChange;

    int[] mitmaps = new int[]{R.mipmap.qilixiang, R.mipmap.quxunzhao, R.mipmap.xiayigetianliang};

    String[] netMipmaps = new String[]{
            "http://img.xiami.net/images/album/img60/1260/66501387132591.jpg",
            "http://img.xiami.net/images/album/img42/30642/3276891387791987.jpg",
            "http://img.xiami.net/images/album/img78/1578/1684311383294026.jpg"
    };

    private int i = 0;
    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;
    private Allocation mBlurInput;
    private Allocation mBlurOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivRoot = (ImageView) findViewById(R.id.iv_root);
        btnChange = (Button) findViewById(R.id.btn_change);

        StatusBarUtil.setTranslucentForImageView(this, 50, btnChange);

        btnChange.setOnClickListener(this);

        drawableFadeDisplayer = new DrawableFadeDisplayer(500);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.btn_change:
//                i++;
//                int scaleRatio = 10;
//                int blurRadius = 8;
//                Bitmap originBitmap = BitmapFactory.decodeResource(getResources(), mitmaps[i % 3]);
//                Bitmap scaledBitmp = Bitmap.createScaledBitmap(originBitmap, originBitmap.getWidth() / scaleRatio, originBitmap.getHeight() / scaleRatio, false);
//                Bitmap blurBitmap = FastBlurUtil.doBlur(scaledBitmp, blurRadius, false);
//                drawableFadeDisplayer.display(blurBitmap, ivRoot);
//                break;
            case R.id.btn_change:
                i++;
                final int scaleRatio = 10;
                final int blurRadius = 8;

                Glide.with(getApplicationContext()).load(netMipmaps[i % 3]).asBitmap().fitCenter().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap originBitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap scaledBitmap = Bitmap.createScaledBitmap(originBitmap, originBitmap.getWidth() / scaleRatio, originBitmap.getHeight() / scaleRatio, false);
                        Bitmap mBitmapToBlur = scaledBitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Bitmap mBlurredBitmap = Bitmap.createBitmap(mBitmapToBlur.getWidth(), mBitmapToBlur.getHeight(),
                                Bitmap.Config.ARGB_8888);

                        mRenderScript = RenderScript.create(MainActivity.this);
                        mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));

                        mBlurInput = Allocation.createFromBitmap(mRenderScript, mBitmapToBlur,
                                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
                        mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput.getType());

                        mBlurInput.copyFrom(mBitmapToBlur);
                        mBlurScript.setRadius(blurRadius);
                        mBlurScript.setInput(mBlurInput);
                        mBlurScript.forEach(mBlurOutput);
                        mBlurOutput.copyTo(mBlurredBitmap);

                        drawableFadeDisplayer.display(mBlurredBitmap, ivRoot);
                        scaledBitmap.recycle();
                        mBitmapToBlur.recycle();
                        mRenderScript.destroy();
                    }
                });
                break;
        }
    }
}
