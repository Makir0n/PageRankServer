/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author makir0n
 */
public class CalPageRank {

    CalPageRank() {

        ArrayList<Integer> transIndexId = new ArrayList<Integer>();
        ArrayList<Integer> tosID = new ArrayList<Integer>();
        ArrayList<Integer> fromsID = new ArrayList<Integer>();

        String jdbc_url = "jdbc:mysql://localhost/LINEtest";
        String user = "root";
        String password = "@xes";
        ResultSet rsLink;

        int outNum = 10;

        try (Connection con = DriverManager.getConnection(jdbc_url, user, password);
                Statement stmt = con.createStatement()) {

            //id同士の関連
            rsLink = stmt.executeQuery("SELECT page_id, pl_from FROM page INNER JOIN pagelinks ON page.page_title = pagelinks.pl_title;");
            while (rsLink.next()) {
                tosID.add(rsLink.getInt("page_id"));
                fromsID.add(rsLink.getInt("pl_from"));
            }
            //indexとpage_idの対応付け
            rsLink = stmt.executeQuery("SELECT page_id FROM page");
            for (int i = 0; rsLink.next() == true; i++) {
                transIndexId.add(rsLink.getInt("page_id"));
            }
            //close the statement and connection
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //行列でに対応できるようにidをindex同士の関係にする
        ArrayList<Integer> tos = new ArrayList<Integer>();
        ArrayList<Integer> froms = new ArrayList<Integer>();
        //データベースidの型・・・？
        for (int i = 0; i < tosID.size(); i++) {
            //一致したindexを返す
            tos.add(transIndexId.indexOf(tosID.get(i)));
        }
        for (int i = 0; i < fromsID.size(); i++) {
            froms.add(transIndexId.indexOf(fromsID.get(i)));
        }

        CulMetrix cm = new CulMetrix(tos, froms, transIndexId.size());
        //pageScoreのindexがtransIndexのindexと一致する
        ArrayList<Page> page = new ArrayList<>();
        ArrayList<Double> scores = cm.getScore();

        //indexがiのところにidとscore入れるもうこれ直接ＤＢでよくね？
        
         for (int i = 0; i < transIndexId.size(); i++) {
         //page.add(transIndexId.get(i), scores.get(i));
         page.add(i, new Page(transIndexId.get(i), scores.get(i)));
         }
        //一致するidのところにscore入れてくんだけど
        //めっちゃデータベースにアクセスするじゃん．．．
        /*
         for (int i = 0; i < transIndexId.size(); i++) {
         //index番号をtotalのindexに指定すると中身のidが
         int transID = transIndexId.get(pageScore.get(i).getPageIndex());
         pageScoreID.get(i).setPageId(transID);
         }
         */
        //あとでこれをデータベースに
        /*
         for (int i = 0; i < page.size(); i++) {
         //for (int i = 0; i < pageScore.size(); i++) {
         //int index = pageScore.get(i).getIndex();
         //pageScoreID.add(transIndexId.indexOf(pageScore.get(i)));
         System.out.print(page.get(i).getId());
         System.out.println("score:" + page.get(i).getScore());
         }
         */
        try (Connection con = DriverManager.getConnection(jdbc_url, user, password);
                Statement stmt = con.createStatement()) {

            //stmt.executeUpdate("ALTER TABLE page ADD pagerank double;");
            for (int i = 0; i < page.size(); i++) {
                //System.out.println(page.get(i).getId());
                stmt.executeUpdate("UPDATE page SET score = " + page.get(i).getScore() + "WHERE page_id = " + page.get(i).getId() + ";");

            }

            
            rsLink = stmt.executeQuery("SELECT * FROM page LIMIT 10;");
            while (rsLink.next()) {
                System.out.println("     " + rsLink.getString(3));
            }
                    

            //close the statement and connection
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
