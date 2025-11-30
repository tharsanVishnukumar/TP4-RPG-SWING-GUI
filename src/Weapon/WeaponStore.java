package Weapon;

import java.util.ArrayList;

import Entity.GameEntity;
import Player.Player;
import Utils.GameConfig;
import Utils.Position;
import java.awt.Color;

public class WeaponStore extends GameEntity {
  private ArrayList<Weapon> weapons;

  public WeaponStore(ArrayList<Weapon> weapons, Position position) {
    this.weapons = weapons;
    super("Magasin d'armes", -1, position);
  }

  public ArrayList<Weapon> getWeapons() {
    return weapons;
  }

  public void setWeapons(ArrayList<Weapon> weapons) {
    this.weapons = weapons;
  }

  public String buyWeapon(Player player, int weaponIndex) {
    var inventery = player.getInventery();
    if (weaponIndex < 0 || weaponIndex >= weapons.size()) {
      return "Arme invalide.";
    }

    var weapon = getWeapons().get(weaponIndex);
    if (inventery.getMoney() < weapon.getPrice()) {
      return "Vous n'avez pas assez d'argent pour acheter cette arme.";
    }
    inventery.addWeapon(weapon);
    inventery.consumeMoney(weapon.getPrice());
    return "vous avez acheté " + weapon.getName() + " pour " + weapon.getPrice() + "£.";
  }

  public String draw() {
    return "W";
  }

  @Override
  public Color getColor() {
    return Color.ORANGE;
  }

  public String showStoreInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("Magasin d'armes: \n");
    sb.append("\tArmes disponibles: \n");
    for (int i = 0; i < weapons.size(); i++) {
      var weapon = weapons.get(i);
      sb
          .append("[").append(i).append("]: ")
          .append(weapon.getName())
          .append(" | dpc: ")
          .append(weapon.getDamage())
          .append(" | prix: ")
          .append(weapon.getPrice())
          .append("£ | \n")
          .append(weapon.draw())
          .append(" \n");
    }
    return sb.toString();
  }

}
