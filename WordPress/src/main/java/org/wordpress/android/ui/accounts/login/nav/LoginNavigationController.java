package org.wordpress.android.ui.accounts.login.nav;

public class LoginNavigationController implements LoginNavigationFsmGetter {

    public interface ContextImplementation {
        void toast(String message);
    }

    private PrologueHandler mPrologueHandler = new PrologueHandler();
    private InputEmailHandler mInputEmailHandler = new InputEmailHandler();
    private InputSiteAddressHandler mInputSiteAddressHandler = new InputSiteAddressHandler();
    private ContextImplementation mContextImplementation;

    public LoginNavigationController(LoginState initialLoginState, ContextImplementation contextImplementation) {
        mCurrentLoginState = initialLoginState;
        mContextImplementation = contextImplementation;
    }

    private LoginState mCurrentLoginState = LoginState.PROLOGUE;

    public void ensureState(LoginState loginState) {
        if (mCurrentLoginState != loginState) {
            throw new RuntimeException("Not in state " + loginState.name());
        }
    }

    private void gotoState(LoginState loginState) {
        if (mCurrentLoginState != loginState) {
            mCurrentLoginState = loginState;
        }
    }

    // Implementation of LoginState.PROLOGUE
    public class PrologueHandler implements LoginEvents.LoginNavPrologue {
        @Override
        public void doStartLogin() {
            ensureState(LoginState.PROLOGUE);

            mContextImplementation.toast("Login is not implemented yet");

            gotoState(LoginState.PROLOGUE);
        }

        @Override
        public void doStartSignup() {
            ensureState(LoginState.PROLOGUE);

            mContextImplementation.toast("Signup is not implemented yet");

            gotoState(LoginState.PROLOGUE);
        }
    }

    public class InputEmailHandler implements LoginEvents.LoginNavInputEmail {
        @Override
        public void gotEmail(String email) {
            ensureState(LoginState.INPUT_EMAIL);

            mContextImplementation.toast("Input email is not implemented yet. Input email: " + email);

            gotoState(LoginState.INPUT_EMAIL);
        }
    }

    public class InputSiteAddressHandler implements LoginEvents.LoginNavInputSiteAddress {
        @Override
        public void gotSiteAddress(String siteAddress) {
            ensureState(LoginState.INPUT_SITE_ADDRESS);

            mContextImplementation.toast("Input site address is not implemented yet. Input site address: " + siteAddress);

            gotoState(LoginState.INPUT_SITE_ADDRESS);
        }
    }

    @Override
    public LoginEvents.LoginNavPrologue getLoginNavPrologue() {
        return mPrologueHandler;
    }

    @Override
    public LoginEvents.LoginNavInputEmail getLoginNavInputEmail() {
        return mInputEmailHandler;
    }

    @Override
    public LoginEvents.LoginNavInputSiteAddress getLoginNavInputSiteAddress() {
        return mInputSiteAddressHandler;
    }
}
