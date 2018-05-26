package com.chetan.commonapp.boundServiceApp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chetan.serverapp.R;

import static com.chetan.commonapp.boundServiceApp.TreasuryDatabase.DATABASE_NAME;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getApplicationContext().deleteDatabase(DATABASE_NAME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getApplicationContext().deleteDatabase(DATABASE_NAME);
    }
}
