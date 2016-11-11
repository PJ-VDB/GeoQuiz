package com.example.pieter_jan.geoquiz;

/**
 * Created by pieter-jan on 11/11/2016.
 */

/**
 * The 'Model'
 */

public class Question {


    //Use 'm' as a naming convention for member variables
    private int mTextResId; // The question text
    private boolean mAnswerTrue; // The question answer

    public Question(int textResId, boolean answerTrue) {
        mAnswerTrue = answerTrue;
        mTextResId = textResId;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
