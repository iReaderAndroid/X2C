# Background
[中文](README_CN.md)

Generally, when writing a page, everyone writes the layout through xml, and loads the xml layout into memory through set contentView or LayoutInflater.from(context).inflate method.
#### advantage
   *  Good maintainability
   *  Support instant preview
   *  Clear code structure

#### Disadvantage
   *  Reading xml is time consuming
   *  Recursive parsing xml is time consuming
   *  The time it takes to generate an object is more than three times that of new.

Our team has also explored many solutions on this issue. Once it reached the other extreme, completely abandoning xml, all controls are new through java, even directly in the canvas, so although the performance is indeed improved, but the code is gone. A little bit of readability and maintainability.
We later reflected on the code to see the machine, or to show it? ? Maybe X2C has given us an answer.

# X2C

In order to preserve the advantages of xml and solve the performance problems it brings, we developed the X2C solution. That is, during the compilation and generation of the APK, the translation of the layout that needs to be translated generates the corresponding java file, so that the developer writes the layout or writes the original xml, but for the program, the runtime loads the corresponding java file.
We use APT (Annotation Processor Tool) + JavaPoet technology to complete the operation of the whole process during the compilation [Annotation] -> [Resolve] -> [Translate xml] -> [Generate java].

# Performance comparison
After the development integration, we did a simple test, the performance comparison is as follows

| Loading method|frequency|Average load time|
| ------ | ------ |  ------ |
|XML|100|30|
|X2C|100|11|

# Integrated use
#### 1.Add dependency
Add dependencies in the build.gradle file of the module
```java
annotationProcessor 'com.zhangyue.we:x2c-apt:1.1.2'
implementation 'com.zhangyue.we:x2c-lib:1.0.6'
```

#### 2.Add annotation
Add annotations to any java class or method that uses the layout.
```java
@Xml(layouts = "activity_main")
```


#### 3.Configure custom properties (no or no)

Create an X2C_CONFIG.xml file under the module, which defines the mapping between attributes and methods. If the receiver is a view, write the view. Otherwise fill in params.

```mxl
<x2c-config>
    <attr name="app:mixColor" toFunc="view.setMixColor(int)" />
    <attr name="android:layout_marginTop" toFunc="params.topMargin=int" />
</x2c-config>
```

#### 4.Load layout through X2C
Replace where you originally used set content view or inflate, as follows:
```java
this.setContentView(R.layout.activity_main); --> X2C.setContentView(this, R.layout.activity_main);
```
```java
LayoutInflater.from(this).inflate(R.layout.activity_main,null); --> X2C.inflate(this,R.layout.activity_main,null);
```


# Process file

#### Original xml

  ```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingLeft="10dp">

    <include
        android:id="@+id/head"
        layout="@layout/head"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerHorizontal="true" />

    <ImageView
        android:id="@+id/ccc"
        style="@style/bb"
        android:layout_below="@id/head" />
</RelativeLayout>
```

#### Generated java file
```java

/**
 * WARN!!! dont edit this file
 * translate from {@link  com.zhangyue.we.x2c.demo.R.layout.activity_main}
 * autho chengwei
 * email chengwei@zhangyue.com
 */
public class X2C_2131296281_Activity_Main implements IViewCreator {
  @Override
  public View createView(Context ctx, int layoutId) {
    	Resources res = ctx.getResources();

        RelativeLayout relativeLayout0 = new RelativeLayout(ctx);
        relativeLayout0.setPadding((int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,res.getDisplayMetrics())),0,0,0);

        View view1 =(View) new X2C_2131296283_Head().createView(ctx,0);
        RelativeLayout.LayoutParams layoutParam1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view1.setLayoutParams(layoutParam1);
        relativeLayout0.addView(view1);
        view1.setId(R.id.head);
        layoutParam1.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);

        ImageView imageView2 = new ImageView(ctx);
        RelativeLayout.LayoutParams layoutParam2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,res.getDisplayMetrics())));
        imageView2.setLayoutParams(layoutParam2);
        relativeLayout0.addView(imageView2);
        imageView2.setId(R.id.ccc);
        layoutParam2.addRule(RelativeLayout.BELOW,R.id.head);

        return relativeLayout0;
  }
}
```
#### Generated map file
```java

/**
 * WARN!!! don't edit this file
 *
 * author chengwei
 * email chengwei@zhangyue.com
 */
public class X2C127_activity implements IViewCreator {
  @Override
  public View createView(Context context) {
        View view = null ;
        int sdk = Build.VERSION.SDK_INT;
        int orientation = context.getResources().getConfiguration().orientation;
        boolean isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (isLandscape) {
        	view = new com.zhangyue.we.x2c.layouts.land.X2C127_Activity().createView(context);
        } else if (sdk >= 27) {
        	view = new com.zhangyue.we.x2c.layouts.v27.X2C127_Activity().createView(context);
        } else if (sdk >= 21) {
        	view = new com.zhangyue.we.x2c.layouts.v21.X2C127_Activity().createView(context);
        } else {
        	view = new com.zhangyue.we.x2c.layouts.X2C127_Activity().createView(context);
        }
        return view;
  }
}

```
# not support
*  The merge tag cannot determine the parent of the xml during compilation, so it cannot be supported.
*  System style, only the style list of the application can be found during compilation, and the system style cannot be queried, so only the in-app style is supported.

# support
* ButterKnifer
* DataBinding
* android widget
* customView
* include tag
* viewStub tag
* fragment label
* Application style
* Custom attribute
* System attribute

| Attribute name|Attribute name|
| ------ |------- |
|android:textSize| app:layout_constraintRight_toLeftOf|
|android:textColor| app:layout_constraintBottom_toTopOf|
|android:text| app:layout_constraintTop_toTopOf|
|android:background| app:layout_constrainedHeight|
|[view all](supportAll.md)|



## There are usage problems and other technical issues, welcome to exchange discussion

> QQ群：`870956525`，Add please specify from`X2C`
>
> <a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=4464e9ee4fc8b05ee3c4eeb4f4be97469c1cfe46cded6b00f4a887ebebb60916"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="掌阅X2C交流群" title="掌阅X2C交流群"></a>
>
> Welcome everyone to use, the project will continue to maintain
>
>
> Another: welcome to join[IReader](http://www.zhangyue.com/jobs) family, study new Android technologies together. Please send your resume`huangjian@zhangyue.com`,Indicate the direction of employment。
>
# LICENSE

```
MIT LICENSE 
Copyright (c) 2018 zhangyue

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
