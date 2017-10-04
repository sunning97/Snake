package com.example.giangnguyen.snake;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.giangnguyen.snake.engine.GameEngine;
import com.example.giangnguyen.snake.enums.Direction;
import com.example.giangnguyen.snake.enums.GameState;
import com.example.giangnguyen.snake.views.SnakeView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GameEngine gameEngine;
    private SnakeView snakeView;
    private final Handler handler = new Handler();
    final long updateDelay = 200;

    private float prevX, prevY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameEngine = new GameEngine();
        gameEngine.initGame();

        snakeView = (SnakeView) findViewById(R.id.snakeView);
        snakeView.setSnakeViewMap(gameEngine.getMap());
        snakeView.setOnTouchListener(this);
        startUpdateHandler();


    }

    private void startUpdateHandler() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gameEngine.update();
                if (gameEngine.getCurrentGameState() == GameState.Running) {
                    handler.postDelayed(this, updateDelay);
                }

                if (gameEngine.getCurrentGameState() == GameState.Lost) {
                    onGameLost();
                }
                snakeView.setSnakeViewMap(gameEngine.getMap());
                snakeView.invalidate();
            }
        }, updateDelay);
    }

    private void onGameLost() {
        Toast.makeText(MainActivity.this, "Óc chó! Thua rồi - Điểm: " + GameEngine.score, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                prevX = motionEvent.getX();
                prevY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                float newX = motionEvent.getX();
                float newY = motionEvent.getY();

                if (Math.abs(newX - prevX) > Math.abs(newY - prevY)) {
                    if (newX > prevX) {
                        gameEngine.updateDirection(Direction.East);
                    } else {
                        gameEngine.updateDirection(Direction.West);
                    }
                } else {
                    if (newY > prevY) {
                        gameEngine.updateDirection(Direction.Sounth);
                    } else {
                        gameEngine.updateDirection(Direction.North);
                    }
                }

                break;
        }
        return true;
    }
}
