package com.google.appinventor.components.runtime;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.YaVersion;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.YailList;

import java.util.HashMap;

@DesignerComponent(version = 1,category = ComponentCategory.CONNECTIVITY,description = "Non visible component to download files",iconName = "images/download.png")
@SimpleObject()
public class Download extends AndroidNonvisibleComponent implements OnDestroyListener{
    public Context context;
    public String dUrl;
    public String des;
    public String t;
    public String dir;
    public String name;
    public long id;
    public HashMap<String,String> map = new HashMap<>();
    public DownloadManager downloadManager;
    public DownloadManager.Request request;
    public BroadcastReceiver completed = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1) == id){
                OnDownloadCompleted();
            }
        }
    };
    public Download(ComponentContainer container){
        super(container.$form());
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        context.registerReceiver(completed,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    @SimpleProperty()
    public void DownloadURL(String url){
        if (!url.isEmpty()){
            dUrl = url;
        }
    }
    @SimpleProperty()
    public void AddRequestHeader(YailList list){
        map.put(list.getString(0),list.getString(1));
    }
    @SimpleProperty()
    public void Description(String description){
        if (description.isEmpty()){
            des = "Downloading file...";
        }else {
            des = description;
        }
    }
    @SimpleProperty()
    public void Title(String title){
        if (title.isEmpty()){
            t = "Download";
        }else {
            t = title;
        }
    }
    @SimpleProperty()
    public void DownloadDir(String dir){
        this.dir = dir;
        if (dir.isEmpty()){
            this.dir = Environment.DIRECTORY_DOWNLOADS;
        }else {
            this.dir = dir;
        }
    }
    @SimpleProperty()
    public void FileName(String f){
        name = f;
    }

    @SimpleFunction()
    public void StarDownload(){
        request = new DownloadManager.Request(Uri.parse(dUrl));
        //request.setMimeType(mimeType);
        for (String k:map.keySet()){
            request.addRequestHeader(k,map.get(k));
        }
        request.setDescription(des);
        request.setTitle(t);
        //request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if (dir.isEmpty()){
            dir = Environment.DIRECTORY_DOWNLOADS;
        }else if(name.isEmpty()) {
            if (!dUrl.isEmpty()) {
                if (dUrl.contains("/")) {
                    name = dUrl.split("/")[dUrl.split("/").length];
                } else {
                    name = dUrl;
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            request.setDestinationInExternalFilesDir(context,dir,name);
        }else {
            request.setDestinationInExternalPublicDir(dir, name);
        }
        id = downloadManager.enqueue(request);
    }
    @SimpleEvent()
    public void OnDownloadCompleted(){
        EventDispatcher.dispatchEvent(this,"OnDownloadCompleted");
    }
    /*@SimpleEvent()
    public void OnDownloadFailed(){
        EventDispatcher.dispatchEvent(this,"OnDownloadFailed");
    }*/

    @Override
    public void onDestroy() {
        context.unregisterReceiver(completed);
    }
}
