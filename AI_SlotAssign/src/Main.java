import utility.Data;
import utility.Setup;

public class Main {

    private static Data data;

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        data = Setup.setup(args);
        System.out.println(data);

        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;

        System.out.println("\n\nExecution time in milliseconds: " + duration);
    }

    private int eval() {
        return (eval_minfilled() * data.getW_minfilled())
                + (eval_pref() * data.getW_pref())
                + (eval_pair() * data.getW_pair())
                + (eval_secdiff() * data.getW_secdiff());
    }

    private int eval_minfilled() {
        return 0;
    }

    private int eval_pref() {
        return 0;
    }

    private int eval_pair() {
        return 0;
    }

    private int eval_secdiff() {
        return 0;
    }

}
