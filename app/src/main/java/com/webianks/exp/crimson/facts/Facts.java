package com.webianks.exp.crimson.facts;

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
public class Facts extends Fragment {
    private ProgressBar fact_progressBar;
    RecyclerView fact_recyclerView;
    private String TAG = Facts.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.facts, container, false);

        fact_progressBar = (ProgressBar) view.findViewById(R.id.fact_progress);

        fact_recyclerView = (RecyclerView) view.findViewById(R.id.fact_recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        fact_recyclerView.setLayoutManager(llm);


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getFactDetails();
    }

    private void getFactDetails() {

        String url = "http://webianks.com/crimson/all_facts.json";

        VolleySingleton volleySingleton = VolleySingleton.getInstance();
        RequestQueue requestQueue = volleySingleton.getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //got the response

                parseFacts(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try {
                    Toast.makeText(getActivity(),"Can't fetch facts.",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        requestQueue.add(jsonObjectRequest);


    }


    private void parseFacts(JSONObject response) {

        try {

            List<AllFacts> list = new ArrayList<>();
            AllFacts allFacts;

            JSONArray factsArray =  response.getJSONArray("all_facts");

            for (int i=0;i<factsArray.length();i++){

                JSONObject factObject = factsArray.getJSONObject(i);

                String fact = factObject.getString("facts");
                String source = factObject.getString("source");

                allFacts = new AllFacts();

                allFacts.setFact(fact);
                allFacts.setSource(source);

                list.add(allFacts);

            }

            showFacts(list);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void showFacts(List<AllFacts> list) {

        FactRecyclerViewAdapter adapter = new FactRecyclerViewAdapter(getActivity(), list);
        fact_recyclerView.setAdapter(adapter);
        fact_progressBar.setVisibility(View.INVISIBLE);

    }

}
