import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        int len = args.length;
        switch (len) {
            case 1 -> {
                if (args[0].equals("-h") || args[0].equals("--help")) {
                    // TODO: a help page with detailed instructions
                    System.out.println("bla bla more details bla bla");
                }
            }
            case 5 -> {
                List<Integer> arguments = Arrays.stream(args)
                        .map(Integer::parseInt)
                        .toList();

                System.out.println(String.join(" ", args));
            }
            default -> {
                System.err.println("Usage: concarrot <# of rabbits> <# of boxes> <carrot generation> <carrot presence> <jump delay>");
                System.exit(64);
            }
        }
    }
    private static void start(List<Integer> args) {

    }

    public class Rabbit implements Runnable {
        String name;
        int index = 0;
        @Override
        public void run() {

        }
    }

    public class Box {
        List<Rabbit> rabbits = null;
        boolean carrotYes = false;
    }
}
