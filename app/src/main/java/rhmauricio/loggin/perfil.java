package rhmauricio.loggin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.io.InputStream;
import java.net.URL;

public class perfil extends AppCompatActivity {
    String correoR,cumpleaños,nombre;
    ImageView imageView3;
    SharedPreferences prefs;
    TextView tcorreo,tcumpleaños,tnombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        prefs=getSharedPreferences(Tags.TAG_PREFERENCES,MODE_PRIVATE);

        imageView3=(ImageView)findViewById(R.id.imageView3);
        tcorreo=(TextView)findViewById(R.id.tcorreo);
        tnombre=(TextView)findViewById(R.id.tnombre);

        correoR=prefs.getString(Tags.TAG_CORREO, "");
        nombre=prefs.getString(Tags.TAG_NOMBRE,"");
        cumpleaños=prefs.getString(Tags.TAG_CUMPLEAÑOS,"");

        if (prefs.getInt(Tags.TAG_OPTLOG, 0) == 1){
            imageView3.setImageDrawable(getDrawable(R.drawable.ic_account_circle_black_48dp));
        }else{
            // Create image from fb URL
            FetchImage fetchImage = new FetchImage(this, new FetchImage.AsyncResponse() {
                @Override
                public void processFinish(Bitmap bitmap) {
                    if (bitmap != null) {
                        Resources res = getResources();
                        RoundedBitmapDrawable roundBitmap = RoundedBitmapDrawableFactory
                                .create(res, bitmap);
                        roundBitmap.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
                        imageView3.setImageDrawable(roundBitmap);
                    }else
                        imageView3.setImageDrawable(getDrawable(R.drawable.ic_account_circle_black_48dp));
                }
            });
            fetchImage.execute(prefs.getString(Tags.TAG_URLIMAGEN, ""));
        }

        tcorreo.setText(correoR);
        tnombre.setText(nombre);



    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.m2principal:
                LoginManager.getInstance().logOut();
                intent=new Intent(perfil.this, MainActivity_loggin.class );
                startActivity(intent);
                finish();
                break;
            case R.id.m2cerrarSesion:
                LoginManager.getInstance().logOut();
                intent=new Intent(perfil.this, loggin.class );
                prefs.edit().putInt(Tags.TAG_OPTLOG,0).apply();
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

