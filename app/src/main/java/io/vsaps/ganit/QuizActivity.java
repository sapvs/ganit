package io.vsaps.ganit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class QuizActivity extends AppCompatActivity {

	private TextView questionTextView;
	private EditText answerEditText;
	private TextView scoreTextView;

	private int firstDigitLength;
	private int secondDigitLength;
	private String operator;
	private int correctCount = 0;
	private int totalCount = 0;

	private double correctAnswer;

	private final Random random = new Random(System.nanoTime());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		// Get intent data
		firstDigitLength = getIntent().getIntExtra("leftSize", 1);
		secondDigitLength = getIntent().getIntExtra("rightSize", 1);
		operator = getIntent().getStringExtra("operator");

		questionTextView = findViewById(R.id.question);
		answerEditText = findViewById(R.id.answer);
		Button checkButton = findViewById(R.id.check_button);
		Button backButton = findViewById(R.id.back_button);
		scoreTextView = findViewById(R.id.score);

		// Load first question
		loadNewQuestion();

		// Check button click listener
		checkButton.setOnClickListener(v -> checkAnswer());

		// Back button click listener
		backButton.setOnClickListener(v -> finish());
	}

	private void loadNewQuestion() {
		int firstNumber = generateRandomNumber(firstDigitLength);
		int secondNumber = generateRandomNumber(secondDigitLength);
		correctAnswer = calculateAnswer(firstNumber, secondNumber, operator);

		questionTextView.setText(getString(R.string.QuestionText, firstNumber, operator, secondNumber));
		answerEditText.setText("");
		answerEditText.requestFocus();
		totalCount++;
	}

	private int generateRandomNumber(int digitLength) {
		if (digitLength <= 1) {
			// 1-digit number: 1..9
			return random.nextInt(9) + 1;
		}
		int min = (int) Math.pow(10, digitLength - 1);
		int max = (int) Math.pow(10, digitLength) - 1;
		return random.nextInt(max - min + 1) + min;
	}


	private double calculateAnswer(int first, int second, String op) {
		return switch (op) {
			case "+" -> first + second;
//			case "-" -> first >= second ? first - second : second - first;
			case "-" -> first - second;
			case "*" -> first * second;
			case "/" -> second != 0 ? (double) first / second : 0;
			default -> 0;
		};
	}

	private void checkAnswer() {
		String userAnswerStr = answerEditText.getText().toString().trim();

		if (userAnswerStr.isEmpty()) {
			Toast.makeText(this, "Please enter an answer", Toast.LENGTH_SHORT).show();
			return;
		}

		try {
			double userAnswer = Double.parseDouble(userAnswerStr);

			if (Math.abs(userAnswer - correctAnswer) < 0.0001) {
				Toast.makeText(this, "Correct! ✓", Toast.LENGTH_SHORT).show();
				correctCount++;
			} else {
				Toast.makeText(this, "Incorrect!.", Toast.LENGTH_SHORT).show();
			}
			updateScore();
			loadNewQuestion();
		} catch (NumberFormatException e) {
			Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
		}
	}

	private void updateScore() {
		scoreTextView.setText(getString(R.string.score, correctCount, totalCount));
	}
}