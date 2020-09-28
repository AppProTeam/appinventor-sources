package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.graphics.Color;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;

@DesignerComponent(version = YaVersion.CUSTOMTABS_COMPONENT_VERSION,
        description = "Non-visible component to launch chrome custom tabs",
        category = ComponentCategory.EXPERIMENTAL,
        nonVisible = true,
        iconName = "images/chromecustomtab.png")
@SimpleObject
@UsesLibraries(libraries = "cct.jar,cct.aar")
public class CustomTabs extends AndroidNonvisibleComponent{
    public Context context;
    private Activity activity;
    private CustomTabService cts;
    //public boolean showTitle;
    //public boolean hideUrlBar;
    public int toolBarColor;	
    //public CustomTabsIntent.Builder builder;
        
    public CustomTabs(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
        activity = container.$context();
	//ShowTitle(true);
	//ToolBarColor(225);
	//HideUrl(false);    
    }

@SimpleFunction
public void Go(String url){
    cts = new CustomTabService(activity, toolBarColor);
    cts.launchUrl(url);
}
        
/*
    @SimpleProperty()
    public void ShowTitle(boolean bool){
        showTitle = bool;
	update();    
    }
        
*/
    @SimpleProperty()
    public void ToolBarColor(int color){
        toolBarColor = color;
	    //update();
    }
        
/*
    @SimpleProperty()
    public void HideUrl(boolean bool){
         hideUrlBar = bool;
	 update();
    }
           
    @SimpleFunction()
    public void OpenCustomTab(String url){
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
    public void update(){
       builder = new CustomTabsIntent.Builder();    
       builder.addDefaultShareMenuItem();
	if(hideUrlBar){
	  builder.enableUrlBarHiding(); 	
	}
	builder.setToolbarColor(toolBarColor);
	builder.setShowTitle(showTitle);    
    }		
*/
}
