package amsi.dei.estg.ipleiria.igdb.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import amsi.dei.estg.ipleiria.igdb.Modelos.IGDbHelper;
import amsi.dei.estg.ipleiria.igdb.R;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private IGDbHelper igDbHelper;

    public static final String EMAIL = "EMAIL";
    public static final String TOKEN = "TOKEN";
    public static final String ID_USER = "ID_USER";
    public static final String PREF_INFO_USER = "PREF_INFO_USER";
    //CRIAR MENU HAMBURGUER
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private String email = "";
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        igDbHelper = new IGDbHelper(this);

        //CUSTOM TOOLBAR
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();

        carregarFragmentoInicial();
        //carregarCabecalho();
    }

    private void carregarFragmentoInicial() {
        navigationView.setCheckedItem(R.id.nav_Lista_jogo);
        Fragment fragment = new ListaJogosFragment();
        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_Lista_jogo:
                fragment = new ListaJogosFragment();
                setTitle(item.getTitle());
                break;
            case R.id.nav_upload_imagem:
                Intent intent = new Intent(this, Upload_imagem.class);
                startActivity(intent);
                //fragment = new DinamicoFragment();
                //setTitle(item.getTitle());
                break;
            case R.id.nav_email:
                System.out.println("-->Nav Email");
                break;
        }
        return false;
    }
}