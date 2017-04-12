package units.balls;

//todo:IMPROVE ABSTRACTION
import game.Game;
import units.GameUnit;

public interface Ball extends GameUnit {

    int getRadius();

    void move(Game game);

    void pressSpace(boolean command);

    void sizeUp();

    void speedUp();

    int getSpeedX();

    int getSpeedY();
}
