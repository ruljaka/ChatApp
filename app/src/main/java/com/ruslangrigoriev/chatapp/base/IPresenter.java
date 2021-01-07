package com.ruslangrigoriev.chatapp.base;

public interface IPresenter<VIEW extends IView> {
    void attach(VIEW view);

    void deAttach();

    void changeStatus(String status);
}
