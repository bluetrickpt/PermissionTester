package com.example.rmendes.permissiontester;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();


    //Small test with location only. We could use a String[] with any number of permissions.
    private static final String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    //Note that there are infinite permissions, since developers can create new permissions.
    //Thus, this list may either be static or ever growing by appending (distinct) packages' permissions as they appear.
    //One way to get all permissions in a given device does exist, although I haven't tested (see http://stackoverflow.com/a/32063889).


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button getGrantedBtn = (Button) findViewById(R.id.get_granted_btn);
        getGrantedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runPermissionLogger(PackageManager.PERMISSION_GRANTED);
            }
        });

        Button getDeniedBtn = (Button) findViewById(R.id.get_denied_btn);
        getDeniedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runPermissionLogger(PackageManager.PERMISSION_DENIED);
            }
        });

    }

    //Logs to console packages with granted/denied permissions from PERMISSIONS.
    //pemissionStatus may be PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED
    private void runPermissionLogger(int permissionStatus) {

        final PackageManager pm = getPackageManager();

        //get a list of all installed apps.
        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : installedApplications) { //For each application
//            Log.d(TAG, "Installed package :" + applicationInfo.packageName);
//            Log.d(TAG, "Source dir : " + applicationInfo.sourceDir);
//            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(applicationInfo.packageName));


            //List all required (may or may not be accepted yet) permissions for this package
            try {
                // pm.getLaunchIntentForPackage(applicationInfo.packageName); //gives the intent --> startActivityForResult(Intent, int requestCode)

                PackageInfo packagePermissionsInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS); //Get this application's package permission info

                String[] packagePermissions = packagePermissionsInfo.requestedPermissions; //Array of all <uses-permission> tags included under <manifest>, or null if there were none.
                if(packagePermissions != null) { //Some apps have no permission requirements
                    String allPermissions = new String();
                    for(String perm : packagePermissions) {
                        allPermissions += perm + "; ";
                    }
                    Log.d(TAG,"Package " + applicationInfo.packageName + " requires: " + allPermissions);
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            //Now check all granted/denied permissions. Note that denied permissions are permissions that were not granted or no user input was yet given.
            //By default, normal permissions are always granted and dangerous permissions require user input (see protectionLevel in Android API)

            for (String permission : PERMISSIONS) { //Check if our PERMISSIONS were accepted or (not yet accepted or denied)
                if (pm.checkPermission(permission, applicationInfo.packageName) == permissionStatus) {
                    String permissionStatusString = (permissionStatus==PackageManager.PERMISSION_GRANTED) ? " has access to " : " is denied to ";
                    Log.d(TAG,"Package: " + applicationInfo.packageName  + permissionStatusString + permission);
                }
            }
        }
        Log.d(TAG,"===================================");
    }

    //We have started by going through each package and check all permissions in a given package.
    //We can do the inverse path. Start with a permission and check all packages' permissions with:
    //getPackagesHoldingPermissions(String[] permissions, int flags) //Returns List<PackageInfo>
}
