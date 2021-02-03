package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaReviewsAdaptador;
import amsi.dei.estg.ipleiria.igdb.Listeners.ReviewsListener;
import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.R;
import amsi.dei.estg.ipleiria.igdb.utils.IGDbJsonParser;

public class ListaReviewsJogo extends AppCompatActivity implements ReviewsListener {

    public static final String TOKEN = "TOKEN";
    public static final String ID_USER = "ID_USER";

    public static final String ID = "ID";
    public static final String PREF_INFO_USER = "PREF_INFO_USER";
    private int idjogo;
    private String id_user, token;

    private ListView lvListaReviews;
    private EditText etDescricaorev, etScore;

    private Reviews reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reviews_jogo);

        idjogo = getIntent().getIntExtra(ID, -1);

        //GET SHARE
        SharedPreferences sharedPrefInfoUser = getSharedPreferences(PREF_INFO_USER, Context.MODE_PRIVATE);
        id_user = sharedPrefInfoUser.getString(ID_USER, "Sem Id");
        token = sharedPrefInfoUser.getString(TOKEN, "Sem TOKEN");


        etDescricaorev = findViewById(R.id.etDescreview);
        etScore = findViewById(R.id.etScore);
        lvListaReviews = findViewById(R.id.lvListaReviews);

        SingletonGestorIGDb.getInstance(this).setReviewsListener(this);
        SingletonGestorIGDb.getInstance(this).getReviewsJogo(this, idjogo);

        lvListaReviews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Reviews r = SingletonGestorIGDb.getInstance(getApplicationContext()).getReview((int) id);
                //VERIFICA SE O UTILIZADOR LOGADO PODE FAZER ALTERAÇÔES
                if (Integer.parseInt(id_user) != r.getId_utilizador()) {
                    Toast.makeText(getApplicationContext(), "Este comentário não é seu", Toast.LENGTH_SHORT).show();
                    return;
                }

                String descricao = ((TextView) view.findViewById(R.id.tvDescricaoRev)).getText().toString();
                String score = ((TextView) view.findViewById(R.id.tvScoreRev)).getText().toString();

                openActivityReviewsUpdate(id, descricao, score);
            }
        });

    }

    public void openActivityReviewsUpdate(long id, String descricao, String score) {
        Intent intent = new Intent(this, Update_reviews.class);

        intent.putExtra(Update_reviews.ID, (int) id);
        intent.putExtra(Update_reviews.DESCRICAO, (String) descricao);
        intent.putExtra(Update_reviews.SCORE, (String) score);
        //SHARE USER SETTINGS
        SharedPreferences sharedPrefUser2 = getSharedPreferences(Update_reviews.PREF_INFO_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPrefUser2.edit();
        editor2.putString(Update_reviews.TOKEN, token);
        editor2.putString(Update_reviews.ID_USER, id_user);
        editor2.putString(Update_reviews.ID_JOGO, String.valueOf(idjogo));
        editor2.apply();
        startActivity(intent);
    }

    @Override
    public void onRefreshListaReviews(ArrayList<Reviews> listaReviews) {
        if (lvListaReviews != null)
            lvListaReviews.setAdapter(new ListaReviewsAdaptador(this, listaReviews));
    }


    @Override
    public void onRefreshDetalhes() {
        setResult(RESULT_OK);
        finish();
        startActivity(getIntent());
    }

    public void ReviewsJogocom(View view) {
        String descr = etDescricaorev.getText().toString();
        String scor = etScore.getText().toString();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        SharedPreferences sharedPrefInfoUser = getSharedPreferences(PREF_INFO_USER, Context.MODE_PRIVATE);
        String id_user = sharedPrefInfoUser.getString(ID_USER, "Sem Id");
        String token = sharedPrefInfoUser.getString(TOKEN, "Sem TOKEN");

        if (IGDbJsonParser.isConnectionInternet(getApplicationContext())) {
            if (validarRev() == true) {

                if (reviews != null) {
                    reviews.setDescricao(descr);
                    reviews.setId_jogo(idjogo);
                    reviews.setData(currentDate);
                    reviews.setId_utilizador(Integer.parseInt(id_user));
                    reviews.setScore(scor);
                    SingletonGestorIGDb.getInstance(getApplicationContext()).adicionarReviewsAPI(reviews, getApplicationContext(), token, Integer.parseInt(id_user));

                } else {
                    reviews = new Reviews(0, idjogo, Integer.parseInt(id_user), etDescricaorev.getText().toString(), currentDate, scor);
                    SingletonGestorIGDb.getInstance(getApplicationContext()).adicionarReviewsAPI(reviews, getApplicationContext(), token, Integer.parseInt(id_user));
                }
            }
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Sem acesso à internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validarRev() {
        String descr = etDescricaorev.getText().toString();
        String scor = etScore.getText().toString();

        if (descr.length() < 3) {
            etDescricaorev.setError("Review Invalido");
            return false;
        }

        if (TextUtils.isEmpty(scor)) {
            etScore.setError("Score Invalido");
            return false;
        } else {
            Float num = Float.parseFloat(scor);

            if (num > 10) {
                etScore.setError("Número maior que 10");
                return false;
            }
            if (num < 0) {
                etScore.setError("Número menor que 0");
                return false;
            }
        }

        return true;
    }


}