# Permission Tester v1.1

## Description

This small test serves as an example of how one can retrieve the permissions of all installed applications on Android devices at any time. The permissions may either be granted or denied. 

Since Android 6.0, permissions may be set at runtime and therefore, some permissions (from the dangerous protection level) may be denied and others accepted (as opposed to the accept all at install time from previous android versions). 

## How to use

Launch the application on a simulator or with the device connected to Android studio. Click on the button "GET PERMISSIONS LOG" and check the logcat. 

For every package, a message with the following format will be displayed:

	Package [PackageName] requires:
	[PermissionP1] (Status: [StatusP1]; Protection Level: [ProtectionLevelP1]; Group: [GroupP1])
	[PermissionP2] (Status: [StatusP2]; Protection Level: [ProtectionLevelP2]; Group: [GroupP2])
	...
	
	Summary:
	Granted permissions (Status equals 0): PermissionP1; PermissionP3; ...
	Denied permissions (Status equals -1): PermissionP2; ...

Example:

	Package com.android.emergency requires: 
	android.permission.CALL_PHONE (Status: 0; Protection Level: 1; Group: android.permission-group.PHONE)
	android.permission.READ_CONTACTS (Status: 0; Protection Level: 1; Group: android.permission-group.CONTACTS)
	android.permission.MANAGE_USERS (Status: 0; Protection Level: 18; Group: null)
	
	Summary:
	Granted permissions (Status equals 0): android.permission.CALL_PHONE; android.permission.READ_CONTACTS; android.permission.MANAGE_USERS; 
	Denied permissions (Status equals -1): 

One can change the permissions in the device settings (e.g. revoke location permission to maps) and then resume the PermissionTester to check the changes by clicking the button again.

Note: Denied permissions are permissions that were either not granted or no user input was yet given. Granted permissions from the dangerous protection level always require user input unless a (dangerous) permission from the same group was already granted - permissions are managed by group granularity.

## Implementation

The code is fully commented in order to understand the implementation: please inspect the `MainActivity.java` file. If you require further assistance, contact rscmendes@dei.uc.pt

Note: All used methods exist since API level 1 (first Android version).



