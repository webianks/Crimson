package com.webianks.exp.crimson;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by R Ankit on 04-10-2016.
 */

public class AstigmatismTest extends AppCompatActivity implements View.OnClickListener {


    private TextView directions;
    private Button startTest;
    private ImageView image;
    private int type;
    int counter=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.color_blind);


        directions = (TextView) findViewById(R.id.test_instructions);
        startTest = (Button) findViewById(R.id.startTest);
        image = (ImageView) findViewById(R.id.image);

        startTest.setOnClickListener(this);
        image.setImageResource(R.drawable.ast1);

        setInfo();

    }

    private void setInfo() {


        String info = getResources().getString(R.string.ast_test) ;

        directions.setText(Html.fromHtml(info));

        if (Build.VERSION.SDK_INT >= 24) {
            directions.setText(Html.fromHtml(info, Html.FROM_HTML_MODE_LEGACY));
        } else {
            directions.setText(Html.fromHtml(info));
        }
    }

    @Override
    public void onClick(View view) {

        counter++;

        if (counter<=5){

            switch (counter){

                case 2:
                    image.setImageResource(R.drawable.ast2);
                    break;

                case 3:
                    image.setImageResource(R.drawable.ast3);
                    break;

                case 4:
                    image.setImageResource(R.drawable.ast4);
                    break;

                case 5:
                    image.setImageResource(R.drawable.ast5);
                    break;

            }

        }else{

            image.setVisibility(View.GONE);
            directions.setText("If your answer was yes in any wheel, you should make an immediate appointment to see an eye doctor.");
            directions.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            startTest.setVisibility(View.GONE);
        }

    }
}