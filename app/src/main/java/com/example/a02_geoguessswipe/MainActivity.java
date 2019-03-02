package com.example.a02_geoguessswipe;

// Created by Ates Yanik - HVA ICT - Deeltijd
// Student number #500790232


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    List<GeoObject> mGeoObjects;
    private GestureDetector mGestureDetector;
    Boolean answer;
    private int trueAnswerCounter;
    private int falseAnswerCounter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGeoObjects = new ArrayList<>();
        trueAnswerCounter = 0;
        falseAnswerCounter = 0;

        for (int i = 0; i < GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES.length; i++) {

            mGeoObjects.add(new GeoObject(GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES[i],
                    GeoObject.PRE_DEFINED_GEO_OBJECT_IMAGE_IDS[i], GeoObject.IS_EUROPEAN[i]));
        }

        final RecyclerView mGeoRecyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

        mGeoRecyclerView.setLayoutManager(mLayoutManager);
        mGeoRecyclerView.setHasFixedSize(true);
        final GeoObjectAdapter mAdapter = new GeoObjectAdapter(this, mGeoObjects);
        mGeoRecyclerView.setAdapter(mAdapter);
        mGeoRecyclerView.addOnItemTouchListener(this);

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        checkAnswer(position,swipeDir);

                        mGeoObjects.remove(position);
                        mAdapter.notifyItemRemoved(position);

                        if (mGeoObjects.size()< 1) {
                            gameOverMessage();
                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mGeoRecyclerView);





    }

    public void checkAnswer(int index, int directionSwipe) {
        answer = mGeoObjects.get(index).getmIsEurooean();

        switch (directionSwipe) {
            case ItemTouchHelper.LEFT:
                if (answer.equals(false)) {
                    correctAnswer();
                    trueAnswerCounter++;
                } else {
                    inCorrectAnswer();
                    falseAnswerCounter++;
                }
                break;
            case ItemTouchHelper.RIGHT:
                if (answer.equals(true)) {
                    correctAnswer();
                    trueAnswerCounter++;
                } else {
                    inCorrectAnswer();
                    falseAnswerCounter++;
                }
                break;
            default:

                break;
        }


    }



    public void correctAnswer() {
        Toast.makeText(this,"Correct", Toast.LENGTH_SHORT).show();
    }

    public void inCorrectAnswer() {
        Toast.makeText(this,"Incorrect", Toast.LENGTH_SHORT).show();
    }

    public void gameOverMessage() {


            if(trueAnswerCounter > falseAnswerCounter) {
                Toast.makeText(this,"YOU WIN! \nCorrect answers: " + trueAnswerCounter + "\n Wrong answers: " + falseAnswerCounter , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,"YOU LOST! \nWrong answers: " + falseAnswerCounter + "\n Correct answers: " + trueAnswerCounter, Toast.LENGTH_LONG).show();
            }


    }



    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        int mAdapterPosition = rv.getChildAdapterPosition(child);
        if (child != null && mGestureDetector.onTouchEvent(e)) {
            Toast.makeText(this, mGeoObjects.get(mAdapterPosition).getmGeoName(), Toast.LENGTH_SHORT).show();
        }
        return false;

    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
