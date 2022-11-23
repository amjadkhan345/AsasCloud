package com.asas.cloud.classes;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;

public class Uttilties {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";


    public static long getimagesize (Context context, Uri uri){
        Bitmap bitmap = null;
        //long bitmap_size = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap = BitmapFactory.decodeFile(uri1.toString());
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long bitmap_size = imageInByte.length / 1024;



        return bitmap_size;
    }



    public static  Long getvideosize(Context context, Uri uri){
        /*String[] mediaColumns = {MediaStore.Video.Media.SIZE};
        Cursor cursor = context.getContentResolver().query(uri, mediaColumns, null, null, null);
        cursor.moveToFirst();
        int sizeColInd = cursor.getColumnIndex(mediaColumns[0]);
        long fileSize = cursor.getLong(sizeColInd);
        cursor.close();*/
        File file= new File(getPath(context, uri));
        long file1 = file.length();
        long fileSize = file1 / 1024;
        return fileSize;
    }


    public static String FileSize(long size) {
        String hrSize = null;

        //double b = size;
        double k = size;
        double m = size/1000.0;
        double g = (size/1000.0)/1000.0;
        double t = (((size/1000.0)/1000.0)/1000.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat("TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat("GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat("MB");
        } else if ( k>1 ) {
            hrSize = (int) k + "KB";//dec.format(k).concat(" KB");
        } else {
            hrSize = "0KB";//dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }
    public static String getPath(Context context, Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }





    public static String createRandomCode(int codeLength){
        char[] chars = "1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        System.out.println(output);
        return output ;
    }

    public static void downloadManager(Context context, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("download");
        request.setTitle(""+getRandomString(6)+".mp4");
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+getRandomString(6)+".mp4");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }
    public static void downloadImageManager(Context context, String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("download");
        request.setTitle(""+getRandomString(6)+".jpg");
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, ""+getRandomString(6)+".jpg");

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    public static String getfiletype(Context context, Uri uri){
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;

    }

}
