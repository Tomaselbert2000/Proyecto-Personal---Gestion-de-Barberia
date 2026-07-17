package com.launcher.controller.interfaces;

public interface ItemController<T> extends Controller {

    void setDataOnItem(T infoDTO);
}
