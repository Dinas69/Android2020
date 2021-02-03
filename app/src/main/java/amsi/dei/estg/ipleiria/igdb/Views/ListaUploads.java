package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaReviewsAdaptador;
import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaUploadAdaptador;
import amsi.dei.estg.ipleiria.igdb.Listeners.UploadListener;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.Modelos.Uploadimagem;
import amsi.dei.estg.ipleiria.igdb.R;

public class ListaUploads extends AppCompatActivity implements UploadListener {

    private ListView uploadsLista;
    private Uploadimagem uploadimagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_uploads);

        uploadsLista = findViewById(R.id.lvListaUploads);

        SingletonGestorIGDb.getInstance(this).setUploadsListener(this);
        SingletonGestorIGDb.getInstance(this).getAllUploadAPI(this);
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