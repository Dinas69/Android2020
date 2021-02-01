package amsi.dei.estg.ipleiria.igdb.Listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;

public interface ComentListener {

    void onRefreshListaComentarios(ArrayList<Comentarios> listaComentarios);

    void onRefreshDetalhes();
}
