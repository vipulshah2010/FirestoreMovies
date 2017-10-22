package com.vipshah.remixermovies.ui.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.ui.movies.MovieListActivity;
import com.vipshah.remixermovies.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.registerButton)
    Button registerButton;

    @BindView(R.id.usernameEditText)
    EditText usernameEditText;

    @BindView(R.id.passwordEditText)
    EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        RemixerBinder.bind(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            navigateToListingScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_remixer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remixer:
                RemixerFragment.newInstance().showRemixer(getSupportFragmentManager(), null);
                return true;
            default:
                return false;
        }
    }

    @OnClick({R.id.loginButton, R.id.registerButton})
    void loginOrRegister(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }

        switch (view.getId()) {
            case R.id.loginButton:
                FirebaseAuth.getInstance().signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                onProcessAuthResult(task);
                            }
                        });
                break;
            case R.id.registerButton:
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(username, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                onProcessAuthResult(task);
                            }
                        });
                break;
        }
    }

    private void onProcessAuthResult(Task<AuthResult> task) {
        if (task != null && task.isSuccessful() && task.getResult() != null) {
            navigateToListingScreen();
            finish();
        }
    }

    @RangeVariableMethod(minValue = 70, maxValue = 100, increment = 1, title = "Change Logo Size")
    void changeLogoSize(Float size) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.width = (int) CommonUtils.getPixels(this, size.intValue());
        params.height = (int) CommonUtils.getPixels(this, size.intValue());
        imageView.setLayoutParams(params);
    }

    private void navigateToListingScreen() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MovieListActivity.class));
        finish();
    }

    @BooleanVariableMethod(title = "Show TextField Icons")
    void toggleTextFieldIcons(Boolean show) {
        if (show) {
            Drawable usernameDrawable = ContextCompat.getDrawable(this, R.drawable.ic_svg_username);
            Drawable passwordDrawable = ContextCompat.getDrawable(this, R.drawable.ic_svg_password);
            usernameEditText.setCompoundDrawablesWithIntrinsicBounds(usernameDrawable, null, null, null);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(passwordDrawable, null, null, null);
        } else {
            usernameEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }
}
