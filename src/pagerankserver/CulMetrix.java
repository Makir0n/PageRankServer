/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagerankserver;

import java.util.ArrayList;
import org.ujmp.core.matrix.SparseMatrix;

/**
 *
 * @author makir0n
 */
public class CulMetrix {

    int row, column;

    // 疎行列はこの全部ゼロな行列でしか初期化できない模様（使い方からして当然だけど）
    SparseMatrix sm;
    SparseMatrix st;

    //double[] pageValue = {1, 1, 1, 1, 1};
    ArrayList<Double> pageValue = new ArrayList<>();

    CulMetrix(ArrayList<Integer> tos, ArrayList<Integer> froms, int size) {
        row = column = size;
        sm = SparseMatrix.factory.zeros(row, column);
        st = SparseMatrix.factory.zeros(row, column);
        for (int i = 0; i < tos.size(); i++) {
            //System.out.print(tos.get(i));
            //System.out.println(froms.get(i));
            sm.setAsInt(1, froms.get(i), tos.get(i));
        }
        //sm.showGUI();
        // 転置後の行列
        // 非ゼロ要素のみ座標をイテレータで転置位置にセット
        for (long[] cd : sm.availableCoordinates()) {
            st.setAsDouble(sm.getAsInt(cd[0], cd[1]), cd[1], cd[0]);
        }
        for (int i = 0; i < size; i++) {
            pageValue.add(1.0);
        }
        //st.showGUI();
        for (int y = 0; y < pageValue.size(); y++) {//列
            int[] arrayNum = new int[pageValue.size()];//各列の要素数
            //ある列の0でない要素の数を数える
            int num = 0;
            for (int x = 0; x < pageValue.size(); x++) {//行
                int element = st.getAsInt(x, y);
                if (element != 0) {
                    //ゼロじゃなかった要素の場所を記憶
                    arrayNum[num] = x;
                    num++;
                }
            }
            //複数リンクされていれば
            if (num > 1) {
                double a = 1 / (double) num;
                for (int i = 0; i < num; i++) {
                    st.setAsDouble(a, arrayNum[i], y);///列は固定で
                    //st.showGUI();
                    //System.out.println(st.getAsInt(3, 0));
                }
            }
        }
//st.showGUI();
        for (int i = 0; i < 1000; i++) {
            for (int x = 0; x < pageValue.size(); x++) {//列
                //ランダムサーファーモデルにのっとりGoogleの採用している0.85
                double d = 0.85;
                double sum = 0;
                for (int y = 0; y < pageValue.size(); y++) {//行
                    double element = st.getAsDouble(x, y);
                    sum += pageValue.get(y) * element;
                    //System.out.print(element);
                }
                pageValue.set(x,(1 - d) + d * sum);
                //System.out.println(x + "," + pageValue.get(x) + "/");
            }
            //System.out.print("\n");
        }

    }

    ArrayList<Double> getValues() {
        return pageValue;
    }

    SparseMatrix getMatrix() {
        return st;
    }
}
