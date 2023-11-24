import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    private static int numberOfRabbits;
    private static int numberOfBoxes;
    private static int carrotRate;
    private static int carrotTimeout;
    private static int sleepTime;
    private static List<Box> boxes;
    public static void main(String[] args) throws InterruptedException {
        int len = args.length;
        numberOfBoxes = 10;
        sleepTime = 20;
        boxes = new ArrayList<>(numberOfBoxes);
        for (int i = 0; i < numberOfBoxes; i++) {
            boxes.add(new Box(i % 2 == 0));
        }

        var rabbit1 = new Rabbit("Georg");
        var rabbit2 = new Rabbit("Tom");
        rabbit2.start();
        rabbit1.start();

         rabbit1.join();
         rabbit2.join();

        System.out.printf("%s: %d%n%s: %d", rabbit1.name, rabbit1.eatenCarrots, rabbit2.name, rabbit2.eatenCarrots);



//
//        switch (len) {
//            case 1 -> {
//                if (args[0].equals("-h") || args[0].equals("--help")) {
//                    // TODO: a help page with detailed instructions
//                    System.out.println("bla bla more details bla bla");
//                }
//            }
//            case 5 -> {
//                numberOfRabbits = Integer.parseInt(args[0]);
//                numberOfBoxes = Integer.parseInt(args[1]);
//                carrotRate = Integer.parseInt(args[2]);
//                carrotTimeout = Integer.parseInt(args[3]);
//                sleepTime = Integer.parseInt(args[4]);
//
//                System.out.println(String.join(" ", args));
//
//                var rabbit1 = new Rabbit("Georg");
//                var rabbit2 = new Rabbit("Tom");
//
//            }
//            default -> {
//                System.err.println("Usage: concarrot <# of rabbits> <# of boxes> <carrot generation> <carrot presence> <jump delay>");
//                System.exit(64);
//            }
//        }
    }
    private static void start(List<Integer> args) {

    }

    public static class Rabbit extends Thread {
        String name;
        int index = 0;
        public int eatenCarrots = 0;

        public Rabbit(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (index < numberOfBoxes) {
               // try {
                    ++index;

                    System.out.println("ribbit. " + name + " at " + index);
                    synchronized (boxes) {
                        if (index < numberOfBoxes && boxes.get(index).carrotYes) {
                            boxes.get(index).carrotYes = false;
                            eatenCarrots++;
                            System.out.println(name + " nom!");
                        }
                    }

               // }
               // catch (InterruptedException e) {
               //     e.printStackTrace();
               //     System.err.println("rabbit interrupted");
               // }
            }
        }
    }

    public static class Box {
//        List<Rabbit> rabbits = null;
        boolean carrotYes = false;

        public Box(boolean carrotYes) {
            this.carrotYes = carrotYes;
        }
    }
}
