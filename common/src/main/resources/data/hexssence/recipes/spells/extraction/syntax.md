# Extraction Recipes
```json5
{
  "type": "hexssence:essence_extraction",
  "ingredients": [
    { "item": "minecraft:amethyst_shard", "count": 1 } // "count" defaults to 1
  ],
  "results": [
    { "item": "hexssence:essence_hex", "count": 1, "chance": 0.75 } // "count" and "chance" defaults to 1
  ],
  "mediaCost": 50000 // cost for the combination, 1 dust is 10000 
}
```
The result item field DOESN'T need the "hexssence:essence_jars/valid" tag.
