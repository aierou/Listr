package com.victor.project.listr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewAList extends AppCompatActivity {
String username;
    DatabaseReference database1;
    LinearLayout linearLayout;
    int i;
    String header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alist);
        Intent intent = getIntent();
        header = intent.getStringExtra("header");
        username = intent.getStringExtra("username").concat("~tables");
        database1  = FirebaseDatabase.getInstance().getReference();
        linearLayout = (LinearLayout) findViewById(R.id.view_alist);
        i=0;

        final EditText headText= new EditText(getApplicationContext());
        headText.setTextColor(0xff000000);
        headText.setBackgroundColor(0xffffffff);
        headText.setInputType(96);
        headText.setText(header);
        headText.setTextSize(36);
        headText.setClickable(false);
        headText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        headText.setId(-1);
        linearLayout.addView(headText);

        ValueEventListener imageListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot s : dataSnapshot.getChildren())
                {
                    final EditText editText= new EditText(getApplicationContext());
                    editText.setTextColor(0xff000000);
                    editText.setBackgroundColor(0xffffffff);
                    editText.setInputType(96);
                    editText.setText(s.getValue().toString());
                    editText.setClickable(false);
                    LinearLayout.LayoutParams llp =new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(0,5,0,5);
                    editText.setLayoutParams(llp);
                    editText.setId(i);
                    linearLayout.addView(editText);
                    i++;

                }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database1.child(username).child(header).addValueEventListener(imageListener);


    }
}
