package io.github.junhaoshih.linebotserver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 擲骰子的結果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiceResult {
    /**
     * 投擲者名稱
     */
    private String rollerName;

    /**
     * 所有投擲的點數
     */
    private List<Integer> points = new ArrayList<>();

    /**
     * 用int array初始化
     * @param pointArray 所有投擲的點數
     */
    public DiceResult(int[] pointArray) {
        for (int j : pointArray) {
            points.add(j);
        }
    }

    /**
     * 骰子數量
     * @return 骰子數量
     */
    public int getDiceCount() {
        return points.size();
    }
}
