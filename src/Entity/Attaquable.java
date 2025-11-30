package Entity;


public abstract class Attaquable {
    double half;
    double maxHalf;
    Attaquable(double half) {
        this.half = half;
        this.maxHalf = half;
    }
    public void hit(double damageCount){
        this.half -= damageCount;
    }
    public boolean isDead(){
        return this.half <= 0;
    }

    public double getMaxHalf() {
        return maxHalf;
    }
    public double getHalf() {
        return half;
    }
}
