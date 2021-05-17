package com.example.crypto_noticer;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class login_Activity extends AppCompatActivity {

    private EditText text1;
    private EditText text2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_account);
        text1 = findViewById(R.id.input_email);
        text2 = findViewById(R.id.input_password);
    }
    public void createClick(View view){

    }

    public void loginClick(View view) throws JSONException {
        final String email = text1.getText().toString();
        String password = text2.getText().toString();


        OkHttpClient client = new OkHttpClient();
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String url = "https://cryp002.herokuapp.com/login";
        JSONObject jObj = new JSONObject();

        jObj.put("username", email);
        jObj.put("password", password);

        String jsonString = jObj.toString();
        RequestBody body = RequestBody.create(JSON, jsonString);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if(response.isSuccessful()){

                    final String myResponse = response.body().string();

                    try {
                        JSONObject jObject = new JSONObject(myResponse);
                        final String result = jObject.getString("status");
                        //Toast.makeText(login_Activity.this, result, Toast.LENGTH_SHORT).show();

                        runOnUiThread(new Runnable() {
                            public void run() {


                                if(result.equals("You have successful login")){
                                    MainActivity.login_statu = true;
                                    MainActivity.account = email;
                                }
                                Toast.makeText(login_Activity.this, result, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        Log.d("d","here is error");
                        e.printStackTrace();
                    }
                }
                else{
                    Log.d("resFail", "your call is failed");
                }
            }
            @Override
            public void onFailure(Call call, IOException e){
                e.printStackTrace();
            }
        });
        finish();
    }
}
