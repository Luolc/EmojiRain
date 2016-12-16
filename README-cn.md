# Emoji Rain

<img src='https://raw.githubusercontent.com/Luolc/EmojiRain/master/ohters/dropping-demo.gif' width="300px" style='border: #f1f1f1 solid 1px'/>

掉表情包咯~

这是一个特别小巧的Android掉emoji表情包实现。效果类似于微信中发送"生日快乐"和"么么哒"之类的词语时触发的动画效果。

现在你也可以把这个有趣的特性加入自己的app啦~在圣诞节的时候给用户掉圣诞树emoji吧~ :D

## 使用

#### Gradle依赖

```gradle
dependencies {
    compile 'com.luolc:emoji-rain:0.1.1'
}
```

#### 参数配置

- per
    - 每一波掉落的emoji个数，默认6个
- duration
    - 掉落动画持续的总时长，默认8000ms
- dropDuration
    - 每个emoji掉落时长的平均值，默认2400ms
- dropFrequency
    - 掉落频率，即每两拨的时间间隔，默认500ms

在layout中配置。 `EmojiRainLayout`继承自`FrameLayout`，你完全可以把它当做原生的`FrameLayout`使用。

```xml
<com.luolc.emojirain.EmojiRainLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:tools="http://schemas.android.com/tools"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/group_emoji_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:per="10"
        app:duration="7200"
        app:dropDuration="2400"
        app:dropFrequency="500"
        tools:context="com.luolc.emojirain.sample.MainActivity">

    <TextView
            android:text="Hello world!"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
</com.luolc.emojirain.EmojiRainLayout>
```

在Java代码中配置。

```java
public class MainActivity extends AppCompatActivity {

    private EmojiRainLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind view
        mContainer = (EmojiRainLayout) findViewById(R.id.group_emoji_container);

        // add emoji sources
        mContainer.addEmoji(R.drawable.emoji_1_3);
        mContainer.addEmoji(R.drawable.emoji_2_3);
        mContainer.addEmoji(R.drawable.emoji_3_3);
        mContainer.addEmoji(R.drawable.emoji_4_3);
        mContainer.addEmoji(R.drawable.emoji_5_3);

        // set emojis per flow, default 6
        mContainer.setPer(10);

        // set total duration in milliseconds, default 8000
        mContainer.setDuration(7200);

        // set average drop duration in milliseconds, default 2400
        mContainer.setDropDuration(2400);

        // set drop frequency in milliseconds, default 500
        mContainer.setDropFrequency(500);
    }
}
```

开始掉落。
```java
mContainer.startDropping();
```

停止掉落。
```java
mContainer.stopDropping();
```

## 兼容性

Android midSdkVersion 14.

## 许可证

    Copyright 2016, Liangchen Luo.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.