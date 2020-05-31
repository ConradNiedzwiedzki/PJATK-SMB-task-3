package pl.pjatk.kn_miniprojekt1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import pl.pjatk.kn_miniprojekt1.R;

public class OptionsActivity extends AppCompatActivity

    private SharedPreferences sharedPreferences;
    private Switch sw;
    private EditText header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sw = (Switch) findViewById(R.id.switch1);
        header = (EditText) findViewById(R.id.editText);

        boolean isDarkerThemeEnabled = sharedPreferences.getBoolean(getString(R.string.themeSetting), false);
        sw.setChecked(isDarkerThemeEnabled);

        String productsListName = sharedPreferences.getString(getString(R.string.productsListName), "");
        header.setText(productsListName);
    }

    public void saveOptions(View view) {
        SharedPreferences.Editor sharedPreferencesChanges = sharedPreferences.edit();
        sharedPreferencesChanges.putBoolean(getString(R.string.themeSetting), sw.isChecked());
        sharedPreferencesChanges.putString(getString(R.string.productsListName), header.getText().toString());
        sharedPreferencesChanges.commit();

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
