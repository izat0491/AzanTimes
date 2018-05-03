package com.example.user.azantimes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String HttpURL;
    TextView dateazan, imsak, subuh, syuruk, zohor, asar, maghrib, isyak, location, zone1;
    String dateHolder, imsakHolder, subuhHolder, syurukHolder, zohorHolder, asarHolder, maghribHolder, isyakHolder, locationHolder;
    Spinner dropdown;
    List<String> allnames = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateazan = (TextView) findViewById(R.id.date);
        imsak = (TextView) findViewById(R.id.imsak);
        subuh = (TextView) findViewById(R.id.subuh);
        syuruk = (TextView) findViewById(R.id.syuruk);
        zohor = (TextView) findViewById(R.id.zohor);
        asar = (TextView) findViewById(R.id.asar);
        maghrib = (TextView) findViewById(R.id.maghrib);
        isyak = (TextView) findViewById(R.id.isyak);
        location = (TextView) findViewById(R.id.location);
        zone1 = (TextView) findViewById(R.id.zone);

        dropdown = (Spinner) findViewById(R.id.spinner);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String zone = dropdown.getSelectedItem().toString();
                String lokasi = dropdown.getSelectedItem().toString();

                int startindex = zone.indexOf('>');
                int startindex2 = lokasi.indexOf('<');
                int endindex2 = lokasi.indexOf('>');

                lokasi = lokasi.substring(startindex2 + 1, endindex2);
                zone = zone.substring(startindex + 1);

                HttpURL = "http://api.azanpro.com/times/today.json?zone=" + zone + "&format=12-hour";
                location.setText("Location: " + lokasi);
                zone1.setText("Zone: " + zone);

                new GetHttpResponse(MainActivity.this).execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        new Getstates(MainActivity.this).execute();


    }

    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        String JSonResult;

        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass(HttpURL);
            try {
                httpServicesClass.ExecuteGetRequest();

                if (httpServicesClass.getResponseCode() == 200) {
                    JSonResult = httpServicesClass.getResponse();

                    if (JSonResult != null) {
                        try {
                            JSONObject lokasi = new JSONObject(JSonResult);


                            JSONArray jsonArray = lokasi.getJSONArray("locations");
                            locationHolder = jsonArray.getString(0);

                            JSONObject jsonObject = new JSONObject(JSonResult);

                            jsonObject = jsonObject.getJSONObject("prayer_times");

                            dateHolder = jsonObject.getString("date");
                            imsakHolder = jsonObject.getString("imsak");
                            subuhHolder = jsonObject.getString("subuh");
                            syurukHolder = jsonObject.getString("syuruk");
                            zohorHolder = jsonObject.getString("zohor");
                            asarHolder = jsonObject.getString("asar");
                            maghribHolder = jsonObject.getString("maghrib");
                            isyakHolder = jsonObject.getString("isyak");

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }
                } else {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            dateazan.setText("Date:  " + dateHolder);
            imsak.setText("Imsak:  " + imsakHolder);
            subuh.setText("Subuh:  " + subuhHolder);
            syuruk.setText("Syuruk:  " + syurukHolder);
            zohor.setText("Zohor:  " + zohorHolder);
            asar.setText("Asar:  " + asarHolder);
            maghrib.setText("Maghrib:  " + maghribHolder);
            isyak.setText("Isyak:  " + isyakHolder);

        }
    }

    // JSON parse class started from here.
    private class Getstates extends AsyncTask<Void, Void, Void> {
        public Context context;

        String JSonResult;

        public Getstates(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServicesClass httpServicesClass = new HttpServicesClass("http://api.azanpro.com/zone/zones.json");
            try {
                httpServicesClass.ExecuteGetRequest();

                if (httpServicesClass.getResponseCode() == 200) {
                    JSonResult = httpServicesClass.getResponse();

                    if (JSonResult != null) {
                        try {
                            JSONObject lokasi = new JSONObject(JSonResult);

                            JSONArray jsonArray = lokasi.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                String lokasii = c.getString("lokasi");
                                String zone = c.getString("zone");
                                String negeri = c.getString("negeri");
                                String data = negeri + "<" + lokasii + ">" + zone;
                                allnames.add(data);


                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                    }
                } else {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, allnames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);

        }
    }
}
