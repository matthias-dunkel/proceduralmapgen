package main


enum Orientation {
    TOP, BOT, LEFT, RIGHT
}

class Rule {
    //Orientation o,
    TileType tile1
    TileType tile2

    Rule(TileType t1, TileType t2) {
        this.tile1 = t1;
        this.tile2 = t2;
    }

    Set<TileType> apply(Tile tile) {
        if(tile.isCollapsed()){
            if(tile.type() == this.tile1) {
                return [this.tile2] as Set
            } 
        
            if(tile.type() == this.tile2) {
                return [this.tile1] as Set
            }

            return [] as Set
        }
        return tile.options.intersect([this.tile1, this.tile2] as Set)
    }
}

