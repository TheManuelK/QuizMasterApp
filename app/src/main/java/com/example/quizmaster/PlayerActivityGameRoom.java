package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivityGameRoom extends AppCompatActivity {

    private ListView playersListView;
    private ArrayAdapter<String> playersAdapter;
    private List<String> playersList;

    private DatabaseReference databaseReference;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity_gameroom);

        // Initialisiere Firebase-Datenbank
        databaseReference = FirebaseDatabase.getInstance("https://quizmaster-7842b-default-rtdb.europe-west1.firebasedatabase.app").getReference();

        // Hole die Room-ID aus dem Intent
        roomId = getIntent().getStringExtra("ROOM_ID");

        // Initialisiere die ListView und die Liste der Spieler
        playersListView = findViewById(R.id.playersListView);
        playersList = new ArrayList<>();
        playersAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playersList);
        playersListView.setAdapter(playersAdapter);

        // Lade die Spieler aus der Firebase-Datenbank
        loadPlayers();
        startGame();
    }

    private void loadPlayers() {
        databaseReference.child("rooms").child(roomId).child("players")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        playersList.clear(); // Leere die Liste, bevor neue Daten hinzugefügt werden
                        for (DataSnapshot playerSnapshot : snapshot.getChildren()) {
                            String playerName = playerSnapshot.getKey(); // Spielername ist der Schlüssel
                            playersList.add(playerName); // Füge den Spielernamen zur Liste hinzu
                        }
                        playersAdapter.notifyDataSetChanged(); // Aktualisiere die ListView
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(PlayerActivityGameRoom.this, "Fehler beim Laden der Spieler: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("PlayerActivityGameRoom", "Fehler beim Laden der Spieler", error.toException());
                    }
                });
    }

    private void startGame() {
        databaseReference.child("rooms").child(roomId).child("QuestionIndex").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = new Intent(PlayerActivityGameRoom.this, PlayerActivityQuestionABCD.class);
                intent.putExtra("ROOM_ID", roomId);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
