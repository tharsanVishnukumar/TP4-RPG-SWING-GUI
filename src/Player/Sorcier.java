package Player;

import Utils.Position;

 public class Sorcier extends Player {
  public Sorcier(String name, Position position) {
    super(name, 120, position);
  }
  @Override
  public String getName() {
      return super.getName() + "Sorcier";
  }
}
