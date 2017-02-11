package com.webianks.exp.crimson.all_tests;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.webianks.exp.crimson.R;

import java.util.ArrayList;
import java.util.List;


public class Tests extends Fragment implements TestRecyclerViewAdapter.RecyclerClickListener {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tests, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getTestNames();

    }

    private void getTestNames() {

        String[] testnames = {"Near Vision","Far Vision", "Colour Blindness","Amsler Grid","Visual Acuity","Astigmatism"};

        List<TestNames> list = new ArrayList<>();
        TestNames test;


        for (int i=0; i<testnames.length;i++){

            test = new TestNames();
            test.setNames(testnames[i]);
            list.add(test);
        }

        showTests(list);

    }

    private void showTests(List<TestNames> list) {

        TestRecyclerViewAdapter adapter = new TestRecyclerViewAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.INVISIBLE);

        adapter.setRecyclerViewListener(this);

    }



    @Override
    public void onClick(String name, int index) {

        Intent intent = new Intent(getActivity(),StartTest.class);
        intent.putExtra("name",name);
        intent.putExtra("type",index);
        startActivity(intent);

    }


}