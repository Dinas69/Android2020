package amsi.dei.estg.ipleiria.igdb.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;
import amsi.dei.estg.ipleiria.igdb.R;

public class ListaComentariosAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Comentarios> comentarios;

    public ListaComentariosAdaptador(Context context, ArrayList<Comentarios> comentarios) {
        this.context = context;
        this.comentarios = comentarios;
    }

    @Override
    public int getCount() {
        return comentarios.size();
    }

    @Override
    public Object getItem(int position) {
        return comentarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return comentarios.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista_comentario, null);
        }
        /*Otimização*/
        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.update(comentarios.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvUsername, tvData, tvDescricaoCom;

        public ViewHolderLista(View view) {
            tvUsername = view.findViewById(R.id.tvUsername);
            tvData = view.findViewById(R.id.tvData);
            tvDescricaoCom = view.findViewById(R.id.tvDescricaoCom);
        }

        public void update(Comentarios comentarios) {
            tvUsername.setText(String.valueOf(comentarios.getId_utilizador()));
            tvData.setText(comentarios.getData() + "");
            tvDescricaoCom.setText(comentarios.getDescricao());
        }
    }
}
