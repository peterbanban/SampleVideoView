package com.jth.sample.videoview.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jth.sample.videoview.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by jth on 17-11-18.
 * 去除残疾人辅助服务
 * 修改Window,WindowManager等控制mDecor删除和添加-->控制layout_root的VISIABLE
 * 修改makeControllerView中加载自己定义layout, initControllerView方法中添加控件处理方案
 */

public class MyMediaController extends FrameLayout {

    private MediaPlayerControl mPlayer;
    private final Context mContext;
    private View mAnchor;
    private View mRoot;
    private RelativeLayout layout_root;
    //    private WindowManager mWindowManager;
//    private Window mWindow;
//    private View mDecor;
//    private WindowManager.LayoutParams mDecorLayoutParams;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private final boolean mUseFastForward;
    private boolean mFromXml;
    private boolean mListenersSet;
    private View.OnClickListener mNextListener, mPrevListener;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;

    private ImageButton mBackButton;
    private ImageButton mPauseButton;
    private ImageButton mFfwdButton;
    private ImageButton mRewButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private CharSequence mPlayDescription;
    private CharSequence mPauseDescription;
//    private final AccessibilityManager mAccessibilityManager;

    public MyMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mContext = context;
        mUseFastForward = true;
        mFromXml = true;
//        mAccessibilityManager = AccessibilityManager.getInstance(context);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (mRoot != null)
            initControllerView(mRoot);
    }

    public MyMediaController(Context context, boolean useFastForward) {
        super(context);
        mContext = context;
        mUseFastForward = useFastForward;
        initFloatingWindowLayout();
        initFloatingWindow();
//        mAccessibilityManager = AccessibilityManager.getInstance(context);
    }

    public MyMediaController(Context context) {
        this(context, true);
    }

    private void initFloatingWindow() {
        //放弃window控制控件显示
//        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        mWindow = new PhoneWindow(mContext);
//        mWindow = ((Activity) mContext).getWindow();
//        mWindow.setWindowManager(mWindowManager, null, null);
//        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
//        mDecor = mWindow.getDecorView();
//        mDecor.setOnTouchListener(mTouchListener);
//        mWindow.setContentView(this);
//        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        // While the media controller is up, the volume control keys should
        // affect the media stream type
//        mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();
    }

    // Allocate and initialize the static parts of mDecorLayoutParams. Must
    // also call updateFloatingWindowLayout() to fill in the dynamic parts
    // (y and width) before mDecorLayoutParams can be used.
    private void initFloatingWindowLayout() {
//        mDecorLayoutParams = new WindowManager.LayoutParams();
//        WindowManager.LayoutParams p = mDecorLayoutParams;
//        p.gravity = Gravity.TOP | Gravity.LEFT;
//        p.height = LayoutParams.WRAP_CONTENT;
//        p.x = 0;
//        p.format = PixelFormat.TRANSLUCENT;
//        p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
//        p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
//                | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
//        p.token = null;
//        p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;
    }

    // Update the dynamic parts of mDecorLayoutParams
    // Must be called with mAnchor != NULL.
    private void updateFloatingWindowLayout() {
//        int[] anchorPos = new int[2];
//        mAnchor.getLocationOnScreen(anchorPos);
        // we need to know the size of the controller so we can properly position it
        // within its space
//        mDecor.measure(MeasureSpec.makeMeasureSpec(mAnchor.getWidth(), MeasureSpec.AT_MOST),
//                MeasureSpec.makeMeasureSpec(mAnchor.getHeight(), MeasureSpec.AT_MOST));
//
//        WindowManager.LayoutParams p = mDecorLayoutParams;
//        p.width = mAnchor.getWidth();
//        p.x = anchorPos[0] + (mAnchor.getWidth() - p.width) / 2;
//        p.y = anchorPos[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight();
    }

    // This is called whenever mAnchor's layout bound changes
    private final OnLayoutChangeListener mLayoutChangeListener =
            new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop, int oldRight,
                                           int oldBottom) {
                    updateFloatingWindowLayout();
//                    if (mShowing) {
//                        mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);
//                    }
                }
            };

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view.
     * When VideoView calls this method, it will use the VideoView's parent
     * as the anchor.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    public void setAnchorView(View view) {
        if (mAnchor != null) {
            mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        mAnchor = view;
        if (mAnchor != null) {
            mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);
        }

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
        if (view instanceof RelativeLayout) {
            RelativeLayout anchorLayout = (RelativeLayout) mAnchor;
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            anchorLayout.removeView(this);
            anchorLayout.addView(this, relativeParams);
        } else {
            throw new RuntimeException("目前zhi只支持RelativeLayout作为父控件!");
        }
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.my_media_controller, null);
        layout_root = (RelativeLayout) mRoot.findViewById(R.id.layout_root);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View v) {
        Resources res = mContext.getResources();
        mPlayDescription = res
                .getText(R.string.lockscreen_transport_play_description);
        mPauseDescription = res
                .getText(R.string.lockscreen_transport_pause_description);
//        mBackButton = (ImageButton) v.findViewById(R.id.iv_back);
//        if (mBackButton != null) {
//            mBackButton.requestFocus();
//            mBackButton.setOnClickListener(mBackListener);
//        }


        mPauseButton = (ImageButton) v.findViewById(R.id.pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
        if (mFfwdButton != null) {
            mFfwdButton.setOnClickListener(mFfwdListener);
            if (!mFromXml) {
                mFfwdButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
            }
        }

        mRewButton = (ImageButton) v.findViewById(R.id.rew);
        if (mRewButton != null) {
            mRewButton.setOnClickListener(mRewListener);
            if (!mFromXml) {
                mRewButton.setVisibility(mUseFastForward ? View.VISIBLE : View.GONE);
            }
        }

        // By default these are hidden. They will be enabled when setPrevNextListeners() is called
        mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (mNextButton != null && !mFromXml && !mListenersSet) {
            mNextButton.setVisibility(View.GONE);
        }
        mPrevButton = (ImageButton) v.findViewById(R.id.prev);
        if (mPrevButton != null && !mFromXml && !mListenersSet) {
            mPrevButton.setVisibility(View.GONE);
        }

        mProgress = (SeekBar) v.findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.time);
        mCurrentTime = (TextView) v.findViewById(R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        installPrevNextListeners();
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
            if (mRewButton != null && !mPlayer.canSeekBackward()) {
                mRewButton.setEnabled(false);
            }
            if (mFfwdButton != null && !mPlayer.canSeekForward()) {
                mFfwdButton.setEnabled(false);
            }
            // TODO What we really should do is add a canSeek to the MediaPlayerControl interface;
            // this scheme can break the case when applications want to allow seek through the
            // progress bar but disable forward/backward buttons.
            //
            // However, currently the flags SEEK_BACKWARD_AVAILABLE, SEEK_FORWARD_AVAILABLE,
            // and SEEK_AVAILABLE are all (un)set together; as such the aforementioned issue
            // shouldn't arise in existing applications.
            if (mProgress != null && !mPlayer.canSeekBackward() && !mPlayer.canSeekForward()) {
                mProgress.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            disableUnsupportedButtons();
            updateFloatingWindowLayout();
//            mWindowManager.addView(mDecor, mDecorLayoutParams);
            if (layout_root != null) {
                Log.e("123", "显示");
                layout_root.setVisibility(VISIBLE);
            }
            Log.e("123", "");
            mShowing = true;
        }
        updatePausePlay();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        post(mShowProgress);

        if (timeout != 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null)
            return;

        if (mShowing) {
            try {
                removeCallbacks(mShowProgress);
//                mWindowManager.removeView(mDecor);
                if (layout_root != null) {
                    Log.w("123", "隐藏");
                    layout_root.setVisibility(GONE);
                }
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private final Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final Runnable mShowProgress = new Runnable() {
        @Override
        public void run() {
            int pos = setProgress();
            if (!mDragging && mShowing && mPlayer.isPlaying()) {
                postDelayed(mShowProgress, 1000 - (pos % 1000));
            }
        }
    };

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                show(0); // show until hide is called
//                break;
//            case MotionEvent.ACTION_UP:
//                show(sDefaultTimeout); // start timeout
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                hide();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (mPauseButton != null) {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer.isPlaying()) {
                mPlayer.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        }
        //这里没有清空掉view，只是隐藏，所以不能屏蔽了返回键
//        else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
//            if (uniqueDown) {
//                hide();
//            }
//            return true;
//        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }


    private final View.OnClickListener mBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).finish();
            }
        }
    };


    private final View.OnClickListener mPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.mipmap.ic_media_video_pause);
            mPauseButton.setContentDescription(mPauseDescription);
        } else {
            mPauseButton.setImageResource(R.mipmap.ic_media_video_play);
            mPauseButton.setContentDescription(mPlayDescription);
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);
            mDragging = true;
            removeCallbacks(mShowProgress);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime((int) newposition));
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            // Ensure that progress is properly updated in the future,
            // the call to show() does not guarantee this because it is a
            // no-op if we are already showing.
            post(mShowProgress);
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mFfwdButton != null) {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null) {
            mRewButton.setEnabled(enabled);
        }
        if (mNextButton != null) {
            mNextButton.setEnabled(enabled && mNextListener != null);
        }
        if (mPrevButton != null) {
            mPrevButton.setEnabled(enabled && mPrevListener != null);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    private final View.OnClickListener mRewListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mPlayer.getCurrentPosition();
            pos -= 5000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private final View.OnClickListener mFfwdListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mPlayer.getCurrentPosition();
            pos += 15000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };

    private void installPrevNextListeners() {
        if (mNextButton != null) {
            mNextButton.setOnClickListener(mNextListener);
            mNextButton.setEnabled(mNextListener != null);
        }

        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(mPrevListener);
            mPrevButton.setEnabled(mPrevListener != null);
        }
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {
        mNextListener = next;
        mPrevListener = prev;
        mListenersSet = true;

        if (mRoot != null) {
            installPrevNextListeners();

            if (mNextButton != null && !mFromXml) {
                mNextButton.setVisibility(View.VISIBLE);
            }
            if (mPrevButton != null && !mFromXml) {
                mPrevButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface MediaPlayerControl {
        void start();

        void pause();

        int getDuration();

        int getCurrentPosition();

        void seekTo(int pos);

        boolean isPlaying();

        int getBufferPercentage();

        boolean canPause();

        boolean canSeekBackward();

        boolean canSeekForward();

        /**
         * Get the audio session id for the player used by this VideoView. This can be used to
         * apply audio effects to the audio track of a video.
         *
         * @return The audio session, or 0 if there was an error.
         */
        int getAudioSessionId();
    }
}
