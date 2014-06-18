package com.appsomehow.badhan.helper;

public class Helper {

    public static boolean isEditAllChecked = false;

    public static int getBloodGroupIndex(String bloodGroup){
        String bloodGroups[] = {"A+","A-","B+","B-","O+","O-","AB+","AB-"};
        for (int i =0; i< 8; i++){
            if (bloodGroups[i].equals(bloodGroup))
                return i;
        }
        return 0;
    }
}
