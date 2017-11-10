package com.hhxh.xijiu.update;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Administrator on 2017/2/17.
 */

public interface IUpdateService {
    SoapObject GetBlockSize() throws IOException, XmlPullParserException;
    SoapObject GetUpdateFileLenth(String UpLoadFileDir) throws IOException, XmlPullParserException;
    SoapObject JsonVersionGetInfoOfLater(String CFullISN, String CName) throws IOException, XmlPullParserException;
    SoapObject LoadFileByBlock(long IstartPost, String UpLoadFileDir) throws IOException, XmlPullParserException;
}
