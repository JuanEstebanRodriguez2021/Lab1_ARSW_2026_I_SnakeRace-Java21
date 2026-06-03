package firstpart;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread {

    private int a,b;
    private List<Integer> primes;

    public PrimeFinderThread(int a, int b) {
        this.a = a;
        this.b = b;
        primes = new LinkedList<>();
    }

    @Override
    public void run() {
        Control control = Control.newControl();
        for (int i = a; i < b; i++) {
            control.checkPause();
            if (isPrime(i)) {
                primes.add(i);
            }
        }
    }

    boolean isPrime(int n) {
        boolean ans;
        if (n > 2) {
            ans = n % 2 != 0;
            for (int i = 3; ans && i * i <= n; i += 2) {
                ans = n % i != 0;
            }
        } else {
            ans = n == 2;
        }
        return ans;
    }

    public int getAmountOfPrimes() {
        return primes.size();
    }

    public List<Integer> getPrimes() {
        return primes;
    }
}