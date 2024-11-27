package com.app.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.todolist.R;
import com.app.todolist.dao.UserDao;
import com.app.todolist.model.User;

public class SignupActivity extends AppCompatActivity {

    private EditText signupEmail, signupName, signupPassword;
    private TextView signupForgotPassword;
    private Button bntSignUp, btnSignInWithGoogle;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signupName = findViewById(R.id.signup_person);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_Password);
        signupForgotPassword = findViewById(R.id.signup_ForgotPassword);
        bntSignUp = findViewById(R.id.signup_btnSignIn);
        btnSignInWithGoogle = findViewById(R.id.signup_btnSignInWithGoogle);

        userDao = new UserDao(this);

        signupForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, ForgotPasswordActivity.class));
            }
        });

        bntSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String password = signupPassword.getText().toString();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(name, email, password);
                    int result = userDao.Register(user);
                    if (result == 1) {
                        Toast.makeText(SignupActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (result == -1) {
                        Toast.makeText(SignupActivity.this, "Email đã tồn tại, vui lòng sử dụng email khác", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignupActivity.this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}