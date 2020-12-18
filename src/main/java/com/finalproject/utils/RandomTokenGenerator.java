/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.utils;

import java.util.Random;

/**
 *
 * @author deepak_lokwani
 */
public class RandomTokenGenerator {
    private static final String upper="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lower=upper.toLowerCase();
    private static final String numbers="1234567890";
    private static final String symbols="!@$*-_";

    public RandomTokenGenerator() {
    }
    public static String gernerate(){
        StringBuilder sb= new StringBuilder();
        String input=upper+lower+numbers+symbols;
        Random rand=new Random();
        for(int i=0;i<60;i++){
            sb.append(input.charAt(rand.nextInt(input.length())));
        }
        return sb.toString();
    }
}
