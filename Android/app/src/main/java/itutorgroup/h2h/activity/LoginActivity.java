package itutorgroup.h2h.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import itutorgroup.h2h.MyApplication;
import itutorgroup.h2h.R;
import itutorgroup.h2h.bean.CustomUser;

public class LoginActivity extends MeetingRoomBaseActivity {

    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {
    }

    @Override
    protected int setContent() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void addListener() {

    }

    public void signinClicked(View view) {
        final String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        final String pwd = ((EditText) findViewById(R.id.pwdEditText)).getText().toString();
        if (email.length()>0 && pwd.length()>0){
            showLoadingDialog();
//            H2HHttpRequest.getInstance().loginH2H(email, pwd, new H2HCallback() {
//                @Override
//                public void onCompleted(final Exception ex, final H2HCallBackStatus status, final H2HResponse response) {
//                    if (isFinishing()) {
//                        return;
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (status == H2HCallBackStatus.H2HCallBackStatusOK) {
//                                getUserInfo(email,pwd);
//
//                            } else {
//                                final String message = "Failed to Login" + response != null && !TextUtils.isEmpty(response.message)
//                                        ? ": " + response.message : "";
//                                dismissLoadingDialog();
//                                showToast(message);
//                            }
//                        }
//                    });
//                }
//            });

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", email);
                jsonObject.put("password", pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.POST, "https://sandbox.liveh2h.com/tutormeetweb/rest/v1/users/login", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            // not doing anything
                            try {
                                CustomUser user = new CustomUser();
                                user.setUid(Integer.parseInt(((JSONObject) response.get("data")).get("userId").toString()));
                                user.setEmail(((JSONObject) response.get("data")).get("email").toString());
                                user.setToken(((JSONObject) response.get("data")).get("token").toString());
                                user.setApiToken(((JSONObject) response.get("data")).get("apiToken").toString());
                                user.setName(((JSONObject) response.get("data")).get("name").toString());

                                showToast("User Sign Up Success");
                                dismissLoadingDialog();


                            } catch (JSONException e) {
                                e.printStackTrace();
                                dismissLoadingDialog();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }

            });
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(req1);

        } else {
            showToast("Please fill all the blanks");
        }
    }

    private void getUserInfo(final String email, final String pwd) {


        Map<String, String> params = new HashMap<String, String>();
        params.put("useremail", email);
        params.put("password", pwd);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://sfsuswe.com/~rarora/H2H/mini/public/users/getUser", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        // not doing anything
                        String jsonResponse = response.toString();
                        showToast("User Sign Up Success");
                        dismissLoadingDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                dismissLoadingDialog();
            }

        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

}
