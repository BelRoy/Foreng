package com.bookleron.foreng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.SeekBar;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Андрей on 22.01.2016.
 */
public class LayerTransform extends Activity{
    ImageView currentImage;
    SeekBar alphaSeek;
    int LayerId;
    int AlphaChannel;
    int TouchX;
    int TouchY;
    int x, y;
    int RealWidth, RealHeight;
    int Width, Height;
    float Rwidth, Rheight;
    int width;
    int height;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layers_position);
        currentImage = (ImageView) findViewById(R.id.imageView3);
        alphaSeek = (SeekBar) findViewById(R.id.alphaSeek);
        Intent intent = getIntent();
        LayerId = intent.getIntExtra("LayerId", 0);

        currentImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int coor[] = {0, 0};
                currentImage.getLocationInWindow(coor);
                //x = Image.getLeft();
                //y = Image.getTop();
                x = coor[0];
                y = coor[1];
                Width = currentImage.getMeasuredWidth();
                Height = currentImage.getMeasuredHeight();
                Rwidth = (float) RealWidth / Width;
                Rheight = (float) RealHeight / Height;
                float k = (RealHeight + RealWidth) / (Width + Height);


                currentImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        currentImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {

                    TouchX = (int) event.getX();
                    TouchY = (int) event.getY();

                    //  UpdateImage(X, Y);
                    UpdateImage(Math.round((TouchX - x) * Rwidth), Math.round((TouchY - y) * Rheight));
                }
                return true;
            }
        });

        AlphaChannel = LayerList.LayerPointXY.get(LayerId).alpha;
        alphaSeek.setProgress(AlphaChannel);
        alphaSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AlphaChannel = alphaSeek.getProgress();
                UpdateImage( Math.round((TouchX-x)*Rwidth),Math.round((TouchY-y)*Rheight));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        UpdateImage();
    }
    public void UpdateImage(){
        if(LayerList.GetCount() >0) {
            Mat render;
            render = new Mat();
        //    render = LayerList.RenderImage();
            render = LayerList.RenderImagePrev(LayerId, LayerList.LayerPointXY.get(LayerId).x, LayerList.LayerPointXY.get(LayerId).y,AlphaChannel);
            Bitmap temp = Bitmap.createBitmap(render.cols(), render.rows(), Bitmap.Config.ARGB_8888);
            RealWidth = temp.getWidth();
            RealHeight = temp.getHeight();
            Utils.matToBitmap(render, temp);
            width = temp.getWidth();
            height = temp.getHeight();
            currentImage.setImageBitmap(temp);

        }
    }
    public void UpdateImage(int x,int y){
        if(LayerList.GetCount() >0) {
            Mat render;
            render = new Mat();
        //    render = LayerList.RenderImage(1,x,y);
            render = LayerList.RenderImagePrev(LayerId,x,y,AlphaChannel);
            Bitmap temp = Bitmap.createBitmap(render.cols(), render.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(render, temp);
            currentImage.setImageBitmap(temp);
        }
    }
    public void TransformButton(View view){
        registerForContextMenu(view);

    }
    public void SaveTransform(View view){
        LayerList.LayerPointXY.get(LayerId).x =  Math.round((TouchX-x)*Rwidth);
        LayerList.LayerPointXY.get(LayerId).y =  Math.round((TouchY-y)*Rheight);
        LayerList.LayerPointXY.get(LayerId).alpha = AlphaChannel;
        finish();
    }
}
