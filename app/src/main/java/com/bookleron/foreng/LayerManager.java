package com.bookleron.foreng;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 25.11.2015.
 */
public class LayerManager extends Activity{

    ImageView imgview;
    Mat c;
    Bitmap temp;
    ListView list;
    LayerManagerAdapter adapter;
    LayerMaganerList[] LayerListManagerArray;
//    List<LayerMaganerList> list1;
    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layer_manager);



    }

    @Override
    public void onResume(){
        super.onResume();
    //    c = new Mat();
     //   c = LayerList.GetLayer(0);
    /*    imgview = (ImageView)findViewById(R.id.imageView2);
        temp = Bitmap.createBitmap(c.cols(),c.rows(), Bitmap.Config.ARGB_8888);
        //temp =((BitmapDrawable) imgview.getDrawable()).getBitmap();
        Utils.matToBitmap(c, temp);
       imgview.setImageBitmap(temp);*/
      /*  LayerMaganerList LayerListManager[] = new LayerMaganerList[]{
                new LayerMaganerList(LayerList.GetLayer(0)),
                new LayerMaganerList(LayerList.GetLayer(1))
        };*/
        //     LayerMaganerList LayerListManager;

         LayerListManagerArray = new LayerMaganerList[LayerList.GetCount()];
        Log.e("POSITION CATION","COUNT = "+LayerList.GetCount());
 //       list1.clear();
     //  for(int i = 0; i<LayerList.GetCount();i++){
       for(int i = LayerList.GetCount()-1; i>=0; i--){
            LayerListManagerArray[i] = new LayerMaganerList(LayerList.GetLayer(i));
       //    list1.add(i,new LayerMaganerList(LayerList.GetLayer(i)));

        }


         adapter = new LayerManagerAdapter(this,R.layout.listview_item_layer_manager,LayerListManagerArray);
     //   adapter = new LayerManagerAdapter(this,R.layout.listview_item_layer_manager,list1);
        list  = (ListView)findViewById(R.id.listView2);
        list.setAdapter(adapter);

    }
    public void DeleteLayerButton(View v){

        com.bookleron.foreng.LayerList.DeleteLayer((Integer)v.getTag());
        //

        LayerListManagerArray = new LayerMaganerList[LayerList.GetCount()];
        Log.e("POSITION CATION","COUNT = "+LayerList.GetCount());
        //  for(int i = 0; i<LayerList.GetCount();i++){
        for(int i = LayerList.GetCount()-1; i>=0; i--){
            LayerListManagerArray[i] = new LayerMaganerList(LayerList.GetLayer(i));

        }

        adapter.notifyDataSetChanged();

    //    Intent reloadList = new Intent(parent.getContext(), LayerManager.class);
    //    parent.getContext().startActivity(reloadList);

    }

}
