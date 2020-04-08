package sample;


public class TableField {

    private Ship ship;
    private int hshCd;

    public TableField(Ship ship, int hshCd) {
        this.ship = ship;
        this.hshCd = hshCd;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship){
        this.ship = ship;
    }

    public int getHshCd() {
        return hshCd;
    }

    public void setHshCd(int hshCd) {
        this.hshCd = hshCd;
    }
}
