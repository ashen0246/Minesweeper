
package com.example.minesweeper;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;



public class GameOverActivity extends AppCompatActivity {

    // Create a layout file for the game over page.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        //No rotation of screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        //default value false
        boolean won = getIntent().getBooleanExtra("won", false);
        if (!won){
            TextView wonMessage1 = findViewById(R.id.wonMessage1);
            wonMessage1.setText(getString(R.string.lost1));

            TextView wonMessage2 = findViewById(R.id.wonMessage2);
            wonMessage2.setText(getString(R.string.lost2));
        }

    }

    public void onClickPlayAgain(View view) {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}