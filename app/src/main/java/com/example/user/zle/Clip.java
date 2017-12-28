package com.example.user.zle;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by AramSadoyan on 12/26/17.
 */

public class Clip extends FrameLayout {

	ImageView btnRemove, btnMove, border, image;
	EditText textV;
	FrameLayout.LayoutParams moveBtnp, borderP, imageP, clipParams;

	FrameLayout.LayoutParams removBtnP;
	private float seTY, seTX, setRemovBtnX;
	private int rLayoutWidth, rLayoutHeight;
	double forTan = 0;
	float alfa = 0;

	public Clip(Context context, ImageView imageView, EditText txtView) {
		super(context);
		init(imageView, txtView);
	}


	public void init(ImageView imView, EditText txtView) {
		rLayoutWidth = 500;
		rLayoutHeight = 500;
		if (imView != null) {
			clipParams = new FrameLayout.LayoutParams(rLayoutWidth, rLayoutHeight);
		} else {
			clipParams = new FrameLayout.LayoutParams(rLayoutWidth, 200);
		}

		this.setBackgroundColor(Color.TRANSPARENT);
		this.setLayoutParams(clipParams);

		if (imView != null) {
			imageP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			imageP.setMargins(25, 25, 25, 25);
			image = new ImageView(getContext());
			image = imView;
			image.setScaleType(ImageView.ScaleType.FIT_XY);
			image.setLayoutParams(imageP);
			this.addView(image);
		} else {
			FrameLayout.LayoutParams paramsExample = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			textV = txtView;
			textV.setGravity(Gravity.CENTER);
			textV.setTextColor(Color.BLACK);
			textV.setTextSize(this.getLayoutParams().height / 6);
			textV.setBackgroundResource(R.drawable.stroke);
			textV.setLayoutParams(paramsExample);
			//TODO change keyboard (into ok button)
            /*textV.setInputType(DEFAULT_KEYS_DIALER);*/
			this.addView(txtView);
		}

		if (imView != null) {
			borderP = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			borderP.setMargins(25, 25, 25, 25);
			border = new ImageView(getContext());
			//TODO set border drawable to image background
            /*border.setBackgroundResource(R.drawable.stroke);*/
			border.setScaleType(ImageView.ScaleType.FIT_XY);
			border.setLayoutParams(borderP);
			this.addView(border);
			this.invalidate();
		}

		removBtnP = new FrameLayout.LayoutParams(50, 50);
		btnRemove = new ImageView(getContext());
		btnRemove.setBackgroundResource(R.drawable.handle_rotate_picsart_light);
		seTX = clipParams.width - removBtnP.width;
		setRemovBtnX = this.getX();
		btnRemove.setX(setRemovBtnX);
		btnRemove.setScaleType(ImageView.ScaleType.FIT_XY);
		btnRemove.setLayoutParams(removBtnP);
		this.addView(btnRemove);

		moveBtnp = new FrameLayout.LayoutParams(50, 50);
		btnMove = new ImageView(getContext());
		btnMove.setBackgroundResource(R.drawable.handle_rotate_picsart_light);
		btnMove.setX(seTX);
		seTY = clipParams.height - removBtnP.height;
		btnMove.setY(seTY);
		btnMove.setLayoutParams(moveBtnp);
		this.removeView(btnMove);
		this.addView(btnMove);
	}

	public ImageView getBtnMove() {
		return btnMove;
	}

	public ImageView getBtnRemove() {
		return btnRemove;
	}

	public void setY(float y) {
		this.seTY = y;
	}

	public int getMovebtnsize() {
		return moveBtnp.width;
	}

	public int getRlayoutWidth() {
		return this.getWidth();
	}

	public int getRLayoutHeight() {
		return this.getHeight();
	}


	public void refreshTextSize( int sizeH) {
		if (textV != null) {

			textV.setTextSize(sizeH / 6);
		}
	}

	public void refreshBtn(int width, int height) {
		setRemovBtnX = 0;
		seTX = width - removBtnP.width;
		seTY = height - removBtnP.height;
		btnRemove.setX(setRemovBtnX);
		btnMove.setX(seTX);
		btnMove.setY(seTY);
	}

	public float getAlfa() {
		forTan = (double) clipParams.height / clipParams.width;
		alfa = (int) Math.toDegrees(Math.atan(forTan));
		return alfa;
	}


	public void hideShowBorder(int visibility) {
		if (image != null) {
			border.setVisibility(visibility);
		}
	}

	public View getClipImage() {
		return image;
	}

	public View getTextView() {
		return textV;
	}

	public String getText() {
		if (textV != null) {
			return textV.getText().toString();
		} else {
			return null;
		}
	}
	public float getTextsize() {
		if (textV != null) {
			return textV.getTextSize();
		} else {
			return 0;
		}
	}


}