/*
 * Name:      Guillermo Narvaez-Paliza
 * eMail:     gnarvaez@brandeis.edu
 * Date:      April 9th, 2017
 * Purpose:   Starter Class: This class holds the behavior
 *            of the game. It continuously draws the next 
 *            frame, creating an animation.
 * Bugs:      None
 */

import processing.sound.*;

import java.util.*;

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

void setup() {
  size(600, 600); //the size of the screen
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

void draw() { 
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

void drawElements() {
  paddle.draw(); //draw paddle
  ball.draw(); //draw ball
  hup.draw(); //draw the game's current state
}  

//---------------------------------KEY PRESSED--------------------------------->>> FINISHED

void keyPressed() {
  if (keyCode == RIGHT) {
    paddle.update(PADDLE_SPEED); //move the paddle to the right
  } else if (keyCode == LEFT) {
    paddle.update(-PADDLE_SPEED); //move the paddle to the left
  }  
}  
  
//---------------------------------MOUSE PRESSED--------------------------------->>> FINISHED
  
void mousePressed() {
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

void makeButtonsNull() { //this makes all buttons null (making it impossible to click buttons when the game is going on)
    gameOverMenu = null;
    menu = null;
    moreToCome = null;
    normalMode = null;
    randomPattern = null;
    gravityMode = null;
}

//---------------------------------GAME OVER SCREEN--------------------------------->>> FINISHED
  
void gameoverScreen() {
  ball.clearSpace();
  textSize(48);
  stroke(255);
  text("GAME OVER", 0.15*width, 6*height/16); //we print all the necessary info
  text("score: " + hup.maxBounces, 0.15*width, 7.2*height/16);
  gameOverMenu = new Button("PLAY AGAIN", 0.15*width, 10*height/16, 200, 50);
  menu = new Button("MENU", 0.15*width, height/2, 200, 50);
  menu.Draw(); //draw the menu button
  gameOverMenu.Draw(); //and draw the play again button
  if (firstTime) {
    whine.play();
    firstTime = false;
  }
  drawElements(); //and draw all the other elements
}  

//---------------------------------MENU SCREEN--------------------------------->>> FINISHED

void menuScreen() {
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
  text("\u00a9 " + hup.name, 0.03*width, 0.97*height);
} 

//---------------------------------MORE TO COME SCREEN--------------------------------->>> FINISHED

void moreToComeScreen() {
  textSize(24);
  fill(255);
  text("If you liked this project\nor have some suggestions\nsend me an email at\nguillonapa@gmail.com", 0.15*width, 0.4*height);
  text("\u00a9 " + hup.name, 0.03*width, 0.97*height);
  menu = new Button("MENU", 0.15*width, 0.7*height, 200, 50);
  menu.Draw();
}  

//---------------------------------CLICK PLAY AGAIN--------------------------------->>> FINISHED

void clickPlayAgain() {
  click.play();
  hup.resetGame(false);
  makeButtonsNull();
}  

//---------------------------------CLICK PLAY RANDOM PATTERM--------------------------------->>> FINISHED

void clickPlayRandomPattern() {
  click.play();
  hup.updateInMenu(false);
  ball.setMode(GameMode.RANDOM);
  ball.setNormalVelocity(); //TODO set normal velocity
  ball.setInitialPositionNormal(); //TODO set normal initial position
  makeButtonsNull();
}  

//---------------------------------CLICK PLAY NORMAL MODE--------------------------------->>> FINISHED

void clickPlayNormalMode() {
  click.play();
  hup.updateInMenu(false);
  ball.setMode(GameMode.REGULAR);
  ball.setNormalVelocity(); //TODO set normal velocity
  ball.setInitialPositionNormal(); //TODO set normal initial position
  makeButtonsNull();
}  

//---------------------------------CLICK PLAY GRAVITY MODE--------------------------------->>> FINISHED

void clickPlayGravityMode() {
  click.play();
  hup.updateInMenu(false);
  ball.setGravityVelocity();
  ball.setXVelocity(2);
  ball.setInitialPosition(width/2, height/4);
  ball.setMode(GameMode.GRAVITY);
  makeButtonsNull();
}  

//---------------------------------CLICK SHOW MORE--------------------------------->>> FINISHED

void clickShowMore() {
  click.play();
  hup.updateMoreToCome(true);
  hup.updateInMenu(false);
  makeButtonsNull();
}  

//---------------------------------CLICK SHOW MENU--------------------------------->>> FINISHED

void clickShowMenu() {
  click.play();
  hup.updateMoreToCome(false);
  hup.updateInMenu(true);
  hup.resetGame(true);
  makeButtonsNull();
}  