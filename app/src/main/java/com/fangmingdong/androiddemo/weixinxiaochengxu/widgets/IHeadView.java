package com.fangmingdong.androiddemo.weixinxiaochengxu.widgets;

/**
 * Created by nerc on 2018/1/11.
 */

public interface IHeadView {

    void onPull(float value);

    void setState(State state);

    /**
     * 作为滑动的时候的一个 临界点
     * @return
     */
    int getContentHeight();

}
