package com.example.aaronsandroidlab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Aaron Odartei
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {


    /** This holds the Textview at the center of the screen */
    private TextView view = null;
    /** This holds the EditText at the center of the screen */
    private EditText passwordTxt = null;
    /** This holds the button at the center of the screen */
    private Button loginBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        view = findViewById(R.id.textView);
        passwordTxt = findViewById(R.id.passwordText);
        loginBtn = findViewById(R.id.loginBtn);


        loginBtn.setOnClickListener(click -> {
            String password = passwordTxt.getText().toString();
            checkPasswordComplexity(password);
        });


    }

    /**
     * This function validates the password field after logging in
     *
     * @param pw The string object that we are checking
     * @return Returns if true
     */
    boolean checkPasswordComplexity(String pw){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;


        for (int i = 0; i<pw.length(); i++){

            char characters = pw.charAt(i);

            if(Character.isUpperCase(characters)){
                foundUpperCase = true;
            } else if(Character.isLowerCase(characters)){
                foundLowerCase = true;
            } else if(Character.isDigit(characters)){
                foundNumber = true;
            } else if(isSpecialCharacter(characters)){
                foundSpecial = isSpecialCharacter(characters);
            }

        }

        if(!foundUpperCase) {
            Toast.makeText(getApplicationContext(), "Missing Uppercase", Toast.LENGTH_SHORT) ;
            return false;
        } else if(!foundLowerCase){
            Toast.makeText(getApplicationContext(), "Missing Lowercase", Toast.LENGTH_SHORT) ;
            return false;
        } else if(!foundNumber){
            Toast.makeText(getApplicationContext(), "Missing Number", Toast.LENGTH_SHORT);
            return false;
        }

        else if(!foundSpecial){
            Toast.makeText(getApplicationContext(), "Missing Special Character", Toast.LENGTH_SHORT);
            return false;
        }

        return false;

    }


    /**
     * Function used to check if passowrd has a special character
     * @param c Characetrs
     * @return
     */
    boolean isSpecialCharacter(char c){

        switch(c){
            case '#':
            case '?':
            case '*':
            case '$':
            case '%':
            case '^':
            case '&':
            case '@':
            case '!':
                return true;

            default:
                return false;

        }

    }
}