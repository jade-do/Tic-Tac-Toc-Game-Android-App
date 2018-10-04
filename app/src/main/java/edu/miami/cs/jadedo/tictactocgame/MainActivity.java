package edu.miami.cs.jadedo.tictactocgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.util.Log;
import android.app.Activity;
import android.widget.Button;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private final int ACTIVITY_GAME_ON = 3;
    private final int ACTIVITY_PROMPT = 4;
    private final int PLAYER_1 = 1;
    private final int PLAYER_2 = 2;
    private final int BLANK = 0;
    private RatingBar ratingBar1;
    private RatingBar ratingBar2;

    private int myBarGapTime;

    private double theDividingLine;

    protected Button playerOneBtn;
    protected Button playerTwoBtn;

    protected String uriOneStringHere;
    protected String uriTwoStringHere;
    protected String nameOneHere;
    protected String nameTwoHere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ratingBar1 = (RatingBar) findViewById(R.id.rating_bar_1);
        ratingBar1.setRating(0);
        ratingBar2 = (RatingBar) findViewById(R.id.rating_bar_2);
        ratingBar2.setRating(0);

        myBarGapTime = getResources().getInteger(R.integer.bar_gap_five_sec);

        playerOneBtn = findViewById(R.id.btn_1);
        playerTwoBtn = findViewById(R.id.btn_2);

        theDividingLine = 0.5;

        // Launch the enter name and image interface

        Intent promptNamePhoto;

        promptNamePhoto = new Intent();
        promptNamePhoto.setClassName("edu.miami.cs.jadedo.tictactocgame", "edu.miami.cs.jadedo.tictactocgame.Prompt");
        startActivityForResult(promptNamePhoto, ACTIVITY_PROMPT );


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater;

        inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    // Menu items

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.one_sec:
                myBarGapTime = getResources().getInteger(R.integer.bar_gap_one_sec);
                return true;
            case R.id.two_sec:
                myBarGapTime = getResources().getInteger(R.integer.bar_gap_two_sec);
                return true;
            case R.id.five_sec:
                myBarGapTime = getResources().getInteger(R.integer.bar_gap_five_sec);
                return true;
            case R.id.ten_sec:
                myBarGapTime = getResources().getInteger(R.integer.bar_gap_ten_sec);
                return true;
            case R.id.reset:
                ratingBar1.setRating(0);
                ratingBar2.setRating(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void myClickHandler(View view){

        Intent nextActivity;

        switch(view.getId()){
            case R.id.btn_start:
                // Starting the main game - TicTacToc
                nextActivity = new Intent();
                nextActivity.setClassName("edu.miami.cs.jadedo.tictactocgame",
                        "edu.miami.cs.jadedo.tictactocgame.GameOn");

                // Passing in information
                nextActivity.putExtra("edu.miami.cs.jadedo.tictactocgame.my_bar_gap_time", myBarGapTime);
                nextActivity.putExtra("edu.miami.cs.jadedo.tictactocgame.dividing_line", theDividingLine);
                nextActivity.putExtra("edu.miami.cs.jadedo.tictactocgame.name_one", nameOneHere);
                nextActivity.putExtra("edu.miami.cs.jadedo.tictactocgame.name_two", nameTwoHere);
                if (uriOneStringHere != null && uriTwoStringHere != null)    {
                    nextActivity.putExtra("edu.miami.cs.jadedo.tictactocgame.uri_one_string", uriOneStringHere);
                    nextActivity.putExtra("edu.miami.cs.jadedo.tictactocgame.uri_two_string", uriTwoStringHere);
                }

                startActivityForResult(nextActivity, ACTIVITY_GAME_ON);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        Uri uriOneHere;
        Uri uriTwoHere;
        int whoWon;
        View startBtn;

        switch (requestCode){

            // Result of winner from TicTacToc Game
            case ACTIVITY_GAME_ON:
                // Setting the rating bar
                if (resultCode == Activity.RESULT_OK){
                    whoWon = data.getIntExtra("edu.miami.cs.jadedo.tictactocgame.who_won", BLANK);
                    String winner = String.valueOf(whoWon);
                    Log.i("WIN", winner);
                    startBtn = findViewById(R.id.btn_start);
                    if (whoWon == PLAYER_1){
                        ratingBar1.setRating(ratingBar1.getRating()+1);
                        if (ratingBar1.getRating() >= 5) {
                            startBtn.setVisibility(View.GONE);
                        }

                    } else if( whoWon == PLAYER_2) {
                        ratingBar2.setRating(ratingBar2.getRating()+1);
                        if (ratingBar2.getRating() >= 5) {
                            startBtn.setVisibility(View.GONE);
                        }
                    }

                    // Setting the dividing line
                    theDividingLine = data.getDoubleExtra("edu.miami.cs.jadedo.tictactocgame.dividing_line", 0.5);

                } else {
                    Log.i("OUT", "They are backing out");
                }
                break;

                // Return the entered players' name and photo

            case ACTIVITY_PROMPT:

                TextView textViewOne;
                TextView textViewTwo;

                    if (resultCode == Activity.RESULT_OK){

                        // Getting the photo information
                            uriOneStringHere = data.getStringExtra("edu.miami.cs.jadedo.tictactocgame.uri_one_string");
                            uriTwoStringHere = data.getStringExtra("edu.miami.cs.jadedo.tictactocgame.uri_two_string");

                            if (uriOneStringHere != null && uriTwoStringHere != null)    {
                                //if (uriOneStringHere.length() > 0 && uriTwoStringHere.length() > 0 )    {
                                uriOneHere = Uri.parse(uriOneStringHere);
                                uriTwoHere = Uri.parse(uriTwoStringHere);

                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriOneHere);
                                    Drawable drawableOne = new BitmapDrawable(getResources(),bitmap);
                                    playerOneBtn.setBackground(drawableOne);
                                } catch (Exception e){
                                    Log.i("ERROR", "Could not get picture from " + uriOneStringHere + " " + e.getMessage());
                                }

                                try {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriTwoHere);
                                    Drawable drawableTwo = new BitmapDrawable(getResources(),bitmap);
                                    playerTwoBtn.setBackground(drawableTwo);
                                } catch (Exception e) {
                                    Log.i("ERROR", "Could not get picture from " + uriTwoStringHere + " " + e.getMessage());
                                }
                        }

                        // Getting the player name information

                        nameOneHere = data.getStringExtra("edu.miami.cs.jadedo.tictactocgame.name_one");
                        nameTwoHere = data.getStringExtra("edu.miami.cs.jadedo.tictactocgame.name_two");

                        if (!nameOneHere.equals("") && !nameTwoHere.equals("")){
                            textViewOne = (TextView)findViewById(R.id.player_1_txt);
                            textViewTwo = (TextView)findViewById(R.id.player_2_txt);
                            textViewOne.setText(nameOneHere);
                            textViewTwo.setText(nameTwoHere);
                        }
                    } else {
                        Log.i("WARNING", "No personal information collected");
                    }


                break;

            default:
                break;
        }
    }
}

