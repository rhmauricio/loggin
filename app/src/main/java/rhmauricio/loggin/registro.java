package rhmauricio.loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class registro extends AppCompatActivity {

    EditText eCorreo, eContraseña,erepContraseña,tname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        eCorreo= (EditText) findViewById(R.id.eCorreo);
        eContraseña= (EditText) findViewById(R.id.eContraseña);
        erepContraseña= (EditText) findViewById(R.id.erepContraseña);
        tname= (EditText) findViewById(R.id.tname);

    }

    public void Registrar(View view) {
        SharedPreferences prefs= getSharedPreferences(Tags.TAG_PREFERENCES,MODE_PRIVATE);
        String correo,contraseña="",contraseñar="",nombre="";
        correo=eCorreo.getText().toString();
        contraseña= eContraseña.getText().toString();
        contraseñar=erepContraseña.getText().toString();
        nombre=tname.getText().toString();


        //HCAER VALIDACIONES (3) CORREO VALIDO, NO ESPACIOS EN BLANCO Y CONTRASEÑA CORRECTA
        //como es una respuesta el Intent no tiene parametro
            if(correo.isEmpty() || contraseña.isEmpty()||contraseñar.isEmpty()){
                Toast t= Toast.makeText(getApplication().getApplicationContext(),"Espacios vacios",Toast.LENGTH_SHORT);
                t.show();
            }else {
                if (!validarEmail(correo)){
                    Toast t = Toast.makeText(getApplication().getApplicationContext(), "Correo Incorrecto", Toast.LENGTH_SHORT);
                    t.show();
                    Log.d("correcto",correo);

                }else if (contraseña.equals(contraseñar)) {

                    prefs.edit().putString(Tags.TAG_CORREO,correo).apply();
                    prefs.edit().putString(Tags.TAG_CONTRASEÑA,contraseña).apply();
                    prefs.edit().putString(Tags.TAG_NOMBRE,nombre).apply();
                    setResult(RESULT_OK);
                    finish();

                } else {
                    Toast t = Toast.makeText(getApplication().getApplicationContext(), "Contraseñas diferentes", Toast.LENGTH_SHORT);
                    t.show();
                }
            }

        }

    private boolean validarEmail (String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

}
