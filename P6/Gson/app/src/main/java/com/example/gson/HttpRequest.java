package com.example.gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class HttpRequest {
    public static void peticion(String cp) {
        URL obj = null;
        StringBuffer response = null;
        HttpURLConnection con = null;
        int responseCode;
        BufferedReader in;
        String inputLine;
        String url = "http://api.geonames.org/postalCodeSearchJSON?postalcode="+cp+"&maxRows=100&username=dispositivos_moviles";
        try {
            obj = new URL(url);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            con = (HttpURLConnection) obj.openConnection();


            responseCode = con.getResponseCode();
            System.out.println("Sending GET to URL: " + url);
            System.out.println("Response Code" + responseCode);


            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);

            }
            in.close();

            try {
                JSONObject myresponst = new JSONObject(response.toString());
                JsonOb.createObj(myresponst);
            } catch (JSONException a) {
                a.printStackTrace();
            }

        } catch (IOException e) {

        }

    }
}
