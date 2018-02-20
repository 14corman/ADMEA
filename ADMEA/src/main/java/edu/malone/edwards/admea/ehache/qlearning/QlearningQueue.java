/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.malone.edwards.admea.ehache.qlearning;

import java.util.LinkedList;

/**
 *
 * @author Cory Edwards
 */
public class QlearningQueue<E> extends LinkedList<E> {
    private final int limit;

    public QlearningQueue(int limit) {
        super();
        this.limit = limit;
    }

    @Override
    public boolean add(E object) {
        boolean add = super.add(object);
        while (size() > limit)
            poll();
        return add;
    }
}
