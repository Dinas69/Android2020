package amsi.dei.estg.ipleiria.igdb.Listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;

public interface ReviewsListener {

    void onRefreshListaReviews(ArrayList<Reviews> listaReviews);

    void onRefreshDetalhes();
}
