package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity; // Oder androidx.fragment.app.FragmentActivity, je nachdem

public class PlayerActivityJoinGame extends AppCompatActivity {

    private Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity_joingame); // Stelle sicher, dass dies dein Layout-Dateiname ist

        // Finde den Button in deinem Layout
        joinButton = findViewById(R.id.joinGameButton); // Stelle sicher, dass R.id.joinGameButton die ID deines Buttons ist

        // Setze den OnClickListener
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Erstelle einen Intent, um zur PlayerActivityGameRoom zu navigieren
                Intent intent = new Intent(PlayerActivityJoinGame.this, PlayerActivityGameRoom.class);
                startActivity(intent);
            }
        });
    }
}