package Weapon;

public class Axe extends Weapon {
    public Axe() {}
    public Axe(String name, int power, float price) {
        super(name, power, price);
    }
    public String draw(){
          return "A";
    }

}
