package amsi.dei.estg.ipleiria.igdb.Adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Jogos;
import amsi.dei.estg.ipleiria.igdb.R;

public class ListaJogosAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Jogos> jogos;

    public ListaJogosAdaptador(Context context, ArrayList<Jogos> jogos) {
        this.context = context;
        this.jogos = jogos;
    }

    @Override
    public int getCount() {
        return jogos.size();
    }

    @Override
    public Object getItem(int position) {
        return jogos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return jogos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista_jogo, null);
        }
        /*Otimização*/
        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.update(jogos.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvNome, tvData, tvTipojogo;

        public ViewHolderLista(View view) {
            tvNome = view.findViewById(R.id.tvNome);
            tvData = view.findViewById(R.id.tvData);
            tvTipojogo = view.findViewById(R.id.tvTipojogo);
        }

        public void update(Jogos jogo) {
            tvNome.setText(jogo.getNome());
            tvData.setText(jogo.getData() + "");
            tvTipojogo.setText(jogo.getNometipo());
        }
    }
}
