package Entity;

public class Obstacle extends Attaquable {
    Obstacle() {
        super(400);
    }

    @Override
    public void hit(double damageCount) {
        super.hit(damageCount * 1.2);
    }
}
