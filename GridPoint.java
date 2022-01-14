public class GridPoint extends Object{

    //-----------------
    //You get this class for free. You can edit it as you see fit for
    //your model, but do not change equals or hashcode
    //------------------
    
    //publically accessible row and col for convenience
    public int row, col;
    
    public GridPoint(int row, int col){
        this.row = row;
        this.col = col;
    }


    //copy construtor
    public GridPoint(GridPoint other){
        this.row = other.row;
        this.col = other.col;
    }


    //The following two methods let this be used as a Key in a HashMap
    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof GridPoint)) return false;
        GridPoint other = (GridPoint) o;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode(){
        return this.row * 31 + this.col;
    }

    //Free two-string method prints out in (x,y) - (col,row) to help you debug
    @Override
    public String toString(){
        return "("+this.col+","+this.row+")";
    }


    //Distance to another gridPoint is the manhattan distance
    //number of rows away + number of cols away
    public int dist(GridPoint other){
        return Math.abs(this.row-other.row)+Math.abs(this.col-other.col);
    }
}
