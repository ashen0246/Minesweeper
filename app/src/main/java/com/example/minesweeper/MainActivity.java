package com.example.minesweeper;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private TextView[][] cell_tvs = new TextView[ROW_COUNT][COLUMN_COUNT];

    private static final int ROW_COUNT = 12;
    private static final int PRIME = 13;
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
    //NEED TO IMPLEMENT
    private int squaresDiscovered = 0;

    //to help with bfs
    //bc u cant put int[] inside and pair compares by reference use hashing strat
    //value = r*prime + c
    private Set<Integer> visited = new HashSet<Integer>();
    private Set<Integer> flagged = new HashSet<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //No rotation of screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

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
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);
                if (field[i][j] != 0) {
                    tv.setText(String.valueOf(field[i][j]));
                }

                GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                lp.setMargins(dpToPixel(2), dpToPixel(2), dpToPixel(2), dpToPixel(2));
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs[i][j] = tv;
            }
        }
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        int[] n = findIndexOfCellTextView(tv);
        int i = n[0];
        int j = n[1];

        if (!visited.contains(i*PRIME+j)) {
            if (breakMode && !flagged.contains(i*PRIME+j)) {
                visited.add(i*13 + j);
                if (field[i][j] != -1) {
                    tv.setBackgroundColor(Color.LTGRAY);
                    tv.setTextColor(Color.GRAY);

                    if (field[i][j] == 0) {
                        //if empty square bfs to break all squares adjacent BFS
                        // set cells around bomb
                        if (i >= 1) {
                            bfsChecker(i - 1, j);
                        }
                        if (i >= 1 && j < COLUMN_COUNT - 1) {
                            bfsChecker(i - 1, j + 1);
                        }
                        if (i >= 1 && j >= 1) {
                            bfsChecker(i - 1, j - 1);
                        }

                        if (j >= 1) {
                            bfsChecker(i, j - 1);
                        }
                        if (j < COLUMN_COUNT - 1) {
                            bfsChecker(i, j + 1);
                        }

                        if (i < ROW_COUNT - 1) {
                            bfsChecker(i + 1, j);
                        }
                        if (i < ROW_COUNT - 1 && j >= 1) {
                            bfsChecker(i + 1, j - 1);
                        }
                        if (i < ROW_COUNT - 1 && j < COLUMN_COUNT - 1) {
                            bfsChecker(i + 1, j + 1);
                        }
                    }
                }
                //game over bomb broken
                else {

                }

            }
            //flag placing mode
            else if (!breakMode){
                if (!flagged.contains(i * PRIME + j)) {
                    flagged.add(i * PRIME + j);
                    tv.setBackgroundColor(Color.DKGRAY);
                } else {
                    flagged.remove(i * PRIME + j);
                    tv.setBackgroundColor(Color.GREEN);
                }
            }
        }

    }

    //helps get rid of repetitive code for bfs portion
    public void bfsChecker(int i, int j){
        if (!visited.contains(i*13 + j)){
            onClickTV(cell_tvs[i][j]);
        }
    }

    public void onClickToggle(View view) {
        breakMode = !breakMode;
        Button button = findViewById(R.id.toggle);

        if (breakMode) {
            button.setBackgroundColor(Color.LTGRAY);
            button.setTextColor(Color.DKGRAY);
        } else {
            button.setBackgroundColor(Color.DKGRAY);
            button.setTextColor(Color.LTGRAY);
        }
    }


    private int[] findIndexOfCellTextView(TextView tv) {
        for (int i = 0; i < ROW_COUNT; i++){
            for (int j = 0; j < COLUMN_COUNT; j++){
                if (cell_tvs[i][j] == tv){
                    return new int[] {i, j};
                }
            }
        }
        return new int[] {-1, -1};
    }

    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}