/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author makir0n ページランク算出して データベースに入れて クライアントからURLとんでくるの待つ
 * とんできたらHTMLからタイトルとリンクのタイトルとってくる タイトルから生えてるリンクをDBから探す 本文中にあるか照合する
 *
 *
 */
public class PageRankServer {

    public static void main(String[] args) {
        // TODO code application logic here

        //CalPageRank pr = new CalPageRank();
        try (ServerSocket server = new ServerSocket(8001);) {
            System.out.println("クライアントからの接続を待ち");
            Socket socket = server.accept();
            System.out.println("クライアント接続");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String url;
            if ((url = in.readLine()) != null) {
                //System.out.println("受信: " + uel);
            }
            
            //out.println(line);
            //System.out.println("送信: " + line);
            AnalyzeHTML analyzehtml = new AnalyzeHTML(url);
            // クライアントは、終了のマークとして0を送付してくる
            /*while ((ch = input.read()) != 0) {
             fos.write(ch);
             }*/
            /*
             OutputStream output = socket.getOutputStream();
             while ((ch = fis.read()) != -1) {
             output.write(ch);
             }
             */
            in.close();
            socket.close();
            server.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
