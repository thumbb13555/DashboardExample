package com.jetec.speeddashboardexample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeekBar mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        EditText mEdit = findViewById(R.id.editText);
        Button btText = findViewById(R.id.button);
        DashBoardView mBoard = (DashBoardView) findViewById(R.id.myDashBoard);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mBoard.setPercentage(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });//onSeekbar
        btText.setOnClickListener((view -> {
            EditText edMax,edMin;
            edMax = findViewById(R.id.edMax);
            edMin = findViewById(R.id.edMin);
            String s = mEdit.getText().toString();
            String mS= edMax.getText().toString();
            String nS= edMin.getText().toString();

            if (s.length()!=0 &&mS.length()!=0&&nS.length()!=0){

                mBoard.setPercentage(Float.parseFloat(s)
                        ,Float.parseFloat(mS)
                        ,Float.parseFloat(nS));

            }
        }));


    }
}
