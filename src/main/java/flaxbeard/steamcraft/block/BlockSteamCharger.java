package flaxbeard.steamcraft.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockSteamCharger extends BlockSteamTransporter implements IWrenchable {
    private final Random rand = new Random();
    @SideOnly(Side.CLIENT)

    public IIcon top;
    private IIcon bottom;

    public BlockSteamCharger() {
        super(Material.iron);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 0.5F, z + 1);
    }

    @Override
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_) {
        int l = MathHelper.floor_double((double) (p_149689_5_.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, l, 2);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
        float px = 1.0F / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_) {
        TileEntitySteamCharger tile = (TileEntitySteamCharger) world.getTileEntity(x, y, z);
        if (tile.getStackInSlot(0) != null) {
            if (!world.isRemote) {
                tile.dropItem(tile.getStackInSlot(0));
            }
            tile.setInventorySlotContents(0, null);
        } else {
            if (player.getHeldItem() != null) {
                if (player.getHeldItem().getItem() instanceof ISteamChargable) {
                    ISteamChargable item = (ISteamChargable) player.getHeldItem().getItem();
                    if (item.canCharge(player.getHeldItem())) {
                        ItemStack copy = player.getCurrentEquippedItem().copy();
                        copy.stackSize = 1;
                        tile.setInventorySlotContents(0, copy);
                        player.getCurrentEquippedItem().stackSize -= 1;
                        tile.randomDegrees = world.rand.nextInt(361);
                    }
                }
            }
        }
        return false;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
        return p_149691_1_ == 1 ? this.bottom : (p_149691_1_ == 0 ? this.bottom : this.blockIcon);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_) {
        this.blockIcon = p_149651_1_.registerIcon("steamcraft:blockCharger");
        this.top = p_149651_1_.registerIcon("steamcraft:blockChargerTop");
        this.bottom = p_149651_1_.registerIcon("steamcraft:blockBrass");
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntitySteamCharger();
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderType() {
        return Steamcraft.chargerRenderID;
    }

    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_) {
        TileEntitySteamCharger tileentitysteamcharger = (TileEntitySteamCharger) p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

        if (tileentitysteamcharger != null) {
            for (int i1 = 0; i1 < tileentitysteamcharger.getSizeInventory(); ++i1) {
                ItemStack itemstack = tileentitysteamcharger.getStackInSlot(i1);

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


        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        if (side != 0 && side != 1) {
            int output = meta;
            switch (side) {
                case 2:
                    output = 2;
                    break;
                case 3:
                    output = 0;
                    break;
                case 4:
                    output = 1;
                    break;
                case 5:
                    output = 3;
                    break;
            }
            if (output == meta && side > 1 && side < 6) {
                switch (ForgeDirection.getOrientation(side).getOpposite().ordinal()) {
                    case 2:
                        output = 2;
                        break;
                    case 3:
                        output = 0;
                        break;
                    case 4:
                        output = 1;
                        break;
                    case 5:
                        output = 3;
                        break;
                }
            }
            world.setBlockMetadataWithNotify(x, y, z, output, 2);
            return true;
        }
        return false;
    }
}