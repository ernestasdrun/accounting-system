package com.example.financingappcurrent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financingappcurrent.Objects.FinanceSystem;
import com.example.financingappcurrent.REST.RESTControl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.financingappcurrent.Constants.address;

public class FinancingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financing);
        GetLibraries getLibraries = new GetLibraries();
        getLibraries.execute("Fake Data");
    }

    public void returnLibrary(View v) {
        GetLibraries getLibraries = new GetLibraries();
        getLibraries.execute("Fake Data");

    }

    private final class GetLibraries extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String output = "";
            String params = strings[0];
            System.out.println("SENT" + params);
            String url = address + "/LibraryGuiHibernate_war_exploded/library/libList";
            try {
                return RESTControl.sendGet(url);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }
    }
}