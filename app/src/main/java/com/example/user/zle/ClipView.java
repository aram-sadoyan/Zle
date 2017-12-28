package com.example.user.zle;

/**
 * Created by AramSadoyan on 12/26/17.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

class ClipView extends FrameLayout {

	Clip clip;
	View borderBG;
	FrameLayout background;
	ImageView moveBt;
	View rect;
	RectF rectFordetection;

	RelativeLayout.LayoutParams btnParams, clipMoveAndRotateRelParams, rectParams;
	private float _xDelta;
	private float _yDelta;
	boolean moved = false;
	boolean movedFromMultitouch = false;


	private float degress;
	private float degres = 0, degres2 = 0, degresFinal, alfa1 = 0, degresAfterMultyTouch = 0;
	private float finalMoveBtnseX, finalMoveBtnseY;

	ImageView imageClip, imageClip2;
	EditText txtView;
	Context context;

	private ArrayList<ClipView> clipsw = new ArrayList<>();

	public ClipView(Context context, FrameLayout background) {
		super(context);
		this.context = context;
		this.background = background;
		init();
	}

	public void init() {
		borderBG = new View(context);

		btnParams = new RelativeLayout.LayoutParams(70, 70);
		rectParams = new RelativeLayout.LayoutParams(10, 10);

		txtView = new EditText(context);
		txtView.setText("");

		txtView.setImeOptions(EditorInfo.IME_ACTION_DONE);

		txtView.invalidate();
		imageClip = new ImageView(context);
		imageClip2 = new ImageView(context);
		imageClip.setImageResource(R.drawable.item1);
		imageClip2.setImageResource(R.drawable.item2);

		//creating Image - clip
		clip = new Clip(context, imageClip, null);

		//creating Text - clip
        /*clip = new Clip(this, null, txtView);*/

		moveBt = new ImageButton(context);
		moveBt.setLayoutParams(btnParams);
		moveBt.setOnTouchListener(moveBtTouch);
		background.setOnClickListener(onClipTouchclick);
		rect = new ImageButton(context);

		clip.getBtnRemove().setOnClickListener(removeListener);

		txtView.addTextChangedListener(new TextWatcher() {
			int initialTxtlength = 4;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				////fixmovebtn
				moveBt.setX(clip.getX() + clip.getRlayoutWidth() - clip.getMovebtnsize());
				moveBt.setY(clip.getY() + clip.getRLayoutHeight() - clip.getMovebtnsize());
				moveBt.setLayoutParams(moveBt.getLayoutParams());
				moveBt.setPivotX(-clip.getWidth() / 2 + moveBt.getLayoutParams().width);
				moveBt.setPivotY(-clip.getHeight() / 2 + moveBt.getLayoutParams().height);
				moveBt.setRotation(degresFinal);
				moveBt.setLayoutParams(moveBt.getLayoutParams());
				background.invalidate();
				moveBt.invalidate();
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int sizeCount = (int) clip.getTextsize() / 2;
				if (txtView.length() > initialTxtlength) {
					clip.getLayoutParams().width += sizeCount;
					FrameLayout.LayoutParams layoutParamsForMovebtn = (FrameLayout.LayoutParams) clip.getLayoutParams();
					layoutParamsForMovebtn.leftMargin -= sizeCount / 2;
					clip.getBtnMove().setX(clip.getBtnMove().getX() + sizeCount);
					///fixmovebtn
					moveBt.setX(clip.getX() + clip.getRlayoutWidth() + 35);/////////???
					moveBt.setY(clip.getY() + clip.getRLayoutHeight() - clip.getMovebtnsize());
					moveBt.setLayoutParams(moveBt.getLayoutParams());
					moveBt.setPivotX(-clip.getWidth() / 2 + moveBt.getLayoutParams().width);
					moveBt.setPivotY(-clip.getHeight() / 2 + moveBt.getLayoutParams().height);
					moveBt.setRotation(degresFinal);
					moveBt.setLayoutParams(moveBt.getLayoutParams());
					background.invalidate();
					moveBt.invalidate();

					alfa1 = clip.getAlfa();
					initialTxtlength = txtView.length();
				} else if (txtView.length() < initialTxtlength && txtView.length() >= 4) {
					clip.getLayoutParams().width -= sizeCount;
					FrameLayout.LayoutParams layoutParamsForMovebtn = (FrameLayout.LayoutParams) clip.getLayoutParams();
					layoutParamsForMovebtn.leftMargin += sizeCount / 2;
					clip.getBtnMove().setX(clip.getBtnMove().getX() - sizeCount);
					//fix movebtn
					moveBt.setX(clip.getX() + clip.getRlayoutWidth() - clip.getMovebtnsize());
					moveBt.setY(clip.getY() + clip.getRLayoutHeight() - clip.getMovebtnsize());
					moveBt.setLayoutParams(moveBt.getLayoutParams());
					moveBt.setPivotX(-clip.getWidth() / 2 + moveBt.getLayoutParams().width);
					moveBt.setPivotY(-clip.getHeight() / 2 + moveBt.getLayoutParams().height);
					moveBt.setRotation(degresFinal);
					moveBt.setLayoutParams(moveBt.getLayoutParams());
					background.invalidate();
					moveBt.invalidate();

					alfa1 = clip.getAlfa();
					initialTxtlength = txtView.length();
				}
				if (txtView.length() == 0) {
					txtView.setCursorVisible(true);
				} else {
					txtView.setCursorVisible(false);
				}
			}
		});
	}


	View.OnClickListener onClipTouchclick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			clearKeyboard();

			hideShowBorders2(View.INVISIBLE);


		}
	};

	View.OnTouchListener rectTouch = new View.OnTouchListener() {

		boolean allowMove = true;

		double initialDistance = 0;
		double deltaPointerD = 0;
		double dimens = 0;
		double initialDistanceForPointer = 0;
		double actualDistanceForPointer = 0;

		public float previusLef = 0, previusTop = 0;

		FrameLayout.LayoutParams layoutParamsparm = null;

		int drw = 0;
		int drh = 0;

		int deltaLeft = 0, deltaTop = 0;
		float X = 0;
		float X1 = 0;

		float Y = 0;
		float Y1 = 0;

		int count = 0;

		PointF deltaPoint = new PointF();
		PointF startPointer0 = new PointF();
		PointF startPointer1 = new PointF();

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			X = (int) event.getRawX();
			Y = (int) event.getRawY();
			try {
				count = clip.getLayoutParams().width / clip.getLayoutParams().height;
			} catch (Exception e) {
			}
			switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					clearKeyboard();
					if (clip.textV == null) {
						rect.setRotation(0);
					}
					allowMove = true;
					setrectForDetectionX_Y();
					if (txtView != null) {
						txtView.setFocusableInTouchMode(true);
						txtView.requestFocus();
						((InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE)).showSoftInput(txtView, 0);
					}

					hideShowBorders2(View.INVISIBLE);
					hideShowBorders(View.VISIBLE);
					fixMoveButton();
					clip.invalidate();

					if (event.getPointerCount() < 2) {
						dimens = clip.getLayoutParams().width;
						FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) clip.getLayoutParams();
						layoutParamsparm = (FrameLayout.LayoutParams) clip.getLayoutParams();
						_xDelta = X - lParams.leftMargin;
						_yDelta = Y - lParams.topMargin;
					}

					deltaPoint.x = clip.getLayoutParams().width - moveBt.getLayoutParams().width;
					deltaPoint.y = clip.getLayoutParams().height - moveBt.getLayoutParams().height;
					break;
				case MotionEvent.ACTION_MOVE:
					minimumSizeCheker(count);
					fixMoveButton();
					if (allowMove) {
						clearKeyboard();
						if (event.getPointerCount() >= 2) {
							actualDistanceForPointer = (int) Math.hypot(event.getX(1) - event.getX(0), event.getY(1) - event.getY(0));////
							deltaPointerD = dimens + (actualDistanceForPointer - initialDistance);

							degres2 = (int) Math.toDegrees(Math.atan2(event.getY(0) - event.getY(1), event.getX(0) - event.getX(1)) - Math.atan2(startPointer0.y - startPointer1.y, startPointer0.x - startPointer1.x));
/*
                            degres3 = (int) Math.toDegrees(Math.atan2(event.getY(1) - event.getY(0), event.getX(1) - event.getX(0)) - Math.atan2(startPointer1.y - startPointer0.y, startPointer1.x - startPointer0.x));//////1/0
*/
							if (!movedFromMultitouch) {
								moved = false;
								clip.setRotation(degresFinal + degres2);
							} else {
								moved = false;
								clip.setRotation(degresAfterMultyTouch + degres2);
							}
							clip.refreshTextSize( clip.getLayoutParams().height - 10);
							clip.invalidate();
							background.invalidate();

							if (startPointer0.y < startPointer1.y) {
								clip.getLayoutParams().width = (int) (deltaPointerD);
								clip.getLayoutParams().height = (int) (deltaPointerD / count);
								clip.refreshTextSize( clip.getLayoutParams().height - 10);
								clip.setLayoutParams(clip.getLayoutParams());
								clip.invalidate();
								background.invalidate();
								FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) clip.getLayoutParams();
								layoutParams5.leftMargin = (int) (X - _xDelta);
								layoutParams5.topMargin = (int) (Y - _yDelta);
								clip.setLayoutParams(layoutParams5);
								minimumSizeCheker(count);
							} else {
								X1 = event.getX(1) + X - event.getX();
								Y1 = event.getY(1) + Y - event.getY();

								FrameLayout.LayoutParams layoutParams9 = (FrameLayout.LayoutParams) clip.getLayoutParams();
								layoutParams9.width = (int) (deltaPointerD);
								layoutParams9.height = (int) (deltaPointerD / count);
								clip.refreshTextSize(clip.getLayoutParams().height - 10);
								clip.setLayoutParams(layoutParams9);
								clip.invalidate();
								background.invalidate();

								FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) clip.getLayoutParams();
								layoutParams5.leftMargin = (int) (X1 - _xDelta + deltaLeft);
								layoutParams5.topMargin = (int) (Y1 - _yDelta + deltaTop);
								clip.setLayoutParams(layoutParams5);
								minimumSizeCheker(count);
							}
						}
						clip.invalidate();
						background.invalidate();
						if (event.getPointerCount() <= 1) {
							FrameLayout.LayoutParams layoutParams5 = (FrameLayout.LayoutParams) clip.getLayoutParams();
							layoutParams5.leftMargin = (int) (X - _xDelta);
							layoutParams5.topMargin = (int) (Y - _yDelta);
							clip.setLayoutParams(layoutParams5);
							minimumSizeCheker(count);
						}
					}
					clip.refreshBtn(clip.getLayoutParams().width, clip.getLayoutParams().height);
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					deltaLeft = (int) (event.getX(0) - event.getX(1));
					deltaTop = (int) (event.getY(0) - event.getY(1));
					previusLef = clip.getX();
					previusTop = clip.getY();
					startPointer0.set(event.getX(0), event.getY(0));
					startPointer1.set(event.getX(1), event.getY(1));
					if (!rectFordetection.contains(startPointer1.x + clip.getX(), startPointer1.y + clip.getY())) {
						allowMove = false;
						if (clip.textV != null) {
							allowMove = true;
						}
					}
					initialDistance = (int) Math.hypot(event.getX(1) - event.getX(0), event.getY(1) - event.getY(0));
					initialDistanceForPointer = (Math.sqrt(Math.pow(event.getX(0) - event.getX(1), 2)) + (Math.pow(event.getY(0) - event.getY(1), 2)));
					drw = ((FrameLayout.LayoutParams) clip.getLayoutParams()).leftMargin + clip.getLayoutParams().width / 2;
					drh = ((FrameLayout.LayoutParams) clip.getLayoutParams()).topMargin + clip.getLayoutParams().height / 2;

					break;
				case MotionEvent.ACTION_POINTER_UP:
					degresAfterMultyTouch = clip.getRotation();
					movedFromMultitouch = true;
					allowMove = false;
					break;
				case MotionEvent.ACTION_UP:
					allowMove = false;
					degresAfterMultyTouch = clip.getRotation();
					movedFromMultitouch = true;
					if (event.getPointerCount() == 1) {
						rect.setRotation(clip.getRotation());
						rect.getLayoutParams().width = clip.getLayoutParams().width-50;
						rect.getLayoutParams().height = clip.getLayoutParams().height-50;
						rect.setX(clip.getX()+50);////////
						rect.setY(clip.getY()+50);//////////
						rect.setLayoutParams(rect.getLayoutParams());
						rect.invalidate();
						fixMoveButton();
					}
					break;
			}
			return false;
		}
	};


	public void drop() {

		clipMoveAndRotateRelParams = new RelativeLayout.LayoutParams(clip.getMovebtnsize(), clip.getMovebtnsize());
		moveBt.setBackgroundColor(Color.TRANSPARENT);
		moveBt.setX(clip.getX() + clip.getLayoutParams().width - moveBt.getLayoutParams().width);
		moveBt.setY(clip.getY() + clip.getLayoutParams().height - moveBt.getLayoutParams().height);
		moveBt.setLayoutParams(clipMoveAndRotateRelParams);

		alfa1 = clip.getAlfa();

		rectParams = new RelativeLayout.LayoutParams(clip.getLayoutParams().width-50, clip.getLayoutParams().height-50);
		rect.setBackgroundColor(Color.TRANSPARENT);
		rect.setX(clip.getX() + 50);////////
		rect.setY(clip.getY() + 50);///////
		rect.setLayoutParams(rectParams);

		background.addView(rect);
		background.addView(moveBt);

		rect.setOnTouchListener(rectTouch);
		fixMoveButton(1);
		background.invalidate();
		clip.getBtnRemove().isFocusableInTouchMode();

	}


	View.OnTouchListener moveBtTouch = new View.OnTouchListener() {

		PointF now = new PointF();
		PointF start = new PointF();
		PointF deltaPoint = new PointF();
		double actualDistance = 0;
		double actualDistance2 = 0;
		int clipCenterX = 0;
		int clipCenterY = 0;
		double dimensR = 0;
		int w = 0, h = 0;
		boolean sized = false;
		float X = 0;
		float Y = 0;
		int count = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					clipCenterX = (int) (clip.getX() + clip.getLayoutParams().width / 2);
					clipCenterY = (int) (clip.getY() + clip.getLayoutParams().height / 2);
					try {
						count = clip.getLayoutParams().width / clip.getLayoutParams().height;
					} catch (Exception e) {
					}
					clearKeyboard();
					FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) clip.getLayoutParams();
					X = lParams.leftMargin + clip.getLayoutParams().width / 2;
					Y = lParams.topMargin + clip.getLayoutParams().height / 2;

					deltaPoint.x = clip.getLayoutParams().width - moveBt.getLayoutParams().width;
					deltaPoint.y = clip.getLayoutParams().height - moveBt.getLayoutParams().height;
					start.set(event.getX(), event.getY());

					dimensR = Math.sqrt((clip.getWidth() * clip.getWidth()) + (clip.getHeight() * clip.getHeight()));
					actualDistance2 = Math.hypot(0 - event.getX(), 0 - event.getY());
					moveBt.invalidate();
					background.invalidate();
					break;
				case MotionEvent.ACTION_MOVE:
					clearKeyboard();
					now.set(event.getX(), event.getY());
					if (!sized) {
						actualDistance = Math.hypot(0 - event.getX(), 0 - event.getY());
						actualDistance = (int) (actualDistance - actualDistance2 + dimensR * 2 / 3);
						h = ((int) (actualDistance * Math.sin(Math.toRadians(alfa1))));
						w = ((int) (actualDistance * Math.cos(Math.toRadians(alfa1))));
						clip.getLayoutParams().width = (w * 3 / 2);
						clip.getLayoutParams().height = (h * 3 / 2);
						minimumSizeCheker(count);
						FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) clip.getLayoutParams();
						layoutParams.leftMargin = (int) (X - w * 3 / 4);
						layoutParams.topMargin = (int) (Y - h * 3 / 4);
						clip.invalidate();
						background.invalidate();
						sized = true;
					} else {
						actualDistance = (int) Math.hypot((-deltaPoint.x) * 2 / 3 - event.getX(), (-deltaPoint.y) * 2 / 3 - event.getY());

						h = ((int) (actualDistance * Math.sin(Math.toRadians(alfa1))));
						w = ((int) (actualDistance * Math.cos(Math.toRadians(alfa1))));
						int wd = w * 3 / 2;
						int hd = h * 3 / 2;
						clip.getLayoutParams().width = wd;
						clip.getLayoutParams().height = hd;

						minimumSizeCheker(count);
						FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) clip.getLayoutParams();
						layoutParams.leftMargin = (int) (X - w * 3 / 4);
						layoutParams.topMargin = (int) (Y - h * 3 / 4);
					}
					clip.refreshBtn(clip.getLayoutParams().width, clip.getLayoutParams().height);
					clip.refreshTextSize(clip.getLayoutParams().height);
					clip.setLayoutParams(clip.getLayoutParams());

					int z = (int) (moveBt.getX() - clipCenterX);
					int z2 = (int) (moveBt.getY() - clipCenterY);
					degres = (int) Math.toDegrees(Math.atan2(now.y + z2, now.x + z) - Math.atan2(start.y + z2, start.x + z));

					if (!moved) {
						if (movedFromMultitouch) {
							movedFromMultitouch = false;
						} else {
							clip.setRotation(degresAfterMultyTouch + degres);
						}
					} else {
						if (movedFromMultitouch) {
							movedFromMultitouch = false;
						} else {
							clip.setRotation(degress + degres);
						}
					}
					background.invalidate();
					break;
				case MotionEvent.ACTION_UP:
					finalMoveBtnseX = clip.getX() + clip.getRlayoutWidth() - clip.getMovebtnsize();
					finalMoveBtnseY = clip.getY() + clip.getRLayoutHeight() - clip.getMovebtnsize();
					moveBt.setX(finalMoveBtnseX);
					moveBt.setY(finalMoveBtnseY);
					moveBt.setPivotX(-clip.getWidth() / 2 + moveBt.getLayoutParams().width);
					moveBt.setPivotY(-clip.getHeight() / 2 + moveBt.getLayoutParams().height);
					degresFinal = clip.getRotation();
					moveBt.setRotation(degresFinal);
					moveBt.setLayoutParams(moveBt.getLayoutParams());

					rect.setRotation(clip.getRotation());
					rect.getLayoutParams().width = clip.getLayoutParams().width-50;
					rect.getLayoutParams().height = clip.getLayoutParams().height-50;
					rect.setX(clip.getX()+50);////////////////
					rect.setY(clip.getY()+50);/////////////////
					rect.setLayoutParams(rect.getLayoutParams());
					rect.invalidate();

					background.invalidate();
					moveBt.invalidate();
					degress = clip.getRotation();
					moved = true;
					break;
			}
			return true;
		}
	};

	View.OnClickListener removeListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			if (getClipPosition(clip)>=0){
				clipsw.remove(getClipPosition(clip));
			}
			background.removeView(clip);
			background.removeView(moveBt);
			background.removeView(rect);
			background.invalidate();
		}
	};
	private int getClipPosition(Clip clip){
		int pos = -1;
		for (int i = 0; i < clipsw.size() ; i++) {
			if (clipsw.get(i).clip == clip){
				pos = i;
			}
		}
		return pos;
	}
	public void fixMoveButton(int i) {

		moveBt.setPivotX(-clip.getWidth() / 2 + moveBt.getLayoutParams().width);
		moveBt.setPivotY(-clip.getHeight() / 2 + moveBt.getLayoutParams().height);
		moveBt.setRotation(degresFinal);
		moveBt.setX(clip.getX() + clip.getBtnMove().getX() - i * clip.getBtnMove().getWidth() / 2);
		moveBt.setLayoutParams(moveBt.getLayoutParams());
		background.invalidate();
		moveBt.invalidate();


	}

	public void clearKeyboard() {
		txtView.clearFocus();
		txtView.setCursorVisible(false);

		InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(txtView.getWindowToken(), 0);
	}

	public void fixMoveButton() {
		moveBt.setX(clip.getX() + clip.getRlayoutWidth() - clip.getMovebtnsize());
		moveBt.setY(clip.getY() + clip.getRLayoutHeight() - clip.getMovebtnsize());
		moveBt.setLayoutParams(moveBt.getLayoutParams());
		moveBt.setPivotX(-clip.getWidth() / 2 + moveBt.getLayoutParams().width);
		moveBt.setPivotY(-clip.getHeight() / 2 + moveBt.getLayoutParams().height);
		moveBt.setRotation(clip.getRotation());
		moveBt.setLayoutParams(moveBt.getLayoutParams());
		background.invalidate();
		moveBt.invalidate();
	}

	public void hideShowBorders(int visibility) {

		clip.getBtnRemove().setVisibility(visibility);
		clip.getBtnMove().setVisibility(visibility);
		if(txtView!=null){
			///   TODO hide textVews  background
			////  clip.getTextView().setVisibility(visibility);
		}
		clip.hideShowBorder(visibility);
		moveBt.setVisibility(visibility);


	}

	public void hideShowBorders2(int visibility) {
		for (int i = 0; i < clipsw.size(); i++) {
			clipsw.get(i).clip.getBtnRemove().setVisibility(visibility);
			clipsw.get(i).clip.getBtnMove().setVisibility(visibility);
			clipsw.get(i).clip.hideShowBorder(visibility);
			clipsw.get(i).moveBt.setVisibility(visibility);
			if(txtView!=null){
				///   TODO hide textVews  background
				////        clipsw.get(i).clip.getTextView().setBackgroundColor();

			}

		}
	}

	public void minimumSizeCheker(int count) {
       /* if (clip.getLayoutParams().height < 100 || clip.getLayoutParams().height == 900) {
            clip.getLayoutParams().width = 110;
            clip.getLayoutParams().height = 110 / count;
            clip.refreshBtn(clip.getLayoutParams().width, clip.getLayoutParams().height);
        }*/
		if(txtView!=null){
			Log.d("TAdwdwG","dwdwdwdwdwdwd");
			if (clip.getLayoutParams().height < 30|| clip.getLayoutParams().height == 900) {
				clip.getLayoutParams().width = 40;
				clip.getLayoutParams().height = 40 / count;
				clip.refreshBtn(clip.getLayoutParams().width, clip.getLayoutParams().height);
			}else{
				if (clip.getLayoutParams().height < 100 || clip.getLayoutParams().height == 900) {
					clip.getLayoutParams().width = 110;
					clip.getLayoutParams().height = 110 / count;
					clip.refreshBtn(clip.getLayoutParams().width, clip.getLayoutParams().height);
				}

			}
		}
	}

	public void setrectForDetectionX_Y() {
		rectFordetection = new RectF();
		rectFordetection.set(clip.getX(), clip.getY(), clip.getX() + clip.getLayoutParams().width, clip.getY() + clip.getLayoutParams().height);
	}


	public void addClipToArraylist() {
		background.addView(clip);
		drop();

	}


	public View getBtnRemove() {
		return clip.getBtnRemove();
	}

	public View getBtnMove() {
		return clip.getBtnMove();
	}


	public void setArray(ArrayList<ClipView> clipslist) {
		this.clipsw = clipslist;
	}

}