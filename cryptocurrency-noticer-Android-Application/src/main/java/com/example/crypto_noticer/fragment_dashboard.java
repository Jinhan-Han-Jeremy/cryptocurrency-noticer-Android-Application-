package com.example.crypto_noticer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class fragment_dashboard extends Fragment {
    ViewGroup viewGroup;
    private TextView name_butto;
    private Button name_button;
    private Button rates_button;
    private Button price_button;

    private String name;
    private String price;
    private String marketCap;
    private String Volume24;
    private String rate;

    private TextView order_dummy;
    private TextView name_dummy;
    private TextView rates_dummy;
    private TextView price_dummy;
    private TextView current_rate;
    private TextView market_view;
    private TextView volume_view;


    private ArrayList<cryptoclass> cryptos;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false); //layout xml added

        name_button = (Button) viewGroup.findViewById(R.id.name_button);
        rates_button = (Button) viewGroup.findViewById(R.id.rates_button);
        price_button = (Button) viewGroup.findViewById(R.id.price_button);

        getSingleCoinValue();

        current_rate = (TextView) viewGroup.findViewById(R.id.current_rates);
        name_dummy = (TextView) viewGroup.findViewById(R.id.name_dummy);
        rates_dummy = (TextView) viewGroup.findViewById(R.id.rates_dummy);
        price_dummy = (TextView) viewGroup.findViewById(R.id.price_dummy);
        market_view = (TextView) viewGroup.findViewById(R.id.marketcap);
        volume_view = (TextView) viewGroup.findViewById(R.id.volume);



        getSingleCoinValue();

        name_dummy.setText(name);
        current_rate.setText(rate);
        rates_dummy.setText(rate);
        price_dummy.setText(price);
        market_view.setText(marketCap);
        volume_view.setText(Volume24);

        name_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int n = 30;
                sorter(cryptos,1);

            }
        });

        rates_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sorter(cryptos,2);
            }
        });

        price_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sorter(cryptos,3);
            }
        });



        return viewGroup;
    }


    public void sorter(ArrayList <cryptoclass> cryptolists, int pick) {
        String temp = "";
        cryptoclass tempobj = new cryptoclass();

        int n = cryptos.size();

        String theCompared = "";
        String theCompared_2 = "";
        double numericalCompared = 0;
        double numericalCompared_2 = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++)
            {
                // to compare one string with other strings

                switch (pick) {
                    case 1:
                        theCompared = cryptolists.get(i).getName();
                        theCompared_2 = cryptolists.get(j).getName();
                        numericalCompared=0;
                        numericalCompared_2 = 0;
                        break;
                    case 2:
                        numericalCompared = Double.parseDouble(cryptolists.get(i).getRates());
                        numericalCompared_2 = Double.parseDouble(cryptolists.get(j).getRates());
                        theCompared = "";
                        theCompared_2 = "";
                        break;
                    case 3:
                        numericalCompared = Double.parseDouble(cryptolists.get(i).getPrice());
                        numericalCompared_2 = Double.parseDouble(cryptolists.get(j).getPrice());
                        theCompared = "";
                        theCompared_2 = "";
                        break;

                }

                if (theCompared.compareTo(theCompared_2) > 0 && numericalCompared==0)
                {
                    Collections.swap(cryptos, i, j);
                }
                else if ((numericalCompared > numericalCompared_2))
                {
                    Collections.swap(cryptos, i, j);
                }

            }
        }

    }

    public void getSingleCoinValue() {
        String url = "https://cryp002.herokuapp.com/getData";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    final String myResponse = response.body().string();

                    try {
                        //JSONObject jObject = new JSONObject(myResponse);
                        JSONArray arr = new JSONArray(myResponse); // here gets the json array return from server
                        JSONObject obj = (JSONObject) arr.get(0); // choose the first item in the json array
                        // if you want the second item just replace 0 to 1


                        setCryptolist(arr);

                        name = cryptos.get(0).getName();             // you call the field "name" under the object
                        // set to final in order to settext with runOnUiTHread
                        price = obj.getString("price");
                        marketCap = obj.getString("MarketCap");
                        Volume24 = obj.getString("Volume24");
                        rate = obj.getString("rateOfChange");


                        //get the past records
                        JSONArray pastArray = obj.getJSONArray("fifthteen_minutes"); // fifthteen_minute is a json array
                        obj = (JSONObject) pastArray.get(0); // get a json object under the json array
                        final String strPastPrice1 = obj.getString("price");
                        obj = (JSONObject) pastArray.get(1); // get the second json object under the json array
                        final String strPastPrice2 = obj.getString("price");



                        /*runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                rates_dummy.setText(name);

                            }
                        });*/


                    } catch (JSONException e) {
                        Log.d("d", "here is error");
                        e.printStackTrace();
                    }
                } else {
                    Log.d("resFail", "your call is failed");
                }
            }


            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });



    }

    public ArrayList<cryptoclass> getCryptolist()
    {
        return cryptos;
    }

    public void setCryptolist(JSONArray arr) throws JSONException {
        int n;
        n = (int)arr.length();   //number of crypto
        cryptos = new ArrayList<cryptoclass>();

        for(int i=0; i < n;i++)
        {
            JSONObject objj = (JSONObject) arr.get(i); // choose the first item in the json array
            // if you want the second item just replace 0 to 1

            cryptoclass ct = new cryptoclass(objj.getString("name"), objj.getString("price"), objj.getString("rateOfChange"), objj.getString("MarketCap"), objj.getString("Volume24"));

            cryptos.add(ct);

            System.out.println(cryptos.get(i).getName());

        }

    }





}
