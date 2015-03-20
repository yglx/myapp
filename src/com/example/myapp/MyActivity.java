package com.example.myapp;

import android.app.Activity;
import android.content.*;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;

import java.util.List;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    GridView icongridView;
    Button btn;
    SeekBar mVoiceSeekBar;
    SeekBar mLightSeekBar;
    int info[];
    CheckBox mBrightCheckBox;
    int num =0;
    Button testani;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        testani = (Button)findViewById(R.id.testanimation);
        testani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup mViewGroup = (ViewGroup)findViewById(R.id.anizi);
                mViewGroup.getChildAt(0).setVisibility(View.GONE);
/*                TextView mtv = new TextView(MyActivity.this);
                mtv.setText("God"+num++);
                mViewGroup.addView(mtv,1);*/
            }
        });
        icongridView = (GridView)findViewById(R.id.gridView);
        info = new int[]{0,0,0,0,0,0};
        icongridView.setAdapter(new IconAdapter(info,this));
        try{
            this.registerReceiver(new EntryBroadCast(),new IntentFilter("adbbroadcast"));
        }catch (Exception e){
            e.printStackTrace();
        }

        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("adbbroadcast");
                intent.putExtra("appid", 2);
                intent.putExtra("state",1);
                sendBroadcast(intent);
            }
        });
        int a = 3;int b = 4;
        mLightSeekBar = (SeekBar)findViewById(R.id.seekBar);
        mLightSeekBar.setMax(225);
        int normal = Settings.System.getInt(getContentResolver(),Settings.System.SCREEN_BRIGHTNESS,225);
        mLightSeekBar.setProgress(normal);
        mLightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Integer tmpInt = seekBar.getProgress();
                    System.out.println(tmpInt);
                    // 51 (seek scale) * 5 = 255 (max brightness)
                    // Old way
                    android.provider.Settings.System.putInt(getContentResolver(),
                            android.provider.Settings.System.SCREEN_BRIGHTNESS,
                            tmpInt); // 0-255
                    tmpInt = Settings.System.getInt(getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS, -1);
                    // Cupcake way..... sucks
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    // lp.screenBrightness = 1.0f;
                    // Float tmpFloat = (float)tmpInt / 255;
                    if (0<= tmpInt && tmpInt <= 255) {
                        lp.screenBrightness = tmpInt;
                    }


                    getWindow().setAttributes(lp);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mBrightCheckBox = (CheckBox)findViewById(R.id.checkBox);
        int msreenMode = getScreenMode();
        if(msreenMode == 0){
            mBrightCheckBox.setChecked(false);
        }else {
            mBrightCheckBox.setChecked(true);
        }
        mBrightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.v("jw","sfsf"+isChecked);
                if(isChecked){
                    setScreenMode(1);
                }else setScreenMode(0);
            }
        });
        mVoiceSeekBar = (SeekBar)findViewById(R.id.audioSeekBar);
        final AudioManager mAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int max = mAudioManager.getStreamMaxVolume( AudioManager.STREAM_SYSTEM );
       int  current = mAudioManager.getStreamVolume( AudioManager.STREAM_SYSTEM );
        mVoiceSeekBar.setMax(max);
        mVoiceSeekBar.setProgress(current);
        mVoiceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private int getScreenMode(){
        int screenMode=0;
        try{
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        }
        catch (Exception localException){

        }
        return screenMode;
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    private int getScreenBrightness(){
        int screenBrightness=255;
        try{
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Exception localException){

        }
        return screenBrightness;
    }
    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private void setScreenMode(int paramInt){
        try{
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        }catch (Exception localException){
            localException.printStackTrace();
        }
    }
    /**
     * 设置当前屏幕亮度值 0--255
     */
    private void saveScreenBrightness(int paramInt){
        try{
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
    }
    public class IconAdapter extends BaseAdapter{
        List list;
        Context mContext;
        String mstring[];
        int drawID[];
        int info[];
        public IconAdapter(Context mContext) {
            this.mContext = mContext;
            mstring = mContext.getResources().getStringArray(R.array.entrysoft);
            drawID = new int[]{R.drawable.zzz_ic_nav,R.drawable.zzz_ic_seeworld,R.drawable.zzz_ic_enter,R.drawable.zzz_ic_tinydog,R.drawable.zzz_ic_bluephone};

        }

        public IconAdapter(int info[], Context mContext) {
            mstring = mContext.getResources().getStringArray(R.array.entrysoft);
            drawID = new int[]{R.drawable.zzz_ic_nav,R.drawable.zzz_ic_seeworld,R.drawable.zzz_ic_enter,R.drawable.zzz_ic_tinydog,R.drawable.zzz_ic_bluephone};
            this.info = info;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int i) {
            return info[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View converView, ViewGroup parent) {
            Holder holder;
            EntryListener mListener;
            if(converView == null){
                converView = View.inflate(mContext,R.layout.quick_ware,null);
                 holder= new Holder();
                holder.imageView = (ImageView)converView.findViewById(R.id.iconView);
                holder.noteinfo = (TextView)converView.findViewById(R.id.noteinf);
                holder.iconName = (TextView)converView.findViewById(R.id.iconName);
                holder.redView = (ImageView)converView.findViewById(R.id.redView);
                holder.mListener = new EntryListener(position);
                converView.setTag(holder);
            }else {
                holder = (Holder)converView.getTag();
            }

                holder.imageView.setBackgroundResource(drawID[position]);
                holder.iconName.setText(mstring[position]);
            if(info[position]>0){
                holder.redView.setVisibility(View.VISIBLE);
                holder.noteinfo.setVisibility(View.VISIBLE);
                holder.noteinfo.setText(""+info[position]);
            }else {
                holder.redView.setVisibility(View.INVISIBLE);
                holder.noteinfo.setVisibility(View.INVISIBLE);
            }
            mListener = holder.mListener;
            holder.imageView.setOnClickListener(mListener);
            return converView;
        }
        public final class Holder{
            ImageView imageView;
            TextView noteinfo;
            TextView iconName;
            ImageView redView;
            EntryListener mListener;
        }
        private class EntryListener implements View.OnClickListener{
            int position;

            String[] apppackage= {"cn.com.mobnote.map","cn.com.mobnote.usercenter",
                    "cn.com.mobnote.entertainment","cn.com.mobnote.traffic","com.example.bluetoothcaller.Activity"};
            String[] appenter = {".map.MapActivity",".MainActivity",".activity.MainActivity",".SetupActivity",".MainActivity"};

            public EntryListener(int position) {
                this.position = position;
            }
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent();
                intent1.setComponent(new ComponentName(apppackage[position],apppackage[position]+appenter[position]));
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);

            }
        }
    }
    public class EntryBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("appid",0);
            int state = intent.getIntExtra("state",0);
            Toast.makeText(context,"pos:"+pos+" state:"+state,Toast.LENGTH_SHORT).show();
            if(pos > 0)
            updateView(pos-1,state);
        }
    }
    void updateView(int position,int changinfo){
        //得到第一个可显示控件的位置，
        int visiblePosition = icongridView.getFirstVisiblePosition();
        if(position - visiblePosition >= 0){
            View view = icongridView.getChildAt(position-visiblePosition);
            IconAdapter.Holder holder = (IconAdapter.Holder)view.getTag();
            if(changinfo>0){
                holder.redView.setVisibility(View.VISIBLE);
                holder.noteinfo.setVisibility(View.VISIBLE);
                holder.noteinfo.setText(changinfo+"");
            }
           else {
                holder.redView.setVisibility(View.INVISIBLE);
                holder.noteinfo.setVisibility(View.INVISIBLE);
            }
        }
        info[position] = changinfo;
    }
}
