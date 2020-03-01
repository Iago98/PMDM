package com.example.gson;

import java.util.ArrayList;
import java.util.List;

public class PostalCodesVO {
    private static List<PostalCode> postalCodes;

    public static List<PostalCode> getPostalCodes() {
        if(postalCodes==null){
            postalCodes=new ArrayList<>();
        }
        return postalCodes;
    }

    public static void setPostalCodes(List<PostalCode> postalCode) {
        postalCodes.clear();
        for (int i=0;i<postalCode.size();i++){
            postalCodes.add(postalCode.get(i));
        }
    }

    @Override
    public String toString() {
        return "PostalCodesVO{}";
    }
}