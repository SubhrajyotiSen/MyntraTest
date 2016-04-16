package com.subhrajyoti.test;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    ArrayList<Model> arrayList;
    String param;
    RecyclerView recyclerView;
    GridLayoutManager linearLayout;
    MainAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        param = getIntent().getStringExtra("title");
        arrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayout = new GridLayoutManager(this,2);
        mainAdapter = new MainAdapter(this,arrayList);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(mainAdapter);
        new FetchData().execute();
    }

    public class FetchData extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            Boolean success = false;
            StringBuilder data = new StringBuilder("");
            try {
                Log.d("DEBUG", "inside try");
                URL url = new URL("http://developer.myntra.com/search/data/"+param);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    data.append(line);
                    Log.d("DEBUG", line);
                }

                if (inputStream != null) {
                    inputStream.close();
                    parseResult(data.toString());
                    Log.d("DEBUG", "input stream not null");
                    success = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return success;
        }
    }

    void parseResult(String data) {
        try {
            JSONObject response = new JSONObject(data);
            JSONObject data2 = response.getJSONObject("data");
            JSONObject results = data2.getJSONObject("results");
            JSONArray products = results.getJSONArray("products");
            for (int i=0;i<products.length();i++) {
                JSONObject jsonObject = products.getJSONObject(i);
                Model model = new Model();
                model.setIMG_URL(jsonObject.getString("search_image"));
                model.setBUY_URL(jsonObject.getString("dre_landing_page_url"));
                arrayList.add(model);
                Log.d("haha",model.getIMG_URL());
            }
            mainAdapter.notifyDataSetChanged();



            /*for(int i = 0; i<results.length(); ++i) {
                JSONObject object= results.getJSONObject(i);
                String imagePath = object.get("poster_path").toString();
                PosterGridItem item = new PosterGridItem();

                item.setImage(imageBase+imagePath+"&api_key="+KEY);
                item.setName(object.get("title").toString());
                item.setRelease(object.get("release_date").toString());
                item.setRating(object.get("vote_average").toString());
                item.setOverview(object.get("overview").toString());
                Log.d("DEBUG", "Image path:" + imageBase+imagePath+"&api_key="+KEY);
                gridData.add(item);
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
