package com.bananacoding.expressologin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = (EditText) findViewById(R.id.tv_email);
        mPasswordEditText = (EditText) findViewById(R.id.tv_password);
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_login);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        // Reset errors.
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError(getString(R.string.error_field_required));
            focusView = mPasswordEditText;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEditText;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError(getString(R.string.error_field_required));
            focusView = mEmailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailEditText.setError(getString(R.string.error_invalid_email));
            focusView = mEmailEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (email.equals("user@email.com") && password.equals("123456")) {
                loginSuccessfully(email);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.error_login_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void loginSuccessfully(String email) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
    }
}

