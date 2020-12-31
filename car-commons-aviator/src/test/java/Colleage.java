import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @description:
 * @Date : 2018/11/23 下午3:58
 * @Author : 石冬冬-Seig Heil(dongdong.shi@mljr.com)
 */
public class Colleage implements Serializable{
    private static final long serialVersionUID = -2528173608057450125L;
    private String name;
    private double score;

    public Colleage() {
    }

    public Colleage(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
