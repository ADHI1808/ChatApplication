package com.example.chatappli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chatappli.adapter.ChatRecyclerAdapter;
import com.example.chatappli.adapter.SearchUserRecyclerAdapter;
import com.example.chatappli.model.ChatMessageModel;
import com.example.chatappli.model.ChatRoomModel;
import com.example.chatappli.model.Usermodel;
import com.example.chatappli.utils.AndroidUtil;
import com.example.chatappli.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {
    Usermodel otherUser;
    String chatRoomId;
    ChatRoomModel chatRoomModel;

    EditText messageinput;
    ImageButton sendmessbutn;
    ImageButton backbtn;
    TextView otherusername;
    RecyclerView recyclerView;
    ChatRecyclerAdapter adapter;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //get UserModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatRoomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserid(), otherUser.getUserId());

        messageinput = findViewById(R.id.chat_message_input);

        backbtn = findViewById(R.id.back_btn);
        otherusername = findViewById(R.id.other_username);
        recyclerView = findViewById(R.id.chat_recycler_view);
        sendmessbutn = findViewById(R.id.message_send_btn);
        imageView = findViewById(R.id.profile_pic_imageview);

        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()) {
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilepic(this, uri, imageView);
                    }
                });

        backbtn.setOnClickListener(v -> {
            onBackPressed();
        });
        otherusername.setText(otherUser.getUsername());

        sendmessbutn.setOnClickListener((v -> {
            String message = messageinput.getText().toString().trim();
            if (message.isEmpty()) {
                return;
            }
            sendMessageToUser(message);

        }));

        getonCreateChatroomModel();
        setupChatRecyclerView();

    }

    void setupChatRecyclerView() {
        Query query = FirebaseUtil.getChatRoomMessageReference(chatRoomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message) {

        chatRoomModel.setLasmessageTimeStamp(Timestamp.now());
        chatRoomModel.setLastMessageSenderId(FirebaseUtil.currentUserid());
        chatRoomModel.setLastmessage(message);
        FirebaseUtil.getChatroomReference(chatRoomId).set(chatRoomModel);


        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtil.currentUserid(), Timestamp.now());
        FirebaseUtil.getChatRoomMessageReference(chatRoomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageinput.setText("");

                        }
                    }
                });
    }

    void getonCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                if (chatRoomModel == null) {
                    chatRoomModel = new ChatRoomModel(
                            chatRoomId,
                            Arrays.asList(FirebaseUtil.currentUserid(), otherUser.getUserId()),
                            Timestamp.now(), ""
                    );
                    FirebaseUtil.getChatroomReference(chatRoomId).set(chatRoomModel);
                }
            }
        });
    }



}


