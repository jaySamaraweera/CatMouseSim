import java.util.Random;

public class Mouse extends Creature {

  public final int [] r = {-1,0,1,0}, c = {0,1,0,-1};
  private int roundCnt = 0;

  public Mouse(int r, int c, City cty, Random rand) {
    super(r, c, cty, rand);
    this.lab = LAB_BLUE;
    this.eaten = eaten;
    this.identity = "MOUSE";
  }

  public void maybeTurn() {
    if (rand.nextInt(20) == 0) {
        this.setD(rand.nextInt(NUM_DIRS));
    }
  }

  public void takeAction() {
    if (eaten) {
      this.city.queueRmCreature(this);
    }

    if (roundCnt == 20) {
        this.city.queueAddCreature(new Mouse(this.getGridPoint().row, this.getGridPoint().col , this.city, rand)); 
    }    
  }

  public boolean die() {
    if ((roundCnt == 30) || (eaten)) {
        return true;    
    }    
    return false;
  }

  public void step() {
    roundCnt++;

    this.bigStepper(r,c);
  }

  
}