package users;

public class FindCars {
    private String carUrl, carBrand ,plateNumber, vinNumber,carMileage,engineCapacity,motorPower,yearProduction,technicalExamination ,oc,nip;

    public FindCars(){

    }

    public FindCars(String carUrl, String carBrand, String plateNumber){

        this.carUrl=carUrl;
        this.carBrand=carBrand;
        this.plateNumber=plateNumber;
    }

    public FindCars(String carUrl,String carBrand,String plateNumber,String vinNumber,String carMileage,String engineCapacity,
                    String motorPower,String yearProduction,String technicalExamination,String oc,String nip){

        this.carUrl= carUrl;
        this.carBrand=carBrand;
        this.plateNumber=plateNumber;
        this.vinNumber=vinNumber;
        this.carMileage=carMileage;
        this.engineCapacity=engineCapacity;
        this.motorPower=motorPower;
        this.yearProduction=yearProduction;
        this.technicalExamination=technicalExamination;
        this.oc=oc;
        this.nip=nip;
    }

    public FindCars(String carBrand,String plateNumber,String vinNumber,String carMileage,String engineCapacity,
                    String motorPower,String yearProduction,String technicalExamination,String oc,String nip){

        this.carBrand=carBrand;
        this.plateNumber=plateNumber;
        this.vinNumber=vinNumber;
        this.carMileage=carMileage;
        this.engineCapacity=engineCapacity;
        this.motorPower=motorPower;
        this.yearProduction=yearProduction;
        this.technicalExamination=technicalExamination;
        this.oc=oc;
        this.nip=nip;
    }

    public String getCarUrl() {
        return carUrl;
    }
    public void setCarUrl(String carUrl) {
        this.carUrl = carUrl;
    }

    public String getCarBrand() {
        return carBrand;
    }
    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getPlateNumber() {return plateNumber;}
    public void setPlateNumber(String plateNumber) {this.plateNumber=plateNumber;}

    public String getVinNumber(){return vinNumber;}
    public void setVinNumber(String vinNumber) {this.vinNumber=vinNumber;}

    public String getCarMileage() {return carMileage;}
    public void setCarMileage(String carMileage) {this.carMileage=carMileage;}

    public String getEngineCapacity() {return engineCapacity;}
    public void setEngineCapacity(String engineCapacity) {this.engineCapacity=engineCapacity;}

    public String getMotorPower() {return motorPower;}
    public void setMotorPower(String motorPower) {this.motorPower=motorPower;}

    public String getYearProduction() {return yearProduction;}
    public void setYearProduction(String yearProduction) {this.yearProduction=yearProduction;}

    public String getTechnicalExamination() {return technicalExamination;}
    public void setTechnicalExamination(String technicalExamination) {this.technicalExamination=technicalExamination;}

    public String getOc() {return oc;}
    public void setOc(String oc) {this.oc=oc;}

    public String getNip() {return  nip;}
    public void setNip(String nip) {this.nip=nip;}



}
