package csci567.foodfinder;


import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

/**
 * Created by bradley on 5/10/16.
 */
public class RestSlideAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Restaurant> m_list;
    private ArrayMap<String, Bitmap> m_imgs;

    public RestSlideAdapter(FragmentManager fm, ArrayList<Restaurant> list, ArrayMap<String, Bitmap> imgs) {
        super(fm);
        m_list = list;
        m_imgs = imgs;
    }

    @Override
    public Fragment getItem(int position) {
        RestSlide frag = RestSlide.init();
        String id = m_list.get(position).getM_id();
        Restaurant rest = m_list.get(position);
        frag.setRest(rest);
        try {
            frag.setImg(m_imgs.get(id));
        }
        catch (Exception e) {
            frag.setImg(null);
        }
        return frag;
    }

    @Override
    public int getCount() {
        if(m_list == null)
        {
            return 0;
        }
        return m_list.size();
    }
}
