package org.example;

import java.util.Scanner;

import static org.example.DocumentUtils.OUTPUT_FILE;
import static org.example.DocumentUtils.SOURCE_FILE;

public class Main
{
    private static Scanner in = new Scanner(System.in);
    private static SteganoEncoder encoder = new SteganoEncoder();

    public static void main(String[] args)
    {
        System.out.println("Введите слово, которое ходите зашифровать:");
        String toEncode = in.nextLine();

        encoder.encode(toEncode, SOURCE_FILE, OUTPUT_FILE);
    }

}