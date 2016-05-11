package csci567.foodfinder.auth.google;

import csci567.foodfinder.auth.core.FirebaseLoginError;

interface GoogleOAuthTaskHandler {
    public void onOAuthSuccess(String token);
    public void onOAuthFailure(FirebaseLoginError firebaseError);
}
