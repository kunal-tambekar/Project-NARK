package itutorgroup.h2h.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import itutorgroup.h2h.R;

public class FirstActivity extends AppCompatActivity {

    private static final String EXTRA_PARAM = "";
    private boolean mDoubleBackToExitPressedOnce = false;

    public static Intent newIntent(Context packageContext, String city){
        Intent intent = new Intent(packageContext, FirstActivity.class);
        intent.putExtra(EXTRA_PARAM, city);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void signupBtnClicked(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    public void loginBtnClicked(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (mDoubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.mDoubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mDoubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
