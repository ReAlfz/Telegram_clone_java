package alf.java.telegram;

import alf.java.telegram.helper.UserDatabase;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private boolean isFlip = true;
    private TextView flip_textview;
    private ViewFlipper viewFlipper;
    private Button btn_register, btn_login;
    private EditText email_login, password_login;
    private EditText username_register, email_register, password_register;

    private FirebaseAuth auth;

    private void initialize() {
        viewFlipper = findViewById(R.id.flipview);

        View font = getLayoutInflater().inflate(R.layout.font_container, null);
        View back = getLayoutInflater().inflate(R.layout.back_container, null);
        viewFlipper.addView(font);
        viewFlipper.addView(back);

        btn_login = font.findViewById(R.id.btn_login);
        btn_register = back.findViewById(R.id.btn_register);

        email_login = font.findViewById(R.id.email_text_login);
        password_login = font.findViewById(R.id.password_text_login);

        username_register = back.findViewById(R.id.username_text_register);
        email_register = back.findViewById(R.id.email_text_register);
        password_register = back.findViewById(R.id.password_text_register);

        flip_textview = findViewById(R.id.flip_textview);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            nextPage(auth.getUid());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        initialize();
        onClickFunction();
        buttonClickFunction();
    }

    private void buttonClickFunction() {
        UserDatabase cloud = new UserDatabase();

        btn_login.setOnClickListener(v -> {
            String email = email_login.getText().toString();
            String password = password_login.getText().toString();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            nextPage(user != null ? user.getUid() : "");

                            email_login.setText("");
                            password_login.setText("");

                        } else {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btn_register.setOnClickListener(v -> {
            String username = username_register.getText().toString();
            String email = email_register.getText().toString();
            String password = password_register.getText().toString();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            cloud.createUser(user != null ? user.getUid() : "", username);
                            nextPage(user != null ? user.getUid() : "");

                            username_register.setText("");
                            email_register.setText("");
                            password_register.setText("");

                        } else {
                            Log.w("Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
        });
    }

    private void nextPage(String uid) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }

    private void onClickFunction() {
        String fullText = "Don't have an account? Register";
        String clickableText = "Register";
        int start = fullText.indexOf(clickableText);
        int end = start + clickableText.length();
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(
                new ForegroundColorSpan(Color.parseColor("#179CDE")),
                start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                flipContainer();
            }
        }, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        flip_textview.setText(spannableString);
        flip_textview.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void flipContainer() {
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.flip_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.flip_out));

        if (isFlip) {
            viewFlipper.showNext();
        } else {
            viewFlipper.showPrevious();
        }
        isFlip = !isFlip;
    }
}