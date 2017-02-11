package com.webianks.exp.crimson.tips;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.webianks.exp.crimson.R;
import com.webianks.exp.crimson.network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by R Ankit on 11-02-2017.
 */

@SuppressLint("ValidFragment")
public class HealthyEyeTips extends Fragment {
    private ProgressBar health_progressBar;
    RecyclerView health_recyclerView;
    private String TAG = HealthyEyeTips.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.health,container,false);

        health_progressBar = (ProgressBar) view.findViewById(R.id.health_progress);

        health_recyclerView = (RecyclerView) view.findViewById(R.id.health_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        health_recyclerView.setLayoutManager(llm);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        gethealttips();
    }

    private void gethealttips() {


        String url = "http://webianks.com/crimson/tipsandmaintain.json";

        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //got the response

                parseHealthtips(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    Toast.makeText(getActivity(),"Can't fetch tips.",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void parseHealthtips(JSONObject response) {

        try {

            List<SleepBetterDetails> list = new ArrayList<>();
            SleepBetterDetails sleepBetterDetails;

            JSONArray TipsArray =  response.getJSONArray("tips");

            for (int i=0;i<TipsArray.length();i++){

                JSONObject factObject = TipsArray.getJSONObject(i);

                String title = factObject.getString("title");
                String description = factObject.getString("description");

                sleepBetterDetails = new SleepBetterDetails();

                sleepBetterDetails.setSleep_title(title);
                sleepBetterDetails.setSleep_desc(description);

                list.add(sleepBetterDetails);

            }

            showHealthtips(list);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showHealthtips(List<SleepBetterDetails> list) {

        SleepBetterRecyclerViewAdapter adapter = new SleepBetterRecyclerViewAdapter(getActivity(),list);
        health_recyclerView.setAdapter(adapter);
        health_progressBar.setVisibility(View.INVISIBLE);

    }

}
