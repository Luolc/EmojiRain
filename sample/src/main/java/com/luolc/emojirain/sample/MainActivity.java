package com.luolc.emojirain.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.luolc.emojirain.EmojiRainLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * EmojiRain sample activity.
 *
 * @author LuoLiangchen
 * @since 2016/12/16
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.group_emoji_container) EmojiRainLayout mContainer;

    @BindView(R.id.et_per) EditText mPer;

    @BindView(R.id.et_duration) EditText mDuration;

    @BindView(R.id.et_drop_duration) EditText mDropDuration;

    @BindView(R.id.et_frequency) EditText mFrequency;

    @OnClick(R.id.btn_drop)
    void onClickDrop() {
        if (!checkInputParams(mPer, mDuration, mDropDuration, mFrequency)) {
            Toast.makeText(this, R.string.please_input_all_params, Toast.LENGTH_SHORT).show();
            return;
        }
        mContainer.stopDropping();
        int per = Integer.parseInt(mPer.getText().toString());
        int duration = Integer.parseInt(mDuration.getText().toString());
        int dropDuration = Integer.parseInt(mDropDuration.getText().toString());
        int frequency = Integer.parseInt(mFrequency.getText().toString());
        mContainer.setPer(per);
        mContainer.setDuration(duration);
        mContainer.setDropDuration(dropDuration);
        mContainer.setDropFrequency(frequency);

        mContainer.startDropping();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContainer.addEmoji(R.drawable.emoji_1_3);
        mContainer.addEmoji(R.drawable.emoji_2_3);
        mContainer.addEmoji(R.drawable.emoji_3_3);
        mContainer.addEmoji(R.drawable.emoji_4_3);
        mContainer.addEmoji(R.drawable.emoji_5_3);
    }

    private boolean checkInputParams(EditText... params) {
        return Stream.of(params).allMatch(param -> param.getText().length() != 0);
    }
}
