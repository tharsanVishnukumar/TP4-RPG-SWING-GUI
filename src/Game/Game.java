package Game;

import Carte.Carte;
import Entity.GameEntity;
import Entity.MapElement;
import Entity.Monster;
import Player.Elfe;
import Player.Player;
import Player.Sorcier;
import Utils.GameConfig;
import Utils.Position;
import Weapon.Weapon;
import Weapon.WeaponStore;
import Weapon.Axe;
import Weapon.Sword;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
  private boolean isGameOver = false;
  private Carte<GameEntity> carte;
  private Player player;
  private WeaponStore weaponStore;
  private Monster fightingMonster;
  private boolean isFightMode = false;
  private boolean isInStoreMode = false;
  private Scanner sn;
  private String message = "";

  public void run() {
    this.carte = new Carte<>(GameConfig.CARTE_WIDTH, GameConfig.CARTE_HEIGHT);
    this.sn = new Scanner(System.in);
    this.player = createPlayer();
    carte.addEntity(player);

    createMonsters();
    createStores();


    while (!isGameOver) {
      clearConsole();
      System.out.println(carte.draw());
      showPlayerInfo();
      System.out.println(GameConfig.ANSI_RED + message + GameConfig.ANSI_RESET);
      playerNextAction();

      System.out.println("Game Over!");
    }
  }

  // do move player on all direction
  private void movePlayer(String direction) {
    Position newPlayerPosition = player.getPosition().copy();
    if (isFightMode) {
      message = "Vous êtes en mode combat, vous ne pouvez pas vous déplacer.";
      return;
    }

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
    }

    if (newPlayerPosition.getX() < 0 || newPlayerPosition.getX() < 0) {
      return;
    }
    if (newPlayerPosition.getX() >= GameConfig.CARTE_WIDTH || newPlayerPosition.getY() >= GameConfig.CARTE_HEIGHT) {
      return;
    }
    player.setPosition(newPlayerPosition);
  }

  public boolean isPlayerOnAStore() {
    var entities = carte.getAllEntitiesAt(player.getPosition());
    for (var entity : entities) {
      if (entity instanceof WeaponStore) {
        return true;
      }
    }
    return false;
  }

  public Monster getMonsterAtPlayerPosition() {
    var entities = carte.getAllEntitiesAt(player.getPosition());
    for (var entity : entities) {
      if (entity instanceof Monster) {
        return (Monster) entity;
      }
    }
    return null;
  }

  private void showUserCommandes() {
    StringBuilder playerCommandes = new StringBuilder("Quel Action voulez vous faire ?\n");
    playerCommandes.append("z: Haut\n")
        .append("s: Bas\n")
        .append("q: Gauche\n")
        .append("d: Droite\n")
        .append("i: Inventaire\n")
        .append("c: Changer d'arme\n");
    if (isPlayerOnAStore()) {
      playerCommandes.append("o: Ouvrir le magasin d'armes (ou b)\n");
    }
    System.out.println(playerCommandes);
  }

  private void askPlayerAction() {
    showUserCommandes();
    String inputAction = sn.nextLine();
    message = "";
    switch (inputAction) {
      case "z":
      case "s":
      case "q":
      case "d":
        movePlayer(inputAction);
        break;
      case "i":
        message = player.getInventery().draw();
        break;
      case "c":
        askChangeWeapon();
        break;
      case "o":
      case "b":
        if (isPlayerOnAStore()) {
          isInStoreMode = true;
        }
        break;
      default:
        break;
    }
  }


  private void askPlayeFightAction() {
    showPlayerFightActionCommands();
    String inputAction = sn.nextLine();
    //String inputAction = sn.next

    message += "\nVous êtes en mode combat contre " + fightingMonster.getName() +
        "!";
    switch (inputAction) {
      case "a":
        attaqueMonster();
        break;
      case "c":
        askChangeWeapon();
        break;
      default:
        message = "Action inconnue en mode combat";
        break;
    }
  }

  private void askChangeWeapon() {
    var inventery = player.getInventery();
    System.out.println(inventery.draw());
    System.out.println("Quel arme voulez vous équiper ? (index)");
    int inventeryWeaponIndex = sn.nextInt();
    Weapon weapon = inventery.getWeapon(inventeryWeaponIndex);
    if (weapon == null) {
    }
    inventery.setSelectedWeapon(weapon);
  }

  private void monsterDie() {
    if (fightingMonster != null) {
      carte.removeEntity(fightingMonster);
      fightingMonster = null;
    }
    isFightMode = false;
  }

  private void attaqueMonster() {
    Weapon weapon = player.getInventery().getSelectedWeapon();
    if (weapon == null) {
      message = "Vous n'avez pas d'arme équipée!";
      return;
    }
    if (fightingMonster == null) {
      isFightMode = false;
    }

    weapon.attaque(fightingMonster);
    message = "Vous avez attaqué " + fightingMonster.getName() + " avec " + weapon.getName() + " pour "
        + weapon.getDamage()
        + " dégâts.\n";
    if (fightingMonster.isDead()) {
      message = "Vous avez vaincu " + fightingMonster.getName() + "!\n";
      var xpGained = getRandomXpAmount();
      var moneyGained = getRandomMoneyAmount();
      player.addXp(xpGained);
      player.getInventery().creditMoney(moneyGained);
      message += "Vous avez gagné " + xpGained + " XP et " + moneyGained + "£.\n";
      monsterDie();
    } else {
      double monsterDamage = 10; // Example fixed damage
      player.hit(monsterDamage);
      message += fightingMonster.getName() + " vous a contre-attaqué pour " +
          monsterDamage + " dégâts.\n";
    }
  }

  public void askPlayerShopAction() {
    var inventery = player.getInventery();
    System.out.println(weaponStore.showStoreInfo());
    System.out.println("Vous avez " + inventery.getMoney() + "£.");
    System.out.println("Quel arme voulez vous acheter ? (index) ou -1 pour quitter");
    int weaponIndex = sn.nextInt();
    if (weaponIndex == -1) {
      isInStoreMode = false;
      return;
    }
    String purchaseResult = weaponStore.buyWeapon(player, weaponIndex);
    message = purchaseResult;
  }

  public void playerNextAction() {
    if (isFightMode) {
      askPlayeFightAction();
    } else if (isInStoreMode) {
      askPlayerShopAction();
    } else {
      askPlayerAction();
    }

    fightingMonster = getMonsterAtPlayerPosition();
    if (!isFightMode && fightingMonster != null) {
      isFightMode = true;
    }

    if (player.isDead()) {
      isGameOver = true;
    }
  }

  public void showPlayerInfo() {
    System.out.println("Joueur:");
    System.out.println("--------------------");
    System.out.println(player.getInfo());
    if (fightingMonster != null) {
      System.out.println("\nMonstre en combat:");
      System.out.println("--------------------");
      System.out.println(fightingMonster.getInfo());
    }

    System.out.println("--------------------");
  }

  private void createMonsters() {
    for (int i = 0; i < 5; i++) {
      Position monsterPosition;
      do {
        monsterPosition = new Position((int) (Math.random() * carte.getWidth()),
            (int) (Math.random() * carte.getHeight()));
      } while (!carte.isPositionFree(monsterPosition));
      var monster = new Monster(monsterPosition);
      carte.addEntity(monster);
    }

  }

  private void createStores() {
    ArrayList<Weapon> storeWeapons = new ArrayList<>();
    Position storePosition = new Position(5, 5);
    storeWeapons.add(new Sword("Épée courte", 15, 10));
    storeWeapons.add(new Axe("Hache de guerre", 25, 15));
    storeWeapons.add(new Sword("Excalabur", 50, 50));
    var weaponStore = new WeaponStore(storeWeapons, storePosition);
    carte.addEntity(weaponStore);
    this.weaponStore = weaponStore;
  }


  private Player createPlayer() {
    Position playerPosition = new Position(0, 0);
    String playName = askPlayerName();

    if (playName.isEmpty()) {
      playName = "Hero";
    }
    Player player;// = new Player(playName, 100, playerPosition);
    showPlayerClassOptions();
    int classChoice = sn.nextInt();
    String className = "";

    switch (classChoice) {
      case 2:
        player = new Elfe(playName, playerPosition);
        className = "Elfe";
        break;
      case 1:// si le choix est 1 ou autre
      default:
        player = new Sorcier(playName, playerPosition);
        className = "Sorcier";
        break;
    }
    message = "Vous avez choisi la classe " + className + ".\n";
    player = new Sorcier(playName, playerPosition);
    return player;
  }
  public String askPlayerName() {
    System.out.println("Quel est votre nom ?");
    var name = sn.nextLine();
    clearConsole();
    return name;
  }

  private void showPlayerClassOptions() {
    String classOptions = "Choisissez votre classe:\n" +
        "1: Sorcier\n" +
        "2: Elfe\n";
    System.out.println(classOptions);
  }

  // random and utils

  private int randomInt(int min, int max) {
    return (int) (Math.random() * (max - min)) + min;
  }

  private int getRandomXpAmount() {
    return randomInt(5, 20);
  }

  private int getRandomMoneyAmount() {
    return randomInt(10, 50);
  }

  public void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  private void showPlayerFightActionCommands() {
    String fightCommands = "Quel Action voulez vous faire ?\n" +
        "a: Attaquer\n" +
        "c: Changer d'arme\n";
    System.out.println(fightCommands);
  }

}
