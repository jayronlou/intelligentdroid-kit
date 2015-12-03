package com.intelligentdroid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 猜比分demo
 * 
 * @author jayronlou
 * 
 */
public class CaibifenView extends View {
	/** 预测平分的emoji */
	private String equallyColor = "#ffb900";
	private Paint equallyPaint = new Paint();
	/** 预测胜分的颜色 */
	private String winColor = "#ff4900";
	private Paint winPaint = new Paint();

	/** 预测负分的颜色 */
	private String loseColor = "#0ae098";

	private String whiteColor = "#ffffff";

	private Paint losePaint = new Paint();
	private Paint whitePaint = new Paint();
	/** 宽度 */
	private float width;
	/** 高度 */
	private float height;
	private float paddingTop = 100;
	private float paddingBottom = 100;
	/** 直径 */
	private float diameter = 0;

	public CaibifenView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CaibifenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CaibifenView(Context context) {
		super(context);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		equallyPaint.setColor(Color.parseColor(equallyColor));
		equallyPaint.setAntiAlias(true);
		winPaint.setColor(Color.parseColor(winColor));
		winPaint.setAntiAlias(true);
		losePaint.setColor(Color.parseColor(loseColor));
		losePaint.setAntiAlias(true);

		whitePaint.setColor(Color.parseColor(whiteColor));
		whitePaint.setAntiAlias(true);

		equallyPaint.setStyle(Style.FILL);
		losePaint.setStyle(Style.FILL);
		whitePaint.setStyle(Style.FILL);
	}

	float finalLength;
	RectF oval;

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		width = getWidth();
		height = getHeight();
		Log.e("width===", width + "");
		Log.e("height===", height + "");
		Log.e("中心点", "x=" + width / 2 + ";y=" + height / 2);
		finalLength = (width > height ? height : width);
		Log.e("finalLength===", height + "");
		diameter = ((finalLength - paddingBottom - paddingTop) / 2);
		Log.e("diameter===", diameter + "");
		oval = new RectF(width / 2 - diameter, height / 2 - diameter, width / 2
				+ diameter, height / 2 + diameter);// 设置个新的长方形，扫描测量
		Log.e("oval.toString()", oval.toString());
		/******************** 直线方程 *************************/
		// a ＝ y-x = height/2-width/2;
		/******************** 直线方程 *************************/
		invalidate();
	}

	// private int litleAngle = 30;
	/** 小圆的半径 */
	private float littleCircleAngle = 10;

	public float equallyRate = 30;
	public float loseRate = 30;
	public float winRate = 40;

	private int lineWidth = 150;
	private int lineHeight = 30;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (width != 0 && height != 0 && oval != null) {
			/******************** 负 ********************************/
			float loseStartAngle = -loseRate / 100 * 360 / 2 - 45;// 开始的角度
			float loseSweetAngle = loseRate / 100 * 360;// 扫描的角度

			canvas.drawArc(oval, loseStartAngle, loseSweetAngle, true,
					losePaint);
			/******************** 负 ********************************/

			/******************** 胜 ********************************/
			float winstartAngle = -loseRate / 100 * 360 / 2 - 45 + loseRate
					/ 100 * 360;// 开始的角度
			float winsweetAngle = winRate / 100 * 360;// 扫描的角度
			canvas.drawArc(oval, winstartAngle, winsweetAngle, true, winPaint);
			/******************** 胜 ********************************/

			/******************** 平 ********************************/
			float equalityartAngle = -loseRate / 100 * 360 / 2 - 45 + loseRate
					/ 100 * 360 + winRate / 100 * 360;// 开始的角度
			float equalitywinsweetAngle = equallyRate / 100 * 360;// 扫描的角度
			canvas.drawArc(oval, equalityartAngle, equalitywinsweetAngle, true,
					equallyPaint);
			/******************** 平 ********************************/

			/******************** 画三个小圆 ********************************/
			// 负
			littleCircleAngle = diameter / 4;
			float loseCx = (float) (width / 2 + diameter
					* Math.cos(-45 * Math.PI / 180));
			float loseCy = (float) (height / 2 + diameter
					* Math.sin(-45 * Math.PI / 180));

			canvas.drawCircle(loseCx, loseCy, littleCircleAngle, whitePaint);
			canvas.drawCircle(loseCx, loseCy, littleCircleAngle - 5, losePaint);

			// 划线

			// 负

			// 胜
			float centerJiaodu = -(float) ((float) (loseRate / 100f) * 360f / 2f)
					- 45
					+ (float) (loseRate / 100f)
					* 360f
					+ (float) (winRate / 100f * 360f / 2f);
			loseCx = (float) (width / 2 + diameter
					* Math.cos(centerJiaodu * Math.PI / 180));

			loseCy = (float) (height / 2 + diameter
					* Math.sin(centerJiaodu * Math.PI / 180));
			canvas.drawCircle(loseCx, loseCy, littleCircleAngle, whitePaint);
			canvas.drawCircle(loseCx, loseCy, littleCircleAngle - 5, winPaint);

			
			
			/********************** 画线 *****************************/
			winPaint.setStrokeWidth(5);
			
			canvas.drawLine(loseCx, loseCy, loseCx - lineWidth*2, loseCy,
					winPaint);
			canvas.drawLine(loseCx - lineWidth*2, loseCy, loseCx - lineWidth*2,
					loseCy -lineHeight, winPaint);
			canvas.drawCircle(loseCx - lineWidth*2, loseCy - lineHeight, 10,winPaint);
			/********************** 画线 *****************************/
			
			/********************** 画字体 *****************************/
			winPaint.setTextSize(40);
			int stringWidth = getStringWidth(winPaint, "34人选择");
			int stringHeight = getStringHeight(winPaint, "34人选择");
			canvas.drawText("34人选择", loseCx - lineWidth*2-stringWidth/2, loseCy - lineHeight-stringHeight, winPaint);
			/********************** 画字体 *****************************/
			// 胜
			
			
			
			
			
			
			/********************** 画圆 *****************************/
			centerJiaodu = -loseRate / 100 * 360 / 2 - 45 + loseRate / 100
					* 360 + winRate / 100 * 360 + equallyRate / 100 * 360 / 2;
			loseCx = (float) (width / 2 + diameter
					* Math.cos(centerJiaodu * Math.PI / 180));
			loseCy = (float) (height / 2 + diameter
					* Math.sin(centerJiaodu * Math.PI / 180));
			canvas.drawCircle(loseCx, loseCy, littleCircleAngle, whitePaint);
			canvas.drawCircle(loseCx, loseCy, littleCircleAngle - 5,
					equallyPaint);
			/********************** 画圆 *****************************/
			
			/********************** 画线 *****************************/
			equallyPaint.setStrokeWidth(5);
			canvas.drawLine(loseCx, loseCy, loseCx - lineWidth, loseCy,
					equallyPaint);
			canvas.drawLine(loseCx - lineWidth, loseCy, loseCx - lineWidth,
					loseCy + lineHeight, equallyPaint);
			canvas.drawCircle(loseCx - lineWidth, loseCy + lineHeight, 10,
					equallyPaint);
			/********************** 画线 *****************************/

			/********************** 画字体 *****************************/
			equallyPaint.setTextSize(40);
			stringWidth = getStringWidth(equallyPaint, "12人选择");
			stringHeight = getStringHeight(equallyPaint, "12人选择");
			canvas.drawText("12人选择", loseCx - lineWidth-stringWidth/2, loseCy + lineHeight+stringHeight-10, equallyPaint);
			/********************** 画字体 *****************************/
			
			
			canvas.drawCircle(width/2, height/2, diameter/2, whitePaint);

		}

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()== MotionEvent.ACTION_DOWN){
			float x2 = event.getX();
			float y1 = event.getY();
			float he = Math.abs(x2-width/2)*Math.abs(x2-width/2)+Math.abs(y1-height/2)*Math.abs(y1-height/2);
			if(he<=(diameter/2*diameter/2)){
				Log.e("位置＝＝＝", "在区域内");
			}
		}
		return super.onTouchEvent(event);
	}
	
	private int getStringWidth(Paint paint, String str) {
		return (int) paint.measureText(str);
	}

	private int getStringHeight(Paint paint, String str) {
		FontMetrics fr = paint.getFontMetrics();
		return (int) Math.ceil(fr.descent - fr.top) + 2; // ceil()
															// 函数向上舍入为最接近的整数。
	}
}
