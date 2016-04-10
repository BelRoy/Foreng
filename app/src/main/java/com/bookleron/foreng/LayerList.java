package com.bookleron.foreng;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.Image;
import android.os.Debug;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Андрей on 23.11.2015.
 */

public class LayerList {

 //   private Mat mat;
    private static ArrayList<Mat> ImageLayer;
    public static ArrayList<PointXY> LayerPointXY;
    public static ArrayList<String> List9;
    public static Mat TempMat; //для временного хранения слоя
    public static int CurrentIndex = 0;
    public static boolean IsInit = false;
    public LayerList(){

    }
    public static void Inizialize(){
        CurrentIndex = 0;
        ImageLayer = new ArrayList<Mat>();
        LayerPointXY = new ArrayList<PointXY>();
        IsInit = true;
    }
    public static void SetTempMat(Mat mat){
        TempMat = mat;
    }
    public static void InsertLayer(Mat mat){
      //  this.ImageLayer.add(mat);
         ImageLayer.add(mat);
        LayerPointXY.add(new PointXY());
     //  List9.add("ge");
    }
    public static Mat GetLayer(int index){
        Mat c,d;

        d = ImageLayer.get(index);
        c = new Mat();
        d.copyTo(c);

        return c;
    }
    public static void DeleteLayer(int index){
        ImageLayer.remove(index);
    //    if(CurrentIndex == index){

     //   }
    }
    public static void MoveLayerUp(int index){
        if(index+1 < ImageLayer.size()){
        //    Collections.swap(ImageLayer,index, index+1);
            Mat temp = ImageLayer.get(index);
            ImageLayer.set(index, ImageLayer.get(index+1));
            ImageLayer.set(index+1, temp);
        }
    }
    public static void Clear(){ ImageLayer.clear();}
    public static int GetCount(){return ImageLayer.size();}
 /*   public static Bitmap MergedLayer(){
        Mat c, b;
        c = new Mat();
        b = new Mat();
        c = LayerList.GetLayer(0);
        b = LayerList.GetLayer(1);
        Size sc = new Size(c.width(),c.height());

        Imgproc.resize(b,b,sc);

    }*/
    public static void ChangeLayer(int index, Mat image){
        ImageLayer.set(index,image);
    }
    public static void ChangeLayer(int index, Bitmap image){

        Mat c;
        c= new Mat();
        Utils.bitmapToMat(image,c);
        ImageLayer.set(index,c);
    }
    public static Mat RenderImage() {
        Mat render;
       render = new Mat();
        Mat image1, image2;
        image1 = new Mat();
        image2 = new Mat();
        image1 = LayerList.GetLayer(0);
//        image2 = LayerList.GetLayer(1);
        double[] data;
        double[] data2;
        double alpha;
        Mat temp;
       temp = new Mat(image1.rows(),image1.cols(), image1.type());
        image1.copyTo(temp);

        Bitmap example = Bitmap.createBitmap(image1.cols(),image1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image1,example);
        Canvas ex = new Canvas(example);
        Paint paint = new Paint();
        ex.drawBitmap(example, new Matrix(), null);
        for(int i=1; i< LayerList.GetCount();i++) {
            image2 = LayerList.GetLayer(i);
            Bitmap example2 = Bitmap.createBitmap(image2.cols(), image2.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(image2, example2);
            paint.setAlpha(LayerPointXY.get(i).alpha);
            ex.drawBitmap(example2, LayerPointXY.get(i).x, LayerPointXY.get(i).y,paint);

        }
        Utils.bitmapToMat(example,render);
        return render;

    /*
            старый способ наложения
     for (int y = 0; y < image2.rows(); y++) {
          for (int x = 0; x < image2.cols(); x++) {

              data = image1.get(y, x);
              data2 = image2.get(y, x);
                    alpha = data2[3];
                    data[0] = (1 - alpha / 256) * data[0] + (alpha * data2[0] / 256);
                    data[1] = (1 - alpha / 256) * data[1] + (alpha * data2[1] / 256);
                    data[2] = (1 - alpha / 256) * data[2] + (alpha * data2[2] / 256);

              temp.put(y, x, data);
              //     temp.pu
          }
      }

        render = temp;

        return render;*/
    }
    public static Mat RenderImage(int x, int y) {
        Mat render;
        render = new Mat();
        Mat image1, image2;
        image1 = new Mat();
        image2 = new Mat();
        image1 = LayerList.GetLayer(0);
//        image2 = LayerList.GetLayer(1);
        double[] data;
        double[] data2;
        double alpha;
        Mat temp;
        temp = new Mat(image1.rows(),image1.cols(), image1.type());
        image1.copyTo(temp);

        Bitmap example = Bitmap.createBitmap(image1.cols(),image1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image1,example);
        Canvas ex = new Canvas(example);
        ex.drawBitmap(example, new Matrix(), null);
        for(int i=1; i< LayerList.GetCount();i++) {
            image2 = LayerList.GetLayer(i);
            Bitmap example2 = Bitmap.createBitmap(image2.cols(), image2.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(image2, example2);

            ex.drawBitmap(example2, x, y, null);
        }

        Utils.bitmapToMat(example,render);
        return render;

    }
    public static Mat RenderImage(int index, int x, int y) {
        Mat render;
        render = new Mat();
        Mat image1, image2;
        image1 = new Mat();
        image2 = new Mat();
        image1 = LayerList.GetLayer(0);
//        image2 = LayerList.GetLayer(1);
        double[] data;
        double[] data2;
        double alpha;
        Mat temp;
        temp = new Mat(image1.rows(),image1.cols(), image1.type());
        image1.copyTo(temp);

        Bitmap example = Bitmap.createBitmap(image1.cols(),image1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image1,example);
        Canvas ex = new Canvas(example);
        ex.drawBitmap(example, new Matrix(), null);
        for(int i=1; i< LayerList.GetCount();i++) {
            image2 = LayerList.GetLayer(i);
            Bitmap example2 = Bitmap.createBitmap(image2.cols(), image2.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(image2, example2);
            if(i == index)
                ex.drawBitmap(example2, x, y, null);
            else
                ex.drawBitmap(example2,0,0,null);
        }

        Utils.bitmapToMat(example,render);
        return render;

    }
    public static Mat RenderImagePrev(int index, int x, int y, int alpha) {
        Mat render;
        render = new Mat();
        Mat image1, image2;
        image1 = new Mat();
        image2 = new Mat();
 //       if(index==0)
  //          image1 = LayerList.TempMat;
   //     else
            image1 = LayerList.GetLayer(0);
//        image2 = LayerList.GetLayer(1);
 //       double[] data;
  //      double[] data2;
  //      double alpha;
        Mat temp;
        temp = new Mat(image1.rows(),image1.cols(), image1.type());
        image1.copyTo(temp);

        Bitmap example = Bitmap.createBitmap(image1.cols(),image1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image1, example);
        Canvas ex = new Canvas(example);
        ex.drawBitmap(example, new Matrix(), null);
        Paint paint = new Paint();
        for(int i=1; i< LayerList.GetCount();i++) {
            if(i == index)
                image2 = LayerList.TempMat;
            else
                image2 = LayerList.GetLayer(i);

            Bitmap example2 = Bitmap.createBitmap(image2.cols(), image2.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(image2, example2);
            if(i == index) {
                paint.setAlpha(alpha);
                ex.drawBitmap(example2, x, y, paint);
            }
            else {
                paint.setAlpha(LayerPointXY.get(i).alpha);
                ex.drawBitmap(example2, LayerPointXY.get(i).x, LayerPointXY.get(i).y, paint);
            }
        }

        Utils.bitmapToMat(example,render);
        return render;

    }
}
