package edu.eci.arsw.math;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class PiDigitsThread extends Thread {

    //Atributos del thread
    public int start;
    public int count;
    public AtomicInteger sharedDigitsCounted;
    private Object lock;
    public LinkedList<Byte> digitsCalculated;
    public boolean running = true;
    //Atributos base de PiDigits
    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    
    public PiDigitsThread(int start, int count, AtomicInteger digits, Object lock){
        this.start = start;
        this.count = count;
        this.sharedDigitsCounted = digits;
        this.lock = lock;
        this.digitsCalculated = new LinkedList<>();
    }


    @Override
    public void run(){
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        double sum = 0;
        int i = 0;
        while(i < count){
            if(running){
                if (i % DigitsPerSum == 0) {
                    sum = 4 * sum(1, start)
                            - 2 * sum(4, start)
                            - sum(5, start)
                            - sum(6, start);
    
                    start += DigitsPerSum;
                }
    
                sum = 16 * (sum - Math.floor(sum));
                digitsCalculated.add((byte) sum);
                sharedDigitsCounted.incrementAndGet();
                
                i++;
            }
            else{
                synchronized(lock){
                    try {
                        lock.wait();
                    } catch (Exception e) {
                    }
                    
                }
            }
            
        }
    }

    public void stopThread(){
        running = false;
    }

    public void resumeTread(){
        running = true;
        synchronized(lock){
            lock.notifyAll();
        }
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