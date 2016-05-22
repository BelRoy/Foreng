package com.bookleron.foreng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 * Created by Андрей on 30.04.2016.
 */
public class CutImage extends Activity {
    int LayerId;
    ImageView Image;
    int x, y;
    int x2, y2;
    Mat imageMat;
    Bitmap imageBitmap;
    int TouchX, TouchY;
    int RealWidth, RealHeight;
    int Width, Height;
    float Rwidth, Rheight;
    float RectWidth=150;
    float RectHeight = 150;
    final int CHANGE_POS = 1;
    Paint paint;
    final int CHANGE_SIZE = 2;
    public int CurrentMode = 1;
    int a_t, b_t, c_t, d_t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        CurrentMode = CHANGE_POS;
        setContentView(R.layout.cut_image);
        Image = (ImageView) findViewById(R.id.imageViewCut);

        Intent intent = getIntent();
        LayerId = intent.getIntExtra("LayerId", 0);
        imageMat = LayerList.GetLayer(LayerId);
        imageBitmap =  Bitmap.createBitmap(imageMat.cols(),imageMat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, imageBitmap);
        RealWidth = imageBitmap.getWidth();
        RealHeight = imageBitmap.getHeight();

        Canvas canvas = new Canvas(imageBitmap);
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawBitmap(imageBitmap, new Matrix(), null);
        int[] c = new int[2];
        Image.getLocationInWindow(c);
        Image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(CurrentMode == CHANGE_POS) {
                        TouchX = (int) event.getX();
                        TouchY = (int) event.getY();
                    }
                    if(CurrentMode == CHANGE_SIZE){
                        RectWidth = (int) event.getX();
                        RectHeight = (int) event.getY();
                    }
                    UpdateImage();
                }
                return true;
            }
        });

        final Button change = (Button) findViewById(R.id.changeButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CurrentMode == CHANGE_POS){
                    CurrentMode = CHANGE_SIZE;
                    change.setText("Изменить положение");

                }
                else{
                    CurrentMode = CHANGE_POS;
                    change.setText("Изменить размер");

                }
                Log.e("RWIDTH",""+CurrentMode);
            }
        });
        canvas.drawRect(c[0], c[1], 150, 150, paint);
     //   canvas.drawLine(c[0],c[0], );
        Image.setImageBitmap(imageBitmap);

  //      leftCut.setLeft(Image.getLeft());
  //      leftCut.setTop(Image.getTop());



        Image.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int coor[] = {0,0};
                Image.getLocationInWindow(coor);
                //x = Image.getLeft();
                //y = Image.getTop();
                x = coor[0];
                y = coor[1];
                Width = Image.getMeasuredWidth();
                Height = Image.getMeasuredHeight();
                Rwidth = (float)RealWidth/Width;
                Rheight = (float)RealHeight/Height;
                float k = (RealHeight+RealWidth)/(Width+Height);
                paint.setStrokeWidth(Math.round(k)+1);

                Log.e("REAL WIDTH", "" + RealWidth + " " + RealHeight + " " + paint.getStrokeWidth());
                Log.e("RWIDTH:", "" + Rwidth + " " + Rheight);

                Image.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }
    public void SaveResult(View view){
        imageMat = LayerList.GetLayer(LayerId);
        Utils.matToBitmap(imageMat, imageBitmap);
        Bitmap temp = Bitmap.createBitmap(imageBitmap,  Math.round((TouchX-x)*Rwidth),Math.round((TouchY-y)*Rheight),
                                            Math.round((RectWidth)*Rwidth), Math.round((RectHeight)*Rheight));

        LayerList.ChangeLayer(LayerId, temp);

        finish();
    }

    public void UpdateImage(){
        Utils.matToBitmap(imageMat, imageBitmap);
        Canvas canvas = new Canvas(imageBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawBitmap(imageBitmap, new Matrix(), null);
   //     canvas.drawRect(TouchX, TouchY, 150, 150, paint);
        int a = Math.round((TouchX-x)*Rwidth);
   //     if(a< 0)
   //         a = 0;
        int b = Math.round((TouchY-y)*Rheight);
    //    if(b<0)
    //        b = 0;
        int c = Math.round((TouchX-x+RectWidth)*Rwidth);
    //    if(c > imageBitmap.getWidth())
      //      c = imageBitmap.getWidth();
        int d = Math.round((TouchY-y+RectHeight)*Rheight);
     //   if(d> imageBitmap.getHeight())
     //       d = imageBitmap.getHeight();
        if(a>=0 || b>=0 || c <= imageBitmap.getWidth() || d<= imageBitmap.getHeight()) {
            canvas.drawRect(a, b, c, d, paint);
            a_t = a;
            b_t = b;
            c_t = c;
            d_t = d;
        }
        else
            canvas.drawRect(a_t, b_t, c_t, d_t, paint);
      //  canvas.drawRect((TouchX-x)*Rwidth,(TouchY-y)*Rheight,(TouchX-x+RectWidth)*Rwidth, (TouchY-y+RectHeight)*Rheight,paint);
        Image.setImageBitmap(imageBitmap);
    }

}
