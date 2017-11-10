package com.hhxh.xijiu.update;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 2017/2/17.
 */

public class UpdateService implements IUpdateService {

    private static final String NAMESPACE ="http://Supoin.MiddlePlatform";
    private static final String URL = "http://download.supoin.com:8012/OutPortForAndroid/OutPortForAndroidService.svc";
    private static final String SOAP_ACTION ="http://Supoin.MiddlePlatform/IOutPortForAndroidServiceContract/";

    @Override
    public SoapObject GetBlockSize() throws IOException, XmlPullParserException {
        String methodName = "GetBlockSize";

        SoapObject soapObject=new SoapObject(NAMESPACE, methodName);
        return accessWcf(soapObject, methodName);
    }

    @Override
    public SoapObject GetUpdateFileLenth(String UpLoadFileDir) throws IOException, XmlPullParserException {
        String methodName = "GetUpdateFileLenth";

        SoapObject soapObject=new SoapObject(NAMESPACE, methodName);
        soapObject.addProperty("UpLoadFileDir", UpLoadFileDir);
        return accessWcf(soapObject, methodName);
    }

    @Override
    public SoapObject JsonVersionGetInfoOfLater(String CFullISN, String CName) throws IOException, XmlPullParserException {
        String methodName = "JsonVersionGetInfoOfLater";

        SoapObject soapObject=new SoapObject(NAMESPACE, methodName);
        soapObject.addProperty("CFullISN",CFullISN);
        soapObject.addProperty("CName",CName);
        return accessWcf(soapObject, methodName);
    }

    @Override
    public SoapObject LoadFileByBlock(long IstartPost, String UpLoadFileDir) throws IOException, XmlPullParserException {
        String methodName = "LoadFileByBlock";

        SoapObject soapObject=new SoapObject(NAMESPACE, methodName);
        soapObject.addProperty("IstartPost", IstartPost);
        soapObject.addProperty("UpLoadFileDir", UpLoadFileDir);
        return accessWcf(soapObject, methodName);
    }

    private SoapObject accessWcf(SoapObject soapObject, String methodName) throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        //envelope.addMapping(ChangeStaffEntity.NAMESPACE, "ChangeStaffEntity", ChangeStaffEntity.class);
        envelope.setOutputSoapObject(soapObject);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.debug = true;//使用调式功能

        transportSE.call(SOAP_ACTION + methodName, envelope);
        return (SoapObject) envelope.bodyIn;
    }
}
