package assignments;

import scheduling.Game;

public class NotCompatible extends GameConstraint {
    public NotCompatible(Game game1, Game game2) {
        super(game1, game2);
    }
}
