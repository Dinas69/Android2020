package amsi.dei.estg.ipleiria.igdb.Views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import amsi.dei.estg.ipleiria.igdb.Listeners.LoginListener;
import amsi.dei.estg.ipleiria.igdb.MainActivity;
import amsi.dei.estg.ipleiria.igdb.Modelos.IGDbHelper;
import amsi.dei.estg.ipleiria.igdb.Modelos.SingletonGestorIGDb;
import amsi.dei.estg.ipleiria.igdb.R;
import amsi.dei.estg.ipleiria.igdb.utils.IGDbJsonParser;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private EditText etEmail, etPassword;

    private IGDbHelper igDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        igDbHelper = new IGDbHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        etEmail.setText("dinas");
        etPassword.setText("monca99per");

        SingletonGestorIGDb.getInstance(getApplicationContext()).setLoginListener(this);
    }

    public void onClickLogin(View view) {
        Log.e("Onclicklogin", "true");
        if (IGDbJsonParser.isConnectionInternet(getApplicationContext())) {
            Log.e("Onclicklogin", "dentro if");
            //Verificar se a password tem pelo menos 4 carateres
            // se o email é valido
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            Log.e("Onclicklogin44", email);
            if (!isUsernameNull(email)) {
                etEmail.setError("Username Invalido");
                return;
            }
            if (!isPasswordValida(password)) {
                etPassword.setError("Password Invalida");
                return;
            }

            SingletonGestorIGDb.getInstance(getApplicationContext()).loginAPI(email, password, getApplicationContext());
            Log.e("Onclicklogin", "SINGLETON COMPLETO");
        } else {
            Log.e("Onclicklogin", "fora if");
            Toast.makeText(getApplicationContext(), "Sem acesso há internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isUsernameNull(String username) {
        if (username.matches("")) {
            return false;
        } else
            return true;
    }

    private boolean isPasswordValida(String password) {
        if (password == null)
            return false;

        return password.length() >= 4;
    }

    @Override
    public void onValidateLogin(String token, String email, String id) {
        Log.e("Onclicklogin22", "onValidateLogin" + token);
        if (token != "fail") {
            //CODIGO PARA MUDAR DE ACTIVIDADE
            guardarInfoSharedPref(token, email, id);
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Login inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarInfoSharedPref(String token, String email, String id) {
        SharedPreferences sharedPrefUser = getSharedPreferences(ListaCometariosJogo.PREF_INFO_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefUser.edit();
        editor.putString(MainMenuActivity.EMAIL, email);
        editor.putString(MainMenuActivity.TOKEN, token);
        editor.putString(MainMenuActivity.ID_USER, id);
        //Para Comentar
        editor.putString(ListaCometariosJogo.TOKEN, token);
        editor.putString(ListaCometariosJogo.ID_USER, id);
        editor.apply();

        //Para Reviews
        SharedPreferences sharedPrefUser2 = getSharedPreferences(ListaReviewsJogo.PREF_INFO_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPrefUser2.edit();
        editor2.putString(ListaReviewsJogo.TOKEN, token);
        editor2.putString(ListaReviewsJogo.ID_USER, id);
        editor2.apply();
    }
}
