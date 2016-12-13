package com.example.rmendes.permissiontester;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button getGrantedBtn = (Button) findViewById(R.id.get_log_btn);
        getGrantedBtn.setOnClickListener(new View.OnClickListener() { //On button click, runs our permission logger (runPermissionLogger())
            @Override
            public void onClick(View view) {
                runPermissionLogger();
            }
        });
    }

    //Logs to console all packages and the respective granted and denied permissions.
    private void runPermissionLogger() {

        final PackageManager pm = getPackageManager();

        //get a list of all installed apps.
        List<ApplicationInfo> installedApplications = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : installedApplications) { //For each application

            try {
                PackageInfo packagePermissionsInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS); //Get this package's permissions info

                String[] packagePermissions = packagePermissionsInfo.requestedPermissions; //Array of all <uses-permission> tags included under <manifest>, or null if there were none.

                if(packagePermissions != null) { //Some apps have no permission requirements
                    Log.d(TAG,"Package " + applicationInfo.packageName + " requires: ");

                    String grantedPermissions = "", deniedPermissions = ""; //A set of all granted (denied) permissions to display as a summary

                    for(String permissionName : packagePermissions) {

                        PermissionInfo permissionInfo = pm.getPermissionInfo(permissionName, 0); //Returns all the information concerning the permission "permissionName"
                        int protectionLevelFlag = permissionInfo.protectionLevel; // See flags in: https://developer.android.com/reference/android/content/pm/PermissionInfo.html
                        String group = permissionInfo.group; //The group may be null

                        int permissionStatusFlag = pm.checkPermission(permissionName, applicationInfo.packageName); //Gets if permission is granted or denied
                        if(permissionStatusFlag == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions += permissionName + "; ";
                        } else { //if it is not granted, it's PERMISSION_DENIED
                            deniedPermissions += permissionName + "; ";
                        }
                        Log.d(TAG, permissionName + " (Status: " + permissionStatusFlag + "; Protection Level: " + protectionLevelFlag + "; Group: " + group + ")");
                    }

                    Log.d(TAG," \n"); //for output format
                    Log.d(TAG, "Summary:");
                    Log.d(TAG, "Granted permissions (Status equals " + PackageManager.PERMISSION_GRANTED + "): " + grantedPermissions);
                    Log.d(TAG, "Denied permissions (Status equals " + PackageManager.PERMISSION_DENIED + "): " + deniedPermissions);
                    Log.d(TAG,"===================================");
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //We have started by going through each package and check all permissions in a given package.
    //We can do the inverse path. Start with a permission and check all packages' permissions with:
    //getPackagesHoldingPermissions(String[] permissions, int flags) //Returns List<PackageInfo>
}
