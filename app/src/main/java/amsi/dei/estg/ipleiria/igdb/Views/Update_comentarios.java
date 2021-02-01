package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.R;
import amsi.dei.estg.ipleiria.igdb.utils.IGDbJsonParser;

public class Update_comentarios extends AppCompatActivity {

    public static final String ID = "ID";
    public static final String DESCRICAO = "DESCRICAO";

    public static final String TOKEN = "TOKEN";
    public static final String ID_USER = "ID_USER";
    public static final String ID_JOGO = "ID_JOGO";

    public static final String PREF_INFO_USER = "PREF_INFO_USER";

    private int idcoment;

    private String descricaocoment, token, id_user, id_jogo;
    private EditText etDescricao;
    private Comentarios comentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_comentarios);

        idcoment = getIntent().getIntExtra(ID, -1);
        comentarios = SingletonGestorIGDb.getInstance(this).getComentario(idcoment);

        descricaocoment = getIntent().getStringExtra(DESCRICAO);

        etDescricao = findViewById(R.id.etDescricaoUpdateCom);
        etDescricao.setText(descricaocoment);


        SharedPreferences sharedPrefInfoUser = getSharedPreferences(PREF_INFO_USER, Context.MODE_PRIVATE);
        id_user = sharedPrefInfoUser.getString(ID_USER, "Sem Id");
        token = sharedPrefInfoUser.getString(TOKEN, "Sem TOKEN");
        id_jogo = sharedPrefInfoUser.getString(ID_JOGO, "Sem TOKEN");

       /* String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Log.e("ONCLICKITEM", "Review id:" + idreview);
        Log.e("ONCLICKITEM", "ID_JOGO :" + id_jogo);
        Log.e("ONCLICKITEM", "ID_USER :" + id_user);
        Log.e("ONCLICKITEM", "SCORE :" + etScore.getText().toString());
        Log.e("ONCLICKITEM", "etDescricao :" + etDescricao.getText().toString());
        Log.e("ONCLICKITEM", "token :" + token);
        Log.e("ONCLICKITEM", "currentDate :" + currentDate);*/
    }

    public void ComentariosJogoComUpdate(View view) {
        if (IGDbJsonParser.isConnectionInternet(getApplicationContext())) {
            if (validar() == true) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                if (comentarios != null) {
                    comentarios.setId_jogo(Integer.parseInt(id_jogo));
                    comentarios.setId_utilizador(Integer.parseInt(id_user));
                    comentarios.setData(currentDate);
                    comentarios.setDescricao(etDescricao.getText().toString());
                    SingletonGestorIGDb.getInstance(getApplicationContext()).editarComentarioAPI(comentarios, getApplicationContext(), token);
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

    public void ComentariosJogoComDelete(View view) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar comentario")
                .setMessage("Pretende mesmo remover?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonGestorIGDb.getInstance(getApplicationContext()).removerComentarioAPI(comentarios, getApplicationContext(), token);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();
        if (IGDbJsonParser.isConnectionInternet(getApplicationContext())) {

        } else {
            Toast.makeText(getApplicationContext(), "Sem acesso à internet", Toast.LENGTH_SHORT).show();
        }
    }
}