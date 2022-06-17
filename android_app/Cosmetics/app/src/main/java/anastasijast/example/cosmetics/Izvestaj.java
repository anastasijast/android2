package anastasijast.example.cosmetics;

public class Izvestaj {
    public String userID;
    public String salonID;
    public Float rejting;
    public Izvestaj(){}
    public Izvestaj(String userID, String salonID, Float rejting){
        this.userID=userID;
        this.salonID=salonID;
        this.rejting=rejting;
    }

    public Float getRejting() {
        return rejting;
    }
}
