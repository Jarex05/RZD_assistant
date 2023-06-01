package com.example.rzdassistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rzdassistant.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityLogirovanie extends AppCompatActivity {

    Button btnSignIn, btnRegister, bStart, bLogaut;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    private TextView tvUserEmail;
    private TextView forgotPassword;

    RelativeLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logirovanie);

        tvUserEmail = findViewById(R.id.tvUserEmail);
        bStart = findViewById(R.id.bStart);
        bLogaut = findViewById(R.id.bLogaut);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);

        forgotPassword = findViewById(R.id.forgot_password);

        RelativeLayout root = (RelativeLayout) findViewById(R.id.root_element);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterWindow();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInWindow();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = auth.getCurrentUser();
        if (cUser != null) {

            showSigned();

            Toast.makeText(this, "Добро пожаловать", Toast.LENGTH_SHORT).show();
        } else {

            notSigned();
        }
    }

    public void resetPassword() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityLogirovanie.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();

                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(MainActivityLogirovanie.this, "Введите ваш Email!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivityLogirovanie.this, "Проверьте ваш Email!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivityLogirovanie.this, "Не удалось отправить! Возможно такой Email address не зарегистрирован!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
    }

    private void showSignInWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Войти");
        dialog.setMessage("Введите данные для входа");

        LayoutInflater inflater = LayoutInflater.from(this);
        View sign_in_window = inflater.inflate(R.layout.sign_in_window, null);
        dialog.setView(sign_in_window);

        final AppCompatEditText email = sign_in_window.findViewById(R.id.emailField);
        final AppCompatEditText pass = sign_in_window.findViewById(R.id.passField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(MainActivityLogirovanie.this, R.string.error_sign_in_window_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.getText().toString().length() < 5) {
                    Toast.makeText(MainActivityLogirovanie.this, R.string.error_sign_in_window_pass, Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                showSigned();
//                                startActivity(new Intent(MainActivityLogirovanie.this, MainActivity.class));
//                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                notSigned();
                                Toast.makeText(MainActivityLogirovanie.this, R.string.error_sign_in_window, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        });

            }
        });

        dialog.show();
    }

    public void onClickSignOut(View view){
        FirebaseAuth.getInstance().signOut();
        bStart.setVisibility(View.GONE);
        tvUserEmail.setVisibility(View.GONE);
        bLogaut.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        forgotPassword.setVisibility(View.VISIBLE);
    }

    private void showSigned(){
        FirebaseUser user = auth.getCurrentUser();

        assert user != null;
        if(user.isEmailVerified()){
            String userName = "Вы вошли как " + user.getEmail();
            tvUserEmail.setText(userName);
            bStart.setVisibility(View.VISIBLE);
            tvUserEmail.setVisibility(View.VISIBLE);
            bLogaut.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
            forgotPassword.setVisibility(View.GONE);
        }
        else {
            bStart.setVisibility(View.GONE);
            tvUserEmail.setVisibility(View.GONE);
            bLogaut.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
            forgotPassword.setVisibility(View.VISIBLE);

            Toast.makeText(MainActivityLogirovanie.this, "Проверьте вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickStart(View view){
        Intent i = new Intent(MainActivityLogirovanie.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void notSigned(){
        bStart.setVisibility(View.GONE);
        tvUserEmail.setVisibility(View.GONE);
        bLogaut.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        forgotPassword.setVisibility(View.VISIBLE);
    }

    private void sendEmailVerification(){
        FirebaseUser user = auth.getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivityLogirovanie.this, "Проверьте вашу почту для подтверждения Email адреса", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivityLogirovanie.this, "Send email failed", Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void showRegisterWindow() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Зарегистрироваться");
        dialog.setMessage("Введите все данные для регистрации");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        dialog.setView(register_window);

        final AppCompatEditText email = register_window.findViewById(R.id.emailField);
        final AppCompatEditText pass = register_window.findViewById(R.id.passField);
        final AppCompatEditText name = register_window.findViewById(R.id.nameField);
        final AppCompatEditText phone = register_window.findViewById(R.id.phoneField);

        dialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(MainActivityLogirovanie.this, R.string.error_Register_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(MainActivityLogirovanie.this, R.string.error_Register_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(phone.getText().toString().length() < 11) {
                    Toast.makeText(MainActivityLogirovanie.this, R.string.error_Register_phone, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pass.getText().toString().length() < 5) {
                    Toast.makeText(MainActivityLogirovanie.this, R.string.error_Register_pass, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Регистрация пользователя

                auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();
                                user.setEmail(email.getText().toString());
                                user.setName(name.getText().toString());
                                user.setPass(pass.getText().toString());
                                user.setPhone(phone.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                showSigned();
                                                sendEmailVerification();
                                                Toast.makeText(MainActivityLogirovanie.this, R.string.success_Register, Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                notSigned();
                                Toast.makeText(MainActivityLogirovanie.this, R.string.error_Register, Toast.LENGTH_LONG).show();

                            }
                        });

            }
        });

        dialog.show();
    }
}