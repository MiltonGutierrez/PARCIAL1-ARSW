package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import org.omg.CORBA.DATA_CONVERSION;

///  <summary>
///  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
///  digits of pi.
///  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
///  *** Translated from C# code: https://github.com/mmoroney/DigitsOfPi ***
///  </summary>
public class PiDigits {

    
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    private ArrayList<PiDigitsThread> threads;
    public AtomicInteger digitsCounted;
    
    /**
     * 
     * @param count
     * @param N
     */
    public void divideRange(int start, int count, int N){
        int startRangeThread = start;
        int digitsPerThread  = count / N;
        int extraDigit = count % N;
        
        this.threads = new ArrayList<>();
        this.digitsCounted = new AtomicInteger(0);

        for(int i = 0; i < N; i++){
            if(extraDigit > i){
                digitsPerThread += 1;
            }
            threads.add(new PiDigitsThread(startRangeThread, digitsPerThread, digitsCounted, new Object()));

            threads.get(i).start();
            startRangeThread += digitsPerThread;
            digitsPerThread = count / N;
        }

    }

    public static byte[] getDigits(int start, int count) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        byte[] digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }

        return digits;

    }

    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public LinkedList<Byte> getDigits(int start, int count, int N) {

        boolean allAlive = true;
        divideRange(start ,count, N);
        while(allAlive){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(PiDigitsThread ptd: threads){
                ptd.stopThread();
            }

            System.out.println("Digitos calculados: " + digitsCounted.get());
            System.out.println("Presiona enter para continuar");
            Scanner sc = new Scanner(System.in);
            String input = sc.nextLine();

            allAlive = false;
            for(PiDigitsThread ptd: threads){
                if(ptd.isAlive()){
                    allAlive = true;
                    ptd.resumeTread();
                }
            }

        }

        LinkedList<Byte> digits = new LinkedList<>();
        for(PiDigitsThread pdt: threads){
            digits.addAll(pdt.digitsCalculated);
        }
        return digits;
    }

    /// <summary>
    /// Returns the sum of 16^(n - k)/(8 * k + m) from 0 to k.
    /// </summary>
    /// <param name="m"></param>
    /// <param name="n"></param>
    /// <returns></returns>
    private static double sum(int m, int n) {
        double sum = 0;
        int d = m;
        int power = n;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, d) / d;
            } else {
                term = Math.pow(16, power) / d;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            d += 8;
        }

        return sum;
    }

    /// <summary>
    /// Return 16^p mod m.
    /// </summary>
    /// <param name="p"></param>
    /// <param name="m"></param>
    /// <returns></returns>
    private static int hexExponentModulo(int p, int m) {
        int power = 1;
        while (power * 2 <= p) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (p >= power) {
                result *= 16;
                result %= m;
                p -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= m;
            }
        }

        return result;
    }

}
