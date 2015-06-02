/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author makir0n
 */
public class PageRankServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        CalPageRank pr = new CalPageRank();

        try (ServerSocket server = new ServerSocket(8001);
                FileOutputStream fos = new FileOutputStream("server_recv.txt");
                FileInputStream fis = new FileInputStream("server_send.txt")) {
            System.out.println("クライアントからの接続を待ちます。");
            Socket socket = server.accept();
            System.out.println("クライアント接続。");

            int ch;
            // クライアントから受け取った内容をserver_recv.txtに出力
            InputStream input = socket.getInputStream();
            // クライアントは、終了のマークとして0を送付してくる
            while ((ch = input.read()) != 0) {
                fos.write(ch);
            }
            // server_send.txtの内容をクライアントに送付
            OutputStream output = socket.getOutputStream();
            while ((ch = fis.read()) != -1) {
                output.write(ch);
            }
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
