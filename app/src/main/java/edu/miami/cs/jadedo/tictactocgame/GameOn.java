package edu.miami.cs.jadedo.tictactocgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.os.Handler;
import android.widget.Button;
import android.util.Log;
import android.net.Uri;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;
import android.widget.Toast;

public class GameOn extends AppCompatActivity {
    private int[][] board;
    private final int PLAYER_1 = 1;
    private final int PLAYER_2 = 2;
    private final int BLANK = 0;

    private ProgressBar myProgressBar;
    private int myBarMaxTimeHere;
    private int myBarGapTimeHere;

    private Button BTN_1;
    private Button BTN_2;
    private  TextView playerName;

    private int currPlayer;
    private int whoseTurnItIs;

    public double dividingLine;

    private String uriOneStringThere;
    private String uriTwoStringThere;
    private Uri uriOneThere;
    private Uri uriTwoThere;
    private String nameOneThere;
    private String nameTwoThere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_on);

        board = new int[3][3];

        initializeBoard(board);

        dividingLine = this.getIntent().getDoubleExtra("edu.miami.cs.jadedo.tictactocgame.dividing_line", 0.5);

        currPlayer = initializePlayer();
        BTN_1 = findViewById(R.id.btn_1_game_on);
        BTN_2 = findViewById(R.id.btn_2_game_on);
        playerName = (TextView)findViewById(R.id.player);

        nameOneThere = this.getIntent().getStringExtra("edu.miami.cs.jadedo.tictactocgame.name_one");
        nameTwoThere = this.getIntent().getStringExtra("edu.miami.cs.jadedo.tictactocgame.name_two");

        setButton();

        myProgressBar = findViewById(R.id.time_left);
        //myBarMaxTimeHere = this.getIntent().getIntExtra("edu.miami.cs.jadedo.tictactocgame.my_bar_max_time",
          //      getResources().getInteger(R.integer.bar_max_10_sec));
        myProgressBar.setProgress(myProgressBar.getMax());
        myBarGapTimeHere = this.getIntent().getIntExtra("edu.miami.cs.jadedo.tictactocgame.my_bar_gap_time",
                      getResources().getInteger(R.integer.bar_gap_five_sec));

        uriOneStringThere = this.getIntent().getStringExtra("edu.miami.cs.jadedo.tictactocgame.uri_one_string");
        uriTwoStringThere = this.getIntent().getStringExtra("edu.miami.cs.jadedo.tictactocgame.uri_two_string");

        if (uriOneStringThere != null && uriTwoStringThere != null)    {
            uriOneThere = Uri.parse(uriOneStringThere);
            uriTwoThere = Uri.parse(uriTwoStringThere);

            setButtonImage(R.id.btn_1_game_on, uriOneThere);
            setButtonImage(R.id.btn_2_game_on, uriTwoThere);
        }

        myProgresser.run();

    }

    private void initializeBoard(int[][] theBoard){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                theBoard[i][j] = BLANK;
            }
        }
    }

    private int initializePlayer(){

        if (Math.random() >= dividingLine){
            dividingLine += 0.1;
            return PLAYER_2;
        } else {
            dividingLine -= 0.1;
            return PLAYER_1;
        }
    }

    private void setButton(){

        if (currPlayer == PLAYER_1){
            BTN_1.setVisibility(View.VISIBLE);
            BTN_2.setVisibility(View.INVISIBLE);
            playerName.setText(nameOneThere);
        } else{
            BTN_2.setVisibility(View.VISIBLE);
            BTN_1.setVisibility(View.INVISIBLE);
            playerName.setText(nameTwoThere);
        }
    }

    private void setButtonImage(int btnId, Uri theUri){
        Button thisButton;

        thisButton = findViewById(btnId);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), theUri);
            Drawable d = new BitmapDrawable(getResources(),bitmap);
            thisButton.setBackground(d);
        } catch (Exception e){
            Log.i("ERROR", "Could not get picture from " + theUri.toString() + " " + e.getMessage());
        }

    }
    private void checkWinner(){

        int checkTie = 0;

        // Check horizontal
        for (int y = 0; y < 3; y++){
            for (int i = 0; i < 3; i++) {
                if(board[i][y] != currPlayer){
                    break;
                }
                if (i == 2){
                    returnWinner();
                }
            }
        }

        // Check vertical
        for (int x = 0; x < 3; x++){
            for (int i = 0; i < 3; i++){
                if (board[x][i] != currPlayer){
                    break;
                }
                if ( i == 2){
                    returnWinner();
                }
            }
        }

        // Check diagonal
        for (int i = 0; i < 3; i++){
            if (board[i][i] != currPlayer){
                break;
            }
            if ( i == 2) {
                returnWinner();
            }
        }

        for (int i = 0; i < 3; i++){
            if (board[2 -i][i] != currPlayer){
                break;
            }

            if (i == 2) {
                returnWinner();
            }
        }

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (board[i][j] != BLANK){
                    checkTie += 1;
                }
            }
        }

        if (checkTie == 9) {
            currPlayer = BLANK;
            returnWinner();
        }

    }

    private void returnWinner (){
        Intent returnIntent;

        returnIntent = new Intent();
        returnIntent.putExtra("edu.miami.cs.jadedo.tictactocgame.who_won", currPlayer);
        returnIntent.putExtra("edu.miami.cs.jadedo.tictactocgame.dividing_line", dividingLine);
        setResult(RESULT_OK, returnIntent);
        finish();

    }

    private void switchPlayer(){
        if (currPlayer == PLAYER_1){
            currPlayer = PLAYER_2;
            Log.e("IN", String.valueOf(currPlayer));
        } else if (currPlayer == PLAYER_2){
            currPlayer = PLAYER_1;
            Log.e("IN", String.valueOf(currPlayer));
        }
    }

    public void myClickHandler(View view){

        Button theButton;

        switch(view.getId()){
            case R.id.my_btn_1:
                board[0][0] = currPlayer;
                theButton = findViewById(R.id.my_btn_1);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_1, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_1, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_2:
                board[0][1] = currPlayer;
                theButton = findViewById(R.id.my_btn_2);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_2, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_2, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_3:
                board[0][2] = currPlayer;
                theButton = findViewById(R.id.my_btn_3);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_3, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_3, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_4:
                board[1][0] = currPlayer;
                theButton = findViewById(R.id.my_btn_4);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_4, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_4, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_5:
                board[1][1] = currPlayer;
                theButton = findViewById(R.id.my_btn_5);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_5, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_5, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_6:
                board[1][2] = currPlayer;
                theButton = findViewById(R.id.my_btn_6);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_6, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_6, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_7:
                board[2][0] = currPlayer;
                theButton = findViewById(R.id.my_btn_7);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_7, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_7, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_8:
                board[2][1] = currPlayer;
                theButton = findViewById(R.id.my_btn_8);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_8, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_8, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            case R.id.my_btn_9:
                board[2][2] = currPlayer;
                theButton = findViewById(R.id.my_btn_9);
                if (currPlayer == PLAYER_1){
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_9, uriOneThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.moon_cream_cat);
                    }
                    whoseTurnItIs = PLAYER_2;
                } else {
                    if (uriOneStringThere != null && uriTwoStringThere != null)    {
                        setButtonImage(R.id.my_btn_9, uriTwoThere);
                    } else {
                        theButton.setBackgroundResource(R.drawable.night_star_cat);
                    }
                    whoseTurnItIs = PLAYER_1;
                }
                theButton.setClickable(false);
                break;
            default:
                break;
        }
    }

    private final Runnable myProgresser = new Runnable(){
        private Handler myHandler = new Handler();

        public void run(){

            myProgressBar.setProgress(myProgressBar.getProgress()-myBarGapTimeHere);

            checkWinner();

            if (myProgressBar.getProgress() <= 0){
                myProgressBar.setProgress(myProgressBar.getMax());
                switchPlayer();
                setButton();
                whoseTurnItIs = currPlayer;
            }

            if (whoseTurnItIs != currPlayer){
                myProgressBar.setProgress(myProgressBar.getMax());
                switchPlayer();
                setButton();
                whoseTurnItIs = currPlayer;
            }

            if (!myHandler.postDelayed(myProgresser, myBarGapTimeHere))  {
                Log.e("ERROR", "Cannot postDelayed");

            }
        }
    };
}

