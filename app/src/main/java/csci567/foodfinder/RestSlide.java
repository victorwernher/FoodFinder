package csci567.foodfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestSlide.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link} factory method to
 * create an instance of this fragment.
 */
public class RestSlide extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Restaurant m_rest;
    private Bitmap m_img;

    private OnFragmentInteractionListener mListener;

    public RestSlide() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RestSlide init() {
        RestSlide fragment = new RestSlide();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int rate = m_rest.getRating();
        String s = rate + " out of 5";

        View view = inflater.inflate(R.layout.fragment_rest_slide, container, false);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        name.setText(m_rest.getName());
        TextView phone = (TextView) view.findViewById(R.id.tv_phone);
        phone.setText(m_rest.getPhoneNum());
        TextView rating = (TextView) view.findViewById(R.id.tv_rating);
        if(rate > 0 )
            rating.setText(s);
        else
            rating.setText("");
        TextView addr = (TextView) view.findViewById(R.id.tv_addr);
        addr.setText(m_rest.getAddress());
        ImageView img = (ImageView) view.findViewById(R.id.rest_image);
        if(m_img != null)
//            img.setImageResource(R.mipmap.ic_launcher);
//        else
            img.setImageBitmap(m_img);
        return view;
    }

    public void setRest(Restaurant rest)
    {
        m_rest = rest;
    }

    public void setImg(Bitmap img)
    {
        m_img = img;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
