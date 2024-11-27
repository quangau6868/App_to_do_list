package com.app.todolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.app.todolist.database.DBHelper;
import com.app.todolist.model.User;

public class ResetPasswordActivity extends AppCompatActivity {

    ImageButton btnImangeReset;
    EditText EditTextPassword, EditTextConfirmPassword;
    Button btnResetPass;
    TextView txtusename;

    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtusename = findViewById(R.id.Reset_3);
        btnImangeReset = findViewById(R.id.Reset_myImageButton);
        EditTextPassword = findViewById(R.id.Reset_new_password);
        EditTextConfirmPassword = findViewById(R.id.Reset_confirm_password);
        btnResetPass = findViewById(R.id.Reset_btnConfirm);

        userDao = new UserDao(this);

        btnImangeReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        txtusename.setText(intent.getStringExtra("username"));

        btnResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = txtusename.getText().toString().trim();
                String newpass1 = EditTextPassword.getText().toString().trim();
                String newpass2 = EditTextConfirmPassword.getText().toString().trim();

                if (userEmail.isEmpty() || newpass1.isEmpty() || newpass2.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!newpass1.equals(newpass2)) {
                    Toast.makeText(ResetPasswordActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    if (newpass2.compareTo(newpass1) == 0) {
                        User user = new User(userEmail,newpass1);
                        boolean check = userDao.updatePassword(user);
                        if (check) {
                            startActivity(new Intent(ResetPasswordActivity.this, SuccesfullyRegisteredActivity.class));
                            Toast.makeText(ResetPasswordActivity.this, "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Đặt lại mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
}