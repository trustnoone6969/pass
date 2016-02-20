package org.delta.pass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;


public class Splash extends Activity {

    String utext="null";

    Boolean root;


        private class SUTask extends AsyncTask<Void, Boolean, Boolean> {
            private final Context context;
            private List<String> suResult = null;

            public SUTask(Context context) {
                this.context=context;
            }
        @Override
        protected Boolean doInBackground(Void... params) {
            // this method is executed in a background thread
            // no problem calling su here

                suResult=Shell.SU.run(new String[]{"busybox chmod -c -R 777 "+Utilities.dbpath2,"busybox chmod -c -R 777 "+Utilities.ppicpath});
                //suResult=Shell.SU.run(new String[]{});
                //suResult=Shell.SU.run(new String[]{"cat storage/sdcard1/s.txt" });

                getContactsList();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            root=aBoolean;
            //setResult(suResult);


           Log.e("Co",Utilities.contacts.get("918675185789@s.whatsapp.net").name+"-"+Utilities.contacts.get("919790892234@s.whatsapp.net").name);
            this.context.startActivity(new Intent(Splash.this,ChatList.class));
        }
    }


    private void getContactsList()
    {
        File file = new File(Utilities.dbpath+Utilities.dbname_contacts);

        if (file.exists()) {

            SQLiteDatabase db = SQLiteDatabase.openDatabase(Utilities.dbpath+Utilities.dbname_contacts, null, SQLiteDatabase.OPEN_READWRITE);

            //SELECT
            Cursor cursor = db.rawQuery("Select jid,display_name,wa_name from wa_contacts order by jid", null);
            if (cursor.moveToFirst()) {

                Utilities.contacts.clear();

                do {
                    String jid=cursor.getString(0);
                    Utilities.contacts.put(jid,new Contact(jid,cursor.getString(1),cursor.getString(2)));


                } while (cursor.moveToNext());
            }
            db.close();

        }

        else{
            Toast.makeText(Splash.this,"NoRoot",Toast.LENGTH_SHORT).show();
        }
    }


    private void setResult(List<String> suResult)
    {
       final TextView tv_root=(TextView)findViewById(R.id.tv_root);
        StringBuilder sb=new StringBuilder();
        sb.append(">");
        if(suResult!=null)
            for(String line:suResult)sb.append(line).append("\n>");
        if(root)
            tv_root.setText("Rooted\n"+sb.toString());
        else
            tv_root.setText("Not Rooted");

        File file = new File(Utilities.dbpath2+Utilities.dbname2);

        if (file.exists()) {
            String jid="919884465829@s.whatsapp.net";
            SQLiteDatabase db = SQLiteDatabase.openDatabase(Utilities.dbpath2+Utilities.dbname2, null, SQLiteDatabase.OPEN_READWRITE);

            //SELECT
            Cursor resultSet = db.rawQuery("Select * from wa_contacts where jid=\""+jid+"\"", null);
            if(resultSet!=null)
            {
                resultSet.moveToFirst();
                tv_root.setText(tv_root.getText() + "\n" + resultSet.getString(5) + "-" + resultSet.getString(0) + "--" + resultSet.getString(7));


                ContentValues values = new ContentValues();
                String where = "jid" + "= '" + jid + "'";
                values.put("display_name", "Gunner");
                values.put("sort_name", "Gunner");
                int count = db.update("wa_contacts", values, where, null);
                Toast.makeText(Splash.this,"xxx"+String.valueOf(count),Toast.LENGTH_SHORT).show();

            }
            /*//UPDATE
            ContentValues values = new ContentValues();
            String where = "jid" + "= '" + jid + "'";
            values.put("status", "BlackoutEpisodes");
            int count =db.update("wa_contacts", values, where, null);
            */




            db.close();

        }

       else{
            Toast.makeText(Splash.this,"hhh",Toast.LENGTH_SHORT).show();
        }



    }

    private void ShowDialog()
    {
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setMessage("Content Modified!");
        ab.setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(this,"Confirmed",Toast.LENGTH_SHORT).show();
            }
        });


        AlertDialog ad=ab.create();
        ad.show();
        Thread p=new Thread(new Runnable() {
            private List<String> suResult = null;
            @Override
            public void run() {
                Log.e("loggg", utext);
                suResult=Shell.SU.run(new String[]{"echo \""+ utext+"\" > "+"storage/sdcard1/s.txt" });

            }

        });
        p.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Utilities.prefs=this.getSharedPreferences("pop",0);
        Utilities.lastread=Utilities.prefs.getString("lastread",String.valueOf(System.currentTimeMillis()));


        SharedPreferences.Editor editor = Utilities.prefs.edit();
        editor.putString("lastread", String.valueOf(System.currentTimeMillis()));
        editor.apply();
        new SUTask(Splash.this).execute();

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = Utilities.prefs.edit();
        editor.putString("lastread",String.valueOf(System.currentTimeMillis()));
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
