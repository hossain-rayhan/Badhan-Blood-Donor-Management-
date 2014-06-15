package com.appsomehow.badhan.helper;


import com.appsomehow.badhan.model.Donor;

public class DonorInfoValidation {

    public static boolean isAllInformationFilled(Donor donor){
        if (donor.getMobile() != null && donor.getMobile().length()>0 && donor.getName() != null && donor.getName().length() > 0 && donor.getNoOfDonation() >=0) {
            return true;
        }
        return false;
    }
}
