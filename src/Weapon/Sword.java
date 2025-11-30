package Weapon;

public class Sword extends Weapon {
  public Sword(String name, int power, float price) {
    super(name, power, price);
  }

  @Override
  public String draw() {
    return "S";
  }
}
