package Player;

import Entity.GameEntity;
import Utils.GameConfig;
import Utils.Position;
import java.awt.Color;

public abstract class Player extends GameEntity {
  private int xp = 0;
  public int getXp() {
    return xp;
  }
  public void addXp(int xp) {
    this.xp += xp;
  }

  private Inventery inventery;

  public Inventery getInventery() {
    return inventery;
  }

  public Player(String name, int halt, Position position) {
    super(name, halt, position);
    this.inventery = new Inventery(10);
  }

  @Override
  public String draw() {
    return "P";
  }

  @Override
  public Color getColor() {
    return Color.BLUE;
  }
}
