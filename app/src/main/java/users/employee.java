package users;

public class employee {

    private String email;
    private String password;
    private String phone;
    private String profilURl;
    private String first_name;
    private String last_name;
    private String nip;
    private String position;


    private String lat;
    private String lng;

    public employee(){
    }

    public employee(String email, String password, String phone, String first_name,String last_name, String profilURl, String nip) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.first_name = first_name;
        this.last_name=last_name;
        this.profilURl = profilURl;
        this.nip = nip;
    }

    public employee(String email, String password, String phone, String first_name,String last_name, String profilURl, String nip,String position) {
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.first_name = first_name;
        this.last_name=last_name;
        this.profilURl = profilURl;
        this.nip = nip;
        this.position=position;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
