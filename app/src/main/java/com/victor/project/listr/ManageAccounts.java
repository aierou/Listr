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

public class ManageAccounts extends AppCompatActivity {

    Button confirmButton;
    EditText userField;
    EditText passField1;
    EditText passField2;
    User user = firebase.auth().currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);

        confirmButton = (Button) findViewById(R.id.confirmAccountChangesButton);
        userField = (EditText) findViewById(R.id.usernameEditText);
        passField1 = (EditText) findViewById(R.id.enterPassEditText);
        passField2 = (EditText) findViewById(R.id.confirmPassEditText);
    }

    public void confirmButtonHandler(View v) {
        Toast toast;

        if(((passField1.getText().toString() != null )|| (passField2.getText().toString()) != null))
        {
            if((passField1.getText().toString().equals(passField2.getText().toString()))) {
                user.updatePassword(newPassword).then(function()
                {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.passResetTrue), Toast.LENGTH_SHORT);
                    toast.show();
                },function(error) {
                    toast = Toast.makeText(getApplicationContext(), getString(R.string.passResetFalse), Toast.LENGTH_SHORT);
                    toast.show();
                });

            }
        }

        if(userField.getText().toString() != null)
        {
            user.updateProfile(
                {
                    displayName: "Jane Q. User",
                    photoURL: "https://example.com/jane-q-user/profile.jpg"
                }).then(function()
                    {
                        toast = Toast.makeText(getApplicationContext(), getString(R.string.userResetTrue), Toast.LENGTH_SHORT);
                        toast.show();
                    },
                function(error)
                    {
                        toast = Toast.makeText(getApplicationContext(), getString(R.string.userResetFalse), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                );

        }
    }
}
