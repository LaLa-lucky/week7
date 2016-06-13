package com.example.week7.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.example.week7.R;
import com.example.week7.adapter.LocationListViewAdapter;
import com.example.week7.domain.Location;
import com.example.week7.utils.ToastUtils;

/**
 * ѡ������λ�õ�ҳ��
 * 
 * @author Administrator
 * 
 */
public class ChooseLocationActivity extends BaseActivity implements
		BDLocationListener {
	private static final int LOCATION_SUCCESS = 0;// ��λ�ɹ�
	private static final int LOCATION_FAIL = 1;// ��λʧ��
	private ListView lvLocation;
	private LocationListViewAdapter adapter;
	private ArrayList<Location> locationList;
	private LinearLayout llProgress;

	/**
	 * ��λ���ö���
	 */
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = this;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOCATION_FAIL:
				llProgress.setVisibility(View.GONE);
				ToastUtils.showToast(ChooseLocationActivity.this,
						(String) msg.obj);
				break;
			case LOCATION_SUCCESS:
				llProgress.setVisibility(View.GONE);
				adapter = new LocationListViewAdapter(
						ChooseLocationActivity.this, locationList);
				lvLocation.setAdapter(adapter);
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * ��ʼ��λ����Ϣ
		 */
		mLocationClient = new LocationClient(this); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������
		initLocation();
		// ������λ����
		mLocationClient.start();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView();
		initListener();
	}

	/**
	 * ��ʼ��������
	 */
	private void initListener() {
		lvLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent data = new Intent();
				data.putExtra("location", locationList.get(position));
				setResult(RESULT_OK, data);
				finish();
			}
		});
	}

	/**
	 * ��ʼ��UI
	 */
	private void initView() {
		setContentView(R.layout.activity_choose_location);
		lvLocation = (ListView) findViewById(R.id.lv_location);
		llProgress = (LinearLayout) findViewById(R.id.ll_progress);
	}

	/**
	 * ��ʼ����λSDK����
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 0;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(false);// ��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}

	/**
	 * ���ܵ���λ����Ļص�
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {
		String province = null, city = null;
		String exception = null;
		if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS��λ���
			province = location.getProvince();
			city = location.getCity();
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ���綨λ���
			province = location.getProvince();
			city = location.getCity();
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
			province = location.getProvince();
			exception = "��ȷ������GPS������";
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			exception = "��������綨λʧ�ܣ����Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��";
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			exception = "���粻ͬ���¶�λʧ�ܣ����������Ƿ�ͨ��";
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			exception = "�޷���ȡ��Ч��λ���ݵ��¶�λʧ�ܣ�һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�";
		}
		List<Poi> list = location.getPoiList();
		locationList = new ArrayList<Location>();
		for (Poi poi : list) {
			Location l = new Location(province, city, poi.getName());
			locationList.add(l);
		}
		Message message = handler.obtainMessage();
		if (exception == null) {
			message.what = LOCATION_SUCCESS;
		} else {
			message.obj = exception;
			message.what = LOCATION_FAIL;
		}
		handler.sendMessage(message);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ֹͣ��λ����
		mLocationClient.stop();
	}
}
