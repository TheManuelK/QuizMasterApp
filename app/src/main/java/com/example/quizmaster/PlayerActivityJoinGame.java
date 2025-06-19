package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PlayerActivityJoinGame extends AppCompatActivity {

    private EditText playerNameEditText;
    private EditText roomIdEditText;
    private Button joinGameButton;

    // Firebase-Datenbankreferenz
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity_joingame);

        // Initialisiere Firebase-Datenbank
        databaseReference = FirebaseDatabase.getInstance("https://quizmaster-7842b-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        // Initialisiere Views
        playerNameEditText = findViewById(R.id.playerNameEditText);
        roomIdEditText = findViewById(R.id.roomIdEditText);
        joinGameButton = findViewById(R.id.joinGameButton);

        // Setze den Listener für den "Join Game"-Button
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGame();
            }
        });
    }

    private void joinGame() {
        String playerName = playerNameEditText.getText().toString().trim();
        String roomId = roomIdEditText.getText().toString().trim();

        if (playerName.isEmpty()) {
            Toast.makeText(this, "Bitte gib deinen Namen ein!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (roomId.isEmpty()) {
            Toast.makeText(this, "Bitte gib die Room-ID ein!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Füge den Spieler zur Firebase-Datenbank hinzu
        databaseReference.child("rooms").child(roomId).child("players").child(playerName).setValue(true)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Spiel beigetreten!", Toast.LENGTH_SHORT).show();

                        // Starte die nächste Activity
                        Intent intent = new Intent(PlayerActivityJoinGame.this, PlayerActivityGameRoom.class);
                        intent.putExtra("ROOM_ID", roomId);
                        intent.putExtra("PLAYER_NAME", playerName);
                        startActivity(intent);

                        // Optional: Diese Activity schließen
                        finish();
                    } else {
                        Toast.makeText(this, "Fehler beim Beitreten des Spiels!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Fehler: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("PlayerActivityJoinGame", "Fehler beim Beitreten des Spiels", e);
                });
    }
}
