package csci567.foodfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {

    private int user_id;
    private ArrayList<Restaurant> m_likes = new ArrayList<>();
    private ArrayList<Restaurant> m_dislikes = new ArrayList<>();
    private RestaurantDataAdapter m_likes_adapter;
    private RestaurantDataAdapter m_dislikes_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Firebase db_ref = LoginActivity.ref.child("account").child(LoginActivity.ref.getAuth().getUid());
        Firebase db_ref2 = LoginActivity.ref.child("account").child(LoginActivity.ref.getAuth().getUid());

        db_ref.child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                m_likes = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    m_likes.add(postSnapshot.getValue(Restaurant.class));
                }

                    setLikesAdapter();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        db_ref2.child("dislikes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                m_dislikes = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    m_dislikes.add(postSnapshot.getValue(Restaurant.class));
                }
                    setDislikesAdapter();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

    }
    @Override
    protected void onStart()
    {
        super.onStart();

    }

    private void setLikesAdapter()
    {
        ListView likes = (ListView) findViewById(R.id.like_list);
        assert likes != null;
        likes.invalidate();
        m_likes_adapter = new RestaurantDataAdapter(this, m_likes, "like");
        likes.setAdapter(m_likes_adapter);
    }

    private void setDislikesAdapter()
    {
        ListView dislikes = (ListView) findViewById(R.id.dislike_list);
        assert dislikes != null;
        dislikes.invalidate();
        m_dislikes_adapter = new RestaurantDataAdapter(this, m_dislikes, "dislike");
        dislikes.setAdapter(m_dislikes_adapter);
    }

}
