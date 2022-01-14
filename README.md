In my Creature.java class, I implemented new variables:
```
  public Creature target;
  
  public int searchDist;
  public String identity;

  public boolean healed = false;  
  public boolean heal = false;
    
  public boolean eaten = false;
  public boolean ate = false;
```
and multiple new methods:
```
    public void setD(int dd) {
        this.dir = dd;
    }
    public String getIdentity() {
        return this.identity;
    }
    public int getDir() {
        return this.dir;
    }
    public void bigStepper(int[] r,int[] c) {  
      int oldRow = this.point.row;
      int oldCol = this.point.col;
      GridPoint p = new GridPoint(oldRow,oldCol);

      this.point.row = (this.point.row + r[dir] + City.HEIGHT) % City.HEIGHT;
      this.point.col = (this.point.col + c[dir] + City.WIDTH) % City.WIDTH;
      this.city.updateGrid(this, p, point);
    }
    public int guageDistance(GridPoint creatureP, GridPoint preyP, int[] r, int[] c) {
      int d = creatureP.dist(preyP);
      int pushD = 0;
      for (int i = 0;i<NUM_DIRS;++i) {
        GridPoint p = new GridPoint(creatureP.row, creatureP.col);
        p.row = (this.point.row + r[i] + City.HEIGHT) % City.HEIGHT;
        p.col = (this.point.col + c[i] + City.WIDTH) % City.WIDTH;
        if (p.dist(preyP) == 0) {
          pushD = i;
          break;
        } else if (p.dist(preyP) < d) {
          pushD = i;
        }
      }
      return pushD;
    }
    public void setTarget(Creature c) {
      this.target = c;
    }
    public Creature getTarget() {
      return this.target;
    }
```
These methods and variables were used to create uniformity in the sense that most of the creatures would use the same variables to interact with each other (in the chasing method) and also to update the GridPoint in the creatureGrid.
My City.java class also had new methods such as an updateGrid(), a darwinism() method (to check if two creatures were on the same spot, if they were, then depending on their identity they could eat, or be eaten), and a findTarget() method (to find a creature a target).

```
    public void updateGrid (Creature ctr, GridPoint prevP, GridPoint currP) {
      creatureGrid.get(prevP).remove(ctr);
      creatureGrid.get(currP).add(ctr);
    }

    public void darwinism(Creature ctr, String[] preyL) {
      List<Creature> l = creatureGrid.get(ctr.getGridPoint());
      int i = 0;
      while (i < preyL.length) {
        for (Creature c : l) {
          if ((c.getIdentity().equals(preyL[i])) && (c.eaten == false)) {
            c.eaten = true;
            ctr.ate = true;
          }
        }
        i++;
      }
    }

    public Creature findTarget(Creature c, String[] identity, int maxDist) {
      /* Finds closest target*/
      int leastDist = maxDist;
      int i = 0;
      Creature target = null;
      
      while (i < identity.length) {
        for (Creature t : creatures) {
          if ((t.getIdentity().equals(identity[i])) && (t.eaten == false)) {
            int d = c.getGridPoint().dist(t.getGridPoint());
            if ((d < leastDist) && (d != 0)) {
              leastDist = d;
              target = t;
            }
          }
        }
        i++;
      }
      return target;
    }
```
I also had multiple new variables in the creature subclasses, including a unique prey list for each creature that could eat, two int arrays to be used for their step() method, and a counter that was decremented, so that if they didnt eat and the counter reached 0, they would die.

```
  public final int [] r = {-2,0,2,0}, c = {0,2,0,-2};
  public final String [] preyList = {"MOUSE"};
  private int roundCnt = 0;
  private int roundsTillStarved = 50;
```

The chasing method was fixed once I fixed how to add/retrieve/delete items from the creatureGrid hashmap, and adding a chasing function for cats wasnt bad either. ZMCat implementation was the same for adding a cat. I implemented a creature called the "Vaccine Giver" that essentially healed Zombie Cats to regular ones. It would appear every 150 rounds, and moved faster than the zombie cats. Moreover, I had to implement helper functions specifically for the Vaccine Giver, which would find a target to heal, and heal it. It also spawned a new one after it reaches its 75 round counter. I observed that usually after some simulation time that there would be hordes of Vaccine Givers and ZMCats essentially just chasing each other, which was cool.

```
    public void healer(Creature ctr, String[] preyL) {
      List<Creature> l = creatureGrid.get(ctr.getGridPoint());
      int i = 0;
      while (i < preyL.length) {
        for (Creature c : l) {
          if ((c.getIdentity().equals(preyL[i])) && (c.eaten == false)) {
            c.healed = true;
            ctr.heal = true;
          }
        }
        i++;
      }
    }

    public Creature giveRelief(Creature c, String[] identity, int maxDist) {
      int leastDist = maxDist;
      int i = 0;
      Creature target = null;
      
      while (i < identity.length) {
        for (Creature t : creatures) {
          if ((t.getIdentity().equals(identity[i])) && (t.healed == false)) {
            int d = c.getGridPoint().dist(t.getGridPoint());
            if ((d < leastDist) && (d != 0)) {
              leastDist = d;
              target = t;
            }
          }
        }
        i++;
      }
      return target;
    }
```
