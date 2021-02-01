package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.R;
import amsi.dei.estg.ipleiria.igdb.utils.IGDbJsonParser;

public class Update_reviews extends AppCompatActivity {

    public static final String ID = "ID";
    public static final String DESCRICAO = "DESCRICAO";
    public static final String SCORE = "SCORE";

    public static final String TOKEN = "TOKEN";
    public static final String ID_USER = "ID_USER";
    public static final String ID_JOGO = "ID_JOGO";

    public static final String PREF_INFO_USER = "PREF_INFO_USER";

    private int idreview;

    private String descricaoreview, scorereview, token, id_user, id_jogo;
    private EditText etDescricao, etScore;
    private Reviews reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reviews);

        idreview = getIntent().getIntExtra(ID, -1);
        reviews = SingletonGestorIGDb.getInstance(this).getReview(idreview);

        descricaoreview = getIntent().getStringExtra(DESCRICAO);
        scorereview = getIntent().getStringExtra(SCORE);

        etDescricao = findViewById(R.id.etDescricaoUpdateRev);
        etDescricao.setText(descricaoreview);

        etScore = findViewById(R.id.etScoreRevUpdate);
        etScore.setText(scorereview);

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

    public void ReviewsJogoRevUpdate(View view) {
        if (IGDbJsonParser.isConnectionInternet(getApplicationContext())) {
            if (validarRev() == true) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                if (reviews != null) {
                    reviews.setId_jogo(Integer.parseInt(id_jogo));
                    reviews.setId_utilizador(Integer.parseInt(id_user));
                    reviews.setScore(etScore.getText().toString());
                    reviews.setData(currentDate);
                    reviews.setDescricao(etDescricao.getText().toString());
                    SingletonGestorIGDb.getInstance(getApplicationContext()).editarReviewAPI(reviews, getApplicationContext(), token);
                }
            }
            return;

        } else {
            Toast.makeText(getApplicationContext(), "Sem acesso à internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validarRev() {
        String descr = etDescricao.getText().toString();
        String scor = etScore.getText().toString();

        if (descr.length() < 3) {
            etDescricao.setError("Review Invalido");
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

        Log.e("REVIEWSTESTE", scor + ":");

        return true;
    }

    public void ReviewsJogoRevDelete(View view) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar review")
                .setMessage("Pretende mesmo remover?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonGestorIGDb.getInstance(getApplicationContext()).removerReviewAPI(reviews, getApplicationContext(), token);
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