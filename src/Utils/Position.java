package Utils;

public class Position {
    int x,y;
    public Position(int x,int y){
        this.x = x;
        this.y = y;
    }
    public void move(int x,int y){
        this.x += x;
        this.y += y;
    }
    public void moveTo(int x,int y){
        this.x = x;
        this.y = y;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Position)){
            return false;
        }
        Position position = (Position) obj;
        if(position.x == this.x && position.y == this.y){
            return true;
        }
        return  false;
    }
    public String toString(){
        return "Position("+this.x+","+this.y+")";
    }
    public Position copy(){
        return new Position(this.x,this.y);
    }
}
