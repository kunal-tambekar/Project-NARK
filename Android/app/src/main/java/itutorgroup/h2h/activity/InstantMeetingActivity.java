package itutorgroup.h2h.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.itutorgroup.h2hmodel.H2HCallback;
import com.itutorgroup.h2hmodel.H2HHttpRequest;
import com.itutorgroup.h2hmodel.H2HResponse;
import com.meetingroom.utils.SystemUtil;
import com.mosai.utils.Tools;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import itutorgroup.h2h.R;

public class InstantMeetingActivity extends MeetingRoomBaseActivity {

    private static final int PERMISSION_CAMERA = 0;
    private static final int PERMISSION_RECORD_AUDIO = 1;
    private EditText etDisplayName, etEmailAddress;
    private String email;
    private Button joinMeetingBtn;
    private ProgressBar spinner;
    private H2HHttpRequest h2HHttpRequest;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ((ContextCompat.checkSelfPermission(context,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {

                h2HHttpRequest = H2HHttpRequest.getInstance();
                h2HHttpRequest.instantMeeting(
                        etDisplayName.getText().toString().trim(),
                        etEmailAddress.getText().toString().trim(),
                        new H2HCallback() {
                            @Override
                            public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response) {
                                if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
                                    launchMeeting(h2HHttpRequest.getOrigin(), h2HHttpRequest.getServerURL(), h2HHttpRequest.getUserToken());
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final String message = response != null && !TextUtils.isEmpty(response.message)
                                                    ? response.message : "Meeting Not Found, Please Enter an Exiting Meeting id";

                                            Toast.makeText(InstantMeetingActivity.this, message, Toast.LENGTH_SHORT).show();
                                            cancelJoinMeeting();
                                        }
                                    });
                                }
                            }
                        });
            } else {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    requestAudioPermission();

                }
            }
        }
    };

    @Override
    protected void initDatas() {
    }

    @Override
    protected void initView() {
        etDisplayName = (EditText) findViewById(R.id.displayNameEditText);
        etEmailAddress = (EditText) findViewById(R.id.emailAddressEditText);
        joinMeetingBtn = (Button) findViewById(R.id.joinMeetingBtn);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelJoinMeeting();
    }

    @Override
    protected int setContent() {
        return R.layout.activity_instant_meeting;
    }

    @Override
    protected void addListener() {
    }

    private void cancelJoinMeeting() {
        spinner.setVisibility(View.INVISIBLE);
        joinMeetingBtn.setClickable(true);
    }

    public void joinMeetingClicked(View view) {

        spinner.setVisibility(View.VISIBLE);
        joinMeetingBtn.setClickable(false);

        if (TextUtils.isEmpty(etDisplayName.getText().toString().trim())) {
            showToast(getString(R.string.displayName_tip));
            cancelJoinMeeting();
            return;
        }

        if (!Tools.isConnect(context)) {
            showToast(getString(R.string.check_network_tip));
            cancelJoinMeeting();
            return;
        }
        requestAudioPermission();
    }

    private void launchMeeting(String origin, String serverURL, final String userToken) {
        Intent intent;
        if (SystemUtil.isTablet(this)) {
            intent = new Intent(this, com.meetingroom.activity.flat.FlatMeetingActivity.class);
        } else {
            intent = new Intent(this, com.meetingroom.activity.MeetingActivity.class);
        }
        com.meetingroom.bean.ServerConfig serverConfig = new com.meetingroom.bean.ServerConfig();
        serverConfig.userToken = userToken;
        serverConfig.origin = origin;
        serverConfig.serverURL = serverURL;
        intent.putExtra("serverConfig", serverConfig);
        startActivityForResult(intent, 0);
    }

    @PermissionGrant(PERMISSION_CAMERA)
    public void requestCameraSuccess() {
        handler.sendEmptyMessage(0);
    }

    @PermissionDenied(PERMISSION_CAMERA)
    public void requestCameraFaild() {
    }

    @PermissionGrant(PERMISSION_RECORD_AUDIO)
    public void requestAudioSuccess() {
        if ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            handler.sendEmptyMessage(0);
        } else {
            requestCameraPermission();
        }
    }

    @PermissionDenied(PERMISSION_RECORD_AUDIO)
    public void requestAudioFailed() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestCameraPermission() {
        MPermissions.requestPermissions(this, PERMISSION_CAMERA, Manifest.permission.CAMERA);
    }

    private void requestAudioPermission() {
        MPermissions.requestPermissions(this, PERMISSION_RECORD_AUDIO, Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        }
    }
}
