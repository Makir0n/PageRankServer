/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author makir0n
 */
public class SortPeople {

    ArrayList<People> sortPeople;

    public SortPeople(ArrayList<People> list) {

        sortPeople = list;

        //page_id??
        //for (int i = 0; i < sortPeople.size(); i++) {
            //compareがintじゃないとダメっぽいので1000かけて大きく
            //People(int i, String n ,double score)
            //sortPeople.add(new People(i, list.get(i).getName(),list.get(i).getScore() * 1000));
        //}
        //        Collections.sort(lst, new PageComparator());
        Collections.sort(list, new PeopleComparator());

        //System.out.println(sortPeople);
    }

    ArrayList<People> getSortedPeople() {
        return sortPeople;
    }
}

class PeopleComparator implements Comparator {

    @Override
    public int compare(Object s, Object t) {
        return (int) (((People) t).getScore()- ((People) s).getScore());
    }
}
