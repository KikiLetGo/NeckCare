package com.elexlab.neckcare.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.elexlab.neckcare.NeckCareApplication;
import com.elexlab.neckcare.R;
import com.elexlab.neckcare.exercise.Action;
import com.elexlab.neckcare.exercise.DownWaveAction;
import com.elexlab.neckcare.exercise.Exercise;
import com.elexlab.neckcare.exercise.LeftRotateAction;
import com.elexlab.neckcare.exercise.LeftWaveAction;
import com.elexlab.neckcare.exercise.RightRotateAction;
import com.elexlab.neckcare.exercise.RightWaveAction;
import com.elexlab.neckcare.exercise.UpWaveAction;
import com.elexlab.neckcare.facetrack.AbstractFaceTracker;
import com.elexlab.neckcare.facetrack.mediapipe.MediaPipeFaceTracker;
import com.elexlab.neckcare.misc.EasyLog;
import com.elexlab.neckcare.phonemonitor.Telegram;
import com.elexlab.neckcare.phonemonitor.UserAvatar;
import java.util.HashMap;
import java.util.Map;

public class ExerciseActivity extends AppCompatActivity {
    private static final String TAG = ExerciseActivity.class.getSimpleName();
    public static void startMe(){
        Intent intent = new Intent(NeckCareApplication.context,ExerciseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NeckCareApplication.context.startActivity(intent);
    }
    private AbstractFaceTracker faceTracker;
    private Exercise exercise;

    private ViewGroup llTurnLeft;
    private ViewGroup llTurnRight;
    private ViewGroup llTurnUp;
    private ViewGroup llTurnDown;
    private View ivRotateLeft;
    private View ivRotateRight;

    private View llRemindLater;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        llTurnLeft = findViewById(R.id.llTurnLeft);
        llTurnRight = findViewById(R.id.llTurnRight);
        llTurnUp = findViewById(R.id.llTurnUp);
        llTurnDown = findViewById(R.id.llTurnDown);
        llRemindLater = findViewById(R.id.llRemindLater);
        ivRotateLeft = findViewById(R.id.ivRotateLeft);
        ivRotateRight = findViewById(R.id.ivRotateRight);

        initFaceTracker();

        GLSurfaceView glSurfaceView = faceTracker.getGLSurfaceView();
        FrameLayout flCameraArea = findViewById(R.id.flCameraArea);
        flCameraArea.removeAllViewsInLayout();
        flCameraArea.addView(glSurfaceView);
        glSurfaceView.setVisibility(View.VISIBLE);
        flCameraArea.requestLayout();

        checkPermission();

        createExercise();

        llRemindLater.setOnClickListener(v -> {
            Telegram telegram = new Telegram(Telegram.MsgType.SUSPEND);
            Map<String,Object> extraInfo = new HashMap<>();
            extraInfo.put("duration",60*5L);//5 mins
            telegram.setExtraInfo(extraInfo);
            UserAvatar.getInstance()
                    .getFSM()
                    .handleMessage(telegram);
            finish();
        });
    }

    private Action currentAction = null;
    private void initFaceTracker(){
        faceTracker = new MediaPipeFaceTracker(this);
        faceTracker.setFaceTrackListener(face -> {
            if(face == null || face.getFacePose()==null){
                return;
            }
            Action action = exercise.action(face.getFacePose());
            if(action == null){
                onExerciseComplete();
            }else{
                if(action != currentAction){
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                }
                currentAction = action;
                runOnUiThread(()->onAction(currentAction));

            }

        });
    }

    private void createExercise(){
        exercise = Exercise.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EasyLog.d(TAG, "onResume");
        faceTracker.startTrack();
    }

    @Override
    protected void onPause() {
        EasyLog.i(TAG, "onPause");
        super.onPause();
        faceTracker.stopTrack();
    }


    @Override
    protected void onDestroy() {
        EasyLog.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean isHasFocus) {
        EasyLog.d(TAG, "onWindowFocusChanged");
        super.onWindowFocusChanged(isHasFocus);
        if (isHasFocus) {
            getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }



    private static final String[] PERMISSIONS_ARRAYS = new String[]{Manifest.permission.CAMERA};

    public void checkPermission() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA}, 100);
            }
    }

    private void onAction(Action action){
        if(action instanceof LeftWaveAction){
            ivRotateLeft.setVisibility(View.INVISIBLE);
            ivRotateRight.setVisibility(View.INVISIBLE);
            llTurnLeft.setVisibility(View.VISIBLE);
            llTurnLeft.invalidate();
            llTurnRight.setVisibility(View.INVISIBLE);
            llTurnUp.setVisibility(View.INVISIBLE);
            llTurnDown.setVisibility(View.INVISIBLE);

        }else if(action instanceof RightWaveAction){
            ivRotateLeft.setVisibility(View.INVISIBLE);
            ivRotateRight.setVisibility(View.INVISIBLE);
            llTurnLeft.setVisibility(View.INVISIBLE);
            llTurnRight.setVisibility(View.VISIBLE);
            llTurnUp.setVisibility(View.INVISIBLE);
            llTurnDown.setVisibility(View.INVISIBLE);
        }else if(action instanceof UpWaveAction){
            ivRotateLeft.setVisibility(View.INVISIBLE);
            ivRotateRight.setVisibility(View.INVISIBLE);
            llTurnLeft.setVisibility(View.INVISIBLE);
            llTurnRight.setVisibility(View.INVISIBLE);
            llTurnUp.setVisibility(View.VISIBLE);
            llTurnDown.setVisibility(View.INVISIBLE);
        }else if(action instanceof DownWaveAction){
            ivRotateLeft.setVisibility(View.INVISIBLE);
            ivRotateRight.setVisibility(View.INVISIBLE);
            llTurnLeft.setVisibility(View.INVISIBLE);
            llTurnRight.setVisibility(View.INVISIBLE);
            llTurnUp.setVisibility(View.INVISIBLE);
            llTurnDown.setVisibility(View.VISIBLE);
        }
        else if(action instanceof LeftRotateAction){
            ivRotateLeft.setVisibility(View.VISIBLE);
            ivRotateRight.setVisibility(View.INVISIBLE);
            llTurnLeft.setVisibility(View.INVISIBLE);
            llTurnRight.setVisibility(View.INVISIBLE);
            llTurnUp.setVisibility(View.INVISIBLE);
            llTurnDown.setVisibility(View.INVISIBLE);
        }
        else if(action instanceof RightRotateAction){
            ivRotateLeft.setVisibility(View.INVISIBLE);
            ivRotateRight.setVisibility(View.VISIBLE);
            llTurnLeft.setVisibility(View.INVISIBLE);
            llTurnRight.setVisibility(View.INVISIBLE);
            llTurnUp.setVisibility(View.INVISIBLE);
            llTurnDown.setVisibility(View.INVISIBLE);
        }
    }

    private void onExerciseComplete(){
        faceTracker.setFaceTrackListener(null);
        Telegram telegram = new Telegram(Telegram.MsgType.EXERCISE_COMPLETE);
        UserAvatar.getInstance()
                .getFSM()
                .handleMessage(telegram);

        runOnUiThread(()->finish());

    }
}
