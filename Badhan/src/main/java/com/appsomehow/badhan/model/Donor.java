package com.appsomehow.badhan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable
public class Donor {
    @DatabaseField(id = true)
    private String mobile;

    @DatabaseField
    private String name;

    @DatabaseField
    private String bloodGroup;

    @DatabaseField
    private Date lastDonationDate;

    @DatabaseField
    private int noOfDonation;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Date getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(Date lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public int getNoOfDonation() {
        return noOfDonation;
    }

    public void setNoOfDonation(int noOfDonation) {
        this.noOfDonation = noOfDonation;
    }
}
