package com.hhxh.xijiu.utils;

import android.util.Base64;


import com.hhxh.xijiu.update.UpdateService;

import org.json.JSONArray;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 2017/2/14.
 */

public class NetworkUtility {

    private static UpdateService updateService = new UpdateService();

    public static String GetUpdateFileLenth(String UpLoadFileDir){
        String result = null;
        JSONArray array = new JSONArray();
        SoapObject soapObj;
        try {
            soapObj = updateService.GetUpdateFileLenth(UpLoadFileDir);// new GetUpdateFileLenthService(UpLoadFileDir).LoadResult();
            if (soapObj != null
                    && SoapPrimitive.class.isInstance(soapObj.getProperty(0))) {
                SoapPrimitive soap = (SoapPrimitive) soapObj.getProperty(0);
                result = (String) soap.getValue();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;

    }

    public static String GetBlockSize(){
        String result = null;
        JSONArray array = new JSONArray();
        SoapObject soapObj;
        try {
            soapObj = updateService.GetBlockSize();// new GetBlockSizeService().LoadResult();
            if (soapObj != null
                    && SoapPrimitive.class.isInstance(soapObj.getProperty(0))) {
                SoapPrimitive soap = (SoapPrimitive) soapObj.getProperty(0);
                result = (String) soap.getValue();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;

    }

    public static String JsonVersionGetInfoOfLater(String CFullISN, String CName){

        String result = null;
        JSONArray array = new JSONArray();
        SoapObject soapObj;
        try {
            soapObj = updateService.JsonVersionGetInfoOfLater(CFullISN, CName);// new JsonVersionGetInfoOfLaterService(CFullISN, CName).LoadResult();
            if (soapObj != null) {
                if(SoapPrimitive.class.isInstance(soapObj.getProperty(0))){
                    SoapPrimitive soap = (SoapPrimitive) soapObj.getProperty(0);
                    result = (String) soap.getValue();
                }else{
                    Object soap =  soapObj.getProperty(0);
                    if(soap.toString().equals("anyType{}"))
                        result="latest";
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;

    }

    public static byte[] LoadFileByBlock (long IstartPost, String UpLoadFileDir){
        byte[] result = null;
        JSONArray array = new JSONArray();
        SoapObject soapObj;
        try {
            soapObj = updateService.LoadFileByBlock(IstartPost, UpLoadFileDir);//new LoadFileByBlockService(IstartPost, UpLoadFileDir).LoadResult();
            if (soapObj != null
                    && SoapPrimitive.class.isInstance(soapObj.getProperty(0))) {
                SoapPrimitive soap = (SoapPrimitive) soapObj.getProperty(0);
//					result=soap.get
                result = Base64.decode(soapObj.getProperty(0).toString(), Base64.DEFAULT);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
