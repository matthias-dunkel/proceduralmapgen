package main 

enum TileType {
    SAND,
    WATER,
    FOREST,
    STONE,
    MOUNTAIN_TOP,
    MOUNTAIN_BOTTOM,
    NOTHING,
}

class TileId {
    int x
    int y
    
    TileId(int x, int y) {
        this.x = x
        this.y = y
    }

    String toString() {
        return "TileId($this.x, $this.y)"
    }

    boolean equals(other){
        return this.x == other.x && this.y == other.y
    }
}

class Tile {
    int x;
    int y;
    TileId id;
    Set<TileType> options;
    TileType type;

    Tile(int x, int y) {
        this.id = new TileId(x,y)
        this.x = x;
        this.y = y;
        this.type = TileType.NOTHING
        this.options = [
            TileType.SAND, 
            TileType.WATER, 
            TileType.FOREST,
            TileType.STONE,
            TileType.MOUNTAIN_TOP,
            TileType.MOUNTAIN_BOTTOM
            ]
    }

    String toString(){
        return "Tile( $this.id, $this.type, $this.options)"
    }

    TileType type(){
        return this.type
    }

    int entropy() {
        return this.options.size()
    }

    boolean isCollapsed() {
        return this.type != TileType.NOTHING
    }

    boolean collapse() {
        if(this.entropy() > 0){
            this.type = this.options.toList()[Math.round(Math.random() * (this.entropy() -1))]
            return true
        }

        if(!isCollapsed()){
            throw new RuntimeException("Tile: $id cannot collapse, because no options are left.")
        }

        return false;
    }

    /**
        @param tile: The neighbour tile, that maybe has changed and this should be updated accordingly to the change made in tile
        @param o: The Orientation that this is to tile.
        @param rules: The Rules that should be applied.
    **/
    boolean update(Tile tile, Orientation o, Rule[] rules) {
        if(this.isCollapsed()){
            return false;
        }

        Set<TileType> possible = []
                        
        for(rule in rules) {
            for(opt in this.options){
                if(tile.isCollapsed()){
                    possible = possible.plus(rule.apply(tile.type(), opt, o))
                    continue
                }

                for(topts in tile.options){
                    possible = possible.plus(rule.apply(topts, opt, o))
                }
            }
        }

        def prev = this.options.size()        
        this.options = this.options.intersect(possible)

        if(this.options.size() == 0) {
            throw new RuntimeException("No Options left. Rules are too strict.")
        }

        return prev != this.options.size()
    }
}