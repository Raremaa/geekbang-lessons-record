package com.masaiqi.geekbang.mvc.controller;

/**
 * 控制器类 - 作为一个Java SPI接口方便后续类发现
 *
 * @author masaiqi
 * @date 2021/3/1 3:18 PM
 */
public interface Controller {

    /**
     * 提供一个初始化钩子方法
     */
    void init();
}
