package csci567.foodfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {

    private int user_id;
    private ArrayList<Restaurant> m_likes = new ArrayList<>();
    private ArrayList<Restaurant> m_dislikes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            user_id = extras.getInt("user_id");
        }

        /*
        TODO Get data from database set equal to m_likes & m_dislikes
         */

        ListView likes = (ListView) findViewById(R.id.like_list);
        assert likes != null;
        likes.setAdapter(new RestaurantDataAdapter(this, m_likes));
        ListView dislikes = (ListView) findViewById(R.id.dislike_list);
        assert dislikes != null;
        likes.setAdapter(new RestaurantDataAdapter(this, m_dislikes));


    }
}
