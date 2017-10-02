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
  
  void Draw() {
    fill(255); //set the color of the rectangle
    rect(x, y, w, h,100); //draw the rectangle
    fill(0); //set the color of the text
    textSize(24); //set the size of the text
    text(label, x + (w / 8), y + (h / 2) + 10); //put the text inside the button
  }
  
  //---------------------------------MOUSE IS OVER--------------------------------->>> FINISHED
  
  boolean MouseIsOver() {
    //if the mouse is within the borders of the button, we return true, false otherwise
    if (mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + h)) {
      return true;
    }
    return false;
  }
}