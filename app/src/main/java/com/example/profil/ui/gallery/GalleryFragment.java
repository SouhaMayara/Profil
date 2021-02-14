package com.example.profil.ui.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.profil.JSONParser;
import com.example.profil.MainActivity;
import com.example.profil.Profil;
import com.example.profil.R;
import com.example.profil.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFragment extends Fragment {
    private GalleryViewModel galleryViewModel;
    private Button btninit,btnval;
    private EditText ednom,edprenom,edpseudo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        /**
         * action valider
         */
        btnval = root.findViewById(R.id.btnval_ajout);
        btninit= root.findViewById(R.id.btninit_ajout);

        ednom = root.findViewById(R.id.ednom_ajout);
        edprenom = root.findViewById(R.id.edprenom_ajout);
        edpseudo = root.findViewById(R.id.edpseudo_ajout);

        String ip="192.168.1.18";
       // String url="http://"+ip+"/servicephp/"+"ajout.php?nom="+nom+"&prenom="+prenom+"&pseudo="+pseudo;
        btninit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ednom.setText("");
                edprenom.setText("");
                edpseudo.setText("");
            }
        });
        btnval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Upload u= new Upload(GalleryFragment.this.getActivity());
                u.execute();

                /*String n = ednom.getText().toString();
                String pn = edprenom.getText().toString();
                String ps = edpseudo.getText().toString();

                String url="http://"+ip+"/servicephp/add_user.php?nom="+n+"&prenom="+pn+"&pseudo="+ps;
                JSONObject response= JSONParser.makeRequest(url);
                try {
                    int s=response.getInt("success");
                    if(s==0){
                        String msg=response.getString("message");
                        System.out.println(msg);
                    }else{
                        String msg=response.getString("message");
                        System.out.println(msg);
                        btnval.setText("profil added!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            }
        });
        return root;
    }

    class Upload extends AsyncTask {
        Context con;
        AlertDialog alert;
        public Upload(Context con) {
            this.con = con;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //UI thread
            AlertDialog.Builder dialog= new AlertDialog.Builder(con);
            dialog.setTitle("Adding..");
            dialog.setMessage("Please Wait...");
            alert=dialog.create();
            alert.show();
        }

        int s;

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String ip="192.168.1.18";
            String nom = ednom.getText().toString();
            String prenom = edprenom.getText().toString();
            String pseudo = edpseudo.getText().toString();

            String url= "http://"+ip+"/servicephp/add_user.php?nom="+nom+"&prenom="+prenom+"&pseudo="+pseudo;
                    //"http://"+ip+
                    //"/androidApp/add_user.php?nom="+nom+
                    //"&prenom="+prenom+
                    //"&pseudo="+pseudo;

            JSONObject response= JSONParser.makeRequest(url);
            //traitement d'erreur

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            //UI thread
            alert.dismiss();

            ednom.setText("");
            edprenom.setText("");
            edpseudo.setText("");
        }
    }

}