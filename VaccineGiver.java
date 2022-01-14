import java.util.Random;

public class VaccineGiver extends Creature {

  public final int [] r = {-4,0,4,0}, c = {0,4,0,-4};
  public final String [] preyList = {"ZMCAT"};
  private int roundCnt = 0;
  private int roundsTillStarved = 150;

  public VaccineGiver(int r, int c, City cty, Random rand) {
    super(r,c,cty,rand);
    this.lab = LAB_MAGENTA;
    this.identity = "VACCINEGIVER";
    this.searchDist = 40;
    this.target = null;
  }

  public void maybeTurn() {
    if (rand.nextInt(5) == 0) {
      this.setD(rand.nextInt(NUM_DIRS));
    }
  }

  public void takeAction() {
    if (roundsTillStarved == 0) {
      this.city.queueRmCreature(this);
    }
    if (roundCnt == 100) {
      this.city.queueAddCreature(new VaccineGiver(this.getGridPoint().row, this.getGridPoint().col, this.city, this.rand));
    }
    this.city.healer(this,preyList);
    this.healedCtr();
  } 

  public boolean die() {
    if ((roundCnt == 150) || (roundsTillStarved == 0)) {
      return true;
    }
    return false;
  } 

  public void step() {
    roundCnt++;
    roundsTillStarved--;

    Creature targ = this.getTarget();

    if ((targ != null) && (targ.die() != true) && (targ.healed != true)) {
      this.lab = LAB_ORANGE;
      int d = this.guageDistance(this.getGridPoint(),targ.getGridPoint(),r,c);
      this.setD(d);
      this.bigStepper(r,c);
    } else if ((targ == null) || (targ.die() == true) || (targ.healed == true)) {
      Creature ctr = this.city.giveRelief(this,preyList,this.searchDist);
      if (ctr != null) {
        this.lab = LAB_ORANGE;
        this.setTarget(ctr);
        int d = this.guageDistance(this.getGridPoint(),ctr.getGridPoint(),r,c);
        this.setD(d);
        this.bigStepper(r,c);
      } else {
        this.lab = LAB_MAGENTA;
        this.maybeTurn();
        this.bigStepper(r,c);
      }
    }
  }

  private void healedCtr() {
    if (heal) {
      roundsTillStarved = 150;
      heal = false;
    }
  }
}