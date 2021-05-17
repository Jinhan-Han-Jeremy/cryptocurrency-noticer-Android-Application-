package com.example.crypto_noticer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_alert_set extends Fragment
{
    ViewGroup viewGroup;
    private TextView mTextView;
    private Button mBtnPrev;
    private Button mBtnNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_alert_set,container,false); //layout xml added
        return viewGroup;
    }








}