package assignments;

import scheduling.Game;

public abstract class GameConstraint {
    private Game game1;
    private Game game2;

    public GameConstraint(Game game1, Game game2) {
        this.game1 = game1;
        this.game2 = game2;
    }

    public Game getGame1() {
        return game1;
    }

    public Game getGame2() {
        return game2;
    }
}
