package amsi.dei.estg.ipleiria.igdb.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Adaptadores.ListaJogosAdaptador;
import amsi.dei.estg.ipleiria.igdb.Listeners.JogosListener;
import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.R;

public class ListaJogosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, JogosListener {

    private ListView lvListaJogos;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ListaJogosFragment() {
        //REQUER CONSTRUTOR VAZIO
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_lista_jogos, container, false);
        lvListaJogos = view.findViewById(R.id.lvListaJogos);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        SingletonGestorIGDb.getInstance(getContext()).setJogosListener(this);
        SingletonGestorIGDb.getInstance(getContext()).getAllJogosAPI(getContext());

        lvListaJogos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetalhesJogosActivity.class);
                intent.putExtra(DetalhesJogosActivity.ID, (int) id);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onRefreshListaJogos(ArrayList<Jogos> listaJogos) {
        if (listaJogos != null)
            lvListaJogos.setAdapter(new ListaJogosAdaptador(getContext(), listaJogos));
    }

    @Override
    public void onRefreshDetalhes() {
        //VAZIO
    }

    @Override
    public void onRefresh() {

        SingletonGestorIGDb.getInstance(getContext()).getAllJogosAPI(getContext());
        swipeRefreshLayout.setRefreshing(false);
    }
}
