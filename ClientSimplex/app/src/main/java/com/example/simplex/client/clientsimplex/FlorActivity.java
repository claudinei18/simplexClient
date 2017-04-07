package com.example.simplex.client.clientsimplex;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

public class FlorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flor);


        final ArrayList<String> listChocolates = getIntent().getStringArrayListExtra("listChocolate");
        final ArrayList<Double> listLucro = (ArrayList<Double>) getIntent().getSerializableExtra("listLucro");
        final ArrayList<Double> listDemanda = (ArrayList<Double>) getIntent().getSerializableExtra("listDemanda");

        final TagView tvTiposFlores = (TagView) findViewById(R.id.tvMateriasPrimas);
        final List<String> listMateriaPrima = new ArrayList<String>();
        final List<Double> listEstoque = new ArrayList<Double>();

        Button bProxFO = (Button) findViewById(R.id.bt_ProxRestricoes);
        bProxFO.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Tag> listTags = tvTiposFlores.getTags();
                for(Tag tag : listTags) {
                    listMateriaPrima.add(tag.text);
                }


                Intent intentMain = new Intent(FlorActivity.this ,
                        RestricoesCadaChocolate.class);
                intentMain.putExtra("listChocolate", (ArrayList<String>) listChocolates);
                intentMain.putExtra("listLucro", (ArrayList<Double>) listLucro);
                intentMain.putExtra("listDemanda", (ArrayList<Double>) listDemanda);
                intentMain.putExtra("listMateriaPrima", (ArrayList<String>) listMateriaPrima);
                intentMain.putExtra("listEstoque", (ArrayList<Double>) listEstoque);

                FlorActivity.this.startActivity(intentMain);
            }
        });


        Button btAddMateiraPrima = (Button) findViewById(R.id.bt_addRestricao);
        btAddMateiraPrima.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText tvMateriaPria = (EditText) findViewById(R.id.et_MateriaPrima);
                EditText tvEstoque = (EditText) findViewById(R.id.etEstoque);

                Tag tag = new Tag(tvMateriaPria.getText().toString());
                tag.tagTextColor = Color.parseColor("#FFFFFF");
                tag.layoutColor =  Color.parseColor("#5F2C16");
                tag.layoutColorPress = Color.parseColor("#555555");
                tag.radius = 20f;
                tag.tagTextSize = 20f;
                tag.layoutBorderSize = 1f;
                tag.layoutBorderColor = Color.parseColor("#FFFFFF");
                tag.isDeletable = true;
                tvTiposFlores.addTag(tag);

                listEstoque.add(Double.parseDouble(tvEstoque.getText().toString()));

                tvMateriaPria.setText("");

            }
        });
    }
}
