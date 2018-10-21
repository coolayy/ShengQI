package com.cheng.wang.shengshui.aty;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import com.cheng.wang.shengshui.view.RenRen;
import com.cheng.wang.shengshuiqi.R;

;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import static com.cheng.wang.shengshui.utils.Constant.isnoPlaySound;


public class RenRenGameAty extends Activity {

    private RenRen gbv;
    public int  widthh;
    public int heighttt;
    TextView renren_textView;
    Button renren_huiqi;
    Button renren_shuaxin;
    Button renren_sheng;

    TextView renren_showtime;

    SoundPool soundPool;//������
    HashMap<Integer, Integer> soundPoolMap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renrengame_layout);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
         widthh = metric.widthPixels;     // 屏幕宽度（像素）
         heighttt = metric.heightPixels;   // 屏幕高度（像素）


        initSound();
        initView();
    }

    private void initView() {

        renren_showtime = (TextView) findViewById(R.id.renren_showtime);
        gbv = (RenRen)findViewById(R.id.renren_gobangview);
        renren_textView = (TextView) findViewById(R.id.renren_text);
        renren_huiqi = (Button) findViewById(R.id.renren_btn1);
        renren_shuaxin = (Button) findViewById(R.id.renren_btn2);
        SimpleDateFormat simpleDateFormat = null;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        renren_textView.setText("当前时间：" + simpleDateFormat.format(new Date()));
        gbv.setTextView(renren_textView);
        gbv.setButtons(renren_huiqi, renren_shuaxin);
        gbv.setShowTimeTextViewTime(jishitime);
        Timer timer = new Timer();
        JishiTask myTask = new JishiTask();
        timer.schedule(myTask, 1000,1000);
    }

    int[] jishitime = {0,0,0,0};//秒，分，时，总
    private class JishiTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    jishitime[0]++;
                    jishitime[3]++;
                    if (jishitime[0] == 60) {
                        jishitime[1]++;
                        jishitime[0] = 0;
                    }
                    if (jishitime[1] == 60) {
                        jishitime[2]++;
                        jishitime[1] = 0;
                    }
                    if (jishitime[2] == 24) {
                        jishitime[2]=0;
                    }
                    renren_showtime.setText(String.format("%02d:%02d:%02d",jishitime[2],jishitime[1],jishitime[0]));
                }
            });
        }
    }

    public void initSound()
    {
        //������
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundPoolMap = new HashMap<Integer, Integer>();
        //�Զ�������
        soundPoolMap.put(1, soundPool.load(this, R.raw.noxiaqi, 1));
        soundPoolMap.put(2, soundPool.load(this, R.raw.dong, 1)); //�������
        soundPoolMap.put(4, soundPool.load(this, R.raw.win, 1)); //Ӯ��
        soundPoolMap.put(5, soundPool.load(this, R.raw.loss, 1)); //����
    }
    //��������
    public void playSound(int sound, int loop)
    {
        if(!isnoPlaySound)
        {
            return;
        }
        AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
    }
}
