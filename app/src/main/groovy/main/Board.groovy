package main 

class Board {
    int width
    int height
    ArrayList<Integer> tilesSorted
    int numberOfCollapsed

    private ArrayList<Tile> tiles
    
    Board(int width, int height){
        this.width = width;
        this.height = height;
        this.tiles = (0..width*height -1).collect{ 
                def c = convertIndexToCoord(it);
                new Tile(c[0], c[1]) 
            }
        this.tilesSorted = (0..width*height -1).collect {
            it
        }
        this.numberOfCollapsed = 0;
    }

    Tuple2<Integer, Integer> convertIndexToCoord(int id) {
        int x = id / this.width
        int y = id % this.width

        return new Tuple(x,y)
    }

    Tile getAt(int row, int col){
        if(row >= this.height || col >= this.width) {
            throw new IndexOutOfBoundsException("Row Index: $row, Row Size: ${this.height}; Col Index: $col, Col size: ${this.width}")
        }
        return this.tiles[row * this.width + col]
    }

    void setAt(int row, int col, TileType val){
        if(row >= this.height || col >= this.width) {
            throw new IndexOutOfBoundsException("Row Index: $row, Row Size: ${this.height}; Col Index: $col, Col size: ${this.width}")
        }

        this.tiles[row * this.width + col] = val       
    }

    int lowestEntropy() {
        def i = 0;
        while(i < this.tilesSorted.size()){
            if(!this.tiles[tilesSorted[i]].isCollapsed()){
                return this.tilesSorted[i]
            }
            i++
        }

        return this.tilesSorted[0]
    }

    void sortTiles() {
        this.tilesSorted = this.tilesSorted.sort { this.tiles[it] }
    }

    void updateNeighbours(int id, Rule[] rules) {
        def c = this.convertIndexToCoord(id)

        updateNeighbours(c[0], c[1], rules)
    }

    void updateNeighbours(int row, int col, Rule[] rules) {
        def q = [this.getAt(row, col)] as Queue

        while(q.size() > 0){
            def collapsed = q.poll()
            def neighbours = []

            if(col -1 >= 0) {
                neighbours.add(this.getAt(row, col -1))
            }

            if(row -1 >= 0) {
                neighbours.add(this.getAt(row -1, col))
            }

            if(col + 1 < this.height){
               neighbours.add(this.getAt(row, col +1))
            }

            if(row + 1 < this.width){
                neighbours.add(this.getAt(row + 1, col))
            }

            neighbours.collect { tile ->
                def didUpdate = tile.update(collapsed, rules)

                if(didUpdate){
                    q << tile
                }
            } 
        }
        
        
    }

    boolean hasSteps() {
        return this.numberOfCollapsed < this.width * this.height
    }

    Tile step(Rule[] rules) {
        if(!this.hasSteps()){
            throw new RuntimeException("No Steps to go. All tiles collapsed.")
        }

        def minId = this.lowestEntropy()
        def minTile = this.tiles[ minId  ];
        minTile.collapse()

        this.numberOfCollapsed++
        this.updateNeighbours(minId, rules)
        this.sortTiles()

        return minTile
    }
}