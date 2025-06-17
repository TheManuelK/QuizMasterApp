package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HostActivityHostGame extends AppCompatActivity {

    private EditText roomNameEditText;
    private Button createRoomButton;

    // Firebase-Datenbankreferenz
    private DatabaseReference databaseReference;

    // HashMap zum Speichern der Fragen
    // HashMap zum Speichern der Fragen
// Ändere String[] zu List<String>
    private HashMap<String, List<String>> questionsMap = new HashMap<>();


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
        Button addQuestionButton = findViewById(R.id.addQuestionButton);
        roomNameEditText = findViewById(R.id.roomName);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRoom();
            }
        });

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddQuestionDialog();
            }
        });
    }

    private void openAddQuestionDialog() {
        // Dialog-Layout laden
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_question, null);

        EditText questionInput = dialogView.findViewById(R.id.question_input);
        EditText option1Input = dialogView.findViewById(R.id.option1_input);
        EditText option2Input = dialogView.findViewById(R.id.option2_input);
        EditText option3Input = dialogView.findViewById(R.id.option3_input);
        EditText option4Input = dialogView.findViewById(R.id.option4_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView)

                .setTitle("Add Question")

                .setPositiveButton("Add", (dialog, which) -> {

                    String question = questionInput.getText().toString().trim();

                    String option1 = option1Input.getText().toString().trim();

                    String option2 = option2Input.getText().toString().trim();

                    String option3 = option3Input.getText().toString().trim();

                    String option4 = option4Input.getText().toString().trim();


                    if (!question.isEmpty() && !option1.isEmpty() && !option2.isEmpty() &&

                            !option3.isEmpty() && !option4.isEmpty()) {


                        // Erstelle eine Liste für die Antworten statt eines Arrays

                        List<String> optionsList = new ArrayList<>(); // Füge import java.util.ArrayList; hinzu

                        optionsList.add(option1);

                        optionsList.add(option2);

                        optionsList.add(option3);

                        optionsList.add(option4);


                        // Frage und Antworten (als Liste) in der HashMap speichern

                        questionsMap.put(question, optionsList); // Speichere die Liste

                        Toast.makeText(HostActivityHostGame.this, "Question added!", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(HostActivityHostGame.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();

                    }

                })

                .setNegativeButton("Cancel", null)

                .create()

                .show();

    }


    private void createRoom() {
        String roomName = roomNameEditText.getText().toString().trim();

        if (roomName.isEmpty()) {
            Toast.makeText(this, "Bitte einen Raum-Namen eingeben!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questionsMap.isEmpty()) {
            Toast.makeText(this, "Bitte füge mindestens eine Frage hinzu!", Toast.LENGTH_SHORT).show();
            return;
        }

        String roomId = UUID.randomUUID().toString();

        HashMap<String, Object> roomData = new HashMap<>();
        roomData.put("roomId", roomId);
        roomData.put("roomName", roomName);
        roomData.put("hostName", "Host");
        roomData.put("createdAt", System.currentTimeMillis());
        roomData.put("questions", questionsMap);
        roomData.put("players", new HashMap<>());
        roomData.put("QuestionIndex", 1);

        databaseReference.child("rooms").child(roomId).setValue(roomData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Raum erstellt! Raum-ID: " + roomId, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HostActivityHostGame.this, HostActivityGameRoom.class);
                        intent.putExtra("ROOM_ID", roomId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Fehler beim Erstellen des Raums!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
