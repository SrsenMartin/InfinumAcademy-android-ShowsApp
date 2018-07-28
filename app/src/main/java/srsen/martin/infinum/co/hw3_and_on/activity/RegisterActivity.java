package srsen.martin.infinum.co.hw3_and_on.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srsen.martin.infinum.co.hw3_and_on.networking.ApiService;
import srsen.martin.infinum.co.hw3_and_on.R;
import srsen.martin.infinum.co.hw3_and_on.Util;
import srsen.martin.infinum.co.hw3_and_on.models.User;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.email_edit_register)
    TextInputLayout emailEdit;

    @BindView(R.id.password_edit_register)
    TextInputLayout passwordEdit;

    @BindView(R.id.passwordConfirm_edit_register)
    TextInputLayout passwordConfirmEdit;

    @BindView(R.id.registerToolbar)
    Toolbar toolbar;

    private ApiService apiService;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        apiService = Util.initApiService();
    }

    @OnClick(R.id.registerButton)
    void onRegisterClicked(){
        String email = emailEdit.getEditText().getText().toString();
        String password = passwordEdit.getEditText().getText().toString();
        String confirmPassword = passwordConfirmEdit.getEditText().getText().toString();

        boolean validEmail = Util.checkEmailEdit(this, emailEdit);
        boolean validPassword = Util.checkPasswordEdit(this, passwordEdit);

        if(!validPassword)  return;
        boolean validConfirm = checkConfirmPassword(password, confirmPassword);
        if(!validEmail || !validConfirm)  return;

        //register
        User user = new User(email, password);
        registerUser(user);
    }

    private boolean checkConfirmPassword(String password, String confirmPassword){
        if(password.equals(confirmPassword)){
            passwordConfirmEdit.setError(null);
            return true;
        }

        passwordConfirmEdit.setError(getString(R.string.password_match));
        return false;
    }

    private void registerUser(User user){
        progressDialog = Util.showProgress(this,
                getString(R.string.register), getString(R.string.loading), true, false);

        apiService.registerUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                Util.hideProgress(progressDialog);

                if(response.code() == 201){
                    Toast.makeText(RegisterActivity.this, getString(R.string.new_user_200), Toast.LENGTH_SHORT).show();
                    finish();
                }else if(response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, R.string.email_exists, Toast.LENGTH_LONG).show();
                    return;
                }else{
                    Toast.makeText(RegisterActivity.this, R.string.unable_register, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Util.hideProgress(progressDialog);
                Util.showError(RegisterActivity.this, getString(R.string.register));
            }
        });
    }

    public static Intent newIntentInstance(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }
}
