package srsen.martin.infinum.co.hw3_and_on.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import srsen.martin.infinum.co.hw3_and_on.R;

public class LoginView extends LinearLayout {

    @BindView(R.id.loginButton)
    Button loginButton;

    @BindView(R.id.createAccount)
    TextView createAccount;

    @BindView(R.id.loginIcon)
    ImageView loginIcon;

    @BindView(R.id.email_edit_login)
    TextInputLayout emailEdit;

    @BindView(R.id.password_edit_login)
    TextInputLayout passwordEdit;

    @BindView(R.id.rememberCheckbox)
    CheckBox rememberCheckBox;

    private OnClickListener loginClicked;
    private OnClickListener registerClicked;
    private CheckboxSelectedListener checkboxSelectedListener;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs){
        LayoutInflater.from(getContext()).inflate(R.layout.view_login, this, true);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        invalidate();
        ButterKnife.bind(this);

        if(attrs == null)   return;

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.LoginView, 0, 0);

        int logoId = a.getResourceId(R.styleable.LoginView_logoIcon, R.drawable.ic_logo);
        loginIcon.setImageResource(logoId);

        int baseColor = a.getColor(R.styleable.LoginView_baseColor, getResources().getColor(R.color.baseColor));
        setBaseColor(baseColor);
    }

    public void setBaseColor(int color){
        loginButton.setBackgroundColor(color);
        createAccount.setTextColor(color);
    }

    public void setOnLoginListener(OnClickListener listener){
        loginClicked = listener;
    }

    public void setOnRegisterListener(OnClickListener listener){
        registerClicked = listener;
    }

    public void setOnCheckboxChecked(CheckboxSelectedListener listener){
        checkboxSelectedListener = listener;
    }

    @OnClick
    void onLoginClicked(){
        if(loginClicked != null){
            loginClicked.onClick(this);
        }

        if(rememberCheckBox != null && rememberCheckBox.isChecked()){
            checkboxSelectedListener.onCheckboxSelected(this);
        }
    }

    @OnClick(R.id.createAccount)
    void onCreateAccountClicked(){
        if(registerClicked != null){
            registerClicked.onClick(this);
        }
    }

    public TextInputLayout getEmailEdit() {
         return emailEdit;
    }

    public TextInputLayout getPasswordEdit() {
        return passwordEdit;
    }

    public interface CheckboxSelectedListener{
        void onCheckboxSelected(View view);
    }
}
