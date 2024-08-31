package alf.java.telegram.helper;

import alf.java.telegram.model.User;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class UserDatabase {
    private FirebaseFirestore database;

    public Task<List<User>> readUser(String uid) {
        TaskCompletionSource<List<User>> taskComplete = new TaskCompletionSource<>();
        List<User> list = new ArrayList<>();

        database = FirebaseFirestore.getInstance();
        CollectionReference reference = database.collection("telegram");

        reference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    User user = User.fromMap(snapshot.getData());
                    if (!user.getUid().equals(uid)) {
                        list.add(user);
                    }
                }

                taskComplete.setResult(list);
            } else {
                Log.d("User", "Error getting documents: ", task.getException());
                taskComplete.setException(task.getException());
            }
        });

        return taskComplete.getTask();
    }

    public void createUser(String uid, String username) {
        database = FirebaseFirestore.getInstance();

        Random random = new Random();
        int index = random.nextInt(icon_list.size());

        User user = new User(username, uid, icon_list.get(index));
        Log.d("user", user.getUid());

        database.collection("telegram").document(uid).set(user.toMap());
    }

    List<String> icon_list = Arrays.asList(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQa4xjShh4ynJbrgYrW_aB4lhKSxeMzQ3cO_A&s",
            "https://static01.nyt.com/images/2022/05/19/opinion/firstpersonPromo/firstpersonPromo-superJumbo.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRI5zfIf97xbPS1fVI19PPFhLmF_fIQs3t-Jg&s",
            "https://www.mnp.ca/-/media/foundation/integrations/personnel/2020/12/16/13/58/personnel-image-2152.jpg?h=800&w=600&hash=348D3CD6A11FA554AF00E5E3C0453243"
    );
}
