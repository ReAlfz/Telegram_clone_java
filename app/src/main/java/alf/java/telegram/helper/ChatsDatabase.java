package alf.java.telegram.helper;

import alf.java.telegram.model.Chat;
import android.util.Log;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatsDatabase {
    private FirebaseFirestore database;
    public void readChat(List<Chat> list, String uid, String fromUid, RecyclerView.Adapter<?> adapter) {
        database = FirebaseFirestore.getInstance();
        CollectionReference reference = database.collection("telegram");
        CollectionReference chatRef = reference.document(uid).collection(fromUid);

        chatRef.orderBy("timestamp").addSnapshotListener((value, error) -> {
           if (error != null) {
               Log.w("Chats", "Listen failed", error);
           }

           if (value != null) {
               if (!list.isEmpty()) {
                   DocumentSnapshot last_chat = value.getDocuments().get(value.size() - 1);
                   Chat chat = Chat.fromMap(Objects.requireNonNull(last_chat.getData()));
                   list.add(chat);
                   adapter.notifyItemInserted(list.size() - 1);

               } else {
                   for (QueryDocumentSnapshot snapshot : value) {
                       Chat chat = Chat.fromMap(snapshot.getData());
                       list.add(chat);
                   }
                   adapter.notifyDataSetChanged();
               }
           } else {
               list.clear();
           }
        });
    }


    public void createChat(String uid, String fromUid, String text) {
        long time_stamp = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();

        Chat currentChat = new Chat(uid, fromUid, text, time_stamp, true);
        Chat fromChat = new Chat(uid, fromUid, text, time_stamp, false);

        database = FirebaseFirestore.getInstance();
        CollectionReference reference = database.collection("telegram");
        CollectionReference current_list = reference.document(uid).collection(fromUid);
        CollectionReference from_list = reference.document(fromUid).collection(uid);
        current_list.document(uuid).set(currentChat.toMap());
        from_list.document(uuid).set(fromChat.toMap());
    }
}
