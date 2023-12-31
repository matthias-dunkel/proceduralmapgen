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

    String toString() {
        return "($t1, $t2, $o)"
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
            new Rule(t2, t1, Orientation.TOP),
            new Rule(t1, t2, Orientation.RIGHT),
            new Rule(t2, t1, Orientation.RIGHT),
            new Rule(t1, t2, Orientation.LEFT),
            new Rule(t2, t1, Orientation.LEFT),
            new Rule(t1, t2, Orientation.BOTTOM),
            new Rule(t2, t1, Orientation.BOTTOM)
        ]
    }

    // Allow all types to be withitself and with all others.
    Rule[] allowAllOrientations(ArrayList<TileType> ts) {
        ts.collect{ tile1 -> ts.collect{ tile2 -> this.allowAllOrientations(tile1, tile2)}  }.flatten()
    }
}
