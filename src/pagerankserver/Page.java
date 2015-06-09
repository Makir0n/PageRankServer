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

    //private int index;
    private double score;
    private int id;

    
    Page(int i, double s) {
        id = i;
        score = s;
    }
    

    public double getScore() {
        return score;
    }

    /*
    public int getPageIndex() {
        return index;
    }
    */
    
    public int getId(){
        return id;
    }
        
    public void setPageId(int id){
         this.id = id;
    }
        
    public void setPageSocre(double score){
         this.score = score;
    }
    @Override
    public String toString() {
        //return getPageIndex()+ ":" + getScore();
        return String.valueOf(getId());
    }
}
