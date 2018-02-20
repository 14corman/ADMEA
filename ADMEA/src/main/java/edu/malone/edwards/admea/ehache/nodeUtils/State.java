/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.ehache.nodeUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Cory Edwards
 * @param <K>
 */
public abstract class State<K> implements Serializable
{
    public K object;
    
    public State(K state)
    {
        object = state;
    }
    
    @Override
    public abstract boolean equals(Object obj);
    
    @Override
    public abstract String toString();

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.object);
        return hash;
    }
}
