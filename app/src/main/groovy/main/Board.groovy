package main 

class Board {

    int width
    int height
    ArrayList<TileId> tilesSorted
    int numberOfCollapsed
    
    private ArrayList<Tile> tiles
    private TileFrequency f;
    
    Board(int width, int height){
        this.width = width;
        this.height = height;
        this.tiles = (0..width*height -1).collect{ 
                def c = convertIndexToCoord(it);
                new Tile(c.x, c.y) 
            }
        this.tilesSorted = this.tiles.collect {
            it.id
        }

        this.f = new TileFrequency()

        this.numberOfCollapsed = 0;
    }

    TileId convertIndexToCoord(int id) {
        int y = id / this.width
        int x = id % this.width

        return new TileId(x, y)
    }

    Tile getAt(TileId id){
        
        if(id.y >= this.height || id.x >= this.width) {
            throw new IndexOutOfBoundsException("No tile with id: $id for Board with dimensions: $this.width x $this.height")
        }
        
        def tile = this.tiles[id.y * this.width + id.x]
        
        if(id != tile.id) {
            throw new RuntimeException("Tiles are not stored correctly in Board. Searched for: $id, got: $tile.id")
        }
        return tile
    }

    void setAt(TileId id, TileType val){
        if(id.y >= this.height || id.x >= this.width) {
            throw new IndexOutOfBoundsException("Row Index: $row, Row Size: ${this.height}; Col Index: $col, Col size: ${this.width}")
        }

        this.tiles[id.y * this.width + id.x] = val       
    }

    Tile lowestEntropy() {
        this.sortTiles()

        def id = this.tilesSorted[0]
        def tile = this.getAt(id)

        return tile
    }

    void removeFirstFromSorted() {
        if(this.tilesSorted.size() > 0){
            this.tilesSorted.remove(0)
        }
    }

    void sortTiles() {
        this.tilesSorted = this.tilesSorted.sort { this.getAt(it).entropy() }
    }

    ArrayList<TileId> getNeighbours(TileId id) {
        ArrayList<TileId> neighbours = []
      
        if(id.x -1 >= 0) { 
            neighbours.add(new TileId(id.x -1, id.y))
        }
        if(id.y -1 >= 0) { 
            neighbours.add(new TileId(id.x, id.y -1))
        }
        if(id.x + 1 < this.width){ 
           neighbours.add(new TileId(id.x +1, id.y))
        }
        if(id.y + 1 < this.height){
            neighbours.add(new TileId(id.x, id.y +1))
        }

        return neighbours
    }

    ArrayList<TileId> updateNeighbours(TileId id, Rule[] rules) {

        ArrayList<TileId> updated = []

        def q = [this.getAt(id)] as Queue

        while(q.size() > 0) {

            def collapsed = q.poll()
            
            def neighbours = getNeighbours(collapsed.id)
            
            neighbours.collect { n ->
                def tile = this.getAt(n)
                def didUpdate = tile.update(collapsed, rules)
                //didUpdate = false;
                if(didUpdate){
                    if(!updated.contains(tile.id) && !tile.isCollapsed()){
                         q << tile
                    }
                    updated.add(tile.id)
                }
            } 
        }

        return updated
    }

    boolean hasSteps() {
        return this.numberOfCollapsed < this.width * this.height
    }

    Tile stepForward(Rule[] rules) {
        if(!this.hasSteps()) {
            throw new RuntimeException("No Steps to go. All tiles collapsed.")
        }

        def minTile = this.lowestEntropy()
        def didCollapse = minTile.collapse(this.f)
        if(didCollapse) {
            removeFirstFromSorted()
            this.f.add(minTile.type())
        }

        this.numberOfCollapsed++
        this.updateNeighbours(minTile.id, rules)
        
        return minTile
    }   

    Tile step(Rule[] rules) {
        return this.stepForward(rules)
    }
}