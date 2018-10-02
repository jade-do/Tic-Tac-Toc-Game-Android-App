package edu.miami.cs.jadedo.tictactocgame;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

public class Prompt extends AppCompatActivity {

    private static final int ACTIVITY_SELECT_PICTURE_1 = 1;
    private static final int ACTIVITY_SELECT_PICTURE_2 = 2;

    private Uri selectedUriOne;
    private Uri selectedUriTwo;
    private String uriOneString;
    private String uriTwoString;

    public Intent returnIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt);

        selectedUriOne = null;
        selectedUriTwo = null;

        uriOneString = "";
        uriTwoString = "";

        returnIntent = new Intent();


    }

    public void myClickHandler(View view){
        Intent galleryIntent;

        switch (view.getId()){
            case R.id.player_1_image:
                galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, ACTIVITY_SELECT_PICTURE_1);
                break;
            case R.id.player_2_image:
                galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, ACTIVITY_SELECT_PICTURE_2);
                break;

            case R.id.btn_start_general:

                EditText editNameOne = findViewById(R.id.enter_name_player_1);
                EditText editNameTwo =  findViewById(R.id.enter_name_player_2);

                String nameOne;
                String nameTwo;

                nameOne = editNameOne.getText().toString();
                nameTwo = editNameTwo.getText().toString();

                if (nameOne.equals("")){
                    nameOne = getResources().getString(R.string.player_1_str);
                }

                if (nameTwo.equals("")){
                    nameTwo = getResources().getString(R.string.player_2_str);
                }
                returnIntent.putExtra("edu.miami.cs.jadedo.tictactocgame.name_one", nameOne);
                returnIntent.putExtra("edu.miami.cs.jadedo.tictactocgame.name_two", nameTwo);

                setResult(RESULT_OK, returnIntent);

                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        Button playerOneButton;
        Button playerTwoButton;
        Bitmap selectedPicture;


        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){

            case ACTIVITY_SELECT_PICTURE_1:
                if (resultCode == Activity.RESULT_OK) {
                    selectedUriOne = data.getData();

                    if (selectedUriOne != null){
                        try {
                            playerOneButton = findViewById(R.id.player_1_image);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUriOne);
                            Drawable drawableOne = new BitmapDrawable(getResources(),bitmap);
                            playerOneButton.setBackground(drawableOne);
                            uriOneString = selectedUriOne.toString();

                        } catch (Exception e){
                            Log.i("ERROR", "Could not get picture from " + uriOneString + " " + e.getMessage());
                        }

                        returnIntent.putExtra("edu.miami.cs.jadedo.tictactocgame.uri_one_string", uriOneString);
                    }

                } else {
                    Log.i("WARNING", "The user backed out. No URI selected");
                }
                break;

            case ACTIVITY_SELECT_PICTURE_2:
                if (resultCode == Activity.RESULT_OK) {
                    selectedUriTwo = data.getData();

                    if (selectedUriTwo != null){
                        try {
                            playerTwoButton = findViewById(R.id.player_2_image);
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedUriTwo);
                            Drawable drawableTwo = new BitmapDrawable(getResources(),bitmap);
                            playerTwoButton.setBackground(drawableTwo);
                            uriTwoString = selectedUriTwo.toString();

                        } catch (Exception e){
                            Log.i("ERROR", "Could not get picture from " + uriTwoString + " " + e.getMessage());
                        }
                        returnIntent.putExtra("edu.miami.cs.jadedo.tictactocgame.uri_two_string", uriTwoString);
                    }
                } else {
                    Log.i("WARNING", "The user backed out. No URI selected");
                }
                break;

            default:
                break;
        }

    }
}
