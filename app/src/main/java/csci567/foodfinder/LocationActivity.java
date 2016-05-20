package csci567.foodfinder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;


public class LocationActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        RestSlide.OnFragmentInteractionListener
{

    public static final String TAG = "Location Activity: ";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private ArrayList<String> place_ids = new ArrayList<>();
    private ArrayList<Restaurant> rest_list = new ArrayList<>();
    private ArrayList<Restaurant> dislikes = new ArrayList<>();
    private ArrayMap<String, Bitmap> images = new ArrayMap<>();
    private int last_count =0;
    private String failed_token;
    private int fail_count = 0;
    private static final int MAX_FAIL = 10;
    private int user_id;


    GoogleApiClient mGoogleApiClient;
    private Location m_location;

    private ViewPager m_pager;
    private PagerAdapter m_pager_adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_location);

        Firebase db_ref = LoginActivity.ref.child("account").child(LoginActivity.ref.getAuth().getUid());
        db_ref.child("dislikes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    dislikes.add(postSnapshot.getValue(Restaurant.class));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.location_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.rest_info:
                Intent intent = new Intent(this, UserInfoActivity.class);
                startActivityForResult(intent, 1);
                return true;
            case R.id.logout_menu_item:
                if(LoginActivity.ref.getAuth() != null) {
                    LoginActivity.ref.unauth();
                }
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();

    }
    public class readFromGooglePlaceAPI extends AsyncTask<String, Void, String> {
        @Override protected String doInBackground(String... param) {
            return readJSON(param[0]);
        }

        protected void onPostExecute(String str) {;
            try {
                fail_count = 0;
                JSONObject root = new JSONObject(str);
                JSONArray results = root.getJSONArray("results");
                Log.d(TAG, "Status: " + root.getString("status"));
                if(root.getString("status").equals("OK"))
                {
                    String next_token = root.getString("next_page_token");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject arrayItems = results.getJSONObject(i);
                        JSONObject geometry = arrayItems.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        String place_id = arrayItems.getString("place_id");

                        if(!contains(place_id))
                            place_ids.add(place_id);
                    }

                    for(int i = last_count; i < place_ids.size(); i++)
                    {
                        get_details(i);
                        get_images(i);
                    }
                    last_count = place_ids.size();
                    if(place_ids.size() < 25)
                    {
                        callAPI(next_token);
                    }
                }
                else
                {
                    if(fail_count < MAX_FAIL)
                        callAPI(failed_token);
                    Log.e(TAG, "HTTP status Error");
                }

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

        }
        public String readJSON(String URL) {
            StringBuilder sb = new StringBuilder();
            HttpGet httpGet = new HttpGet(URL);
            HttpClient client = new DefaultHttpClient();

            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    Log.e("JSON", "Couldn't find JSON file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }

    private void callAPI(String next_token)
    {
        if(m_location != null) {
            getLocation();
            if(next_token.equals("")) {
                new readFromGooglePlaceAPI().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=" + m_location.getLatitude() + "," + m_location.getLongitude() + "&rankby=distance&sensor=true&" +
                        "key=AIzaSyBcG7NTO3E5cJ6PznCxuLr17X-SRoWMwQo&types=food");
            }
            else {
                failed_token = next_token;

                new readFromGooglePlaceAPI().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                        "location=" + m_location.getLatitude() + "," + m_location.getLongitude() + "&rankby=distance&sensor=true&" +
                        "pagetoken=" + next_token + "&key=AIzaSyBcG7NTO3E5cJ6PznCxuLr17X-SRoWMwQo&types=food");
            }
        }
        else
        {
            Log.e(TAG, "No Location");
        }
    }

    private void getLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
        else
        {
            Location l = new Location("");
            l.setLatitude(39.7285);
            l.setLongitude(-121.8375);
            m_location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if(m_location == null)
            {
                m_location = l;
                Log.d(TAG, "Error getting location");
            }
        }


    }

    private void get_details(int position) {
        (Places.GeoDataApi.getPlaceById(mGoogleApiClient,place_ids.get(position)))
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                                        Place place = places.get(0);
                            String name = place.getName().toString();
                            String lat = String.valueOf(place.getLatLng().latitude);
                            String lng = String.valueOf(place.getLatLng().longitude);
                            String addr = place.getAddress().toString();
                            String phone = place.getPhoneNumber().toString();
                            if(phone.length() != 0)
                                phone = phone.substring(3);
                            int rating = Math.round(place.getRating()*10);
                            String id = place.getId();

                            rest_list.add(new Restaurant(name,addr,phone,rating,id, lat, lng));
                            m_pager_adapt.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, "Place not found");
                        }
                        places.release();
                    }
                });
    }

    private void get_images(final int position)
    {
        final String id = place_ids.get(position);
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, id)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
                    @Override
                    public void onResult(@NonNull PlacePhotoMetadataResult result) {
                        if (result.getStatus().isSuccess()) {
                            PlacePhotoMetadataBuffer photoMetadataBuffer = result.getPhotoMetadata();
                            if (photoMetadataBuffer.getCount() > 0) {
                                photoMetadataBuffer.get(0).getScaledPhoto(mGoogleApiClient, 600, 400)
                                        .setResultCallback(new ResultCallback<PlacePhotoResult>() {
                                            @Override
                                            public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                                                if (!placePhotoResult.getStatus().isSuccess()) {
                                                    return;
                                                }
                                                images.put(id, placePhotoResult.getBitmap());
                                                m_pager_adapt.notifyDataSetChanged();
                                            }
                                        });
                            }
                            photoMetadataBuffer.release();
                        }
                    }
                });

        }

    private boolean contains(String id)
    {
        for(int i = 0; i < dislikes.size(); i++)
        {
            if(id.equals(dislikes.get(i).getM_id()))
            {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "XXXXXXXXXXXXXXX Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location

        getLocation();
        callAPI("");
        m_pager = (ViewPager) findViewById(R.id.pager);
        m_pager_adapt = new RestSlideAdapter(getSupportFragmentManager(), rest_list, images);
        m_pager.setAdapter(m_pager_adapt);
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

