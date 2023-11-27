import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Main {
    private static final String[] nameRezervoir = {
            "Mio", "Yuuko", "Mai", "Hakase", "Nano", "Sakamoto", "Izumi", "Makato", "Takasaki", "Kana", "Sasahara", // ordinary life
            "Osaka", "Oliver", "Sophia", "Liam", "Emma", "Noah", "Anya", "Jackson", "Olivia", "Lucas", "Isabella"   // quintessential gpt names
    };
    private static int numberOfRabbits;
    private static int numberOfBoxes;
    private static int carrotRate;
    private static int carrotTimeout;
    private static int sleepTime;
    private static boolean yes = true;
    private final static List<Box> boxes = new ArrayList<>();


    public static void main(String[] args) {
        System.out.println("Welcome! For the best results, keep the rabbit sleep time and carrot placement times low.");
        getParameters();

        System.out.println("Game on!");

        List<String> n = new ArrayList<>(List.of(nameRezervoir));
        Collections.shuffle(n);
        Queue<String> names = new ArrayDeque<>(n);

        for (int i = 0; i < numberOfBoxes; i++) {
            boxes.add(new Box());
        }

        List<Rabbit> rabbits = new ArrayList<>(numberOfRabbits);
        for (int i = 0; i < numberOfRabbits; i++) {
            var rabbit = new Rabbit(names.poll());
            rabbit.start();
            rabbits.add(rabbit);
        }
        var person = new Person();

        person.start();

        long s = System.currentTimeMillis();

        try {
            for (var rabbit : rabbits ) {
                rabbit.join();
            }

            yes = false;
            person.join();

        } catch (InterruptedException e) {
            System.err.println("main thread interrupted. setting the flag..");
            Thread.currentThread().interrupt();
        }



        for (var rabbit : rabbits ) {
            System.out.printf("%s has eaten %d carrots. wow!%n", rabbit.name, rabbit.eatenCarrots);
        }

        long interval = System.currentTimeMillis() - s;
        System.out.println("Program finished in " + interval);

        /*
        ---------------------------------------------------------------------------------------
        |   I like using the program arguments instead of doing the question answer thing.
        |   Here's that approach that I started but abandoned to adhere to homework requirements
        |   Since I put some effort in it, I couldn't bring myself to delete it
        ---------------------------------------------------------------------------------------


        int len = args.length;

        switch (len) {
            case 1 -> {
                if (args[0].equals("-h") || args[0].equals("--help")) {
                    // TODO: a help page with detailed instructions
                    System.out.println("bla bla more details bla bla");
                }
            }
            case 5 -> {
                numberOfRabbits = Integer.parseInt(args[0]);
                numberOfBoxes = Integer.parseInt(args[1]);
                carrotRate = Integer.parseInt(args[2]);
                carrotTimeout = Integer.parseInt(args[3]);
                sleepTime = Integer.parseInt(args[4]);

                System.out.println(String.join(" ", args));

                var rabbit1 = new Rabbit("Georg");
                var rabbit2 = new Rabbit("Tom");

            }
            default -> {
                System.err.println("Usage: concarrot <# of rabbits> <# of boxes> <carrot generation> <carrot presence> <jump delay>");
                System.exit(64);
            }
        }
        --------------------------------------------------------------------------------------------

         */

    }

    public static void getParameters() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of rabbits: ");
        numberOfRabbits = Integer.parseInt(scanner.nextLine());
        if (numberOfRabbits > nameRezervoir.length) {
            numberOfRabbits = nameRezervoir.length;
            System.out.printf("%s %d %s%n%s %d%n", "oops! we only have", nameRezervoir.length,
                    "rabbits.", "rabbit number is reduced to", nameRezervoir.length);
            // this prevents a null name when they enter more rabbits than we have names for them
        }

        System.out.print("Enter number of boxes: ");
        numberOfBoxes = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter carrot production rate: ");
        carrotRate = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter carrot timeout: ");
        carrotTimeout = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter rabbit sleep time: ");
        sleepTime = Integer.parseInt(scanner.nextLine());

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
                            long bye = 0;
                            int idx = 0;

                            if (!times.isEmpty()) {
                                 bye = times.poll();
                            }
                            if (!carrots.isEmpty()) {
                                 idx = carrots.poll();
                            }
                            System.out.println("removed carrot at " + idx + " after " + (System.currentTimeMillis() - bye) + " ms timeout");
                            synchronized (boxes) {
                                boxes.get(idx).carrotYes = false;
                            }

                        }

                    }


                    synchronized (boxes) {
                        boxes.get(carrotIdx).carrotYes = true;
                    }

                    TimeUnit.MILLISECONDS.sleep(carrotRate);
                    // this suppresses the busy waiting warning in IntelliJ
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

                    System.out.printf("rabbit %s in box %d with state %s%n", name, index, currentThread().getState());
                    synchronized (boxes) {
                        if (index < numberOfBoxes && boxes.get(index).carrotYes) {
                            boxes.get(index).carrotYes = false;
                            eatenCarrots++;
                            System.out.println(name + " ate " + index + " nom!");
                        }
                    }
                    TimeUnit.MILLISECONDS.sleep(sleepTime);

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
