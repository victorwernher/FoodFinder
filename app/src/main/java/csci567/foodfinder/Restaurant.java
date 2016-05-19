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


    public Restaurant() {
    }

    public Restaurant(String name, String address)
    {
        m_name = name;
        m_address = address;
        m_phone_num = "555-5555";
        m_rating = 0;
    }

    public Restaurant(String m_name, String m_address, String m_phone_num, int m_rating, String m_id,
                      String m_lat, String m_lng)
    {
        this.m_name = m_name;
        this.m_address = m_address;
        this.m_phone_num = m_phone_num;
        this.m_rating = m_rating;
        this.m_id = m_id;
        this.m_lat = m_lat;
        this.m_lng = m_lng;
    }

//    public void setImage(Bitmap img)
//    {
//        m_image = img;
//    }

    public String getM_name()
    {
        return m_name;
    }

    public String getM_address()
    {
        return m_address;
    }

    public String getM_phone_num()
    {
        return m_phone_num;
    }

    public int getM_rating()
    {
        return m_rating;
    }

    public String getM_id(){ return m_id; };

    public String getM_lat(){ return m_lat; };

    public String getM_lng(){ return m_lng; };


}
