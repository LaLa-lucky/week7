package com.example.week7.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.week7.R;
import com.example.week7.constant.Config;

/**
 * �����������
 * 
 * @author Administrator
 * 
 */
public class NewsDetailActivity extends BaseActivity implements OnClickListener {

	private ImageView ivBack;
	private WebView webView;
	private TextView tvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initData();
		initListener();
	}

	/**
	 * ��ʼ������
	 */
	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String title = bundle.getString("title");
		String url = bundle.getString("url");
		String number = bundle.getString("number");
		String date = bundle.getString("date");
		// ������ص������ַ
		String address = Config.SERVER_URL + "?c=Newspage&a=show&url=" + url
				+ "&title=" + title + "&number=" + number + "&date=" + date;
		webView.loadUrl(address);
		// ����WebView
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);// ����֧��js
		webSettings.setUseWideViewPort(true);// ���ô����ԣ��������������
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);// ҳ��֧������
		webSettings.setSupportZoom(true);
		// �����WebView������ҳ��
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		tvTitle.setText(title);
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_news_detail);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		webView = (WebView) findViewById(R.id.webView);
		tvTitle = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		}
	}

}
