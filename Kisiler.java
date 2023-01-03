package Model;

public class Kisiler {

    String ad,durum;

    public Kisiler()
    {

    }

    public Kisiler (String ad, String durum)
    {
        this.ad =ad;
        this.durum =durum;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}

