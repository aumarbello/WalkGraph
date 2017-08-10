package com.example.ahmed.walkgraph.presentation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ahmed.walkgraph.R;
import com.example.ahmed.walkgraph.data.local.GraphDAO;

/**
 * Created by ahmed on 8/9/17.
 */

public class ContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testing database creation
        GraphDAO graphDAO = GraphDAO.getDao(this);
    }
}
