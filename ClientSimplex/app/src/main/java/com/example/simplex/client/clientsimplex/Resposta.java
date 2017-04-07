package com.example.simplex.client.clientsimplex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Resposta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resposta);

        String resposta = getIntent().getStringExtra("resposta");
        LinearLayout mLeft = (LinearLayout) findViewById(R.id.respName);
        LinearLayout mRight = (LinearLayout) findViewById(R.id.respResp);

        String[] aux = resposta.split("\n");
        for(int i = 0; i < aux.length - 1; i++){
            String[]x = aux[i].split("-");
            mLeft.addView(createNewTextView(x[0]));
            DecimalFormat df = new DecimalFormat("#.###");
            String d = df.format(Double.parseDouble(x[1]));
            System.out.println(x[0] + "-" + d);
            mRight.addView(createNewTextView(d));
        }

        mLeft.addView(createNewTextView(aux[aux.length - 1]));

    }

    private TextView createNewTextView(String text) {
        final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        textView.setTextSize(30);
        return textView;
    }
}
