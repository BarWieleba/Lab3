package com.example.myfrags;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements Fragment1.onButtonClickListener{

    /*private FragmentManager fragmentManager;
    private Fragment fragment1, fragment2, fragment3, fragment4;*/

    private int[] frames;
    private boolean hidden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            frames = new int[]{
                    R.id.frame1,
                    R.id.frame2,
                    R.id.frame3,
                    R.id.frame4,
            };
            hidden = false;

            Fragment[] fragments = new Fragment[]{ new Fragment1(), new Fragment_2(), new Fragment_3(), new Fragment_4()};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            for(int i = 0; i < 4; i++){
                transaction.add(frames[i], fragments[i]);
            }
            transaction.addToBackStack(null);
            transaction.commit();
        }
        else{
            frames = savedInstanceState.getIntArray("FRAMES");
            hidden = savedInstanceState.getBoolean("HIDDEN");
        }


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putIntArray("FRAMES", frames);
        outState.putBoolean("HIDDEN", hidden);
    }

    @Override
    public void onButtonClickShuffle() {
        /*Toast.makeText(getApplicationContext(), "SHUFFLE", Toast.LENGTH_SHORT).show();*/

        List<Integer> list = new ArrayList<Integer>(Arrays.asList(frames[0], frames[1], frames[2], frames[3]));
        Collections.shuffle(list);
        for (int i = 0; i < 4; i++) frames[i] = list.get(i).intValue();

        newFragments();
    }

    @Override
    public void onButtonClickClockwise() {
        /*Toast.makeText(getApplicationContext(), "CLOCKWISE", Toast.LENGTH_SHORT).show();*/

        int t = frames[0];
        frames[0] = frames[1];
        frames[1] = frames[2];
        frames[2] = frames[3];
        frames[3] = t;

        newFragments();
    }

    @Override
    public void onButtonClickHide() {
        /*Toast.makeText(getApplicationContext(), "HIDE", Toast.LENGTH_SHORT).show();*/
        if(hidden){
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment f : fragmentManager.getFragments()){
            if(f instanceof Fragment1){
                continue;
            }
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(f);
            transaction.addToBackStack(null);
            transaction.commit();
        }
        hidden=true;
    }

    @Override
    public void onButtonClickRestore() {
        /*Toast.makeText(getApplicationContext(), "RESTORE", Toast.LENGTH_SHORT).show();*/

        if(!hidden){
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        for (Fragment f : fragmentManager.getFragments()){
            if(f instanceof Fragment1){
                continue;
            }
            transaction.show(f);
        }
        transaction.addToBackStack(null);
        transaction.commit();

        hidden=false;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Fragment1){
            ((Fragment1) fragment).setOnButtonClickListener(this);
        }
    }

    private void newFragments(){
        Fragment[] newFragments = new Fragment[]{new Fragment1(), new Fragment_2(), new Fragment_3(), new Fragment_4()};

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        for (int i = 0; i < 4; i++) {
            transaction.replace(frames[i], newFragments[i]);
            if (hidden && !(newFragments[i] instanceof Fragment1)) transaction.hide(newFragments[i]);
        }

        transaction.addToBackStack(null);
        transaction.commit();
    }
}