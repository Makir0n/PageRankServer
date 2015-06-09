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

    SparseMatrix sm;
    SparseMatrix st;

    ArrayList<Double> pageScore = new ArrayList<>();

    CulMetrix(ArrayList<Integer> tos, ArrayList<Integer> froms, int size) {
        row = column = size;
        sm = SparseMatrix.factory.zeros(row, column);
        st = SparseMatrix.factory.zeros(row, column);
        //fromはtoにリンクしてる
        for (int i = 0; i < tos.size(); i++) {
            sm.setAsInt(1, froms.get(i), tos.get(i));
        }
        // 転置後の行列
        // 非ゼロ要素のみ座標をイテレータで転置位置にセット
        //toはfromからリンクされてる
        for (long[] cd : sm.availableCoordinates()) {
            st.setAsDouble(sm.getAsInt(cd[0], cd[1]), cd[1], cd[0]);
        }
        //初期値としてすべてのページに１を与える
        for (int i = 0; i < size; i++) {
            pageScore.add(1.0);
        }
        for (int y = 0; y < pageScore.size(); y++) {//列
            int[] arrayNum = new int[pageScore.size()];//各列の要素数
            //ある列の0でない要素の数を数える
            int num = 0;
            for (int x = 0; x < pageScore.size(); x++) {//行
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
                }
            }
        }

        for (int i = 0; i < 1000; i++) {
            for (int x = 0; x < pageScore.size(); x++) {//列
                //ランダムサーファーモデルにのっとりGoogleの採用している0.85
                double d = 0.85;
                double sum = 0;
                for (int y = 0; y < pageScore.size(); y++) {//行
                    double element = st.getAsDouble(x, y);
                    sum += pageScore.get(y) * element;
                }
                pageScore.set(x,(1 - d) + d * sum);
            }
        }

    }
    //pageScoreのindexとscoreが一致してる
    ArrayList<Double> getScore() {
        return pageScore;
    }
}
