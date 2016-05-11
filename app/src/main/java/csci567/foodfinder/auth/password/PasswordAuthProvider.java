package csci567.foodfinder.auth.password;

import android.content.Context;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import csci567.foodfinder.auth.core.FirebaseAuthProvider;
import csci567.foodfinder.auth.core.FirebaseResponse;
import csci567.foodfinder.auth.core.FirebaseLoginError;
import csci567.foodfinder.auth.core.AuthProviderType;
import csci567.foodfinder.auth.core.TokenAuthHandler;

public class PasswordAuthProvider extends FirebaseAuthProvider {

    private final String LOG_TAG = "PasswordAuthProvider";

    public PasswordAuthProvider(Context context, AuthProviderType providerType, String providerName, Firebase ref, TokenAuthHandler handler) {
        super(context, providerType, providerName, ref, handler);
    }

    public void login(String email, String password) {
        getFirebaseRef().authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                getHandler().onSuccess(authData);
            }
            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                getHandler().onUserError(new FirebaseLoginError(FirebaseResponse.MISC_PROVIDER_ERROR, firebaseError.toString()));
            }
        });
    }

    public void logout() {}
}
