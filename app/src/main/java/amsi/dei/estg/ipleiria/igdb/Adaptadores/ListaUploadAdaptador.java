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

                String string = uploadimagems.getPath();
                File imgFile = new File(string);

                if (imgFile.exists()) {
                    //Uri selectedImage = Uri.parse("file://" + string);
                   // Uri selectedImage2 = Uri.fromFile(new File(string));

                    //ParcelFileDescriptor pfd = context.getApplicationContext().getContentResolver().openFileDescriptor(selectedImage2, "r");
                    //decode
                    //Bitmap bitmap2 = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    myBitmap = getResizedBitmap(myBitmap, 400);

                    //bitmap2 = getResizedBitmap(bitmap2, 400);

                    imageView.setImageBitmap(myBitmap);
                    //BitMapToString(bitmap2);
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

        public String BitMapToString(Bitmap userImage1) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
            byte[] b = baos.toByteArray();
            Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
            return Document_img1;
        }
    }
}
