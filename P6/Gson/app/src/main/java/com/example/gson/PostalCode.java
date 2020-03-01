package com.example.gson;

public class PostalCode {

    private String adminName1;
    private String adminName2;
    private String adminName3;
    private String placeName;
    private String countryCode;

    public String getCountryCode() {

        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAdminName2() {
        return adminName2;
    }

    public void setAdminName2(String adminName2) {
        this.adminName2 = adminName2;
    }

    public String getAdminName3() {
        return adminName3;
    }

    public void setAdminName3(String adminName3) {
        this.adminName3 = adminName3;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAdminName1() {
        return adminName1;
    }

    public void setAdminName1(String adminName1) {
        this.adminName1 = adminName1;
    }

    @Override
    public String toString() {
        return
                "adminName1=" + adminName1 +
                ", adminName2=" + adminName2 +
                ", adminName3=" + adminName3 +
                ", placeName=" + placeName;
    }

    public PostalCode(String countryCode,String adminName1, String adminName2, String adminName3, String placeName) {
        this.countryCode=countryCode;
        this.adminName1 = adminName1;
        this.adminName2 = adminName2;
        this.adminName3 = adminName3;
        this.placeName = placeName;
    }
}