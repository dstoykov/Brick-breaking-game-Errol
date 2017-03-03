package units;

import game.Game;

import java.awt.*;

public class Ball {
    private float centerX;
    private float centerY;
    private int w;
    private int h;
    private int radius;

    private int speedX;
    private int speedY;
    private Platform platform;
    Brick[] bricks;
    Stone[] stones;

    public static boolean isSpacePressed;

    public Ball(int centerX, int centerY, int radius, int w, int h, int speedX, int speedY, Platform platform, Brick[] bricks, Stone[] stones) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.w = w;
        this.h = h;
        this.speedX = speedX;
        this.speedY = speedY;
        this.platform = platform;
        this.bricks = bricks;
        this.stones = stones;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public void move(Game game) {
        float ballMinX = radius;
        float ballMinY = radius;
        float ballMaxX = 800 - radius;
        float ballMaxY = 600 - radius;


// If Space is pressed-ball moves,
// otherwise stands still on platform
        if (isSpacePressed) {
            centerX += speedX;
            centerY += speedY;

            if (new Rectangle((int) getCenterX(), (int) getCenterY(), getW(), getH())
                    .intersects(new Rectangle(platform.getPlatformX(), platform.getPlatformY(), platform.getPlatformWidth(), platform.getPlatformHeight()))) {
                if (Game.State == Game.STATE.GAME) {
                    Game.playSound(this, "/sounds/ping_platform.wav");
                }
                this.setSpeedY(-this.getSpeedY());

                int segment = platform.getPlatformWidth() / 5;
                int first = platform.getPlatformX() + segment;
                int second = platform.getPlatformX() + segment * 2;
                int third = platform.getPlatformX() + segment * 3;
                int fourth = platform.getPlatformX() + segment * 4;
                int center = (int) (this.getCenterX() + this.getW() / 2);

                if (center < first) {
                    this.setSpeedX(-5);
                } else if (center >= first && center < second) {
                    this.setSpeedX(-5);
                } else if (center >= second && center < third) {
                    if (this.getSpeedX() > 0) {
                        this.setSpeedX(1);
                    } else {
                        this.setSpeedX(-1);
                    }
                } else if (center >= third && center < fourth) {
                    this.setSpeedX(5);
                } else if (center > fourth) {
                    this.setSpeedX(5);
                }
                // Reset ball's position out of collision.
                this.setCenterY(platform.getPlatformY() - this.getH());
            }

            // Draw the bricks
            if (bricks != null) {
                for (Brick brick : bricks) {
                    if (brick.destroyed) continue;
                    hitBrick(brick,game);
                }
            }
            // Draw the stones
            if (stones != null) {
                for (Stone stone : stones) {
                    if (stone != null) {
                        stone.hitCount++;
                        hitBrick(stone,game);
                    }
                }
            }


            if (centerX <= 0) {

                speedX = -speedX;
                centerX = ballMinX - 1;
                if (Game.State == Game.STATE.GAME) {
                    Game.playSound(this, "/sounds/ping_wall.wav");
                }

            } else if (centerX + speedX > ballMaxX - speedX - radius) {

                speedX = -speedX;
                if (Game.State == Game.STATE.GAME) {
                    Game.playSound(this, "/sounds/ping_wall.wav");
                }
            }
            if (centerY <= 0) {
                speedY = -speedY;
                {
                    Game.playSound(this, "/sounds/ping_wall.wav");
                }
            } else if (centerY > ballMaxY) {
                speedY = -speedY;
                centerY = ballMaxY;
                if (Game.State == Game.STATE.GAME) {
                    Game.playSound(this, "/sounds/ping_wall.wav");
                }
            }
        } else {
            centerX = platform.getPlatformX() + 45;
            centerY = platform.getPlatformY() - 20;
        }
    }

    private void hitBrick(Brick brick, Game game) {


        if (brick.getRect().intersects(new Rectangle((int) getCenterX(), (int) getCenterY(), getW(), getH()))) {

            if (Game.State == Game.STATE.GAME) {
                Game.playSound(this, "/sounds/ping_brick.wav");
            }
            int top = (int) this.getCenterY();
            int bottom = (int) (this.getCenterY() + this.getH());
            int left = (int) this.getCenterX();
            int right = (int) (this.getCenterX() + this.getW());


            int oldDy = this.getSpeedY();
            if (brick.getRect().contains(left, top - 1)) {
                int dy = this.getSpeedY();
                this.setSpeedY(dy < 0 ? -dy : dy);
            } else if (brick.getRect().contains(left, bottom + 1)) {
                int dy = this.getSpeedY();
                this.setSpeedY(dy < 0 ? dy : -dy);
            }

            if (brick.getRect().contains(right + 1, top) && oldDy == this.getSpeedY()) {
                int dx = this.getSpeedX();
                this.setSpeedX(dx < 0 ? dx : -dx);
            } else if (brick.getRect().contains(left - 1, top) && oldDy == this.getSpeedY()) {
                int dx = this.getSpeedX();
                this.setSpeedX(dx < 0 ? -dx : dx);
            }
            brick.hitBrick();
            if(brick.bonus!=null){
                game.addBonus(brick.getBonus());

           }
        }
    }

    public void render(Graphics g) {
        g.drawOval((int) this.centerX, (int) this.centerY, this.w, this.h);
    }

    public void sizeUp() {
        this.setH(40);
        this.setW(40);
        this.setRadius(20);

    }
}
