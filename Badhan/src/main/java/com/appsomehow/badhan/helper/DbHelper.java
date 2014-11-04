package com.appsomehow.badhan.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.appsomehow.badhan.R;
import com.appsomehow.badhan.model.Donor;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "DonorDb.sqlite";
    private static final int DATABASE_VERSION = 2;
    private Context context;

    private Dao<Donor, Integer> donorDao = null;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Donor.class);
        } catch (SQLException e) {
            Log.e(DbHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        List<String> allSql = new ArrayList<String>();
        try {
            /*TableUtils.dropTable(connectionSource, Donor.class,true);
            TableUtils.createTable(connectionSource, Donor.class);
            CSVToDbHelper.readCSVAndInserIntoDb(context, R.raw.donor, DbTableName.Donor);*/
            for (String sql : allSql) {
                sqLiteDatabase.execSQL(sql);
            }
        } catch (Exception e) {
            Log.e(DbHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }
    }

    public Dao<Donor, Integer> getDonorDao() {
        if (null == donorDao) {
            try {
                donorDao = getDao(Donor.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return donorDao;
    }

}
