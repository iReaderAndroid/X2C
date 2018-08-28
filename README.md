# 背景

&ensp;一般大家在写页面时都是通过xml写布局，通过setContentView、或LayoutInflater.from(context).inflate方法将xml布局加载到内存中。

#### 优点
   *  可维护性好
   *  支持即时预览
   *  代码结构清晰

#### 缺点
   *  读取xml很耗时
   *  递归解析xml较耗时
   *  反射生成对象的耗时是new的3倍以上

&ensp;&ensp;&ensp;&ensp;我们团队在这个问题上也探索过很多解决方案，一度走到了另一个极端，完全废弃xml，所有控件通过java来new，甚至直接在canvas里绘制，这样虽然性能确实提升了，但是代码已经没有了一丁点可读性，可维护性。
&ensp;&ensp;&ensp;&ensp;我们后来反思代码到底是给机器看的，还是给人看的？？也许X2C已经给了我们一个答案

# X2C

&ensp;&ensp;&ensp;&ensp;为了即保留xml的优点，又解决它带来的性能问题，我们开发了X2C方案。即在编译生成APK期间，将需要翻译的layout翻译生成对应的java文件，这样对于开发人员来说写布局还是写原来的xml，但对于程序来说，运行时加载的是对应的java文件。
&ensp;&ensp;&ensp;&ensp;我们采用APT（Annotation Processor Tool）+ JavaPoet技术来完成编译期间【注解】->【解注解】->【翻译xml】->【生成java】整个流程的操作。

# 性能对比
在开发集成完之后我们做了简单的测试，性能对比如下

| 加载方式|次数|平均加载时间|
| ------ | ------ |  ------ |
|XML|100|30|
|X2C|100|11|

# 集成使用
#### 1.导入依赖
```java
annotationProcessor project(':apt')
implementation project(':lib')
```

#### 2.添加注解
```java
@Xml(layouts = {R.layout.activity_main})
```


#### 3.配置自定义属性(没有可不配)

在module下建立X2C_CONFIG.xml文件，里面配置定义属性和方法的映射关系,如果接收者是view，则写view.否则填params.

```mxl
<x2c-config>
    <attr name="app:mixColor" toFunc="view.setMixColor(int)" />
    <attr name="android:layout_marginTop" toFunc="params.topMargin=int" />
</x2c-config>
```

#### 4.通过X2C加载布局
```java
X2C.setContentView(this, R.layout.activity_main);
```
```java
X2C.inflate(this,R.layout.activity_main,null);
```


# 过程文件

#### 原始的xml

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

#### 生成的java文件
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
#### 生成的映射文件
```java
/**
 * WARN!!! dont edit this file
 *
 * autho chengwei
 * email chengwei@zhangyue.com
 */
public class X2C_2131296283 implements IViewCreator {
  @Override
  public View createView(Context context, int layoutId) {
    	View view;
        switch(layoutId){
        	case 2131296283:
        		view = new X2C_2131296283_Activity_Main().createView(context,2131296283);
        		break;
        	case 2131296284:
        		view = new X2C_2131296283_Head().createView(context,2131296284);
        		break;
        	default:
        		view = null;
        		break;
        }
        return view;
  }
}

```
# 不支持
*  merge标签 ,在编译期间无法确定xml的parent，所以无法支持
*  系统style,在编译期间只能查到应用的style列表，无法查询系统style，所以只支持应用内style

# 支持
* 各种系统控件、自定义控件
* include标签
* viewStub标签
* 应用style
* 自定义属性(感谢[Anzhi-Meiying](https://github.com/Anzhi-Meiying)提出的宝贵意见)
* 系统属性

| 属性名称|属性名称|
| ------ |------- |
|android:textSize| app:layout_constraintRight_toLeftOf|
|android:textColor| app:layout_constraintBottom_toTopOf|
|android:text| app:layout_constraintTop_toTopOf|
|android:background| app:layout_constrainedHeight|
|[查看全部](supportAll.md)|




## 有使用问题和其他技术问题，欢迎加群交流讨论

> QQ群：`558449447`，添加请注明来自`X2C`
>
> <a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=4464e9ee4fc8b05ee3c4eeb4f4be97469c1cfe46cded6b00f4a887ebebb60916"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="Android技术交流分享" title="Android技术交流分享"></a>
>
> 欢迎各位使用，该项目会持续维护。
>
>
> 另：欢迎加入[掌阅](http://www.zhangyue.com/jobs)大家庭，一起研究Android新技术。简历请发送`huangjian@zhangyue.com`,注明应聘方向。
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
