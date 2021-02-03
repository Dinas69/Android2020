package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Listeners.JogosListener;
import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.R;

public class DetalhesJogosActivity extends AppCompatActivity implements JogosListener {

    public static final String ID = "ID";
    private Jogos jogos;
    private TextView tvNome, tvDescricao, tvData, tvTipojogo, tvTrailer;
    private String token;
    private int idjogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_jogo);
        idjogo = getIntent().getIntExtra(ID, -1);
        jogos = SingletonGestorIGDb.getInstance(this).getJogo(idjogo);

        tvNome = findViewById(R.id.tvNomejogo);
        tvDescricao = findViewById(R.id.tvDescricaojogo);
        tvData = findViewById(R.id.tvDatajogo);
        tvTipojogo = findViewById(R.id.tvTipojogojogo);
        tvTrailer = findViewById(R.id.tvTrailerjogo);
        carregarDetalhesJogo();

    }

    public void ListaReviews(View view) {
        Intent intent = new Intent(this, ListaReviewsJogo.class);
        intent.putExtra(ListaReviewsJogo.ID, (int) idjogo);
        startActivity(intent);
    }

    public void ListaComent(View view) {
        Intent intent = new Intent(this, ListaCometariosJogo.class);
        intent.putExtra(ListaCometariosJogo.ID, (int) idjogo);
        startActivity(intent);
    }

    private void carregarDetalhesJogo() {
        tvNome.setText(jogos.getNome());
        tvDescricao.setText(jogos.getDescricao());
        tvTipojogo.setText(jogos.getNometipo());
        tvTrailer.setText(Html.fromHtml("<a href='https://www.youtube.com/watch?v=" + jogos.getTrailer() + "'>youtube trailer</a>"));
        tvData.setText(jogos.getData() + "");
    }

    @Override
    public void onRefreshListaJogos(ArrayList<Jogos> listaJogos) {
        //VAZIO
    }

    @Override
    public void onRefreshDetalhes() {
        setResult(RESULT_OK);
        finish();
    }

}