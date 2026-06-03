package firstpart;

import java.util.Scanner;

public class Control extends Thread {
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;
    private static Control control;
    private final Scanner scanner;
    private volatile boolean pause = false;
    private final int NDATA = MAXVALUE / NTHREADS;
    private PrimeFinderThread pft[];
    private Control() {
        scanner = new Scanner(System.in);
        pft = new PrimeFinderThread[NTHREADS];
        int i;
        for (i = 0; i < NTHREADS - 1; i++) {
            pft[i] = new PrimeFinderThread(i * NDATA, (i + 1) * NDATA);
        }
        pft[i] = new PrimeFinderThread(i * NDATA, MAXVALUE + 1);
    }
    public static Control newControl() {
        if (control == null) {
            control = new Control();
        }
        return control;
    }
    @Override
    public void run() {
        for (PrimeFinderThread thread : pft) {
            thread.start();
        }
        try {
            while (!allThreadsFinished()) {
                Thread.sleep(TMILISECONDS);
                pauseThreads();
                Thread.sleep(100);
                System.out.println("\nPrimos encontrados: " + amountOfPrimesFound());
                System.out.println("Presione ENTER para continuar...");
                scanner.nextLine();
                resumeThreads();
            }
            System.out.println("Busqueda Terminada");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int amountOfPrimesFound() {
        int amount = 0;
        for (PrimeFinderThread t : pft) {
            amount += t.getAmountOfPrimes();
        }
        return amount;
    }
    private boolean allThreadsFinished(){
        for  (PrimeFinderThread thread : pft) {
            if (thread.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public synchronized void pauseThreads() {
        pause = true;
    }
    public synchronized void resumeThreads() {
        pause = false;
        notifyAll();
    }
    public synchronized void checkPause() {

        while (pause) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}