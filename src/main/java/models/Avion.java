public class Avion {
    private String etat,HourD,HourA,NumV,AeroD,AeroA,TV;
    private static int id = 0;
    private int refA,Speed;

    public Avion(){
        id++;
        refA=id;
    }

    public Avion(String etat,String HourD,String HourA,String NumV,String AeroD,String AeroA,String TV,int Speed){
        this.etat = etat;
        this.HourD = HourD;
        this.HourA = HourA;
        this.NumV = NumV;
        this.AeroD = AeroD;
        this.AeroA = AeroA;
        this.TV = TV;
        this.Speed = Speed;
        refA++;
    }

    public void setEtat(String etat){
        this.etat = etat;
    }

    public String getEtat(){
        return etat;
    }

    public void setHourD(String HourD){
        this.HourD = HourD;
    }

    public String getHourD(){
        return HourD;
    }

    public void setHourA(String HourA){
        this.HourA = HourA;
    }

    public String getHourA(){
        return HourA;
    }

    ///
    public void setNumV(String NumV){
        this.NumV = NumV;
    }

    public String getNumV(){
        return NumV;
    }

    public void setAeroD(String AeroD){
        this.AeroD = AeroD;
    }

    public String getAeroD(){
        return AeroD;
    }

    public void setAeroA(String AeroA){
        this.AeroA = AeroA;
    }

    public String getAeroA(){
        return AeroA;
    }

    public int getrefA(){
        return refA;
    }

    public void setTV(String TV){
        this.TV = TV;
    }

    public String getTV(){
        return TV;
    }

    public void setSpeed(int Speed){
        this.Speed = Speed;
    }

    public int getSpeed(){
        return Speed;
    }
}
