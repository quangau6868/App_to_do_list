package com.app.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.todolist.MainActivity;
import com.app.todolist.R;
import com.app.todolist.dao.UserDao;
import com.app.todolist.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPasaword;
    private TextView loginForgotPassword, loginSignUpLink;
    private Button btnSign, btnSignGG;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginEmail = findViewById(R.id.login_Email);
        loginPasaword = findViewById(R.id.loginPassword);
        loginForgotPassword = findViewById(R.id.loginForgotPassword);
        btnSign = findViewById(R.id.login_btnSignIn);
        btnSignGG = findViewById(R.id.login_btnSignInWithGoogle);
        loginSignUpLink = findViewById(R.id.login_SignUpLink);

        userDao = new UserDao(this);

        ImageView imageViewShow = findViewById(R.id.imageView_show_hide);
        imageViewShow.setImageResource(R.drawable.baseline_remove_red_eye_24);
        imageViewShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginPasaword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    loginPasaword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imageViewShow.setImageResource(R.drawable.baseline_remove_red_eye_24);
                } else {
                    loginPasaword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    imageViewShow.setImageResource(R.drawable.baseline_visibility_off_24);
                }
                loginPasaword.setSelection(loginPasaword.getText().length());
            }
        });

        loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnSignGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });

        loginSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPasaword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(LoginActivity.this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(email, password);
                    boolean check = userDao.CheckLogin(user);
                    if (check) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}