package amsi.dei.estg.ipleiria.igdb.Listeners;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;
import amsi.dei.estg.ipleiria.igdb.Modelos.Uploadimagem;

public interface UploadListener {

    void onRefreshListaUpload(ArrayList<Uploadimagem> listaUpload);

    void onRefreshDetalhes();
}
