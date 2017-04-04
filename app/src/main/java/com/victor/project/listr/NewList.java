package com.victor.project.listr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class NewList extends AppCompatActivity {
    int i=0;
String username;
    Intent intent;
    ArrayList<String> list = new ArrayList<>();
    EditText headtext;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    Button sbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list);
        final DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        username = intent.getStringExtra("username");
        final String users_headers = username.concat("~headers");
        final String users_tables = username.concat("~tables");
        headtext = (EditText) findViewById(R.id.header);
        //linearLayout = (LinearLayout) findViewById(R.id.linearnewlist);
        //relativeLayout = (RelativeLayout) findViewById(R.id.newlistrl);
        sbutton = (Button) findViewById(R.id.savebtn);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            makeitem();

            }
        });*/

        //for(int j =i; j<=6; j++ )
        //{
       //     makeitem();
       // }

      /*
        final Button button= new Button(getApplicationContext());
        button.setText("Save");
        //changed linerar lay yo relative lay
        button.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/
        sbutton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                TrackGPS gps = new TrackGPS(NewList.this);


                if(gps.canGetLocation()){


                    double longitude = gps.getLongitude();
                    double latitude = gps.getLatitude();

                    Toast.makeText(getApplicationContext(),"Longitude:"+Double.toString(longitude)+"\nLatitude:"+Double.toString(latitude),Toast.LENGTH_SHORT).show();
                }
                else
                {

                    gps.showSettingsAlert();
                    return;
                }

                final String header = headtext.getText().toString();



                //Create our new list in the db
                List l = new List(header, Globals.gps.getLatitude(), Globals.gps.getLongitude());
                DatabaseReference ref = mdatabase.child("Lists").push();
                ref.setValue(l);

                for(i-=1;i>=0;i--){
                    EditText edit = (EditText) findViewById(i);

                    String tok = edit.getText().toString();
                    if(!(tok.isEmpty())){
                        mdatabase.child("Lists").child(ref.getKey()).child("items").push().setValue(tok);
                    }
                }

                //Add our list to the user
                mdatabase.child("Users").child(Globals.username).child("lists").push().setValue(ref.getKey());

                //Add user to list
                mdatabase.child("Lists").child(ref.getKey()).child("members").push().setValue(Globals.username);
//
//                mdatabase.child("Lists").push().setValue(l).getKey();
//                mdatabase.child(users_headers).child(header).setValue(l);
//                mdatabase.child(users_tables).child(header).setValue(list);
                Intent back = new Intent(NewList.this, MainListActivity.class);
                back.putExtra("username",intent.getStringExtra("username"));

                finish();

            }
        });


    }
    public void addItem(final View view){
        LinearLayout myLayout = (LinearLayout) findViewById(R.id.scrolllayout);
        final LinearLayout HorizonLayout = new LinearLayout(this);
        HorizonLayout.setOrientation(LinearLayout.HORIZONTAL);
        final EditText newtext = new EditText(this);
        newtext.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
        newtext.setId(i);
        final Button remove_button = new Button(this);
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
        String text = "new item";
        newtext.setText(text);
        HorizonLayout.addView(newtext);
        HorizonLayout.addView(remove_button);
        myLayout.addView(HorizonLayout);
        i++;
    }

    public void makeitem(){

        final EditText editText= new EditText(getApplicationContext());
        editText.setTextColor(0xff000000);
        editText.setBackgroundColor(0xffffffff);
        editText.setInputType(96);
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
