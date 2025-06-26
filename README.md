# Death & Taxes
*Forge Edition*

**About**

Instead of losing your items to the void, lava, or a distant cave thousands of blocks from your base, they will now be picked up by a new mob called the Scavenger. The Scavenger arrives once you have respawned, willing to return your loot for a price. But be quick, the Scavenger leaves one day after being summoned.

**Compatibility**

This is technically a gravestone mod, where the Scavenger functions as the gravestone block. Using this mod alongside other gravestone mods can cause item duplication, empty inventories, or other instabilities. The goal of this mod is to add a price to death, balancing the fact that items are no longer lost on death.

**Merchant Pricing**

Upon player death, every item is evaluated using this loot table:
```
deathtaxes:gameplay/scavenger_pricing
```
The loot table contains these parameters:

* `origin` : The player's **respawn location**
* `entity` : The **respawned player**
* `tool` : The **currently evaluated item**

The loot table uses `match_tool` and adds to the count of `minecraft:emerald` for every match.

**Player Drop Blacklist**

To prevent the player from buying emeralds with emeralds, an item tag list has been added that discards any matching items:
```
deathtaxes:gameplay/blacklisted_loot
```

**Additional Conditions**

Two new loot conditions have been added to versions prior to **1.21.5** to make up for the lack of data component comparison. These two loot conditions are:
* `deathtaxes:has_rarity`
* `deathtaxes:has_enchantments`