package main;

class Generator {

    Map map
    Rule[] rules
    int numberOfCollapsed;

    Generator(Map map, Rule[] rules) {
        this.map = map
        this.rules = rules
        this.numberOfCollapsed = 0;
    }

    boolean hasSteps() {
        return this.numberOfCollapsed < this.map.width * this.map.height
    }

    ArrayList<TileId> updateNeighbours(TileId id) {

        ArrayList<TileId> updated = []

        def q = [this.map.getAt(id)] as Queue

        while(q.size() > 0) {

            def collapsed = q.poll()
            
            def neighbours = this.map.getNeighbours(collapsed.id)
            
            neighbours.collect { n ->
                def tile = this.map.getAt(n)
                def didUpdate = tile.update(collapsed, this.rules)
                
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

    Tile stepForward() {
        if(!this.hasSteps()) {
            throw new RuntimeException("No Steps to go. All tiles collapsed.")
        }

        def minTile = this.map.lowestEntropy()
        def didCollapse = minTile.collapse()
        if(didCollapse) {
            this.map.removeFromSorted(minTile.id)
        }

        this.numberOfCollapsed++
        this.updateNeighbours(minTile.id)
        
        return minTile
    }   

    Tile step() {
        return this.stepForward()
    }

}