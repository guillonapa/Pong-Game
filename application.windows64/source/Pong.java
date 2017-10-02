import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Pong extends PApplet {

/*
 * Name:      Guillermo Narvaez-Paliza
 * eMail:     gnarvaez@brandeis.edu
 * Date:      April 9th, 2017
 * Purpose:   Starter Class: This class holds the behavior
 *            of the game. It continuously draws the next 
 *            frame, creating an animation.
 * Bugs:      None
 */





//STATIC VARIABLES
static final int PADDLE_SPEED = 30;

//BUTTONS
Button menu;
Button gameOverMenu;
Button normalMode;
Button randomPattern;
Button gravityMode;
Button moreToCome;

//SOUND FILES
SoundFile click;
SoundFile fart;
SoundFile init;
SoundFile moan;
SoundFile whine;

//FIELDS
boolean firstTime;
Ball ball;
Paddle paddle;
Headsup hup;

//---------------------------------SETUP--------------------------------->>> FINISHED

public void setup() {
   //the size of the screen
  //first we initialize the objects we will be using
  hup = new Headsup();
  paddle = new Paddle();
  ball = new Ball();
  firstTime = true;
  //and initialize the sounds we will be using for the game
  click = new SoundFile(this, "Click2-Sebastian-759472264.mp3");
  fart = new SoundFile(this, "Quick Fart-SoundBible.com-655578646.mp3");
  init = new SoundFile(this, "SMALL_CROWD_APPLAUSE-Yannick_Lemieux-1268806408.mp3");
  moan = new SoundFile(this, "Moaning Moan-SoundBible.com-2069423388.mp3");
  whine = new SoundFile(this, "Whine-SoundBible.com-1207627053.mp3");
  init.play(); //and play the welcome sound
}

//---------------------------------DRAW--------------------------------->>> FINISHED

public void draw() { 
  background(50);
  PFont write;
  write = loadFont("Krungthep-48.vlw");
  textFont(write); //set a new font
  if (hup.gameOver() && !hup.inMenu) {
    gameoverScreen(); //show the game over screen
  } else if (hup.inMenu && !hup.moreToCome) {
    menuScreen(); //show the menu screen
  } else if (hup.moreToCome) {
    moreToComeScreen(); //show the more to come screen
  } else {
    firstTime = true;
    ball.update(fart,moan); //update the ball
    drawElements(); //draw all elements of the game
  }    
}

//---------------------------------DRAW ELEMENTS--------------------------------->>> FINISHED

public void drawElements() {
  paddle.draw(); //draw paddle
  ball.draw(); //draw ball
  hup.draw(); //draw the game's current state
}  

//---------------------------------KEY PRESSED--------------------------------->>> FINISHED

public void keyPressed() {
  if (keyCode == RIGHT) {
    paddle.update(PADDLE_SPEED); //move the paddle to the right
  } else if (keyCode == LEFT) {
    paddle.update(-PADDLE_SPEED); //move the paddle to the left
  }  
}  
  
//---------------------------------MOUSE PRESSED--------------------------------->>> FINISHED
  
public void mousePressed() {
  if (gameOverMenu != null && gameOverMenu.MouseIsOver()) {
    clickPlayAgain(); //play again
  } else if (randomPattern != null && randomPattern.MouseIsOver()) {
    clickPlayRandomPattern(); //play the random pattern
  } else if (normalMode != null && normalMode.MouseIsOver()) {
    clickPlayNormalMode(); //play the normal mode
  } else if (gravityMode != null && gravityMode.MouseIsOver()) {
    clickPlayGravityMode(); //play the gravity mode
  } else if (moreToCome != null && moreToCome.MouseIsOver()) {
    clickShowMore(); //show the show more screen
  } else if (menu != null && menu.MouseIsOver()) {
    clickShowMenu(); //show the menu screen
  }  
}

//---------------------------------MAKE BUTTONS NULL--------------------------------->>> FINISHED

public void makeButtonsNull() { //this makes all buttons null (making it impossible to click buttons when the game is going on)
    gameOverMenu = null;
    menu = null;
    moreToCome = null;
    normalMode = null;
    randomPattern = null;
    gravityMode = null;
}

//---------------------------------GAME OVER SCREEN--------------------------------->>> FINISHED
  
public void gameoverScreen() {
  ball.clearSpace();
  textSize(48);
  stroke(255);
  text("GAME OVER", 0.15f*width, 6*height/16); //we print all the necessary info
  text("score: " + hup.maxBounces, 0.15f*width, 7.2f*height/16);
  gameOverMenu = new Button("PLAY AGAIN", 0.15f*width, 10*height/16, 200, 50);
  menu = new Button("MENU", 0.15f*width, height/2, 200, 50);
  menu.Draw(); //draw the menu button
  gameOverMenu.Draw(); //and draw the play again button
  if (firstTime) {
    whine.play();
    firstTime = false;
  }
  drawElements(); //and draw all the other elements
}  

//---------------------------------MENU SCREEN--------------------------------->>> FINISHED

public void menuScreen() {
  textSize(48);
  fill(255);
  text("MENU", 228, height/8);
  stroke(255);
  normalMode = new Button("REGULAR", 200, 2*height/8, 200, 50);
  randomPattern = new Button("PATTERN", 200, 3*height/8, 200, 50);
  gravityMode = new Button("GRAVITY", 200, 4*height/8, 200, 50);
  moreToCome = new Button("MORE...", 200, 5*height/8, 200, 50);
  normalMode.Draw();
  randomPattern.Draw();
  gravityMode.Draw();
  moreToCome.Draw();
  textSize(24);
  fill(255);
  text("\u00a9 " + hup.name, 0.03f*width, 0.97f*height);
} 

//---------------------------------MORE TO COME SCREEN--------------------------------->>> FINISHED

public void moreToComeScreen() {
  textSize(24);
  fill(255);
  text("If you liked this project\nor have some suggestions\nsend me an email at\nguillonapa@gmail.com", 0.15f*width, 0.4f*height);
  text("\u00a9 " + hup.name, 0.03f*width, 0.97f*height);
  menu = new Button("MENU", 0.15f*width, 0.7f*height, 200, 50);
  menu.Draw();
}  

//---------------------------------CLICK PLAY AGAIN--------------------------------->>> FINISHED

public void clickPlayAgain() {
  click.play();
  hup.resetGame(false);
  makeButtonsNull();
}  

//---------------------------------CLICK PLAY RANDOM PATTERM--------------------------------->>> FINISHED

public void clickPlayRandomPattern() {
  click.play();
  hup.updateInMenu(false);
  ball.setMode(GameMode.RANDOM);
  ball.setNormalVelocity(); //TODO set normal velocity
  ball.setInitialPositionNormal(); //TODO set normal initial position
  makeButtonsNull();
}  

//---------------------------------CLICK PLAY NORMAL MODE--------------------------------->>> FINISHED

public void clickPlayNormalMode() {
  click.play();
  hup.updateInMenu(false);
  ball.setMode(GameMode.REGULAR);
  ball.setNormalVelocity(); //TODO set normal velocity
  ball.setInitialPositionNormal(); //TODO set normal initial position
  makeButtonsNull();
}  

//---------------------------------CLICK PLAY GRAVITY MODE--------------------------------->>> FINISHED

public void clickPlayGravityMode() {
  click.play();
  hup.updateInMenu(false);
  ball.setGravityVelocity();
  ball.setXVelocity(2);
  ball.setInitialPosition(width/2, height/4);
  ball.setMode(GameMode.GRAVITY);
  makeButtonsNull();
}  

//---------------------------------CLICK SHOW MORE--------------------------------->>> FINISHED

public void clickShowMore() {
  click.play();
  hup.updateMoreToCome(true);
  hup.updateInMenu(false);
  makeButtonsNull();
}  

//---------------------------------CLICK SHOW MENU--------------------------------->>> FINISHED

public void clickShowMenu() {
  click.play();
  hup.updateMoreToCome(false);
  hup.updateInMenu(true);
  hup.resetGame(true);
  makeButtonsNull();
}  
/*
 * Name:      Guillermo Narvaez-Paliza
 * eMail:     gnarvaez@brandeis.edu
 * Date:      April 9th, 2017
 * Purpose:   Ball Class: This class contains the whole
 *            behavior of the ball. This behavior changes
 *            depending on the current game mode.
 * Bugs:      None
 */

class Ball {
  
  //STATIC VARIABLES
  
  final static int INIT_X = 430;
  final static int INIT_Y = 1;
  final static int INIT_X_VEL = 5;
  final static int INIT_Y_VEL = 5;
  
  final static int GRAV_X_SPEED = 2;
  
  final static int UPDATE_RATE = 10;
  final static int BALL_SIZE = 16;
  final static int REG_SPEED = 7;
  
  //GAME MODE
  
  GameMode mode;
  
  //FIELDS
  
  int gravSpeed;
  
  PVector position;
  PVector velocity;
  
  boolean firstTime;
  boolean bounced;
  boolean hitWall;

  int initLineX;
  int initLineY;
  int forAcc;
  
  int count;
  LinkedList<int[]> lines;
  
  //---------------------------------BALL CONSTRUCTOR--------------------------------->>> FINISHED
  
  Ball() {
    count = 0;
    forAcc = 4; //this is the acceleration of the ball (under gravitational conditions)
    gravSpeed = 12; //the initial impulse speed for a ball
    position = new PVector(INIT_X, INIT_Y); //initialize the position vector
    velocity = new PVector(5, -5); //initialize the velocity vector
    setRandomInitialVelocity(); //set the velocity components to random values
    firstTime = true; //to keep track of the moment when the ball becomes lost for the first time (first frame)
    bounced = true; //if a ball has just bounced
    hitWall = false; //if a ball has just hit a wall
    initLineX = INIT_X; //to keep track of the initial positions of every line (x-coordinate)
    initLineY = INIT_Y; //to keep track of the initial positions of every line (y-coordinate)
    lines = new LinkedList<int[]>(); //initialize a linked list to keep track of the lines that have been created
  }
  
  //---------------------------------DRAW--------------------------------->>> FINISHED
  
  public void draw() {
    fill(255); //set the color to white
    ellipse(position.x, position.y, BALL_SIZE, BALL_SIZE); //draw the ball
  }
  
  //---------------------------------UPDATE--------------------------------->>> FINISHED
  
  /*
   *  To compute where the ball is in the next frame
   */
  public void update(SoundFile bounceSound, SoundFile lostBall) {
    if (mode == GameMode.RANDOM) {
      printLines(); //we print the pattern created so far
    }  
    hitWall = enforceBorders(); //check if the ball is at a border
    lostBall(lostBall); //check if the ball has been lost
    bounced = paddleBlock(bounceSound); //check if the ball is being blocked by the paddle
    if (mode == GameMode.RANDOM) {
      if (hitWall || bounced) {
        updateLines(); //add the created line to the list
      } else { 
        drawPartialLine(); //we draw a partially completed line following the ball
      } 
    }  
    if (mode == GameMode.GRAVITY) {  //if we are playing Gravity mode
      count++; //update the count every frame
      count %= UPDATE_RATE;
      if (count == 0) { //we will update the speed every 'UPDATE_RATE' frames
        velocity.y = velocity.y + forAcc; //then we accelerate
      }
    }  
    position.set(position.x + velocity.x, position.y + velocity.y); //we update the ball's position
  }
  
  //---------------------------------ENFORCE BORDERS--------------------------------->>> FINISHED
  
  /*
   *  This method enforces the physical borders of the game
   */
  public boolean enforceBorders() {
    if (position.x >= width) { //if we hit the right edge
      velocity.x = -abs(velocity.x); //we change the velocity's x-component
      return true;
    } else if (position.x <= 0) { //if we hit the left edge
      velocity.x = abs(velocity.x); //we change the velocity's x-component
      return true;
    } else if (position.y >= height + BALL_SIZE) { //if we go out the bottom
      velocity.set(0,0); //we freeze the ball
      return false; //and return false since we didn't hit a wall
    } else if (position.y <= 0){ //if we hit the top
      velocity.y = abs(velocity.y); //we change the velocity's y-component
      return true;
    }
    return false; //return false if we did not collide
  }  
  
  //---------------------------------LOST BALL--------------------------------->>> FINISHED
  
  public void lostBall(SoundFile lostBall) {
    if (position.y >= height + BALL_SIZE/2 && firstTime) { //if the ball goes off the bottom for the first time
      paddle.resetPosition(); //we reset the position of the paddle
      velocity.set(0,0); //then we freeze the ball
      lostBall.play(); //we play the sound of loosing a ball
      firstTime = false; //and update the variable for the next frame
    } else if (position.y >= height + BALL_SIZE/2) { //for the second frame
      firstTime = true; //restore the variable
      position.set(INIT_X, INIT_Y); //set the ball to its initial position
      initLineX = INIT_X; //update the begining of the line to draw
      initLineY = INIT_Y;
      setRandomInitialVelocity(); //we set a new random velocity
      hup.update(true); //and update the state of the game (number of balls left)
      if (mode == GameMode.GRAVITY) {
        setGravityVelocity(); //set the starting velocity for gravity
        setXVelocity(GRAV_X_SPEED); //set the x-component of the velocity
        setInitialPosition(width/2, height/4); //set the initial position of the ball
      }  
      delay(1000); //give the user a second break before starting going to the menu
    } 
  }
  
  //---------------------------------PADDLE BLOCK--------------------------------->>> FINISHED
  
   public boolean paddleBlock(SoundFile bounceSound) {
    if (mode == GameMode.RANDOM) {
      if (shouldBounce()) {
        bounceRandom(bounceSound); //we change the velocity of the ball randomly
        return true;
      }
      return false;
    }  else if (mode == GameMode.GRAVITY) {
        if (shouldBounce()) {
          bounceGravity(bounceSound); //we change the velocity of the ball according to gravity
          return true;
        } 
        return false;
    }  else {
      if (shouldBounce()) {
        if (position.x + velocity.x <= (paddle.x + paddle.w/2)) {
          bounceLeft(bounceSound); //we reflect the ball to the left
          return true;
        } else {
          bounceRight(bounceSound); //we reflect the ball to the right
          return true;
        }
      }
      return false;
    }  
  }
  
  //---------------------------------SHOULD BOUNCE--------------------------------->>> FINISHED
  
  public boolean shouldBounce() {
    boolean isPreviousAbovePaddle = (position.y - velocity.y < paddle.y); //true if the position of the ball in the previous moment was above the paddle
    boolean isNextBelowPaddle = (position.y + velocity.y > paddle.y); //true if the position of the ball in the next frame will be below the paddle
    boolean isBallTrajectoryOnPaddle = (position.x + velocity.x) >= paddle.x && (position.x + velocity.x) <= paddle.x + paddle.w; //true if the ball will end hitting the padele
    return isPreviousAbovePaddle && isNextBelowPaddle && isBallTrajectoryOnPaddle; //return true if all these statements are true
  }  
  
  //---------------------------------CLEAR SPACE--------------------------------->>> FINISHED
  
  public void clearSpace() {
    lines.clear(); //clear the lines that have been drawn
  }
  
  //---------------------------------SET MODE--------------------------------->>> FINISHED
    
  public void setMode(GameMode mode) {
    this.mode = mode;
  }  
  
  //---------------------------------SET GRAVITY VELOCITY--------------------------------->>> FINISHED
  
  public void setGravityVelocity() {
    velocity.set(0,0);
  }
  
  //---------------------------------SET NORMAL VELOCITY--------------------------------->>> FINISHED
  
  public void setNormalVelocity() {
    setRandomInitialVelocity();
  }  
  
  //---------------------------------SET X VELOCITY--------------------------------->>> FINISHED
  
  public void setXVelocity(int xVel) {
    velocity.x = xVel;
  } 
  
  //---------------------------------SET INITIAL POSITION--------------------------------->>> FINISHED
  
  public void setInitialPosition(int x, int y) {
    position.set(x, y);
  }  
  
  //---------------------------------SET INITIAL POSITION NOMRAL--------------------------------->>> FINISHED
  
  public void setInitialPositionNormal() {
    position.set(INIT_X,INIT_Y);
  }  
  
  //---------------------------------SET RANDOM INITIAL POSITION--------------------------------->>> FINISHED
  
  public void setRandomInitialVelocity() {
    do {
      double angle = (Math.random()*(Math.PI/3)+(Math.PI/12)); //get a random angle (not close to horizontal and not close to vertical angles)
      velocity.y = -(float)(REG_SPEED*Math.sin(angle)); //use the sin to set the y-component
      if (Math.random() < 0.5f) { //decide randomly which side it will go to
        velocity.x = abs((float)(REG_SPEED*Math.cos(angle))); //and use the cos to set the x-component (positive)
      } else {
        velocity.x = -abs((float)(REG_SPEED*Math.cos(angle))); //or negative
      }
    } while (velocity.x == 0); //due to casting, sometimes we get a zero velocity in the x-component
  }
  
  //---------------------------------PRINT LINES--------------------------------->>> FINISHED
 
  public void printLines() {
    for (int[] i : lines) { //go over all the saved lines
      stroke((float)(Math.random()*255), (float)(Math.random()*255), (float)(Math.random()*255)); //select a random color
      line(i[0], i[1], i[2], i[3]); //and print the lines
    } 
  }  
  
  //---------------------------------UPDATE LINES--------------------------------->>> FINISHED
  
  public void updateLines() { //this adds a new line to the list
    int[] toAdd = {initLineX, initLineY, (int)position.x, (int)position.y}; //with the initial positions and the final positions
    lines.add(toAdd);
    initLineX = (int)position.x;
    initLineY = (int)position.y;
  }  
  
  //---------------------------------DRAW PARTIAL LINE--------------------------------->>> FINISHED
  
  public void drawPartialLine() {
    stroke((float)(Math.random()*255), (float)(Math.random()*255), (float)(Math.random()*255)); //create new 
    line(initLineX, initLineY, position.x, position.y);
  } 
  
  //---------------------------------BOUNCE RANDOM--------------------------------->>> FINISHED
  
  public void bounceRandom(SoundFile bounceSound) { //bounces randomly from the paddle
    float angle = (float)(Math.random()*(Math.PI/2)+(Math.PI/4));
    velocity.y = -(float)(REG_SPEED*Math.sin(angle));
    velocity.x = (float)(REG_SPEED*Math.cos(angle));
    bounceSound.play();
    hup.update(false);
  }  
  
  //---------------------------------BOUNCE GRAVITY--------------------------------->>> FINISHED
  
  public void bounceGravity(SoundFile bounceSound) { //bounces from the paddle with a new random verical velocity
    gravSpeed = (int)(Math.random()*8) + 11;
    velocity.y = -gravSpeed; //an upward velocity
    bounceSound.play();
    hup.update(false);
  }  
  
  //---------------------------------BOUNCE LEFT--------------------------------->>> FINISHED
  
  public void bounceLeft(SoundFile bounceSound) { //makes the ball bounce upwards and to the left
    velocity.y = -abs(velocity.y);
    velocity.x = -abs(velocity.x);
    bounceSound.play();
    hup.update(false);
  }  
  
  //---------------------------------BOUNCE RIGHT--------------------------------->>> FINISHED
  
  public void bounceRight(SoundFile bounceSound) { //makes the ball bounce upwards and to the right 
    velocity.y = -abs(velocity.y);
    velocity.x = abs(velocity.x);
    bounceSound.play();
    hup.update(false);
  }  
  
}
/*
 * Name:      Guillermo Narvaez-Paliza
 * eMail:     gnarvaez@brandeis.edu
 * Date:      April 9th, 2017
 * Purpose:   Button Class: This class contains the info
 *            for a button.
 * Bugs:      None
 */

class Button {
  String label;
  float x;    // top left corner x position
  float y;    // top left corner y position
  float w;    // width of button
  float h;    // height of button
  
  //---------------------------------BUTTON CONSTRUCTOR--------------------------------->>> FINISHED
  
  Button(String label, float x, float y, float w, float h) {
    this.label = label;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }
  
  //---------------------------------DRAW--------------------------------->>> FINISHED
  
  public void Draw() {
    fill(255); //set the color of the rectangle
    rect(x, y, w, h,100); //draw the rectangle
    fill(0); //set the color of the text
    textSize(24); //set the size of the text
    text(label, x + (w / 8), y + (h / 2) + 10); //put the text inside the button
  }
  
  //---------------------------------MOUSE IS OVER--------------------------------->>> FINISHED
  
  public boolean MouseIsOver() {
    //if the mouse is within the borders of the button, we return true, false otherwise
    if (mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + h)) {
      return true;
    }
    return false;
  }
}
/*
 * Name:      Guillermo Narvaez-Paliza
 * eMail:     gnarvaez@brandeis.edu
 * Date:      April 9th, 2017
 * Purpose:   GameMode Class: This class is an enum
 *            that helps us keep track of which game
 *            mode we are playing in.
 * Bugs:      None
 */

public enum GameMode { 
  REGULAR, RANDOM, GRAVITY
}
/*
 * Name:      Guillermo Narvaez-Paliza
 * eMail:     gnarvaez@brandeis.edu
 * Date:      April 9th, 2017
 * Purpose:   HeadsUp Class: This class holds in memory
 *            the current state of the game, and displays
 *            the information on the screen.
 * Bugs:      None
 */

class Headsup {
  int round;
  int bounces;
  int maxBounces;
  String lives;
  String name;
  boolean inMenu;
  boolean moreToCome;
  
  //---------------------------------HEADSUP CONSTRUCTOR--------------------------------->>> FINISHED
  
  Headsup() {
    inMenu = true; //to check if we are in the menu
    moreToCome = false; //to check if we are in the "More to come" screen
    round = 1; //we start in round 1
    bounces = 0; //with zero bounces
    maxBounces = 0; //and with no max score
    lives = "3"; //we start with three lives
    name = "Guillermo Narvaez"; //and this is my name (author of the code)
  }
  
  //---------------------------------DRAW--------------------------------->>> FINISHED
    
  public void draw() {
    //draw the game status as a "headsup" display on the gameboard
    PFont write;
    write = loadFont("Krungthep-48.vlw");
    textFont(write); //we load and set up a different font
    textSize(48);
    //and print all the necessary info on the screen
    text("Max Score: " + maxBounces, 20, 50);
    text("Bounces: " + bounces, 20, 100);
    text("Lives\n" + "      "+lives, width - 150, 50);
    textSize(24);
    text("\u00a9 " + name, 0.03f*width, 0.97f*height);
    text("Round: " + round, 0.7f*width, 0.97f*height);
  }
  
  //---------------------------------UPDATE--------------------------------->>> FINISHED
  
  /*
   *  This constantly updates the state of the game.
   *
   *  @params hasLost      true if the ball has been lost
   */
  public void update(boolean hasLost) {
    if (hasLost) { //first we check if the ball has been lost
      if (bounces > maxBounces) {
        maxBounces = bounces; //update the max score if necessary
      }  
      bounces = 0; //reset the current bounces and then update the number of lives
      if (lives.equals("3")) {
        lives = "2";
      } else if (lives.equals("2")) {
        lives = "1";
      } else {
        lives = "0";
      }  
    } else {  //else, if we have not lost a ball...
      if (!gameOver()) {
        bounces += 1; //we update the bounce count if the was a bounce from the paddle
      }  
    }
  }
  
  //---------------------------------UPDATE IN MENU--------------------------------->>> FINISHED
  
  public void updateInMenu(boolean value) {
    inMenu = value; //to check if the menu screen should be active
  }  
  
  //---------------------------------UPDATE MORE TO COME--------------------------------->>> FINISHED
  
  public void updateMoreToCome(boolean value) {
    moreToCome = value; //to check if the More to Come screen should be active
  }
  
  //---------------------------------GAME OVER--------------------------------->>> FINISHED
  
  public boolean gameOver() {
    return (lives.equals("0")); //returns true if the round has been lost
  }  
  
  //---------------------------------RESET GAME--------------------------------->>> FINISHED
  
  /*
   *  This game resets the game after a ball has been lost
   *  
   *  @params newMode    true if the round has been lost
   */
  public void resetGame(boolean newMode) {
    if (newMode) {
      round = 0;
    }  
    round += 1;
    bounces = 0;
    maxBounces = 0;
    lives = "3";
  } 
  
}
/*
 * Name:      Guillermo Narvaez-Paliza
 * eMail:     gnarvaez@brandeis.edu
 * Date:      April 9th, 2017
 * Purpose:   Paddle Class: This class contains
 *            the info for the paddle in the game.
 * Bugs:      None
 */

class Paddle {
  float x; //x position of top left corner
  float y; //y position of top left corner
  float w; //the width of the paddle
  float h; //the height of the paddle
  
  float initX; //holds the original value of the paddle
  
  //---------------------------------PADDLE CONSTRUCTOR--------------------------------->>> FINISHED
  
  Paddle() {
    x = width/2;
    y = height * .85f;
    w = width/9;
    x = x - w/2; //to place paddle at middle of screen
    h = 15;
    initX = x; //to save this initial value in memory
  }
  
  //---------------------------------DRAW--------------------------------->>> FINISHED
  
  public void draw() {
    fill(255);
    rect(x, y, w, h, 0.1f, 0.1f, 0.1f, 0.1f);
  }
  
  //---------------------------------UPDATE--------------------------------->>> FINISHED
  
  public void update(int displace) {
    //we displace when called for and when possible
    if (x > 0 && displace < 0) {
      x += displace;
    } else if (x < width - w && displace > 0) {
      x += displace;
    }  
  }
  
  //---------------------------------RESET POSITION--------------------------------->>> FINISHED
    
  public void resetPosition() {
    x = initX; //we simply reset the x-position
  }
  
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Pong" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
