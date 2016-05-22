package com.bookleron.foreng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collection;

/**
 * Created by Андрей on 10.01.2016.
 */
public class ImageEffect extends Activity{
    ImageView ImagePreview;
    Mat ImageMat;
    Bitmap Image;
    int LayerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.effect_image);
        Intent intent = getIntent();
        ImagePreview = (ImageView) findViewById(R.id.imagePreview);

      //  Mat c;
       // c = new Mat();
        LayerId = intent.getIntExtra("ImageId", 0);
        ImageMat = new Mat();
        ImageMat = LayerList.GetLayer(intent.getIntExtra("ImageId",0));
        Image = Bitmap.createBitmap(ImageMat.cols(),ImageMat.rows(), Bitmap.Config.ARGB_8888);
   //     Utils.matToBitmap(ImageMat,Image);
   //     ImagePreview.setImageBitmap(Image);
        UpdateImagePreview();
    }
    public void blurFilter(double param){
        int kernelSize = 3;
        Mat kernel;
        kernel = Mat.ones(kernelSize,kernelSize, CvType.CV_32F);
        Scalar c = new Scalar(1/9);
        Core.multiply(kernel, c, kernel);
   //     Utils.bitmapToMat(image, currentMat);
        if(param == 0)  param =1;
        //   Core.divide(5, kernel, kernel);
        //  Imgproc.filter2D(currentMat, outMat, -1, kernel2);
        //   Core.multiply(currentMat, c, outMat);
        Imgproc.blur(ImageMat, ImageMat, new Size(param, param));
   //     Utils.matToBitmap(outMat, image);
    //    currentImage.setImageBitmap(image);
        UpdateImagePreview();

    }
    public void EdgeFilter(View view) {
        Mat kernel;
        //Utils.bitmapToMat(image, currentMat);
        int kernelSize = 3;
        kernel = Mat.ones(kernelSize,kernelSize,CvType.CV_8U);
        //    kernel.put(0,0,1/3);
        //   kernel.put(0,1,1/3);
        //  Scalar c = new Scalar(1/9);
        // Core.multiply(kernel, c, kernel);
        kernel.put(0, 0, (float) 0);
        kernel.put(0, 1, (float) 0);
        kernel.put(0, 2, (float) 0);
        kernel.put(1, 0, (float) 1/3);
        kernel.put(1, 1, (float) 0);
        kernel.put(1, 2, (float) 2);
        kernel.put(2, 0, (float) 1/3);
        kernel.put(2, 1, (float) 0);
        kernel.put(2, 2, (float) 2);
        Imgproc.erode(ImageMat, ImageMat, kernel);
        UpdateImagePreview();
       // Utils.matToBitmap(outMat, image);
       // currentImage.setImageBitmap(image);
    }
    public void BlackAndWhite(View view){
        Imgproc.cvtColor(ImageMat,ImageMat,Imgproc.COLOR_RGB2GRAY);

        UpdateImagePreview();
    }
    public void ColorBalance(View view){
        Mat temp;
        temp = new Mat(ImageMat.rows(),ImageMat.cols(),ImageMat.type());
        int x1 = 255/5;
        int y1 = 255/150;
        int z1 = 255/89;

        byte temp1[] = new byte[(int)(ImageMat.total() * ImageMat.channels())];

        ImageMat.get(0,0,temp1);
        for(int i =0; i< temp1.length; i++){
            temp1[i] =(byte)(temp1[i]*2);
        }
        ImageMat.put(0, 0, temp1);
/*        for (int y = 0; y < ImageMat.rows(); y++) {
            for (int x = 0; x < ImageMat.cols(); x++) {
                double[] data;
                data = ImageMat.get(y, x);
                data[0] = x1*data[0];
                data[1] = y1*data[1];
                data[2] = z1*data[2];
                ImageMat.put(y, x, data);
            }
        }
*/
        UpdateImagePreview();
    }
    public void UpdateImagePreview(){
        Utils.matToBitmap(ImageMat, Image);
        ImagePreview.setImageBitmap(Image);
    }
    public void SaveImage(View view){
        LayerList.ChangeLayer(LayerId, ImageMat);
        finish();

    }
    public void SetPos(View view){
        LayerList.SetTempMat(ImageMat);
        Intent goToMove = new Intent(this,LayerTransform.class);
        goToMove.putExtra("LayerId", LayerId);
        startActivity(goToMove);
    }
    public void BlurFilter(View view) {

        ShowDialogBlur();
    }
    public void BrightnessFilter(View view){
  //      Scalar scalar = new Scalar(255,255,255);
 //       Core.multiply(ImageMat,scalar,ImageMat);
  //      Core.add(ImageMat, scalar, ImageMat);
 //       UpdateImagePreview();
        ShowDialogBrightness();
    }
    public void BrightnessChange(int param){
        Scalar scalar = new Scalar(param,param,param);
        Core.add(ImageMat, scalar, ImageMat);
        UpdateImagePreview();
    }
    public void ResetImage(View view){
        ImageMat = LayerList.GetLayer(LayerId);
        UpdateImagePreview();
    }
    public void Contrast(View view){
        ShowDialogContrast();
    }
    public void ContrastChange(float param){
        ImageMat.convertTo(ImageMat,-1,param+1,0);
        UpdateImagePreview();
    }
    public void ShowDialogContrast(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Контраст");
        View seek = getLayoutInflater().inflate(R.layout.brightness_seek,null);

        final SeekBar seekBar = (SeekBar) seek.findViewById(R.id.seekBarBrightness);
        final TextView textBar = (TextView) seek.findViewById(R.id.textViewBrightness);
        seekBar.setMax(200);
        seekBar.setProgress(100);

        dialog.setPositiveButton("Применить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContrastChange((seekBar.getProgress()-100)/100f);
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textBar.setText(String.valueOf(seekBar.getProgress()-100));
            }
        });
        dialog.setView(seek);
        dialog.create();
        dialog.show();
    }
    public void ShowDialogBrightness(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Яркость");
        View seek = getLayoutInflater().inflate(R.layout.brightness_seek,null);

        final SeekBar seekBar = (SeekBar) seek.findViewById(R.id.seekBarBrightness);
        final TextView textBar = (TextView) seek.findViewById(R.id.textViewBrightness);
        dialog.setPositiveButton("Применить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BrightnessChange(seekBar.getProgress()-255);
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textBar.setText(String.valueOf(seekBar.getProgress()-255));
            }
        });
        dialog.setView(seek);
        dialog.create();
        dialog.show();
    }
    public void GoToCut(View view){
        LayerList.SetTempMat(ImageMat);
        Intent goToMove = new Intent(this,CutImage.class);
        goToMove.putExtra("LayerId", LayerId);
        startActivity(goToMove);
    }
    public void ShowDialogBlur(){
        //     Mat prevImage = currentMat;

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Параметры размытия");
        View seek = getLayoutInflater().inflate(R.layout.blur_seek,null);

        final SeekBar seekBar = (SeekBar) seek.findViewById(R.id.seekBar);
        final TextView textBar = (TextView) seek.findViewById(R.id.textBarSeek);
        dialog.setPositiveButton("Применить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                blurFilter(seekBar.getProgress());
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textBar.setText(String.valueOf(seekBar.getProgress()));
            }
        });
        dialog.setView(seek);
        dialog.create();
        dialog.show();
        //    ProgressDialog dialog = new ProgressDialog(this);

    }
}

