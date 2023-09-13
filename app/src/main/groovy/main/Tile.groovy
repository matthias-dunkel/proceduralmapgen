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

class TileFrequency {
    Map m;
    int n;
    
    TileFrequency(){
        this.m = [:]
        this.n = 0
    }

    TileFrequency(ArrayList<Tuple2<TileId, Integer>> f) {
        for(t in f){
            this.m[t[0]] = t[1]
        }
    }

    String toString() {
        def r = "TileFrequency( n: $this.n \n"
        for (mi in m) {
            r += "  $mi.key : $mi.value\n"
        }
        return r + ")\n"
    }

    int add(TileType t) {
        if(m[t] == null) {
            m[t] = 1
            this.n++
        }
        return ++m[t]
    }

    int get(TileType t) {
        if(m[t] == null) {
            m[t] = 1
            this.n++
        }
        return m[t]
    }

    void set(TileType t, int v) {
        if(m[t] != null) {
            this.n -= m[t]
        }
        m[t] = v;
        this.n += v;
    }

    int frequencyOf(TileType t) {
        return m[t] / this.n
    }

    TileType chooseRandomly() {
        def i = Math.random() * this.n;
        println(i)
        def sum = 0;
        for(entry in m) {
            sum += entry.value
            if(sum >= i ) {
                return entry.key;
            }
        }
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

    boolean collapse(TileFrequency f) {
        if(this.entropy() > 0) {
            def tf = new TileFrequency()

            for (o in this.options) {
                tf.set(o, f.get(o))
            }

            this.type = tf.chooseRandomly()
            
            return true
        }

        if(!this.isCollapsed()){
            throw new RuntimeException("Tile: $id cannot collapse, because no options are left.")
        }

        return false;
    }

    boolean update(Tile collapsed, Rule[] rules) {
        if(this.isCollapsed()){
            return false;
        }

        Set<TileType> possible = []
                        
        for(rule in rules) {
            possible = possible.plus(rule.apply(collapsed))
        }

        def prev = this.options.size()
        this.options = this.options.intersect(possible)

        if(this.options.size() == 0) {
            throw new RuntimeException("No Options left. Rules are too strict.")
        }

        return prev != this.options.size()
    }
}