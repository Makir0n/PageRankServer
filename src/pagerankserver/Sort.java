/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.ujmp.core.matrix.SparseMatrix;

/**
 *
 * @author makir0n
 */
public class Sort {

    ArrayList<Page> lst;

    //idが数字でない可能性とか
    //Sort(int edge_num, ArrayList<Double> values) {
Sort(ArrayList<Double> values) {
        lst = new ArrayList<Page>();

        //page_id??
        
        for (int i = 0; i < values.size(); i++) {
            //System.out.println(values.get(i));
            //compareがintじゃないとダメっぽいので1000かけて大きく
            lst.add(new Page(i, values.get(i) * 1000));
        }
        /*
         lst.add(new Page("page1", 34));
         lst.add(new Page("page2", 13));
         lst.add(new Page("page3", 18));
         */
        //System.out.println(lst);

        Collections.sort(lst, new PageComparator());

        //System.out.println(lst);
    }

    ArrayList<Page> getBestID() {
        return lst;
    }

}

class PageComparator implements Comparator {

    @Override
    public int compare(Object s, Object t) {
        return (int) (((Page) t).getValue() - ((Page) s).getValue());
    }
}
