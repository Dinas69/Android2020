package amsi.dei.estg.ipleiria.igdb.Adaptadores;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import amsi.dei.estg.ipleiria.igdb.Modelos.Comentarios;
import amsi.dei.estg.ipleiria.igdb.Modelos.Uploadimagem;
import amsi.dei.estg.ipleiria.igdb.R;

public class ListaUploadAdaptador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Uploadimagem> uploadimagems;
    private ImageView imageView;
    private String Document_img1 = "";

    public ListaUploadAdaptador(Context context, ArrayList<Uploadimagem> uploadimagems) {
        this.context = context;
        this.uploadimagems = uploadimagems;
    }

    @Override
    public int getCount() {
        return uploadimagems.size();
    }

    @Override
    public Object getItem(int position) {
        return uploadimagems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return uploadimagems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lista_uploads, null);
        }
        /*Otimização*/
        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.update(uploadimagems.get(position));

        return convertView;
    }

    private class ViewHolderLista {
        private TextView tvUploadNome, tvUploadUser;

        public ViewHolderLista(View view) {
            tvUploadNome = view.findViewById(R.id.tvUploadNome);
            tvUploadUser = view.findViewById(R.id.tvUploadUser);
            //imageViewUpload = view.findViewById(R.id.imageViewUpload);
            imageView = view.findViewById(R.id.imageViewUpload);
        }

        public void update(Uploadimagem uploadimagems) {
            try {

                tvUploadNome.setText(String.valueOf(uploadimagems.getNome()));
                tvUploadUser.setText(uploadimagems.getId_user() + "");

                //ontem PATH da imagem
                String string = uploadimagems.getPath();
                //Cria um ficheiro com o caminho da imagem
                File imgFile = new File(string);
                //Verifica se existe
                if (imgFile.exists()) {
                    //Decode
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    //Resize
                    myBitmap = getResizedBitmap(myBitmap, 400);
                    //Set imagem
                    imageView.setImageBitmap(myBitmap);
                }

            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }

        public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            return Bitmap.createScaledBitmap(image, width, height, true);
        }
    }
}
