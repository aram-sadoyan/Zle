package com.example.user.zle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.user.zle.generatorPuzzle.PuzzleSelectActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ClipView clipView;
    Button btn;
    FrameLayout background;
    private ArrayList<ClipView> clips = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        background = (FrameLayout) findViewById(R.id.backround);


        findViewById(R.id.button).setOnClickListener(dropClip);
        findViewById(R.id.button3).setOnClickListener(checkParams);


		int[] mImageIds = { R.drawable.noimage, R.drawable.images};

		Intent intent = new Intent(this, PuzzleSelectActivity.class);
		intent.putExtra("images", mImageIds);

		this.startActivity(intent);

	}


    View.OnClickListener dropClip = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            clipView = new ClipView(getApplicationContext(), background);
            clips.add(clipView);
            clipView.setArray(clips);
            clipView.addClipToArraylist();

}
    };


    View.OnClickListener checkParams = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Log.d("AG", String.valueOf(clips.size()));
            for (int i = 0; i < clips.size(); i++) {
                float X = clips.get(i).clip.getX() + 25;////margin
                float Y = clips.get(i).clip.getY() + 25;////margin
                if (clips.get(i).clip.getClipImage() != null) {
                    Log.d("clipParams", "getX = " + X + " getY =" + Y + " RotationAngel =" + clips.get(i).clip.getRotation() + " clipWidth= " + clips.get(i).clip.getLayoutParams().width + " clipHeight= " + clips.get(i).clip.getLayoutParams().height);
                } else if (clips.get(i).clip.textV != null) {
                    Log.d("clipParams", "TEXT " + "getX = " + clips.get(i).clip.getX() + " getY =" + clips.get(i).clip.getY() + " RotationAngel =" + clips.get(i).clip.getRotation() + " textSize= " + clips.get(i).clip.getTextsize() + " TEXT = " + clips.get(i).clip.getText());

                }
            }
        }
    };






    public void hideShowBorders(int visibility) {
        for (int i = 0; i < clips.size(); i++) {
            clips.get(i).getBtnRemove().setVisibility(visibility);
            clips.get(i).getBtnMove().setVisibility(visibility);
            /*clips.get(i).hideShowBorder(visibility);*/
            clips.get(i).moveBt.setVisibility(visibility);
            /*borderNotVisible = true;*/
        }
    }

    public void removeClip(Clip clip) {
        clips.remove(clip);
    }

}