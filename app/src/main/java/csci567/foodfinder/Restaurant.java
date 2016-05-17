package csci567.foodfinder;

/**
 * Created by bradley on 5/10/16.
 */
public class Restaurant {
    private String m_name;
    private String m_address;
    private String m_phone_num;
    private int m_rating;
    private String m_id;
    private String m_lat;
    private String m_lng;

    public Restaurant(String name, String address)
    {
        m_name = name;
        m_address = address;
        m_phone_num = "555-5555";
        m_rating = 0;
    }

    public Restaurant(String name, String address, String phone_num, int rating, String id,
                      String lat, String lng)
    {
        m_name = name;
        m_address = address;
        m_phone_num = phone_num;
        m_rating = rating;
        m_id = id;
        m_lat = lat;
        m_lng = lng;
    }

//    public void setImage(Bitmap img)
//    {
//        m_image = img;
//    }

    public String getName()
    {
        return m_name;
    }

    public String getAddress()
    {
        return m_address;
    }

    public String getPhoneNum()
    {
        return m_phone_num;
    }

    public int getRating()
    {
        return m_rating;
    }

    public String getId(){ return m_id; };

    public String getLat(){ return m_lat; };

    public String getLng(){ return m_lng; };


}
