package Entity;

import Utils.GameConfig;
import Utils.Position;
import java.awt.Color;

public class Monster extends GameEntity {
  public Monster(Position position) {
    super("Monster", 100, position);
  }

  @Override
  public void hit(double damageCount) {
    super.hit(damageCount);
  }

  @Override
  public String draw() {
    return "M";
  }

  @Override
  public Color getColor() {
    return Color.RED;
  }
}
