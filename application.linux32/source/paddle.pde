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
    y = height * .85;
    w = width/9;
    x = x - w/2; //to place paddle at middle of screen
    h = 15;
    initX = x; //to save this initial value in memory
  }
  
  //---------------------------------DRAW--------------------------------->>> FINISHED
  
  void draw() {
    fill(255);
    rect(x, y, w, h, 0.1, 0.1, 0.1, 0.1);
  }
  
  //---------------------------------UPDATE--------------------------------->>> FINISHED
  
  void update(int displace) {
    //we displace when called for and when possible
    if (x > 0 && displace < 0) {
      x += displace;
    } else if (x < width - w && displace > 0) {
      x += displace;
    }  
  }
  
  //---------------------------------RESET POSITION--------------------------------->>> FINISHED
    
  void resetPosition() {
    x = initX; //we simply reset the x-position
  }
  
}