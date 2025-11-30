package Weapon;

public class Bow  extends Weapon {
    public Bow() {}

    public Bow(String name, int power, float price) {
        super(name, power, price);
    }

    @Override
    public String draw() {
        return "   (\n" +
                "    \\\n" +
                "     )\n" +
                "##-------->        b'ger\n" +
                "     )\n" +
                "    /\n" +
                "   (\n";
    }
}
