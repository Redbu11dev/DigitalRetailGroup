/**
 * Copyright 2016 Ali Muzaffar
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vashkpi.digitalretailgroup.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.view.ViewCompat;

import com.vashkpi.digitalretailgroup.R;

public class PinEditText extends AppCompatEditText {
    private static final String XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";

    public static final String DEFAULT_MASK = "\u25CF";

    protected String mMask = null;
    protected StringBuilder mMaskChars = null;
    protected String mSingleCharHint = null;
    protected int mAnimatedType = 0;
    protected float mSpace = 0; //24 dp by default, space between chars
    protected float mCharWidth;
    protected float charCount = 4;
    protected int mMaxLength = 4;
    protected RectF[] mLineCoords;
    protected float[] mCharBottom;
    protected Paint mCharPaint;
    protected Paint mLastCharPaint;
    protected Paint mSingleCharPaint;
    protected Drawable mPinBackground;
    protected Rect mTextHeight = new Rect();

    protected OnClickListener mClickListener;
    protected OnPinEnteredListener mOnPinEnteredListener = null;

    protected float mLineStroke = 1; //1dp by default
    protected float mLineStrokeSelected = 2; //2dp by default
    protected Paint mLinesPaint;
    protected boolean mAnimate = false;
    protected boolean mHasError = false;
    protected ColorStateList mOriginalTextColors;

    public PinEditText(Context context) {
        super(context);
    }

    public PinEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PinEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setMaxLength(final int maxLength) {
        mMaxLength = maxLength;
        charCount = maxLength;

        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        setText(null);
        invalidate();
    }

    public void setMask(String mask) {
        mMask = mask;
        mMaskChars = null;
        invalidate();
    }

    public void setSingleCharHint(String hint) {
        mSingleCharHint = hint;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        float multi = context.getResources().getDisplayMetrics().density;
        mLineStroke = multi * mLineStroke;
        mLineStrokeSelected = multi * mLineStrokeSelected;
        mSpace = multi * mSpace; //convert to pixels for our density

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PinEditText, 0, 0);
        try {
            TypedValue outValue = new TypedValue();
            ta.getValue(R.styleable.PinEditText_pinAnimationType, outValue);
            mAnimatedType = outValue.data;
            mMask = ta.getString(R.styleable.PinEditText_pinCharacterMask);
            mSingleCharHint = ta.getString(R.styleable.PinEditText_pinRepeatedHint);
            mLineStroke = ta.getDimension(R.styleable.PinEditText_pinLineStroke, mLineStroke);
            mLineStrokeSelected = ta.getDimension(R.styleable.PinEditText_pinLineStrokeSelected, mLineStrokeSelected);
            mSpace = ta.getDimension(R.styleable.PinEditText_pinCharacterSpacing, mSpace);
            mPinBackground = ta.getDrawable(R.styleable.PinEditText_pinBackgroundDrawable);
        } finally {
            ta.recycle();
        }

        mCharPaint = new Paint(getPaint());
        mCharPaint.setTextAlign(Paint.Align.CENTER);
        mLastCharPaint = new Paint(getPaint());
        mLastCharPaint.setTextAlign(Paint.Align.CENTER);
        mSingleCharPaint = new Paint(getPaint());
        mSingleCharPaint.setTextAlign(Paint.Align.CENTER);
        mLinesPaint = new Paint(getPaint());
        mLinesPaint.setStrokeWidth(mLineStroke);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorControlActivated,
                outValue, true);

        setBackgroundResource(0);

        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 4);
        charCount = mMaxLength;

        //Disable copy paste
        super.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        // When tapped, move cursor to end of text.
        super.setOnClickListener(v -> {
            setSelection(getText().length());
            if (mClickListener != null) {
                mClickListener.onClick(v);
            }
        });

        super.setOnLongClickListener(v -> {
            setSelection(getText().length());
            return true;
        });

        //If input type is password and no mask is set, use a default mask
        if ((getInputType() & InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD && TextUtils.isEmpty(mMask)) {
            mMask = DEFAULT_MASK;
        } else if ((getInputType() & InputType.TYPE_NUMBER_VARIATION_PASSWORD) == InputType.TYPE_NUMBER_VARIATION_PASSWORD && TextUtils.isEmpty(mMask)) {
            mMask = DEFAULT_MASK;
        }

        if (!TextUtils.isEmpty(mMask)) {
            mMaskChars = getMaskChars();
        }

        //Height of the characters, used if there is a background drawable
        getPaint().getTextBounds("|", 0, 1, mTextHeight);

        mAnimate = mAnimatedType > -1;
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);

        if ((type & InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD
                || (type & InputType.TYPE_NUMBER_VARIATION_PASSWORD) == InputType.TYPE_NUMBER_VARIATION_PASSWORD) {
            // If input type is password and no mask is set, use a default mask
            if (TextUtils.isEmpty(mMask)) {
                setMask(DEFAULT_MASK);
            }
        } else {
            // If input type is not password, remove mask
            setMask(null);
        }
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mLastCharPaint.setColor(color);
        mCharPaint.setColor(color);
        //mSingleCharPaint.setColor(color);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mOriginalTextColors = getTextColors();
        if (mOriginalTextColors != null) {
            mLastCharPaint.setColor(mOriginalTextColors.getDefaultColor());
            mCharPaint.setColor(mOriginalTextColors.getDefaultColor());
            mSingleCharPaint.setColor(getCurrentHintTextColor());
        }
        int availableWidth = getWidth() - ViewCompat.getPaddingEnd(this) - ViewCompat.getPaddingStart(this);
        if (mSpace < 0) {
            mCharWidth = (availableWidth / (charCount * 2 - 1));
        } else {
            mCharWidth = (availableWidth - (mSpace * (charCount - 1))) / charCount;
        }
        mLineCoords = new RectF[(int) charCount];
        mCharBottom = new float[(int) charCount];
        int startX;
        int bottom = getHeight() - getPaddingBottom();
        int rtlFlag;
        final boolean isLayoutRtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
        if (isLayoutRtl) {
            rtlFlag = -1;
            startX = (int) (getWidth() - ViewCompat.getPaddingStart(this) - mCharWidth);
        } else {
            rtlFlag = 1;
            startX = ViewCompat.getPaddingStart(this);
        }
        for (int i = 0; i < charCount; i++) {
            mLineCoords[i] = new RectF(startX, bottom, startX + mCharWidth, bottom);
            if (mPinBackground != null) {
                mLineCoords[i].top = 0;
            }

            if (mSpace < 0) {
                startX += rtlFlag * mCharWidth * 2;
            } else {
                startX += rtlFlag * (mCharWidth + mSpace);
            }
            mCharBottom[i] = mLineCoords[i].bottom;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int measuredWidth = 0;
            int measuredHeight = 0;

            // Если указана конкретная ширина, растягиваем высоту. Как дефолтно ведёт себя TextView
            if (widthMode == MeasureSpec.EXACTLY) {
                measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
                measuredHeight = (int) ((measuredWidth - (charCount - 1 * mSpace)) / charCount);
            } else if (heightMode == MeasureSpec.EXACTLY) {
                measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
                measuredWidth = (int) ((measuredHeight * charCount) + (mSpace * charCount - 1));
            } else if (widthMode == MeasureSpec.AT_MOST) {
                measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
                measuredHeight = (int) ((measuredWidth - (charCount - 1 * mSpace)) / charCount);
            } else if (heightMode == MeasureSpec.AT_MOST) {
                measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
                measuredWidth = (int) ((measuredHeight * charCount) + (mSpace * charCount - 1));
            } else {
                // Both unspecific
                // Try for a width based on our minimum
                measuredWidth = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();

                // Whatever the width ends up being, ask for a height that would let the pie
                // get as big as it can
                measuredHeight = (int) ((measuredWidth - (charCount - 1 * mSpace)) / charCount);
            }

            setMeasuredDimension(
                    resolveSizeAndState(measuredWidth, widthMeasureSpec, 1), measuredHeight);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        throw new RuntimeException("setCustomSelectionActionModeCallback() not supported.");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        CharSequence text = getFullText();
        int textLength = text.length();
        float[] textWidths = new float[textLength];
        getPaint().getTextWidths(text, 0, textLength, textWidths);

        float hintWidth = 0;
        if (mSingleCharHint != null) {
            float[] hintWidths = new float[mSingleCharHint.length()];
            getPaint().getTextWidths(mSingleCharHint, hintWidths);
            for (float i : hintWidths) {
                hintWidth += i;
            }
        }
        for (int i = 0; i < charCount; i++) {
            //If a background for the pin characters is specified, it should be behind the characters.
            if (mPinBackground != null) {
                updateDrawableState(i < textLength, i == textLength);
                mPinBackground.setBounds((int) mLineCoords[i].left, (int) mLineCoords[i].top, (int) mLineCoords[i].right, (int) mLineCoords[i].bottom);
                mPinBackground.draw(canvas);
            }
            float middleWidth = (mLineCoords[i].left + mLineCoords[i].right) / 2;
            if (textLength > i) {
                if (!mAnimate || i != textLength - 1) {
                    canvas.drawText(text, i, i + 1, middleWidth, getTextCenterHeight(mLineCoords[i].bottom, mCharPaint), mCharPaint);
                } else {
                    canvas.drawText(text, i, i + 1, middleWidth, getTextCenterHeight(mLineCoords[i].bottom, mLastCharPaint), mLastCharPaint);
                }
            } else if (mSingleCharHint != null) {
                canvas.drawText(mSingleCharHint, middleWidth, getTextCenterHeight(mLineCoords[i].bottom, mSingleCharPaint), mSingleCharPaint);
            }
        }
    }

    private float getTextCenterHeight(float height, Paint paint) {
        return (height - paint.ascent() - paint.descent()) / 2;
    }

    private CharSequence getFullText() {
        if (TextUtils.isEmpty(mMask)) {
            return getText();
        } else {
            return getMaskChars();
        }
    }

    private StringBuilder getMaskChars() {
        if (mMaskChars == null) {
            mMaskChars = new StringBuilder();
        }
        int textLength = getText().length();
        while (mMaskChars.length() != textLength) {
            if (mMaskChars.length() < textLength) {
                mMaskChars.append(mMask);
            } else {
                mMaskChars.deleteCharAt(mMaskChars.length() - 1);
            }
        }
        return mMaskChars;
    }

    protected void updateDrawableState(boolean hasText, boolean isNext) {
        if (mHasError) {
            mPinBackground.setState(new int[]{android.R.attr.state_active});
        } else if (isFocused()) {
            mPinBackground.setState(new int[]{android.R.attr.state_focused});
            if (isNext) {
                mPinBackground.setState(new int[]{android.R.attr.state_focused, android.R.attr.state_selected});
            } else if (hasText) {
                mPinBackground.setState(new int[]{android.R.attr.state_focused, android.R.attr.state_checked});
            }
        } else {
            if (hasText) {
                mPinBackground.setState(new int[]{-android.R.attr.state_focused, android.R.attr.state_checked});
            } else {
                mPinBackground.setState(new int[]{-android.R.attr.state_focused});
            }
        }
    }

    public void setError(boolean hasError) {
        mHasError = hasError;
        invalidate();
    }

    public boolean isError() {
        return mHasError;
    }

    /**
     * Request focus on this PinEditText
     */
    public void focus() {
        requestFocus();

        // Show keyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(this, 0);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf) {
        super.setTypeface(tf);
        setCustomTypeface(tf);
    }

    @Override
    public void setTypeface(@Nullable Typeface tf, int style) {
        super.setTypeface(tf, style);
        setCustomTypeface(tf);
    }

    private void setCustomTypeface(@Nullable Typeface tf) {
        if (mCharPaint != null) {
            mCharPaint.setTypeface(tf);
            mLastCharPaint.setTypeface(tf);
            mSingleCharPaint.setTypeface(tf);
            mLinesPaint.setTypeface(tf);
        }
    }

    public void setPinBackground(Drawable pinBackground) {
        mPinBackground = pinBackground;
        invalidate();
    }

    @Override
    protected void onTextChanged(CharSequence text, final int start, int lengthBefore, final int lengthAfter) {
        setError(false);
        if (mLineCoords == null || !mAnimate) {
            if (mOnPinEnteredListener != null && text.length() == mMaxLength) {
                mOnPinEnteredListener.onPinEntered(text);
            }
            return;
        }

        if (mAnimatedType == -1) {
            invalidate();
            return;
        }

        if (lengthAfter > lengthBefore) {
            if (mAnimatedType == 0) {
                animatePopIn();
            } else {
                animateBottomUp(text, start);
            }
        }
    }

    private void animatePopIn() {
        ValueAnimator va = ValueAnimator.ofFloat(1, getPaint().getTextSize());
        va.setDuration(200);
        va.setInterpolator(new OvershootInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLastCharPaint.setTextSize((Float) animation.getAnimatedValue());
                PinEditText.this.invalidate();
            }
        });
        if (getText().length() == mMaxLength && mOnPinEnteredListener != null) {
            va.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mOnPinEnteredListener.onPinEntered(getText());
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        va.start();
    }

    private void animateBottomUp(CharSequence text, final int start) {
        mCharBottom[start] = mLineCoords[start].bottom;
        ValueAnimator animUp = ValueAnimator.ofFloat(mCharBottom[start] + getPaint().getTextSize(), mCharBottom[start]);
        animUp.setDuration(300);
        animUp.setInterpolator(new OvershootInterpolator());
        animUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                mCharBottom[start] = value;
                PinEditText.this.invalidate();
            }
        });

        mLastCharPaint.setAlpha(255);
        ValueAnimator animAlpha = ValueAnimator.ofInt(0, 255);
        animAlpha.setDuration(300);
        animAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mLastCharPaint.setAlpha(value);
            }
        });

        AnimatorSet set = new AnimatorSet();
        if (text.length() == mMaxLength && mOnPinEnteredListener != null) {
            set.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mOnPinEnteredListener.onPinEntered(getText());
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        set.playTogether(animUp, animAlpha);
        set.start();
    }

    public void setAnimateText(boolean animate) {
        mAnimate = animate;
    }

    public void setOnPinEnteredListener(OnPinEnteredListener l) {
        mOnPinEnteredListener = l;
    }

    public interface OnPinEnteredListener {
        void onPinEntered(CharSequence str);
    }
}