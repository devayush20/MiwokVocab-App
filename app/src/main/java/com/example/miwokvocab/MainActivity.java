package com.example.miwokvocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        // Find the View that shows the numbers category
        TextView numbers = (TextView) findViewById(R.id.numbers);
        // Set a click listener on that View
        numbers.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers View is clicked on.
            @Override
            public void onClick(View view) {
                Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);
                startActivity(numbersIntent);
            }
        });

        //Find the View that shows the family members category
        TextView family=(TextView) findViewById(R.id.family_members);
        //Set a click listener on that view
        family.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent familyIntent=new Intent(MainActivity.this,FamilyMembersActivity.class);
                startActivity(familyIntent);
            }
        });

        //Find the View that shows the colors
        TextView colors_view=(TextView) findViewById(R.id.colors);
        //Set a click listener on the view
        colors_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent colorIntent=new Intent(MainActivity.this,ColorsActivity.class);
                startActivity(colorIntent);
            }
        });
        //Find the View that shows the phrases
        TextView phrases_view=(TextView) findViewById(R.id.phrases);
        //Set a click listener in the view
        phrases_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent phraseIntent=new Intent(MainActivity.this, PhrasesActivity.class);
                startActivity(phraseIntent);
            }
        });

    }

    /*public void handleNumbersActivity(View view) {
        Intent numberIntent=new Intent(this,NumbersActivity.class);
        startActivity(numberIntent);
    }
    public void handleFamilyActivity(View view)
    {
        Intent familyIntent=new Intent(this,FamilyMembersActivity.class);
        startActivity(familyIntent);
    }
    public void handlePhrasesActivity(View view)
    {
        Intent phrasesIntent=new Intent(this,PharasesActivity.class);
        startActivity(phrasesIntent);
    }
    public void handleColorsActivity(View view)
    {
        Intent colorsIntent=new Intent(this,ColorsActivity.class);
        startActivity(colorsIntent);
    }*/
}