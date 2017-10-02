package rhmauricio.loggin;

/**
 * Created by mauri on 1/10/2017.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

public class FetchImage extends AsyncTask<String, Void, Bitmap> {

    public interface AsyncResponse{
        void processFinish(Bitmap bitmap);
    }

    private Context context;
    private final AsyncResponse delegate;

    public FetchImage(Context context, AsyncResponse listener){
        this.context = context;
        this.delegate = listener;
    }

    @Override
    protected Bitmap doInBackground(String... objects) {
        Bitmap bitmap = null;
        try {
            URL imageURL = new URL(objects[0]);
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        }catch (Exception e){
            Log.d("URL Exception", e.toString());
        }finally {
            // Return image
            return bitmap;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap == null){
            delegate.processFinish(null);
        }
        else {
            if(this.delegate != null)
                delegate.processFinish(bitmap);
        }
    }
}
