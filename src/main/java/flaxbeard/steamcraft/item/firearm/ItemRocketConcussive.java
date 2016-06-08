package flaxbeard.steamcraft.item.firearm;

import flaxbeard.steamcraft.api.enhancement.IRocket;
import flaxbeard.steamcraft.entity.projectile.EntityConcussiveRocket;
import flaxbeard.steamcraft.entity.projectile.EntityRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class ItemRocketConcussive extends Item implements IRocket {
    @Override
    public EntityRocket changeBullet(EntityRocket bullet) {
        return new EntityConcussiveRocket(bullet.worldObj, (EntityPlayer) bullet.shootingEntity, bullet.inputParam4, bullet.explosionSize);
    }
}
