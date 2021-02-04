package amsi.dei.estg.ipleiria.igdb.Views;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import amsi.dei.estg.ipleiria.igdb.Listeners.UploadListener;
import amsi.dei.estg.ipleiria.igdb.Modelos.IGDbHelper;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.Modelos.Uploadimagem;
import amsi.dei.estg.ipleiria.igdb.R;

public class Upload_imagem extends AppCompatActivity implements View.OnClickListener {


    public static final String PREF_INFO_USER = "PREF_INFO_USER";
    public static final String TOKEN = "TOKEN";
    public static final String ID_USER = "ID_USER";

    public static final String KEY_User_Document1 = "doc1";
    ImageView IDProf;
    Button Upload_Btn;
    //USER
    private String id_user, token;

    private String Document_img1 = "";
    IGDbHelper igDbHelper;
    Uploadimagem uploadimagem;

    String nomeImagem, picturePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_imagem);

        //GET SHARE
        SharedPreferences sharedPrefInfoUser = getSharedPreferences(PREF_INFO_USER, Context.MODE_PRIVATE);
        id_user = sharedPrefInfoUser.getString(ID_USER, "Sem Id");
        token = sharedPrefInfoUser.getString(TOKEN, "Sem TOKEN");

        IDProf = (ImageView) findViewById(R.id.IdProf);
        Upload_Btn = (Button) findViewById(R.id.UploadBtn);

        IDProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                       /* Toast.makeText(Upload_imagem.this, "Permission granted", Toast.LENGTH_SHORT).show();
                        selectImage();*/
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        //requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
                    }
                    if (getApplicationContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                       /* Toast.makeText(Upload_imagem.this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                        selectImage();*/
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
                    }
                } else {
                    Toast.makeText(Upload_imagem.this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                selectImage();
            }
        });

        Upload_Btn.setOnClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(Upload_imagem.this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Upload_imagem.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(Upload_imagem.this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Upload_imagem.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Tirar foto", "Escolhe na galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Upload_imagem.this);
        builder.setTitle("Adicionar foto!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                try {
                    if (options[item].equals("Tirar foto")) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);

                       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));*/
                        /*startActivityForResult(intent, 1);*/
                    } else if (options[item].equals("Escolhe na galeria")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2) {
                Uri selectedImage = data.getData();
                //GET PATH
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                //SET PATH
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                //GET NAME
                String[] filePath2 = {MediaStore.Images.Media.DISPLAY_NAME};
                Cursor c2 = getContentResolver().query(selectedImage, filePath2, null, null, null);
                c2.moveToFirst();

                //SET NAME
                int nameIndex = c2.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                nomeImagem = c2.getString(nameIndex);
                c2.close();

                ParcelFileDescriptor pfd = getApplicationContext().getContentResolver().openFileDescriptor(selectedImage, "r");
                Bitmap bitmap2 = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());

                bitmap2 = getResizedBitmap(bitmap2, 400);
                IDProf.setImageBitmap(bitmap2);
                BitMapToString(bitmap2);
            } else if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                IDProf.setImageBitmap(photo);

                //Cria o nome para a foto
                String obtemNome = random2();
                //Convert Bitmap to String
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), photo, obtemNome, null);

                //obtem a STRING do Bitmap e convert para Uri
                Uri selectedImage = Uri.parse(path);
                //GET PATH
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                //SET PATH
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                //GET NAME
                String[] filePath2 = {MediaStore.Images.Media.DISPLAY_NAME};
                Cursor c2 = getContentResolver().query(selectedImage, filePath2, null, null, null);
                c2.moveToFirst();

                //SET NAME
                int nameIndex = c2.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                nomeImagem = c2.getString(nameIndex);
                c2.close();

                ParcelFileDescriptor pfd = getApplicationContext().getContentResolver().openFileDescriptor(selectedImage, "r");
                Bitmap bitmap2 = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());

                bitmap2 = getResizedBitmap(bitmap2, 400);
                IDProf.setImageBitmap(bitmap2);
                BitMapToString(bitmap2);

                return;
            }
        } catch (Exception e) {
            Log.e("UPLOAD2", e + "");
        }
    }

    private static String random2() {
        String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";
        int sizeOfRandomString = 40;
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
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

    @Override
    public void onClick(View v) {
        if (Document_img1.equals("") || Document_img1.equals(null)) {
            ContextThemeWrapper ctw = new ContextThemeWrapper(Upload_imagem.this, R.style.Theme_AppCompat);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctw);
            alertDialogBuilder.setTitle("Empty");
            alertDialogBuilder.setMessage("Não pode mandar este valor null, por favor insira uma imagem.");
            alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            alertDialogBuilder.show();
            return;
        } else {
            try {

                Uploadimagem conteudo = new Uploadimagem();
                //Conteudo para o POST
                conteudo.setNome(nomeImagem);
                conteudo.setPath(picturePath);
                conteudo.setId_user(Integer.parseInt(id_user));
                //POST do upload
                SingletonGestorIGDb.getInstance(getApplicationContext()).uploadImagemAPI(conteudo, getApplicationContext(), token);
                //Mensagem de sucesso
                Toast.makeText(getApplicationContext(), "Enviado com sucesso", Toast.LENGTH_SHORT).show();
                //Reset ImageView
                ImageView imageView = findViewById(R.id.IdProf);
                imageView.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
            } catch (Exception e) {
                Log.e("UPLOAD", e.getMessage());
            }
        }
    }

    public void OpenGaleria(View view) {
        Intent intent = new Intent(this, ListaUploads.class);
        intent.putExtra("id_user", String.valueOf(id_user));
        intent.putExtra("token", token);
        startActivity(intent);
    }
}