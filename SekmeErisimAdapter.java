package com.example.wpchatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class SekmeErisimAdapter extends FragmentPagerAdapter {

    public SekmeErisimAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i)
    {
        switch (i)

        {
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                GruplarFragment gruplarFragment = new GruplarFragment();
                return gruplarFragment;

            case 2:
                KisilerFragment kisilerFragment = new KisilerFragment();
                return kisilerFragment;

            default:
                return null;

        }



    }

    @Override
    public int getCount()
    {
        return 3;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position)
    {
        switch (position)

        {
            case 0:
                return "Sohbetler";

            case 1:
               return "Gruplar";

            case 2:
               return "Kişiler";

            default:
                return null;

        }



    }
}
