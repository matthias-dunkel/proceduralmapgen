package main 

class Board {
    int width
    int height
    ArrayList<Tile> tiles;
    ArrayList<Integer> tilesSorted;
    int numberOfCollapsed;
    
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

    // TODO: Optimize this step. As it is takes O(n) to find the minimum.
    // Also if several Tiles have the same entropy then on eshould be randomly picked.
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
        while(q.size() > 0) {
            def collapsed = q.poll();
            
            if(col - 1 > 0) {
                def tile = this.getAt(row, col-1)
            }
        
        def left;
        def top;
        def right;
        def bottom;
        for(int rowN = row - 1; rowN <= row + 1; rowN++) {
            if(rowN < this.height && rowN >= 0) {
                for(int colN = col -1; colN < col + 2; colN++) {
                    
                    if(colN == col && rowN == row) {
                        continue;
                    }

                    if(colN >= 0 && colN < this.width) {
                        def tile = this.getAt(rowN, colN);
                        def didUpdate = tile.update(collapsed, rules)
                        
                        if(didUpdate){
                            q << tile
                        }
                    }
                }
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

        println("Type of Collapsed: $minTile.type")

        this.numberOfCollapsed++
        this.updateNeighbours(minId, rules)
        this.sortTiles()

        return minTile
    }
}