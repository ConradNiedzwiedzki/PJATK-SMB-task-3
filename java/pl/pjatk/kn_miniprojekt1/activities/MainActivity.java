package pl.pjatk.kn_miniprojekt1.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import pl.pjatk.kn_miniprojekt1.R;

public class MainActivity extends AppCompatActivity
{
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
        prepareSharedPreferences();
        getButtonsPreferences();
    }

    public void addButtonHandler(View v)
    {
        Intent i = new Intent(this,ProductListActivity.class);
        startActivity(i);
    }

    public void settingsButtonHandler(View v)
    {
        Intent i = new Intent(this,OptionsActivity.class);
        startActivity(i);
    }

    @SuppressLint("ResourceType")
    private void getButtonsPreferences()
    {
        Button optionsButton = findViewById(R.id.settings_button);
        Button productButton = findViewById(R.id.add_button);
        String color =  sharedPreferences.getString("mainButtonColors", "black");
        optionsButton.setTextSize(sharedPreferences.getFloat("mainButtonSize", 16f));
        productButton.setTextColor(Color.parseColor(color));
    }

    @SuppressLint("ResourceType")
    private void prepareSharedPreferences()
    {
        if(sharedPreferences.getString("myPreferences","mainButtonColors").isEmpty())
        {
            sharedPreferences.edit().putString("mainButtonColors",getResources().getString(R.color.black)).apply();
        }
    }

    public void shopListButton(View v)
    {
        Intent i = new Intent(this,ShopListActivity.class);
        startActivity(i);
    }
}
