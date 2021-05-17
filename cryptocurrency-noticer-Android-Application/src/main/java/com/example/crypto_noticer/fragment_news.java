package com.example.crypto_noticer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;





public class fragment_news extends Fragment
{
    ViewGroup viewGroup;
    private WebView mWebView;

    private String myUrl = "http://";// 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_news,container,false); //layout xml added

        // 웹뷰 셋팅
        mWebView = (WebView) viewGroup.findViewById(R.id.webView);//xml 자바코드 연결
        mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용

        mWebView.loadUrl("https://www.cnbc.com/search/?query=cryptocurrency&qsearchterm=cryptocurrency");//웹뷰 실행


        return viewGroup;
    }
}


