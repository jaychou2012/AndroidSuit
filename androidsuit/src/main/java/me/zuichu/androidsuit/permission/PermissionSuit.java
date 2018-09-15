package me.zuichu.androidsuit.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class PermissionSuit {
    private Activity activity;
    private PermissionListener permissionListener;
    private int requestCode = 0;
    private ArrayList<String> requestPermissions = new ArrayList<String>();
    public static final String[]
            group_storage = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final String[]
            group_camera = {Manifest.permission.CAMERA};
    public static final String[]
            group_contacts = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS};
    public static final String[]
            group_location = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String[]
            group_microphone = {Manifest.permission.RECORD_AUDIO};
    public static final String[]
            group_phone = {Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP,
            Manifest.permission.ADD_VOICEMAIL,
            Manifest.permission.PROCESS_OUTGOING_CALLS
    };
    public static final String[]
            group_calendar = {Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR};
    public static final String[]
            group_sensor = {Manifest.permission.BODY_SENSORS};
    public static final String[]
            group_sms = {
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_WAP_PUSH
    };

    public static PermissionSuit with(Activity activity) {
        return new PermissionSuit(activity);
    }

    private PermissionSuit(Activity activity) {
        this.activity = activity;
    }

    public PermissionSuit setPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions.clear();
            if (permissions.length == 0) {
                permissions = getPermissions(activity);
            }
            for (String permission : permissions) {
                if (!hasPermission(permission)) {
                    requestPermissions.add(permission);
                }
            }
            if (requestPermissions.size() == 0) {
                if (permissionListener != null) {
                    permissionListener.getPermission(permissions[0]);
                }
                return this;
            }
            PermissionFragment.newInstance(requestPermissions, requestCode).request(activity.getFragmentManager());
        }
        return this;
    }

    public PermissionSuit setPermissions(String[]... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions.clear();
            String[] strings = new String[]{};
            if (permissions.length == 0) {
                strings = getPermissions(activity);
            } else {
                strings = getStringsOneDimensional(permissions);
            }
            for (String permission : strings) {
                if (!hasPermission(permission)) {
                    requestPermissions.add(permission);
                }
            }
            if (requestPermissions.size() == 0) {
                if (permissionListener != null) {
                    permissionListener.getPermission(getStringsOneDimensional(permissions).toString());
                }
                return this;
            }
            PermissionFragment.newInstance(requestPermissions, requestCode).request(activity.getFragmentManager());
        }
        return this;
    }

    public PermissionSuit setPermissions(ArrayList<String> permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions.clear();
            if (permissions.size() == 0) {
                permissions = (ArrayList<String>) Arrays.asList(getPermissions(activity));
            }
            for (String permission : permissions) {
                if (!hasPermission(permission)) {
                    requestPermissions.add(permission);
                }
            }
            if (requestPermissions.size() == 0) {
                return this;
            }
            PermissionFragment.newInstance(requestPermissions, requestCode).request(activity.getFragmentManager());
        }
        return this;
    }

    public static void toApplicationSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public boolean hasPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(activity, permission);
        return result == PackageManager.PERMISSION_GRANTED ? true : false;
    }

    public String[] getPermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    private String[] getStringsOneDimensional(String[][] strings) {
        String[] string;
        int len = 0;
        for (String[] element : strings) {
            len += element.length;
        }
        string = new String[len];
        int index = 0;
        for (String[] array : strings) {
            for (String element : array) {
                string[index++] = element;
            }
        }
        return string;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                System.out.println("授权" + permissionListener);
                if (permissionListener != null) {
                    System.out.println("授权回调");
                    permissionListener.getPermission(permissions[i]);
                }
            } else {
                System.out.println("没授权" + permissionListener);
                if (permissionListener != null) {
                    System.out.println("没授权回调");
                    permissionListener.noPermision(permissions[i]);
                }
            }
        }
    }

    public PermissionSuit setPermissionListener(PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
        return this;
    }

    public interface PermissionListener {
        void getPermission(String permission);

        void noPermision(String permission);
    }
}
