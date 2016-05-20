package csci567.foodfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import csci567.foodfinder.auth.core.AuthProviderType;
import csci567.foodfinder.auth.core.FirebaseLoginBaseActivity;
import csci567.foodfinder.auth.core.FirebaseLoginError;

/*
 * This initial class handles the login and then launches the main activity.
 *
 * While you can log in from any gmail or facebook accounts, the following are the
 * test accounts I use.
 *
 * username for all logins(email, facebook, google): csci567foodfinder@gmail.com
 * password for all logins: #!FF22536
 *
 * FirebaseUI project was imported into this project. FirebaseArray, FirebaseListAdapter,
 * FirebaseRecyclerAdapter, and the whole auth folder are from it.
 */
public class LoginActivity extends FirebaseLoginBaseActivity {

    /*
     * For now leaving this public static
     */
    public static Firebase ref;
    public static final int MAIN_ACTIVITY_LOGOUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        ref = new Firebase(this.getString(R.string.firebase_url));
    }

    @Override
    public Firebase getFirebaseRef() {
        return ref;
    }

    /*
     * Login Errors
     */
    @Override
    public void onFirebaseLoginProviderError(FirebaseLoginError firebaseError) {
        resetFirebaseLoginPrompt();
    }

    @Override
    public void onFirebaseLoginUserError(FirebaseLoginError firebaseError) {
        resetFirebaseLoginPrompt();
    }

    /*
     * When activity starts and a state is detected, one of these will go off.
     */
    @Override
    public void onFirebaseLoggedIn(AuthData authData) {
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onFirebaseLoggedOut() {
        setContentView(R.layout.activity_login);
    }

    /*
     * Gets result from activity, currently for logout.
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == MAIN_ACTIVITY_LOGOUT) {
                setEnabledAuthProvider(AuthProviderType.FACEBOOK);
                setEnabledAuthProvider(AuthProviderType.GOOGLE);
                showFirebaseLoginPrompt();
            }
        }
    }

    /*
     * Automatically launches login on start if not logged in.
     * After log out can only use log in button.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(ref.getAuth() == null){
            setEnabledAuthProvider(AuthProviderType.FACEBOOK);
            setEnabledAuthProvider(AuthProviderType.GOOGLE);
            showFirebaseLoginPrompt();

        }

    }
}