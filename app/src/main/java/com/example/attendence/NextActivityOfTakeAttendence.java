package com.example.attendence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NextActivityOfTakeAttendence extends AppCompatActivity {

    FirebaseFirestore db;
    Button save_btn;
    TextView tv_ids;
    String[] present_status = new String[ArrayAdapterForTakeAttendence.model_list.size()];
    String[] absent_status = new String[ArrayAdapterForTakeAttendence.model_list.size()];
    String my_section,dates,teach_id;
    Map<Object,Object> list = new HashMap<>();
    Intent in;
    public int a;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_of_take_attendence);

        tv_ids = (TextView) findViewById(R.id.tv);
        save_btn = findViewById(R.id.final_save_btn);
        pb = findViewById(R.id.nxt_pgb);

        db = FirebaseFirestore.getInstance();

        for (int i = 0; i < ArrayAdapterForTakeAttendence.model_list.size(); i++){
            if(ArrayAdapterForTakeAttendence.model_list.get(i).getSelected())
            {
                present_status[i] =  tv_ids.getText() + " " + ArrayAdapterForTakeAttendence.model_list.get(i).getId_no()+ " PRESENT "+ "\n";
                tv_ids.setText(present_status[i]);
            }
            if(!(ArrayAdapterForTakeAttendence.model_list.get(i).getSelected()))
            {
                absent_status[i] = tv_ids.getText() + " " + ArrayAdapterForTakeAttendence.model_list.get(i).getId_no()+ " ABSENT "+ "\n";
                tv_ids.setText(absent_status[i]);
            }

        }

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String dd = String.valueOf(today.monthDay);
        String mm = String.valueOf(today.month + 1);
        String yy = String.valueOf(today.year);

        dates = dd+"-"+mm+"-"+yy;

        in = new Intent(this,TChoiceActivity.class);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                Intent t = getIntent();
                teach_id = t.getStringExtra("take_ID");
                my_section = t.getStringExtra("sec");

                in.putExtra("my_id",teach_id);

                String st[] = tv_ids.getText().toString().split("\n");
                a = st.length;
                    for (int i = 0; i < a; i++) {
                        list.put(""+i+"", st[i]);
                    }
                db.collection("Attendence").document(teach_id).collection(my_section).document(dates)
                        .set(list).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        pb.setVisibility(View.GONE);
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
