package com.webianks.exp.crimson.network;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.webianks.exp.crimson.CrimsonApplication;

public class VolleySingleton {

    public static VolleySingleton instance = null;
    private RequestQueue requestQueue;


    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(CrimsonApplication.getContext(), new MyHurlStack());
    }


    public static VolleySingleton getInstance() {

        if (instance == null) {
            instance = new VolleySingleton();
        }


        return instance;
    }


    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
