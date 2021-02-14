package com.example.profil.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.profil.JSONParser;
import com.example.profil.Profil;
import com.example.profil.R;
import com.example.profil.ui.gallery.GalleryFragment;
import com.example.profil.ui.slideshow.SlideshowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Button btntelecharger;
    ListView lv;
    ArrayList<Profil> data=new ArrayList<Profil>();
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        lv=root.findViewById(R.id.lv_home);
        //FloatingActionButton fab = root.findViewById(R.id.fab);

        btntelecharger=root.findViewById(R.id.btntelecharger_home);
        btntelecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lancer un thread de telechargement
                /**
                 * extends Thread +implements Runnuble ==>Handler
                 * 2eme methode : Asynctask
                 */
                Telechargement t=new Telechargement(HomeFragment.this.getActivity());
                t.execute(0);
            }
        });

        return root;
    }
    class Telechargement extends AsyncTask
    {
        Context con;
        AlertDialog alert;
        String msg;

        public Telechargement(Context con) {
            this.con = con;

        }

        @Override
        protected void onPreExecute()//(Integer ... entries)
        {
            super.onPreExecute();
            //UIT
            AlertDialog.Builder dialog=new AlertDialog.Builder(con);
            dialog.setTitle("Telechargement");
            dialog.setMessage("veuillez patientez");
            alert=dialog.create();
            alert.show();

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            //==>Run /2eme processus
            try{
                Thread.sleep(1000);           }
                catch(InterruptedException e){
                e.printStackTrace();
            }
            /*btntelecharger.setText("50% terminé.."); //UIT
            //==>generern interrupted Exception*/
            publishProgress(1);
            String ip="192.168.1.18";
            //ipv4
            //nom de votre site en cas d'ebergement
            //10.0.2.2 : AVD
            String url="http://"+ip+"/servicephp/get_all_user.php";
            JSONObject response= JSONParser.makeRequest(url);
            try {
                int s=response.getInt("success");
                if(s==0){
                    msg=response.getString("message");
                }else{
                    JSONArray tab=response.getJSONArray("profil");
                    for(int i=0;i<tab.length();i++){
                        JSONObject ligne=tab.getJSONObject(i);
                        String n=ligne.getString("nom");
                        String p=ligne.getString("prenom");
                        String ps=ligne.getString("pseudo");
                        data.add(new Profil(n,p,ps));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*try{
                Thread.sleep(1000);           }
            catch(InterruptedException e){
                e.printStackTrace();
            }*/
            publishProgress(2);
            /*btntelecharger.setBackgroundColor(Color.RED);
            //==>erreur car accès btn telecharger*/
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            //UIT
            if(values[0]==(Object)1){
                btntelecharger.setText("50% terminé..");
            }
            if (values[0]==(Object)2){
                btntelecharger.setBackgroundColor(Color.GREEN);
                btntelecharger.setText("All Done");
            }
            if (values[0]==(Object)3){
                AlertDialog.Builder dialog= new AlertDialog.Builder(con);
                dialog.setMessage(msg);
                alert=dialog.create();
                alert.show();
            }

            /*else{
                btntelecharger.setBackgroundColor(Color.rgb(0,150,0));
            }*/
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //UIT
            alert.dismiss();
            //recycle adapter 2:26
            ArrayAdapter ad=new ArrayAdapter(con,
                    android.R.layout.simple_list_item_1,
                    data);
            lv.setAdapter(ad);
        }
    }
}