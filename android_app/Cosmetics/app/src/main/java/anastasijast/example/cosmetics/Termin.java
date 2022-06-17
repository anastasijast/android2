package anastasijast.example.cosmetics;

public class Termin {
    public String vreme;
    public String datum;
    public String korisnik;
    public String salon;
    public int status;

    public Termin(){

    }
    public Termin(String vreme,String datum, String korisnik, String salon,int status){
        this.vreme=vreme;
        this.datum=datum;
        this.korisnik=korisnik;
        this.salon=salon;
        this.status=status;
    }
    public String getVreme(){return vreme;}
    public String getDatum() {return datum;}
    public String getKorisnik(){return korisnik;}
    public String getSalon(){return salon;}

    public int getStatus() {
        return status;
    }
}
