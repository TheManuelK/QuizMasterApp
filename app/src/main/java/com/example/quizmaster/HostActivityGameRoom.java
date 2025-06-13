package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HostActivityGameRoom extends AppCompatActivity {

    private DatabaseReference roomRef;

    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Hier wird das Layout der Activity gesetzt
        setContentView(R.layout.host_activity_gameroom);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        roomId = getIntent().getStringExtra("ROOM_ID");

        TextView roomIdSetter = findViewById(R.id.inviteCode);
        roomIdSetter.setText(getIntent().getStringExtra("ROOM_ID"));

        Button startButton = findViewById(R.id.startGameButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostActivityGameRoom.this, HostActivityGame.class);
                intent.putExtra("ROOM_ID", roomId); // Stelle sicher, dass roomId nicht null ist
                startActivity(intent);
            }
        });
    }
}