package csci567.foodfinder;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Firebase database_ref;
    private ArrayList<Restaurant> likes;
    private ArrayList<Restaurant> dislikes;

    public Database() {
        database_ref = LoginActivity.ref.child("account").child(LoginActivity.ref.getAuth().getUid());
        likes = new ArrayList<Restaurant>();
        dislikes = new ArrayList<Restaurant>();
    }

    public void add_like(Restaurant like) {
       database_ref.child("likes").child(like.getM_id()).setValue(like);
    }

    public void add_dislike(Restaurant dislike) {
        database_ref.child("dislikes").child(dislike.getM_id()).setValue(dislike);
    }

    public void delete_like(String like_id) {
        database_ref.child("likes").child(like_id).removeValue();
    }

    public void delete_dislike(String dislike_id) {
        database_ref.child("dislikes").child(dislike_id).removeValue();
    }

    public ArrayList<Restaurant> get_likes() {
        database_ref.child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    likes.add(postSnapshot.getValue(Restaurant.class));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return likes;
    }
    public ArrayList<Restaurant> get_dislikes() {
        database_ref.child("dislikes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Restaurant dislike = postSnapshot.getValue(Restaurant.class);
                    dislikes.add(dislike);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        return dislikes;
    }

    public ArrayList<Restaurant> get_filled_likes()
    {
        return likes;
    }

}
