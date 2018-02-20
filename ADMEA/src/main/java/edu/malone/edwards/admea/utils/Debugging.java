/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.utils;

import edu.malone.edwards.admea.ehache.ASystem;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.openide.util.Exceptions;

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
    
    public void addTiming(double time, boolean newLine)
    {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter("R:\\GameofLife\\timing results.csv", true)))
        {
            bw.write(Double.toString(time));
            if(newLine)
                bw.newLine();
            else
                bw.write(",");

        } 
        catch (IOException ex) 
        {
            Exceptions.printStackTrace(ex);
        }
    }
}
