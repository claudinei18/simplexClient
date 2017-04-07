package com.example.simplex.client.clientsimplex;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestricoesCadaChocolate extends AppCompatActivity {
    List<EditText> allEds = new ArrayList<EditText>();
    ArrayList<String> listChocolates = null;
    ArrayList<Double> listLucro = null;
    ArrayList<Double> listDemanda = null;
    ArrayList<String> listMateriaPrima = null;
    ArrayList<Double> listEstoque = null;
    JSONArray jsonArray = new JSONArray();
    String resposta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restricoes_cada_chocolate);

        listChocolates = getIntent().getStringArrayListExtra("listChocolate");
        listLucro = (ArrayList<Double>) getIntent().getSerializableExtra("listLucro");
        listDemanda = (ArrayList<Double>) getIntent().getSerializableExtra("listDemanda");
        listMateriaPrima = getIntent().getStringArrayListExtra("listMateriaPrima");
        listEstoque = (ArrayList<Double>) getIntent().getSerializableExtra("listEstoque");

        LinearLayout mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        for(String elemento: listChocolates){
            mLayout.addView(createNewTextView("Digite abaixo o quando de materia prima " + elemento + " precisa: "));
            for(String elemento2: listMateriaPrima){
                mLayout.addView(createNewTextView(elemento2));
                mLayout.addView(createNewEditText(elemento2));
            }
        }

        Button btRunSimplex = (Button) findViewById(R.id.runSimplex);
        btRunSimplex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cont = 0;
                for(int i = 0; i < listChocolates.size(); i++){
                    System.out.println(listChocolates.get(i));
                    for(int j = 0; j < listMateriaPrima.size(); j++, cont++){
                        System.out.println(allEds.get(cont).getText());
                    }
                }
                montarJson();
                makeRequest();
            }
        });

    }

    public void makeRequest(){
        String url = "https://simplexserver.herokuapp.com/simplex";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        resposta = response;
                        Intent intentMain = new Intent(RestricoesCadaChocolate.this ,
                                Resposta.class);
                        intentMain.putExtra("resposta", resposta);

                        RestricoesCadaChocolate.this.startActivity(intentMain);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("json", jsonArray.toString());
                System.out.println(jsonArray.toString());
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public void montarJson(){
        jsonArray = new JSONArray();

        //F.O.
        JSONObject o = new JSONObject();
        try {
            o.put("f.o.", "max");
            o.put("ml", "0");
            JSONArray res = new JSONArray();
            for(int j = 0; j < listChocolates.size(); j++){
                String nomeChocolate = listChocolates.get(j);
                String x = listLucro.get(j).toString();
                JSONObject oo = new JSONObject();
                oo.put(nomeChocolate, x);
                res.put(oo);
            }
            o.put("res", res);
            o.put("ope", "1");
            jsonArray.put(o);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Materia Prima
        for(int i = 0; i < listMateriaPrima.size(); i++){
            o = new JSONObject();
            try {
                o.put("nome", listMateriaPrima.get(i));
                o.put("ml", listEstoque.get(i));
                JSONArray res = new JSONArray();
                int posNoAllEd = i;
                for(int j = 0; j < listChocolates.size(); j++){
                    String nomeChocolate = listChocolates.get(j);
                    String x = allEds.get(posNoAllEd).getText().toString();
                    JSONObject oo = new JSONObject();
                    oo.put(nomeChocolate, x);
                    res.put(oo);
                    posNoAllEd += allEds.size() / listChocolates.size();
                }
                o.put("res", res);
                o.put("ope", "4");
                jsonArray.put(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Demanda
        for(int i = 0; i < listDemanda.size(); i++){
            o = new JSONObject();
            try {
                o.put("nome", "demandaDe" + listChocolates.get(i));
                o.put("ml", listDemanda.get(i));
                JSONArray res = new JSONArray();
                for(int j = 0; j < listChocolates.size(); j++){
                    String nomeChocolate = listChocolates.get(j);
                    if(i == j){
                        JSONObject oo = new JSONObject();
                        oo.put(nomeChocolate, "1");
                        res.put(oo);
                    }else{
                        JSONObject oo = new JSONObject();
                        oo.put(nomeChocolate, "0");
                        res.put(oo);
                    }
                }
                o.put("res", res);
                o.put("ope", "4");
                jsonArray.put(o);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        System.out.println(jsonArray);
    }

    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        return textView;
    }

    private EditText createNewEditText(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final EditText editText = new EditText(this);
        editText.setLayoutParams(lparams);
        editText.setHint(text);
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        allEds.add(editText);
        return editText;
    }
}
