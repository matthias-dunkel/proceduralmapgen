# Procedural Map Generation
Inspired by the Wave Function Collapse Algorithm, this project delves into the generation of simple maps using rulesets.

# Top Level Algorithm
1. Initialize a grid of tiles with a specified width and height. Initialize every tile with a set of tile type options `O`.  
2. While a tile exists with a set size larger than 1:
    1. Choose a tile `t` and choose a tiletype `o` from the options for `t`. This step is called **collapsing**.
    2. Update tiles recursively with a given rule set `R`, which defines the possible neighbour relationships of possible tiletypes.

Choosing a tile `t` in step 2.1 can be implemented in several different ways. In this project it is implemented by choosing one of the tiles with the least possible options left.

Choosing a tiletype `o` can be implemented in several different ways. In this project `o` is choosen randomly from `O_i`.
# Prerequisites
- Install gradle
- Install Groovy

# How to use

- Clone the repository to your drive.
- Run `cd proceduralmapgen`.
- Run `gradle run` to run the example in the `App.groovy`.

## Map Settings
In the `App.groovy` you can change the `WIDTH` (Width of the map), `HEIGHT` (Height of the map) and the `SCALE` (Size of the Tiles in Renderer.)

## Renderers
You can choose between two renderers in the `App` class.
1. `StepWise` will show every intermediate step of the algorithm.
2. `Finished` will show the end result after the algorithm has completed. This will take time till the result is visualized. 

## Rules
You can change the rules. Rules are tuples `(TileType, TileType, Orientation)`, which allow the defined `TileType`s to be neighbours in a specific Orientation. For example:
All tiles of type `T1` should be allowed to be the left neighbours of all tiles of type `T2`. The according rule would be:
`(T2, T1, LEFT)` (Spoken: "tiles of type `T2` can have left neihbours of type `T1`"). 

If you choose too strict rules, the algorithm may come in a state of contradiction, and will terminate with an error. In the future there may be a back tracking approach to reduce contradictions.

# What to expect
The algorithm is not optimized yet. For large maps the algortihm will take longer time.

A good start is a 20x20 Map.

## Examples
For both examples, the same ruleset was used. It is the standard ruleset defined in the App class main function.

![Forest](/output/20x20.png)
![Forest](/output/20x20-mountains.png)
