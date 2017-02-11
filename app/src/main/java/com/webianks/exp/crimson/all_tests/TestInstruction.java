package com.webianks.exp.crimson.all_tests;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.webianks.exp.crimson.R;


public class TestInstruction extends AppCompatActivity implements View.OnClickListener {

    private TextView directions;
    private Button startTest;
    private ImageView image;
    private int type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.test_instructions);

        Intent intent = getIntent();
        type = intent.getIntExtra("type",-1);

        directions = (TextView) findViewById(R.id.test_instructions);
        startTest = (Button) findViewById(R.id.startTest);
        image = (ImageView) findViewById(R.id.image);

        startTest.setOnClickListener(this);

        setInfo(type);

    }

    private void setInfo(int type) {


        String[] infos = getResources().getStringArray(R.array.test_directions) ;

        directions.setText(Html.fromHtml(infos[type]));

        if (Build.VERSION.SDK_INT >= 24) {
            directions.setText(Html.fromHtml(infos[type], Html.FROM_HTML_MODE_LEGACY));
        } else {
            directions.setText(Html.fromHtml(infos[type]));
        }

    }

    @Override
    public void onClick(View view) {

        directions.setVisibility(View.INVISIBLE);
        startTest.setVisibility(View.INVISIBLE);


        switch (type){
            case 0:
                image.setImageResource(R.drawable.near);
                break;
            case 1:
                image.setImageResource(R.drawable.far);
                break;
            case 4:
                image.setImageResource(R.drawable.visual);
                break;

        }

        image.setVisibility(View.VISIBLE);
    }
}
