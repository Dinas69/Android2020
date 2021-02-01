package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaComentariosAdaptador;
import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaJogosAdaptador;
import amsi.dei.estg.ipleiria.igdb.Listeners.ComentListener;
import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.IGDbHelper;
import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.R;
import amsi.dei.estg.ipleiria.igdb.utils.IGDbJsonParser;

public class ListaCometariosJogo extends AppCompatActivity implements ComentListener {

    public static final String TOKEN = "TOKEN";
    public static final String ID_USER = "ID_USER";

    public static final String ID = "ID";
    public static final String PREF_INFO_USER = "PREF_INFO_USER";
    private int idjogo;

    private ListView lvListaComentarios;
    private EditText etDescricao;

    private Comentarios comentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_cometarios_jogo);

        idjogo = getIntent().getIntExtra(ID, -1);
        etDescricao = findViewById(R.id.editTextDescricao);
        lvListaComentarios = findViewById(R.id.lvListaComentarios);

        SingletonGestorIGDb.getInstance(this).setComentariosListener(this);
        SingletonGestorIGDb.getInstance(this).getComentariosJogo(this, idjogo);

    }

    @Override
    public void onRefreshListaComentarios(ArrayList<Comentarios> listaComentarios) {
        if (lvListaComentarios != null)
            lvListaComentarios.setAdapter(new ListaComentariosAdaptador(this, listaComentarios));

    }

    @Override
    public void onRefreshDetalhes() {
        setResult(RESULT_OK);
        finish();
        startActivity(getIntent());
    }

    public void ComentarJogo(View view) {
        String descr = etDescricao.getText().toString();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences sharedPrefInfoUser = getSharedPreferences(PREF_INFO_USER, Context.MODE_PRIVATE);
        String id_user = sharedPrefInfoUser.getString(ID_USER, "Sem Id");
        String token = sharedPrefInfoUser.getString(TOKEN, "Sem TOKEN");

        if (IGDbJsonParser.isConnectionInternet(getApplicationContext())) {
            if (validar() == true) {

                if (comentarios != null) {
                    comentarios.setDescricao(descr);
                    comentarios.setId_jogo(idjogo);
                    comentarios.setData(currentDate);
                    comentarios.setId_utilizador(Integer.parseInt(id_user));
                    SingletonGestorIGDb.getInstance(getApplicationContext()).adicionarComentariosAPI(comentarios, getApplicationContext(), token, Integer.parseInt(id_user));

                } else {
                    comentarios = new Comentarios(0, idjogo, Integer.parseInt(id_user), etDescricao.getText().toString(), currentDate);
                    SingletonGestorIGDb.getInstance(getApplicationContext()).adicionarComentariosAPI(comentarios, getApplicationContext(), token, Integer.parseInt(id_user));
                    // onRefreshDetalhes();
                }
            }
            return;

        } else {
            Toast.makeText(getApplicationContext(), "Sem acesso à internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validar() {
        String descr = etDescricao.getText().toString();
        if (descr.length() < 3) {
            etDescricao.setError("Comentários Invalido");
            return false;
        }
        return true;
    }
}