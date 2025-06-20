package com.example.quizmaster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HostActivityGame extends AppCompatActivity {

    private TextView questionTextView;
    private Button nextQuestionButton;

    private DatabaseReference databaseReference;
    private ArrayList<String> questionList;
    private HashMap<String, String[]> questionsMap;
    private int currentQuestionIndex = 0;

    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_game);

        // Initialisiere Views
        questionTextView = findViewById(R.id.questionTextView);
        nextQuestionButton = findViewById(R.id.nextQuestionButton);

        // Hole die Raum-ID aus dem Intent
        roomId = getIntent().getStringExtra("ROOM_ID");

        // Initialisiere Firebase-Datenbank
        databaseReference = FirebaseDatabase.getInstance("https://quizmaster-7842b-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference();

        // Lade die Fragen aus der Firebase-Datenbank
        loadQuestions(roomId);
        databaseReference.child("rooms").child(roomId).child("QuestionIndex").setValue(1);

        // Setze den Listener für den "Next Question"-Button
        nextQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextQuestion();
            }
        });
    }

    private void loadQuestions(String roomId) {
        databaseReference.child("rooms").child(roomId).child("questions")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                           /* HashMap<String, Object> questionsMap = (HashMap<String, Object>) task.getResult().getValue();

                            for (String question : questionsMap.keySet()) {
                                Object optionsObject = questionsMap.get(question);

                                if (optionsObject instanceof ArrayList) {
                                    ArrayList<String> optionsList = (ArrayList<String>) optionsObject;

                                    Log.d("Firebase", "Frage: " + question);
                                    Log.d("Firebase", "Antwortmöglichkeiten: " + optionsList);
                                }
                            }*/
                            Toast.makeText(this, "Fragen erfolgreich geladen!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Keine Fragen gefunden!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Fehler beim Abrufen der Fragen!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void showQuestion(int index) {
        if (index >= 0 && index < questionList.size()) {
            String question = questionList.get(index);
            questionTextView.setText(question);

            // Aktualisiere den QuestionIndex in der Firebase-Datenbank
            databaseReference.child("rooms").child(roomId).child("QuestionIndex").setValue(index)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("HostActivityGame", "QuestionIndex erfolgreich aktualisiert: " + index);
                        } else {
                            Log.e("HostActivityGame", "Fehler beim Aktualisieren des QuestionIndex");
                        }
                    });
        } else {
            Toast.makeText(this, "Keine weiteren Fragen verfügbar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showNextQuestion() {
        if (currentQuestionIndex + 1 < questionList.size()) {
            currentQuestionIndex++;
            showQuestion(currentQuestionIndex);
        } else {
            Toast.makeText(this, "Das Quiz ist beendet!", Toast.LENGTH_SHORT).show();
        }
    }
}
