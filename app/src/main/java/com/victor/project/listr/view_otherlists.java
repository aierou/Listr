package com.victor.project.listr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.id;

public class view_otherlists extends AppCompatActivity {
    DatabaseReference database1;
    LinearLayout linearscroll;
    RelativeLayout relativeLayout;
    int i;
    String header;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_otherlists);
        Intent intent = getIntent();
        header = intent.getStringExtra("header");
        name = intent.getStringExtra("name");
        database1  = FirebaseDatabase.getInstance().getReference();
        linearscroll = (LinearLayout) findViewById(R.id.linearscroll);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_view_otherlists);
        i=0;

        final TextView headText= (TextView)findViewById(R.id.editText2);
        headText.setText(name);


        ValueEventListener imageListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for(final DataSnapshot s : dataSnapshot.getChildren())
                {
                    final LinearLayout horizontal = new LinearLayout(getApplicationContext());
                    horizontal.setOrientation(LinearLayout.HORIZONTAL);
                    final TextView editText= new TextView(getApplicationContext());
                    editText.setTextColor(0xff000000);
                    editText.setBackgroundColor(0xffffffff);
                    editText.setInputType(96);
                    editText.setText(s.getValue().toString());
                    headText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    editText.setClickable(false);
                    LinearLayout.LayoutParams llp =new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                    llp.setMargins(0,5,0,5);
                    horizontal.setLayoutParams(llp);
                    editText.setLayoutParams(llp);
                    editText.setId(i);
                    horizontal.addView(editText);
                    //horizontal.addView(remove_button);//these add the dynamically created item to the view
                    linearscroll.addView(horizontal);//this adds the layout to the scroll list
                    i++;


                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database1.child("Lists").child(header).child("items").addValueEventListener(imageListener);


    }

}