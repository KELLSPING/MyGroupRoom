package com.example.mygrouproom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    String ReceiverUid, ReceiverName, ReceiverImage, SenderUid;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public static String sImage;
    public static String rImage;
    MaterialCardView btnSend;
    EditText etMessage;
    String senderRoom, receiverRoom;
    RecyclerView messageAdapter;
//    ArrayList<Messages> messagesArrayList;
//    MessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        database = FirebaseDatabase.getInstance();
//        auth = FirebaseAuth.getInstance();
//
//        ReceiverUid = getIntent().getStringExtra("receiverUid");
//        ReceiverName = getIntent().getStringExtra("receiverName");
//        ReceiverImage = getIntent().getStringExtra("receiverImage");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 顯示 menu 中的返回按鍵
//        getSupportActionBar().setTitle(ReceiverName);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_chat_activity, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}