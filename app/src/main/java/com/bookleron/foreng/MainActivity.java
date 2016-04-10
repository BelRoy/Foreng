package com.bookleron.foreng;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.engine.OpenCVEngineInterface;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/*public class Sample1Java extends Activity implements CameraBridgeViewBase.CvCameraViewListener {
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    //Мы готовы использовать OpenCV
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }
}*/
public class MainActivity extends Activity {

/*  static{
        if(OpenCVLoader.initDebug()) {

            Log.i("openCV","buged");
        }
        else{
            Log.i("opencvh","initialized");
        }
    }*/

    ImageView currentImage;
    WebView currentWebView;
    Mat currentMat,outMat,kernel,kernel2,prevImage;
    Bitmap image;
    static final private int FILE_MANAGER_ACTIVITY = 1; //код для окна выбора файлов
    static final private int RESULT_FROM_GALLERY = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Log.e("Hello", "Start");
        setContentView(R.layout.activity_main);
    //    mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);

       if(!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mOpenCVCallBack))
        {
            Log.e("Hello", "Cannot connect to OpenCV Manager");
        }


        currentImage = (ImageView)findViewById(R.id.imageView);
        currentWebView = (WebView) findViewById(R.id.webView);
        currentWebView.getSettings().setSupportZoom(true);
        currentWebView.getSettings().setBuiltInZoomControls(true);
      //  currentImage.setImageAlpha(10);

      //  image = (Bitmap)getResources(R.drawable.forest);
        image = ((BitmapDrawable)currentImage.getDrawable()).getBitmap();
    //    Mat c = new Mat(5,5, CvType.CV_16S);

    //   Mat currentMat = new Mat();
      //  Mat outputMat = new Mat();
      //  currentMat = new Mat();
     //   Utils.bitmapToMat(image,currentMat);
      //  Imgproc.GaussianBlur(currentMat, currentMat, new Size(10, 10), 1.5, 1.5);
       // Utils.matToBitmap(currentMat, image);
       currentImage.setImageBitmap(image);
        SlidingMenu slideMenu = new SlidingMenu(this);
        slideMenu.setMode(SlidingMenu.LEFT);
        slideMenu.setBehindWidthRes(R.dimen.slidemenu_behind_width);
        slideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slideMenu.setMenu(R.layout.slidemenu);
        slideMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);




        if(!LayerList.IsInit)
        LayerList.Inizialize();


    }

    @Override
    public void onResume()
    {
        super.onResume();
      if (!OpenCVLoader.initDebug()) {
            Log.d("45", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mOpenCVCallBack);
        } else {
            Log.d("45", "OpenCV library found inside package. Using it!");
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
     //   OpenCVLoader.initDebug();
     //   Lol();
        UpdateImage();
    }
   /* public void onResume()
    {
        super.onResume();
        //Вызываем асинхронный загрузчик библиотеки
      //  OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mOpenCVCallBack);
    }*/
    private BaseLoaderCallback  mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {

                switch (status) {
                    case LoaderCallbackInterface.SUCCESS:
                    {
                        Log.e("Hello", "connect to OpenCV Manager");

                        //Мы готовы использовать OpenCV
                    } break;
                    default:
                    {
                        super.onManagerConnected(status);
                    } break;
                }

        }
    };

     //   @Override

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void RefreshButton(View view){
        UpdateImage();
    }
    public void ImageTouch(View view){
        if(LayerList.GetCount() == 0){
            this.openGallery(view);
        }
        else{
            this.goToLayerManager(view);
        }
    }
    public void openGallery(View view) {
 /*       Intent goToGallery = new Intent(MainActivity.this,GallerySelect.class);
  //      startActivity(goToGallery);
        startActivityForResult(goToGallery, FILE_MANAGER_ACTIVITY);
        */
        //Выбираем изображение через галерею
        // Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(gallery, RESULT_FROM_GALLERY);

        Intent gallery = new Intent(Intent.ACTION_PICK);
        gallery.setType("image/*");
        startActivityForResult(gallery, RESULT_FROM_GALLERY);
    }

    public void NewSimpleProject(View view){
      //  LayerList.ImageLayer = new ArrayList<Mat>();
        LayerList.Inizialize();
        //     Mat c = new Mat()
   //     currentMat = new Mat();
    //    outMat = new Mat();

        Mat image1, image2;
        image1 = new Mat();
        image2 = new Mat();
        //  outMat.setTo(new Scalar(5));
        //  outMat.setTo(Scalar.all(4));
        Resources res = this.getResources();
        Bitmap temp1;
        temp1 = BitmapFactory.decodeResource(res,R.drawable.forest);
        Utils.bitmapToMat(temp1,image1);
      //  Utils.bitmapToMat(image, currentMat);
        //if(LayerList.GetCount() == 0) {
            LayerList.InsertLayer(image1);

        //загружаем 2 слой

        Bitmap temp;

        temp = BitmapFactory.decodeResource(res,R.drawable.cow4);
        Mat temp2,temp3;
        temp2 = new Mat();
        temp3 = new Mat();
        Utils.bitmapToMat(temp,temp2);
 //       if(LayerList.GetCount() == 1) {
            LayerList.InsertLayer(temp2);
   //     }
        //конец теста
/*
//               Imgproc.GaussianBlur(currentMat, currentMat, new Size(10, 10), 1.5, 1.5);
        //   Imgproc.putText(currentMat,"Hello",new Point(4,4),);
        Imgproc.cvtColor(image1, image2, Imgproc.COLOR_BGR2GRAY);
        //    Size sc = new Size(100,100);
        //  Imgproc.resize(temp2,temp2,sc);

        //    Imgproc.resize(currentMat,temp2,sc);
        //  currentMat = currentMat*5;
        //     currentMat.mul(currentMat,5);
        //    Utils.matToBitmap(currentMat, image);
        //  currentImage.setImageBitmap(image);


        image2 = image1;

        for(int y=0;y<temp2.rows();y++) {
            for (int x = 0; x < temp2.cols(); x++) {
                //    int alpha =256*(x+y)/(currentMat.cols()+currentMat.rows());
                double[] data = image1.get(y, x);
                double[] data2 = temp2.get(y, x);
                double alpha = data2[3];
                data[0] = (1 - alpha / 256) * data[0] + (alpha * data2[0] / 256);
                data[1] = (1 - alpha / 256) * data[1] + (alpha * data2[1] / 256);
                data[2] = (1 - alpha / 256) * data[2] + (alpha * data2[2] / 256);

                image2.put(y, x, data);
            }
        }
        //     List<Mat> listOfMat = new ArrayList<Mat>();
        //      Core.split(temp2,listOfMat);

        //   Bitmap.createBitmap(image,0,0,temp3.cols(),temp3.rows());
        //  Utils.matToBitmap(temp3,image);

        Bitmap tempImage = Bitmap.createBitmap(image1.cols(),image1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(image1,tempImage);
        //    Utils.matToBitmap(listOfMat.get(3),image);
      currentImage.setImageBitmap(tempImage);
*/
        UpdateImage();

    }

    public void UpdateImage(){
      if(LayerList.GetCount() >0) {
            Mat render;
            render = new Mat();
            render = LayerList.RenderImage();
            Bitmap temp = Bitmap.createBitmap(render.cols(), render.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(render, temp);

    /*      String html="<html><body><img src='{IMAGE_PLACEHOLDER}' /></body></html>";
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          temp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
          byte[] byteArray = byteArrayOutputStream.toByteArray();
          String imgageBase64 =  Base64.encodeToString(byteArray, Base64.DEFAULT);
          String image = "data:image/png;base64," + imgageBase64;
          html = html.replace("{IMAGE_PLACEHOLDER}", image);
          currentWebView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", "");*/

           currentImage.setImageBitmap(temp);
        }

    }
    public void TransfromLayer(View view){
        Intent a = new Intent(this,LayerTransform.class);
        startActivity(a);
    }
    public void filterOn(View view) {
  /*      int kernelSize = 3;
        kernel = Mat.ones(kernelSize,kernelSize,CvType.CV_32F);
        Scalar c = new Scalar(1/9);
        Core.multiply(kernel,c, kernel);
        Utils.bitmapToMat(image, currentMat);
     //   Core.divide(5, kernel, kernel);
      //  Imgproc.filter2D(currentMat, outMat, -1, kernel2);
     //   Core.multiply(currentMat, c, outMat);
        Imgproc.blur(currentMat,outMat,new Size(10,10));
        Utils.matToBitmap(outMat, image);
        currentImage.setImageBitmap(image);
        */
        ShowDialogBlur();
    }

    public void blurFilter(double param){
        int kernelSize = 3;
        kernel = Mat.ones(kernelSize,kernelSize,CvType.CV_32F);
        Scalar c = new Scalar(1/9);
        Core.multiply(kernel,c, kernel);
        Utils.bitmapToMat(image, currentMat);
        if(param == 0)  param =1;
     //   Core.divide(5, kernel, kernel);
      //  Imgproc.filter2D(currentMat, outMat, -1, kernel2);
     //   Core.multiply(currentMat, c, outMat);
        Imgproc.blur(currentMat,outMat,new Size(param,param));
        Utils.matToBitmap(outMat, image);
        currentImage.setImageBitmap(image);

    }
    public void resetChange(View view) {
        Utils.matToBitmap(currentMat, image);
        currentImage.setImageBitmap(image);
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

    public void ScalePositive(View view) {
   /*     Utils.bitmapToMat(image, currentMat);
    //    Imgproc.resize(currentMat, outMat, new Size(10, 10));
       Imgproc.resize(currentMat, outMat, currentMat.size(), 0.5, 0.5, Imgproc.INTER_LINEAR);
     //   Imgproc.resize(currentMat,outMat,Si,0.5,0.5,Imgproc.INTER_LINEAR);
        Utils.matToBitmap(outMat, image);
       currentImage.setImageBitmap(image);*/

        Mat temp;
        temp = new Mat();
        temp = LayerList.RenderImage();
        Size size = new Size(temp.cols()*1.5,temp.rows()*1.5);
        Imgproc.resize(temp, temp, size);

        Bitmap temp2 = Bitmap.createBitmap(temp.cols(), temp.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(temp, temp2);
        currentImage.setScaleType(ImageView.ScaleType.MATRIX);
        currentImage.setImageBitmap(temp2);



    }

    public void Contrast(View view) {
      /*  Utils.bitmapToMat(image, currentMat);
        Mat newMat =  Mat.zeros(currentMat.rows(),currentMat.cols(),currentMat.type());
        for(int i =0; i<currentMat.rows();i++){
            for(int j=0; j<currentMat.cols();j++){
                for(int c =0; c<3;c++){
                //    newMat.put()
         //           newMat.row(4).col(2).
                }
            }
        }*/
        Utils.bitmapToMat(image, currentMat);
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
        Imgproc.erode(currentMat, outMat, kernel);
        Utils.matToBitmap(outMat, image);
        currentImage.setImageBitmap(image);
    }

    public void NewProjectStart(View view){
        final AlertDialog.Builder cation = new AlertDialog.Builder(this);
        cation.setTitle("Начать новый проект? Старый проект будет удален");
        cation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LayerList.Clear();
                LayerList.Inizialize();


                SetDefaultImage();
                dialog.cancel();
            }
        });
        cation.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        cation.create();
        cation.show();
    }

    public void SetDefaultImage(){
        Resources res = getResources();
        Bitmap temp1;
        temp1 = BitmapFactory.decodeResource(res,R.drawable.text5598);
        currentImage.setImageBitmap(temp1);
    }
    public void SaveImageResult(View view){
    /*    Mat renderMat =  LayerList.RenderImage();
        Bitmap temp =  Bitmap.createBitmap(renderMat.cols(),renderMat.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(renderMat,temp);*/
        BitmapDrawable draw = (BitmapDrawable) currentImage.getDrawable();
        Bitmap temp = draw.getBitmap();
    //    File sdCard = Environment.getExternalStorageDirectory();
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(sdCard,"test.png");//создание файла

        boolean success = false;

        String filePath = image.getPath();

        FileOutputStream outStream;
      //  MediaStore.Images.Media.insertImage(getContentResolver(),temp,"image1.png","Created by Foreng");

        success = true;
        try {
            outStream = new FileOutputStream(image);
            temp.compress(Bitmap.CompressFormat.PNG, 100, outStream); //100 - полное качество

            outStream.flush();
            outStream.close();

            success = true;
         //   MediaStore.Images.Media.insertImage(getContentResolver(), sdCard.toString() +"test.png","test","test");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        if(success){
            //добавление изображения в галерею
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.MediaColumns.DATA, filePath);

            getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

            //вызов подсказки
            Toast.makeText(getApplicationContext(),"Image success saved",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Something was wrong",Toast.LENGTH_LONG).show();
        }

    }

    public void goToLayerManager(View view) {
        Intent layerManager = new Intent(MainActivity.this,LayerManager.class);
       startActivity(layerManager);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILE_MANAGER_ACTIVITY){ //ответ с окна выбора файлов
            if(resultCode == RESULT_OK){
                LoadFileToLayer(data.getStringExtra("FILE_MANAGER"));
            }
        }
        if(requestCode == RESULT_FROM_GALLERY && resultCode == RESULT_OK && data != null){
            Uri selectedFile = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedFile, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.e("FILE PATH", picturePath);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap selectedImage = BitmapFactory.decodeFile(picturePath.toString().trim(),option);
          //  LayerList.InsertLayer();
         //   LayerList.ImageLayer.clear();
        //   LayerList.ChangeLayer(0, selectedImage);
            Mat convert;
            convert = new Mat();
            Utils.bitmapToMat(selectedImage,convert);
            LayerList.InsertLayer(convert);
        //    currentImage.setImageBitmap(selectedImage);
        }
    }
    public void LoadFileToLayer(String path){

    }
}
