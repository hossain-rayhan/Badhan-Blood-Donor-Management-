package com.appsomehow.badhan.helper;

import android.content.Context;
import android.widget.Toast;

public class Helper {

    public static boolean isEditAllChecked = false;

    public static void showToast(Context ctx, String msg){
        Toast toast = Toast.makeText(ctx,msg,Toast.LENGTH_LONG);
        toast.show();
    }

    public static int getBloodGroupIndex(String bloodGroup){
        String bloodGroups[] = {"A+","A-","B+","B-","O+","O-","AB+","AB-"};
        for (int i =0; i< 8; i++){
            if (bloodGroups[i].equals(bloodGroup))
                return i;
        }
        return 0;
    }
}
