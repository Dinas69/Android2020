package amsi.dei.estg.ipleiria.igdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import amsi.dei.estg.ipleiria.igdb.Modelos.IGDbHelper;

public class MainActivity extends AppCompatActivity {
    private IGDbHelper igDbHelper;

    public static final String EMAIL = "EMAIL";
    public static final String TOKEN = "TOKEN";
    public static final String PREF_INFO_USER = "PREF_INFO_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        igDbHelper = new IGDbHelper(this);
    }
}