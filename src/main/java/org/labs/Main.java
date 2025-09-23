package org.labs;

import org.labs.dining.DiningProgrammersWorld;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try {
            new DiningProgrammersWorld().startDining(1_000, 3, 7);
        } catch (InterruptedException e) {
            System.out.println("Ooopps... " + Arrays.toString(e.getStackTrace()));
        }
    }
}