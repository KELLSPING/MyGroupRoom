package com.example.mygrouproom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    TextView tv0, tv1, tv2, tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        SenderUid = auth.getUid();

        tv0 = findViewById(R.id.tv0);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        tv0.setText(SenderUid);
        
        tv1.setText(database.getReference("groups").getKey());

        tv2.setText(database.getReference("groups").child("members").getKey());

        tv3.setText(database.getReference("groups").child("name").getKey());
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