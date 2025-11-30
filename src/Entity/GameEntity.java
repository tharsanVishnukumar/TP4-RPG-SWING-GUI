package Entity;

import Drawable.Drawable;
import Utils.Position;
import java.awt.Color;

public abstract class GameEntity extends Attaquable implements MapElement {
  public GameEntity(String name, double half, Position basePosition) {
    this.position = basePosition;
    this.name = name;
    super(half);
  }

  private String name;
  private Position position;

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getHealthBar() {
    int totalBars = 10;
    int filledBars = (int) ((this.getHalf() / this.getMaxHalf()) * totalBars);
    StringBuilder healthBar = new StringBuilder("[");
    for (int i = 0; i < totalBars; i++) {
      if (i < filledBars) {
        healthBar.append("█");
      } else {
        healthBar.append(" ");
      }
    }
    healthBar.append("]");
    return healthBar.toString();
  }

  public String getHealthInfo() {
    return String.format("%s %s %.0f/%.0f", this.name, this.getHealthBar(), this.getHalf(), this.getMaxHalf());
  }

  public String getInfo() {
    StringBuilder sb = new StringBuilder();
    sb.append("Nom: ").append(this.getName()).append("\n");
    sb.append("PV: ").append(this.getHealthInfo()).append("\n");
    return sb.toString();
  }

  @Override
  public String draw() {
    return String.valueOf(getName().charAt(0)).toUpperCase();
  }

  public abstract Color getColor();
}
