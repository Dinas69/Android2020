package amsi.dei.estg.ipleiria.igdb.Listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;

public interface JogosListener {

    void onRefreshListaJogos(ArrayList<Jogos> listaJogos);

    void onRefreshDetalhes();
}
