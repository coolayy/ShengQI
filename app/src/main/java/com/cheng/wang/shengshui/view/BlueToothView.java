package com.cheng.wang.shengshui.view;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng.wang.shengshui.aty.BlueToothGameAty;
import com.cheng.wang.shengshui.utils.Constant;
import com.cheng.wang.shengshui.utils.Utils;
import com.cheng.wang.shengshuiqi.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

import static com.cheng.wang.shengshui.utils.Constant.ground;
import static com.cheng.wang.shengshui.utils.Constant.isnoPlaySound;

public class BlueToothView extends SurfaceView implements Runnable,SurfaceHolder.Callback{
    BlueToothGameAty father;
    private Canvas canvas;
    private SurfaceHolder sfh;
    private Thread th = new Thread(this);


    protected static int GRID_SIZE = 2;    //设置为国际标准
    protected static int GRID_WIDTH = 36; // 棋盘格的宽度
    protected static int CHESS_DIAMETER = 37; // 棋的直径
    protected static int mStartX;// 棋盘定位的左上角X
    protected static int mStartY;// 棋盘定位的左上角Y

    //    private static int[][] mGridArray; // 网格
    private Stack<String> storageArray;


    int wbflag = 1; //该下白棋了=2，该下黑棋了=1. 这里先下黑棋（黑棋以后设置为机器自动下的棋子）
    int mLevel = 1; //游戏难度
    int mWinFlag = 0;
    private final int BLACK = 1;
    private final int WHITE = 2;
    private Paint paint1;

    private boolean WHITE_FLAG ;
    private Bitmap whiteMap;
    private Bitmap blackMap;
    ;
    boolean focus=false;
    int selectqizi=0;
    int startI,startJ;
    int endI,endJ;
    private int whoWin = 0;// 0没有，1白色win，2黑色win
    private boolean isStop = false;
    private boolean tiaoqi=false;





    //private TextView mStatusTextView; //  根据游戏状态设置显示的文字
    private TextView mStatusTextView; //  根据游戏状态设置显示的文字

    private Bitmap btm1;
    private final Paint mPaint = new Paint();

    CharSequence mText;
    CharSequence STRING_WIN = "白棋赢啦!  ";
    CharSequence STRING_LOSE = "黑棋赢啦!  ";
    CharSequence STRING_EQUAL = "和棋！  ";

    public BlueToothView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.father=(BlueToothGameAty) context;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        sfh = this.getHolder();
        sfh.addCallback((SurfaceHolder.Callback) this);

        init();
    }

    //按钮监听器
    MyButtonListener myButtonListener;
    public void setActionCallbak(BlueToothGameAty blueToothGameAty) {
        this.blueToothGameAty = blueToothGameAty;
    }
    private  BlueToothGameAty blueToothGameAty;

    // 初始化黑白棋的Bitmap
    public void init() {

//        storageArray = new Stack<>();
        myButtonListener = new MyButtonListener();
//        wbflag = WHITE; //初始为先下黑棋
        mWinFlag = 0; //清空输赢标志。
//        mGridArray = new int[GRID_SIZE - 1][GRID_SIZE - 1];



        paint1=new Paint();
        paint1.setColor(Color.YELLOW);
        paint1.setTextSize(28);
        paint1.setAntiAlias(true);

        Bitmap bitmap = Bitmap.createBitmap(CHESS_DIAMETER, CHESS_DIAMETER, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Resources r = this.getContext().getResources();
        whiteMap = BitmapFactory.decodeResource(r, R.drawable.human);
        blackMap = BitmapFactory.decodeResource(r, R.drawable.ai);
//        renren=BitmapFactory.decodeResource(r,R.drawable.renrengame_background);

    }

    //设置显示的textView
    public void setTextView(TextView tv) {
        mStatusTextView = tv;
        //mStatusTextView.setVisibility(View.INVISIBLE);
    }

    //悔棋按钮
    private Button huiqi;
    //刷新那妞
    private Button refresh;

    //设置两个按钮
    public void setButtons(Button huiqi, Button refresh) {
        this.huiqi = huiqi;
        this.refresh = refresh;

        huiqi.setOnClickListener(myButtonListener);
        refresh.setOnClickListener(myButtonListener);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mStartX = w / 2 - GRID_SIZE * 6*GRID_WIDTH / 2;
        mStartY = h / 2 - GRID_SIZE *6* GRID_WIDTH / 2;
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        th.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Utils.initGroup();
        isStop = true;
    }


    /**
     * 点下出现棋子
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isStop) {
                for (int y = 0; y < 5; y++) {
                    for (int x = 0; x < 5; x++) {

                        if (isLegal(x,y)&&isInCircle(event.getX(), event.getY(), x, y)) {
                            if (Utils.hewin()) {
                                father.playSound(4,1);
                                mText = STRING_LOSE;
                                showTextView(mText);
                                blueToothGameAty.onPutChess("BLACK");
//                                isStop=true;
//												whoWin = 2;
                                Toast.makeText(getContext(), "黑赢了", Toast.LENGTH_SHORT).show();
                            }
                            if (Utils.baiwin()){
                                father.playSound(4,1);
                                mText = STRING_WIN;
                                showTextView(mText);
                                blueToothGameAty.onPutChess("WHITE");

//                                isStop=true;
//											 whoWin=1;
                                Toast.makeText(getContext(), "白赢了", Toast.LENGTH_SHORT).show();
                            }


                            if (WHITE_FLAG) {
                                int i = -1, j = -1;
                                int[] pos = getpos(event);
                                i = pos[0];
                                j = pos[1];
                                if (focus == false&&isLegal(i,j)) {//之前没有选中棋子
                                    if (Constant.ground[y][x] != 0) {//有棋子
                                        if (Constant.ground[y][x] == 1) {//点击的是自己的棋子

//                                            selectqizi = ground[i][j];
                                            focus = true;
                                            startI = i;
                                            startJ = j;
                                        }
                                    }
                                } else {//之前选中棋子
                                    if (Constant.ground[y][x] != 0) {//点击有棋子
                                        if (Constant.ground[y][x] == 1) {//是自己的棋子
//                                            selectqizi = ground[i][j];
                                            startI = i;
                                            startJ = j;
                                        }

                                    } else {//点击的位置没有棋子


                                        endI = i;
                                        endJ = j;
                                        WHITE_FLAG = true;
                                        if (isLegal(startI,startJ)&&isLegal(endI,endJ)&&Math.abs(endI - startI) <= 1 && Math.abs(endJ - startJ) <= 1
                                                || (Math.abs(endI - startI) == 2 && Math.abs(endJ - startJ) == 0 && (startJ == 0 || startJ == 4))) {
//
                                            if (endI == 2 && endJ == 0 && (startI == 1 && startJ == 1 || startI == 3 && startJ == 1)) {
                                                WHITE_FLAG = true;
                                                continue;
                                            } else if (endI == 2 && endJ == 4 && (startI == 1 && startJ == 3 || startI == 3 && startJ == 3)) {
                                                WHITE_FLAG = true;
                                                continue;
                                            } else if (startI == 2 && startJ == 0 && (endI == 1 && endJ == 1 || endI == 3 && endJ == 1)) {
                                                WHITE_FLAG = true;
                                                continue;
                                            } else if (startI == 2 && startJ == 4 && (endI == 1 && endJ == 3 || endI == 3 && endJ == 3)) {
                                                WHITE_FLAG = true;
                                                continue;
                                            }

                                            ground[endJ][endI] = 1;
                                            ground[startJ][startI] = 0;
                                            String temp = startI + ":" + startJ+ ":" +endI + ":"+endJ;
                                            //通过回调方法通知Activity下棋动作
                                            blueToothGameAty.onPutChess(temp);
                                            father.playSound(2,1);

                                            startI = -1;
                                            startJ = -1;
                                            endI = -1;
                                            endJ = -1;//还原保存点
                                            focus = false;//标记买选中棋子
                                            WHITE_FLAG = false;
                                        } else {
                                            WHITE_FLAG = true;
                                        }

                                    }
                                }

                            } else {
                                int i = -1, j = -1;
                                int[] pos = getpos(event);
                                i = pos[0];
                                j = pos[1];
                                if (focus == false&&isLegal(i,j)) {
                                    if (Constant.ground[y][x] != 0) {
                                        if (Constant.ground[y][x] == 2) {

//                                            selectqizi = ground[i][j];
                                            focus = true;
                                            startI = i;
                                            startJ = j;
                                        }
                                    }
                                } else {
                                    if (Constant.ground[y][x] != 0) {
                                        if (Constant.ground[y][x] == 2) {
//                                            selectqizi = ground[i][j];
                                            focus = true;
                                            startI = i;
                                        }

                                    } else {//点击的位置没有棋子

//										if (Utils.dulegal()) {
                                        endI = i;
                                        endJ = j;
                                        WHITE_FLAG = false;

                                        if ((isLegal(startI,startJ)&&isLegal(endI,endJ)&&Math.abs(endI - startI) <= 1 && Math.abs(endJ - startJ) <= 1)
                                                || (Math.abs(endI - startI) == 2 && Math.abs(endJ - startJ) == 0 && (startJ == 0 || startJ == 4))) {

                                            if (endI == 2 && endJ == 0 && (startI == 1 && startJ == 1 || startI == 3 && startJ == 1)) {
                                                WHITE_FLAG = false;
                                                continue;
                                            } else if (endI == 2 && endJ == 4 && (startI == 1 && startJ == 3 || startI == 3 && startJ == 3)) {
                                                WHITE_FLAG = false;
                                                continue;
                                            } else if (startI == 2 && startJ == 0 && (endI == 1 && endJ == 1 || endI == 3 && endJ == 1)) {
                                                WHITE_FLAG = false;
                                                continue;
                                            } else if (startI == 2 && startJ == 4 && (endI == 1 && endJ == 3 || endI == 3 && endJ == 3)) {
                                                WHITE_FLAG = false;
                                                continue;
                                            }

                                            ground[endJ][endI] = 2;
                                            ground[startJ][startI] = 0;
                                            String temp = startI + ":" + startJ + ":" + endI + ":" + endJ;
                                            //通过回调方法通知Activity下棋动作
                                            blueToothGameAty.onPutChess(temp);
                                            father.playSound(2, 1);


                                            startI = -1;
                                            startJ = -1;
                                            endI = -1;
                                            endJ = -1;//还原保存点
                                            focus = false;//标记买选中棋子
                                            WHITE_FLAG = true;
                                        }
                                        else if (isLegal(startI,startJ)&&isLegal(endI,endJ)&&(ground[startJ + (endJ - startJ) / 2][startI + (endI - startI) / 2] == 1)) {
                                            if (Math.abs(endI - startI) == 4 && Math.abs(endJ - startJ) == 4 && startJ + (endJ - startJ) / 2 == startI + (endI - startI) / 2) {
                                                continue;
                                            }

                                            ground[endJ][endI] = 2;
                                            ground[startJ][startI] = 0;
                                            String temp= startI + ":" + startJ+ ":" +endI + ":"+endJ;
                                            //通过回调方法通知Activity下棋动作
                                            blueToothGameAty.onPutChess(temp);
                                            father.playSound(2,1);
                                            ground[startJ + (endJ - startJ) / 2][startI + (endI - startI) / 2] = 0;
                                            tiaoqi=true;
                                            startI = -1;
                                            startJ = -1;
                                            endI = -1;
                                            endJ = -1;//还原保存点
                                            focus = false;//标记买选中棋子
                                            WHITE_FLAG = true;
                                        }
                                    }
                                }
                            }

                            return super.onTouchEvent(event);
                        }

                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }


    public int [] getpos(MotionEvent event){

        int []pos=new int[2];
        double x=event.getX();
        double y=event.getY();
        if (x>mStartX-GRID_WIDTH*2&&x<mStartY+14*GRID_WIDTH&y>mStartY-2*GRID_WIDTH&&y<mStartY+14*GRID_WIDTH){
            pos[0]= (int) Math.round ((x-mStartX)/(3*GRID_WIDTH));
            pos[1]= (int) Math.round((y-mStartY)/(3*GRID_WIDTH));
        }else {
            pos[0]=-1;
            pos[1]=-1;
        }
        return pos;
    }

    // 判断是否与某点最近
    private boolean isInCircle(float touch_x, float touch_y, int x, int y) {

        return ((touch_x - x  * 3* GRID_WIDTH-mStartX)
                * (touch_x - x * 3 * GRID_WIDTH-mStartX) + (touch_y - y*3 * GRID_WIDTH-mStartY)
                * (touch_y - y * 3 * GRID_WIDTH-mStartY))<2*GRID_WIDTH*GRID_WIDTH;
    }

    public void draw() {
        try {
            canvas = sfh.lockCanvas();
            Paint paintBackground = new Paint();
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.chess_background);
            canvas.drawBitmap(bitmap, null, new Rect(mStartX - 2 * GRID_WIDTH, mStartY - 2 * GRID_WIDTH, mStartX + 6 * GRID_WIDTH * GRID_SIZE + 2 * GRID_WIDTH, mStartY + 6 * GRID_WIDTH * GRID_SIZE + 2 * GRID_WIDTH), paintBackground);
            // 画棋盘
            Paint paintRect = new Paint();
            paintRect.setColor(Color.BLACK);
            paintRect.setStrokeWidth(2);
            paintRect.setStyle(Style.STROKE);


            // 横线
            for (int i = 0; i <5; i++) {
                if (i==0||i==4){
                    canvas.drawLine(mStartX, 3* GRID_WIDTH * i
                            +mStartY, mStartX+12 * GRID_WIDTH, 3
                            * GRID_WIDTH * i + mStartY, paintRect);}
                if (i==1||i==3){
                    canvas.drawLine(mStartX+3*GRID_WIDTH, 3* GRID_WIDTH * i
                            +mStartY, mStartX+9*GRID_WIDTH, 3
                            * GRID_WIDTH * i + mStartY, paintRect);
                }


            }
            int j=2;
            canvas.drawLine(mStartX + 3 * GRID_WIDTH * j,
                    mStartY, mStartX + 3
                            * GRID_WIDTH * j,mStartY+12 *GRID_WIDTH,
                    paintRect);
            canvas.drawLine(mStartX,
                    mStartY,mStartX+12*GRID_WIDTH,12*GRID_WIDTH +mStartY, paintRect);
            canvas.drawLine(mStartX,
                    mStartY+12*GRID_WIDTH,mStartX+12*GRID_WIDTH, mStartY, paintRect);


            for (int y = 0; y <5; y += 1) {
                for (int x = 0; x < 5; x += 1) {
//					if ((x==1&&y==0)||(x==3&&y==0)||(x==0&&y==1)||(x==4&&y==1)|| (x==0&&y==2)||(x==1&&y==2)||(x==3&&y==2)
//							||(x==4&&y==2)||(x==0&&y==3)||(x==4&&y==3)||(x==1&&y==4)||(x==3&&y==4)){
//						continue;
//
//					}
                    if (Constant.ground[y][x] != 0)
                        drawMyBitmap(x, y);
                    if (focus){
                        drawFocus();
                    }

                }// 这里加入判断是否有人获胜

            }
            //画棋盘的外边框
            paintRect.setStrokeWidth(4);
            canvas.drawRect(mStartX - 2 * GRID_WIDTH, mStartY - 2 * GRID_WIDTH, mStartX + 6 * GRID_WIDTH * GRID_SIZE + 2 * GRID_WIDTH, mStartY + GRID_WIDTH * 6 * GRID_SIZE + 2 * GRID_WIDTH, paintRect);


        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            if (canvas != null) {
                sfh.unlockCanvasAndPost(canvas);
            }
        }
    }

    public static boolean isLegal(int x, int y) {
        if (((y==0||y==4)&&(x==0||x==2||x==4))||
                ((y==1||y==3)&&x>=1&&x<=3)||
                (y==2&&x==2)){
            return true;
        }
        return false;
    }

    // 以某点为中心，画图片上去
    private void drawMyBitmap(int x, int y) {

        if (Constant.ground[y][x] ==1)
            canvas.drawBitmap(whiteMap, (x*3 * GRID_WIDTH+mStartX)
                    - 2*Constant.CHESS_R, (y*3*GRID_WIDTH+mStartY)
                    - 2*Constant.CHESS_R, null);
        else if (ground[y][x]==2){
            canvas.drawBitmap(blackMap, (x*3 * GRID_WIDTH+mStartX)
                    - 2*Constant.CHESS_R, (y*3* GRID_WIDTH+mStartY)
                    - 2*Constant.CHESS_R, null);
        }
    }

    private void drawFocus() {
        if (focus) {
            canvas.drawCircle(startI * 3 *GRID_WIDTH+ mStartX, startJ * 3 * GRID_WIDTH + mStartY, 18, paint1);
        }
    }

    public void putChess(int x, int y, int blackwhite) {
        ground[x][y] = blackwhite;
        String temp = x + ":" + y;
//        storageArray.push(temp);
        //通过回调方法通知Activity下棋动作
        blueToothGameAty.onPutChess(temp + ":" + blackwhite);
        invalidate();
    }
    //收到对方传来的棋子
    public void xiaqi(String command) {
        if ((command == null)) {
            return;
        }
        if ((command.equals("HUIQI"))) {


            isnoPlaySound = !isnoPlaySound;
        } else if ((command.equals("REFRESH"))) {
            setVisibility(View.VISIBLE);
            mStatusTextView.invalidate();
            Utils.initGroup();
            init();
            invalidate();
            for (int i = 0; i < showtime.length; i++) {
                showtime[i] = 0;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
            mStatusTextView.setText("蓝牙对战模式 佳计：" + simpleDateFormat.format(new Date()));
        } else if ((command.equals("WHITE"))) {
            mStatusTextView.setText("白棋赢辣");
        } else if (command.equals("BLACK")) {
            mStatusTextView.setText("黑棋赢辣");
        } else {
            Log.d("whalea", "收到的指令:" + command);
            String[] temps = command.split(":");
            int a = Integer.parseInt(temps[0]);
            int b = Integer.parseInt(temps[1]);
            int c = Integer.parseInt(temps[2]);
            int d = Integer.parseInt(temps[3]);
//                int e = Integer.parseInt(temps[4]);
            ground[b][a] = 0;
            if (WHITE_FLAG) {
                ground[d][c] = 1;
            } else {
                ground[d][c] = 2;
                ground[b + (d - b) / 2][a + (c - a) / 2] = 0;
            }
            if (WHITE_FLAG) {
                WHITE_FLAG = false;
            } else {
                WHITE_FLAG = true;
            }
//            String temp = a + ":" + b;
//            storageArray.push(temp);
        }
        invalidate();
    }

    public interface BlueToothActionListner {
        void  onPutChess(String temp);
        void onBtnPress(int i); //0悔棋  1刷新
    }







    public void showTextView(CharSequence mT) {
        this.mStatusTextView.setText(mT);
        mStatusTextView.setVisibility(View.VISIBLE);
    }

    private int[] showtime;

    public void setShowTimeTextViewTime(int[] showtime) {
        this.showtime = showtime;
    }

    class MyButtonListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //如果是悔棋
                case R.id.bluetooth_btn1:
                    blueToothGameAty.onBtnPress(0);
                    isnoPlaySound=!isnoPlaySound;
                    break;
                //如果是刷新
                case R.id.bluetooth_btn2:
                    blueToothGameAty.onBtnPress(1);
                    setVisibility(View.VISIBLE);
                    mStatusTextView.invalidate();
                    Utils.initGroup();
                    WHITE_FLAG=false;
                    init();
                    invalidate();
                    for (int i = 0; i < showtime.length; i++) {
                        showtime[i] = 0;
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                    mStatusTextView.setText("蓝牙对战模式 妙计：" + simpleDateFormat.format(new Date()));
                    break;
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (!isStop) {
            draw();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
