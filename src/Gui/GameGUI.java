package Gui;

import Carte.Carte;
import Drawable.Drawable;
import Entity.MapElement;
import Entity.GameEntity;
import Entity.Monster;
import Player.Elfe;
import Player.Player;
import Player.Sorcier;
import Player.Inventery;
import Utils.GameConfig;
import Utils.Position;
import Weapon.Axe;
import Weapon.Sword;
import Weapon.Weapon;
import Weapon.WeaponStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameGUI extends JFrame {
  private Carte<GameEntity> map;
  private Player player;
  private WeaponStore weaponStore;
  private Monster fightingMonster;
  private boolean isFightMode = false;
  private boolean isInStoreMode = false;

  private GridPanel gridPanel;
  private JPanel controlsPanel;
  private JTextArea infoArea;
  private JLabel messageLabel;

  public GameGUI() {
    super("RPG GUI");
    this.map = new Carte<>(GameConfig.CARTE_WIDTH, GameConfig.CARTE_HEIGHT);
    this.player = createPlayer();
    map.addEntity(player);
    createMonsters();
    createStores();
    setupUI();
    updateView("Bienvenue!");
  }

  private void setupUI() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800, 600);
    setLayout(new BorderLayout());

    gridPanel = new GridPanel();
    add(gridPanel, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BorderLayout());

    controlsPanel = new JPanel();
    bottomPanel.add(controlsPanel, BorderLayout.NORTH);

    infoArea = new JTextArea(6, 20);
    infoArea.setEditable(false);
    bottomPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);

    messageLabel = new JLabel("");
    bottomPanel.add(messageLabel, BorderLayout.SOUTH);

    add(bottomPanel, BorderLayout.SOUTH);

    updateControls();

    // Key Bindings
    InputMap im = gridPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = gridPanel.getActionMap();

    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0), "up");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "down");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "left");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "right");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "inv");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "attack");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "attack");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0), "shop");

    am.put("up", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        movePlayer("z");
      }
    });
    am.put("down", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        movePlayer("s");
      }
    });
    am.put("left", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        movePlayer("q");
      }
    });
    am.put("right", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        movePlayer("d");
      }
    });
    am.put("inv", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        showInventory();
      }
    });
    am.put("attack", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        attackMonster();
      }
    });
    am.put("shop", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (isPlayerOnStore())
          openShop();
      }
    });
  }

  private void updateControls() {
    controlsPanel.removeAll();
    if (isFightMode) {
      JButton attack = new JButton("Attaquer (A)");
      attack.addActionListener(e -> attackMonster());
      controlsPanel.add(attack);
    } else {
      JButton up = new JButton("Haut (Z)");
      JButton down = new JButton("Bas (S)");
      JButton left = new JButton("Gauche (Q)");
      JButton right = new JButton("Droite (D)");

      up.addActionListener(e -> movePlayer("z"));
      down.addActionListener(e -> movePlayer("s"));
      left.addActionListener(e -> movePlayer("q"));
      right.addActionListener(e -> movePlayer("d"));

      controlsPanel.add(left);
      controlsPanel.add(up);
      controlsPanel.add(down);
      controlsPanel.add(right);

      if (isPlayerOnStore()) {
        JButton shop = new JButton("Boutique (B)");
        shop.addActionListener(e -> openShop());
        controlsPanel.add(shop);
      }
    }
    JButton inv = new JButton("Inventaire (I)");
    inv.addActionListener(e -> showInventory());
    controlsPanel.add(inv);

    controlsPanel.revalidate();
    controlsPanel.repaint();
    // Ensure focus remains on the window for key bindings
    gridPanel.requestFocusInWindow();
  }

  private boolean isPlayerOnStore() {
    return player.getPosition().equals(weaponStore.getPosition());
  }

  private void openShop() {
    String shopInfo = weaponStore.showStoreInfo();
    String choice = JOptionPane.showInputDialog(this,
        shopInfo + "\nEntrez l'index de l'arme à acheter (-1 pour quitter):");
    if (choice != null) {
      try {
        int idx = Integer.parseInt(choice);
        if (idx != -1) {
          String result = weaponStore.buyWeapon(player, idx);
          updateView(result);
        }
      } catch (NumberFormatException ignored) {
      }
    }
  }

  private Player createPlayer() {
    Position playerPosition = new Position(0, 0);
    Player p = new Sorcier("Hero", playerPosition);
    // give starter weapon and some money
    Inventery inv = p.getInventery();
    inv.addWeapon(new Sword("Epée courte", 15, 10));
    inv.addWeapon(new Axe("Hache", 25, 15));
    inv.creditMoney(100);
    return p;
  }

  private void createMonsters() {
    for (int i = 0; i < 5; i++) {
      Position monsterPosition;
      do {
        monsterPosition = new Position((int) (Math.random() * map.getWidth()),
            (int) (Math.random() * map.getHeight()));
      } while (!map.isPositionFree(monsterPosition) || monsterPosition.equals(player.getPosition()));
      var monster = new Monster(monsterPosition);
      map.addEntity(monster);
    }
  }

  private void createStores() {
    ArrayList<Weapon> storeWeapons = new ArrayList<>();
    Position storePosition = new Position(5, 5);
    storeWeapons.add(new Sword("Épée courte", 15, 10));
    storeWeapons.add(new Axe("Hache de guerre", 25, 15));
    storeWeapons.add(new Sword("Excalabur", 50, 50));
    var ws = new WeaponStore(storeWeapons, storePosition);
    map.addEntity(ws);
    this.weaponStore = ws;
  }

  private void movePlayer(String direction) {
    if (isFightMode) {
      updateView("Vous êtes en mode combat, vous ne pouvez pas vous déplacer.");
      return;
    }
    Position newPlayerPosition = player.getPosition().copy();
    switch (direction) {
      case "z":
        newPlayerPosition.setY(newPlayerPosition.getY() - 1);
        break;
      case "s":
        newPlayerPosition.setY(newPlayerPosition.getY() + 1);
        break;
      case "q":
        newPlayerPosition.setX(newPlayerPosition.getX() - 1);
        break;
      case "d":
        newPlayerPosition.setX(newPlayerPosition.getX() + 1);
        break;
      default:
        break;
    }
    if (newPlayerPosition.getX() < 0 || newPlayerPosition.getY() < 0)
      return;
    if (newPlayerPosition.getX() >= map.getWidth() || newPlayerPosition.getY() >= map.getHeight())
      return;
    player.setPosition(newPlayerPosition);
    postPlayerAction();
    updateControls();
  }

  private void postPlayerAction() {
    fightingMonster = getMonsterAtPlayerPosition();
    if (!isFightMode && fightingMonster != null) {
      isFightMode = true;
      updateView("Un monstre vous attaque !");
      return;
    }
    if (player.isDead()) {
      updateView("Vous êtes mort. Game Over.");
      disableControls();
      return;
    }
    updateView("");
  }

  private Monster getMonsterAtPlayerPosition() {
    var entities = map.getAllEntitiesAt(player.getPosition());
    for (var entity : entities) {
      if (entity instanceof Monster)
        return (Monster) entity;
    }
    return null;
  }

  private void attackMonster() {
    if (!isFightMode || fightingMonster == null) {
      updateView("Aucun monstre à attaquer ici.");
      return;
    }
    var weapon = player.getInventery().getSelectedWeapon();
    if (weapon == null) {
      updateView("Vous n'avez pas d'arme équipée.");
      return;
    }
    weapon.attaque(fightingMonster);
    String msg = "Vous avez attaqué " + fightingMonster.getName() + " avec " + weapon.getName() + " pour "
        + weapon.getDamage() + " dégâts.";
    if (fightingMonster.isDead()) {
      map.removeEntity(fightingMonster);
      fightingMonster = null;
      isFightMode = false;
      player.addXp(10);
      player.getInventery().creditMoney(10);
      msg += " Monstre vaincu ! Vous gagnez 10 XP et 10€.";
    } else {
      double monsterDamage = 10;
      player.hit(monsterDamage);
      msg += " Le monstre contre-attaque et vous inflige " + monsterDamage + " dégâts.";
      if (player.isDead()) {
        msg += " Vous êtes mort.";
      }
    }
    updateView(msg);
    updateControls();
  }

  private void showInventory() {
    Inventery inv = player.getInventery();
    StringBuilder sb = new StringBuilder();
    sb.append("Inventaire:\n");
    sb.append("Argent: ").append(inv.getMoney()).append("\n");
    sb.append("Armes:\n");
    var weapons = inv.getWeapons();
    for (int i = 0; i < weapons.size(); i++) {
      var w = weapons.get(i);
      sb.append(i).append(": ").append(w.getName()).append(" dmg:").append(w.getDamage());
      if (w == inv.getSelectedWeapon())
        sb.append(" (équipée)");
      sb.append("\n");
    }
    String choice = JOptionPane.showInputDialog(this, sb.toString() + "\nIndex arme à équiper (annuler pour quitter):");
    if (choice != null) {
      try {
        int idx = Integer.parseInt(choice);
        if (idx >= 0 && idx < weapons.size()) {
          inv.setSelectedWeapon(weapons.get(idx));
          updateView("Arme équipée: " + weapons.get(idx).getName());
        }
      } catch (NumberFormatException ignored) {
      }
    }
  }

  private void disableControls() {
    // simple approach: disable all buttons
    for (Component c : getContentPane().getComponents()) {
      c.setEnabled(false);
    }
  }

  private void updateView(String message) {
    gridPanel.repaint();
    StringBuilder info = new StringBuilder();
    info.append("=== JOUEUR ===\n");
    info.append(player.getInfo());
    info.append("XP: ").append(player.getXp()).append("\n");
    info.append("Argent: ").append(player.getInventery().getMoney()).append("£\n");
    Weapon w = player.getInventery().getSelectedWeapon();
    info.append("Arme: ").append(w != null ? w.getName() : "Aucune").append("\n");

    if (fightingMonster != null) {
      info.append("\n=== COMBAT ===\n");
      info.append(fightingMonster.getInfo()).append("\n");
    }
    info.append("--------------------\n");
    infoArea.setText(info.toString());
    messageLabel.setText(message);
  }

  public static void launch() {
    SwingUtilities.invokeLater(() -> {
      GameGUI g = new GameGUI();
      g.setVisible(true);
    });
  }

  private class GridPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      int w = getWidth();
      int h = getHeight();
      int rows = GameConfig.CARTE_HEIGHT;
      int cols = GameConfig.CARTE_WIDTH;
      int cellWidth = w / cols;
      int cellHeight = h / rows;

      for (int y = 0; y < rows; y++) {
        for (int x = 0; x < cols; x++) {
          int px = x * cellWidth;
          int py = y * cellHeight;

          // Draw background
          g.setColor(Color.WHITE);
          g.fillRect(px, py, cellWidth, cellHeight);
          g.setColor(Color.LIGHT_GRAY);
          g.drawRect(px, py, cellWidth, cellHeight);

          // Draw entity
          MapElement entity = map.getEntityAt(x, y);
          if (entity != null) {
            g.setColor(entity.getColor());
            g.fillRect(px + 5, py + 5, cellWidth - 10, cellHeight - 10);

            // Draw name or symbol
            g.setColor(Color.WHITE);
            String symbol = entity.draw(); // Assuming draw() returns a short string/char
            if (symbol.length() > 1)
              symbol = symbol.substring(0, 1);
            g.drawString(symbol, px + cellWidth / 2 - 3, py + cellHeight / 2 + 3);
          }
        }
      }
    }
  }
}
