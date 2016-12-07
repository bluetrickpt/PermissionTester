## Permission Tester v1.0

# Description

This small test serves as an example of how one can retrieve the permissions of all installed applications on Android devices. The permissions may either be granted or denied. 
Since Android 6.0, permissions may be set at runtime and therefore, some permissions may be denied and other accepted (as opposed to the accept all at install time from previous android versions).

(Not implemented) Besides installed packages, this can also retrive uninstalled packages if the data directory persists on the device after uninstall (see https://developer.android.com/reference/android/content/pm/PackageManager.html#getInstalledApplications(int)).

# How to use

For this small demo, only ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION permissions were checked but one can easily adapt for any permission set. Note that there are infinite permissions, since developers can create new permissions. 

Launch the application on a simulator or with the device connected to Android studio. Click on one of the buttons and check the logcat. 

For every package, one or two messages will be displayed: The first message lists all the permissions required by the package and the second message will show only if the package has GRANTED permission (if "GET GRANTED PERMISSIONS LOG" was click) or DENIED (if GET DENIED PERMISSIONS LOG) was clicked.


One can change permissions in the device settings (e.g. revoke location permission to maps) and then resume the PermissionTester to check the changes by clicking the buttons again.

Note: Denied permissions are permissions that were either not granted or no user input was yet given.

# Implementation

The PackageManager (https://developer.android.com/reference/android/content/pm/PackageManager.html) may be used to access all installed packages on the Android device -- see getInstalledApplications(int). After getting all the applications data, one can use checkPermission(String, String) to check if a given permission is either "PERMISSION_GRANTED" or "PERMISSION_DENIED".

Note: All used methods exist since API level 1 (first Android version).



