package com.kaplanteam.cathy.dangerparty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cole on 3/22/18.
 */

public class EndGameActivity extends AppCompatActivity {

    private TextView scoreText, highScoreText, prizeDesc;
    private SharedPreferences counter, highScore;
    private SharedPreferences.Editor editor;
    private ImageView prizeImg;
    private Button playAgain;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);
        Intent i = getIntent();

        prizeDesc = (TextView) findViewById(R.id.textView_prize);
        prizeImg = (ImageView) findViewById(R.id.imageView_prizePic);

        ArrayList<FruitPrize> fruit = new ArrayList<>();
        fruit.add(new FruitPrize("dragon fruit for your ferocity", R.drawable.dragonfruit));
        fruit.add(new FruitPrize("papaya for your indigestion", R.drawable.papaya));
        fruit.add(new FruitPrize("pomelo for your juiciness", R.drawable.pomelo));
        fruit.add(new FruitPrize("mango for your colorful language", R.drawable.mango));
        fruit.add(new FruitPrize("rambutan for your prickly skin", R.drawable.rambutan));
        fruit.add(new FruitPrize("pineapple for your lack of vitamin C",
                R.drawable.pineapple));
        fruit.add(new FruitPrize("kiwi for your bird-like qualities", R.drawable.kiwi));
        fruit.add(new FruitPrize("coconut for your meaty guts", R.drawable.coconut));
        fruit.add(new FruitPrize("star fruit for your awesomeness", R.drawable.starfruit));
        fruit.add(new FruitPrize(" passion fruit for your passion", R.drawable.passionfruit));

        int rand = (int) (Math.random() * fruit.size());

        prizeDesc.setText("You are awarded a " + fruit.get(rand).getDesc());
        prizeImg.setImageResource(fruit.get(rand).getFruitPic());

        counter = this.getSharedPreferences("HELLO", MODE_PRIVATE);
        highScore = this.getSharedPreferences("SCORE", MODE_PRIVATE);
        editor = highScore.edit();


        playAgain = (Button) findViewById(R.id.button_play_again);
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EndGameActivity.this, GameActivity.class);
                startActivity(i);
            }
        });

        scoreText = (TextView) findViewById(R.id.textview_score);
        highScoreText = (TextView) findViewById(R.id.textView_highScore);


        scoreText.setText("Score: " + counter.getInt("score", -1));

        if(counter.getInt("score", -1) > highScore.getInt("highScore", -1))
        {
            editor.putInt("highScore", counter.getInt("score", -1));
            editor.commit();
            highScoreText.setText("High Score: " + highScore.getInt("highScore", -1));
        }
        else {
            highScoreText.setText("High Score: " + highScore.getInt("highScore", -1));
        }

        //fruits for game
        //mango
        //guava
        //pineapple
        //coconut
        //pomelo
        //passion fruit
        //dragonfruit
        //star fruit
        //kiwi



    }
}