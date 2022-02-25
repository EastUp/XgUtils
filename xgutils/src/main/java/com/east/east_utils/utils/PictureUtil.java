//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.east.east_utils.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

public class PictureUtil {
    public PictureUtil() {
    }

    public static String bitmapToString(String filePath) {
        Bitmap bitmap = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, 0);
    }

    public static InputStream bitmapToInputStream(String filePath) {
        Bitmap bitmap = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 40, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static OutputStream bitmapToOutputStream(String filePath) {
        Bitmap bitmap = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 40, baos);
        return baos;
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) (height / reqHeight));
            int widthRatio = Math.round((float) (width / reqWidth));
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap getSmallBitmap(String filePath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

    }

    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static File getAlbumDir() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getAlbumName());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    public static String getAlbumName() {
        return "sheguantong";
    }

    public static int getExifOrientation(String filepath) {
        short degree = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        if (exif != null) {
            int orientation = exif.getAttributeInt("Orientation", -1);
            if (orientation != -1) {
                switch (orientation) {
                    case 3:
                        degree = 180;
                        break;
                    case 6:
                        degree = 90;
                        break;
                    case 8:
                        degree = 270;
                }
            }
        }

        return degree;
    }

    public static Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);

        try {
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError var5) {
            var5.printStackTrace();
        }

        if (returnBm == null) {
            returnBm = bitmap;
        }

        if (bitmap != returnBm) {
            bitmap.recycle();
        }

        return returnBm;
    }

    public static Bitmap getLoacalBitmap(String path) {
        Options opts = new Options();
        return BitmapFactory.decodeFile(path, opts);
    }

    public static void retateInitialize(File file) {
        int degree = getExifOrientation(file.getAbsolutePath());
        if (degree != 0) {
            Bitmap bitmap = rotateBitmap(degree, getLoacalBitmap(file.getAbsolutePath()));

            try {
                FileOutputStream e = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 100, e);
                e.flush();
                e.close();
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

    }



    /**
     * @param image   第一：质量压缩法：
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( /*baos.toByteArray().length / 1024>50*/baos.toByteArray().length / 1024>100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 50;//每次都减少10
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }
//        image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * @param srcPath  图片按比例大小压缩方法（根据路径获取图片并压缩）
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        Options newOpts = new Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
    /**
     * @param image   图片按比例大小压缩方法（根据Bitmap图片压缩）：
     * @return
     */
    public static Bitmap comp(Bitmap image, float hh , float ww) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Options newOpts = new Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//        float hh = 120f;//这里设置高度为800f
//        float ww = 160f;//这里设置宽度为480f
//        float hh = 60f;//这里设置高度为800f
//        float ww = 80f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        saveImage(bitmap,"before");

        /* 设置图片放大的比例 */
//        double scale=1/(double)be;
//        /* 计算这次要放大的比例 */
////        scaleWidth=(float)(scaleWidth*scale);
////        scaleHeight=(float)(scaleHeight*scale);
//        /* 产生reSize后的Bitmap对象 */
//        Matrix matrix = new Matrix();
//        matrix.postScale(be,be);
////        matrix.postScale(w, h);
//        Bitmap bitmap1 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
//                bitmap.getHeight(),matrix,true);
//        bitmap.recycle();
//        saveImage(bitmap1,"after");
        if(bitmap!=null){
            return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        }else{
            return null;
        }
//        return compressImageWithP(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public  static String saveCroppedImage(Bitmap bmp) {//压缩图片后获取路径
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();

        file = new File(("/sdcard/" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis()) + "temp.jpg").trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        // /sdcard/myFolder/temp_cropped.jpg
        String newFilePath = "/sdcard/myFolder" + "/" + mName + "_cropped" + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFilePath;
    }

    public static String saveImage(Bitmap bmp, String name){
        File file = new File("/sdcard/myFolder");
        if (!file.exists())
            file.mkdir();

        file = new File(("/sdcard/" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(System.currentTimeMillis()) + "temp.jpg").trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        // /sdcard/myFolder/temp_cropped.jpg
//        String newFilePath = "/sdcard/myFolder" + "/" + mName + "_cropped" + sName;
        String newFilePath = "/sdcard/myFolder" + "/" + name + "_cropped" + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFilePath;
    }


    public static Bitmap getSmallBitmap(Bitmap bm) {

        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bm.compress(CompressFormat.JPEG, 50, baos);
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }



    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static void deleteChildFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
//            file.delete();
        }
    }

}
