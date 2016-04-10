package com.bookleron.foreng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;

/**
 * Created by Андрей on 25.11.2015.
 */
public class LayerManagerAdapter extends ArrayAdapter<LayerMaganerList> {
    Context context;
    int layoutResourseId;
    LayerMaganerList[] LayerList;
    public LayerManagerAdapter(Context context, int resource, LayerMaganerList[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResourseId = resource;
        this.LayerList = objects;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View row = convertView;
        LayerHolder holder=null;
        if(row==null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourseId,parent,false);
            holder = new LayerHolder();
            holder.imagePreview = (ImageView)row.findViewById(R.id.imageView2);
            holder.UpButton = (Button)row.findViewById(R.id.buttonUp);
            holder.DownButton = (Button)row.findViewById(R.id.buttonDown);
            holder.SettingLayer = (Button) row.findViewById(R.id.buttonSetting);
            holder.Delete = (Button) row.findViewById(R.id.buttonDelete);
            row.setTag(holder);
        }
        else{
            holder = (LayerHolder)row.getTag();
        }
        final LayerMaganerList managerList = LayerList[position];
        Bitmap prev;
        prev = Bitmap.createBitmap(managerList.image.cols(),managerList.image.rows(), Bitmap.Config.ARGB_8888);
     //   prev = ((BitmapDrawable)holder.imagePreview.getDrawable()).getBitmap();
        Utils.matToBitmap(managerList.image,prev);
        holder.imagePreview.setImageBitmap(prev);
        holder.SettingLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToEffect = new Intent(parent.getContext(), ImageEffect.class);
                goToEffect.putExtra("ImageId", position);
                parent.getContext().startActivity(goToEffect);
            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.bookleron.foreng.LayerList.DeleteLayer(position);
               Intent reloadList = new Intent(parent.getContext(), LayerManager.class);
                parent.getContext().startActivity(reloadList);

            }
        });
        holder.UpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.bookleron.foreng.LayerList.MoveLayerUp(position);
                Intent reloadList = new Intent(parent.getContext(), LayerManager.class);
                parent.getContext().startActivity(reloadList);
            }
        });

        return row;
       // return super.getView(position, convertView, parent);
    }
    static class LayerHolder{
        ImageView imagePreview;
        Button UpButton;
        Button DownButton;
        Button SettingLayer;
        Button Delete;

    }
}

