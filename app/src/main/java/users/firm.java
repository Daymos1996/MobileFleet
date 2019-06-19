package users;

public class firm {


    private String nip;
    private String nameFirm;
    private String adressFirm;


    public firm(){
    }

    public firm(String nip, String nameFirm,String adressFirm) {

        this.nip = nip;
        this.nameFirm=nameFirm;
        this.adressFirm=adressFirm;
    }


    public String getNip() {
        return nip;
    }
    public void setNip(String nip) {
        this.nip = nip;
    }
    public String getNameFirm() { return nameFirm; }
    public void setNameFirm(String nameFirm) { this.nameFirm = nameFirm; }
    public String getAdressFirm() { return adressFirm; }
    public void setAdressFirm(String adressFirm) { this.adressFirm = adressFirm; }
}
