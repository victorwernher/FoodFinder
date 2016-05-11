package csci567.foodfinder.auth.core;

public class FirebaseLoginError {
    public String message;
    public FirebaseResponse error;

    public FirebaseLoginError(FirebaseResponse error, String message) {
        this.message = message;
        this.error = error;
    }

    public String toString() {
        return error.toString() + ": " + message;
    }

}
