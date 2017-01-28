package itutorgroup.h2h.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.itutorgroup.h2hmodel.H2HResponse;
import com.itutorgroup.h2hmodel.H2HUserManager;

import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.Singleton;


public class MainActivity extends MeetingRoomBaseActivity {

    private static final String EXTRA_PARAM = "";

    public static Intent newIntent(Context packageContext, String param){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_PARAM, param);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int setContent() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }

    public void wishListButtonClicked(View v){
        startActivity(new Intent(this, WishListActivity.class));
    }

    public void joinBtnClicked(View view) {
        startActivity(new Intent(this, JoinMeetingActivity.class));
    }

    public void instantMeetingBtnClicked(View view) {
        startActivity(new Intent(this, InstantMeetingActivity.class));
    }


    public void scheduleBtnClicked(View view) {
        if (!H2HUserManager.getInstance().isLogin()) {
            showToast("You need to login first");
        } else {
            startActivity(new Intent(this, ScheduleMeetingActivity.class));
        }
    }

    public void signoutBtnClicked(View view) {
        H2HHttpRequest.getInstance().logoutH2H(new H2HCallback() {
            @Override
            public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response) {
                showToast("You have signed out");
                Singleton.setCurrentUser(null);
            }
        });
    }
}
