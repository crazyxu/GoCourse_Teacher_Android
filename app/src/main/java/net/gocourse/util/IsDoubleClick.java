package net.gocourse.util;

/**
 * 判断统一按钮是否连续点击的多次
 * 避免重要或者复杂操作多次触发
 */

public class IsDoubleClick {

    // 双击事件记录最近一次点击的ID
    private static long lastClickId = -1;
    // 双击事件记录最近一次点击的时间
    private static long lastClickTime;
    //点击次数
    private static int clickNum = 0;

    public static boolean isDoubleClick(long id) {
        //flag为false表示没有多次点击
        boolean flag = false;
        //点击次数自增1
        clickNum++;

        //当前点击ID不是上次记录ID
        if (lastClickId != id) {
            //点击次数重置为1
            clickNum = 1;

            //记录当前点击的ID和时间
            lastClickId = id;
            lastClickTime = System.currentTimeMillis();
        }
        //如果点击次数为1，表示没有多次点击，记录ID并重置点击时间
        if (1 == clickNum) {
            lastClickId = id;
            lastClickTime = System.currentTimeMillis();
        }else{
            //点击次数不为1（只能是2）,重置点击次数并判断点击ID和上次点击时的记录值是否相同
            clickNum=0;
            if (id == lastClickId) {
                //如果时间差是不超过1000ms，则认为多次点击，
                if ((Math.abs(lastClickTime - System.currentTimeMillis()) < 1000))
                    flag = true;
                //重置点击次数和时间
                clickNum = 1;
                lastClickTime = System.currentTimeMillis();
            }
        }
        return flag;
    }

}
