/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

import java.rmi.server.ObjID;
import java.util.Arrays;
import java.util.LinkedList;

/**
 *
 * @author hcadavid
 */
public class Main {

    public static void main(String a[]) {
        PiDigits pd =  new PiDigits();
        System.out.println(bytesToHex(pd.getDigits(0, 20000, 11)));

        /* COMPARACION SIN TIEMPO DE ESPERA (5 segundos luego presionar enter)
        //Con threads
        PiDigits pd =  new PiDigits();
        long inicioThreads = System.currentTimeMillis();
        System.out.println(bytesToHex(pd.getDigitsNoWaiting(0, 20000, 11)));
        long finalThreads = System.currentTimeMillis();

        System.out.println("tiempo threads: " + (finalThreads - inicioThreads));
        
        //Sin threads
        System.out.println("\n");
        long inicioNoThreads = System.currentTimeMillis();
        System.out.println(bytesToHex(PiDigits.getDigits(0, 20000)));
        long finalNoThreads = System.currentTimeMillis();
        System.out.println("tiempo no treads: " + (finalNoThreads - inicioNoThreads));*/
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(LinkedList<Byte> bytes) {
        char[] hexChars = new char[bytes.size() * 2];
        for (int j = 0; j < bytes.size(); j++) {
            int v = bytes.get(j) & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }


    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }
}
