#### [中文版文档](https://github.com/Luolc/EmojiRain/blob/master/README-cn.md)

# Emoji Rain

<img src='https://raw.githubusercontent.com/Luolc/EmojiRain/master/others/dropping-demo.gif' width="300px" style='border: #f1f1f1 solid 1px'/>

Hey, it's raining emoji!

This is a really simple and funny animation for Android. You could find similar animations when sending "Happy birthday" or something else special in WeChat app.

Now you are able to add this funny thing to your own app as well. Give a surprise to your users on Christmas Day by dropping emojis! :D

## Usage

#### Gradle dependency

```gradle
dependencies {
    compile 'com.luolc:emoji-rain:0.1.1'
}
```

#### Config

- per
    - How many emojis will dropping in each flow, default 6
- duration
    - The total duration of the animation, default 8000ms
- dropDuration
    - The average dropping duration for a specific emoji, default 2400ms
- dropFrequency
    - The interval between two flows, default 500ms

Config in layout. `EmojiRainLayout` inherits from `FrameLayout`. You can just use it as a native `FrameLayout` view.

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

Config in java code.

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

Start animation.
```java
mContainer.startDropping();
```

Stop animation.
```java
mContainer.stopDropping();
```

## Compatibility

Android midSdkVersion 14.

## License

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