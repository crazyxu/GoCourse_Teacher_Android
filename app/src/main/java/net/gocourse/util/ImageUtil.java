package net.gocourse.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import net.gocourse.teacher.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * User: xucan
 * Date: 2015-07-22
 * Time: 13:30
 * FIXME 图片处理
 */
public class ImageUtil {
    /**
     * 从网络异步获取图片并设置ImageView
     * @param iv    ImageView
     * @param url   图片地址
     * @param queue 来着调用者的请求列队
     */
    public static void setIvByUrl(ImageView iv,String url,RequestQueue queue,final String localName){

        ImageLoader loader=new ImageLoader(queue, new BitmapCache(new BitmapCache.BitmapCacheBack() {
            @Override
            public void dealWithBitmap(Bitmap bitmap) {
                //保存到本地存储
                saveImgOnSd(bitmap,localName);
            }
        }));
        //ImageLoader监听，设置默认和加载失败图片
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(iv, R.drawable.default_user_head,R.drawable.default_user_head);
        loader.get(url,listener);
    }

    /**
     * 从本地存储获取图片，并设置到ImageView
     * @param iv ImageView
     * @param localName 图片的本地存储名称
     * @return 如果本地没有图片，返回false
     */
    public static boolean setIvByLocal(ImageView iv,String localName){
        String imgPath = Environment.getExternalStorageDirectory() + Constant.LOCAL_PATH_IMAGES;
        Bitmap bitmap=getImgFromSd(imgPath + localName);
        if (bitmap!=null){
            iv.setImageBitmap(bitmap);
            return true;
        }else {
            return false;
        }
    }

    /**
     *把bitmap写入sd
     * @param bm bitmap图片
     *@param imgName 图片名称
     *@return 保存是否成功
     */
    public static boolean  saveImgOnSd(Bitmap bm,String imgName) {
        //判断是否存在外部存储
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            //获取图片存储路径
            String imgPath = Environment.getExternalStorageDirectory() + Constant.LOCAL_PATH_IMAGES;
            File dirFile = new File(imgPath);
            try {
                if(!dirFile.exists()){
                    dirFile.mkdirs();
                }
                File file = new File( imgPath+imgName);
                if(!file.exists())
                    file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                //图片压缩（100表示没有压缩）
                bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    /**
     * 从sd卡获取图像
     * @param path
     * @return Bitmap
     */
    public static Bitmap getImgFromSd(String path){
        Bitmap bitmap=null;
        try{
            bitmap= BitmapFactory.decodeFile(path);
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 获取网络图片并设置ImageVIew,想保存到Local使用setIvByUrl
     * @param queue
     * @param url
     * @param defaultImg
     * @return
     */
    public static void imageLoader(ImageView iv,RequestQueue queue,String url,int defaultImg,int errorImg,int imgWidth,int imgHeight){
        ImageLoader.ImageListener listener=ImageLoader.getImageListener(iv,defaultImg,errorImg);
        ImageLoader loader=new ImageLoader(queue, new BitmapCache(null));
        loader.get(url,listener,imgWidth,imgHeight);
    }

    /**
     * 删除本地文件
     */
    public static void clear(){
        //获取图片存储路径
        String imgPath = Environment.getExternalStorageDirectory() + Constant.LOCAL_PATH_IMAGES;
        File dirFile = new File(imgPath);
        if (dirFile!=null)
            RecursionDeleteFile(dirFile);

    }

    /**
     * 使用递归删除文件夹及其内容
     * @param file
     */
    public static void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 检查是否存在SDCard
     * @return
     */
    public static boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }






}
