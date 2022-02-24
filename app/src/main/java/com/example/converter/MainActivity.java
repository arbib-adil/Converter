package com.example.converter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Spinner spinner1;
    private Spinner spinner2;
    private TextView result;
    private EditText input;
    private ArrayList<String> bases;
    private ArrayList<String> basesOnClick;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> secondaryAdapter;
    private Button btn;
    private TextView msgError;
    private MediaPlayer errorSound;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inflate
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        input = findViewById(R.id.input);
        result = findViewById(R.id.result);
        btn = findViewById(R.id.button);
        msgError = findViewById(R.id.msgError);


        bases = new ArrayList<>();
        bases.add("Bin");
        bases.add("Dec");
        bases.add("Hex");


        //create an adapter
        adapter = new ArrayAdapter<>(this, R.layout.spinner, bases);

        //spinner 1
        spinner1.setAdapter(adapter);


//        spinner 2
        spinner2.setAdapter(adapter);



        //output textview
        result = findViewById(R.id.result);



        //items click listeners
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                basesOnClick = new ArrayList<>();
                switch (i){
                    case 0:
                        basesOnClick.add("Dec");
                        basesOnClick.add("Hex");
                        break;
                    case 1:
                        basesOnClick.add("Bin");
                        basesOnClick.add("Hex");
                        break;
                    case 2:
                        basesOnClick.add("Bin");
                        basesOnClick.add("Dec");
                        break;
                    default:
                        break;
                }
                secondaryAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner, basesOnClick);
                spinner2.setAdapter(secondaryAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInput()){
                    result.setText(finalResult());
                }
                else {
                    if(errorSound==null){
                        errorSound = MediaPlayer.create(MainActivity.this, R.raw.error);
                    }
                    errorSound.start();
                    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                    input.startAnimation(shake);
                }
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!checkInput()){
                    input.setBackgroundResource(R.drawable.error_bg);
                    msgError.setText("Invalid input");
                }
                else {
                    input.setBackgroundResource(R.drawable.edittext_bg);
                    msgError.setText("");
                }
                if(input.getText().toString().isEmpty()){
                    input.setBackgroundResource(R.drawable.edittext_bg);
                    msgError.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

    private String finalResult(){
        String s1 = spinner1.getSelectedItem().toString();
        String s2 = spinner2.getSelectedItem().toString();
        String inpt = input.getText().toString();
        String output = null;
        switch (s1){
            case "Bin":
                output = s2.equals("Dec") ? String.valueOf(Integer.parseInt(inpt, 2)):decToHex(Integer.parseInt(inpt, 2));
                break;
            case "Dec":
                output = s2.equals("Bin") ? Integer.toBinaryString(Integer.parseInt(inpt)):decToHex(Integer.parseInt(inpt));
                break;
            case "Hex":
                output = s2.equals("Bin") ? hexToBin(inpt):String.valueOf(Integer.parseInt(inpt, 16));
                break;
        }
        return output;
    }



//    public void showToast(){
//        LayoutInflater layoutInflater = getLayoutInflater();
//        View layout = layoutInflater.inflate(R.layout.toast_error, findViewById(R.id.toast_layout));
//        tstMsg = layout.findViewById(R.id.toastTxt);
//        toast = new Toast(getApplicationContext());
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
//    }



    public boolean checkInput(){
        String inpt = input.getText().toString();
        String s1 = spinner1.getSelectedItem().toString();
        if(s1.equals("Bin"))return checkBin(inpt);
        else if(s1.equals("Dec"))return checkDec(inpt);
        else if(s1.equals("Hex"))return checkHex(inpt);
        return true;
    }



    //calculation functions
    public static int hexToDec(String hex){
        String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++)
        {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
    public static String decToHex(int decimal){
        int rem;
        String hex="";
        char[] hexChars ={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(decimal>0)
        {
            rem=decimal%16;
            hex=hexChars[rem]+hex;
            decimal=decimal/16;
        }
        return hex;
    }
    static String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
    }

    //check user input
    public boolean checkHex(String input){
        return input.matches("[A-F0-9]+");
    }
    public boolean checkBin(String input){
        return input.matches("[0-1]+");
    }
    public boolean checkDec(String input){
        return input.matches("[0-9]+");
    }


}