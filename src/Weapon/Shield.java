package Weapon;

public class Shield extends Weapon {
    public Shield() {}

    public Shield(String name, int power, float price) {
        super(name, power, price);
    }

    @Override
    public String draw() {
        return "    _..._\n" +
                ".-'_.---._'-.\n" +
                "||####|(__)||\n" +
                "((####|(**)))\n" +
                " '\\###|_''/'\n" +
                "  \\\\()|##//\n" +
                "   \\\\ |#//\n" +
                "    .\\_/.\n" +
                "     L.J\n" +
                "      \"\n";
    }
}
