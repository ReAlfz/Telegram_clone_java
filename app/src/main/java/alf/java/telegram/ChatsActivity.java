package alf.java.telegram;

import alf.java.telegram.adapter.ChatAdapter;
import alf.java.telegram.helper.ChatsDatabase;
import alf.java.telegram.model.Chat;
import alf.java.telegram.model.User;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {
    private ImageView btn_back, icon_chats;
    private TextView title;
    private MaterialButton send_chats;
    private TextInputEditText inputEditText;
    private RecyclerView chats_recycler;
    private final List<Chat> chatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        ChatsDatabase cloud = new ChatsDatabase();
        User user = getIntent().getParcelableExtra("user", User.class);
        String currentUid = getIntent().getStringExtra("uid");

        initialize();
        toolBarLoad(user);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chats_recycler.setLayoutManager(linearLayoutManager);
        ChatAdapter chatAdapter = new ChatAdapter(chatList);
        chats_recycler.setAdapter(chatAdapter);
        cloud.readChat(chatList, currentUid, user.getUid(), chatAdapter);


        send_chats.setOnClickListener(view -> {
            String text = String.valueOf(inputEditText.getText());
            cloud.createChat(currentUid, user.getUid(), text);
            inputEditText.setText("");
        });
    }

    private void toolBarLoad(User user) {

        btn_back.setOnClickListener(v -> finish());
        title.setText(user.getUsername());
        Picasso.get()
                .load(user.getIcon())
                .into(icon_chats);
    }

    private void initialize() {
        btn_back = findViewById(R.id.back_button_iv);
        icon_chats = findViewById(R.id.icon_chats);
        title = findViewById(R.id.chats_username);
        send_chats = findViewById(R.id.send_chat);
        chats_recycler = findViewById(R.id.chats_recycler);
        inputEditText = findViewById(R.id.input_chats);
    }
}