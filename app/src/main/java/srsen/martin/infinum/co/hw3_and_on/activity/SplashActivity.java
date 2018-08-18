package srsen.martin.infinum.co.hw3_and_on.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import srsen.martin.infinum.co.hw3_and_on.Provider;
import srsen.martin.infinum.co.hw3_and_on.R;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.logoAnimationIcon)
    ImageView animationLogoIcon;

    @BindView(R.id.animationScreen)
    RelativeLayout animationScreen;

    @BindView(R.id.textAnimation)
    TextView textAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        animate();
    }

    private void animate(){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce_animation);

        animation.setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationLogoIcon.setTranslationY(-1 * animationLogoIcon.getHeight());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animateText();
            }
        });

        animationLogoIcon.startAnimation(animation);
    }

    private void animateText(){
        textAnimation.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale_animation);

        animation.setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(() -> logIn(), 2000);
            }
        });

        textAnimation.startAnimation(animation);
    }

    private void logIn(){
        if(Provider.getRememberState(this)){
            String token = Provider.getToken(this);
            loginWithToken(token);
        }else{
            loginWithDetails();
        }

        finish();
    }

    private void loginWithDetails(){
        Intent intent = LoginActivity.newIntentInstance(this);
        startActivity(intent);
    }

    private void loginWithToken(String token){
        Intent intent = MainActivity.newIntentInstance(this);
        startActivity(intent);
    }

    private class AnimationAdapter implements Animation.AnimationListener{
        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationEnd(Animation animation) {}
        @Override
        public void onAnimationRepeat(Animation animation) {}
    }
}
