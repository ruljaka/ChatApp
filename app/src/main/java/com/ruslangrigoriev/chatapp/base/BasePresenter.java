package com.ruslangrigoriev.chatapp.base;

public abstract class BasePresenter<VIEW extends IView> implements IPresenter<VIEW> {

    protected VIEW view;

    @Override
    public void attach(VIEW view) {
        this.view = view;
    }

    @Override
    public void deAttach() {
        view = null;
    }
}
