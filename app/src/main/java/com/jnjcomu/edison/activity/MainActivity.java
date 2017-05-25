package com.jnjcomu.edison.activity;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.jnjcomu.edison.EdisonApplication;
import com.jnjcomu.edison.R;
import com.jnjcomu.edison.storage.Preference;
import com.jnjcomu.edison.ui.Anim;
import com.jnjcomu.edison.ui.LogoInterpolator;
import com.jnjcomu.edison.callback.CloudEventListener;
import com.loplat.placeengine.Plengi;
import com.loplat.placeengine.PlengiResponse;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements CloudEventListener {

    protected Anim anim = new Anim();
    protected LogoInterpolator li = new LogoInterpolator(0.2, 20);
    protected TimerTask mTask;
    protected Timer mTimer;
    protected Preference mPref;

    Plengi plengi;

    @ViewById
    protected Toolbar toolbar;

    @ViewById
    protected TextView txtPlace;

    @App
    protected EdisonApplication application;

    @ViewById
    protected ImageView logo;

    @ViewById(R.id.btn_retry)
    protected CardView retry;

    @ViewById
    protected Switch swtScanning;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        application.destroyEventListener();
        mTimer.cancel();
    }

    @AfterViews
    protected void initActivity() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.setTitle(null);

        application.setEventListener(this);
        plengi = application.getPlengi();

        mPref = new Preference(this);
        if(mPref.getBoolean("activated", true)) {
            plengi.start();
            scan();
        } else {
            swtScanning.setChecked(false);
            txtPlace.setText("장소 인식 기능이 꺼져있습니다.");
        }
    }

    @CheckedChange
    protected void swtScanning(boolean isChecked) {
        if(isChecked) {
            mPref.putBoolean("activated", true);
            plengi.start();
            scan();
        } else {
            mPref.putBoolean("activated", false);
            plengi.stop();
            anim.cancelAnim(logo);
            txtPlace.setText("장소 인식 기능이 꺼져있습니다.");
            if(mTimer!=null)
                mTimer.cancel();
        }
    }

    @Click
    protected void btnRetry() {
        retry.setVisibility(View.GONE);
        scan();
    }

    /**
     * EventListener
     *
     * @param response PlengiResponse
     * @see CloudEventListener
     */
    @Override
    public void onPlaceDefault(PlengiResponse response) {
        String msg = "현재 계신 장소는 " + response.place.name + "입니다.";
        display(msg);
    }

    /**
     * EventListener
     *
     * @param response PlengiResponse
     * @see CloudEventListener
     */
    @Override
    public void onPlaceNear(PlengiResponse response) {
        String msg = "현재 " + response.place.name + " 주변 입니다.";
        display(msg);
    }

    /**
     * EventListener
     *
     * @param response PlengiResponse
     * @see CloudEventListener
     */
    @Override
    public void onPlaceIn(PlengiResponse response) {
        String msg = "현재 " + response.place.name + "에 입실하셨습니다.";
        display(msg);
    }

    /**
     *
     * @param msg String
     */
    private void display(String msg) {
        mTimer.cancel();
        txtPlace.setText(msg);
        anim.cancelAnim(logo);
        anim.startAnim(this, logo, R.anim.logo_scale, li);
    }

    private void scan() {
        timer();
        plengi.getCurrentPlaceInfo();
        txtPlace.setText("스캔중...");
        anim.startAnim(this, logo, R.anim.logo_vibrate);
    }

    private void timer() {
        final Handler mHandler = new Handler();
        mTask = new TimerTask() {
            @Override
            public void run() {
                //application.getPlengi().stop();
                mHandler.post(() -> {
                    display("요청시간이 초과되었습니다.");
                    retry.setVisibility(View.VISIBLE);
                });
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 30000);
    }

}
