package alf.java.telegram;

import alf.java.telegram.adapter.UserAdapter;
import alf.java.telegram.helper.UserDatabase;
import alf.java.telegram.model.User;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private boolean isLoading = true;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextInputEditText inputEditText;
    private final List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String currentUid = getIntent().getStringExtra("uid");

        progressBar = findViewById(R.id.loading_list_user);
        recyclerView = findViewById(R.id.user_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        TextView logout = findViewById(R.id.logout);

        loadLoading();

        UserDatabase cloud = new UserDatabase();
        cloud.readUser(currentUid).addOnSuccessListener(
                users -> {
                    userList.addAll(users);
                    isLoading = false;
                    loadLoading();

                    UserAdapter adapter = new UserAdapter(userList, currentUid);
                    recyclerView.setAdapter(adapter);
                }
        );

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });
    }


    private void loadLoading() {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}