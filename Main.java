import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;


public class Main {
    private static final String[] nameRezervoir = {
            "Mio", "Yuuko", "Mai", "Hakase", "Nano", "Sakamoto", "Izumi", "Makato", "Takasaki", "Kana", "Sasahara", // ordinary life
            "Osaka", "Oliver", "Sophia", "Liam", "Emma", "Noah", "Ava", "Jackson", "Olivia", "Lucas", "Isabella"
    };
    private static int numberOfRabbits;
    private static int numberOfBoxes;
    private static int carrotRate;
    private static int carrotTimeout;
    private static int sleepTime;
    private static boolean yes = true;
    private static List<Box> boxes;
    private static Queue<String> names;

    public static void main(String[] args) throws InterruptedException {
        getParameters();


        List<String> n = new ArrayList<>(List.of(nameRezervoir));
        Collections.shuffle(n);
        names = new ArrayDeque<>(n);

        boxes = new ArrayList<>(numberOfBoxes);
        for (int i = 0; i < numberOfBoxes; i++) {
            boxes.add(new Box());
        }

        List<Rabbit> rabbits = new ArrayList<>(numberOfRabbits);
        for (int i = 0; i < numberOfRabbits; ++i) {
            var rabbit = new Rabbit(names.poll());
            rabbit.start();
            rabbits.add(rabbit);
        }
        var person = new Person();

        person.start();

        long s = System.currentTimeMillis();

        for (var rabbit : rabbits ) {
            rabbit.join();
        }

        yes = false;

        person.join();

        long interval = System.currentTimeMillis() - s;

        /*
        ---------------------------------------------------------------------------------------
        |   I like using the program arguments instead of doing the question answer thing.
        |   Here's that approach that I started but abandoned to adhere to homework requirements
        ---------------------------------------------------------------------------------------
         */

//        int len = args.length;
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
        /*
        --------------------------------------------------------------------------------------------
         */
    }

    public static void getParameters() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of rabbits: ");
        numberOfRabbits = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter number of boxes: ");
        numberOfBoxes = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter carrot production rate: ");
        carrotRate = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter carrot timeout: ");
        carrotTimeout = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter rabbit sleep time: ");
        sleepTime = Integer.parseInt(scanner.nextLine());
        System.out.println("Game on!");

    }

    public static class Person extends Thread {
        Queue<Integer> carrots = new ArrayDeque<>();
        Queue<Long> times = new ArrayDeque<>();
        Random random = new Random(System.currentTimeMillis());

        public void run() {
            while (yes) {
                try {
                    int carrotIdx = random.nextInt(numberOfBoxes);
                    System.out.printf("picked %d%n", carrotIdx);
                    carrots.add(carrotIdx);
                    times.add(System.currentTimeMillis());
                    if (!times.isEmpty()) {
                        long lastTime = times.peek();
//                        System.out.printf("diff %d%n", System.currentTimeMillis() - lastTime );
                        if (System.currentTimeMillis() - lastTime > carrotTimeout) {
                            long bye = times.poll();
                            int idx = carrots.poll();
                            System.out.println("removed carrot at " + idx + " after " + (System.currentTimeMillis() - bye) + " ms timeout");
                            synchronized (boxes) {
                                boxes.get(idx).carrotYes = false;
                            }

                        }

                    }


                    synchronized (boxes) {
                        boxes.get(carrotIdx).carrotYes = true;
                    }

                    Thread.sleep(carrotRate);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    public static class Rabbit extends Thread {
        public int eatenCarrots = 0;
        String name;
        int index = 0;

        public Rabbit(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            while (index < numberOfBoxes) {
                try {
                    ++index;

                    System.out.println("ribbit. " + name + " at " + index);
                    synchronized (boxes) {
                        if (index < numberOfBoxes && boxes.get(index).carrotYes) {
                            boxes.get(index).carrotYes = false;
                            eatenCarrots++;
                            System.out.println(name + " ate " + index + " nom!");
                        }
                    }
                    Thread.sleep(sleepTime);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.err.println("rabbit interrupted");
                }
            }
        }
    }

    public static class Box {
        boolean carrotYes = false;


    }
}
