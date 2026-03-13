package io.vsaps.ganit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	private Spinner leftSizeSpinner;
	private Spinner rightSizeSpinner;
	private Spinner operatorSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		leftSizeSpinner = findViewById(R.id.spinner_left_size);
		rightSizeSpinner = findViewById(R.id.spinner_right_size);
		operatorSpinner = findViewById(R.id.spinner_operator);
		Button startButton = findViewById(R.id.button_start);


		startButton.setOnClickListener(view -> {
			int leftSize = Integer.parseInt((String) leftSizeSpinner.getSelectedItem());
			int rightSize = Integer.parseInt((String) rightSizeSpinner.getSelectedItem());
			String operator = (String) operatorSpinner.getSelectedItem();

			Intent intent = new Intent(this, QuizActivity.class);
			intent.putExtra("leftSize", leftSize);
			intent.putExtra("rightSize", rightSize);
			intent.putExtra("operator", operator);
			startActivity(intent);
		});
	}
}