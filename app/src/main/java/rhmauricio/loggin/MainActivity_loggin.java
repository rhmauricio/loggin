package rhmauricio.loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity_loggin extends AppCompatActivity {
    String correoR, contraseñaR;
    GoogleApiClient mGoogleApiClient;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_loggin);

        //para recibir los datos


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //.enableAutoManage(this /* FragmentActivity */ (contexto), this /* OnConnectionFailedListener */)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "Error en login",
                                Toast.LENGTH_SHORT).show();

                    }
                }  /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }



    //AHORA EL QUE SE ENCARGA DE GENERAR LA CONEXION


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.mperfil:
                intent = new Intent(MainActivity_loggin.this, perfil.class);

                startActivity(intent);
                finish();
                break;
            case R.id.mcerrar:
                prefs = getSharedPreferences(Tags.TAG_PREFERENCES,MODE_PRIVATE);
                editor=prefs.edit();

                editor.putInt("optLog",0);
                editor.apply(); // cada que s evaya a subir informacion debe haber un editor.commint

                LoginManager.getInstance().logOut();
                signOut();
                intent = new Intent(MainActivity_loggin.this, loggin.class);

                startActivity(intent);
                finish();//Elimina de la pila el que lo llamo
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }
}
