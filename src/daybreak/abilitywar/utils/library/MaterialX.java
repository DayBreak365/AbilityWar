package daybreak.abilitywar.utils.library;
/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Hex_27
 * Copyright (c) 2020 Crypto Morin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.google.common.collect.ImmutableSet;
import daybreak.abilitywar.utils.base.minecraft.version.ServerVersion;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public enum MaterialX {
	ACACIA_BOAT("BOAT_ACACIA"),
	ACACIA_BUTTON("WOOD_BUTTON"),
	ACACIA_DOOR("ACACIA_DOOR_ITEM"),
	ACACIA_FENCE,
	ACACIA_FENCE_GATE,
	ACACIA_LEAVES("LEAVES_2"),
	ACACIA_LOG("LOG_2"),
	ACACIA_PLANKS(4, "WOOD"),
	ACACIA_PRESSURE_PLATE("WOOD_PLATE"),
	ACACIA_SAPLING(4, "SAPLING"),
	ACACIA_SIGN("SIGN"),
	ACACIA_SLAB(4, "WOOD_STEP"),
	ACACIA_STAIRS,
	ACACIA_TRAPDOOR("TRAP_DOOR"),
	ACACIA_WALL_SIGN("WALL_SIGN"),
	ACACIA_WOOD("LOG_2"),
	ACTIVATOR_RAIL,
	AIR,
	ALLIUM(2, "RED_ROSE"),
	ANDESITE(5, "STONE"),
	ANDESITE_SLAB,
	ANDESITE_STAIRS,
	ANDESITE_WALL,
	ANVIL,
	APPLE,
	ARMOR_STAND,
	ARROW,
	ATTACHED_MELON_STEM(7, "MELON_STEM"),
	ATTACHED_PUMPKIN_STEM(7, "PUMPKIN_STEM"),
	AZURE_BLUET(3, "RED_ROSE"),
	BAKED_POTATO,
	BAMBOO(Version.v1_14),
	BAMBOO_SAPLING(Version.v1_14),
	BARREL(Version.v1_14),
	BARRIER,
	BAT_SPAWN_EGG(65, "MONSTER_EGG"),
	BEACON,
	BEDROCK,
	BEEF("RAW_BEEF"),
	BEEHIVE(Version.v1_15),
	/**
	 * Beetroot is a known material in pre-1.13
	 * Use XBlock when comparing block types.
	 */
	BEETROOT("BEETROOT_BLOCK"),
	BEETROOTS("BEETROOT"),
	BEETROOT_SEEDS,
	BEETROOT_SOUP,
	BEE_NEST(Version.v1_15),
	BEE_SPAWN_EGG(Version.v1_15),
	BELL(Version.v1_14),
	BIRCH_BOAT("BOAT_BIRCH"),
	BIRCH_BUTTON("WOOD_BUTTON"),
	BIRCH_DOOR("BIRCH_DOOR_ITEM"),
	BIRCH_FENCE,
	BIRCH_FENCE_GATE,
	BIRCH_LEAVES(2, "LEAVES"),
	BIRCH_LOG(2, "LOG"),
	BIRCH_PLANKS(2, "WOOD"),
	BIRCH_PRESSURE_PLATE("WOOD_PLATE"),
	BIRCH_SAPLING(2, "SAPLING"),
	BIRCH_SIGN("SIGN"),
	BIRCH_SLAB(2, "WOOD_STEP"),
	BIRCH_STAIRS("BIRCH_WOOD_STAIRS"),
	BIRCH_TRAPDOOR("TRAP_DOOR"),
	BIRCH_WALL_SIGN("WALL_SIGN"),
	BIRCH_WOOD(2, "LOG"),
	BLACK_BANNER("BANNER"),
	BLACK_BED(15, "BED"),
	BLACK_CARPET(15, "CARPET"),
	BLACK_CONCRETE(15, "CONCRETE"),
	BLACK_CONCRETE_POWDER(15, "CONCRETE_POWDER"),
	BLACK_DYE(Version.v1_14, "INK_SACK"),
	BLACK_GLAZED_TERRACOTTA(15),
	BLACK_SHULKER_BOX,
	BLACK_STAINED_GLASS(15, "STAINED_GLASS"),
	BLACK_STAINED_GLASS_PANE(15, "STAINED_GLASS_PANE"),
	BLACK_TERRACOTTA(15, "STAINED_CLAY"),
	BLACK_WALL_BANNER("WALL_BANNER"),
	BLACK_WOOL(15, "WOOL"),
	BLAST_FURNACE(Version.v1_14),
	BLAZE_POWDER,
	BLAZE_ROD,
	BLAZE_SPAWN_EGG(61, "MONSTER_EGG"),
	BLUE_BANNER(12, "BANNER"),
	BLUE_BED(4, "BED"),
	BLUE_CARPET(11, "CARPET"),
	BLUE_CONCRETE(11, "CONCRETE"),
	BLUE_CONCRETE_POWDER(11, "CONCRETE_POWDER"),
	BLUE_DYE(4, "LAPIS_LAZULI"),
	BLUE_GLAZED_TERRACOTTA,
	BLUE_ICE(Version.v1_13),
	BLUE_ORCHID(1, "RED_ROSE"),
	BLUE_SHULKER_BOX,
	BLUE_STAINED_GLASS(11, "STAINED_GLASS"),
	BLUE_STAINED_GLASS_PANE(11, "STAINED_GLASS_PANE"),
	BLUE_TERRACOTTA(11, "STAINED_CLAY"),
	BLUE_WALL_BANNER(11, "WALL_BANNER"),
	BLUE_WOOL(11, "WOOL"),
	BONE,
	BONE_BLOCK,
	BONE_MEAL(15, "INK_SACK"),
	BOOK,
	BOOKSHELF,
	BOW,
	BOWL,
	BRAIN_CORAL(Version.v1_13),
	BRAIN_CORAL_BLOCK(Version.v1_13),
	BRAIN_CORAL_FAN(Version.v1_13),
	BRAIN_CORAL_WALL_FAN,
	BREAD,
	BREWING_STAND("BREWING_STAND_ITEM"),
	BRICK("CLAY_BRICK"),
	BRICKS("BRICK"),
	BRICK_SLAB(4, "STEP"),
	BRICK_STAIRS,
	BRICK_WALL,
	BROWN_BANNER(3, "BANNER"),
	BROWN_BED(12, "BED"),
	BROWN_CARPET(12, "CARPET"),
	BROWN_CONCRETE(12, "CONCRETE"),
	BROWN_CONCRETE_POWDER(12, "CONCRETE_POWDER"),
	BROWN_DYE(3, "INK_SACK"),
	BROWN_GLAZED_TERRACOTTA,
	BROWN_MUSHROOM,
	BROWN_MUSHROOM_BLOCK("HUGE_MUSHROOM_1"),
	BROWN_SHULKER_BOX,
	BROWN_STAINED_GLASS(12, "STAINED_GLASS"),
	BROWN_STAINED_GLASS_PANE(12, "STAINED_GLASS_PANE"),
	BROWN_TERRACOTTA(12, "STAINED_CLAY"),
	BROWN_WALL_BANNER(3, "WALL_BANNER"),
	BROWN_WOOL(12, "WOOL"),
	BUBBLE_COLUMN(Version.v1_13),
	BUBBLE_CORAL(Version.v1_13),
	BUBBLE_CORAL_BLOCK(Version.v1_13),
	BUBBLE_CORAL_FAN(Version.v1_13),
	BUBBLE_CORAL_WALL_FAN,
	BUCKET,
	CACTUS,
	CAKE("CAKE_BLOCK"),
	CAMPFIRE(Version.v1_14),
	CARROT("CARROT_ITEM"),
	CARROTS("CARROT"),
	CARROT_ON_A_STICK("CARROT_STICK"),
	CARTOGRAPHY_TABLE(Version.v1_14),
	CARVED_PUMPKIN(1, Version.v1_13, "PUMPKIN"),
	CAT_SPAWN_EGG,
	CAULDRON("CAULDRON_ITEM"),
	/**
	 * 1.13 tag is not added because it's the same thing as {@link #AIR}
	 *
	 * @see #VOID_AIR
	 */
	CAVE_AIR("AIR"),
	CAVE_SPIDER_SPAWN_EGG(59, "MONSTER_EGG"),
	CHAINMAIL_BOOTS,
	CHAINMAIL_CHESTPLATE,
	CHAINMAIL_HELMET,
	CHAINMAIL_LEGGINGS,
	CHAIN_COMMAND_BLOCK("COMMAND_CHAIN"),
	CHARCOAL(1, "COAL"),
	CHEST("LOCKED_CHEST"),
	CHEST_MINECART("STORAGE_MINECART"),
	CHICKEN("RAW_CHICKEN"),
	CHICKEN_SPAWN_EGG(93, "MONSTER_EGG"),
	CHIPPED_ANVIL(1, "ANVIL"),
	CHISELED_QUARTZ_BLOCK(1, "QUARTZ_BLOCK"),
	CHISELED_RED_SANDSTONE(1, "RED_SANDSTONE"),
	CHISELED_SANDSTONE(1, "SANDSTONE"),
	CHISELED_STONE_BRICKS(3, "SMOOTH_BRICK"),
	CHORUS_FLOWER,
	CHORUS_FRUIT,
	CHORUS_PLANT,
	CLAY,
	CLAY_BALL,
	CLOCK("WATCH"),
	COAL,
	COAL_BLOCK,
	COAL_ORE,
	COARSE_DIRT(1, "DIRT"),
	COBBLESTONE,
	COBBLESTONE_SLAB(3, "STEP"),
	COBBLESTONE_STAIRS,
	COBBLESTONE_WALL("COBBLE_WALL"),
	COBWEB("WEB"),
	COCOA(Version.v1_15),
	COCOA_BEANS(3, "COCOA"),
	COD("RAW_FISH"),
	COD_BUCKET(Version.v1_13),
	COD_SPAWN_EGG(Version.v1_13),
	COMMAND_BLOCK("COMMAND"),
	COMMAND_BLOCK_MINECART("COMMAND_MINECART"),
	COMPARATOR("REDSTONE_COMPARATOR"),
	COMPASS,
	COMPOSTER(Version.v1_14),
	CONDUIT(Version.v1_13),
	COOKED_BEEF,
	COOKED_CHICKEN,
	COOKED_COD("COOKED_FISH"),
	COOKED_MUTTON,
	COOKED_PORKCHOP("GRILLED_PORK"),
	COOKED_RABBIT,
	COOKED_SALMON(1, "COOKED_FISH"),
	COOKIE,
	CORNFLOWER(Version.v1_14),
	COW_SPAWN_EGG(92, "MONSTER_EGG"),
	CRACKED_STONE_BRICKS(2, "SMOOTH_BRICK"),
	CRAFTING_TABLE("WORKBENCH"),
	CREEPER_BANNER_PATTERN,
	CREEPER_HEAD(4, "SKULL_ITEM"),
	CREEPER_SPAWN_EGG(50, "MONSTER_EGG"),
	CREEPER_WALL_HEAD(4, "SKULL_ITEM"),
	CROSSBOW,
	CUT_RED_SANDSTONE(Version.v1_13),
	CUT_RED_SANDSTONE_SLAB("STONE_SLAB2"),
	CUT_SANDSTONE(Version.v1_13),
	CUT_SANDSTONE_SLAB("STEP"),
	CYAN_BANNER(6, "BANNER"),
	CYAN_BED(9, "BED"),
	CYAN_CARPET(9, "CARPET"),
	CYAN_CONCRETE(9, "CONCRETE"),
	CYAN_CONCRETE_POWDER(9, "CONCRETE_POWDER"),
	CYAN_DYE(6, "INK_SACK"),
	CYAN_GLAZED_TERRACOTTA,
	CYAN_SHULKER_BOX,
	CYAN_STAINED_GLASS(9, "STAINED_GLASS"),
	CYAN_STAINED_GLASS_PANE(9, "STAINED_GLASS_PANE"),
	CYAN_TERRACOTTA(9, "STAINED_CLAY"),
	CYAN_WALL_BANNER(6, "WALL_BANNER"),
	CYAN_WOOL(9, "WOOL"),
	DAMAGED_ANVIL(2, "ANVIL"),
	DANDELION("YELLOW_FLOWER"),
	DARK_OAK_BOAT("BOAT_DARK_OAK"),
	DARK_OAK_BUTTON("WOOD_BUTTON"),
	DARK_OAK_DOOR("DARK_OAK_DOOR_ITEM"),
	DARK_OAK_FENCE,
	DARK_OAK_FENCE_GATE,
	DARK_OAK_LEAVES(1, "LEAVES_2"),
	DARK_OAK_LOG(1, "LOG_2"),
	DARK_OAK_PLANKS(5, "WOOD"),
	DARK_OAK_PRESSURE_PLATE("WOOD_PLATE"),
	DARK_OAK_SAPLING(5, "SAPLING"),
	DARK_OAK_SIGN("SIGN"),
	DARK_OAK_SLAB(5, "WOOD_STEP"),
	DARK_OAK_STAIRS,
	DARK_OAK_TRAPDOOR("TRAP_DOOR"),
	DARK_OAK_WALL_SIGN("WALL_SIGN"),
	DARK_OAK_WOOD(1, "LOG_2"),
	DARK_PRISMARINE(2, "PRISMARINE"),
	DARK_PRISMARINE_SLAB(Version.v1_13),
	DARK_PRISMARINE_STAIRS(Version.v1_13),
	DAYLIGHT_DETECTOR("DAYLIGHT_DETECTOR_INVERTED"),
	DEAD_BRAIN_CORAL(Version.v1_13),
	DEAD_BRAIN_CORAL_BLOCK(Version.v1_13),
	DEAD_BRAIN_CORAL_FAN(Version.v1_13),
	DEAD_BRAIN_CORAL_WALL_FAN(Version.v1_13),
	DEAD_BUBBLE_CORAL(Version.v1_13),
	DEAD_BUBBLE_CORAL_BLOCK(Version.v1_13),
	DEAD_BUBBLE_CORAL_FAN(Version.v1_13),
	DEAD_BUBBLE_CORAL_WALL_FAN(Version.v1_13),
	DEAD_BUSH,
	DEAD_FIRE_CORAL(Version.v1_13),
	DEAD_FIRE_CORAL_BLOCK(Version.v1_13),
	DEAD_FIRE_CORAL_FAN(Version.v1_13),
	DEAD_FIRE_CORAL_WALL_FAN(Version.v1_13),
	DEAD_HORN_CORAL(Version.v1_13),
	DEAD_HORN_CORAL_BLOCK(Version.v1_13),
	DEAD_HORN_CORAL_FAN(Version.v1_13),
	DEAD_HORN_CORAL_WALL_FAN(Version.v1_13),
	DEAD_TUBE_CORAL(Version.v1_13),
	DEAD_TUBE_CORAL_BLOCK(Version.v1_13),
	DEAD_TUBE_CORAL_FAN(Version.v1_13),
	DEAD_TUBE_CORAL_WALL_FAN(Version.v1_13),
	DEBUG_STICK(Version.v1_13),
	DETECTOR_RAIL,
	DIAMOND,
	DIAMOND_AXE,
	DIAMOND_BLOCK,
	DIAMOND_BOOTS,
	DIAMOND_CHESTPLATE,
	DIAMOND_HELMET,
	DIAMOND_HOE,
	DIAMOND_HORSE_ARMOR("DIAMOND_BARDING"),
	DIAMOND_LEGGINGS,
	DIAMOND_ORE,
	DIAMOND_PICKAXE,
	DIAMOND_SHOVEL("DIAMOND_SPADE"),
	DIAMOND_SWORD,
	DIORITE(3, "STONE"),
	DIORITE_SLAB,
	DIORITE_STAIRS,
	DIORITE_WALL,
	DIRT,
	DISPENSER,
	DOLPHIN_SPAWN_EGG(Version.v1_13),
	DONKEY_SPAWN_EGG(32, "MONSTER_EGG"),
	DRAGON_BREATH("DRAGONS_BREATH"),
	DRAGON_EGG,
	DRAGON_HEAD(5, Version.v1_12, "SKULL_ITEM"),
	DRAGON_WALL_HEAD(5, "SKULL_ITEM"),
	DRIED_KELP(Version.v1_13),
	DRIED_KELP_BLOCK(Version.v1_13),
	DROPPER,
	DROWNED_SPAWN_EGG(Version.v1_13),
	EGG,
	ELDER_GUARDIAN_SPAWN_EGG(4, "MONSTER_EGG"),
	ELYTRA,
	EMERALD,
	EMERALD_BLOCK,
	EMERALD_ORE,
	ENCHANTED_BOOK,
	ENCHANTED_GOLDEN_APPLE(1, "GOLDEN_APPLE"),
	ENCHANTING_TABLE("ENCHANTMENT_TABLE"),
	ENDERMAN_SPAWN_EGG(58, "MONSTER_EGG"),
	ENDERMITE_SPAWN_EGG(67, "MONSTER_EGG"),
	ENDER_CHEST,
	ENDER_EYE("EYE_OF_ENDER"),
	ENDER_PEARL,
	END_CRYSTAL,
	END_GATEWAY,
	END_PORTAL("ENDER_PORTAL"),
	END_PORTAL_FRAME("ENDER_PORTAL_FRAME"),
	END_ROD,
	END_STONE("ENDER_STONE"),
	END_STONE_BRICKS("END_BRICKS"),
	END_STONE_BRICK_SLAB(4, "STEP"),
	END_STONE_BRICK_STAIRS("SMOOTH_STAIRS"),
	END_STONE_BRICK_WALL,
	EVOKER_SPAWN_EGG(34, "MONSTER_EGG"),
	EXPERIENCE_BOTTLE("EXP_BOTTLE"),
	FARMLAND("SOIL"),
	FEATHER,
	FERMENTED_SPIDER_EYE,
	FERN(2, "LONG_GRASS"),
	FILLED_MAP("MAP"),
	FIRE,
	FIREWORK_ROCKET("FIREWORK"),
	FIREWORK_STAR("FIREWORK_CHARGE"),
	FIRE_CHARGE("FIREBALL"),
	FIRE_CORAL(Version.v1_13),
	FIRE_CORAL_BLOCK(Version.v1_13),
	FIRE_CORAL_FAN(Version.v1_13),
	FIRE_CORAL_WALL_FAN,
	FISHING_ROD,
	FLETCHING_TABLE(Version.v1_14),
	FLINT,
	FLINT_AND_STEEL,
	FLOWER_BANNER_PATTERN,
	FLOWER_POT("FLOWER_POT_ITEM"),
	FOX_SPAWN_EGG(Version.v1_14),
	/**
	 * This special material cannot be obtained as an item.
	 */
	FROSTED_ICE,
	FURNACE("BURNING_FURNACE"),
	FURNACE_MINECART("POWERED_MINECART"),
	GHAST_SPAWN_EGG(56, "MONSTER_EGG"),
	GHAST_TEAR,
	GLASS,
	GLASS_BOTTLE,
	GLASS_PANE("THIN_GLASS"),
	GLISTERING_MELON_SLICE("SPECKLED_MELON"),
	GLOBE_BANNER_PATTERN,
	GLOWSTONE,
	GLOWSTONE_DUST,
	GOLDEN_APPLE,
	GOLDEN_AXE("GOLD_AXE"),
	GOLDEN_BOOTS("GOLD_BOOTS"),
	GOLDEN_CARROT,
	GOLDEN_CHESTPLATE("GOLD_CHESTPLATE"),
	GOLDEN_HELMET("GOLD_HELMET"),
	GOLDEN_HOE("GOLD_HOE"),
	GOLDEN_HORSE_ARMOR("GOLD_BARDING"),
	GOLDEN_LEGGINGS("GOLD_LEGGINGS"),
	GOLDEN_PICKAXE("GOLD_PICKAXE"),
	GOLDEN_SHOVEL("GOLD_SPADE"),
	GOLDEN_SWORD("GOLD_SWORD"),
	GOLD_BLOCK,
	GOLD_INGOT,
	GOLD_NUGGET,
	GOLD_ORE,
	GRANITE(1, "STONE"),
	GRANITE_SLAB,
	GRANITE_STAIRS,
	GRANITE_WALL,
	GRASS("LONG_GRASS"),
	GRASS_BLOCK("GRASS"),
	GRASS_PATH,
	GRAVEL,
	GRAY_BANNER(8, "BANNER"),
	GRAY_BED(7, "BED"),
	GRAY_CARPET(7, "CARPET"),
	GRAY_CONCRETE(7, "CONCRETE"),
	GRAY_CONCRETE_POWDER(7, "CONCRETE_POWDER"),
	GRAY_DYE(8, "INK_SACK"),
	GRAY_GLAZED_TERRACOTTA,
	GRAY_SHULKER_BOX,
	GRAY_STAINED_GLASS(7, "STAINED_GLASS"),
	GRAY_STAINED_GLASS_PANE(7, "STAINED_GLASS_PANE"),
	GRAY_TERRACOTTA(7, "STAINED_CLAY"),
	GRAY_WALL_BANNER(8, "WALL_BANNER"),
	GRAY_WOOL(7, "WOOL"),
	GREEN_BANNER(2, "BANNER"),
	GREEN_BED(13, "BED"),
	GREEN_CARPET(13, "CARPET"),
	GREEN_CONCRETE(13, "CONCRETE"),
	GREEN_CONCRETE_POWDER(13, "CONCRETE_POWDER"),
	GREEN_DYE(2, "INK_SACK"),
	GREEN_GLAZED_TERRACOTTA,
	GREEN_SHULKER_BOX,
	GREEN_STAINED_GLASS(13, "STAINED_GLASS"),
	GREEN_STAINED_GLASS_PANE(13, "STAINED_GLASS_PANE"),
	GREEN_TERRACOTTA(13, "STAINED_CLAY"),
	GREEN_WALL_BANNER(2, "WALL_BANNER"),
	GREEN_WOOL(13, "WOOL"),
	GRINDSTONE(Version.v1_14),
	GUARDIAN_SPAWN_EGG(68, "MONSTER_EGG"),
	GUNPOWDER("SULPHUR"),
	HAY_BLOCK,
	HEART_OF_THE_SEA(Version.v1_13),
	HEAVY_WEIGHTED_PRESSURE_PLATE("IRON_PLATE"),
	HONEYCOMB(Version.v1_15),
	HONEYCOMB_BLOCK(Version.v1_15),
	HONEY_BLOCK(Version.v1_15),
	HONEY_BOTTLE(Version.v1_15),
	HOPPER,
	HOPPER_MINECART,
	HORN_CORAL(Version.v1_13),
	HORN_CORAL_BLOCK(Version.v1_13),
	HORN_CORAL_FAN(Version.v1_13),
	HORN_CORAL_WALL_FAN,
	HORSE_SPAWN_EGG(100, "MONSTER_EGG"),
	HUSK_SPAWN_EGG(23, "MONSTER_EGG"),
	ICE,
	INFESTED_CHISELED_STONE_BRICKS(5, "MONSTER_EGGS"),
	INFESTED_COBBLESTONE(1, "MONSTER_EGGS"),
	INFESTED_CRACKED_STONE_BRICKS(4, "MONSTER_EGGS"),
	INFESTED_MOSSY_STONE_BRICKS(3, "MONSTER_EGGS"),
	INFESTED_STONE("MONSTER_EGGS"),
	INFESTED_STONE_BRICKS(2, "MONSTER_EGGS"),
	INK_SAC("INK_SACK"),
	IRON_AXE,
	IRON_BARS("IRON_FENCE"),
	IRON_BLOCK,
	IRON_BOOTS,
	IRON_CHESTPLATE,
	IRON_DOOR("IRON_DOOR_BLOCK"),
	IRON_HELMET,
	IRON_HOE,
	IRON_HORSE_ARMOR("IRON_BARDING"),
	IRON_INGOT,
	IRON_LEGGINGS,
	IRON_NUGGET,
	IRON_ORE,
	IRON_PICKAXE,
	IRON_SHOVEL("IRON_SPADE"),
	IRON_SWORD,
	IRON_TRAPDOOR,
	ITEM_FRAME,
	JACK_O_LANTERN,
	JIGSAW(Version.v1_14),
	JUKEBOX,
	JUNGLE_BOAT("BOAT_JUNGLE"),
	JUNGLE_BUTTON("WOOD_BUTTON"),
	JUNGLE_DOOR("JUNGLE_DOOR_ITEM"),
	JUNGLE_FENCE,
	JUNGLE_FENCE_GATE,
	JUNGLE_LEAVES(3, "LEAVES"),
	JUNGLE_LOG(3, "LOG"),
	JUNGLE_PLANKS(3, "WOOD"),
	JUNGLE_PRESSURE_PLATE("WOOD_PLATE"),
	JUNGLE_SAPLING(3, "SAPLING"),
	JUNGLE_SIGN("SIGN"),
	JUNGLE_SLAB(3, "WOOD_STEP"),
	JUNGLE_STAIRS("JUNGLE_WOOD_STAIRS"),
	JUNGLE_TRAPDOOR("TRAP_DOOR"),
	JUNGLE_WALL_SIGN("WALL_SIGN"),
	JUNGLE_WOOD(3, "LOG"),
	KELP(Version.v1_13),
	KELP_PLANT(Version.v1_13),
	KNOWLEDGE_BOOK(Version.v1_12, "BOOK"),
	LADDER,
	LANTERN("SEA_LANTERN"),
	LAPIS_BLOCK,
	LAPIS_LAZULI(4, "INK_SACK"),
	LAPIS_ORE,
	LARGE_FERN(3, "DOUBLE_PLANT"),
	LAVA("STATIONARY_LAVA"),
	LAVA_BUCKET,
	LEAD("LEASH"),
	LEATHER,
	LEATHER_BOOTS,
	LEATHER_CHESTPLATE,
	LEATHER_HELMET,
	LEATHER_HORSE_ARMOR(Version.v1_14),
	LEATHER_LEGGINGS,
	LECTERN(Version.v1_14),
	LEVER,
	LIGHT_BLUE_BANNER(3, "BANNER"),
	LIGHT_BLUE_BED(3, "BED"),
	LIGHT_BLUE_CARPET(3, "CARPET"),
	LIGHT_BLUE_CONCRETE(3, "CONCRETE"),
	LIGHT_BLUE_CONCRETE_POWDER(3, "CONCRETE_POWDER"),
	LIGHT_BLUE_DYE(12, "INK_SACK"),
	LIGHT_BLUE_GLAZED_TERRACOTTA,
	LIGHT_BLUE_SHULKER_BOX,
	LIGHT_BLUE_STAINED_GLASS(3, "STAINED_GLASS"),
	LIGHT_BLUE_STAINED_GLASS_PANE(3, "STAINED_GLASS_PANE"),
	LIGHT_BLUE_TERRACOTTA(3, "STAINED_CLAY"),
	LIGHT_BLUE_WALL_BANNER(12, "WALL_BANNER"),
	LIGHT_BLUE_WOOL(3, "WOOL"),
	LIGHT_GRAY_BANNER(7, "BANNER"),
	LIGHT_GRAY_BED(8, "BED"),
	LIGHT_GRAY_CARPET(8, "CARPET"),
	LIGHT_GRAY_CONCRETE(8, "CONCRETE"),
	LIGHT_GRAY_CONCRETE_POWDER(8, "CONCRETE_POWDER"),
	LIGHT_GRAY_DYE(7, "INK_SACK"),
	/**
	 * Renamed to SILVER_GLAZED_TERRACOTTA in 1.13
	 * Renamed to LIGHT_GRAY_GLAZED_TERRACOTTA in 1.14
	 */
	LIGHT_GRAY_GLAZED_TERRACOTTA(Version.v1_12, "SILVER_GLAZED_TERRACOTTA"),
	LIGHT_GRAY_SHULKER_BOX("SILVER_SHULKER_BOX"),
	LIGHT_GRAY_STAINED_GLASS(8, "STAINED_GLASS"),
	LIGHT_GRAY_STAINED_GLASS_PANE(8, "STAINED_GLASS_PANE"),
	LIGHT_GRAY_TERRACOTTA(8, "STAINED_CLAY"),
	LIGHT_GRAY_WALL_BANNER(7, "WALL_BANNER"),
	LIGHT_GRAY_WOOL(8, "WOOL"),
	LIGHT_WEIGHTED_PRESSURE_PLATE("GOLD_PLATE"),
	LILAC(1, "DOUBLE_PLANT"),
	LILY_OF_THE_VALLEY(Version.v1_14),
	LILY_PAD("WATER_LILY"),
	LIME_BANNER(10, "BANNER"),
	LIME_BED(5, "BED"),
	LIME_CARPET(5, "CARPET"),
	LIME_CONCRETE(5, "CONCRETE"),
	LIME_CONCRETE_POWDER(5, "CONCRETE_POWDER"),
	LIME_DYE(10, "INK_SACK"),
	LIME_GLAZED_TERRACOTTA,
	LIME_SHULKER_BOX,
	LIME_STAINED_GLASS(5, "STAINED_GLASS"),
	LIME_STAINED_GLASS_PANE(5, "STAINED_GLASS_PANE"),
	LIME_TERRACOTTA(5, "STAINED_CLAY"),
	LIME_WALL_BANNER(10, "WALL_BANNER"),
	LIME_WOOL(5, "WOOL"),
	LINGERING_POTION,
	LLAMA_SPAWN_EGG(103, "MONSTER_EGG"),
	LOOM(Version.v1_14),
	MAGENTA_BANNER(13, "BANNER"),
	MAGENTA_BED(2, "BED"),
	MAGENTA_CARPET(2, "CARPET"),
	MAGENTA_CONCRETE(2, "CONCRETE"),
	MAGENTA_CONCRETE_POWDER(2, "CONCRETE_POWDER"),
	MAGENTA_DYE(13, "INK_SACK"),
	MAGENTA_GLAZED_TERRACOTTA,
	MAGENTA_SHULKER_BOX,
	MAGENTA_STAINED_GLASS(2, "STAINED_GLASS"),
	MAGENTA_STAINED_GLASS_PANE(2, "STAINED_GLASS_PANE"),
	MAGENTA_TERRACOTTA(2, "STAINED_CLAY"),
	MAGENTA_WALL_BANNER(13, "WALL_BANNER"),
	MAGENTA_WOOL(2, "WOOL"),
	MAGMA_BLOCK(Version.v1_12, "MAGMA"),
	MAGMA_CREAM,
	MAGMA_CUBE_SPAWN_EGG(62, "MONSTER_EGG"),
	MAP("EMPTY_MAP"),
	MELON("MELON_BLOCK"),
	MELON_SEEDS,
	MELON_SLICE("MELON"),
	MELON_STEM,
	MILK_BUCKET,
	MINECART,
	MOJANG_BANNER_PATTERN,
	MOOSHROOM_SPAWN_EGG(96, "MONSTER_EGG"),
	MOSSY_COBBLESTONE,
	MOSSY_COBBLESTONE_SLAB(3, "STEP"),
	MOSSY_COBBLESTONE_STAIRS,
	MOSSY_COBBLESTONE_WALL(1, "COBBLE_WALL"),
	MOSSY_STONE_BRICKS(1, "SMOOTH_BRICK"),
	MOSSY_STONE_BRICK_SLAB(4, "STEP"),
	MOSSY_STONE_BRICK_STAIRS("SMOOTH_STAIRS"),
	MOSSY_STONE_BRICK_WALL,
	MOVING_PISTON("PISTON_MOVING_PIECE"),
	MULE_SPAWN_EGG(32, "MONSTER_EGG"),
	MUSHROOM_STEM("BROWN_MUSHROOM"),
	MUSHROOM_STEW("MUSHROOM_SOUP"),
	MUSIC_DISC_11("GOLD_RECORD"),
	MUSIC_DISC_13("GREEN_RECORD"),
	MUSIC_DISC_BLOCKS("RECORD_3"),
	MUSIC_DISC_CAT("RECORD_4"),
	MUSIC_DISC_CHIRP("RECORD_5"),
	MUSIC_DISC_FAR("RECORD_6"),
	MUSIC_DISC_MALL("RECORD_7"),
	MUSIC_DISC_MELLOHI("RECORD_8"),
	MUSIC_DISC_STAL("RECORD_9"),
	MUSIC_DISC_STRAD("RECORD_10"),
	MUSIC_DISC_WAIT("RECORD_11"),
	MUSIC_DISC_WARD("RECORD_12"),
	MUTTON,
	MYCELIUM("MYCEL"),
	NAME_TAG,
	NAUTILUS_SHELL(Version.v1_13),
	NETHERRACK,
	NETHER_BRICK("NETHER_BRICK_ITEM"),
	NETHER_BRICKS("NETHER_BRICK"),
	NETHER_BRICK_FENCE("NETHER_FENCE"),
	NETHER_BRICK_SLAB(4, "STEP"),
	NETHER_BRICK_STAIRS,
	NETHER_BRICK_WALL,
	NETHER_PORTAL("PORTAL"),
	NETHER_QUARTZ_ORE("QUARTZ_ORE"),
	NETHER_STAR,
	/**
	 * Just like mentioned in https://minecraft.gamepedia.com/Nether_Wart
	 * Nether wart is also known as nether stalk in the code.
	 * NETHER_STALK is the planted state of nether warts.
	 */
	NETHER_WART("NETHER_WARTS"),
	NETHER_WART_BLOCK,
	NOTE_BLOCK,
	OAK_BOAT("BOAT"),
	OAK_BUTTON("WOOD_BUTTON"),
	/**
	 * WOODEN_DOOR: BLOCK
	 * WOOD_DOOR: ITEM
	 */
	OAK_DOOR("WOOD_DOOR"),
	OAK_FENCE("FENCE"),
	OAK_FENCE_GATE("FENCE_GATE"),
	OAK_LEAVES("LEAVES"),
	OAK_LOG("LOG"),
	OAK_PLANKS("WOOD"),
	OAK_PRESSURE_PLATE("WOOD_PLATE"),
	OAK_SAPLING("SAPLING"),
	OAK_SIGN("SIGN"),
	OAK_SLAB("WOOD_STEP"),
	OAK_STAIRS("WOOD_STAIRS"),
	OAK_TRAPDOOR("TRAP_DOOR"),
	OAK_WALL_SIGN("WALL_SIGN"),
	OAK_WOOD("LOG"),
	OBSERVER,
	OBSIDIAN,
	OCELOT_SPAWN_EGG(98, "MONSTER_EGG"),
	ORANGE_BANNER(14, "BANNER"),
	ORANGE_BED(1, "BED"),
	ORANGE_CARPET(1, "CARPET"),
	ORANGE_CONCRETE(1, "CONCRETE"),
	ORANGE_CONCRETE_POWDER(1, "CONCRETE_POWDER"),
	ORANGE_DYE(14, "INK_SACK"),
	ORANGE_GLAZED_TERRACOTTA,
	ORANGE_SHULKER_BOX,
	ORANGE_STAINED_GLASS(1, "STAINED_GLASS"),
	ORANGE_STAINED_GLASS_PANE(1, "STAINED_GLASS_PANE"),
	ORANGE_TERRACOTTA(1, "STAINED_CLAY"),
	ORANGE_TULIP(5, "RED_ROSE"),
	ORANGE_WALL_BANNER(14, "WALL_BANNER"),
	ORANGE_WOOL(1, "WOOL"),
	OXEYE_DAISY(8, "RED_ROSE"),
	PACKED_ICE,
	PAINTING,
	PANDA_SPAWN_EGG(Version.v1_14),
	PAPER,
	PARROT_SPAWN_EGG(105, "MONSTER_EGG"),
	PEONY(5, "DOUBLE_PLANT"),
	PETRIFIED_OAK_SLAB("WOOD_STEP"),
	PHANTOM_MEMBRANE(Version.v1_13),
	PHANTOM_SPAWN_EGG(Version.v1_13),
	PIG_SPAWN_EGG(90, "MONSTER_EGG"),
	PILLAGER_SPAWN_EGG(Version.v1_14),
	PINK_BANNER(9, "BANNER"),
	PINK_BED(6, "BED"),
	PINK_CARPET(6, "CARPET"),
	PINK_CONCRETE(6, "CONCRETE"),
	PINK_CONCRETE_POWDER(6, "CONCRETE_POWDER"),
	PINK_DYE(9, "INK_SACK"),
	PINK_GLAZED_TERRACOTTA,
	PINK_SHULKER_BOX,
	PINK_STAINED_GLASS(6, "STAINED_GLASS"),
	PINK_STAINED_GLASS_PANE(6, "STAINED_GLASS_PANE"),
	PINK_TERRACOTTA(6, "STAINED_CLAY"),
	PINK_TULIP(7, "RED_ROSE"),
	PINK_WALL_BANNER(14, "WALL_BANNER"),
	PINK_WOOL(6, "WOOL"),
	PISTON("PISTON_BASE"),
	PISTON_HEAD("PISTON_EXTENSION"),
	PLAYER_HEAD(3, "SKULL_ITEM"),
	PLAYER_WALL_HEAD(3, "SKULL_ITEM"),
	PODZOL(2, "DIRT"),
	POISONOUS_POTATO,
	POLAR_BEAR_SPAWN_EGG(102, "MONSTER_EGG"),
	POLISHED_ANDESITE(6, "STONE"),
	POLISHED_ANDESITE_SLAB,
	POLISHED_ANDESITE_STAIRS,
	POLISHED_DIORITE(4, "STONE"),
	POLISHED_DIORITE_SLAB,
	POLISHED_DIORITE_STAIRS,
	POLISHED_GRANITE(2, "STONE"),
	POLISHED_GRANITE_SLAB,
	POLISHED_GRANITE_STAIRS,
	POPPED_CHORUS_FRUIT("CHORUS_FRUIT_POPPED"),
	POPPY("RED_ROSE"),
	PORKCHOP("PORK"),
	POTATO("POTATO_ITEM"),
	POTATOES("POTATO"),
	POTION,
	POTTED_ACACIA_SAPLING(4, "FLOWER_POT"),
	POTTED_ALLIUM(2, "FLOWER_POT"),
	POTTED_AZURE_BLUET(3, "FLOWER_POT"),
	POTTED_BAMBOO,
	POTTED_BIRCH_SAPLING(2, "FLOWER_POT"),
	POTTED_BLUE_ORCHID(1, "FLOWER_POT"),
	POTTED_BROWN_MUSHROOM("FLOWER_POT"),
	POTTED_CACTUS("FLOWER_POT"),
	POTTED_CORNFLOWER,
	POTTED_DANDELION("FLOWER_POT"),
	POTTED_DARK_OAK_SAPLING(5, "FLOWER_POT"),
	POTTED_DEAD_BUSH("FLOWER_POT"),
	POTTED_FERN(2, "FLOWER_POT"),
	POTTED_JUNGLE_SAPLING(3, "FLOWER_POT"),
	POTTED_LILY_OF_THE_VALLEY,
	POTTED_OAK_SAPLING("FLOWER_POT"),
	POTTED_ORANGE_TULIP(5, "FLOWER_POT"),
	POTTED_OXEYE_DAISY(8, "FLOWER_POT"),
	POTTED_PINK_TULIP(7, "FLOWER_POT"),
	POTTED_POPPY("FLOWER_POT"),
	POTTED_RED_MUSHROOM("FLOWER_POT"),
	POTTED_RED_TULIP(4, "FLOWER_POT"),
	POTTED_SPRUCE_SAPLING(1, "FLOWER_POT"),
	POTTED_WHITE_TULIP(6, "FLOWER_POT"),
	POTTED_WITHER_ROSE,
	POWERED_RAIL,
	PRISMARINE,
	PRISMARINE_BRICKS(1, "PRISMARINE"),
	PRISMARINE_BRICK_SLAB(4, "STEP"),
	PRISMARINE_BRICK_STAIRS(Version.v1_13),
	PRISMARINE_CRYSTALS,
	PRISMARINE_SHARD,
	PRISMARINE_SLAB(Version.v1_13),
	PRISMARINE_STAIRS(Version.v1_13),
	PRISMARINE_WALL,
	PUFFERFISH(3, "RAW_FISH"),
	PUFFERFISH_BUCKET(Version.v1_13),
	PUFFERFISH_SPAWN_EGG(Version.v1_13),
	PUMPKIN,
	PUMPKIN_PIE,
	PUMPKIN_SEEDS,
	PUMPKIN_STEM,
	PURPLE_BANNER(5, "BANNER"),
	PURPLE_BED(10, "BED"),
	PURPLE_CARPET(10, "CARPET"),
	PURPLE_CONCRETE(10, "CONCRETE"),
	PURPLE_CONCRETE_POWDER(10, "CONCRETE_POWDER"),
	PURPLE_DYE(5, "INK_SACK"),
	PURPLE_GLAZED_TERRACOTTA,
	PURPLE_SHULKER_BOX,
	PURPLE_STAINED_GLASS(10, "STAINED_GLASS"),
	PURPLE_STAINED_GLASS_PANE(10, "STAINED_GLASS_PANE"),
	PURPLE_TERRACOTTA(10, "STAINED_CLAY"),
	PURPLE_WALL_BANNER(5, "WALL_BANNER"),
	PURPLE_WOOL(10, "WOOL"),
	PURPUR_BLOCK,
	PURPUR_PILLAR,
	PURPUR_SLAB("PURPUR_DOUBLE_SLAB"),
	PURPUR_STAIRS,
	QUARTZ,
	QUARTZ_BLOCK,
	QUARTZ_PILLAR(2, "QUARTZ_BLOCK"),
	QUARTZ_SLAB(7, "STEP"),
	QUARTZ_STAIRS,
	RABBIT,
	RABBIT_FOOT,
	RABBIT_HIDE,
	RABBIT_SPAWN_EGG(101, "MONSTER_EGG"),
	RABBIT_STEW,
	RAIL("RAILS"),
	RAVAGER_SPAWN_EGG(Version.v1_14),
	REDSTONE,
	REDSTONE_BLOCK,
	REDSTONE_LAMP("REDSTONE_LAMP_OFF"),
	REDSTONE_ORE("REDSTONE_ORE"),
	REDSTONE_TORCH("REDSTONE_TORCH_ON"),
	REDSTONE_WALL_TORCH(1, "REDSTONE_TORCH_ON"),
	REDSTONE_WIRE,
	RED_BANNER(1, "BANNER"),
	RED_BED(14, "BED"),
	RED_CARPET(14, "CARPET"),
	RED_CONCRETE(14, "CONCRETE"),
	RED_CONCRETE_POWDER(14, "CONCRETE_POWDER"),
	RED_DYE(1, "ROSE_RED"),
	RED_GLAZED_TERRACOTTA,
	RED_MUSHROOM,
	RED_MUSHROOM_BLOCK("HUGE_MUSHROOM_2"),
	RED_NETHER_BRICKS("RED_NETHER_BRICK"),
	RED_NETHER_BRICK_SLAB(4, "STEP"),
	RED_NETHER_BRICK_STAIRS,
	RED_NETHER_BRICK_WALL,
	RED_SAND(1, "SAND"),
	RED_SANDSTONE,
	RED_SANDSTONE_SLAB("STONE_SLAB2"),
	RED_SANDSTONE_STAIRS,
	RED_SANDSTONE_WALL,
	RED_SHULKER_BOX,
	RED_STAINED_GLASS(14, "STAINED_GLASS"),
	RED_STAINED_GLASS_PANE(14, "STAINED_GLASS_PANE"),
	RED_TERRACOTTA(14, "STAINED_CLAY"),
	RED_TULIP(4, "RED_ROSE"),
	RED_WALL_BANNER(1, "WALL_BANNER"),
	RED_WOOL(14, "WOOL"),
	REPEATER("DIODE"),
	REPEATING_COMMAND_BLOCK("COMMAND_REPEATING"),
	ROSE_BUSH(4, "DOUBLE_PLANT"),
	ROTTEN_FLESH,
	SADDLE,
	SALMON(1, "RAW_FISH"),
	SALMON_BUCKET(Version.v1_13),
	SALMON_SPAWN_EGG(Version.v1_13),
	SAND,
	SANDSTONE,
	SANDSTONE_SLAB(1, "STEP"),
	SANDSTONE_STAIRS,
	SANDSTONE_WALL,
	SCAFFOLDING(Version.v1_14),
	SCUTE(Version.v1_13),
	SEAGRASS(Version.v1_13),
	SEA_LANTERN,
	SEA_PICKLE(Version.v1_13),
	SHEARS,
	SHEEP_SPAWN_EGG(91, "MONSTER_EGG"),
	SHIELD,
	SHULKER_BOX("PURPLE_SHULKER_BOX"),
	SHULKER_SHELL,
	SHULKER_SPAWN_EGG(69, "MONSTER_EGG"),
	SILVERFISH_SPAWN_EGG(60, "MONSTER_EGG"),
	SKELETON_HORSE_SPAWN_EGG(28, "MONSTER_EGG"),
	SKELETON_SKULL("SKULL_ITEM"),
	SKELETON_SPAWN_EGG(51, "MONSTER_EGG"),
	SKELETON_WALL_SKULL("SKULL_ITEM"),
	SKULL_BANNER_PATTERN,
	SLIME_BALL,
	SLIME_BLOCK,
	SLIME_SPAWN_EGG(55, "MONSTER_EGG"),
	SMITHING_TABLE,
	SMOKER(Version.v1_14),
	SMOOTH_QUARTZ(Version.v1_13),
	SMOOTH_QUARTZ_SLAB(7, "STEP"),
	SMOOTH_QUARTZ_STAIRS,
	SMOOTH_RED_SANDSTONE(2, "RED_SANDSTONE"),
	SMOOTH_RED_SANDSTONE_SLAB("STONE_SLAB2"),
	SMOOTH_RED_SANDSTONE_STAIRS,
	SMOOTH_SANDSTONE(2, "SANDSTONE"),
	SMOOTH_SANDSTONE_SLAB("STEP"),
	SMOOTH_SANDSTONE_STAIRS,
	SMOOTH_STONE("STEP"),
	SMOOTH_STONE_SLAB("STEP"),
	SNOW,
	SNOWBALL("SNOW_BALL"),
	SNOW_BLOCK,
	SOUL_SAND,
	SPAWNER("MOB_SPAWNER"),
	SPECTRAL_ARROW,
	SPIDER_EYE,
	SPIDER_SPAWN_EGG(52, "MONSTER_EGG"),
	SPLASH_POTION,
	SPONGE,
	SPRUCE_BOAT("BOAT_SPRUCE"),
	SPRUCE_BUTTON("WOOD_BUTTON"),
	SPRUCE_DOOR("SPRUCE_DOOR_ITEM"),
	SPRUCE_FENCE,
	SPRUCE_FENCE_GATE,
	SPRUCE_LEAVES(1, "LEAVES"),
	SPRUCE_LOG(1, "LOG"),
	SPRUCE_PLANKS(1, "WOOD"),
	SPRUCE_PRESSURE_PLATE("WOOD_PLATE"),
	SPRUCE_SAPLING(1, "SAPLING"),
	SPRUCE_SIGN("SIGN"),
	SPRUCE_SLAB(1, "WOOD_STEP"),
	SPRUCE_STAIRS("SPRUCE_WOOD_STAIRS"),
	SPRUCE_TRAPDOOR("TRAP_DOOR"),
	SPRUCE_WALL_SIGN("WALL_SIGN"),
	SPRUCE_WOOD(1, "LOG"),
	SQUID_SPAWN_EGG(94, "MONSTER_EGG"),
	STICK,
	STICKY_PISTON("PISTON_STICKY_BASE"),
	STONE,
	STONECUTTER(Version.v1_14),
	STONE_AXE,
	STONE_BRICKS("SMOOTH_BRICK"),
	STONE_BRICK_SLAB(4, "STEP"),
	STONE_BRICK_STAIRS("SMOOTH_STAIRS"),
	STONE_BRICK_WALL,
	STONE_BUTTON,
	STONE_HOE,
	STONE_PICKAXE,
	STONE_PRESSURE_PLATE("STONE_PLATE"),
	STONE_SHOVEL("STONE_SPADE"),
	STONE_SLAB("STEP"),
	STONE_STAIRS,
	STONE_SWORD,
	STRAY_SPAWN_EGG(6, "MONSTER_EGG"),
	STRING,
	STRIPPED_ACACIA_LOG("LOG_2"),
	STRIPPED_ACACIA_WOOD("LOG_2"),
	STRIPPED_BIRCH_LOG(2, "LOG"),
	STRIPPED_BIRCH_WOOD(2, "LOG"),
	STRIPPED_DARK_OAK_LOG("LOG"),
	STRIPPED_DARK_OAK_WOOD("LOG"),
	STRIPPED_JUNGLE_LOG(3, "LOG"),
	STRIPPED_JUNGLE_WOOD(3, "LOG"),
	STRIPPED_OAK_LOG("LOG"),
	STRIPPED_OAK_WOOD("LOG"),
	STRIPPED_SPRUCE_LOG(1, "LOG"),
	STRIPPED_SPRUCE_WOOD(1, "LOG"),
	STRUCTURE_BLOCK,
	/**
	 * Originally developers used barrier blocks for its purpose.
	 * So technically this isn't really considered as a suggested material.
	 */
	STRUCTURE_VOID,
	SUGAR,
	/**
	 * Sugar Cane is a known material in pre-1.13
	 * Use XBlock when comparing block types.
	 */
	SUGAR_CANE("SUGAR_CANE_BLOCK"),
	SUNFLOWER("DOUBLE_PLANT"),
	SUSPICIOUS_STEW(Version.v1_14),
	SWEET_BERRIES(Version.v1_14),
	SWEET_BERRY_BUSH(Version.v1_14),
	TALL_GRASS(2, "DOUBLE_PLANT"),
	TALL_SEAGRASS(2, Version.v1_13),
	TERRACOTTA("HARD_CLAY"),
	TIPPED_ARROW,
	TNT,
	TNT_MINECART("EXPLOSIVE_MINECART"),
	TORCH,
	TOTEM_OF_UNDYING("TOTEM"),
	TRADER_LLAMA_SPAWN_EGG(Version.v1_14),
	TRAPPED_CHEST,
	TRIDENT(Version.v1_13),
	TRIPWIRE,
	TRIPWIRE_HOOK,
	TROPICAL_FISH(2, "RAW_FISH"),
	TROPICAL_FISH_BUCKET(Version.v1_13),
	TROPICAL_FISH_SPAWN_EGG(Version.v1_13),
	TUBE_CORAL(Version.v1_13),
	TUBE_CORAL_BLOCK(Version.v1_13),
	TUBE_CORAL_FAN(Version.v1_13),
	TUBE_CORAL_WALL_FAN,
	TURTLE_EGG(Version.v1_13),
	TURTLE_HELMET(Version.v1_13),
	TURTLE_SPAWN_EGG(Version.v1_13),
	VEX_SPAWN_EGG(35, "MONSTER_EGG"),
	VILLAGER_SPAWN_EGG(120, "MONSTER_EGG"),
	VINDICATOR_SPAWN_EGG(36, "MONSTER_EGG"),
	VINE,
	/**
	 * 1.13 tag is not added because it's the same thing as {@link #AIR}
	 *
	 * @see #CAVE_AIR
	 */
	VOID_AIR("AIR"),
	WALL_TORCH("TORCH"),
	WANDERING_TRADER_SPAWN_EGG(Version.v1_14),
	/**
	 * This is used for blocks only.
	 * In 1.13- WATER will turn into STATIONARY_WATER after it finished spreading.
	 * After 1.13+ this uses
	 * https://hub.spigotmc.org/javadocs/spigot/org/bukkit/block/data/Levelled.html water flowing system.
	 * Use XBlock for this instead.
	 */
	WATER("STATIONARY_WATER"),
	WATER_BUCKET,
	WET_SPONGE(1, "SPONGE"),
	/**
	 * Wheat is a known material in pre-1.13
	 * Use XBlock when comparing block types.
	 */
	WHEAT("CROPS"),
	WHEAT_SEEDS("SEEDS"),
	WHITE_BANNER(15, "BANNER"),
	WHITE_BED("BED"),
	WHITE_CARPET("CARPET"),
	WHITE_CONCRETE("CONCRETE"),
	WHITE_CONCRETE_POWDER("CONCRETE_POWDER"),
	WHITE_DYE(15, Version.v1_14, "INK_SACK"),
	WHITE_GLAZED_TERRACOTTA,
	WHITE_SHULKER_BOX,
	WHITE_STAINED_GLASS("STAINED_GLASS"),
	WHITE_STAINED_GLASS_PANE("STAINED_GLASS_PANE"),
	WHITE_TERRACOTTA("STAINED_CLAY"),
	WHITE_TULIP(6, "RED_ROSE"),
	WHITE_WALL_BANNER(15, "WALL_BANNER"),
	WHITE_WOOL("WOOL"),
	WITCH_SPAWN_EGG(66, "MONSTER_EGG"),
	WITHER_ROSE(Version.v1_14),
	WITHER_SKELETON_SKULL(1, "SKULL_ITEM"),
	WITHER_SKELETON_SPAWN_EGG(5, "MONSTER_EGG"),
	WITHER_SKELETON_WALL_SKULL(1, "SKULL_ITEM"),
	WOLF_SPAWN_EGG(95, "MONSTER_EGG"),
	WOODEN_AXE("WOOD_AXE"),
	WOODEN_HOE("WOOD_HOE"),
	WOODEN_PICKAXE("WOOD_PICKAXE"),
	WOODEN_SHOVEL("WOOD_SPADE"),
	WOODEN_SWORD("WOOD_SWORD"),
	WRITABLE_BOOK("BOOK_AND_QUILL"),
	WRITTEN_BOOK,
	YELLOW_BANNER(11, "BANNER"),
	YELLOW_BED(4, "BED"),
	YELLOW_CARPET(4, "CARPET"),
	YELLOW_CONCRETE(4, "CONCRETE"),
	YELLOW_CONCRETE_POWDER(4, "CONCRETE_POWDER"),
	YELLOW_DYE(11, "INK_SACK"),
	YELLOW_GLAZED_TERRACOTTA,
	YELLOW_SHULKER_BOX,
	YELLOW_STAINED_GLASS(4, "STAINED_GLASS"),
	YELLOW_STAINED_GLASS_PANE(4, "STAINED_GLASS_PANE"),
	YELLOW_TERRACOTTA(4, "STAINED_CLAY"),
	YELLOW_WALL_BANNER(11, "WALL_BANNER"),
	YELLOW_WOOL(4, "WOOL"),
	ZOMBIE_HEAD(2, "SKULL_ITEM"),
	ZOMBIE_HORSE_SPAWN_EGG(29, "MONSTER_EGG"),
	ZOMBIE_PIGMAN_SPAWN_EGG(57, "MONSTER_EGG"),
	ZOMBIE_SPAWN_EGG(54, "MONSTER_EGG"),
	ZOMBIE_VILLAGER_SPAWN_EGG(27, "MONSTER_EGG"),
	ZOMBIE_WALL_HEAD(2, "SKULL_ITEM");

	private static final ImmutableSet<String> DAMAGEABLES = ImmutableSet.of(
			"HELMET", "CHESTPLATE", "LEGGINGS", "BOOTS", "SWORD", "AXE", "PICKAXE", "SHOVEL", "HOE",
			"ELYTRA", "TRIDENT", "HORSE_ARMOR", "BARDING", "SHEARS", "FLINT_AND_STEEL", "BOW",
			"FISHING_ROD", "CARROT_ON_A_STICK", "CARROT_STICK", "SPADE", "SHIELD"
	);

    /*
     * A set of all the legacy names without duplicates.
     * <p>
     * It'll help to free up a lot of memory if it's not used.
     * Add it back if you need it.
     *
     * @see #containsLegacy(String)
     * @since 2.2.0
     *
    private static final ImmutableSet<String> LEGACY_VALUES = VALUES.stream().map(MaterialX::getLegacy)
            .flatMap(Arrays::stream)
            .filter(m -> m.charAt(1) == '.')
            .collect(Collectors.collectingAndThen(Collectors.toSet(), ImmutableSet::copyOf));
    */


	/**
	 * Pre-compiled RegEx pattern.
	 * Include both replacements to avoid recreating string multiple times with multiple RegEx checks.
	 *
	 * @since 3.0.0
	 */
	private static final Pattern FORMAT_PATTERN = Pattern.compile("\\W+");
	/**
	 * Cached result if the server version is after the v1.13 flattening update.
	 * Please don't mistake this with flat-chested people. It happened.
	 *
	 * @since 3.0.0
	 */
	private static final boolean NEW_VERSION = supports(13);
	/**
	 * The data value of this material https://minecraft.gamepedia.com/Java_Edition_data_values/Pre-flattening
	 *
	 * @see #getData()
	 */
	private final byte data;
	private final Version version;
	private final String legacy;
	private final Material material;

	MaterialX(int data, Version version, String legacy) {
		this.data = (byte) data;
		this.version = version;
		this.legacy = legacy;
		if (ServerVersion.getVersion() >= 13) {
			Material material = Material.getMaterial(name());
			this.material = material != null ? material : (legacy != null ? Material.getMaterial(legacy) : null);
		} else {
			Material material = legacy != null ? Material.getMaterial(legacy) : null;
			this.material = material != null ? material : Material.getMaterial(name());
		}
	}

	MaterialX(Version version, String legacy) {
		this(0, version, legacy);
	}

	MaterialX(int data, String legacy) {
		this(data, Version.v1_12, legacy);
	}

	MaterialX(int data, Version version) {
		this(data, version, null);
	}

	MaterialX(int data) {
		this(data, Version.v1_12, null);
	}

	MaterialX(Version version) {
		this(0, version, null);
	}

	MaterialX(String legacy) {
		this(0, legacy);
	}

	MaterialX() {
		this(0);
	}

	private enum Version {
		v1_12(12), v1_13(13), v1_14(14), v1_15(15);

		private final int version;

		Version(int version) {
			this.version = version;
		}
	}

	/**
	 * Checks if the version is 1.13 Aquatic Update or higher.
	 * An invocation of this method yields the cached result from the expression:
	 * <p>
	 * <blockquote>
	 * {@link #supports(int 13)}}
	 * </blockquote>
	 *
	 * @return true if 1.13 or higher.
	 * @see #supports(int)
	 * @since 1.0.0
	 */
	public static boolean isNewVersion() {
		return NEW_VERSION;
	}

	/**
	 * Attempts to build the string like an enum name.
	 * Removes all the spaces, numbers and extra non-English characters. Also removes some config/in-game based strings.
	 *
	 * @param name the material name to modify.
	 * @return a Material enum name.
	 * @since 2.0.0
	 */

	private static String format(String name) {
		return FORMAT_PATTERN.matcher(name.trim().replace('-', '_').replace(' ', '_')).replaceAll("").toUpperCase(Locale.ENGLISH);
	}

	/**
	 * Checks if the specified version is the same version or higher than the current server version.
	 *
	 * @param version the major version to be checked. "1." is ignored -> 1.12 = 12 | 1.9 = 9
	 * @return true of the version is equal or higher than the current version.
	 * @since 2.0.0
	 */
	public static boolean supports(int version) {
		return ServerVersion.getVersion() >= version;
	}

	/**
	 * Converts the enum names to a more friendly and readable string.
	 *
	 * @return a formatted string.
	 * @see #toWord(String)
	 * @since 2.1.0
	 */

	public static String toWord(Material material) {
		Objects.requireNonNull(material, "Cannot translate a null material to a word");
		return toWord(material.name());
	}

	/**
	 * Parses an enum name to a normal word.
	 * Normal names have underlines removed and each word capitalized.
	 * <p>
	 * <b>Examples:</b>
	 * <pre>
	 *     EMERALD                 -> Emerald
	 *     EMERALD_BLOCK           -> Emerald Block
	 *     ENCHANTED_GOLDEN_APPLE  -> Enchanted Golden Apple
	 * </pre>
	 *
	 * @param name the name of the enum.
	 * @return a cleaned more readable enum name.
	 * @since 2.1.0
	 */

	private static String toWord(String name) {
		return WordUtils.capitalize(name.replace('_', ' ').toLowerCase(Locale.ENGLISH));
	}

	/**
	 * Checks if the material can be damaged by using it.
	 * Names going through this method are not formatted.
	 *
	 * @param material the name of the material.
	 * @return true of the material can be damaged.
	 * @see #isDamageable()
	 * @since 1.0.0
	 */
	public static boolean isDamageable(String material) {
		Objects.requireNonNull(material, "Material name cannot be null");

		for (String damageable : DAMAGEABLES) {
			if (material.contains(damageable)) return true;
		}
		return false;
	}

	/**
	 * Gets the version which this material was added in.
	 * If the material doesn't have a version it'll return 0;
	 *
	 * @return the Minecraft version which tihs material was added in.
	 * @since 3.0.0
	 */
	public int getMaterialVersion() {
		return version.version;
	}

	/**
	 * Sets the {@link Material} (and data value on older versions) of an item.
	 * Damageable materials will not have their durability changed.
	 * <p>
	 * Use {@link #parseItem()} instead when creating new ItemStacks.
	 *
	 * @param item the item to change its type.
	 * @see #parseItem()
	 * @since 3.0.0
	 */

	@SuppressWarnings("deprecation")
	public ItemStack setType(ItemStack item) {
		Objects.requireNonNull(item, "Cannot set material for null ItemStack");

		item.setType(this.parseMaterial());
		if (!NEW_VERSION && !this.isDamageable()) item.setDurability(this.data);
		return item;
	}

	/**
	 * User-friendly readable name for this material
	 * In most cases you should be using {@link #name()} instead.
	 *
	 * @return string of this object.
	 * @see #toWord(String)
	 * @since 3.0.0
	 */
	@Override
	public String toString() {
		return toWord(this.name());
	}

	/**
	 * Gets the ID (Magic value) of the material.
	 *
	 * @return the ID of the material or <b>-1</b> if it's a new block or the material is not supported.
	 * @since 2.2.0
	 */
	@SuppressWarnings("deprecation")
	public int getId() {
		Material material = this.parseMaterial();
		return material == null ? -1 : material.getId();
	}

	/**
	 * Checks if the material can be damaged by using it.
	 * Names going through this method are not formatted.
	 *
	 * @return true if the item can be damaged (have its durability changed), otherwise false.
	 * @see #isDamageable(String)
	 * @since 1.0.0
	 */
	public boolean isDamageable() {
		return isDamageable(this.name());
	}

	/**
	 * The data value of this material <a href="https://minecraft.gamepedia.com/Java_Edition_data_values/Pre-flattening">pre-flattening</a>.
	 * <p>
	 * Can be accessed with {@link ItemStack#getData()} then {@code MaterialData#getData()}
	 * or {@link ItemStack#getDurability()} if not damageable.
	 *
	 * @return data of this material, or 0 if none.
	 * @since 1.0.0
	 */
	@SuppressWarnings("deprecation")
	public byte getData() {
		return data;
	}

	public boolean hasData() {
		return data != 0;
	}

	/**
	 * Get a list of materials names that was previously used by older versions.
	 * If the material was added in a new version {@link #isNewVersion()},
	 * then the first element will indicate which version the material was added in.
	 *
	 * @return a list of legacy material names and the first element as the version the material was added in if new.
	 * @since 1.0.0
	 */

	public String getLegacy() {
		return legacy;
	}

	/**
	 * Parses an item from this MaterialX.
	 * Uses data values on older versions.
	 *
	 * @return an ItemStack with the same material (and data value if in older versions.)
	 * @see #setType(ItemStack)
	 * @since 1.0.0
	 */
	public ItemStack parseItem() {
		Material material = this.parseMaterial();
		if (material == null) return null;
		return NEW_VERSION ? new ItemStack(material) : new ItemStack(material, 1, this.data);
	}

	/**
	 * Parses the material of this MaterialX.
	 *
	 * @return the material related to this MaterialX based on the server version.
	 * @since 1.0.0
	 */
	public Material parseMaterial() {
		return material;
	}

	/**
	 * Checks if an item has the same material (and data value on older versions).
	 *
	 * @param item item to check.
	 * @return true if the material is the same as the item's material (and data value if on older versions), otherwise false.
	 * @since 1.0.0
	 */
	public boolean compareType(ItemStack item) {
		Objects.requireNonNull(item, "Cannot compare with null ItemStack");
		if (item.getType() != this.parseMaterial()) return false;
		return NEW_VERSION || this.isDamageable() || item.getDurability() == this.data;
	}

	public boolean compareType(Block block) {
		Objects.requireNonNull(block, "Cannot compare with null Block");
		if (block.getType() != this.parseMaterial()) return false;
		return NEW_VERSION || block.getData() == this.data;
	}

	/**
	 * Checks if this material is supported in the current version.
	 * Suggested materials will be ignored.
	 * <p>
	 * Note that you should use {@link #parseMaterial()} and check if it's null
	 * if you're going to parse and use the material later.
	 *
	 * @return true if the material exists in {@link Material} list.
	 * @since 2.0.0
	 */
	public boolean isSupported() {
		return supports(getMaterialVersion());
	}

}