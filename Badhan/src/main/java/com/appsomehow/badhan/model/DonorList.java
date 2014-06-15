package com.appsomehow.badhan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class DonorList {

    @DatabaseField
    private int id;

    @DatabaseField
    private String mobile;

    @DatabaseField
    private String name;
}
