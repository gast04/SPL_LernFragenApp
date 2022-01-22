package com.niku.splapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    JSONObject json = null;
    Iterator<String> questions_it = null;
    List<String> question_numbers = null;

    TextView question_view;
    TextView a1_view;
    TextView a2_view;
    TextView a3_view;
    TextView a4_view;
    TextView correct_view;
    TextView[] answer_views = null;
    int correct_answer = 0;
    boolean next_question = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        question_view = (TextView)findViewById(R.id.txt_question);
        a1_view = (TextView)findViewById(R.id.txt_a1);
        a2_view = (TextView)findViewById(R.id.txt_a2);
        a3_view = (TextView)findViewById(R.id.txt_a3);
        a4_view = (TextView)findViewById(R.id.txt_a4);
        answer_views = new TextView[]{a1_view, a2_view, a3_view, a4_view};
        correct_view = (TextView)findViewById(R.id.txt_correct);

        Context ctx = this.getApplicationContext();
        json = loadJSONFromAsset(ctx);
        if (json == null)
            return;

        question_numbers = new ArrayList<>();
        for(Iterator<String> questions_it = json.keys(); questions_it.hasNext(); ) {
            String q_num = questions_it.next();
            question_numbers.add(q_num);
        }

        showRandomQuestion();
    }

    public void showRandomQuestion() {
        reset_colors();

        try {
            int q_id = new Random().nextInt(question_numbers.size());
            String q_num = question_numbers.get(q_id);
            String question = json.getJSONObject(q_num).getString("question");
            correct_answer = json.getJSONObject(q_num).getInt("correct");
            question_view.setText("("+ q_num + ") "+ question);

            JSONObject answers = json.getJSONObject(q_num).getJSONObject("answers");
            for(int i = 0; i < 4; i++) {
                String str_i = Integer.toString(i);
                answer_views[i].setText(str_i + ": " + answers.getString(str_i));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject loadJSONFromAsset(Context context) {
        JSONObject json_string = null;
        try {
            AssetManager asm = context.getAssets();
            InputStream is = asm.open("spl_questions.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            json_string = new JSONObject(json);

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            Log.v("NIKU", ex.getMessage());
            return null;
        }
        return json_string;
    }

    public void reset_colors() {
        for(int i = 0; i < 4; i++) {
            answer_views[i].setBackgroundColor(Color.WHITE);
            correct_view.setText("");
        }
    }

    public void validate_answer(int pressed) {
        for(int i = 0; i < 4; i++) {
            answer_views[i].setBackgroundColor(Color.RED);
        }
        answer_views[correct_answer].setBackgroundColor(Color.GREEN);

        if(pressed == correct_answer){
            correct_view.setText("RICHTIG");
        }
        else {
            correct_view.setText("FALSCH");
        }
    }

    public void btn_answer_1(View view) {
        if (next_question) {
            showRandomQuestion();
            next_question = false;
        }
        else{
            validate_answer(0);
            next_question = true;
        }
    }

    public void btn_answer_2(View view) {
        if (next_question) {
            showRandomQuestion();
            next_question = false;
        }
        else{
            validate_answer(1);
            next_question = true;
        }
    }

    public void btn_answer_3(View view) {
        if (next_question) {
            showRandomQuestion();
            next_question = false;
        }
        else{
            validate_answer(2);
            next_question = true;
        }
    }

    public void btn_answer_4(View view) {
        if (next_question) {
            showRandomQuestion();
            next_question = false;
        }
        else{
            validate_answer(3);
            next_question = true;
        }
    }
}
