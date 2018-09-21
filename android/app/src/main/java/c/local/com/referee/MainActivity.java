package c.local.com.referee;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class MainActivity extends AppCompatActivity implements OnClickListener {

	private int firstPointCount = 0;
	private int secondPointCount = 0;

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

		firstPoint = findViewById(R.id.firstPoint);
		secondPoint = findViewById(R.id.secondPoint);

		clearButton = findViewById(R.id.clear);
		firstMinusButton = findViewById(R.id.firstMinusButton);
		firstPlusButton = findViewById(R.id.firstPlusButton);
		secondMinusButton = findViewById(R.id.firstMinusButton);
		secondPlusButton = findViewById(R.id.firstPlusButton);

		clearButton.setOnClickListener(this);
		firstMinusButton.setOnClickListener(this);
		firstPlusButton.setOnClickListener(this);
		secondMinusButton.setOnClickListener(this);
		secondPlusButton.setOnClickListener(this);

		firstPoint.setText(this.first());
		secondPoint.setText(this.second());
	}

	@Override
	public void onClick(android.view.View view) {
		switch (view.getId()) {
			case R.id.clear:
				clear();
				break;
			case R.id.firstMinusButton:
				firstPointCount--;
				break;
			case R.id.firstPlusButton:
				firstPointCount++;
				break;
			case R.id.secondMinusButton:
				secondPointCount--;
				break;
			case R.id.secondPlusButton:
				secondPointCount++;
				break;
		}
		new android.os.Handler(android.os.Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				firstPoint.setText(String.valueOf(firstPointCount));
				secondPoint.setText(String.valueOf(secondPointCount));
			}
		});
	}

	public void clear() {
		firstPointCount = 0;
		secondPointCount = 0;
	}
	public String first() {
		return String.valueOf(firstPointCount);
	}
	public String second() {
		return String.valueOf(secondPointCount);
	}
}
