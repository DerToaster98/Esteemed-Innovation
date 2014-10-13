package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.client.render.BlockSteamPipeRenderer;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public class BlockBoiler extends BlockSteamTransporter implements IWrenchable {

    @SideOnly(Side.CLIENT)
    public static IIcon steamIcon;
    private static boolean field_149934_M;
    private final Random rand = new Random();
    private final boolean field_149932_b;
    public IIcon camoIcon;
    public IIcon camoOnIcon;
    @SideOnly(Side.CLIENT)
    private IIcon field_149935_N;
    @SideOnly(Side.CLIENT)
    private IIcon field_149936_O;
    private IIcon boilerOnIcon;
    private IIcon boilerOffIcon;
    public BlockBoiler(boolean on) {
        super(Material.iron);
        this.field_149932_b = on;
    }

    public static void updateFurnaceBlockState(boolean p_149931_0_, World p_149931_1_, int p_149931_2_, int p_149931_3_, int p_149931_4_) {
        int l = p_149931_1_.getBlockMetadata(p_149931_2_, p_149931_3_, p_149931_4_);
        TileEntity tileentity = p_149931_1_.getTileEntity(p_149931_2_, p_149931_3_, p_149931_4_);
        field_149934_M = true;

        if (p_149931_0_) {
            p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_, SteamcraftBlocks.boilerOn);
        } else {
            p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_, SteamcraftBlocks.boiler);
        }

        field_149934_M = false;
        p_149931_1_.setBlockMetadataWithNotify(p_149931_2_, p_149931_3_, p_149931_4_, l, 2);

        if (tileentity != null) {
            tileentity.validate();
            p_149931_1_.setTileEntity(p_149931_2_, p_149931_3_, p_149931_4_, tileentity);
        }
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return Steamcraft.boilerRenderID;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        int x2 = x + dir.offsetX;
        int y2 = y + dir.offsetY;
        int z2 = z + dir.offsetZ;

        if (world.getTileEntity(x2, y2, z2) instanceof TileEntityBoiler) {
            TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x2, y2, z2);
            int l = world.getBlockMetadata(x2, y2, z2);
            if (boiler != null && boiler.disguiseBlock != null && boiler.disguiseBlock != Blocks.air && !BlockSteamPipeRenderer.updateWrenchStatus()) {

                return side == l ? super.shouldSideBeRendered(world, x, y, z, side) : false;
            }
        }
        return super.shouldSideBeRendered(world, x, y, z, side);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x, y, z);
        if (boiler.isBurning()) {
            int l = world.getBlockMetadata(x, y, z);
            float f = (float) x + 0.5F;
            float f1 = (float) y + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
            float f2 = (float) z + 0.5F;
            float f3 = 0.52F;
            float f4 = rand.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.spawnParticle("smoke", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f - f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.spawnParticle("smoke", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f3), (double) f1, (double) (f2 + f4), 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 - f3), 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.spawnParticle("smoke", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
                world.spawnParticle("flame", (double) (f + f4), (double) f1, (double) (f2 + f3), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public void onBlockAdded(World p_149726_1_, int p_149726_2_, int p_149726_3_, int p_149726_4_) {
        super.onBlockAdded(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
        this.func_149930_e(p_149726_1_, p_149726_2_, p_149726_3_, p_149726_4_);
    }

    private void func_149930_e(World p_149930_1_, int p_149930_2_, int p_149930_3_, int p_149930_4_) {
        if (!p_149930_1_.isRemote) {
            Block block = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ - 1);
            Block block1 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ + 1);
            Block block2 = p_149930_1_.getBlock(p_149930_2_ - 1, p_149930_3_, p_149930_4_);
            Block block3 = p_149930_1_.getBlock(p_149930_2_ + 1, p_149930_3_, p_149930_4_);
            byte b0 = 3;

            if (block.func_149730_j() && !block1.func_149730_j()) {
                b0 = 3;
            }

            if (block1.func_149730_j() && !block.func_149730_j()) {
                b0 = 2;
            }

            if (block2.func_149730_j() && !block3.func_149730_j()) {
                b0 = 5;
            }

            if (block3.func_149730_j() && !block2.func_149730_j()) {
                b0 = 4;
            }

            p_149930_1_.setBlockMetadataWithNotify(p_149930_2_, p_149930_3_, p_149930_4_, b0, 2);
        }
    }

    public IIcon getIcon(IBlockAccess block, int x, int y, int z, int side) {
        int meta = block.getBlockMetadata(x, y, z);
        if (meta == 0) {
            meta = 3;
        }
        if (side == meta) {
            TileEntityBoiler boiler = (TileEntityBoiler) block.getTileEntity(x, y, z);
            return boiler.isBurning() ? boilerOnIcon : boilerOffIcon;
        } else {
            return side == 1 ? this.field_149935_N : this.blockIcon;
        }


    }

    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        if (p_149691_2_ == 0) {
            p_149691_2_ = 3;
        }
        return p_149691_1_ == 1 ? this.field_149935_N : (p_149691_1_ == 0 ? this.field_149935_N : (p_149691_1_ != p_149691_2_ ? this.blockIcon : this.boilerOffIcon));
    }

    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blockBrass");
        this.steamIcon = p_149651_1_.registerIcon("steamcraft:steam");

        this.boilerOnIcon = p_149651_1_.registerIcon("steamcraft:boilerOn");
        this.boilerOffIcon = p_149651_1_.registerIcon("steamcraft:boiler");
        this.camoOnIcon = p_149651_1_.registerIcon("steamcraft:boilerCamoOn");
        this.camoIcon = p_149651_1_.registerIcon("steamcraft:boilerCamo");
        //this.field_149936_O = p_149651_1_.registerIcon(this.field_149932_b ? "steamcraft:boilerOn" : "steamcraft:boiler");
        this.field_149935_N = p_149651_1_.registerIcon("steamcraft:blockBrass");
    }

    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
        int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0) {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 2, 2);
        }

        if (l == 1) {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 5, 2);
        }

        if (l == 2) {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 3, 2);
        }

        if (l == 3) {
            p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, 4, 2);
        }

        if (p_149689_6_.hasDisplayName()) {
            // ((TileEntityBoiler)p_149689_1_.getTileEntity(p_149689_2_, p_149689_3_, p_149689_4_)).func_145951_a(p_149689_6_.getDisplayName());
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityBoiler();
    }

    @Override
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer player, int par6, float par7, float par8, float par9) {
        TileEntityBoiler tileentityboiler = (TileEntityBoiler) par1World.getTileEntity(par2, par3, par4);

        if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.water_bucket) {
            if (tileentityboiler != null) {
                int num = tileentityboiler.fill(ForgeDirection.UP, new FluidStack(FluidRegistry.WATER, 1000), true);
                if (!player.capabilities.isCreativeMode && num != 0) {
                    player.inventory.consumeInventoryItem(Items.water_bucket);
                    player.inventory.addItemStackToInventory(new ItemStack(Items.bucket));
                    player.inventoryContainer.detectAndSendChanges();
                }
            }
            return true;
        } else {
            if (par1World.isRemote) {
                return true;
            } else {

                if (tileentityboiler != null) {
                    player.openGui(Steamcraft.instance, 0, par1World, par2, par3, par4);
                }

                return true;
            }
        }
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
        if (!field_149934_M) {
            TileEntityBoiler tileentityboiler = (TileEntityBoiler) p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

            if (tileentityboiler != null) {
                for (int i1 = 0; i1 < tileentityboiler.getSizeInventory(); ++i1) {
                    ItemStack itemstack = tileentityboiler.getStackInSlot(i1);

                    if (itemstack != null) {
                        float f = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                        float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                        while (itemstack.stackSize > 0) {
                            int j1 = this.rand.nextInt(21) + 10;

                            if (j1 > itemstack.stackSize) {
                                j1 = itemstack.stackSize;
                            }

                            itemstack.stackSize -= j1;
                            EntityItem entityitem = new EntityItem(p_149749_1_, (double) ((float) p_149749_2_ + f), (double) ((float) p_149749_3_ + f1), (double) ((float) p_149749_4_ + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                            if (itemstack.hasTagCompound()) {
                                entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
                            }

                            float f3 = 0.05F;
                            entityitem.motionX = (double) ((float) this.rand.nextGaussian() * f3);
                            entityitem.motionY = (double) ((float) this.rand.nextGaussian() * f3 + 0.2F);
                            entityitem.motionZ = (double) ((float) this.rand.nextGaussian() * f3);
                            p_149749_1_.spawnEntityInWorld(entityitem);
                        }
                    }
                }

                p_149749_1_.func_147453_f(p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_);
            }
        }

        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return new ItemStack(SteamcraftBlocks.boiler);
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        if (player.isSneaking()) {
            return true;
        } else if (side != 0 && side != 1) {
            world.setBlockMetadataWithNotify(x, y, z, side == meta ? ForgeDirection.getOrientation(side).getOpposite().ordinal() : side, 2);
            return true;
        }
        return false;
    }
}
