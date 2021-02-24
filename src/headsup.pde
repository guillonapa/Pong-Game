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
    
  void draw() {
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
    text("\u00a9 " + name, 0.03*width, 0.97*height);
    text("Round: " + round, 0.7*width, 0.97*height);
  }
  
  //---------------------------------UPDATE--------------------------------->>> FINISHED
  
  /*
   *  This constantly updates the state of the game.
   *
   *  @params hasLost      true if the ball has been lost
   */
  void update(boolean hasLost) {
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
  
  void updateInMenu(boolean value) {
    inMenu = value; //to check if the menu screen should be active
  }  
  
  //---------------------------------UPDATE MORE TO COME--------------------------------->>> FINISHED
  
  void updateMoreToCome(boolean value) {
    moreToCome = value; //to check if the More to Come screen should be active
  }
  
  //---------------------------------GAME OVER--------------------------------->>> FINISHED
  
  boolean gameOver() {
    return (lives.equals("0")); //returns true if the round has been lost
  }  
  
  //---------------------------------RESET GAME--------------------------------->>> FINISHED
  
  /*
   *  This game resets the game after a ball has been lost
   *  
   *  @params newMode    true if the round has been lost
   */
  void resetGame(boolean newMode) {
    if (newMode) {
      round = 0;
    }  
    round += 1;
    bounces = 0;
    maxBounces = 0;
    lives = "3";
  } 
  
}