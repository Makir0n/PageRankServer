/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTextField;

/**
 *
 * @author makir0n
 */
public class AnalyzeHTML {

    static String pageTitle = null;
    static ArrayList<String> linkTitle = new ArrayList();
    static ArrayList<String> linkDB = new ArrayList();

    //static ArrayList<Integer> tosID = new ArrayList<Integer>();
    //static ArrayList<Integer> fromsID = new ArrayList<Integer>();
    int fromID;

    AnalyzeHTML(String url) {
        try {

            //getTitle("http://ja.wikipedia.org/wiki/%E5%9B%9E%E9%8D%8B%E8%82%89");
            //getTitle("http://imagingrium.com/test/%E5%AD%98%E5%91%BD%E4%BA%BA%E7%89%A9.html");
            getTitle(url);
        } catch (Exception ex) {
            Logger.getLogger(AnalyzeHTML.class.getName()).log(Level.SEVERE, null, ex);
        }

        int index = pageTitle.indexOf(" - Wikipedia");
        pageTitle = new String(pageTitle.substring(0, index));
        //pageTitle = "存命人物";
        //System.out.println(pageTitle);

        String jdbc_url = "jdbc:mysql://localhost/LINEtest";
        String user = "root";
        String password = "@xes";
        ResultSet rsLink;

        //ページタイトルからidとってきてfromとってきて
        try (Connection con = DriverManager.getConnection(jdbc_url, user, password);
                Statement stmt = con.createStatement()) {
            //今のページのid調べる
            //そのidがfromになってるページのtitleを取ってくる

            String s = "SELECT * FROM page WHERE page_title LIKE '" + pageTitle + "';";
            rsLink = stmt.executeQuery(s);
            while (rsLink.next()) {
                fromID = rsLink.getInt("page_id");
            }
            s = "SELECT * FROM page INNER JOIN pagelinks ON page.page_title = pagelinks.pl_title where pagelinks.pl_from = " + fromID + ";";
            rsLink = stmt.executeQuery(s);
            while (rsLink.next()) {
                s = new String(rsLink.getBytes("page_title"), "UTF-8");
                linkDB.add(s);
                //System.out.println(s);
            }
            //両方の配列にあったリンク先の名前の配列をつくる
            for (int i = 0, j = 0; i < linkDB.size(); i++) {
                if (linkDB.indexOf(linkTitle.get(i)) != -1) {
                    linkTitle.set(j, linkDB.get(i));
                    j++;
                }
            }
            //System.out.println(linkTitle);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //people型が便利？いやでも向こうでつくってるからidわかったら
    //違うはつくったのは存命人物か

    ArrayList<String> getLinkPageId() {
        return linkTitle;
    }

    public static void getTitle(String page_url) throws Exception {
        //アクセスしたいページpage_url
        URL url = new URL(page_url);
        URLConnection conn = url.openConnection();

        //文字コードを変換するための情報を取得
        //String charset = Arrays.asList(conn.getContentType().split(";")).get(1);
        //String encoding = Arrays.asList(charset.split("=")).get(1);
        String encoding = "UTF-8";

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
        StringBuffer response = new StringBuffer();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line + "\n");
        }
        in.close();

        Pattern title_pattern = Pattern.compile("<title>([^<]+)</title>", Pattern.CASE_INSENSITIVE);
        Pattern link_pattern = Pattern.compile("<a.*?href=\".*?\".*?>(.*?)</a>", Pattern.DOTALL);

        Matcher title_matcher = title_pattern.matcher(response.toString());
        Matcher link_matcher = link_pattern.matcher(response.toString());

        if (title_matcher.find()) {
            //System.out.println(title_matcher.group(1));
            pageTitle = title_matcher.group(1);
        }
        while (link_matcher.find()) {
            linkTitle.add(link_matcher.group(1).replaceAll("\\s", ""));
            //System.out.println(link_matcher.group(1).replaceAll("\\s", ""));
            /*
             人１
             人２
             人３
             人４
             */
        }
    }
}
