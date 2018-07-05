package vlab.android.architecture.ui.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import vlab.android.architecture.R;
import vlab.android.architecture.base.BaseFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends BaseFragment {

    private TextView mTvResult;
    private LoginErrorHandler mLoginErrorHandle;
    // view model for login
    private LoginViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginErrorHandle = new LoginErrorHandler();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AutoCompleteTextView email = view.findViewById(R.id.email);
        EditText password = view.findViewById(R.id.password);
        mTvResult = view.findViewById(R.id.tv_result);

        view.findViewById(R.id.btn_sign_in).setOnClickListener(view1 -> {
            mViewModel.login(email.getText().toString(), password.getText().toString());
        });
    }

    @Override
    protected void initViewModel() {
        mViewModel = provideViewModel(LoginViewModel.class, false);
    }

    @Override
    protected void bindViewModel() {
        mViewModel.onLoginSuccessObs().observe(this, userInfo -> {
            mTvResult.setText(getString(R.string.login_success, userInfo.getUserName()));
        });

        mViewModel.onLoginFailedObs().observe(this, throwable -> {
            mTvResult.setText(mLoginErrorHandle.parseError(throwable));
        });

        mViewModel.onLoadingObs().observe(this, this::showProgressDialog);
    }
}

