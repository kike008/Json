package com.example.kike.json;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    //URL donde se obtendran los datos de JSON
    private static String url = "http://eulisesrdz.260mb.net/kike/aacc.json";

    //Nombre de los nodos

    private final String KEY_VACUNA = "vacunacion";
    private final String KEY_ID_A = "id_A";
    private final String KEY_COMUNIDAD = "comunidad";
    private final String KEY_MVZ = "mvz";
    private final String KEY_TELEFONO = "telefono";
    private final String KEY_PERROS = "perros";
    private final String KEY_GATOS = "gatos";

    //Se crea un arreglo
    JSONArray vacunacion = null;
    ArrayList<HashMap<String, String>> vacunacionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vacunacionList = new ArrayList<HashMap<String, String>>();
        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // obtener los valores del que se seleccionan del ListItem
                String id_a = ((TextView) view.findViewById(R.id.id_a)).getText().toString();
                String comunidad = ((TextView) view.findViewById(R.id.comunidad)).getText().toString();
                String mvz = ((TextView) view.findViewById(R.id.mvz)).getText().toString();
                String telefono = ((TextView) view.findViewById(R.id.telefono)).getText().toString();
                String perros = ((TextView) view.findViewById(R.id.perros)).getText().toString();
                String gatos = ((TextView) view.findViewById(R.id.gatos)).getText().toString();

                // se inicia la nueva actividad
                Intent in = new Intent(getApplicationContext(),vista_vacuna.class);
                //se declaran los datos
                in.putExtra(KEY_ID_A, id_a);
                in.putExtra(KEY_COMUNIDAD, comunidad);
                in.putExtra(KEY_MVZ, mvz);
                in.putExtra(KEY_TELEFONO, telefono);
                in.putExtra(KEY_PERROS, perros);
                in.putExtra(KEY_GATOS, gatos);

                //se inicia la actividad
                startActivity(in);
                }

            });
        //se invoca el metodo
        new GetVacunas().execute();
        }


private class GetVacunas extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // Creating service handler class instance
        ServiceHandler sh = new ServiceHandler();
        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
        Log.d("Response: ", "> " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // se obtiene los nodos del arreglo JSON
                vacunacion = jsonObj.getJSONArray(KEY_VACUNA);

                // looping through All Contacts
                for (int i = 0; i < vacunacion.length(); i++) {
                    JSONObject c = vacunacion.getJSONObject(i);
                    String Id_a = c.getString(KEY_ID_A);
                    String comunidad = c.getString(KEY_COMUNIDAD);
                    String mvz = c.getString(KEY_MVZ);
                    String telefono = c.getString(KEY_TELEFONO);
                    String perros = c.getString(KEY_PERROS);
                    String gatos = c.getString(KEY_GATOS);

                    // tmp hashmap for single contact
                    HashMap<String, String> contact = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    contact.put(KEY_ID_A, Id_a);
                    contact.put(KEY_COMUNIDAD, comunidad);
                    contact.put(KEY_MVZ, mvz);
                    contact.put(KEY_TELEFONO, telefono);
                    contact.put(KEY_PERROS, perros);
                    contact.put(KEY_GATOS, gatos);
                    // se añade nuevos contactos a la lista alumnos
                    vacunacionList.add(contact);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, vacunacionList,
                R.layout.lista_vacuna, new String[] {KEY_ID_A, KEY_COMUNIDAD, KEY_MVZ, KEY_TELEFONO, KEY_PERROS, KEY_GATOS},
                new int[] { R.id.id_a,R.id.comunidad,R.id.mvz,R.id.telefono, R.id.perros, R.id.gatos });
        setListAdapter(adapter);
    }
}
}




