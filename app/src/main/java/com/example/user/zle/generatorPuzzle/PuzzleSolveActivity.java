package com.example.user.zle.generatorPuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.user.zle.R;

import java.io.File;

public class PuzzleSolveActivity extends Activity {
	private PuzzleView puzzleView;
	private Bitmap image;

	private int puzzleInt;

	private AlertDialog.Builder builder;
	private AlertDialog alert;
	private ImageView preview_view;
	private Dialog preview_dialog;

	public void onStart() {
		super.onStart();
		builder = new AlertDialog.Builder(this);

		puzzleInt = this.getIntent().getIntExtra("image", 0);
		image = (Bitmap) BitmapFactory.decodeResource(getResources(), puzzleInt);

		puzzleView = (PuzzleView) this.findViewById(R.id.puzzleView);

		// Now we need to test if it already exists.
		File testExistance = new File(path(puzzleInt));

		if (testExistance != null && testExistance.exists()) {
			puzzleView.loadPuzzle(path(puzzleInt));
		} else {
			puzzleView.loadPuzzle(image, path(puzzleInt));
		}

		preview_view = (ImageView) getLayoutInflater().inflate(R.layout.preview_puzzle, null);
		preview_view.setImageBitmap(image);

		preview_dialog = new Dialog(this);
		preview_dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		preview_dialog.setContentView(preview_view);

		preview_view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preview_dialog.dismiss();
			}
		});

		Button menu = (Button) this.findViewById(R.id.menu);

		final CharSequence[] items = {
				getString(R.string.show_cover),
				getString(R.string.solve), getString(R.string.reset)};

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
					case 0:
						preview_dialog.show();
						break;
					case 1:
						puzzleView.solve();
						break;
					case 2:
						puzzleView.shuffle();
						break;
				}
			}
		});

		alert = builder.create();

		OnClickListener menuListener = new OnClickListener() {
			public void onClick(View v) {
				alert.show();
			}
		};

		menu.setOnClickListener(menuListener);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.solve);
	}

	public void onPause() {
		super.onPause();
		puzzleView.savePuzzle(path(puzzleInt));
	}

	@Override
	public void onSaveInstanceState(Bundle b) {
		super.onSaveInstanceState(b);
		b.putInt("puzzleInt", puzzleInt);
	}

	@Override
	public void onRestoreInstanceState(Bundle b) {
		super.onRestoreInstanceState(b);
		puzzleInt = b.getInt("puzzleInt");
		puzzleView.loadPuzzle(path(puzzleInt));
	}

	private String path(int puzzle) {
		String rv = null;
		if (Environment.getExternalStorageState().equals("mounted")) {
			rv = getExternalCacheDir().getAbsolutePath() + "/" + puzzle + "/"
					+ "easy" + "/";
		} else {
			rv = getCacheDir().getAbsolutePath() + "/" + puzzle + "/"
					+ "easy" + "/";
		}
		return rv;
	}
}