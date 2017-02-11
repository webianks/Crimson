package com.webianks.exp.crimson.reports;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.webianks.exp.crimson.R;

/**
 * Created by R Ankit on 11-02-2017.
 */


public class FullViewReport extends AppCompatActivity{

    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_view);

        image = (ImageView) findViewById(R.id.image);

        String url = getIntent().getStringExtra(getString(R.string.image_url));

        Glide.with(this).load(url).into(image);

    }
}
