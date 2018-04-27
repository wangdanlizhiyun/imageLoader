# 微仿glide

## 效果图

<img src="aa.gif" width="200px"/>
<img src="bb.gif" width="200px"/>

## 加载整个手机的图片后内存还算稳定

<img src="neicun.png" width="2120px"/>

 1。自动支持gif
 
 2。自动绑定生命周期(1.页面关闭时未完成的任务自动取消 2.页面不可见时gif自动停止加载，可见时自动恢复)
 
 3。支持网络图 assets resource 本地file

 
 在application里初始化
    ```
        ImageCache.getInstance().init(this,getCacheDir().getAbsolutePath()+"/image");
                Image.placeHolder = new LoadingDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.loading_gray));
                Image.errorDrawable = new FailedDrawable(Color.RED);
    ```
  使用示例
  
  ```
    Image.with(that).load(TDSystemGallery.sList.get(position).getPath()).placeHolder(loadingDrawable)
                                    .errorDrawable(errorDrawable)
                                    .face()
                                    .size(Util.getScreenWidth(that) / 3, Util.getScreenWidth(that) / 3)
                                    .into(imageView);
                                    
                                    ImageView imageView1 = findViewById(R.id.iv1);
                                            Image.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1524719691407&di=6b60fa1d61c82f19fa3c2f511a98bc43&imgtype=0&src=http%3A%2F%2Fwww.21998.cn%2Fuploadfile%2Fnews%2Fimage%2F20161107%2F20161107171401_12213.gif").into(imageView1);
                                            ImageView imageView2 = findViewById(R.id.iv2);
                                            Image.with(this).loadAsserts("mp.gif").into(imageView2);
                                            ImageView imageView3 = findViewById(R.id.iv3);
                                            Image.with(this).errorDrawable(null).load(R.drawable.xiaosong).into(imageView3);
                                            ImageView imageView4 = findViewById(R.id.iv4);
                                            Image.with(this).errorDrawable(null).load(R.drawable.g).into(imageView4);
  ```
  
    
   