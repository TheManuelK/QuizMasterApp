package com.example.quizmaster;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizmaster.QuizDatabaseHelper;
import com.example.quizmaster.R;

public class HostActivityHostGameAddQuestion extends AppCompatActivity {

    private EditText editTextQuestion, editTextOptionA, editTextOptionB, editTextOptionC, editTextOptionD, editTextCorrectAnswer;
    private LinearLayout layoutABCD, layoutEstimate, layoutButton;
    private Spinner spinnerQuestionType;
    private Button buttonSaveQuestion;

    private QuizDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_activity_hoastgame_addquestion);

        // SQLite-Datenbank initialisieren
        databaseHelper = new QuizDatabaseHelper(this);

        // UI-Elemente initialisieren
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextOptionA = findViewById(R.id.editTextOptionA);
        editTextOptionB = findViewById(R.id.editTextOptionB);
        editTextOptionC = findViewById(R.id.editTextOptionC);
        editTextOptionD = findViewById(R.id.editTextOptionD);
        editTextCorrectAnswer = findViewById(R.id.editTextCorrectAnswer);
        layoutABCD = findViewById(R.id.layoutABCD);
        layoutEstimate = findViewById(R.id.layoutEstimate);
        layoutButton = findViewById(R.id.layoutButton);
        spinnerQuestionType = findViewById(R.id.spinnerQuestionType);
        buttonSaveQuestion = findViewById(R.id.buttonSaveQuestion);

        // Spinner-Listener für Fragetyp
        spinnerQuestionType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                switch (selectedType) {
                    case "ABCD":
                        layoutABCD.setVisibility(View.VISIBLE);
                        layoutEstimate.setVisibility(View.GONE);
                        layoutButton.setVisibility(View.GONE);
                        break;
                    case "Schätzfrage":
                        layoutABCD.setVisibility(View.GONE);
                        layoutEstimate.setVisibility(View.VISIBLE);
                        layoutButton.setVisibility(View.GONE);
                        break;
                    case "Button-Frage":
                        layoutABCD.setVisibility(View.GONE);
                        layoutEstimate.setVisibility(View.GONE);
                        layoutButton.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Nichts tun
            }
        });

        // Button-Listener zum Speichern der Frage
        buttonSaveQuestion.setOnClickListener(v -> saveQuestion());
    }

    private void saveQuestion() {
        String question = editTextQuestion.getText().toString().trim();
        String questionType = spinnerQuestionType.getSelectedItem().toString();

        if (question.isEmpty()) {
            Toast.makeText(this, "Bitte eine Frage eingeben", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questionType.equals("ABCD")) {
            String optionA = editTextOptionA.getText().toString().trim();
            String optionB = editTextOptionB.getText().toString().trim();
            String optionC = editTextOptionC.getText().toString().trim();
            String optionD = editTextOptionD.getText().toString().trim();
            String correctAnswer = editTextCorrectAnswer.getText().toString().trim();

            if (optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || optionD.isEmpty() || correctAnswer.isEmpty()) {
                Toast.makeText(this, "Bitte alle Felder für die ABCD-Frage ausfüllen", Toast.LENGTH_SHORT).show();
                return;
            }

            // Frage in die SQLite-Datenbank einfügen
            databaseHelper.addQuestion(question, questionType, optionA, optionB, optionC, optionD, correctAnswer);
        } else {
            // Andere Fragetypen können hier hinzugefügt werden
            databaseHelper.addQuestion(question, questionType, null, null, null, null, null);
        }

        Toast.makeText(this, "Frage gespeichert!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
