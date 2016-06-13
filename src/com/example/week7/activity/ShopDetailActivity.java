package com.example.week7.activity;

import com.example.week7.R;
import com.example.week7.constant.Config;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * У԰С����ϸ����
 * 
 * @author Administrator
 * 
 */
public class ShopDetailActivity extends BaseActivity implements OnClickListener {
	private ImageView ivBack;
	private LinearLayout llProgress;
	private WebView webView;
	private String id;// ���ݿ��е�id
	private Button btnCall;
	private String tel;// ��ϰ�绰

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
		id = intent.getStringExtra("id");
		tel = intent.getStringExtra("tel");
		initWebView();
	}

	/**
	 * ��ʼ��WebView
	 */
	private void initWebView() {
		String url = Config.SERVER_URL + "?c=Goods&a=get_goods_by_id&gid=" + id;
		webView.loadUrl(url);
		// ����javascript֧��
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				llProgress.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				llProgress.setVisibility(View.GONE);
			}

		});
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		ivBack.setOnClickListener(this);
		btnCall.setOnClickListener(this);
	}

	/**
	 * ��ʼ������
	 */
	private void initView() {
		setContentView(R.layout.activity_shop_detail);
		ivBack = (ImageView) findViewById(R.id.iv_back);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
		webView = (WebView) findViewById(R.id.webView);
		btnCall = (Button) findViewById(R.id.btn_call);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// �������ĺ��˰�ť
		case R.id.iv_back:
			finish();
			break;
		// ��ϰ��
		case R.id.btn_call:
			Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + tel));
			startActivity(intent);
			break;
		}
	}
}
