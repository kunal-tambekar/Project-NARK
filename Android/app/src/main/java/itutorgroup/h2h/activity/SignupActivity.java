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
import java.util.Locale;
import java.util.Map;

import itutorgroup.h2h.MyApplication;
import itutorgroup.h2h.R;

public class SignupActivity extends MeetingRoomBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int setContent() {
        return R.layout.activity_signup;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }

    public void signupClicked(View view) {
        final String firstName = ((EditText) findViewById(R.id.firstNameEditText)).getText().toString().trim();
        final String lastName = ((EditText) findViewById(R.id.lastNameEditText)).getText().toString().trim();
        final String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        final String pwd = ((EditText) findViewById(R.id.pwdEditText)).getText().toString();
        if (firstName.length() > 0 && lastName.length() > 0 && email.length() > 0 && pwd.length() >= 5 && pwd.length() <= 19) {
            showLoadingDialog();

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("locale", Locale.getDefault().getLanguage()); // ['en', 'cn', 'tw', 'zh']
                jsonObject.put("type", "0");
                jsonObject.put("firstName", firstName);
                jsonObject.put("lastName", lastName);
                jsonObject.put("email", email);
                jsonObject.put("password", pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.POST, "https://sandbox.liveh2h.com/tutormeetweb/rest/v1/users/signup", jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, response.toString());
                            // not doing anything
                            try {
                                String uid = ((JSONObject) response.get("data")).get("userId").toString();
                                showToast("User Sign Up Success");
                                insertUserInfo(firstName, lastName, email, pwd, uid);
//                                dismissLoadingDialog();

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
                            " Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }

            });
            // Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(req1);

        } else {
            showToast("Please fill all the blanks, password must between 5-19 characters");
        }


    }

    private void insertUserInfo(final String first, final String last, final String email, final String pwd, final String userid) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userid);
        params.put("username", first + " " + last);
        params.put("useremail", email);
        params.put("password", pwd);


        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, "http://sfsuswe.com/~rarora/H2H/mini/public/users/addUser", new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        // not doing anything
                        String jsonResponse = response.toString();
//                        showToast("User Sign Up Success");
                        dismissLoadingDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                dismissLoadingDialog();
            }

        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }


}
