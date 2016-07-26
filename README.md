###本人是一个网易云音乐的重度使用患者,也是一只安卓开发🐶，喜欢它切换歌曲时背景模糊切换，想自己实现下。最初感觉有点难以上手，还好我面向搜索引擎编程的能力尚可，借鉴了许多思路，最后的效果还算可以。

###主要用到的技术点:
* Glide获得网络图片
* RenderScript来模糊处理图片（代码里还有一个是Fastblur的算法在java层对canvas直接模糊处理的办法,但相关代码被我注释掉了,可能我心里还是更倾向使用RenderScript。感兴趣的可以把注释的代码打开跑一跑，效果几乎不差）
* TransitionDrawable来处理图片切换的淡入淡出效果

###已经上传了jCenter
```
compile 'com.j1adong.blurview:blurview:0.0.2'
```

###使用方法
```
//传入一张Bitmap即可,如果你想修改更多的参数,请自行修改源码或者等后续版本的更新
blurImageView.blur(bitmap);
```

###演示效果不是很清真,实机上还是不错的
![演示](imgs/demo.gif)