package com.example.chatappli.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatappli.ChatActivity;
import com.example.chatappli.R;
import com.example.chatappli.model.ChatRoomModel;
import com.example.chatappli.model.Usermodel;
import com.example.chatappli.utils.AndroidUtil;
import com.example.chatappli.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {

    Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserId())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        boolean lastMessagesentByMe=model.getLastMessageSenderId().equals(FirebaseUtil.currentUserid());



                        Usermodel otherUsermodel=task.getResult().toObject(Usermodel.class);

                        FirebaseUtil.getOtherProfilePicStorageRef(otherUsermodel.getUserId()).getDownloadUrl()
                                .addOnCompleteListener(t->{
                            if (t.isSuccessful()){
                                Uri uri=t.getResult();
                                AndroidUtil.setProfilepic(context,uri,holder.profilepic);
                            }
                        });

                        holder.usernametext.setText(otherUsermodel.getUsername());
                        if (lastMessagesentByMe){
                            holder.lastmessagetext.setText("You:"+model.getLastmessage());
                        }
                        else {
                            holder.lastmessagetext.setText(model.getLastmessage());
                        }
                        holder.lastmessagetime.setText(FirebaseUtil.timestampToString(model.getLasmessageTimeStamp()));

                        holder.itemView.setOnClickListener(v ->{
                            Intent intent=new Intent(context, ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(intent,otherUsermodel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });
                    }
                });
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row,parent,false);
        return new ChatRoomModelViewHolder(view);
    }

    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder{

        TextView usernametext;
        TextView lastmessagetext;
        TextView lastmessagetime;
        ImageView profilepic;
        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernametext=itemView.findViewById(R.id.username_text);
            lastmessagetext=itemView.findViewById(R.id.last_text);
            profilepic=itemView.findViewById(R.id.profile_pic_imageview);
            lastmessagetime=itemView.findViewById(R.id.last_message_time_text);
        }
    }
}
