package scheduling;

public class Practice extends Activity {
    private int number;

    public Practice(String identifier) {
        super(identifier);
        parse(identifier);
    }

    private void parse(String identifier) {
        String[] split = identifier.split(" ");
        number = Integer.parseInt(split[split.length - 1].trim());
    }
}
