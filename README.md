# 背景

&ensp;一般大家在写页面时都是通过xml写布局，通过setContentView、或LayoutInflater.from(context).inflate
方法将xml布局加载到内存中。
   
优点
   *  可维护性好
   *  支持即时预览
   *  代码结构清晰
    
缺点
   *  读取xml很耗时
   *  递归解析xml较耗时
   *  反射生成对象的耗时是new的3倍以上
       
# X2C
    
为了即保留xml的优点，又解决它带来的性能问题，我们提出了X2C方案。即在编译生成APK期间，将需要翻译的
layout翻译生成对应的java文件，这样对于开发人员来说写布局还是写原来的xml，但对于程序来说，运行时加载的
是对应的java文件。
我们采用APT（Annotation Processor Tool）+ JavaPoet技术来完成编译期间【注解】->【解注解】->【翻译xml】->【生成java】整个流程的操作。

# 集成使用
####1.导入依赖
```java
annotationProcessor project(':com.zhangyue.we.x2c:apt:1.2.5')
compile project(':com.zhangyue.we.x2c:lib:1.2.5')
```
   
####2.添加注解
```java
@Xml(layouts = {R.layout.activity_main})
```

####3.通过X2C加载布局
```java
X2C.instance().setContentView(this, R.layout.activity_main);
```
```java
X2C.instance().inflate(this,R.layout.activity_main,null);
```


#过程文件

#### 原始的xml

  ```xml
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:paddingLeft="10dp">
    
        <ImageView
            android:id="@+id/ccc"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@drawable/broadcast" />
    
    </LinearLayout>
```

####生成的java文件
```java
   public class X2C_A7f_Activity_Main implements IViewCreator {
       @Override
       public View createView(Context ctx, int layoutId) {
            Resources res = ctx.getResources();
     
             LinearLayout linearLayout0 = new LinearLayout(ctx);
             linearLayout0.setOrientation(LinearLayout.VERTICAL);
             linearLayout0.setPadding((int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,res.getDisplayMetrics())),0,0,0);
     
             ImageView imageView1 = new ImageView(ctx);
             LinearLayout.LayoutParams layoutParam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
             imageView1.setLayoutParams(layoutParam1);
             linearLayout0.addView(imageView1);
             imageView1.setId(R.id.ccc);
             imageView1.setBackgroundResource(R.drawable.broadcast);
     
             return linearLayout0;
       }
     }
```
####生成的映射文件
```java
public class X2C_A7f implements IViewCreator {
  @Override
  public View createView(Context context, int layoutId) {
    	View view;
        switch(layoutId){
        	case 2131296283:
        		view = new X2C_A7f_Activity_Main().createView(context,2131296283);
        		break;
        	default:
        		view = null;
        		break;
        }
        return view;
  }
} 
```
#不支持的
*  merge ,在编译期间无法确定xml的parent，所以无法支持
*  系统style,在编译期间只能查到应用的style列表，无法查询系统style，所以只支持应用内style

#支持的属性

| 属性名称| 
| ------ | 
| android:textSize |
|android:textColor|
|android:text|
|android:background|
|android:layout_marginLeft|
|android:layout_marginTop|
|android:layout_marginRight|
|android:layout_marginBottom|
|android:paddingLeft|
|android:paddingTop|
|android:paddingRight|
|android:paddingBottom|
|android:padding|
|android:gravity|
|android:orientation|
|android:id|
|android:layout_centerInParent|
|android:layout_centerVertical|
|android:layout_centerHorizontal|
|android:layout_alignParentLeft|
|android:layout_alignParentTop|
|android:layout_alignParentRight|
|android:layout_alignParentBottom|
|android:layout_above|
|android:layout_below|
|android:layout_toLeftOf|
|android:layout_toRightOf|
|android:layout_alignLeft|
|android:layout_alignTop|
|android:layout_alignRight|
|android:layout_alignBottom|
|android:scaleType|
|android:src|
|android:visibility|
|android:clipToPadding|
|android:ellipsize|
|android:lineSpacingExtra|
|android:maxLines|