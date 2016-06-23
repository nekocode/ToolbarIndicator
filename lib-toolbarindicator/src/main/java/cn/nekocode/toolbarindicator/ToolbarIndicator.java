package cn.nekocode.toolbarindicator;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import static android.support.v4.view.ViewPager.OnPageChangeListener;


public class ToolbarIndicator extends LinearLayout implements OnPageChangeListener {

    private final static int DEFAULT_INDICATOR_WIDTH = 3;
    private final static int TEXT_SIZE = 20;
    private ViewPager mViewpager;
    private int mIndicatorMargin;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mAnimatorResId = R.animator.scale_with_alpha;
    private int mAnimatorReverseResId = -1;
    private int mIndicatorBackground = R.drawable.white_radius;
    private int mIndicatorUnselectedBackground = R.drawable.white_radius;
    private int mCurrentPosition = 0;
    private Animator mAnimationOut;
    private Animator mAnimationIn;
    private String title[];
    private TextPaint textPaint;
    private int scrollX;

    public ToolbarIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public ToolbarIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        handleTypedArray(context, attrs);
        mAnimationOut = AnimatorInflater.loadAnimator(context, mAnimatorResId);
        if (mAnimatorReverseResId == -1) {
            mAnimationIn = AnimatorInflater.loadAnimator(context, mAnimatorResId);
            mAnimationIn.setInterpolator(new ReverseInterpolator());
        } else {
            mAnimationIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId);
        }

        setPadding(0, dip2px(TEXT_SIZE + 8), 0, 0);
        setBackgroundColor(0x00000000);

        textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(dip2px(TEXT_SIZE));
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.ToolbarIndicator);
            mIndicatorWidth =
                    typedArray.getDimensionPixelSize(R.styleable.ToolbarIndicator_ti_width, -1);
            mIndicatorHeight =
                    typedArray.getDimensionPixelSize(R.styleable.ToolbarIndicator_ti_height, -1);
            mIndicatorMargin =
                    typedArray.getDimensionPixelSize(R.styleable.ToolbarIndicator_ti_margin, -1);

            mAnimatorResId = typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_animator,
                    R.animator.scale_with_alpha);
            mAnimatorReverseResId =
                    typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_animator_reverse, -1);
            mIndicatorBackground = typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_drawable,
                    R.drawable.white_radius);
            mIndicatorUnselectedBackground =
                    typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_drawable_unselected,
                            mIndicatorBackground);
            typedArray.recycle();
        }
        mIndicatorWidth =
                (mIndicatorWidth == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorWidth;
        mIndicatorHeight =
                (mIndicatorHeight == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorHeight;
        mIndicatorMargin =
                (mIndicatorMargin == -1) ? dip2px(DEFAULT_INDICATOR_WIDTH) : mIndicatorMargin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int center = width/2;
        int x, y;
        int alpha;
        int alphaOffsetXMax = (int) (center * 1.0f);

        float ratio = (scrollX * 1.0f) / mViewpager.getWidth();

        for(int i =0; i<title.length; i++) {
            x = (int) (i * width - ratio * width + center);
            y = (int) (getHeight()*0.5f + dip2px(TEXT_SIZE + 8)*0.25f);
            int alphaOffsetX = Math.abs(x-center);

            if(alphaOffsetX > alphaOffsetXMax) {
                alpha = 0;
            } else {
                alpha = (int) ((1.0f - ((alphaOffsetX * 1.0f) / alphaOffsetXMax)) * 255);
            }
            textPaint.setAlpha(alpha);
            canvas.drawText(title[i], x, y, textPaint);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        mViewpager = viewPager;
        mCurrentPosition = mViewpager.getCurrentItem();
        createIndicators(viewPager);
        mViewpager.addOnPageChangeListener(this);
        onPageSelected(mCurrentPosition);

        this.title = new String[viewPager.getAdapter().getCount()];
        for(int i=0; i<title.length; i++) {
            title[i] = String.valueOf(viewPager.getAdapter().getPageTitle(i));
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        scrollX = position * mViewpager.getWidth() + positionOffsetPixels;
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        if (mAnimationIn.isRunning()) mAnimationIn.cancel();
        if (mAnimationOut.isRunning()) mAnimationOut.cancel();

        View currentIndicator = getChildAt(mCurrentPosition);
        currentIndicator.setBackgroundResource(mIndicatorUnselectedBackground);
        mAnimationIn.setTarget(currentIndicator);
        mAnimationIn.start();

        View selectedIndicator = getChildAt(position);
        selectedIndicator.setBackgroundResource(mIndicatorBackground);
        mAnimationOut.setTarget(selectedIndicator);
        mAnimationOut.start();

        mCurrentPosition = position;
    }

    @Override public void onPageScrollStateChanged(int state) {
    }

    private void createIndicators(ViewPager viewPager) {
        removeAllViews();
        int count = viewPager.getAdapter().getCount();
        if (count <= 0) {
            return;
        }

        addIndicator(mIndicatorBackground);

        for (int i = 1; i < count; i++) {
            addIndicator(mIndicatorUnselectedBackground);
        }

        mAnimationOut.setTarget(getChildAt(mCurrentPosition));
        mAnimationOut.start();
    }

    private void addIndicator(@DrawableRes int backgroundDrawableId) {
        View Indicator = new View(getContext());
        Indicator.setBackgroundResource(backgroundDrawableId);
        addView(Indicator, mIndicatorWidth, mIndicatorHeight);
        LayoutParams lp = (LayoutParams) Indicator.getLayoutParams();
        lp.leftMargin = mIndicatorMargin;
        lp.rightMargin = mIndicatorMargin;
        Indicator.setLayoutParams(lp);
        mAnimationOut.setTarget(Indicator);
        mAnimationOut.start();
    }

    private class ReverseInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float value) {
            return Math.abs(1.0f - value);
        }
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
