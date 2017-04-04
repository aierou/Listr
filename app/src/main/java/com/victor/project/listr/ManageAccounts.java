package com.victor.project.listr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import static com.victor.project.listr.Globals.username;

public class ManageAccounts extends AppCompatActivity {

    Button confirmButton;
    TextView userField;
    EditText passField1;
    EditText passField2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);
        confirmButton = (Button) findViewById(R.id.confirmAccountChangesButton);
        userField = (TextView) findViewById(R.id.usernameEditText);
        userField.setText(Globals.username);
        passField1 = (EditText) findViewById(R.id.enterPassEditText);
        passField2 = (EditText) findViewById(R.id.confirmPassEditText);
    }

    public void confirmButtonHandler(View v) {
        Toast toast;

        if(!passField1.getText().toString().equals("") && !passField2.getText().toString().equals("")
                && passField1.getText().toString().equals(passField2.getText().toString()))
        {
            Globals.database.child("Users").child(Globals.username).child("password").setValue(passField1.getText().toString());
            finish();
        }else{
            passField1.setText("");
            passField2.setText("");
            Toast.makeText(getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
        }
    }
}
