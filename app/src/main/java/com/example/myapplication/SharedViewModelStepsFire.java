package com.example.myapplication;

import androidx.lifecycle.ViewModel;

public class SharedViewModelStepsFire extends ViewModel {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}