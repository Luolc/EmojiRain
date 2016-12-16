package com.luolc.emojirain;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.percent.PercentFrameLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pools;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.luolc.emojirain.utils.Randoms;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Emoji rain animates in this layout.
 *
 * @author LuoLiangchen
 * @since 2016/12/13
 */

public class EmojiRainLayout extends PercentFrameLayout {

    private static int EMOJI_STANDARD_SIZE;

    private static final float RELATIVE_DROP_DURATION_OFFSET = 0.25F;

    private static final int DEFAULT_PER = 6;

    private static final int DEFAULT_DURATION = 8000;

    private static final int DEFAULT_DROP_DURATION = 2400;

    private static final int DEFAULT_DROP_FREQUENCY = 500;

    private CompositeSubscription mSubscriptions;

    private int mWindowHeight;

    private int mEmojiPer;

    private int mDuration;

    private int mDropAverageDuration;

    private int mDropFrequency;

    private List<Drawable> mEmojis;

    private Pools.SynchronizedPool<ImageView> mEmojiPool;

    {
        EMOJI_STANDARD_SIZE = dip2px(36);
    }

    public EmojiRainLayout(Context context) {
        this(context, null);
    }

    public EmojiRainLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiRainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) init(context, attrs);
    }

    public void setPer(int per) {
        mEmojiPer = per;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setDropDuration(int dropDuration) {
        mDropAverageDuration = dropDuration;
    }

    public void setDropFrequency(int frequency) {
        mDropFrequency = frequency;
    }

    public void addEmoji(Bitmap emoji) {
        mEmojis.add(new BitmapDrawable(getResources(), emoji));
    }

    public void addEmoji(Drawable emoji) {
        mEmojis.add(emoji);
    }

    public void addEmoji(@DrawableRes int resId) {
        mEmojis.add(ContextCompat.getDrawable(getContext(), resId));
    }

    public void clearEmojis() {
        mEmojis.clear();
    }

    /**
     * Stop dropping animation after all emojis in the screen currently
     * dropping out of the screen.
     */
    public void stopDropping() {
        mSubscriptions.clear();
    }

    /**
     * Start dropping animation.
     * The animation will last for n flow(s), which n is {@code mDuration}
     * divided by {@code mDropFrequency}.
     * The interval between two flows is {@code mDropFrequency}.
     * There will be {@code mEmojiPer} emojis dropping in each flow.
     * The dropping animation for a specific emoji is a random value with mean
     * {@code mDropAverageDuration} and relative offset {@code RELATIVE_DROP_DURATION_OFFSET}.
     */
    public void startDropping() {
        initEmojisPool();

        Randoms.setSeed(7);
        mWindowHeight = getWindowHeight();
        Subscription subscription = Observable.interval(mDropFrequency, TimeUnit.MILLISECONDS)
                .take(mDuration / mDropFrequency)
                .flatMap(flow -> Observable.range(0, mEmojiPer))
                .map(i -> mEmojiPool.acquire())
                .filter(e -> e != null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::startDropAnimationForSingleEmoji, Throwable::printStackTrace);
        mSubscriptions.add(subscription);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EmojiRainLayout);

        mSubscriptions = new CompositeSubscription();
        mEmojis = new ArrayList<>();
        mEmojiPer = ta.getInteger(R.styleable.EmojiRainLayout_per, DEFAULT_PER);
        mDuration = ta.getInteger(R.styleable.EmojiRainLayout_duration, DEFAULT_DURATION);
        mDropAverageDuration = ta.getInteger(R.styleable.EmojiRainLayout_dropDuration,
                DEFAULT_DROP_DURATION);
        mDropFrequency = ta.getInteger(R.styleable.EmojiRainLayout_dropFrequency,
                DEFAULT_DROP_FREQUENCY);

        ta.recycle();
    }

    private void startDropAnimationForSingleEmoji(final ImageView emoji) {
        final TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, Randoms.floatAround(0, 5),
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.ABSOLUTE, mWindowHeight);
        translateAnimation.setDuration((int)
                (mDropAverageDuration * Randoms.floatAround(1, RELATIVE_DROP_DURATION_OFFSET)));
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mEmojiPool.release(emoji);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        emoji.startAnimation(translateAnimation);
    }

    private int getWindowHeight() {
        final WindowManager windowManager = ((WindowManager) getContext().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point.y;
    }

    private void initEmojisPool() {
        final int emojiTypeCount = mEmojis.size();
        if (emojiTypeCount == 0) throw new IllegalStateException("There are no emojis");

        clearDirtyEmojisInPool();
        final int expectedMaxEmojiCountInScreen =
                (int) ((1 + RELATIVE_DROP_DURATION_OFFSET)
                        * mEmojiPer
                        * mDropAverageDuration
                        / ((float) mDropFrequency));
        mEmojiPool = new Pools.SynchronizedPool<>(expectedMaxEmojiCountInScreen);
        for (int i = 0; i < expectedMaxEmojiCountInScreen; i++) {
            final ImageView emoji = generateEmoji(mEmojis.get(i % emojiTypeCount));
            addView(emoji, 0);
            mEmojiPool.release(emoji);
        }
    }

    private ImageView generateEmoji(Drawable emojiDrawable) {
        ImageView emoji = new ImageView(getContext());
        emoji.setImageDrawable(emojiDrawable);
        final int width = (int) (EMOJI_STANDARD_SIZE * (1.0 + Randoms.positiveGaussian()));
        final int height = (int) (EMOJI_STANDARD_SIZE * (1.0 + Randoms.positiveGaussian()));
        final LayoutParams params = new LayoutParams(width, height);
        params.getPercentLayoutInfo().leftMarginPercent = Randoms.floatStandard();
        params.topMargin = -height;
        params.leftMargin = ((int) (-0.5F * width));
        emoji.setLayoutParams(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            emoji.setElevation(100);
        }
        return emoji;
    }

    private void clearDirtyEmojisInPool() {
        if (mEmojiPool != null) {
            ImageView dirtyEmoji;
            while ((dirtyEmoji = mEmojiPool.acquire()) != null) removeView(dirtyEmoji);
        }
    }

    private int dip2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }
}
