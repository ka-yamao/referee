package c.local.com.referee;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity implements OnClickListener {

	private DatabaseReference mDatabase;
	private String sid;
	private Score score;

	private TextView firstPoint;
	private TextView secondPoint;

	private Button clearButton;
	private Button firstMinusButton;
	private Button firstPlusButton;

	private Button secondMinusButton;
	private Button secondPlusButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// スコア
		this.sid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		score = new Score();
		firstPoint = findViewById(R.id.firstPoint);
		secondPoint = findViewById(R.id.secondPoint);

		clearButton = findViewById(R.id.clear);
		firstMinusButton = findViewById(R.id.firstMinusButton);
		firstPlusButton = findViewById(R.id.firstPlusButton);
		secondMinusButton = findViewById(R.id.secondMinusButton);
		secondPlusButton = findViewById(R.id.secondPlusButton);

		clearButton.setOnClickListener(this);
		firstMinusButton.setOnClickListener(this);
		firstPlusButton.setOnClickListener(this);
		secondMinusButton.setOnClickListener(this);
		secondPlusButton.setOnClickListener(this);

		firstPoint.setText(score.first());
		secondPoint.setText(score.second());


		mDatabase = FirebaseDatabase.getInstance().getReference();

		writeScore();
	}

	@Override
	public void onClick(android.view.View view) {
		switch (view.getId()) {
			case R.id.clear:
				score.clear();
				break;
			case R.id.firstMinusButton:
				if (score.firstPoint == 0) {
					return;
				}
				score.firstPoint--;
				break;
			case R.id.firstPlusButton:
				score.firstPoint++;
				break;
			case R.id.secondMinusButton:
				if (score.secondPoint == 0) {
					return;
				}
				score.secondPoint--;
				break;
			case R.id.secondPlusButton:
				score.secondPoint++;
				break;
		}

		writeScore();
		new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				firstPoint.setText(score.first());
				secondPoint.setText(score.second());
			}
		});
	}


	private void writeScore() {
		mDatabase.child("score").child(this.sid).setValue(score);
	}
}
