package scheduling;

import java.util.Arrays;

public class Practice extends Activity {
    private int number;

    public Practice(String identifier) {
        super(identifier);
//        parse(identifier);
    }

    private void parse(String identifier) {
        String[] split = identifier.split(" ");
        System.out.println(Arrays.toString(split));
        number = Integer.parseInt(split[5].trim());
    }

//    @Override
//    public int compareTo(Activity other) {
//        // Call the superclass compareTo first
//        int result = super.compareTo(other);
//        if (result != 0) {
//            return result; // If leagues or age groups differ, return that result
//        }
//
//        // Cast to GameActivity to compare scores
//        if (other instanceof Practice) {
//            Practice otherPrac = (Practice) other;
//            return Integer.compare(this.number, otherPrac.number); // Compare by score
//        }
//
//        // If the other activity is not a GameActivity, you might decide how to handle this.
//        return 0; // Or throw an exception, or some default case
//    }
}
