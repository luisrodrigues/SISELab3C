import java.util.ArrayList;
//producer consumer test
public class ExerciseC {
    private final static int MaxTasks = 100;
    private final static int Limit = 5;

    static class TaskList {
        private ArrayList<Integer> contents = new ArrayList<Integer>();
        private int pos = -1;

        public synchronized int get() throws InterruptedException {
            while (contents.isEmpty()) {
                wait();
            }
            int value = contents.remove(pos);
            pos--;
            notifyAll();
            return value;
        }

        public synchronized void put(int value) throws InterruptedException {
            while (contents.size() >= Limit) {
                wait();
            }
            contents.add(value);
            pos++;
            notifyAll();
        }
    }
    
    static class Consumer extends Thread {
        private TaskList tasks;
        private int number;

        public Consumer(TaskList c, int number) {
            tasks = c;
            this.number = number;
        }
        public void run() {
            int value = 0;
            for (int i = 0; i < MaxTasks; i++) {
                try {
                    value = tasks.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Consumer #" + this.number + " got: " + value);
            }
        }
    }

    static class Producer extends Thread {
        private TaskList tasknumber;
        private int number;

        public Producer(TaskList c, int number) {
            tasknumber = c;
            this.number = number;
        }
        public void run() {
            for (int i = 0; i < MaxTasks; i++) {
                try {
                    tasknumber.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Producer #" + this.number + " put: " + i);
                try {
                    sleep((int)(Math.random() * 100));
                } catch (InterruptedException e) {

                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TaskList c = new TaskList();
        Producer p1 = new Producer(c, 1);
        Producer p2 = new Producer(c, 2);
        Consumer c1 = new Consumer(c, 1);
        Consumer c2 = new Consumer(c, 2);

        p1.start();
        p2.start();
        c1.start();
        c2.start();
        p1.join();
        c1.join();
        p2.join();
        c2.join();
    }
}
