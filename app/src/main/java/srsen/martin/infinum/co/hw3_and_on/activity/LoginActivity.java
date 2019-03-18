package srsen.martin.infinum.co.hw3_and_on.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srsen.martin.infinum.co.hw3_and_on.Provider;
import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.models.Data;
import srsen.martin.infinum.co.hw3_and_on.models.Token;
import srsen.martin.infinum.co.hw3_and_on.models.User;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_login)
    TextInputLayout emailEdit;

    @BindView(R.id.password_edit_login)
    TextInputLayout passwordEdit;

    @BindView(R.id.rememberCheckbox)
    CheckBox rememberCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.loginButton)
    void onLoginClicked(){
        String email = emailEdit.getEditText().getText().toString();
        String password = passwordEdit.getEditText().getText().toString();

        boolean validEmail = Util.checkEmailEdit(this, emailEdit);
        boolean validPassword = Util.checkPasswordEdit(this, passwordEdit);
        if(!validEmail || !validPassword)  return;

        //login
        User user = new User(email, password);
        loginUser(user);
    }

    private void loginUser(User user){
        Util.showProgress(this, getString(R.string.login), getString(R.string.loading), true, false);

        Provider.getApiService().loginUser(user).enqueue(new Callback<Data<Token>>() {
            @Override
            public void onResponse(@NonNull Call<Data<Token>> call, @NonNull Response<Data<Token>> response) {
                Util.hideProgress();

                if(response.isSuccessful()){
                    String token = response.body().getData().getToken();

                    logIn();
                    Toast.makeText(LoginActivity.this, R.string.logn_sucess, Toast.LENGTH_SHORT).show();
                    checkCheckBoxState(token);
                }else{
                    Toast.makeText(LoginActivity.this, R.string.unable_login, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Data<Token>> call, @NonNull Throwable t) {
                Util.hideProgress();
                Util.showError(LoginActivity.this, getString(R.string.login));
            }
        });
    }

    private void logIn(){
        Intent intent = MainActivity.newIntentInstance(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    private void checkCheckBoxState(String token){
        Provider.saveToken(this, token, rememberCheckBox.isChecked());
    }

    @OnClick(R.id.createAccount)
    void onCreateAccountClicked(){
        Intent intent = RegisterActivity.newIntentInstance(this);
        startActivity(intent);
    }

    public static Intent newIntentInstance(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}
