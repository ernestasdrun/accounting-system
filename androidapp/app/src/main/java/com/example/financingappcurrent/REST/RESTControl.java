package com.example.financingappcurrent.REST;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RESTControl {
    private static OutputStream out;
    private static BufferedWriter writer;


    public static String sendPost(String p_url, String postDataParameters) throws Exception {
        URL url = new URL(p_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        setConnectionParams(connection, "POST");

        out = connection.getOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(postDataParameters);
        writer.flush();
        writer.close();
        out.close();

        int resCode = connection.getResponseCode();
        if (resCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }
        return null;
    }

    public static String sendGet(String p_url) throws IOException {
        URL url = new URL(p_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        int resCode = connection.getResponseCode();
        System.out.println("Response code: " + resCode);

        if (resCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();

            while ((line = in.readLine()) != null) {
                response.append(line);
                break;
            }
            in.close();
            return response.toString();
        } else {
            return "";
        }
    }

    public static String sendPut(String p_url, String postDataParameters) throws Exception {
        URL url = new URL(p_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        setConnectionParams(connection, "PUT");

        OutputStream out = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(postDataParameters);
        writer.flush();
        writer.close();
        out.close();

        int resCode = connection.getResponseCode();
        if (resCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }
        return null;
    }

    public static String sendDelete(String p_url, String postDataParameters) throws Exception {
        URL url = new URL(p_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        setConnectionParams(connection, "DELETE");

        OutputStream out = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(postDataParameters);
        writer.flush();
        writer.close();
        out.close();

        int resCode = connection.getResponseCode();
        if (resCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        }
        return null;
    }

    private static void setConnectionParams(HttpURLConnection connection, String type) throws ProtocolException {
        connection.setReadTimeout(20000);
        connection.setConnectTimeout(20000);
        connection.setRequestMethod(type);
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoInput(true);
        connection.setDoOutput(true);
    }
}
