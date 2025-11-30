package Drawable;

import java.awt.Color;
import java.awt.Graphics;

public interface Drawable {
  public String draw();
  public Color getColor();

  default void paint(Graphics g, int x, int y, int width, int height) {
    g.setColor(getColor());
    g.fillRect(x + 5, y + 5, width - 10, height - 10);

    g.setColor(Color.WHITE);
    String symbol = draw();
    if (symbol != null && symbol.length() > 1)
      symbol = symbol.substring(0, 1);
    if (symbol != null)
      g.drawString(symbol, x + width / 2 - 3, y + height / 2 + 3);
  }
}
