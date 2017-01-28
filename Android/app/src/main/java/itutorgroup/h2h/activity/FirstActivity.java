package itutorgroup.h2h.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import itutorgroup.h2h.R;

public class FirstActivity extends AppCompatActivity {

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

}
