package com.QuetzalSoft.myapplication.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ProgressBar;

public class user_data {
    public static int uid;
    public static String username;
    public static String email;
    public static String address;
    public static String phone_number;
    public static ProgressDialog dialog;

    public static void show_dialog(Context context){
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait..");
        dialog.setCancelable(false);
        try {
            dialog.show();
        }
        catch (Exception e){}
    }

    public static void dismiss_dialog(){
        dialog.dismiss();
    }
}
