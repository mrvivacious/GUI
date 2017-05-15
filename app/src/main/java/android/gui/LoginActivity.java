package android.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "LoginActivity";
    private FirebaseAuth auth;
    private Button register;
    private Button login;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        register = (Button) findViewById(R.id.b_register);
        login = (Button) findViewById(R.id.b_login);

        super.onStart();

        //Register user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.et_username);
                password = (EditText) findViewById(R.id.et_password);

                final String rawUsername = username.getText().toString();
                final String rawPassword = password.getText().toString();

                final int minimumPassLength = 8;

                //Register a user
                auth.createUserWithEmailAndPassword(rawUsername, rawPassword).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Intent register = new Intent(LoginActivity.this, IllActivity.class);
                                    Toast.makeText(LoginActivity.this, "Registered successfully.",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(register);
                                }
                                //If the username isn't an email
                                else if (!rawUsername.contains("@")) {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Error: Badly formatted email address,  or " +
                                                    "email already in use.",
                                            Toast.LENGTH_LONG).show();
                                }
                                //If the password isn't a valid length
                                else if (rawPassword.length() < minimumPassLength) {
                                    Toast.makeText(LoginActivity.this, "Password must be at least 8 characters " +
                                            "in length.", Toast.LENGTH_LONG).show();
                                }
                            }

                        });
            }
        });


        //Login registered user
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText) findViewById(R.id.et_username);
                password = (EditText) findViewById(R.id.et_password);

                String rawUsername = username.getText().toString();
                String rawPassword = password.getText().toString();

                auth.signInWithEmailAndPassword(rawUsername, rawPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                if (task.isSuccessful()) {
                                    Intent login = new Intent(LoginActivity.this, IllActivity.class);
                                    Toast.makeText(LoginActivity.this, "Login succeeded.",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(login);
                                }

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(LoginActivity.this, "Login failed, try again later.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });
    }
}
