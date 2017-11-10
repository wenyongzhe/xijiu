package com.hhxh.xijiu.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2017/2/14.
 */

public class ZipCompressUtility {

    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte
    private  int BLOCK = 2048;
    /*
     * Java文件操作 获取文件扩展名
     *
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
    /*
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    /// <summary>
    /// 压缩文件, 压缩文件名采用源文件名+.zip
    /// </summary>
    /// <param name="sourceFile">源文件名带路径</param>
    /// <param name="desPath">压缩后放的目录</param>
    /// <example>
    ///     <remarks>
    ///         将C:\123.txt压缩后放入到C:\zip目录下，文件名为123.zip
    ///     </remarks>
    ///     <code>        ///
    ///         SharpZip.ZipFile("C:\\123.txt", "C:\\zip");
    ///     </code>
    /// </example>
    /// <returns>无</returns>
    public static void ZipFile(String sourceFile, String desPath) throws IOException
    {
        File file = new File(sourceFile);
        if (!file.exists())
            try {
                throw new Exception("指定的源文件不存在!");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        String sourceFileWithout = getFileNameNoEx(sourceFile);//源文件名不带扩展名
//        		Path.GetFileNameWithoutExtension(sourceFile);       //源文件名不带扩展名
        String zipFile = String.format("{0}/{1}.zip", desPath, sourceFileWithout);   //目标文件
        //压缩文件名为空时使用源文件名+.zip
        File file1= new File(zipFile);
        if(!file1.exists())
            file1.createNewFile();
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream s = new ZipOutputStream(new BufferedOutputStream(fos) );
        ZipEntry entry = null ;
//        while ((entry = s.getNextEntry()) != null){
        try {
            s.setLevel(9);      //设置压缩级别0-9
            byte[] buffer = new byte[4096];
            String strEntry; //保存每个zip的条目名称
            strEntry = entry.getName();
            File entryFile = new File(desPath + strEntry);
            File entryDir = new File(entryFile.getParent());
            if (!entryDir.exists()) {
                entryDir.mkdirs();
            }
            entry = new ZipEntry(entryFile.getName());
            s.putNextEntry(entry);
//            using (FileStream fs = File.OpenRead(sourceFile))
            FileInputStream fs = new FileInputStream(sourceFile);
            {
                int soureBytes = 0;
                do
                {
                    soureBytes = fs.read(buffer, 0, buffer.length);
                    s.write(buffer, 0, soureBytes);
                } while (soureBytes > 0);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            s.finish();
            s.close();
        }
    }

    /// <summary>
    /// 解压缩文件
    /// </summary>
    /// <param name="sourceZip">压缩文件全名带路径</param>
    /// <param name="unZipPath">解压的目录</param>
    /// <example>
    ///     <remarks>
    ///         将C:\zip\123.zip压缩文件解压后放入到C:\目录下
    ///     </remarks>
    ///     <code>
    ///         SharpZip.UnZipFile("C:\\zip\\123.txt", "C:\\");
    ///     </code>
    /// </example>
    /// <returns>无</returns>
    public static void UnZipFile(String sourceZip, String unZipPath) throws Exception
    {
        File desDir = new File(unZipPath);
        if(!desDir.exists())
        {
            desDir.mkdirs();
        }
        if(!desDir.getAbsoluteFile().exists())
        {
            desDir.getAbsoluteFile().mkdirs();
        }
        ZipFile zf = new ZipFile(sourceZip);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();)
        {
            ZipEntry entry = ((ZipEntry)entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = unZipPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if(desFile.isDirectory()){
                desFile.delete();
                desFile.createNewFile();
            }
            if (!desFile.exists())
            {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists())
                {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[1024];
            int realLength;
            while ((realLength = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
//    	File file = new File(sourceZip);
//        if (!file.exists())
//            throw new Exception("指定的zip文件不存在!");
//        File fileDir = new File(unZipPath);
//        if(!fileDir.exists())
//        	fileDir.mkdirs();
//        FileInputStream fis = new FileInputStream(sourceZip);
//    	ZipInputStream s = new ZipInputStream(new BufferedInputStream(fis));
////        ZipInputStream s = new ZipInputStream(File.OpenRead(sourceZip));
//        try
//        {
//        	BufferedOutputStream dest = null; //缓冲输出流
//            ZipEntry theEntry = s.getNextEntry();
//            int count;
//	    	byte data[] = new byte[4096];
//            if (theEntry != null)
//            {
//                String strFile = String.format("{0}/{1}", unZipPath, theEntry.getName());
//                File file1 = new File(strFile);
//                if(!file1.exists())
//                	file1.delete();
//                FileOutputStream fos = new FileOutputStream(strFile);
//    	    	dest = new BufferedOutputStream(fos, 4096);
//    	    	while ((count = s.read(data, 0, 4096)) != -1) {
//    	    		dest.write(data, 0, count);
//    	    	}
//
//            }
//        }
//        catch (Exception ex)
//        {
//            throw ex;
//        }
//        finally
//        {
//            s.closeEntry();
//            s.close();
//        }
    }



    /// <summary>
    /// 压缩多个文件
    /// </summary>
    /// <param name="fileList">被压缩的文件列表</param>
    /// <param name="zipFileName">不带路径的压缩文件名</param>
    /// <param name="descPath">解压至目录</param>
    /// <param name="deleteSourceFile">是否删除原文件</param>
    /// <example>
    ///     <remarks>
    ///         将1.txt,2.txt两个文件压缩成t.zip并放在c:\zip目录下。
    ///     </remarks>
    ///     <code>
    ///         List&lt;String&gt; fileList = new List&lt;String&gt;;
    ///         fileList.Add("C:\\1.txt");
    ///         fileList.Add("C:\\2.txt");
    ///
    ///         SharpZip.ZipMutiFiles(fileList, "t.zip", "C:\\zip", true);
    ///     </code>
    /// </example>
    /// <returns>返回文件长度</returns>
    public static long ZipMutiFiles(List<String> fileList, String zipFileName, String descPath, Boolean deleteSourceFile) throws IOException
    {
//    	ZipOutputStream s = new ZipOutputStream(File.Create(String.format("{0}/{1}", descPath, zipFileName)));
        String strFile=descPath+zipFileName;//String.format("{0}/{1}", descPath, zipFileName);
//    	File zipFile= new File(strFile);
//    	if(!zipFile.exists())
//    		zipFile.createNewFile();
        FileOutputStream fos=new FileOutputStream(strFile);
        ZipOutputStream s = new ZipOutputStream(new BufferedOutputStream(fos));
        try
        {
            s.setLevel(9);      //设置压缩级别0-9
            byte[] buffer = new byte[4096];
            BufferedOutputStream dest =null;
            for (String file : fileList)
            {
                File strDir = new File(descPath);

                if (!descPath.equals("") && !strDir.exists())
                    strDir.mkdirs();
                String filename = "";
                File file1;
                if(descPath.equals("")){
                    file1 = new File(file);
                    filename =file1.getName();
                }else{
                    file1 = new File(file);
                    filename=descPath+file1.getName();//String.format("{0}/{1}", descPath, file1.getName());
                }
                file1= new File(filename);
//                zipFile(file1, zipout, rootpath)
                ZipEntry entry = new ZipEntry(file1.getName());
//                entry.DateTime = DateTime.Now;
                Date date=new Date();
//                entry.setTime(Long.parseLong(Helper.getdateandtimeby24(date)));
                s.putNextEntry(entry);
                FileInputStream fs = new FileInputStream(filename);
                int soureBytes = 0;
                while ((soureBytes = fs.read(buffer)) != -1) {
                    s.write(buffer, 0, soureBytes);
                }
                fs.close();
//                {
//                    do
//                    {
//                        soureBytes = fs.read(buffer, 0, buffer.length);
//                        s.write(buffer, 0, soureBytes);
//                    } while (soureBytes > 0);
//                    fs.close();
//                }
                if (deleteSourceFile)
                    file1.delete();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            s.finish();
            s.close();
        }
        File f = new File(descPath+zipFileName);
//        FileInfo f = new FileInfo(String.Format(@"{0}\{1}", descPath, zipFileName));
        return f.length();
    }
    /// <summary>
    /// 解压多个文件
    /// </summary>
    /// <param name="zipFileName">压缩文件</param>
    /// <param name="descPath">解压目标路径</param>
    /// <param name="deleteZipFile">解压后是否删除原压缩文件</param>
    /// <example>
    ///     <remarks>
    ///         将t.zip文件解压并放在c:\zip目录下，并删除掉原来的压缩文件。
    ///     </remarks>
    ///     <code>
    ///         List&lt;String&gt; fileList = null;
    ///
    ///         SharpZip.UnZipMutiFile("t.zip", "C:\\zip", true);
    ///     </code>
    /// </example>
    /// <returns>返回解压文件列表</returns>
    public static List<String> UnZipMutiFile(String zipFileName, String descPath, boolean deleteZipFile) throws Exception
    {
        List<String> fileList = new ArrayList<String>();
        File file = new File(zipFileName);
        if (!file.exists())
            throw new Exception("指定的zip文件不存在!");
        File strDir= new File(descPath);
        if (!strDir.exists())
            strDir.mkdirs();
        FileInputStream fs= new FileInputStream(zipFileName);
        ZipInputStream s = new ZipInputStream(new BufferedInputStream(fs));
        try
        {
            ZipEntry theEntry = null;
            while ((theEntry = s.getNextEntry()) != null)
            {
                String strfile = String.format("{0}/{1}", descPath, theEntry.getName());
                File file1=new File(strfile);
                if (file1.exists())
                    file1.delete();
                FileOutputStream fos = new FileOutputStream(strfile);
                {
                    int length = 0;
                    byte[] data = new byte[2048];
                    while (true)
                    {
                        length = s.read(data, 0, data.length);
                        if (length > 0)
                        {
                            fos.write(data, 0, length);
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                fileList.add(strfile);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            s.close();
        }
        if (deleteZipFile)
            file.delete();
        return fileList;
    }




    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile 生成的压缩文件
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.close();
    }

    /**
     * 批量压缩文件（夹）
     *
     * @param resFileList 要压缩的文件（夹）列表
     * @param zipFile 生成的压缩文件
     * @param comment 压缩文件的注释
     * @throws IOException 当压缩过程出错时抛出
     */
    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment)
            throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.setComment(comment);
        zipout.close();
    }



    /**
     * 获得压缩文件内文件列表
     *
     * @param zipFile 压缩文件
     * @return 压缩文件内文件名称
     * @throws ZipException 压缩文件格式有误时抛出
     * @throws IOException 当解压缩过程出错时抛出
     */
//    public static ArrayList<String> getEntriesNames(File zipFile) throws ZipException, IOException {
//        ArrayList<String> entryNames = new ArrayList<String>();
//        Enumeration<?> entries = getEntriesEnumeration(zipFile);
//        while (entries.hasMoreElements()) {
//            ZipEntry entry = ((ZipEntry)entries.nextElement());
//            entryNames.add(new String(getEntryName(entry).getBytes("GB2312"), "8859_1"));
//        }
//        return entryNames;
//    }



    /**
     * 取得压缩文件对象的注释
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的注释
     * @throws UnsupportedEncodingException
     */
    public static String getEntryComment(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getComment().getBytes("GB2312"), "8859_1");
    }

    /**
     * 取得压缩文件对象的名称
     *
     * @param entry 压缩文件对象
     * @return 压缩文件对象的名称
     * @throws UnsupportedEncodingException
     */
    public static String getEntryName(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getName().getBytes("GB2312"), "8859_1");
    }

    /**
     * 压缩文件
     *
     * @param resFile 需要压缩的文件（夹）
     * @param zipout 压缩的目的文件
     * @param rootpath 压缩的文件路径
     * @throws FileNotFoundException 找不到文件时抛出
     * @throws IOException 当压缩过程出错时抛出
     */
    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
            throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
                    BUFF_SIZE);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }
}
