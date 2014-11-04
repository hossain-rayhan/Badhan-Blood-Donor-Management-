package com.appsomehow.badhan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;

@DatabaseTable
public class Donor {
    @DatabaseField(generatedId = true,allowGeneratedIdInsert=true)
    private int id;

    @DatabaseField
    private String mobile;

    @DatabaseField
    private String name;

    @DatabaseField
    private String bloodGroup;

    @DatabaseField
    private Date lastDonationDate;

    @DatabaseField
    private int noOfDonation;

    @DatabaseField
    private String address;

    @DatabaseField
    private String preferredArea;

    @DatabaseField
    private String comment;

    public Donor() {
    }

    public Donor(int id, String mobile, String name, String bloodGroup, Date lastDonationDate, int noOfDonation, String address, String preferredArea, String comment) {
        this.id = id;
        this.mobile = mobile;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.lastDonationDate = lastDonationDate;
        this.noOfDonation = noOfDonation;
        this.address = address;
        this.preferredArea = preferredArea;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPreferredArea() {
        return preferredArea;
    }

    public void setPreferredArea(String preferredArea) {
        this.preferredArea = preferredArea;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
