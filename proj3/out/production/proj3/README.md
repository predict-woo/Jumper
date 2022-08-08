# Build Your Own World Design Document

**Partner 1:**

**Partner 2:**

## Classes and Data Structures

### Engine
runs the entire thing, 
creates map, player
creates logic

### Light
```
x
y
state: true / false
```

### Map
```
generates map: int[][]
outside: -1
ground: 0
wall: 1
light: 2
```

### Player
```
x
y
moves
lightRadius
Map
```

### Renderer
```
player
map
light
```
-> returns `TETile[][]`

## Algorithms
```
bsp
```
## Persistence
