package com.subhrajyoti.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

public class MainActivity extends AppCompatActivity {


    ListView listview;
    ArrayList<String> arrayList;
    ArrayAdapter arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrayList = new ArrayList<>();

        CalendarProvider calendarProvider = new CalendarProvider(this);
        List<me.everything.providers.android.calendar.Calendar> calendarList = calendarProvider.getCalendars().getList();
        final List<Event> events = calendarProvider.getEvents(calendarList.get(2).id).getList();
        for (int i=0;i<events.size();i++)
            arrayList.add(events.get(i).title);

        listview = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listview.setAdapter(arrayAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,ViewActivity.class);
                String title = events.get(position).title;
                String desc = events.get(position).description;
                Toast.makeText(getApplicationContext(),events.get(position).status,Toast.LENGTH_LONG).show();
                intent.putExtra("title",getOccassion(title,desc));
                startActivity(intent);
            }
        });



    }

    public String getOccassion(String string,String s2)
    {
        String[] birthday = new String[]{"birthday","bday"};
        String[] party = new String[]{"party","parties","bash","nightclub","gala","wing-ding"};
        String[] meeting = new String[]{"meeting","appointment","conference","interview"};
        String[] wedding = new String[]{"wedding","marriage","engagement","mehandi","reception"};
        String[] vacation = new String[]{"vacation","holiday","holidays"};
        String[] casual = new String[]{"date","get together","reunion","meetup"};
        string= string.toLowerCase();
        s2 = s2.toLowerCase();
        for (String s : birthday)
        {
            if (string.contains(s)||s2.contains(s))
            {
                return "birthday";
            }
        }
        for (String s : party)
        {
            if (string.contains(s)||s2.contains(s))
            {
                return "party";
            }
        }

        for (String s : meeting)
        {
            if (string.contains(s)||s2.contains(s))
            {
                return "formal";
            }
        }
        for (String s : casual)
        {
            if (string.contains(s)||s2.contains(s))
            {
                return "casual";
            }
        }
        for (String s : wedding)
        {
            if (string.contains(s)||s2.contains(s))
            {
                return "wedding";
            }
        }

        for (String s : vacation)
        {
            if (string.contains(s)||s2.contains(s))
            {
                return "vacation";
            }
        }

        return "Nothing found";
    }


    }

