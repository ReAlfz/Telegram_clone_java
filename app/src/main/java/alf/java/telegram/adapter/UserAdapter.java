package alf.java.telegram.adapter;

import alf.java.telegram.ChatsActivity;
import alf.java.telegram.R;
import alf.java.telegram.model.User;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final List<User> list;
    private final String currentUid;

    public UserAdapter(List<User> list, String currentUid) {
        this.list = list;
        this.currentUid = currentUid;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder viewHolder, int i) {
        User data = list.get(i);
        viewHolder.username.setText(data.getUsername());
        viewHolder.last_text.setText("");

        Picasso.get()
                .load(data.getIcon())
                .into(viewHolder.icon);

        viewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ChatsActivity.class);
            intent.putExtra("user", data);
            intent.putExtra("uid", currentUid);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView username, last_text;
        final ImageView icon;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon_item);
            username = itemView.findViewById(R.id.username_item);
            last_text = itemView.findViewById(R.id.last_text_item);
        }
    }
}


