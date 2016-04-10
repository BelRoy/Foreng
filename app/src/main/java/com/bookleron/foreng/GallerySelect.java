package com.bookleron.foreng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileFilter;
import java.lang.ref.SoftReference;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Андрей on 27.10.2015.
 */
public class GallerySelect extends Activity {

        TextView text;
        Spinner spinner;
        ListView listview;
        String pathDir;

    public void onCreate(Bundle savedInstanseState) {
        super.onCreate(savedInstanseState);
        setContentView(R.layout.select_image);
        text = (TextView) findViewById(R.id.textView);
      //  spinner = (Spinner) findViewById(R.id.spinner);
     //   File f = Environment.getExternalStorageDirectory();
        listview = (ListView) findViewById(R.id.listView);
      //  File f = new File(sdcard.getAbsolutePath());
  /*     File f = new File("/");
        File[] files = f.listFiles();
        ArrayAdapter<String> list = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item ,new ArrayList<String>());

   //    ArrayAdapter<String> list = new ArrayAdapter<String>(t)
        String type="",type2="";

        for(int i =0; i<files.length;i++){
            if(files[i].isFile()) {//если файл, то получить его расширение
                type = files[i].getName();
                type2 = type.substring(type.lastIndexOf(".") + 1, type.length());
            }
            if(files[i].isDirectory() ||(files[i].isFile() && (type2=="jpg" || type2=="png")) ) { //если папка или проверить формат файла
                list.add(files[i].getName());

            }
            else{
                list.add(files[i].getName() + "[predic]");

            }
        }
        text.setText(files[0].getName()) ;
     //   spinner.setAdapter(list);
        listview.setAdapter(list); */
        LoadDirectory("/");
     //   LoadDirectory2("/");
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   text.setText(listview.getSelectedItem());
                String c = (String) listview.getItemAtPosition(position);
                //    text.setText(c);
                File file = new File(c);
                if (file.isDirectory()) {
                    LoadDirectory(c);
                }
                if (file.isFile()) {

                }
                android.util.Log.w("", "/" + c);
                //   LoadDirectory2("/"+File.separator+c);

                //   pathDir+=c;
                //    text.setText(pathDir);
            }
        });
    }

    public void LoadFile(String path){
     //   if(path)
        Intent postPath = new Intent();
        postPath.putExtra("FILE_MANAGER", path);
    }
    public void LoadDirectory2(String path){
        String c[];
        File f = new File(path,"");
       c = f.list();

       ArrayAdapter<String> list = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item ,c);
        listview.setAdapter(list);
       // ArrayAdapter<String> list = new ArrayAdapter<String>(this,)
    }

    public void LoadDirectory(String path){
        int countImg=0; //количество изображений в папке
    //    pathDir += path;
   //     File f = new File(pathDir,"");
   //   File f = new File(Environment.getExternalStorageDirectory()+"//");
     //   if(path=="" || path==" " || path==null) path="/";
        android.util.Log.w("FORENG First Path", path);
        File f = new File(path);
        if(f.exists() && f.canRead()) {
            File[] files = f.listFiles();

            android.util.Log.w("FORENG", files[0].getPath());

            ArrayAdapter<String> list = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new ArrayList<String>());

            //    ArrayAdapter<String> list = new ArrayAdapter<String>(t)
            String type = "", type2 = "";
         //  if(files.length==0) {

               for (int i = 0; i < files.length; i++) {


                  if (files[i].isFile()) {//если файл, то получить его расширение
                       type = files[i].getName();
                      android.util.Log.w("FILES NAME",type);
                     // type = files[i].getAbsolutePath();
                       type2 = type.substring(type.lastIndexOf(".") + 1, type.length());
                      android.util.Log.w("Files TEMP",type2);
                      if(type2.equals("jpg") || type2.equals("png")){

                          list.add(files[i].getAbsolutePath());
                      }
                   }
                   if (files[i].isDirectory()) { //если папка или проверить формат файла
                       //   list.add(files[i].getName());
                       list.add(files[i].getAbsolutePath());

                   }
             /*     if (files[i].isFile() && (type2 == "jpg" || type2 == "png")) {
                       list.add(files[i].getAbsolutePath());
                       countImg++;
                   }*/
                   //     else {
                   //     list.add(files[i].getName() + "[predic]");
                   //        list.add(files[i].getAbsolutePath());
                   //  }
                //   list.add(files[i].getAbsolutePath());
               }
         //      text.setText(files[0].getName());
               //   spinner.setAdapter(list);
               listview.refreshDrawableState();
               listview.setAdapter(list);
//               text.setText("В этой папке " + countImg + " изображений");
               pathDir = path;
           }
        //    else{
        //       ShowMessage("Эта папка пуста");
         //  }
       // }
        else{
         /*   final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Нет прав на просмотр папки");
            alert.setNegativeButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
        alert.create();
            alert.show();*/
            ShowMessage("Нет прав на просмотр папки");
        }
    }

    public void ShowMessage(String header){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(header);
        alert.setNegativeButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.create();
        alert.show();
    }

    public void backDir(View view) {

        String temp = pathDir; String temp2;
  //     android.util.Log.w("FORENG",temp);
     //  temp = "/hello/world";
        temp2 = temp.substring(0,temp.lastIndexOf('/'));

    //    android.util.Log.w("FORENG", temp2);
      //  if(temp2.charAt(0)!='/')
         //   LoadDirectory("/");
       // else
        if(temp2.trim().length()<=0){
            LoadDirectory("/");
        }
        else
        LoadDirectory(temp2);
    }
}


