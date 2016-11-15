package com.example.pieter_jan.geoquiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Controller: connects the model ('Question.java') and the views ('activity_quiz.xml')
 */

public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;
    private TextView mQuestionTextView;
    private Button mCheatButton;

    // An array for the questions
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, true),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };

    // The index for the question
    private int mCurrentIndex = 0;
    // Tag for Debugging
    private static final String TAG = "QuizActivity";
    // Key to save state across screen rotations
    private static final String KEY_INDEX = "index";
    // Notification if the user is cheating
    private static final int REQUEST_CODE_CHEAT = 0;
    // did the user cheat
    //private boolean mIsCheater; // removed because it will be tied to the question number

    // Array to keep track of what question was cheated on;
    private boolean[] cheatArray = new boolean[mQuestionBank.length];
    private final String[] KEY_ARRAY = new String[mQuestionBank.length]; // the key-value pairs used to save the instance


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log a message
        Log.d(TAG, "onCreate (Bundle) called");
        setContentView(R.layout.activity_quiz);

        // Create the keyarray to save the instance states to each question
        createKeysInArray();

        //Get references to widgets
        mTrueButton = (Button) findViewById(R.id.true_button);
        mFalseButton = (Button) findViewById(R.id.false_button);

        //Set listeners to widgets
        //Listens to on clicks
        // Everything within the brackets is an 'anonymous inner class': a new nameless class with its entire implementation
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        //Normally you do the listening and referencing per widget, so we will do that from now on.

        // Attaching the question to the textview
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        // Users can also press the text field to go to the next question
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        // Make the next button functional

        mNextButton = (ImageButton) findViewById(R.id.next_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                cheatArray[mCurrentIndex] = false;
                updateQuestion();
            }
        });

        // Make a previous button

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                cheatArray[mCurrentIndex] = false;
                updateQuestion();
            }
        });

        // Add a cheat button

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        // Check if there is a previous instance (from screen rotation)
        if (savedInstanceState != null){
            // Save the index of the current question; when the screen is rotated the question stays the same
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            // the user can not clear mIsCheater by rotating the screen
            //mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);

            for(int i =0; i<KEY_ARRAY.length; i++){
                cheatArray[i] = savedInstanceState.getBoolean(KEY_ARRAY[i],false);
            }

        }


        updateQuestion();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, mCurrentIndex);
//        outState.putBoolean(KEY_CHEATER, mIsCheater);

        // save the values from cheatArray
        for(int i=0; i<KEY_ARRAY.length; ++i){
            outState.putBoolean(KEY_ARRAY[i], cheatArray[i]);
        }

    }

    private void updateQuestion(){
        // Create a stack trace to see where the method is called; for debugging purposes
        // Log.d(TAG, "Updating questiono text for question #" + mCurrentIndex, new Exception());
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question); // setText method requires the resource id of the string

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode != Activity.RESULT_OK){
//            return;
//        }
//
//        if(requestCode == REQUEST_CODE_CHEAT){
//            if(data == null){
//                return;
//            }
////            mIsCheater = CheatActivity.wasAnswerShown(data);

        if (data == null) {
            return;
        }
            cheatArray[mCurrentIndex] = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false); // made the key public, not sure if it's allowed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    private void checkAnswer(boolean userPressedTrue){
        // Check if the answer is true or not
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        // Initialize response
        int messageResId = 0;

        if (cheatArray[mCurrentIndex]){
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    }

    /**
     * Popule the KEY_ARRAY in order to save instance states (to check if someone cheated before)
     * the keys will look like: question_0; question_1; ...
     */
    private void createKeysInArray(){
        for(int i=0; i<KEY_ARRAY.length; ++i){
            KEY_ARRAY[i] = "question_"+i;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
