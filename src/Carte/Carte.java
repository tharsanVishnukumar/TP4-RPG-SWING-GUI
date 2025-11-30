package Carte;

import Drawable.Drawable;
import Entity.MapElement;
import Utils.GameConfig;
import Utils.Position;
import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;

public class Carte<T extends MapElement> implements Drawable {
  public Carte(int width, int height) {
    this.width = width;
    this.height = height;
    entities = new ArrayList<>();
  }
  private ArrayList<T> entities;
  private int width;
  private int height;
  public int getWidth() {
    return width;
  }
  public int getHeight() {
    return height;
  }

  public boolean isPositionFree(Position position) {
    var entity = getEntityAtPosition(position);
    if (entity != null) {
      return false;
    }
    return true;
  }
  private T getEntityAtPosition(Position position) {
    for (T entity : entities) {
      if (entity.getPosition().equals(position)) {
        return entity;
      }
    }
    return null;
  }
  public  T getEntityAt(int x, int y) {
    Position position = new Position(x, y);
    return getEntityAtPosition(position);
  }
  public ArrayList<T> getAllEntitiesAt(Position position) {
    ArrayList<T> foundEntities = new ArrayList<>();
    for (T entity : entities) {
      if (entity.getPosition().equals(position)) {
        foundEntities.add(entity);
      }
    }
    return foundEntities;
  }

  public void addEntity(T entity) {
    entities.add(entity);
  }

  public void removeEntity(T entity){
    entities.remove(entity);
  }
  @Override
  public String draw() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < GameConfig.CARTE_HEIGHT; y++) {
      for (int x = 0; x < GameConfig.CARTE_WIDTH; x++) {
        T entity = getEntityAt(x, y);
        if (entity == null) {
          sb.append("{ } ");
        } else {
          sb.append("{" + entity.draw() + "} ");
        }
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  @Override
  public Color getColor() {
    return Color.WHITE;
  }

  @Override
  public void paint(Graphics g, int x, int y, int width, int height) {
    int cellWidth = width / this.width;
    int cellHeight = height / this.height;

    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        int px = x + j * cellWidth;
        int py = y + i * cellHeight;

        // Draw background
        g.setColor(Color.WHITE);
        g.fillRect(px, py, cellWidth, cellHeight);
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(px, py, cellWidth, cellHeight);

        // Draw entity
        T entity = getEntityAt(j, i);
        if (entity != null) {
          entity.paint(g, px, py, cellWidth, cellHeight);
        }
      }
    }
  }
}
