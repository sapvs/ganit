package io.vsaps.ganit;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity {

	private LinearLayout mainLayout;
	private TextView questionTextView, scoreTextView, timerView;
	private EditText answerEditText;

	private int firstDigitLength, secondDigitLength, duration, correctCount, totalCount;
	private String operator;

	private double correctAnswer;
	private final Random random = new Random(System.nanoTime());

	private int SUBTLE_GREEN, SUBTLE_RED;

	private CountDownTimer timer;


	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);


		// Get intent data
		firstDigitLength = getIntent().getIntExtra("leftSize", 1);
		secondDigitLength = getIntent().getIntExtra("rightSize", 1);
		operator = getIntent().getStringExtra("operator");
		duration = getIntent().getIntExtra("duration", 30);

		SUBTLE_GREEN = getResources().getColor(R.color.subtle_green, getTheme());
		SUBTLE_RED = getResources().getColor(R.color.subtle_red, getTheme());

		mainLayout = findViewById(R.id.main_layout);
		questionTextView = findViewById(R.id.question);
		answerEditText = findViewById(R.id.answer);
		Button checkButton = findViewById(R.id.check_button);
		Button backButton = findViewById(R.id.back_button);
		scoreTextView = findViewById(R.id.score);
		timerView = findViewById(R.id.timer);
		scoreTextView.setText(getString(R.string.score, correctCount, totalCount));
		Activity me = this;


		timer = new CountDownTimer(TimeUnit.SECONDS.toMillis(duration), TimeUnit.SECONDS.toMillis(1)) {
			@Override
			public void onTick(long millisUntilFinished) {
				timerView.setText(getString(R.string.timer, 1 + TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)));
			}

			@Override
			public void onFinish() {
				new AlertDialog.Builder(me).setTitle("score").setMessage(scoreTextView.getText()).setPositiveButton("ok", (dialog, which) -> me.finish()).setOnDismissListener(dialog -> me.finish()).create().show();
			}
		};


		timer.start();
		loadNewQuestion();    // Load first question
		checkButton.setOnClickListener(v -> checkAnswer()); // Check button click listener
		backButton.setOnClickListener(v -> finish());        // Back button click listener
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
			return random.nextInt(9) + 1;
		}
		int min = (int) Math.pow(10, digitLength - 1);
		int max = (int) Math.pow(10, digitLength) - 1;
		return random.nextInt(max - min + 1) + min;
	}


	private double calculateAnswer(int first, int second, String op) {
		return switch (op) {
			case "+" -> first + second;
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
				correctCount++;
				mainLayout.setBackgroundColor(SUBTLE_GREEN);
			} else {
				mainLayout.setBackgroundColor(SUBTLE_RED);
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