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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

public class ViewAList extends AppCompatActivity {
    DatabaseReference database1;
    LinearLayout linearscroll;
    RelativeLayout relativeLayout;
    int i;
    String header;
    String name;
    HashMap<String, EditText> keys = new HashMap<>();

    @Override
    public void onBackPressed() {
        exitToMain();
    }

    private void exitToMain(){
        Intent toMain = new Intent(ViewAList.this, MainListActivity.class);
        startActivity(toMain);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alist);
        Intent intent = getIntent();
        header = intent.getStringExtra("header");
        name = intent.getStringExtra("name");
        database1  = FirebaseDatabase.getInstance().getReference();
        linearscroll = (LinearLayout) findViewById(R.id.linearscroll);
        relativeLayout = (RelativeLayout) findViewById(R.id.view_alist);
        i=0;

        final EditText headText= new EditText(getApplicationContext());
        headText.setTextColor(0xff000000);
        headText.setBackgroundColor(0xffffffff);
        headText.setInputType(96);
        headText.setText(name);
        headText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25); //Magic numbers woo
        headText.setClickable(false);
        headText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        headText.setId(-1);
        relativeLayout.addView(headText);
        Button sbutton = (Button) findViewById(R.id.view_save);
        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //database1.child("Lists").child(header);
                String newheader = headText.getText().toString();
                if(newheader.equals(header)) {
                    newheader = newheader + "(New)";
                }
                database1.child("Lists").child(header).child("name").setValue(newheader);
                name = newheader;

                //Change modified values
                for(Map.Entry<String, EditText> entry : keys.entrySet()){
                    database1.child("Lists")
                            .child(header)
                            .child("items")
                            .child(entry.getKey()).setValue(entry.getValue().getText().toString());
                }
                exitToMain();
            }

        });
        Button prefs = (Button) findViewById(R.id.btn_prefs);
        prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewAList.this, PrefMainActivity.class);
                i.putExtra("id", header);
                i.putExtra("name", name);
                ViewAList.this.startActivity(i);
            }
        });
        Button add_btn = (Button) findViewById(R.id.view_add);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout myLayout = (LinearLayout) findViewById(R.id.linearscroll);
                final LinearLayout HorizonLayout = new LinearLayout(getApplicationContext());
                HorizonLayout.setOrientation(LinearLayout.HORIZONTAL);
                final EditText newtext = new EditText(getApplicationContext());
                newtext.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
                newtext.setTextColor(0xff000000);
                newtext.setBackgroundColor(0xffffffff);
                newtext.setInputType(96);
                newtext.setClickable(false);
                final String text = "new item";
                newtext.setId(i);
                final Button remove_button = new Button(getApplicationContext());
                remove_button.setText("X");
                remove_button.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
                remove_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HorizonLayout.removeView(newtext);
                        HorizonLayout.removeView(remove_button);
                    }
                });
                //String text = "new item";
                newtext.setText(text);
                HorizonLayout.addView(newtext);
                HorizonLayout.addView(remove_button);
                myLayout.addView(HorizonLayout);
                DatabaseReference ref = database1.child("Lists").child(header).child("items").push();
                ref.setValue(text);
                linearscroll.removeAllViews();
                i++;
            }
        });

        ValueEventListener imageListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                keys = new HashMap<>();
                i = 0;
                for(final DataSnapshot s : dataSnapshot.getChildren())
                {
                    final LinearLayout horizontal = new LinearLayout(getApplicationContext());
                    horizontal.setOrientation(LinearLayout.HORIZONTAL);
                    final EditText editText= new EditText(getApplicationContext());
                    editText.setTextColor(0xff000000);
                    editText.setBackgroundColor(0xffffffff);
                    editText.setInputType(96);
                    editText.setText(s.getValue().toString());
                    editText.setClickable(false);
                    LinearLayout.LayoutParams llp =new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                    llp.setMargins(0,5,0,5);
                    horizontal.setLayoutParams(llp);
                    editText.setLayoutParams(llp);
                    editText.setId(i);
                    final Button remove_button = new Button(getApplicationContext());
                    remove_button.setText("X");
                    remove_button.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
                    remove_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            database1.child("Lists").child(header).child("items").child(s.getKey().toString()).removeValue();//removes child on database
                            linearscroll.removeAllViews();//This removes all views so they can be reloaded via thefor loop
                        }
                    });
                    horizontal.addView(editText);
                    keys.put(s.getKey(), editText);
                    horizontal.addView(remove_button);//these add the dynamically created item to the view
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