package main 

class Map {

    int width
    int height
    ArrayList<TileId> tilesSorted
    int numberOfCollapsed
    
    private ArrayList<Tile> tiles
    
    Map(int width, int height){
        this.width = width;
        this.height = height;
        this.tiles = (0..width*height -1).collect{ 
                def c = convertIndexToCoord(it);
                new Tile(c.x, c.y) 
            }
        this.tilesSorted = this.tiles.collect {
            it.id
        }

        this.numberOfCollapsed = 0;
    }

    TileId convertIndexToCoord(int id) {
        int y = id / this.width
        int x = id % this.width

        return new TileId(x, y)
    }

    Tile getAt(TileId id){
        
        if(id.y >= this.height || id.x >= this.width) {
            throw new IndexOutOfBoundsException("No tile with id: $id for Map with dimensions: $this.width x $this.height")
        }
        
        def tile = this.tiles[id.y * this.width + id.x]
        
        if(id != tile.id) {
            throw new RuntimeException("Tiles are not stored correctly in Map. Searched for: $id, got: $tile.id")
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

    
}