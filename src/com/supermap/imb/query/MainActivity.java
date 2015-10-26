package com.supermap.imb.query;

import com.supermap.data.Color;
import com.supermap.data.CursorType;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.DatasetVector;
import com.supermap.data.GeoStyle;
import com.supermap.data.Geometry;
import com.supermap.data.QueryParameter;
import com.supermap.data.Recordset;
import com.supermap.data.SpatialQueryMode;
import com.supermap.imb.appconfig.DefaultDataConfig;
import com.supermap.imb.appconfig.MyApplication;
import com.supermap.imb.query.Gesture.DrawnListener;
import com.supermap.imb.query.Gesture.SearchAroundListener;
import com.supermap.mapping.Layers;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.MapView;
import com.supermap.mapping.TrackingLayer;
import com.supermap.mapping.dyn.DynamicStyle;
import com.supermap.mapping.dyn.DynamicView;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
/**
* <p>
* Title:��ͼ��ѯ
* </p>
* 
* <p>
* Description:
* ============================================================================>
* ------------------------------��Ȩ����----------------------------
* ���ļ�Ϊ SuperMap iMobile ��ʾDemo�Ĵ��� 
* ��Ȩ���У�������ͼ����ɷ����޹�˾
* ----------------------------------------------------------------
* ----------------------------SuperMap iMobile ��ʾDemo˵��---------------------------
* 
* 1��Demo��飺
*   չʾ�㡢�ߡ��淶Χ��ѯ�����Բ�ѯ��
* 2��Demo���ݣ�����Ŀ¼��"SuperMap/Demos/Data/QueryData/"
*           ��ͼ���ݣ�"changchun.smwu", "changchun.udb", "changchun.udd"
*           ���Ŀ¼��"/SuperMap/License/"
* 3���ؼ�����/��Ա: 
*    QueryParameter.setSpatialQueryObject();       ����
*    QueryParameter.setSpatialQueryMode();         ����
*    QueryParameter.setCursorType();               ����
*    QueryParameter.setAttributeFilter();          ����
*    DatasetVector.query();                        ����
*    Recordset.getGeometry();                      ����
*
* 4������չʾ
*   (1)�㡢�ߡ��淶Χ��ѯ��
*   (2)���Բ�ѯ��
* ------------------------------------------------------------------------------
* ============================================================================>
* </p> 
* 
* <p>
* Company: ������ͼ����ɷ����޹�˾
* </p>
* 
*/
public class MainActivity extends Activity implements OnClickListener{
	
	private MapControl mMapControl = null;
	private DynamicView mDynView = null;
	private MapView mMapView = null;
	private Gesture mGesture;
	private QueryResultPopup mPopup = null;
	private EditText mKeyQuery = null;
	private Context context = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		initUI();
		
		prepareData();
//        mMapControl.getMap().setWorkspace(MyApplication.getInstance().getOpenedWorkspace());
//        mMapControl.getMap().open(MyApplication.getInstance().getOpenedWorkspace().getMaps().get(0));
//        mMapControl.getMap().refresh();
        
//        mDynView = new DynamicView(this, mMapControl.getMap());
//        mDynView.setHitTestTolerance(BitmapFactory.decodeResource(getResources(), R.drawable.ic_btn_poi).getWidth());
//        mMapView.addDynamicView(mDynView);
//		
//        mGesture = new Gesture(mMapView,mDynView);
//		mPopup = new QueryResultPopup(mMapView,mDynView);
//		
//		mGesture.setSearchAroundListener(new SearchAroundListener() {
//			@Override
//			public void searchGeometry(Geometry geoRegion) {
//				query(geoRegion, DatasetType.POINT);
//			}
//		});
	}
	
	  /*
     * ׼����ͼ����
     */
	private void prepareData(){
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("���ݼ�����...");
		progress.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
		progress.show();
		new Thread(){
			@Override
			public void run() {
				super.run();
				//��������
				new DefaultDataConfig().autoConfig();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progress.dismiss();
						MyApplication.getInstance().openWorkspace();
						mMapControl.getMap().setWorkspace(MyApplication.getInstance().getOpenedWorkspace());
					    mMapControl.getMap().open(MyApplication.getInstance().getOpenedWorkspace().getMaps().get(0));
					       
					    mMapControl.getMap().refresh();
					    mMapControl.getMap().setFullScreenDrawModel(true);
				        mDynView = new DynamicView(context, mMapControl.getMap());
				        mDynView.setHitTestTolerance(BitmapFactory.decodeResource(getResources(), R.drawable.ic_btn_poi).getWidth());
				        mMapView.addDynamicView(mDynView);
						
				        mGesture = new Gesture(mMapView,mDynView);
						mPopup = new QueryResultPopup(mMapView,mDynView);
						
						mGesture.setSearchAroundListener(new SearchAroundListener() {
							@Override
							public void searchGeometry(Geometry geoRegion) {
								query(geoRegion, DatasetType.POINT);
							}
						});  
					    
					}
				});
			}
		}.start();
	}
	
	/**
	 * ��ʼ������
	 */
	private void initUI(){
		mMapView =  (MapView) findViewById(R.id.mapview);
    	mMapControl = mMapView.getMapControl();
    	
    	mKeyQuery = (EditText) findViewById(R.id.et_query);
    	mKeyQuery.setOnTouchListener(textOnTouchListener);
    	findViewById(R.id.btnZoomIn).setOnClickListener(this);
    	findViewById(R.id.btnZoomOut).setOnClickListener(this);
    	findViewById(R.id.btnViewEntire).setOnClickListener(this);
    	findViewById(R.id.btn_querypt).setOnClickListener(this);
    	findViewById(R.id.btn_queryline).setOnClickListener(this);
    	findViewById(R.id.btn_queryregion).setOnClickListener(this);
    	findViewById(R.id.btn_query).setOnClickListener(this);
    	findViewById(R.id.btn_list).setOnClickListener(this);
    	findViewById(R.id.btn_clear).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_querypt:
			clearTrackingLayer();
			mMapView.removeAllCallOut();
			mDynView.clear();
			mDynView.refresh();
		    mPopup.colsePoup();
			closeKeyBoard();
			
			mGesture.draw();
			mGesture.setDrawnListener(new DrawnListener() {
				@Override
				public void drawnGeometry(Geometry geoRegion) {
					query(geoRegion, DatasetType.POINT);
					geoRegion.dispose();
				}
			});
			break;
		case R.id.btn_queryline:
			clearTrackingLayer();
			mMapView.removeAllCallOut();
			mDynView.clear();
			mDynView.refresh();
		    mPopup.colsePoup();
		    closeKeyBoard();
		    
			mGesture.draw();
			mGesture.setDrawnListener(new DrawnListener() {
				@Override
				public void drawnGeometry(Geometry geoRegion) {
					query(geoRegion, DatasetType.LINE);
					geoRegion.dispose();
				}
			});
			break;
		case R.id.btn_queryregion:
			clearTrackingLayer();
			mMapView.removeAllCallOut();
			mDynView.clear();
			mDynView.refresh();
			mPopup.colsePoup();
			closeKeyBoard();
			
			mGesture.draw();
			mGesture.setDrawnListener(new DrawnListener() {
				@Override
				public void drawnGeometry(Geometry geoRegion) {
					query(geoRegion, DatasetType.REGION);
					geoRegion.dispose();
				}
			});
			break;
		case R.id.btn_query:
			clearTrackingLayer();
			mMapView.removeAllCallOut();
			mDynView.clear();
			mDynView.refresh();
			mPopup.colsePoup();
			
			String key = mKeyQuery.getText().toString();
			query(key);
			closeKeyBoard();
			break;
		case R.id.btn_list:
			mPopup.show();
			break;
		case R.id.btn_clear:
			mDynView.clear();
			mDynView.refresh();
			mMapView.removeAllCallOut();
			mPopup.dismiss();			
			clearTrackingLayer();
			mMapControl.getMap().refresh();
			break;
		case R.id.btnZoomIn:
			//double curScale = mMapControl.getMap().getScale();
			mMapControl.getMap().zoom(2);
			mMapControl.getMap().refresh();
			break;
		case R.id.btnZoomOut:
			//curScale = mMapControl.getMap().getScale();
			mMapControl.getMap().zoom(0.5);
			mMapControl.getMap().refresh();
			break;
		case R.id.btnViewEntire:
			mMapControl.getMap().viewEntire();
			mMapControl.getMap().refresh();
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * �������ѯ����
	 * @param georegion  ��ѯ����
	 * @param type       ���ݼ�����
	 */
	private void query(Geometry georegion,DatasetType type){
		mPopup.clear();
		Layers layers = mMapControl.getMap().getLayers();
		DynamicStyle style = new DynamicStyle();
		style.setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_poi));
		for(int i=layers.getCount()-1;i>=0;i--){
			
			Dataset dataset = layers.get(i).getDataset();
		
			if(dataset.getType().equals(type)){
				DatasetVector dv = (DatasetVector)dataset;
				QueryParameter param = new QueryParameter();
				param.setSpatialQueryObject(georegion);
				param.setSpatialQueryMode(SpatialQueryMode.CONTAIN); 
				param.setCursorType(CursorType.STATIC);
				Recordset recordset = dv.query(param);
				mPopup.addResult(recordset);
			}
		}
		mMapView.invalidate();
		mPopup.show();
		
		addQueryRegion(georegion);
	}
	
	/**
	 * ���ؼ��ֲ�ѯ
	 * @param key    �ؼ���
	 */
	private void query(String key){
		if(key.length()==0){
			Toast.makeText(this, "û������ؼ���", Toast.LENGTH_LONG).show();
			return;
		}
		
		mPopup.clear();
		
		Layers layers = mMapControl.getMap().getLayers();
		for(int i=layers.getCount()-1;i>=0;i--){
			Dataset dataset = layers.get(i).getDataset();
			if(dataset.getType().equals(DatasetType.POINT)){
				DatasetVector dv = (DatasetVector)dataset;
				QueryParameter param = new QueryParameter();
				param.setAttributeFilter("name like \"%"+key+"%\"");
				Recordset recordset = dv.query(param);
				mPopup.addResult(recordset);
			}
		}
		mPopup.show();
	}

	/**
	 * �ڸ��ٲ�����Ӳ�ѯ����
	 * @param georegion
	 */
	private void addQueryRegion(Geometry georegion){
		GeoStyle style = new GeoStyle();
		style.setFillForeColor(new Color(190, 190, 190));
		style.setFillOpaqueRate(30);	
		style.setLineColor(new Color(180, 180, 200));
		style.setLineWidth(0.4);		
		georegion.setStyle(style);
		
		mMapControl.getMap().getTrackingLayer().add(georegion, "");
		mMapControl.getMap().refresh();
	}
	
	/**
	 * ��ո��ٲ�
	 */
	private void clearTrackingLayer(){
		if (mMapControl.getMap().getTrackingLayer().getCount() < 1) {
			return;
		}
		
		mMapControl.getMap().getTrackingLayer().clear();
		mMapControl.getMap().refresh();
	}
	
	private OnTouchListener textOnTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			mPopup.colsePoup();

			return false;
		}

	};

	/**
	 * �رռ���
	 */
	private void closeKeyBoard(){
		//���ؼ���
		InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(mKeyQuery.getWindowToken(), 0);
		

	}
}
