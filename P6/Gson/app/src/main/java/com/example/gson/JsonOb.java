package com.example.gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JsonOb {


    public static void createObj(JSONObject json) {
        JSONArray arrayJsn=null;
         List<PostalCode> list= new ArrayList<>();
        String countryCode="";
        String adminName2="";
        String adminName1="";
        String placeName="";
        String adminName3="";

        try {
             arrayJsn=json.getJSONArray("postalCodes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int x=0;x<arrayJsn.length();x++){
            JSONObject gson = null;

            try {
                gson = arrayJsn.getJSONObject(x);
            } catch (JSONException e) {
                e.printStackTrace();
            }

                try {
                    countryCode = gson.getString("countryCode");
                }catch (Exception e){
                    countryCode ="";
                }
            try {
                adminName2 = gson.getString("adminName2");
            }catch (Exception e){
                adminName2 ="";
            }
                try {
                    adminName1 = gson.getString("adminName1");
                }catch (Exception e){
                    adminName1 ="";
                }
                try {
                    placeName = gson.getString("placeName");
                }catch (Exception e){
                    placeName ="";
                }
                try {
                    adminName3 = gson.getString("adminName3");
                }catch (Exception e){
                    adminName3 ="";
                }


            PostalCode postal = new PostalCode(countryCode,adminName1,adminName2,adminName3,placeName);

            list.add(postal);

        }

        PostalCodesVO.setPostalCodes(list);



    }
    public static String[] getArray(){
        ArrayList<String> lista = new ArrayList<String>();

        for(int x=0;x<PostalCodesVO.getPostalCodes().size();x++){
            boolean aux=false;

            for (int d=0;d<lista.size();d++){
                if(PostalCodesVO.getPostalCodes().get(x).getCountryCode().equalsIgnoreCase(lista.get(d))){
                    aux=true;
                }
            }
            if(!aux){
                lista.add(PostalCodesVO.getPostalCodes().get(x).getCountryCode());
            }

        }
        String[] array= new String[lista.size()];
        for(int i=0;i<lista.size();i++){
            array[i]=lista.get(i);
        }
        for(int i=0;i<lista.size();i++){
            System.out.println("asvca"+array[i]);

        }
        return array;
    }
}
