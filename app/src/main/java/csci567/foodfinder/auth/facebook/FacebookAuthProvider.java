package csci567.foodfinder.auth.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.Firebase;
import csci567.foodfinder.auth.core.FirebaseAuthProvider;
import csci567.foodfinder.auth.core.FirebaseResponse;
import csci567.foodfinder.auth.core.FirebaseLoginError;
import csci567.foodfinder.auth.core.FirebaseOAuthToken;
import csci567.foodfinder.auth.core.AuthProviderType;
import csci567.foodfinder.auth.core.TokenAuthHandler;

import java.util.Arrays;
import java.util.Collection;

public class FacebookAuthProvider extends FirebaseAuthProvider {

    private final String TAG = "FacebookAuthProvider";
    public CallbackManager mCallbackManager;
    private LoginManager mLoginManager;
    private Boolean isReady = false;

    public FacebookAuthProvider(Context context, AuthProviderType providerType, String providerName, Firebase ref, final TokenAuthHandler handler) {
        super(context, providerType, providerName, ref, handler);
        FacebookSdk.sdkInitialize(context.getApplicationContext());

        mLoginManager = LoginManager.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        mLoginManager.registerCallback(mCallbackManager,
            new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    AccessToken token = loginResult.getAccessToken();

                    FirebaseOAuthToken foToken = new FirebaseOAuthToken(
                            getProviderName(),
                            token.getToken().toString());

                    onFirebaseTokenReceived(foToken, handler);
                }

                @Override
                public void onCancel() {
                    getHandler().onUserError(new FirebaseLoginError(FirebaseResponse.LOGIN_CANCELLED, "User closed login dialog."));
                }

                @Override
                public void onError(FacebookException ex) {
                    getHandler().onProviderError(new FirebaseLoginError(FirebaseResponse.MISC_PROVIDER_ERROR, ex.toString()));
                }
            }
        );

        String facebookAppId = "";

        try {
            ApplicationInfo ai = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            facebookAppId = bundle.getString("com.facebook.sdk.ApplicationId");
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NullPointerException e) {}

        if (facebookAppId == null) {
            getHandler().onProviderError(new FirebaseLoginError(FirebaseResponse.MISSING_PROVIDER_APP_ID, "Missing Facebook Application ID, is it set in your AndroidManifest.xml?"));
            return;
        }

        if (facebookAppId.compareTo("") == 0) {
            getHandler().onProviderError(new FirebaseLoginError(FirebaseResponse.INVALID_PROVIDER_APP_ID, "Invalid Facebook Application ID, is it set in your res/values/strings.xml?"));
            return;
        }

        isReady = true;
    }

    public void login() {
        if (isReady) {
            Collection<String> permissions = Arrays.asList("public_profile");
            mLoginManager.logInWithReadPermissions((Activity)getContext(), permissions);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void logout() {
        mLoginManager.logOut();
    }
}
