package com.appsomehow.badhan.helper;

import android.content.Context;
import com.appsomehow.badhan.model.Donor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rayhan on 6/29/2014.
 */
public class CSVToDbHelper {
    public static void readCSVAndInserIntoDb(Context ctx, int resourceId, DbTableName dbTableName) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        InputStream inputStream = ctx.getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String readLine;
        Donor donor;
        try {
            while ((readLine = br.readLine()) != null) {
                String[] result = readLine.split(",");
                if (dbTableName.equals(DbTableName.Donor)) {
                    Date donationDate = sdf.parse(result[4]);
                    donor = new Donor(0, result[1], result[2],result[3], donationDate, Integer.parseInt(result[5]),result[6],result[7],result[8]);
                    DbManager.getInstance().addDonor(donor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
