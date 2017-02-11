package com.webianks.exp.crimson.sleep_better;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.webianks.exp.crimson.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by R Ankit on 11-02-2017.
 */

@SuppressLint("ValidFragment")
public class SleepBetter extends Fragment implements View.OnClickListener {

    private String TAG = SleepBetter.class.getSimpleName();
    private Button filterButton;
    private Button strainButton;
    private boolean monitoring;
    private ScreenReceiver mReceiver = new ScreenReceiver();
    private Intent filterIntent;
    private boolean filtering;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sleep_better, container, false);


        filterButton = (Button) view.findViewById(R.id.filter_button);
        strainButton = (Button) view.findViewById(R.id.eye_button);

        filterButton.setOnClickListener(this);
        strainButton.setOnClickListener(this);

        filterIntent = new Intent(getActivity(), ScreenFilterService.class);

        SharedPreferences sp = getActivity().getSharedPreferences("monitoring", MODE_PRIVATE);
        monitoring = sp.getBoolean("digital_monitoring", false);
        filtering = sp.getBoolean("filtering", false);

        if (!monitoring)
            strainButton.setText("Start Monitoring");
        else
            strainButton.setText("Stop Monitoring");


        if (!filtering)
            filterButton.setText("Start Filtering");
        else
            filterButton.setText("Stop Filtering");

        return view;

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.filter_button) {

            if (!filtering)
                startFiltering();
            else
                stopFiltering();
        } else if (view.getId() == R.id.eye_button) {


            if (!monitoring)
                digitalStrain();
            else
                disableDigitalEyeStrainMonitoring();


        }

    }


    private void digitalStrain() {


        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Digital Eye Strain");
        dialog.setMessage(getResources().getString(R.string.digital_eye_text));
        dialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {


                activateDigitalEyeStrainMonitoring();

            }
        });
        dialog.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

            }
        });
        dialog.show();
    }

    private void activateDigitalEyeStrainMonitoring() {

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        getActivity().registerReceiver(mReceiver, filter);

        ScreenReceiver.scheduleAlarmNow(getActivity());

        Toast.makeText(getActivity(), "Monitoring started successfully.", Toast.LENGTH_LONG).show();
        monitoring = true;
        strainButton.setText("Stop Monitoring");


        SharedPreferences.Editor editor = getActivity().getSharedPreferences("monitoring", MODE_PRIVATE).edit();
        editor.putBoolean("digital_monitoring", true);
        editor.apply();

    }


    private void disableDigitalEyeStrainMonitoring() {

        try {

            getActivity().unregisterReceiver(mReceiver);

            strainButton.setText("Start Monitoring");
            monitoring = false;

            Toast.makeText(getActivity(), "Monitoring stopped.", Toast.LENGTH_LONG).show();


            SharedPreferences.Editor editor = getActivity().getSharedPreferences("monitoring", MODE_PRIVATE).edit();
            editor.putBoolean("digital_monitoring", false);
            editor.apply();

        } catch (Exception e) {

            e.printStackTrace();
            Log.d(TAG, e.getMessage());

            Toast.makeText(getActivity(), "Monitoring stopped.", Toast.LENGTH_LONG).show();
            strainButton.setText("Start Monitoring");
            monitoring = false;


            SharedPreferences.Editor editor = getActivity().getSharedPreferences("monitoring", MODE_PRIVATE).edit();
            editor.putBoolean("digital_monitoring", false);
            editor.apply();


        }

    }

    private void startFiltering() {

        getActivity().startService(filterIntent);
        filtering = true;
        filterButton.setText("Stop Filtering");

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("monitoring", MODE_PRIVATE).edit();
        editor.putBoolean("filtering", true);
        editor.apply();

    }


    private void stopFiltering() {
        getActivity().stopService(filterIntent);
        filtering = false;
        filterButton.setText("Start Filtering");

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("monitoring", MODE_PRIVATE).edit();
        editor.putBoolean("filtering", false);
        editor.apply();

    }
}
