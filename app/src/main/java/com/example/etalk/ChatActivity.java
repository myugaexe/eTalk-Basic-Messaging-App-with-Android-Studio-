package com.example.etalk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private TextView chatTitle;
    private EditText messageInput;
    private ImageView sendButton;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<String> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatTitle = findViewById(R.id.chatTitle);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);

        String contactName = getIntent().getStringExtra("contactName");
        if (contactName != null) {
            chatTitle.setText(contactName);
        }

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(view -> sendMessage());
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();

        if (!message.isEmpty()) {
            messageList.add(message);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            messageRecyclerView.scrollToPosition(messageList.size() - 1);

            messageInput.setText("");
        }
    }
}
