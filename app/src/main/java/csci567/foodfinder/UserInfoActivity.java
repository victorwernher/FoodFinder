package csci567.foodfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class UserInfoActivity extends AppCompatActivity {

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            user_id = extras.getInt("user_id");
        }
    }
}
