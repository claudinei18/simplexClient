package com.example.simplex.client.clientsimplex;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.kaede.tagview.Tag;
import me.kaede.tagview.TagView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final List<String> listChocolates = new ArrayList<String>();
        final List<Double> listLucroDeCadaChocolate = new ArrayList<Double>();
        final List<Double> listDemandaDeCadaChocolate = new ArrayList<Double>();


        final TagView tvTiposChocolates = (TagView) findViewById(R.id.tvTiposChocolates);

        Button btAddBuque = (Button) findViewById(R.id.bt_addRestricao);
        btAddBuque.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText etTipoChocolate = (EditText) findViewById(R.id.et_TiposChocolate);
                EditText etLucro = (EditText) findViewById(R.id.lucro);
                EditText etDemanda = (EditText) findViewById(R.id.et_DemandaMercado);

                Tag tag = new Tag(etTipoChocolate.getText().toString() +"-" + etLucro.getText().toString() + "-" + etDemanda.getText().toString());
                tag.tagTextColor = Color.parseColor("#FFFFFF");
                tag.layoutColor =  Color.parseColor("#5F2C16");
                tag.layoutColorPress = Color.parseColor("#555555");
                tag.radius = 20f;
                tag.tagTextSize = 20f;
                tag.layoutBorderSize = 1f;
                tag.layoutBorderColor = Color.parseColor("#FFFFFF");
                tag.isDeletable = true;
                tvTiposChocolates.addTag(tag);

                etTipoChocolate.setText("");
            }
        });

        Button bProxFlores = (Button) findViewById(R.id.bt_ProximoFlores);
        bProxFlores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Tag> listTags = tvTiposChocolates.getTags();
                for(Tag tag : listTags) {
                    String[] celula = tag.text.split("-");
                    listChocolates.add(celula[0]);
                    listLucroDeCadaChocolate.add(Double.parseDouble(celula[1]));
                    listDemandaDeCadaChocolate.add(Double.parseDouble(celula[2]));

                }
                Intent intentMain = new Intent(MainActivity.this ,
                        FlorActivity.class);
                intentMain.putExtra("listChocolate", (ArrayList<String>) listChocolates);
                intentMain.putExtra("listLucro", (ArrayList<Double>) listLucroDeCadaChocolate);
                intentMain.putExtra("listDemanda", (ArrayList<Double>) listDemandaDeCadaChocolate);

                MainActivity.this.startActivity(intentMain);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
