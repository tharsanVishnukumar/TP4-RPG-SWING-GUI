package Player;

import java.util.ArrayList;
import java.awt.Color;

import Drawable.Drawable;
import Weapon.Weapon;

public class Inventery implements Drawable {
  public Inventery(double money) {
    this.money = money;
  }

  private ArrayList<Weapon> weapons = new ArrayList<>();
  private Weapon selectedWeapon = null;
  private double money = 0;

  public void addWeapon(Weapon weapon) {
    weapons.add(weapon);
    if (selectedWeapon == null) {
      selectedWeapon = weapon;
    }
  }

  public Weapon getWeapon(int index) {
    if (index < 0 || index >= weapons.size()) {
      return null;
    }
    return weapons.get(index);
  }


  public ArrayList<Weapon> getWeapons() {
    return weapons;
  }

  public void setWeapons(ArrayList<Weapon> weapons) {
    this.weapons = weapons;
  }

  public Weapon getSelectedWeapon() {
    return selectedWeapon;
  }

  public void setSelectedWeapon(Weapon selectedWeapon) {
    this.selectedWeapon = selectedWeapon;
  }

  public double getMoney() {
    return money;
  }

  public void creditMoney(double amount) {
    this.money += amount;
  }
  public void consumeMoney(double amount) {
    this.money -= amount;
  }

  @Override
  public String draw() {
    StringBuilder sb = new StringBuilder();
    sb.append("Inventaire: \n");
    sb.append("\tArgent: ").append(money).append("£").append("\n");
    sb.append("\tArmes: \n");
    if (!weapons.isEmpty()) {
      for (int i = 0; i < weapons.size(); i++) {
        var weapon = weapons.get(i);
        sb
            .append("\t\t[").append(i).append("]: ")
            .append(weapon.getName())
            .append(" | dpc: ")
            .append(weapon.getDamage())
            .append(" | ")
            .append(weapon.draw())
            .append(" ");
      }
    } else {
      sb.append("\tAucun arme dans l'inventaire.\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  @Override
  public Color getColor() {
    return Color.WHITE;
  }
}
