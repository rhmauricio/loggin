package rhmauricio.loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class loggin extends AppCompatActivity {
    String correoR="", contraseñaR="",correo="", contraseña="", picture="";
    EditText eCorreo, eContraseña;
    LoginButton loginButton;
    CallbackManager callbackManager;
    public static int opLog=0;
    GoogleApiClient mGoogleApiClient;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);

        prefs = getSharedPreferences(Tags.TAG_PREFERENCES, MODE_PRIVATE);
        loginButton=(LoginButton)findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        callbackManager=CallbackManager.Factory.create(); //fuerza a que se ejeucte el metodo de respuesta
        eCorreo=(EditText)findViewById(R.id.eCorreo);
        eContraseña=(EditText)findViewById(R.id.eContraseña);


        //LOGIN GOOGLE
        //GSO ME DA LOS ACCESOS A LOS SERVICIOS DE GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //AHORA EL QUE SE ENCARGA DE GENERAR LA CONEXION
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

        // el listener del boton

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        //registro con face

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),"Login Exitoso",
                        Toast.LENGTH_SHORT).show();
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,picture");
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    // get email and id of the user
                                    try{
                                        String uriPicture = "";
                                        if (me.getString("email") != null)
                                            prefs.edit().putString(Tags.TAG_CORREO, me.getString("email")).apply();

                                        if(me.getString("name") != null)
                                            prefs.edit()
                                                    .putString(Tags.TAG_NOMBRE,me.getString("name"))
                                                    .apply();

                                        if (me.getString("picture") != null){
                                            uriPicture = new JSONObject(
                                                    new JSONObject(me.getString("picture")).getString("data")).getString("url");
                                            prefs.edit()
                                                    .putString(Tags.TAG_URLIMAGEN,uriPicture)
                                                    .apply();
                                        }


                                    }catch (Exception e){
                                        Log.d("FB Error", e.toString());
                                    }
                                    opLog=2;
                                    goMainActivity();
                                }
                            }
                        });
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Login Cancelado",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"Error de Login",
                        Toast.LENGTH_SHORT).show();
            }

        });

/*        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "rhmauricio.loggin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }*/
    }

    public void regitrarse(View view) {
        //cuando se de click en registrar se llama a la actividad registro y debemos esperar datos

        Intent intent = new Intent(loggin.this, registro.class); // de donde vengo y a donde voy
        startActivityForResult(intent,1234); // el codigo es para que el registro activity responda con ese codigo

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1234 && resultCode==RESULT_OK) {
            opLog = 1;

        }else if(requestCode==5678){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();

            try {
                if(account.getEmail()!=null)
                    prefs.edit().putString(Tags.TAG_CORREO, account.getEmail()).apply();
                else
                    prefs.edit().putString(Tags.TAG_CORREO, "No tiene correo").apply();
                if(account.getDisplayName() != null)
                    prefs.edit().putString(Tags.TAG_NOMBRE,account.getDisplayName()).apply();
                else
                    prefs.edit().putString(Tags.TAG_NOMBRE,"No tiene nombre").apply();
                if(account.getPhotoUrl() != null)
                    prefs.edit().putString(Tags.TAG_URLIMAGEN,account.getPhotoUrl().toString()).apply();
                else
                    prefs.edit().putString(Tags.TAG_URLIMAGEN,null).apply();

            }catch (Exception e){
                Log.d("Google Error", e.toString());

            }

            handleSignInResult(result);


        }else{
            callbackManager.onActivityResult(requestCode,resultCode,data);

            opLog=2;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void inciar(View view) {

        correo=eCorreo.getText().toString();
        contraseña=eContraseña.getText().toString();

        contraseñaR=prefs.getString(Tags.TAG_CONTRASEÑA,"");
        correoR=prefs.getString(Tags.TAG_CORREO,"");

        if(correo.isEmpty() || contraseña.isEmpty()){
            Toast t= Toast.makeText(getApplication().getApplicationContext(),"Espacios Vacios",Toast.LENGTH_SHORT);
            t.show();

        }else if(correoR.isEmpty() || contraseñaR.isEmpty()) {
            Toast t= Toast.makeText(getApplication().getApplicationContext(),"Debe registrar los datos",Toast.LENGTH_SHORT);
            t.show();

        }else if(contraseña.equals(contraseñaR) && correo.equals(correoR)){
           goMainActivity();
        }else{
            Toast t= Toast.makeText(getApplication().getApplicationContext(),"Datos invalidos",Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void goMainActivity(){

        editor=prefs.edit();editor.putInt("optLog",opLog);

        editor.putInt("optLog",opLog);
        editor.commit();

        Intent intent = new Intent(loggin.this, MainActivity_loggin.class);
        startActivity(intent);
        finish();
    }
    public void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 5678);
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GOOGLE", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
           Toast.makeText(getApplicationContext(),acct.getDisplayName(),Toast.LENGTH_SHORT).show();
            opLog=3;
            goMainActivity();
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getApplicationContext(),"Error login",Toast.LENGTH_SHORT).show();

        }
    }
}
