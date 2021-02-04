package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaReviewsAdaptador;
import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaUploadAdaptador;
import amsi.dei.estg.ipleiria.igdb.Listeners.UploadListener;
import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.Modelos.Uploadimagem;
import amsi.dei.estg.ipleiria.igdb.R;

public class ListaUploads extends AppCompatActivity implements UploadListener {

    private ListView uploadsLista;
    private Uploadimagem uploadimagem;
    private String id_user, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_uploads);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id_user");
        token = intent.getStringExtra("token");
        uploadsLista = findViewById(R.id.lvListaUploads);

        SingletonGestorIGDb.getInstance(this).setUploadsListener(this);
        SingletonGestorIGDb.getInstance(this).getAllUploadAPI(this, id_user, token);

        uploadsLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Log.e("UplodaDelete", String.valueOf(id));
                    openActivityDeleteUpload(String.valueOf(id));

                    //SingletonGestorIGDb.getInstance(getApplicationContext()).removerUploadAPI(uploadimagem, getApplicationContext(), token, String.valueOf(id));
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
    }

    public void openActivityDeleteUpload(String id) {
        Intent intent1 = new Intent(this, Delete_upload.class);
        intent1.putExtra("id", id);
        intent1.putExtra("token", token);
        startActivity(intent1);

    }

    public void removerFoto() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Eliminar foto")
                .setMessage("Pretende mesmo remover?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //SingletonGestorIGDb.getInstance(getApplicationContext()).removerReviewAPI(reviews, getApplicationContext(), token);
                        //SingletonGestorIGDb.getInstance(getApplicationContext()).removerUploadAPI(uploadimagem, getApplicationContext(), token, id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
    }

    @Override
    public void onRefreshListaUpload(ArrayList<Uploadimagem> listaUpload) {
        if (uploadsLista != null)
            uploadsLista.setAdapter(new ListaUploadAdaptador(this, listaUpload));
    }

    @Override
    public void onRefreshDetalhes() {
        setResult(RESULT_OK);
        finish();
        startActivity(getIntent());

    }
}