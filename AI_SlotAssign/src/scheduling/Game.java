package scheduling;

public class Game extends Activity {
    private Practice associatedPractice;

    public Game(String identifier) {
        super(identifier);
    }

    public Practice getAssociatedPractice() {
        return associatedPractice;
    }

    public void setAssociatedPractice(Practice p) {
        this.associatedPractice = p;
    }

    @Override
    public String toString() {
        return super.toString() + " with associated practice: " + associatedPractice;
    }
}
