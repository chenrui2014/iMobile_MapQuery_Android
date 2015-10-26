package com.supermap.imb.appconfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipException;
import com.supermp.imb.file.FileManager;
import com.supermp.imb.file.MyAssetManager;
import com.supermp.imb.file.ZipUtils;

public class DefaultDataConfig {

	private final       String QueryData     = "QueryData";
	public static final String QueryDataPath = MyApplication.SDCARD+"SuperMap/Demos/Data/QueryData/";
	public static final String LicPath       = MyApplication.SDCARD+"SuperMap/License/";
	private final       String LicName       = "Trial License.slm";
	
	private static final String DefaultServer = "Changchun.smwu";
	
	public static String WorkspacePath = QueryDataPath+DefaultServer;
	
	
	public DefaultDataConfig()
	{
		
	}
	
	/**
	 * ��������
	 */
	public void autoConfig(){
		//���������������Ϊ�û��Ѿ�����������
		String mapDataPah = QueryDataPath;
		String license    = LicPath + LicName;
		
		File licenseFile = new File(license);
		if(!licenseFile.exists())
			configLic();
		
		
		File dir = new File(mapDataPah);
		if(!dir.exists()){
			FileManager.getInstance().mkdirs(mapDataPah);
			//configLic();
			if(!FileManager.getInstance().isFileExsit(mapDataPah+DefaultServer)){
			    configMapData();
			}
		}else{
			//�������û���������ô�ɣ�������һ��root���أ�
			if(FileManager.getInstance().isFileExsit(mapDataPah+DefaultServer)){
				
				return;
			}
			boolean hasMapData = false;
			File[] datas = dir.listFiles();
			for(File data:datas){
				if(data.getName().endsWith("SMWU")||data.getName().endsWith("smwu")
					||data.getName().endsWith("SXWU")||data.getName().endsWith("sxwu"))
				{
					//���Ĭ�ϵ����ݱ�ɾ�����Ǿͼ��ص�һ�������ռ�
					WorkspacePath = data.getAbsolutePath();
					hasMapData = true;
					break;
				}
			}
			if(!hasMapData)
			{
				//configLic();
				configMapData();
			}
		}
	}
	
	/**
	 * ��������ļ�
	 */
	private void configLic()
	{
		InputStream is = MyAssetManager.getInstance().open(LicName);
		if(is != null)
		   FileManager.getInstance().copy(is, LicPath+LicName);
	}
	
	/**
	 * ���õ�ͼ����
	 */
	private void configMapData(){
		String[] datas = MyAssetManager.getInstance().opendDir(QueryData);
		for(String data:datas){
			InputStream is = MyAssetManager.getInstance().open(QueryData+"/"+data);
			String zip = QueryDataPath+"/"+data;
			boolean result = FileManager.getInstance().copy(is, zip);
			if(result){
				try {
					File zipFile = new File(zip);
					ZipUtils.upZipFile(zipFile, QueryDataPath);
					//ɾ��ѹ����
					zipFile.delete();
				} catch (ZipException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
