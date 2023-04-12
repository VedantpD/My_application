package com.example.myapplication;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("MyAccessibilityService","onAccessibilityService : ");
        String packageName = event.getPackageName().toString();
        String text = event.getText().toString();
        PackageManager packageManager = this.getPackageManager();
        try{
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName,0);
            CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
            Log.d("MyAccessibilityService","Application: "+applicationLabel+" text: "+text);
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Application and text", "Application: "+applicationLabel+" text: "+text);
            editor.apply();
        }catch(PackageManager.NameNotFoundException e){
            System.err.println("An error occurred: " + e.getMessage());
            Log.d("MyAccessibilityService","Application: "+e.getMessage()+" text: "+text);
            SharedPreferences prefs = getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Application and text", "Application: "+e.getMessage()+" text: "+text);
        }
    }

    @Override
    public void onInterrupt() {
        Log.d("MyAccessibilityService","something went wrong");
    }
    @Override
    public void onServiceConnected() {
        super.onServiceConnected();

        AccessibilityServiceInfo info  = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED;

        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;


        info.notificationTimeout = 100;

        this.setServiceInfo(info);


    }

}
