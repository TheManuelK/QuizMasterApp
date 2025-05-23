package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class HostActivityHostGame extends AppCompatActivity {

    private EditText roomNameEditText;
    private Button createRoomButton;

    // Firebase-Datenbankreferenz
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Hier wird das Layout der Activity gesetzt
        setContentView(R.layout.host_activity_hostgame);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisiere Firebase-Datenbank mit der expliziten URL
        databaseReference = FirebaseDatabase.getInstance("https://quizmaster-7842b-default-rtdb.europe-west1.firebasedatabase.app").getReference();


        Button startButton = findViewById(R.id.startButton);
        Button addQustionButton = findViewById(R.id.addQuestionButton);
        roomNameEditText = findViewById(R.id.roomName);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom();
            }
        });

        addQustionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HostActivityHostGame.this, HostActivityHostGameAddQuestion.class);
                startActivity(intent);
            }
        });

    }

    private void createRoom() {
        // Hole den eingegebenen Raum-Namen
        String roomName = roomNameEditText.getText().toString().trim();

        if (roomName.isEmpty()) {
            Toast.makeText(this, "Bitte einen Raum-Namen eingeben!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generiere eine eindeutige Raum-ID
        String roomId = UUID.randomUUID().toString();

        // Erstelle ein HashMap-Objekt für die Raumdaten
        HashMap<String, Object> roomData = new HashMap<>();
        roomData.put("roomId", roomId);
        roomData.put("roomName", roomName);
        roomData.put("hostName", "Host"); // Hier kannst du den Namen des Hosts einfügen
        roomData.put("createdAt", System.currentTimeMillis());
        roomData.put("players", new HashMap<>()); // Leere Liste für Spieler

        // Speichere die Raumdaten in Firebase
        databaseReference.child("rooms").child(roomId).setValue(roomData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Raum erstellt! Raum-ID: " + roomId, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HostActivityHostGame.this, HostActivityGameRoom.class);
                        intent.putExtra("ROOM_ID", roomId);
                        startActivity(intent);
                        // Optional: Diese Activity schließen, wenn der Nutzer nicht zurückkehren soll
                        finish();
                    } else {
                        Toast.makeText(this, "Fehler beim Erstellen des Raums!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

