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

    static ArrayList<String> linkTitle = new ArrayList();
    static ArrayList<String> linkDB = new ArrayList();

    //static ArrayList<Integer> tosID = new ArrayList<Integer>();
    //static ArrayList<Integer> fromsID = new ArrayList<Integer>();
    int fromID;

    AnalyzeHTML(String url) {

        //System.out.println(url);
        String pageTitle = null;
        try {
            pageTitle = getTitle(url);
        } catch (Exception ex) {
            Logger.getLogger(AnalyzeHTML.class.getName()).log(Level.SEVERE, null, ex);
        }
        int index = pageTitle.indexOf(" - Wikipedia");
        pageTitle = new String(pageTitle.substring(0, index));
        //pageTitle = "存命人物";
        //System.out.println(pageTitle);

        String jdbc_url = "jdbc:mysql://localhost/LINEpage";
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
                //System.out.println(s.);
            }
            System.out.println("DB"+linkDB.size());
            System.out.println("Title"+linkTitle.size());
            //両方の配列にあったリンク先の名前の配列をつくる
            ArrayList<String> link = new ArrayList();
            int j,i;
            for (i = 0, j = 0; i < linkDB.size(); i++) {
                if (linkTitle.indexOf(linkDB.get(i)) != -1) {//linkDBとlinkTitle
                    link.add(linkDB.get(i));
                    j++;
                }
            }
            System.out.println(j);
            System.out.println("link"+link.size());
            System.out.println(link);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ArrayList<String> getLinkPageId() {
        return linkTitle;
    }

    public static String getTitle(String page_url) throws Exception {
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

        while (link_matcher.find()) {
            linkTitle.add(link_matcher.group(1).replaceAll("\\s", ""));
        }

        String pageTitle = null;
        if (title_matcher.find()) {
            pageTitle = title_matcher.group(1);
            //System.out.println(pageTitle);
        }
        return pageTitle;
    }
}
