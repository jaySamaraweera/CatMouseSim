import java.util.Random;

public class ZMCat extends Creature {

  public final int [] r = {-3,0,3,0}, c = {0,3,0,-3};
  public final String [] preyList = {"CAT","MOUSE"};
  private int roundCnt = 0;
  private int roundsTillStarved = 100;

  public ZMCat(int r, int c, City cty, Random rand) {
    super(r,c,cty,rand);
    this.lab = LAB_RED;
    this.identity = "ZMCAT";
    this.searchDist = 40;
    this.target = null;
  }

  public void maybeTurn() {
    if (rand.nextInt(5) == 0) {
      this.setD(rand.nextInt(NUM_DIRS));
    }
  }

  public void takeAction() {
    this.beenHealed();
    this.city.darwinism(this,preyList);
    this.ate();
  } 

  public boolean die() {
    if ((roundCnt == 100) || (roundsTillStarved == 0)) {
      return true;
    }
    return false;
  }

  public void step() {
    roundCnt++;
    roundsTillStarved--;

    Creature targ = this.getTarget();

    if ((targ != null) && (targ.die() != true) && (targ.eaten != true)) {
      this.lab = LAB_BLACK;
      int d = this.guageDistance(this.getGridPoint(),targ.getGridPoint(),r,c);
      this.setD(d);
      this.bigStepper(r,c);
    } else if ((targ == null) || (targ.die() == true) || (targ.eaten == true)) {
      Creature ctr = this.city.findTarget(this,preyList,this.searchDist);
      if (ctr != null) {
        this.setTarget(ctr);
        this.lab = LAB_BLACK;
        int d = this.guageDistance(this.getGridPoint(),ctr.getGridPoint(),r,c);
        this.setD(d);
        this.bigStepper(r,c);
      } else {
        this.lab = LAB_RED;
        this.maybeTurn();
        this.bigStepper(r,c);
      }
    }
  }

  private void ate() {
    if (ate)
    roundsTillStarved = 100;
    ate = false;
  }

  private void beenHealed() {
    if (healed) {
      this.city.queueRmCreature(this);
      this.city.queueAddCreature(new Cat(this.getGridPoint().row, this.getGridPoint().col , this.city, rand));
    }
  }
}