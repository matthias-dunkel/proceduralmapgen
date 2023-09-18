# Procedural Map Generation
Inspired by the Wave Function Collapse Algorithm, this project delves into the generation of simple maps using rulesets.

# Idea
A map is a grid of tiles. Every tile has a type (for example a color). The algorithm uses rules to define relationships between tile types. The goal of the algorithm is to find a valid assignment of types to tiles without breaking a rule. In the start every tile can have all possible types, but for every step one tile is assigned one type, which then leads to an update of the other tiles, reducing the type options that can be assigned to the tiles.

# Prerequisites
- Install gradle
- Install Groovy

# First Run
A fully working example is shown in the App File. Execute `gradle run` to run the example. After some seconds building, a window should open and the map is generated and visualized.

# How to use
## Settings
In the `App.groovy` you can change the `WIDTH` (Width of the map), `HEIGHT` (Height of the map) and the `SCALE` (Size of the Tiles in Renderer.)

## Renderers
You can choose between two renderers in the `App` class.
1. `StepWise` will show every intermediate step of the algorithm.
2. `Finished` will show the end result after the algorithm has completed. This will take time till the result is visualized. 

## Rules
You can change the rules. Rules are tuples `(TileType, TileType, Orientation)`, which allow the defined `TileType`s to be neighbours in a specific Orientation. For example:
All tiles of type `T1` should be allowed to be the left neighbours of all tiles of type `T2`. The according rule would be:
`(T2, T1, LEFT)` (Spoken tiles of type `T2` can have neihbours of type `T1`). 

If you choose too strict rules, the algorithm may come in a state of contradiction, and will terminate with an error. In the future there may be a back tracking approach to reduce contradictions.

# What to expect
The algorithm is not optimized yet. For large maps the algortihm will take longer time.

A good start is a 20x20 Map.

## Examples
For both examples, the same ruleset was used. It is the standard ruleset defined in the App class main function.

![Forest](/output/20x20.png)
![Forest](/output/20x20-mountains.png)
