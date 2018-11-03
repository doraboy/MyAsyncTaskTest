package tw.dora.myasynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mesg;
    private MyAsyncTask myAsyncTask;
    private int[] imgIds = {R.id.img0,R.id.img1,R.id.img2};
    private ImageView[] imgViews = new ImageView[imgIds.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mesg=findViewById(R.id.mesg);
        for(int i=0;i<imgViews.length;i++){
            imgViews[i] = findViewById(imgIds[i]);
        }

    }

    public void test1(View view) {
        myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute("https://www.wikihow.com/images/2/24/Conference-Call-on-an-Android-Step-11.jpg",
                "https://www.androidpolice.com/wp-content/uploads/2018/04/SMS-conversation-Android-Messages-Hero.jpg",
                "http://cdn.gtricks.com/2017/07/AFFIX_20170726_182447.png"
                );

    }

    public void test2(View view) {
        if(myAsyncTask!=null){
            myAsyncTask.cancel(true);
        }
    }

    private class MyAsyncTask extends AsyncTask<String,Bitmap,Void>{
        private int index = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.v("brad","onPreExecute");
        }

        @Override
        protected Void doInBackground(String... urls) {
            Log.v("brad","doInBackground");
            String ret ="OK";
            for(String url:urls){
                Log.v("brad",url);

                try {
                    URL imgurl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imgurl.openConnection();
                    Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());
                    publishProgress(bmp);
                } catch (Exception e) {
                    Log.v("brad",e.toString());
                    ret = "canceled";
                    break;
                }


            }
            return null;
        }

        //這裡可秀前景
        @Override
        protected void onProgressUpdate(Bitmap... bmps) {
            super.onProgressUpdate(bmps);
            Log.v("brad","onProgressUpdate");
            imgViews[index].setImageBitmap(bmps[0]);
            index++;
        }

        //這裡可秀前景
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.v("brad","onPostExecute");
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("秀完圖片了")
                    .show();

        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
            Log.v("brad","onCancelled(Void aVoid)");
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.v("brad","onCancelled()");
        }


    }
}
