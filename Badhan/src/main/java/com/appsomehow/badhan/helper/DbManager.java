package com.appsomehow.badhan.helper;

import android.content.Context;
import com.appsomehow.badhan.model.Donor;
import java.sql.SQLException;
import java.util.List;

public class DbManager {

    static private DbManager instance;

    static public void init(Context ctx) {
        if (null==instance) {
            instance = new DbManager(ctx);
        }
    }

    static public DbManager getInstance() {
        return instance;
    }

    private DbHelper helper;
    private DbManager(Context ctx) {
        helper = new DbHelper(ctx);
    }

    private DbHelper getHelper() {
        return helper;
    }

    public List<Donor> getAllDonors() {
        List<Donor> donorList = null;
        try {
            donorList = getHelper().getDonorDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donorList;
    }

    public Donor getDonorWithMobile(String donorMobile){
        Donor donor = null;
        try {
            donor = getHelper().getDonorDao().queryForId(donorMobile);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return donor;
    }

    public void addDonor(Donor donor) {
        try {
            getHelper().getDonorDao().create(donor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDonor(Donor donor) {
        try {
            getHelper().getDonorDao().update(donor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
