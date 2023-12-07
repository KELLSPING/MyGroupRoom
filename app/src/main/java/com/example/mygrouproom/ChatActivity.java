package com.example.mygrouproom;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
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
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity implements  TextToSpeech.OnInitListener {
    DatabaseReference userRef, groupNameRef;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public static String sImage, rImage;
    MaterialCardView btnSend;
    EditText etMessage;
    String currentUserId, currentUserName, currentUserEmail,
            currentUserStatus, currentUserImageUri, currentUserRegisTimeStamp,
            currentUserLanguage;
    String chatName, chatSenderId, chatSendMsgTimeStamp, chatMessage, chatImageUri;
    TextView tv;
    ScrollView scrollView;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Log.d("kells", "ChatActivity : onCreate()");

        linkComponents();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle(database.getReference("Groups").child("GroupChatRoom1").getKey());

        initTextToSpeech();
        checkingSpeakTest();

        // get current user info
        currentUserId = auth.getUid();
        userRef = database.getReference().child("Users");
        userRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("kells", "ChatActivity : onCreate() : userRef : onDataChange()");
                if (dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue(String.class);
                    currentUserEmail = dataSnapshot.child("email").getValue(String.class);
                    currentUserStatus = dataSnapshot.child("status").getValue(String.class);
                    currentUserImageUri = dataSnapshot.child("imageUri").getValue(String.class);
                    currentUserLanguage = dataSnapshot.child("language").getValue(String.class);
                    currentUserRegisTimeStamp = dataSnapshot.child("regisTimeStamp").getValue().toString();
                }
                Log.d("kells", "ChatActivity : userRef : onDataChange() : currentUserRegisTimeStamp = " + currentUserRegisTimeStamp);
                if (currentUserLanguage != null){
                    Toast.makeText(ChatActivity.this, currentUserLanguage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 讀取數據時發生錯誤
                Log.d("kells", "Firebase Error reading data", databaseError.toException());
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();

                if (TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this, "Empty message", Toast.LENGTH_SHORT).show();
                }

                long sendMsgTimeStamp = System.currentTimeMillis();

                etMessage.setText("");

                Messages messages = new Messages(currentUserName, currentUserId, sendMsgTimeStamp, message, currentUserImageUri);

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
        Log.d("kells", "ChatActivity : onStart()");

        groupNameRef = database.getReference().child("Groups").child("GroupChatRoom1");

        groupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("kells", "ChatActivity : onStart() : groupNameRef : onChildAdded()");
                if (snapshot.exists()){
                    chatName = snapshot.child("name").getValue().toString();
                    chatSenderId = snapshot.child("senderId").getValue().toString();
                    chatSendMsgTimeStamp = snapshot.child("sendMsgTimeStamp").getValue().toString();
                    chatMessage = snapshot.child("message").getValue().toString();
                    chatImageUri = snapshot.child("imageUri").getValue().toString();

                    if (currentUserId == null){
                        Log.d("kells", "Current user ID is null");
                    } else if (chatSenderId == null){
                        Log.d("kells", "Chat sender ID is null");
                    } else {
                        if (!chatSenderId.equals(currentUserId)){
                            Log.d("kells", "currentUserId != chatSenderId");
                            if (currentUserRegisTimeStamp == null){
                                Log.d("kells", "Current user register time stamp is null");
                            } else if (chatSendMsgTimeStamp == null){
                                Log.d("kells", "Chat message time stamp is null");
                            } else {
                                if (chatMsgEarlierCurrUserRegis(currentUserRegisTimeStamp, chatSendMsgTimeStamp)){
                                    Log.d("kells", "chat message time is earlier than current user register time");
                                } else {
                                    Log.d("kells", "chat message time is later than current user register time");
                                    tv.append(chatName + "\n"
                                            + chatMessage + "\n"
                                            + formatTimeStamp(chatSendMsgTimeStamp) + "\n\n\n");

                                    scrollScrollViewToBottom();
                                }
                            }
                        }
                    }

                    tv.append(chatName + "\n"
                            + chatMessage + "\n"
                            + formatTimeStamp(chatSendMsgTimeStamp) + "\n\n\n");

                    scrollScrollViewToBottom();
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

    private boolean chatMsgEarlierCurrUserRegis(String currUserRegisTs, String chatSendMsgTs) {
        Log.d("kells", "ChatActivity : chatMsgEarlierCurrUserRegis()");
            long chatSendMsgTsL = transferStrToTimestamp(chatSendMsgTs);
            long currUserRegisTsL = transferStrToTimestamp(currUserRegisTs);
            if (currUserRegisTsL > chatSendMsgTsL){
                return  true;
            } else {
                return false;
            }
    }

    private long transferStrToTimestamp(String str){
        return Long.parseLong(str);
    }

    private String formatTimeStamp(String timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        long timeStampL = Long.parseLong(timeStamp);

        Date date = new Date(timeStampL);

        String timeStampF = dateFormat.format(date);
        return timeStampF;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("kells", "ChatActivity : onResume()");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("kells", "ChatActivity : onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("kells", "ChatActivity : onStop()");

        // 不管是否正在朗读TTS都被打断
        tts.stop();
        // 关闭，释放资源
        tts.shutdown();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
        Log.d("kells", "ChatActivity : onDestroy()");
    }

    private void linkComponents() {
        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);
        tv = findViewById(R.id.tv);
        scrollView = findViewById(R.id.scrollView);
    }

    private void scrollScrollViewToBottom() {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

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

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            // setLanguage设置语言
            int result = tts.setLanguage(Locale.CHINA);
            int result2 = tts.setLanguage(Locale.CHINESE);
            Log.d("kells：", "语言设置结果："+result+":"+result2);
            // TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失
            // TextToSpeech.LANG_NOT_SUPPORTED：不支持
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.d("kells：", "数据丢失或不支持：");
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }else {
                //不支持中文就将语言设置为英文
                Log.d("kells：", "将语言设置为英文");
                //tts.setLanguage(Locale.US);
                //tts.setLanguage(Locale.CHINESE);
                tts.setLanguage(Locale.CHINA);
            }
        }
    }

    private void initTextToSpeech() {
        // 参数Context,TextToSpeech.OnInitListener
        tts = new TextToSpeech(this, this);
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        tts.setPitch(1.0f);
        // 设置语速
//        tts.setSpeechRate(0.5f);

    }

    private void speakText(final String text, String Language) {

        if (Language == "tw"){
            Log.d("kells", "tw " );

            tts.setLanguage(Locale.CHINA);
        }else{
            Log.d("kells", "en " );
            tts.setLanguage(Locale.US);
        }

        Log.d("kells", "准备朗读" );
        // validate
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(ChatActivity.this, "请您输入要朗读的文字", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        if (tts != null && !tts.isSpeaking()) {
            /*
                TextToSpeech的speak方法有两个重载。
                // 执行朗读的方法
                speak(CharSequence text,int queueMode,Bundle params,String utteranceId);
                // 将朗读的的声音记录成音频文件
                synthesizeToFile(CharSequence text,Bundle params,File file,String utteranceId);
                第二个参数queueMode用于指定发音队列模式，两种模式选择
                （1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
                （2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，
                等前面的语音任务执行完了才会执行新的语音任务
             */
            Log.d("kells", "开始朗读");

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    } // speakText


    private void checkingSpeakTest() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    speakText("语音设备检测结果正常。","zh-TW");
                    Log.d("kells" ,"Joel");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}