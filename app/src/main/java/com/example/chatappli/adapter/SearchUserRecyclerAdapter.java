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
import com.example.chatappli.model.Usermodel;
import com.example.chatappli.utils.AndroidUtil;
import com.example.chatappli.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<Usermodel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Usermodel> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull Usermodel model) {
        holder.usernametext.setText(model.getUsername());
        holder.phoneText.setText(model.getPhno());
        if (model.getUserId().equals(FirebaseUtil.currentUserid())){
            holder.usernametext.setText(model.getUsername()+" (Me)");
        }

        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t->{
                    if (t.isSuccessful()){
                        Uri uri=t.getResult();
                        AndroidUtil.setProfilepic(context,uri,holder.profilepic);
                    }
                });
        holder.itemView.setOnClickListener(v ->{

            Intent intent=new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{

        TextView usernametext;
        TextView phoneText;
        ImageView profilepic;
        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernametext=itemView.findViewById(R.id.username_text);
            phoneText=itemView.findViewById(R.id.phone_text);
            profilepic=itemView.findViewById(R.id.profile_pic_imageview);
        }
    }
}
