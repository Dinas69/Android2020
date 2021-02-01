package amsi.dei.estg.ipleiria.igdb.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Reviews;
import amsi.dei.estg.ipleiria.igdb.R;

public class ListaReviewsAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Reviews> reviews;

    public ListaReviewsAdaptador(Context context, ArrayList<Reviews> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return reviews.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista_reviews, null);
        }
        /*Otimização*/
        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.update(reviews.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvUsername, tvData, tvDescricao, tvScore;

        public ViewHolderLista(View view) {
            tvUsername = view.findViewById(R.id.tvUsernameRev);
            tvData = view.findViewById(R.id.tvDataRev);
            tvScore = view.findViewById(R.id.tvScoreRev);
            tvDescricao = view.findViewById(R.id.tvDescricaoRev);
        }

        public void update(Reviews reviews) {
            tvUsername.setText(String.valueOf(reviews.getId_utilizador()));
            tvData.setText(reviews.getData() + "");
            tvScore.setText(reviews.getScore());
            tvDescricao.setText(reviews.getDescricao());
        }
    }
}
