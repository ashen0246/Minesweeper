package com.example.minesweeper;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;

    private static final int ROW_COUNT = 12;
    private static final int COLUMN_COUNT = 10;
    private static final int NUM_BOMBS = 4;
    private static final int squaresNeeded = ROW_COUNT * COLUMN_COUNT - NUM_BOMBS;

    private int flagsLeft = 4;
    // true equals break false equals flag
    private boolean breakMode = true;

    //map with bombs
    //-1 bomb
    private int[][] field = new int[ROW_COUNT][COLUMN_COUNT];
    //used to check if game won at end
    private int squaresDiscovered = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //No rotation of screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        cell_tvs = new ArrayList<TextView>();

        //set up random bombs
        //works
        for (int i = 0; i < 4; i++){
            Random rand = new Random();
            int r = rand.nextInt(ROW_COUNT);
            int c = rand.nextInt(COLUMN_COUNT);
            if (field[r][c] == 0){
                field[r][c] = -1;

                // set cells around bomb
                if (r >= 1)                                 {field[r-1][c] += 1;}
                if (r >= 1 && c < COLUMN_COUNT-1)           {field[r-1][c+1] += 1;}
                if (r >= 1 && c >= 1)                       {field[r-1][c-1] += 1;}

                if (c >= 1)                                 {field[r][c-1] += 1;}
                if (c < COLUMN_COUNT-1)                     {field[r][c+1] += 1;}

                if (r < ROW_COUNT - 1)                      {field[r+1][c] += 1;}
                if (r < ROW_COUNT-1 && c >= 1)              {field[r+1][c-1] += 1;}
                if (r < ROW_COUNT-1 && c < COLUMN_COUNT-1)  {field[r+1][c+1] += 1;}
            }
            else{
                i--;
            }
        }

        GridLayout grid = (GridLayout) findViewById(R.id.field);
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j=0; j < COLUMN_COUNT; j++) {
                TextView tv = new TextView(this);
                tv.setHeight( dpToPixel(32) );
                tv.setWidth( dpToPixel(32) );
                tv.setTextSize(24);
                tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);
                tv.setText(String.valueOf(field[i][j]));

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int n = findIndexOfCellTextView(tv);
        int i = n/COLUMN_COUNT;
        int j = n%COLUMN_COUNT;

        tv.setBackgroundColor(Color.LTGRAY);
        tv.setTextColor(Color.GRAY);
    }

    public void onClickToggle(View view){
        //Implement
    }

    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}