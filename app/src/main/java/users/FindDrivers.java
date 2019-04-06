package users;

public class FindDrivers {
    private String profilURl, first_name,last_name, id,phone;
    private boolean is_sharing;


    public FindDrivers(String profilURl, String first_name) {
        this.profilURl = profilURl;
        this.first_name = first_name;
    }

    public FindDrivers(String profilURl, String first_name,String last_name) {
        this.profilURl = profilURl;
        this.first_name = first_name;
        this.last_name = last_name;
    }
    public FindDrivers(String profilURl, String first_name,String last_name, String id) {
        this.profilURl = profilURl;
        this.first_name = first_name;
        this.last_name = last_name;
        this.id=id;
    }

    public FindDrivers(String profilURl, String first_name,String last_name, String id,String phone) {
        this.profilURl = profilURl;
        this.first_name = first_name;
        this.last_name = last_name;
        this.id=id;
        this.phone=phone;
    }



    public FindDrivers(String profilURl, String first_name,String last_name,String id, boolean is_sharing) {
        this.profilURl = profilURl;
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
        this.is_sharing = is_sharing;
    }


    public FindDrivers(){

    }

    public String getProfilURl() {
        return profilURl;
    }

    public void setProfilURl(String profilURl) {
        this.profilURl = profilURl;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }


    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone(){return phone;}

    public void setPhone(String phone) {this.phone=phone;}

    public boolean isIs_sharing() {
        return is_sharing;
    }

    public void setIs_sharing(boolean is_sharing) {
        this.is_sharing = is_sharing;
    }
}
