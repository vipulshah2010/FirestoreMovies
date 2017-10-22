package com.vipshah.remixermovies.ui.login;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.libraries.remixer.annotation.BooleanVariableMethod;
import com.google.android.libraries.remixer.annotation.RangeVariableMethod;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import com.google.android.libraries.remixer.annotation.StringListVariableMethod;
import com.google.android.libraries.remixer.ui.view.RemixerFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vipshah.remixermovies.R;
import com.vipshah.remixermovies.ui.movies.list.MoviesListActivity;
import com.vipshah.remixermovies.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView {

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

    @BindView(R.id.coverView)
    View coverView;

    @BindView(R.id.remixFabButton)
    FloatingActionButton remixFabButton;

    private LoginContract.LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        RemixerBinder.bind(this);
        RemixerFragment.newInstance().attachToFab(this, remixFabButton);

        loginPresenter = new LoginPresenterImpl(this);

        loginPresenter.checkLogin();

    }

    @OnClick({R.id.loginButton, R.id.registerButton})
    void loginOrRegister(View view) {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        switch (view.getId()) {
            case R.id.loginButton:
                loginPresenter.login(username, password);
                break;
            case R.id.registerButton:
                loginPresenter.register(username, password);
                break;
        }
    }

    @Override
    public void onLoginResult(boolean isSuccess) {
        if (isSuccess) {
            showMovies();
            finish();
        } else {
            Toast.makeText(this, "Failed to log in!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegisterResult(boolean isSuccess) {
        if (isSuccess) {
            showMovies();
            finish();
        } else {
            Toast.makeText(this, "Failed to register!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckLogin(boolean isLoggedIn) {
        if (isLoggedIn) {
            showMovies();
        }
    }

    // Remixer Configuration

    @RangeVariableMethod(minValue = 70, maxValue = 100, increment = 1, title = "Change Logo Size")
    void changeLogoSize(Float size) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.width = (int) CommonUtils.getPixels(this, size.intValue());
        params.height = (int) CommonUtils.getPixels(this, size.intValue());
        imageView.setLayoutParams(params);
    }

    private void showMovies() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MoviesListActivity.class));
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

    @StringListVariableMethod(title = "Cover View Color", limitedToValues = {"Accent", "Primary", "Primary Dark"})
    void changeCoverViewColor(String color) {
        switch (color) {
            case "Accent":
                coverView.setBackgroundColor(CommonUtils.getColor(this, R.color.colorAccent));
                break;
            case "Primary":
                coverView.setBackgroundColor(CommonUtils.getColor(this, R.color.colorPrimary));
                break;
            case "Primary Dark":
                coverView.setBackgroundColor(CommonUtils.getColor(this, R.color.colorPrimaryDark));
                break;
        }
    }
}
