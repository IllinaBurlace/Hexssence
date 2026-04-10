# Combination Recipes
```json5
{
  "type": "hexssence:essence_combination",
  "ingredients": [
    { "item": "hexssence:essence_hex", "count": 1 } // "count" defaults to 1, "item" needs to have the "hexssence:essence_jar/valid" tag otherwise combination is impossible
  ],
  "results": [
    { "item": "minecraft:amethyst_shard", "count": 1, "chance": 0.75 } // "count" and "chance" defaults to 1
  ],
  "mediaCost": 50000 // cost for the combination, 1 dust is 10000 
}
```