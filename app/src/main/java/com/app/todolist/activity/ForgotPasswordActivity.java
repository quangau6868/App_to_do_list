package com.app.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.todolist.R;
import com.app.todolist.dao.UserDao;
import com.app.todolist.model.User;

public class ForgotPasswordActivity extends AppCompatActivity {

    ImageButton imgbtn;
    EditText forgotEmail;
    Button btnConfirmPass;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgbtn = findViewById(R.id.forgot_myImageButton);
        forgotEmail = findViewById(R.id.forgot_Email);
        btnConfirmPass = findViewById(R.id.forgot_btnConfirm);

        userDao = new UserDao(this);


        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirmPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = forgotEmail.getText().toString();
                if (email.isEmpty() ) {
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
                } else {
                    User user = new User(email);
                    int result = userDao.checkEmail(user);
                    if (result == -1) {
                        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("username", email);
                        startActivity(intent);
                    }else {
                        Toast.makeText(ForgotPasswordActivity.this, "Email không tồn tại, vui lòng sử dụng đăng kí email khác", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}