package flaxbeard.steamcraft.world;

import cpw.mods.fml.common.IWorldGenerator;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;

import java.util.Random;

public class SteamcraftOreGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        String[] zincArray = Config.zincDims.split(";");
        final int[] zincIDs = new int[zincArray.length];

        String[] copperArray = Config.copperDims.split(";");
        final int[] copperIDs = new int[copperArray.length];

        switch (world.provider.dimensionId) {
            case -1:
                generateNether(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 0:
                generateSurface(world, random, chunkX * 16, chunkZ * 16);
                break;
            case 1:
                generateEnd(world, random, chunkX * 16, chunkZ * 16);
                break;
        }

        for (int id : copperIDs) {
            generateExtra(world, random, chunkX * 16, chunkZ * 16, id);
        }
        
        for (int id : zincIDs) {
            generateExtra(world, random, chunkX * 16, chunkZ * 16, id);
        }
    }

    private void generateEnd(World world, Random random, int i, int j) {
        if (Config.genCopperEnd) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(128);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 0, 10, Blocks.end_stone)).generate(world, random, x, y, z);
            }
        }

        if (Config.genZincEnd) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(128);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 1, 7, Blocks.end_stone)).generate(world, random, x, y, z);
            }
        }
    }

    private void generateSurface(World world, Random random, int i, int j) {
        if (Config.genCopperOverworld) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(80);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 0, 10, Blocks.stone)).generate(world, random, x, y, z);
            }
        }

        if (Config.genZincOverworld) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(75);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 1, 7, Blocks.stone)).generate(world, random, x, y, z);
            }
        }
    }

    private void generateNether(World world, Random random, int i, int j) {
        if (Config.genCopperNether) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(128);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 0, 10, Blocks.netherrack)).generate(world, random, x, y, z);
            }
        }

        if (Config.genZincNether) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(128);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 1, 7, Blocks.netherrack)).generate(world, random, x, y, z);
            }
        }
    }

    private void generateExtra(World world, Random random, int i, int j, int id) {
        world.provider.dimensionId = id;

        if (Config.genZincExtras) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(128);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 1, 7, Blocks.stone)).generate(world, random, x, y, z);
            }
        }

        if (Config.genCopperExtras) {
            for (int k = 0; k < 10; k++) {
                int x = i + random.nextInt(16);
                int y = random.nextInt(128);
                int z = j + random.nextInt(16);
                (new WorldGenMinable(SteamcraftBlocks.steamcraftOre, 0, 7, Blocks.stone)).generate(world, random, x, y, z);
            }
        }
    }
}
