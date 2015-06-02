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
class Page {

    private int index;
    private double value;
    private int id;

    Page(int n, double v) {
        index = n;
        value = v;
    }

    public double getValue() {
        return value;
    }

    public int getPageIndex() {
        return index;
    }
    
    public void setPageId(int id){
         this.id = id;
    }
    
    public int getId(){
        return id;
    }
    @Override
    public String toString() {
        return getPageIndex()+ ":" + getValue();
        //return String.valueOf(getId());
    }
}
