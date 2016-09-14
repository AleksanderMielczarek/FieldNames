package com.github.aleksandermielczarek.fieldnamesexample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by Aleksander Mielczarek on 13.09.2016.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView data1 = (TextView) findViewById(R.id.data1);
        TextView data2 = (TextView) findViewById(R.id.data2);
        data1.setText(TestDataFieldNames.FIELD_TEST_DATA);
        data2.setText(TestDataFieldNames.FIELD_TEST_DATA2);
        setSupportActionBar(toolbar);
    }
}
