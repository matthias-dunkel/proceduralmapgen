# Procedural Map Generation
Inspired by the Wave Function Collapse Algorithm, this project delves into the generation of simple maps using rulesets.

# Prerequisites
- Install gradle
- Install Groovy

# First Run

Execute `gradle run`. After some seconds building a window should open and the map is generated and visualized.

# How to use:
## Settings:
In the `App.groovy` you can change the `WIDTH` (Width of the map), `HEIGHT` (Height of the map) and the `SCALE` (Size of the Tiles in Renderer.)

## Renderers
You can choose between two renderers in the `App` class.
1. `StepWise` will show every intermediate step of the algorithm.
2. `Finished` will show the end result after the algorithm has completed. This will take time till the result is visualized. 

## Rules
You can change the rules. Rules are tuples `(TileType, TileType)`, which allow the defined `TileType`s to be neighbours. Neighbours are the four adjacent tiles in the map of a tile.

If you choose too strict rules, the algorithm may come in a state of contradiction, and will terminate with an error. In the future there may be a back tracking approach to reduce contradictions.

# What to expect
The algorithm is not optimized yet. For large Maps the algortihm will take longer time.

A good start is a 20x20 Map.
