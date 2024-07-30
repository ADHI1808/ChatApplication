package com.example.chatappli;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatappli.model.Usermodel;
import com.example.chatappli.utils.AndroidUtil;
import com.example.chatappli.utils.FirebaseUtil;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Profile_fragment extends Fragment {


    ImageView profilepic;
    EditText usernameinput;
    Button update;
    ProgressBar progressBar;
    TextView logoutbtn;
    EditText phoneinput;

    Usermodel currentUserModel;
    ActivityResultLauncher<Intent> imagePicLauncher;
    Uri selectedImageURI;

    public Profile_fragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePicLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            selectedImageURI = data.getData();
                            AndroidUtil.setProfilepic(getContext(), selectedImageURI, profilepic);
                        }
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        profilepic = view.findViewById(R.id.profile_image_view);
        usernameinput = view.findViewById(R.id.profile_user_name);
        phoneinput = view.findViewById(R.id.profile_phone);
        update = view.findViewById(R.id.profile_update_button);
        progressBar = view.findViewById(R.id.profile_progressbar);
        logoutbtn = view.findViewById(R.id.logout_btn);

        getUserData();

        update.setOnClickListener(v -> {
            updatebtnclick();
        });
        logoutbtn.setOnClickListener(v -> {
            FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseUtil.logout();
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });

        });

        profilepic.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512, 512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePicLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        return view;
    }

    void updatebtnclick() {
        String newusername = usernameinput.getText().toString();
        if (newusername.isEmpty() || usernameinput.length() < 3) {
            usernameinput.setError("Username Should Be Atleast 3 Chars");
            return;
        }
        currentUserModel.setUsername(newusername);
        setInprogress(true);

        if (selectedImageURI != null) {
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageURI)
                    .addOnCompleteListener(v -> {
                        updateTofireStore();
                    });
        } else {
            updateTofireStore();
        }

    }

    void updateTofireStore() {
        FirebaseUtil.currentuserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    setInprogress(false);
                    if (task.isSuccessful()) {
                        AndroidUtil.showToast(getContext(), "Updated Successfully");
                    } else {
                        AndroidUtil.showToast(getContext(), "Update Failed");
                    }
                });
    }

    void getUserData() {
        setInprogress(true);
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri uri = task.getResult();
                AndroidUtil.setProfilepic(getContext(), uri, profilepic);
            }
        });

        FirebaseUtil.currentuserDetails().get().addOnCompleteListener(task -> {
            setInprogress(false);
            currentUserModel = task.getResult().toObject(Usermodel.class);
            usernameinput.setText(currentUserModel.getUsername());
            phoneinput.setText(currentUserModel.getPhno());

        });
    }

    void setInprogress(boolean inprogress) {
        if (inprogress) {
            progressBar.setVisibility(View.VISIBLE);
            update.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
        update.setVisibility(View.VISIBLE);
        }
    }

}