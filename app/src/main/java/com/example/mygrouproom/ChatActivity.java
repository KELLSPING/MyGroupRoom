package com.example.mygrouproom;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    DatabaseReference chatRef, userRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public static String sImage;
    public static String rImage;
    MaterialCardView btnSend;
    EditText etMessage;
    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;
    MessagesAdapter adapter;

    String currentDate, currentTime;
    String currentUserId, currentUserName, currentUserEmail, currentUserStatus, currentUserImageUri;

    String currentGroupName;
    String ReceiverUid, ReceiverName, ReceiverImage, SenderUid;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);
        messageAdapter = findViewById(R.id.messageAdapter);

        tv = findViewById(R.id.tv);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle(database.getReference("Groups").child("GroupChatRoom").getKey());

        chatRef = database.getReference().child("Groups");
        String msgKey = chatRef.push().getKey(); // senderId

        // get current user info
        currentUserId = auth.getUid();
        userRef = database.getReference().child("Users");
        userRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue(String.class);
                    currentUserEmail = dataSnapshot.child("email").getValue(String.class);
                    currentUserStatus = dataSnapshot.child("status").getValue(String.class);
                    currentUserImageUri = dataSnapshot.child("imageUri").getValue(String.class);
                    tv.setText(currentUserImageUri);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 讀取數據時發生錯誤
                Log.e("Firebase", "Error reading data", databaseError.toException());
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();

                if (TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this, "Empty message", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendarForDate = Calendar.getInstance();
                    SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    currentDate = currentDateFormat.format(calendarForDate.getTime());

                    Calendar calendarForTime = Calendar.getInstance();
                    SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss");
                    currentTime = currentTimeFormat.format(calendarForTime.getTime());
                }

                etMessage.setText("");

                Date date = new Date();

                Messages messages = new Messages(date.getTime(), msgKey, message);

                database = FirebaseDatabase.getInstance();
                database.getReference().child("Group")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

            }
        }); // btnSend

    } // onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ChatActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return false;
    }
}