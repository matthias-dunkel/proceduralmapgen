package main


enum Orientation {
    TOP, BOTTOM, LEFT, RIGHT
}


/**
* A Rule defines a possible combination of tiles. (T1, T2, LEFT) means, that T2 can be left of T1.
*/
class Rule {
    Orientation o
    TileType t1
    TileType t2

    Rule(TileType t1, TileType t2, Orientation o) {
        this.t1 = t1
        this.t2 = t2
        this.o = o
    }

    Set<TileType> apply(TileType type1, TileType type2, Orientation o) {
        if(type1 == this.t1 && type2 == this.t2 && this.o == o) {
            return [this.t2] as Set
        } 
    
        return [] as Set
    }   
}

class RuleSetGenerator {
    Rule[] allowAllOrientations(TileType t1, TileType t2) {
        return [
            new Rule(t1, t2, Orientation.TOP),
            new Rule(t1, t2, Orientation.RIGHT),
            new Rule(t1, t2, Orientation.LEFT),
            new Rule(t1, t2, Orientation.BOTTOM)
        ]
    }
}
