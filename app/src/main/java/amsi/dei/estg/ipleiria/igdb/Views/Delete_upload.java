package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.Modelos.Uploadimagem;
import amsi.dei.estg.ipleiria.igdb.R;

public class Delete_upload extends AppCompatActivity {

    private String id, token;
    Uploadimagem uploadimagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_upload);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        token = intent.getStringExtra("token");
    }

    public void DeleteUpload(View view) {

        SingletonGestorIGDb.getInstance(getApplicationContext()).removerUploadAPI(uploadimagem, getApplicationContext(), token, id);
        finish();
        Intent intent = new Intent(this, ListaUploads.class);
        startActivity(intent);
    }
}