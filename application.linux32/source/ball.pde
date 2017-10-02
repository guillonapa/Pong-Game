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
  
  void draw() {
    fill(255); //set the color to white
    ellipse(position.x, position.y, BALL_SIZE, BALL_SIZE); //draw the ball
  }
  
  //---------------------------------UPDATE--------------------------------->>> FINISHED
  
  /*
   *  To compute where the ball is in the next frame
   */
  void update(SoundFile bounceSound, SoundFile lostBall) {
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
  boolean enforceBorders() {
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
  
  void lostBall(SoundFile lostBall) {
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
  
   boolean paddleBlock(SoundFile bounceSound) {
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
  
  boolean shouldBounce() {
    boolean isPreviousAbovePaddle = (position.y - velocity.y < paddle.y); //true if the position of the ball in the previous moment was above the paddle
    boolean isNextBelowPaddle = (position.y + velocity.y > paddle.y); //true if the position of the ball in the next frame will be below the paddle
    boolean isBallTrajectoryOnPaddle = (position.x + velocity.x) >= paddle.x && (position.x + velocity.x) <= paddle.x + paddle.w; //true if the ball will end hitting the padele
    return isPreviousAbovePaddle && isNextBelowPaddle && isBallTrajectoryOnPaddle; //return true if all these statements are true
  }  
  
  //---------------------------------CLEAR SPACE--------------------------------->>> FINISHED
  
  void clearSpace() {
    lines.clear(); //clear the lines that have been drawn
  }
  
  //---------------------------------SET MODE--------------------------------->>> FINISHED
    
  void setMode(GameMode mode) {
    this.mode = mode;
  }  
  
  //---------------------------------SET GRAVITY VELOCITY--------------------------------->>> FINISHED
  
  void setGravityVelocity() {
    velocity.set(0,0);
  }
  
  //---------------------------------SET NORMAL VELOCITY--------------------------------->>> FINISHED
  
  void setNormalVelocity() {
    setRandomInitialVelocity();
  }  
  
  //---------------------------------SET X VELOCITY--------------------------------->>> FINISHED
  
  void setXVelocity(int xVel) {
    velocity.x = xVel;
  } 
  
  //---------------------------------SET INITIAL POSITION--------------------------------->>> FINISHED
  
  void setInitialPosition(int x, int y) {
    position.set(x, y);
  }  
  
  //---------------------------------SET INITIAL POSITION NOMRAL--------------------------------->>> FINISHED
  
  void setInitialPositionNormal() {
    position.set(INIT_X,INIT_Y);
  }  
  
  //---------------------------------SET RANDOM INITIAL POSITION--------------------------------->>> FINISHED
  
  void setRandomInitialVelocity() {
    do {
      double angle = (Math.random()*(Math.PI/3)+(Math.PI/12)); //get a random angle (not close to horizontal and not close to vertical angles)
      velocity.y = -(float)(REG_SPEED*Math.sin(angle)); //use the sin to set the y-component
      if (Math.random() < 0.5) { //decide randomly which side it will go to
        velocity.x = abs((float)(REG_SPEED*Math.cos(angle))); //and use the cos to set the x-component (positive)
      } else {
        velocity.x = -abs((float)(REG_SPEED*Math.cos(angle))); //or negative
      }
    } while (velocity.x == 0); //due to casting, sometimes we get a zero velocity in the x-component
  }
  
  //---------------------------------PRINT LINES--------------------------------->>> FINISHED
 
  void printLines() {
    for (int[] i : lines) { //go over all the saved lines
      stroke((float)(Math.random()*255), (float)(Math.random()*255), (float)(Math.random()*255)); //select a random color
      line(i[0], i[1], i[2], i[3]); //and print the lines
    } 
  }  
  
  //---------------------------------UPDATE LINES--------------------------------->>> FINISHED
  
  void updateLines() { //this adds a new line to the list
    int[] toAdd = {initLineX, initLineY, (int)position.x, (int)position.y}; //with the initial positions and the final positions
    lines.add(toAdd);
    initLineX = (int)position.x;
    initLineY = (int)position.y;
  }  
  
  //---------------------------------DRAW PARTIAL LINE--------------------------------->>> FINISHED
  
  void drawPartialLine() {
    stroke((float)(Math.random()*255), (float)(Math.random()*255), (float)(Math.random()*255)); //create new 
    line(initLineX, initLineY, position.x, position.y);
  } 
  
  //---------------------------------BOUNCE RANDOM--------------------------------->>> FINISHED
  
  void bounceRandom(SoundFile bounceSound) { //bounces randomly from the paddle
    float angle = (float)(Math.random()*(Math.PI/2)+(Math.PI/4));
    velocity.y = -(float)(REG_SPEED*Math.sin(angle));
    velocity.x = (float)(REG_SPEED*Math.cos(angle));
    bounceSound.play();
    hup.update(false);
  }  
  
  //---------------------------------BOUNCE GRAVITY--------------------------------->>> FINISHED
  
  void bounceGravity(SoundFile bounceSound) { //bounces from the paddle with a new random verical velocity
    gravSpeed = (int)(Math.random()*8) + 11;
    velocity.y = -gravSpeed; //an upward velocity
    bounceSound.play();
    hup.update(false);
  }  
  
  //---------------------------------BOUNCE LEFT--------------------------------->>> FINISHED
  
  void bounceLeft(SoundFile bounceSound) { //makes the ball bounce upwards and to the left
    velocity.y = -abs(velocity.y);
    velocity.x = -abs(velocity.x);
    bounceSound.play();
    hup.update(false);
  }  
  
  //---------------------------------BOUNCE RIGHT--------------------------------->>> FINISHED
  
  void bounceRight(SoundFile bounceSound) { //makes the ball bounce upwards and to the right 
    velocity.y = -abs(velocity.y);
    velocity.x = abs(velocity.x);
    bounceSound.play();
    hup.update(false);
  }  
  
}