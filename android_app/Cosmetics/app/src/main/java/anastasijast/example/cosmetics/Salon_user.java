package anastasijast.example.cosmetics;

public class Salon_user {
    public String rabotno;
    public String lokacija;
    public Salon_user(){

    }
    public Salon_user(String rabotno, String lokacija){
        this.rabotno=rabotno;
        this.lokacija=lokacija;
    }
    public String getRabotno(){return rabotno;}
    public String getLokacija(){return lokacija;}
}
