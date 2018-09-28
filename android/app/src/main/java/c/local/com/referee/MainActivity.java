package c.local.com.referee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AndroidRuntimeException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;


public class MainActivity extends AppCompatActivity implements OnClickListener {

	private final String BASE_URL = "https://referee-694c6.firebaseapp.com/#";
	private DatabaseReference mDatabase;
	private String sid;
	private Score score;

	private TextView firstPoint;
	private TextView secondPoint;

	private Button qrButton;
	private Button shareButton;
	private Button clearButton;
	private Button firstMinusButton;
	private Button firstPlusButton;

	private Button secondMinusButton;
	private Button secondPlusButton;

	private DialogFragment dialogFragment;
	private FragmentManager flagmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// スコア
		this.sid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		score = new Score();
		firstPoint = findViewById(R.id.firstPoint);
		secondPoint = findViewById(R.id.secondPoint);

		qrButton = findViewById(R.id.qr);
		shareButton = findViewById(R.id.share);
		clearButton = findViewById(R.id.clear);
		firstMinusButton = findViewById(R.id.firstMinusButton);
		firstPlusButton = findViewById(R.id.firstPlusButton);
		secondMinusButton = findViewById(R.id.secondMinusButton);
		secondPlusButton = findViewById(R.id.secondPlusButton);

		qrButton.setOnClickListener(this);
		shareButton.setOnClickListener(this);
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
			case R.id.qr:
				flagmentManager = getSupportFragmentManager();
				dialogFragment = new AlertDialogFragment();
				dialogFragment.show(flagmentManager, "test alert dialog");
				return;
			case R.id.share:
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_SUBJECT, "Referee");
				intent.putExtra(Intent.EXTRA_TEXT, BASE_URL + sid);
				startActivity(intent);
				return;
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


	public static class AlertDialogFragment extends DialogFragment {

		private AlertDialog dialog;
		private AlertDialog.Builder alert;

		@Override
		@NonNull
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("Score URL");

			// カスタムレイアウトの生成
			View alertView = getActivity().getLayoutInflater().inflate(R.layout.qr_dailog, null);

			// alert_layout.xmlにあるボタンIDを使う
			ImageView qrImageView = alertView.findViewById(R.id.qr);
			Button closeButton = alertView.findViewById(R.id.close);
			closeButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// Dialogを消す
					getDialog().dismiss();
				}
			});

			int size = 300;
			try {
				BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
				MainActivity activity = (MainActivity) getActivity();
				//QRコードをBitmapで作成
				Bitmap bitmap = barcodeEncoder.encodeBitmap(activity.BASE_URL + activity.sid, BarcodeFormat.QR_CODE, size, size);
				qrImageView.setImageBitmap(bitmap);

			} catch (WriterException e) {
				throw new AndroidRuntimeException("Barcode Error.", e);
			}


			// ViewをAlertDialog.Builderに追加
			alert.setView(alertView);

			// Dialogを生成
			dialog = alert.create();
			dialog.show();

			return dialog;
		}

	}


}
