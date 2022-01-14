import java.util.Random;

public class Cat extends Creature {

  public final int [] r = {-2,0,2,0}, c = {0,2,0,-2};
  public final String [] preyList = {"MOUSE"};
  private int roundCnt = 0;
  private int roundsTillStarved = 50;

  public Cat(int r, int c, City cty, Random rand) {
    super(r,c,cty,rand);
    this.lab = LAB_YELLOW;
    this.eaten = eaten;
    this.identity = "CAT";
    this.searchDist = 20;
    this.target = null;
  }

  public void maybeTurn() {
    if (rand.nextInt(5) == 0) {
      this.setD(rand.nextInt(NUM_DIRS));
    }
  }

  public void takeAction() {
    if ((eaten) || (roundsTillStarved == 0)) {
      this.city.queueAddCreature(new ZMCat(this.getGridPoint().row, this.getGridPoint().col , this.city, rand));
      this.city.queueRmCreature(this);
    }
    this.city.darwinism(this,preyList);
    this.ate();
  } 

  public boolean die() {
    if ((roundCnt == 50) || (roundsTillStarved == 0) || (eaten)) {
      return true;
    }
    return false;
  } 

  public void step() {
    roundCnt++;
    roundsTillStarved--;

    Creature targ = this.getTarget();

    if ((targ != null) && (targ.die() != true) && (targ.eaten != true)) {
      this.lab = LAB_CYAN;
      int d = this.guageDistance(this.getGridPoint(),targ.getGridPoint(),r,c);
      this.setD(d);
      this.bigStepper(r,c);
    } else if ((targ == null) || (targ.die() == true) || (targ.eaten == true)) {
      Creature ctr = this.city.findTarget(this,preyList,this.searchDist);
      if (ctr != null) {
        this.setTarget(ctr);
        this.lab = LAB_CYAN;
        int d = this.guageDistance(this.getGridPoint(),ctr.getGridPoint(),r,c);
        this.setD(d);
        this.bigStepper(r,c);
      } else {
        this.lab = LAB_YELLOW;
        this.maybeTurn();
        this.bigStepper(r,c);
      }
    }
  }
  private void ate() {
    if (ate)
    roundsTillStarved = 50;
    ate = false;
  }
}