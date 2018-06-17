package com.example.cristianv.popularmovies.display;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.cristianv.popularmovies.R;

/**
 * Created by cristian.verdes on 20.02.2018.
 */

public class HelpActivity extends AppCompatActivity{

    public static void start(Context context) {
        Intent starter = new Intent(context, HelpActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setTitle(R.string.help);

    }
}
