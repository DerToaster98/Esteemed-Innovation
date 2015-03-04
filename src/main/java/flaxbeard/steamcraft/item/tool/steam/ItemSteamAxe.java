package flaxbeard.steamcraft.item.tool.steam;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamChargable;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.HashMap;
import java.util.List;

public class ItemSteamAxe extends ItemAxe implements ISteamChargable {
    public static HashMap<Integer, MutablePair<Integer, Integer>> stuff = new HashMap<Integer, MutablePair<Integer, Integer>>();
    public IIcon[] icon = new IIcon[2];
    private boolean hasBrokenBlock = false;

    public ItemSteamAxe() {
        super(EnumHelper.addToolMaterial("AXE", 2, 320, 1.0F, -1.0F, 0));
    }

    public static void checkNBT(EntityPlayer player) {
        if (!stuff.containsKey(player.getEntityId())) {
            stuff.put(player.getEntityId(), MutablePair.of(0, 0));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4) {
        super.addInformation(me, player, list, par4);
        list.add(EnumChatFormatting.WHITE + "" + (me.getMaxDamage() - me.getItemDamage()) * this.steamPerDurability() + "/" + me.getMaxDamage() * this.steamPerDurability() + " SU");
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_) {
        hasBrokenBlock = true;
        return true;
    }

    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        checkNBT(player);

        MutablePair info = stuff.get(player.getEntityId());
        int ticks = (Integer) info.left;
        return this.icon[ticks > 50 ? 0 : 1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon[0] = this.itemIcon = ir.registerIcon("steamcraft:axe0");
        this.icon[1] = ir.registerIcon("steamcraft:axe1");
    }

    public void onUpdate(ItemStack stack, World par2World, Entity player, int par4, boolean par5) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("player")) {
            stack.stackTagCompound.setInteger("player", -1);
        }
        int oldPlayer = stack.stackTagCompound.getInteger("player");
        if (oldPlayer != player.getEntityId()) {
            stack.stackTagCompound.setInteger("player", player.getEntityId());

        }
        if (player instanceof EntityPlayer) {
            checkNBT((EntityPlayer) player);
            MutablePair info = stuff.get(player.getEntityId());
            int ticks = (Integer) info.left;
            int speed = (Integer) info.right;

            if (hasBrokenBlock) {
                speed -= 10;
                hasBrokenBlock = false;
            }
            int addedTicks = Math.min(((Double) Math.floor((double) speed / 1000D * 25D)).intValue(), 50);
            ticks += addedTicks;
            ////Steamcraft.log.debug("speed: "+speed + "; ticks: "+ticks + "; added: "+addedTicks);
            if (speed > 0) {
                speed--;
            } else if (ticks <= 0) {
                ticks = 0;
            } else {
                ticks--;
            }


            ticks = ticks % 100;
            stuff.put(player.getEntityId(), MutablePair.of(ticks, speed));
        }
    }


    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player) {
        checkNBT(player);
        if (stack.getItemDamage() < stack.getMaxDamage() - 1) {
            MutablePair info = stuff.get(player.getEntityId());
            int ticks = (Integer) info.left;
            int speed = (Integer) info.right;
            if (speed <= 1000) {
                speed += Math.min(90, 1000 - speed);
                stack.damageItem(1, player);
            }
            stuff.put(player.getEntityId(), MutablePair.of(ticks, speed));
        }
        return stack;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return 1.0F;
    }

    @Override
    public int steamPerDurability() {
        return Config.steamToolConsumptionAxe;
    }

    @Override
    public boolean canCharge(ItemStack me) {
        return true;
    }

}
