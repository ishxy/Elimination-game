package com.shxy.xiaoxiaole;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by caolu on 2017/3/6.
 */

public class GameBack extends RelativeLayout implements View.OnClickListener {

    private ImageView[][] mImageItems;
    private Bitmap[] mAllBitmaps;
    private Info[][] mInfos;


    private int mWidth;
    private int maxLine = 9;
    private int mPadding = 3;
    private int mMargin = 3;
    private boolean once = true;
    private int mItemWidth;
    private boolean isAnimation = false;
    private RelativeLayout mAnimationLayout;

    private static final int DROP_TIME = 1000;
    private static final int CHANGE_TIME = 300;
    private static final int MESSAGE_TIME = 100;
    private static final int BEGIN_TIME = 500;

    public GameBack(Context context) {
        this(context, null);
    }

    public GameBack(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameBack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAllBitmaps();
        initAnimationLayout();
        handler.sendEmptyMessageDelayed(START_MESSAGE, BEGIN_TIME);
    }

    //初始化动画布局
    private void initAnimationLayout() {
        mAnimationLayout = new RelativeLayout(getContext());
        //重要！！  RelativeLayout 默认包裹内容
        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mAnimationLayout, lp);
    }

    private static final int START_MESSAGE = 0x00;
    private static final int RESET_IMAGE = 0x01;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_MESSAGE:
                    if (checkAllImageView()) {
                        isAnimation = true;
                        setChangeTempImage();
                    } else {
                        isAnimation = false;
                    }
                    break;
                case RESET_IMAGE:
                    setVisiable();
                    break;
            }
        }
    };

    private void test() {
        for (int i = 0; i < maxLine; i++) {
            Log.i("shxy - state = ", mInfos[i][0].imageType + " " + mInfos[i][1].imageType + " " + mInfos[i][2].imageType
                    + " " + mInfos[i][3].imageType + " " + mInfos[i][4].imageType + " " + mInfos[i][5].imageType + " "
                    + mInfos[i][6].imageType + " " + mInfos[i][7].imageType + " " + mInfos[i][8].imageType + " "
            );
        }
    }

    //设置所有图片
    private void initAllBitmaps() {
        mAllBitmaps = new Bitmap[maxLine];
        mAllBitmaps[0] = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        mAllBitmaps[1] = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
        mAllBitmaps[2] = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
        mAllBitmaps[3] = BitmapFactory.decodeResource(getResources(), R.drawable.img4);
        mAllBitmaps[4] = BitmapFactory.decodeResource(getResources(), R.drawable.img5);
        mAllBitmaps[5] = BitmapFactory.decodeResource(getResources(), R.drawable.img6);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);
        mWidth = Math.min(widthSize, heightSize);
        if (once) {
            mItemWidth = (mWidth - mPadding * 2 - mMargin * (maxLine - 1)) / (maxLine);
            initItems();
            once = false;
        }
        setMeasuredDimension(mWidth, mWidth);
    }

    /*private int xx[][] = {
            {1, 0, 3, 3, 2, 0, 4, 4, 0},
            {0, 1, 3, 0, 0, 0, 1, 1, 1},
            {4, 0, 1, 5, 0, 2, 3, 3, 4},
            {1, 2, 0, 0, 4, 2, 0, 0, 3},
            {3, 3, 1, 2, 1, 1, 1, 1, 1},
            {1, 5, 3, 3, 3, 3, 0, 1, 2},
            {4, 2, 4, 1, 0, 3, 3, 1, 4},
            {5, 5, 1, 2, 3, 3, 0, 4, 3},
            {3, 4, 1, 4, 0, 3, 2, 0, 1}
    };*/

    private int xx[][] = {
            {2, 1, 1, 3, 4, 1, 1, 4, 5},
            {4, 1, 0, 3, 3, 3, 3, 5, 4},
            {0, 1, 3, 2, 4, 2, 3, 5, 1},
            {0, 2, 1, 5, 5, 0, 4, 0, 1},
            {0, 0, 3, 3, 4, 4, 5, 1, 1},
            {1, 1, 0, 0, 5, 3, 1, 2, 4},
            {2, 2, 1, 1, 2, 1, 0, 4, 1},
            {0, 3, 1, 3, 0, 3, 3, 1, 2},
            {3, 1, 3, 3, 2, 1, 2, 1, 0},};

    //初始化imageview 和 info
    private void initItems() {
        mImageItems = new ImageView[maxLine][maxLine];
        mInfos = new Info[maxLine][maxLine];
        int width = mItemWidth;
        for (int i = 0; i < maxLine; i++) {
            for (int j = 0; j < maxLine; j++) {
                int pos = getRadomInteger();
                mInfos[i][j] = new Info();
                mInfos[i][j].imageType = xx[i][j];
                // mInfos[i][j].imageType = pos;
                RelativeLayout.LayoutParams lp = new LayoutParams(width, width);
                ImageView img = new ImageView(getContext());
                img.setScaleType(ImageView.ScaleType.FIT_XY);
                img.setImageBitmap(mAllBitmaps[xx[i][j]]);
                //img.setImageBitmap(mAllBitmaps[pos]);
                img.setId(i * maxLine + j + 1);
                img.setOnClickListener(this);
                img.setTag(new Point(i, j));
                mImageItems[i][j] = img;
                final int finalI = i;
                final int finalJ = j;
                if (j != maxLine - 1) {
                    lp.rightMargin = mMargin;
                }
                if (i != 0) {
                    lp.topMargin = mMargin;
                }
                if (j != 0) {
                    lp.addRule(RelativeLayout.RIGHT_OF, mImageItems[i][j - 1].getId());
                }
                if (i != 0) {
                    lp.addRule(RelativeLayout.BELOW, mImageItems[i - 1][j].getId());
                }

                addView(mImageItems[i][j], lp);
            }
        }
        test();
    }

    private ImageView mFirstView = null, mSecondView = null;
    private String HEIGHT_COLOR = "#77ff0000";

    @Override
    public void onClick(View v) {
        if (isAnimation) {
            return;
        }
        if (mFirstView == null) {
            mFirstView = (ImageView) v;
            mFirstView.setColorFilter(Color.parseColor(HEIGHT_COLOR));
        } else if (mFirstView == v) {
            mFirstView.setColorFilter(null);
            mFirstView = null;
        } else {
            mSecondView = (ImageView) v;
            if (!canChange()) {
                mFirstView.setColorFilter(null);
                mFirstView = (ImageView) v;
                mFirstView.setColorFilter(Color.parseColor(HEIGHT_COLOR));
                return;
            }
            /**
             * 是相邻的块，移动逻辑
             */
            moveTwoImage();
        }
    }


    private void moveTwoImage() {
        test();
        Log.i("shxy - state", "before");
        //先假设移动，后处理情况
        changeTowViewInfo();

        /**
         * 开启不可交换动画
         */
        if (!checkAllImageView()) {
            test();

            mFirstView.setVisibility(INVISIBLE);
            mSecondView.setVisibility(INVISIBLE);
            ImageView first = copyAnimationImageView(mFirstView,mSecondView);
            ImageView second = copyAnimationImageView(mSecondView,mFirstView);
            TranslateAnimation a1 = new TranslateAnimation(-mPadding * 2, mSecondView.getLeft() - mFirstView.getLeft(), -mPadding * 2, mSecondView.getTop() - mFirstView.getTop());
            a1.setRepeatMode(TranslateAnimation.REVERSE);
            a1.setDuration(CHANGE_TIME);
            a1.setRepeatCount(1);
            TranslateAnimation a2 = new TranslateAnimation(-mPadding * 2, mFirstView.getLeft() - mSecondView.getLeft(), -mPadding * 2, mFirstView.getTop() - mSecondView.getTop());
            a2.setDuration(CHANGE_TIME);
            a2.setRepeatMode(TranslateAnimation.REVERSE);
            a2.setRepeatCount(1);
            first.startAnimation(a1);
            second.startAnimation(a2);
            a2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimation = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnimation = false;
                    mFirstView.setVisibility(VISIBLE);
                    mSecondView.setVisibility(VISIBLE);
                    mAnimationLayout.removeAllViews();
                    mFirstView.setColorFilter(null);

                    Log.i("shxy - state", "after");
                    changeTowViewInfo();
                    mFirstView = null;
                    mSecondView = null;
                    test();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        } else {
            /**
             * 开启可交换动画，并处理交换后的逻辑
             */

            mFirstView.setVisibility(INVISIBLE);
            mSecondView.setVisibility(INVISIBLE);
            ImageView first = copyAnimationImageView(mFirstView,mSecondView);
            ImageView second = copyAnimationImageView(mSecondView,mFirstView);
            TranslateAnimation a1 = new TranslateAnimation(-mPadding * 2, mSecondView.getLeft() - mFirstView.getLeft() - mPadding*2, -mPadding * 2, mSecondView.getTop() - mFirstView.getTop()- mPadding*2);
            a1.setRepeatMode(TranslateAnimation.REVERSE);
            a1.setDuration(CHANGE_TIME);
            TranslateAnimation a2 = new TranslateAnimation(-mPadding * 2, mFirstView.getLeft() - mSecondView.getLeft()- mPadding*2, -mPadding * 2, mFirstView.getTop() - mSecondView.getTop()- mPadding*2);
            a2.setDuration(CHANGE_TIME);
            a2.setRepeatMode(TranslateAnimation.REVERSE);
            first.startAnimation(a1);
            second.startAnimation(a2);
            a2.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnimation = true;
                    mFirstView.setColorFilter(null);

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Point firstPoint = (Point) mFirstView.getTag();
                    Point secondPoint = (Point) mSecondView.getTag();
                    mFirstView.setImageBitmap(mAllBitmaps[mInfos[firstPoint.x][firstPoint.y].imageType]);
                    mSecondView.setImageBitmap(mAllBitmaps[mInfos[secondPoint.x][secondPoint.y].imageType]);
                    mFirstView.setVisibility(VISIBLE);
                    mSecondView.setVisibility(VISIBLE);
                    mFirstView = null;
                    mSecondView = null;
                    mAnimationLayout.removeAllViews();
                    test();
                    resetAllInfo();
                    handler.sendEmptyMessageDelayed(START_MESSAGE,MESSAGE_TIME);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
    private ImageView copyAnimationImageView(ImageView sourse) {
        return copyAnimationImageView(sourse,sourse);
    }

    private ImageView copyAnimationImageView(ImageView sourse,ImageView bitmapSourse) {
        ImageView target = new ImageView(getContext());
        Point p = (Point) sourse.getTag();
        target.setScaleType(ImageView.ScaleType.FIT_XY);
        Point point = (Point) bitmapSourse.getTag();
        target.setImageBitmap(mAllBitmaps[mInfos[point.x][point.y].imageType]);
        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = sourse.getLeft() - mPadding;
        lp.topMargin = sourse.getTop() - mPadding;
        mAnimationLayout.addView(target, lp);
        return target;
    }


    //点击的俩个按键是否是相邻的
    private boolean canChange() {
        Point p1 = (Point) mFirstView.getTag();
        Point p2 = (Point) mSecondView.getTag();
        if (p1.x != p2.x && p1.y != p2.y) {
            return false;
        }
        if (p1.x == p2.x) {
            if (p1.y + 1 == p2.y) {
                return true;
            } else if (p1.y - 1 == p2.y) {
                return true;
            } else {
                return false;
            }
        } else {
            if (p1.x + 1 == p2.x) {
                return true;
            } else if (p1.x - 1 == p2.x) {
                return true;
            } else {
                return false;
            }

        }
    }

    private void changeTowViewInfo() {
        /**
         * 出现过错误
         */
        Point p1 = (Point) mFirstView.getTag();
        Point p2 = (Point) mSecondView.getTag();
        Info temp = mInfos[p1.x][p1.y];
        mInfos[p1.x][p1.y] = mInfos[p2.x][p2.y];
        mInfos[p2.x][p2.y] = temp;
    }

    //检查所有imageview状态
    private boolean checkAllImageView() {
        boolean have = false;
        for (int i = maxLine - 1; i >= 0; i--) {
            for (int j = 0; j < maxLine; j++) {
                if (eliminate(i, j, mInfos[i][j].imageType)) {
                    have = true;
                    Log.i("shxy - state", "i = " + i + "  j = " + j);
                    for (int x = i - 1; x >= 0; x--) {
                        mInfos[x][j].needChange = true;
                        mInfos[x][j].dowmTime++;
                    }
                    /**
                     * 防止顶部消除时列总共消除计算出现问题
                     */
                    if (i == 0) {
                        mInfos[0][j].dowmTime++;
                    }
                    mInfos[i][j].isTarget = true;
                    mInfos[i][j].needChange = true;
                }
            }
        }
        return have;
    }

    //设置将要交换的临时图片
    private void setChangeTempImage() {
        totalAnimation = 0;
        for (int i = 0; i < maxLine; i++) {
            for (int j = 0; j < maxLine; j++) {
                if (mInfos[i][j].needChange) {
                    if (!mInfos[i][j].isTarget) {
                        totalAnimation++;
                    }
                    mImageItems[i][j].setVisibility(View.INVISIBLE);
                    int max = mInfos[0][j].dowmTime;
                    if (i < max) {
                        mInfos[i][j].tempType = getRadomInteger();
                    }
                    if (!mInfos[i][j].isTarget) {
                        mInfos[i + mInfos[i][j].dowmTime][j].tempType = mInfos[i][j].imageType;
                    }
                }
            }
        }
        /**
         * 防止顶部三个消除时没有动画而导致产生空白
         */
        if (totalAnimation == 0) {
            isAnimation = false;
            setVisiable();
        } else {
            setAndStartAnimation();
        }
    }

    private int totalAnimation = 0;
    private int nowAnimation = 0;


    /**
     * 设置并启动动画
     * 重点检查
     */
    private void setAndStartAnimation() {
        nowAnimation = 0;
        for (int i = 0; i < maxLine; i++) {
            for (int j = 0; j < maxLine; j++) {
                final Info info = mInfos[i][j];
                ImageView sourse = mImageItems[i][j];
                if (info.needChange && !info.isTarget) {
                    ImageView img = new ImageView(getContext());
                    img.setScaleType(ImageView.ScaleType.FIT_XY);
                    img.setImageBitmap(mAllBitmaps[info.imageType]);
                    LayoutParams params = new LayoutParams(mItemWidth, mItemWidth);
                    Log.i("shxy - if", mItemWidth + "");
                    params.leftMargin = sourse.getLeft() - mPadding;
                    params.topMargin = sourse.getTop() - mPadding;
                    mAnimationLayout.addView(img, params);
                    DropAnimation d = new DropAnimation(-mPadding * 2, -mPadding * 2, 0, (sourse.getBottom() - sourse.getTop()) * (info.dowmTime));
                    d.setDuration(DROP_TIME);
                    img.startAnimation(d);
                    d.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            nowAnimation++;
                            if (nowAnimation == totalAnimation) {
                                handler.sendEmptyMessageDelayed(RESET_IMAGE, MESSAGE_TIME);
                                mAnimationLayout.removeAllViews();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }
        }
    }

    //设置可见状态
    private void setVisiable() {
        for (int i = 0; i < maxLine; i++) {
            for (int j = 0; j < maxLine; j++) {
                Info info = mInfos[i][j];
                ImageView img = mImageItems[i][j];
                if (info.needChange) {

                    img.setVisibility(VISIBLE);
                    /**
                     * 该行多次调用后出现问题
                     */
                    img.setImageBitmap(mAllBitmaps[mInfos[i][j].tempType]);
                    reset(info);
                }
            }
        }
        handler.sendEmptyMessageDelayed(START_MESSAGE, MESSAGE_TIME);
    }

    private void reset(Info info) {
        info.needChange = false;
        info.isTarget = false;
        info.imageType = info.tempType;
        info.tempType = -1;
        info.dowmTime = 0;

    }
    private void resetAllInfo(){
        for (int i =0;i<maxLine;i++){
            for (int j =0;j<maxLine;j++){
                mInfos[i][j].needChange = false;
                mInfos[i][j].isTarget = false;
                mInfos[i][j].tempType = -1;
                mInfos[i][j].dowmTime = 0;
            }
        }
    }

    private int getRadomInteger() {
        return (int) ((Math.random() * 10) % 6);
    }

    private boolean eliminate(int x, int y, int type) {
        return hEliminate(x, y, type) || vEliminate(x, y, type);
    }

    private static final int ELIMINATE_NUM = 3;

    private boolean vEliminate(int x, int y, int type) {
        int sum = 1;
        int row = x;
        while (--x >= 0) {
            if (mInfos[x][y].imageType == type) {
                sum++;
            } else {
                break;
            }
        }
        x = row;
        while (++x < maxLine) {
            if (mInfos[x][y].imageType == type) {
                sum++;
            } else {
                break;
            }
        }
        if (sum >= 3) {
            return true;
        }
        return false;
    }

    private boolean hEliminate(int x, int y, int type) {
        int sum = 1;
        int row = y;

        while (--y >= 0) {
            if (mInfos[x][y].imageType == type) {
                sum++;
            } else {
                break;
            }
        }
        y = row;
        while (++y < maxLine) {
            if (mInfos[x][y].imageType == type) {
                sum++;
            } else {
                break;
            }
        }
        if (sum >= 3) {
            return true;
        }
        return false;
    }

    //存储信息
    private class Info {
        public int dowmTime = 0;
        public boolean needChange = false;
        public boolean isTarget = false;
        public int imageType = -1;
        public int tempType = -1;
    }

    //动画类
    private class DropAnimation extends TranslateAnimation {

        public DropAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
            super(fromXDelta, toXDelta, fromYDelta, toYDelta);
            setDuration(500);
            setFillAfter(true);
        }
    }
}
