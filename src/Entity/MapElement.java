package Entity;

import Drawable.Drawable;
import Utils.Position;

public interface MapElement extends Drawable {
    Position getPosition();
    void setPosition(Position position);
}
