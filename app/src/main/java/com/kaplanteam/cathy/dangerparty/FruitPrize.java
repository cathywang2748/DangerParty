package com.kaplanteam.cathy.dangerparty;

/**
 * Created by per6 on May 07, 2018 at 12:28
 */

public class FruitPrize {
    private String desc;
    private int fruitPic;

    public FruitPrize(String desc, int fruitPic) {
        this.desc = desc;
        this.fruitPic = fruitPic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getFruitPic() {
        return fruitPic;
    }

    public void setFruitPic(int fruitPic) {
        this.fruitPic = fruitPic;
    }
}
