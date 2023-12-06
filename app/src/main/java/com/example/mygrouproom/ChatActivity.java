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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    DatabaseReference userRef, groupRef, groupNameRef;
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

        getSupportActionBar().setTitle(database.getReference("Groups").child("GroupChatRoom1").getKey());

        // get current group chat room name
        groupRef = database.getReference().child("Groups");
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    currentGroupName = dataSnapshot.child("GroupChatRoom1").getKey();
//                    tv.setText(currentGroupName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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

                String msgKey = groupRef.push().getKey(); // senderId

                String currentDateTime = currentDate + " " + currentTime;

                Messages messages = new Messages(currentUserName, currentUserId, currentDateTime, msgKey, message);

                database = FirebaseDatabase.getInstance();
                database.getReference().child("Groups")
                        .child("GroupChatRoom1")
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
    protected void onStart() {
        super.onStart();
        groupNameRef = database.getReference().child("Groups").child("GroupChatRoom1");

        groupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    String chatName = snapshot.child("name").getValue().toString();
                    String chatSenderId = snapshot.child("senderId").getValue().toString();
                    String chatDataTime = snapshot.child("dataTime").getValue().toString();
                    String chatMessage = snapshot.child("message").getValue().toString();

                    tv.append(chatName + "\n"
                            + chatMessage + "\n"
                            + chatDataTime + "\n\n\n");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    } // onStart

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