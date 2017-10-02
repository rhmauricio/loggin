package rhmauricio.loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class splash extends AppCompatActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences(Tags.TAG_PREFERENCES,MODE_PRIVATE);
        final int optLog = prefs.getInt("optLog",0);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //para que no se voltee la pantalla el el celular

                // una clase para crear una tarea temporizada
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                if(optLog==0) {
                    // clase y      contructor, puee ser vacio pero en este caso necesitamos algo detemrinado
                    intent = new Intent(splash.this, loggin.class); //tambien sirve para sacar los datos del celular como fotos y camara
                }else {
                    intent = new Intent(splash.this, MainActivity_loggin.class);

                }
                startActivity(intent);
                finish();// para que solo aparezca una vez

            }
        };
        Timer timer= new Timer();
        timer.schedule(task,3000); // para esperar 3000 ms

    }
}
