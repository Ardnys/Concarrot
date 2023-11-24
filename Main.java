import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;


public class Main {
    private static int numberOfRabbits;
    private static int numberOfBoxes;
    private static int carrotRate;
    private static int carrotTimeout;
    private static int sleepTime;
    private static boolean yes = true;
    private static List<Box> boxes;
    public static void main(String[] args) throws InterruptedException {
        int len = args.length;
        numberOfBoxes = 10;
        sleepTime = 20;
        carrotRate = 20;
        carrotTimeout = 80;

        boxes = new ArrayList<>(numberOfBoxes);
        for (int i = 0; i < numberOfBoxes; i++) {
            boxes.add(new Box());
        }

        var rabbit1 = new Rabbit("Georg");
        var rabbit2 = new Rabbit("Tom");
        var person = new Person();

        person.start();

        long s = System.currentTimeMillis();
        rabbit2.start();
        rabbit1.start();

         rabbit1.join();
         rabbit2.join();

         yes = false;

        person.join();

         long interval = System.currentTimeMillis() - s;

        System.out.printf("%s: %d%n%s: %d%ntook %d ms %n", rabbit1.name, rabbit1.eatenCarrots,
                rabbit2.name, rabbit2.eatenCarrots, interval);



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

    public static class Person extends Thread {
        Queue<Integer> carrots = new ArrayDeque<>();
        Queue<Long> times = new ArrayDeque<>();
        Random random = new Random(System.currentTimeMillis());

        public void run() {
            while (yes) {
                try {
                    int carrotIdx = random.nextInt(numberOfBoxes);
                    System.out.printf("picked %d at %d%n", carrotIdx, System.currentTimeMillis());
                    carrots.add(carrotIdx);
                    times.add(System.currentTimeMillis());
                    if (!times.isEmpty()) {
                        long lastTime = times.peek();
                        System.out.printf("diff %d%n", System.currentTimeMillis() - lastTime );
                        if (System.currentTimeMillis() - lastTime > carrotTimeout) {
                            long bye = times.poll();
                            int idx = carrots.poll();
                            System.out.println("removed carrot at " + idx + " after " + bye + " ms timeout");
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
        String name;
        int index = 0;
        public int eatenCarrots = 0;

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
                            System.out.println(name + " ate "+ index + " nom!");
                        }
                    }
                    Thread.sleep(sleepTime);

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    System.err.println("rabbit interrupted");
                }
            }
        }
    }

    public static class Box {
//        List<Rabbit> rabbits = null;
        boolean carrotYes = false;


    }
}
