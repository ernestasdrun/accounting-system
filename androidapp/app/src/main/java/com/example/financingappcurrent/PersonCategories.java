package com.example.financingappcurrent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.financingappcurrent.Objects.Category;
import com.example.financingappcurrent.REST.RESTControl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.financingappcurrent.Constants.address;

public class PersonCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_categories);
        Intent from = this.getIntent();

        String userId = "{\"userId\":\"" + from.getSerializableExtra("id") + "\"}";
        PersonCategoriesGet personCategoriesGet = new PersonCategoriesGet();
        personCategoriesGet.execute(userId);
    }

    private final class PersonCategoriesGet extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String output = "";
            String params = strings[0];
            System.out.println("SENT" + params);
            String url = address + "/untitled_war_exploded/category/personCatList";
            try {
                return RESTControl.sendPost(url, params);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("RECEIVED: " + result);
            if (result != null) {
                try {
                    Type listType = new TypeToken<ArrayList<Category>>() {
                    }.getType();
                    final List<Category> userCats = new Gson().fromJson(result, listType);
                    ListView listView = findViewById(R.id.personCategoriesList);
                    ArrayAdapter<Category> arrayAdapter = new ArrayAdapter<>(PersonCategories.this, android.R.layout.simple_list_item_1, userCats);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast info = Toast.makeText(PersonCategories.this, "Selected category: " + userCats.get(position).toString(), Toast.LENGTH_LONG);
                            info.show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PersonCategories.this, "Wrong data", Toast.LENGTH_LONG);
                }
            } else {
                Toast.makeText(PersonCategories.this, "Wrong data", Toast.LENGTH_LONG);
            }
        }
    }
}