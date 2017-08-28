/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.utils;

import edu.malone.edwards.admea.ASystem;

/**
 *  Will print out Debugging information if DEBUG is set to true in ASystem.
 * @author Cory Edwards
 */
public class Debugging {
    
    /**
     * Calls System.out.print()
     * @param string The String that will be printed.
     */
    public void print(String string)
    {
        if(ASystem.DEBUG)
            System.out.print(string);
    }
    
    /**
     * Calls System.out.println()
     * @param string The String that will be printed.
     */
    public void println(String string)
    {
        if(ASystem.DEBUG)
            System.out.println(string);
    }
}
