package com.example.financingappcurrent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financingappcurrent.REST.RESTControl;

import static com.example.financingappcurrent.Constants.address;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void connectToSystem(View v) {
        EditText login = findViewById(R.id.loginName);
        EditText psw = findViewById(R.id.loginPsw);
        String dataToSend = "{\"login\":\"" + login.getText().toString() + "\", \"psw\":\"" + psw.getText().toString() + "\"}";
        CheckBox checkBox = findViewById(R.id.checkBox);

        if (checkBox.isChecked()) {
            CompanyLogin companyLogin = new CompanyLogin();
            companyLogin.execute(dataToSend);
        } else {
            PersonLogin  personLogin = new PersonLogin();
            personLogin.execute(dataToSend);
        }

    }

    private  final class PersonLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LoginActivity.this, "Validating login data", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = address + "/untitled_war_exploded/user/personLogin";
            String postDataParameters = params[0];
            System.out.println("SENT: " + postDataParameters);
            try {
                return RESTControl.sendPost(url, postDataParameters);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error while getting data from web";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("RECEIVED: " + result);
            if (result != null && !result.equals("Wrong credentials")) {
                try {
                    Intent userCats = new Intent(LoginActivity.this, PersonCategories.class); // is kurio i kuri activity einu irasyt
                    userCats.putExtra("id", result);
                    startActivity(userCats);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Wrong data", Toast.LENGTH_LONG).show();
                }
            }
            Toast.makeText(LoginActivity.this, "Wrong data", Toast.LENGTH_LONG).show();
        }
    }


    private final class CompanyLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(LoginActivity.this, "Validating login data", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = address + "/untitled_war_exploded/user/companyLogin";
            String postDataParameters = params[0];
            System.out.println("SENT: " + postDataParameters);
            try {
                return RESTControl.sendPost(url, postDataParameters);
            } catch (Exception e) {
                e.printStackTrace();
                return "Error while getting data from web";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("RECEIVED: " + result);
            if (result != null && !result.equals("Wrong credentials")) {
                try {
                    Intent userCats = new Intent(LoginActivity.this, CompanyCategories.class); // is kurio i kuri activity einu irasyt
                    userCats.putExtra("id", result);
                    startActivity(userCats);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Wrong data", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}