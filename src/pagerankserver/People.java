/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

/**
 *
 * @author makir0n
 */
public class People {

    private int id;
    private String name;
    private double score;

    People(int i, String n, double s) {
        id = i;
        name = n;
        score = s;
    }

    public double getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return getId() + ":" + getName() + ":" + getScore();
        //return getName();
    }

}
