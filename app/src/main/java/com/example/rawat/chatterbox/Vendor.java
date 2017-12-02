package com.example.rawat.chatterbox;

import com.google.gson.annotations.SerializedName;

/**
 * Created by WIN10 on 12/2/2017.
 */

public class Vendor {

    @SerializedName("userId")
    private int supplierId;
    @SerializedName("color")
    private String colors;
    @SerializedName("name")
    private String companyName;

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
