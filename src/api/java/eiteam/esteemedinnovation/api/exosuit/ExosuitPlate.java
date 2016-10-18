package eiteam.esteemedinnovation.api.exosuit;

import eiteam.esteemedinnovation.api.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class ExosuitPlate {
    private String identifier;
    private String invMod;
    private String armorMod;
    private Object plate;
    private String effect;

    public ExosuitPlate(String id, Object item, String invLocMod, String armorLocMod, String effectLoc) {
        identifier = id;
        invMod = invLocMod;
        armorMod = armorLocMod;
        plate = item;
        effect = effectLoc;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Object getItem() {
        return plate;
    }

    public void setItem(Object item) {
        plate = item;
    }

    public ResourceLocation getIcon(IExosuitArmor item) {
        return new ResourceLocation(item.getString() + invMod);
    }

    public String getArmorLocation(IExosuitArmor item, EntityEquipmentSlot slot) {
        // TODO: Abstract out of API
        if (slot != EntityEquipmentSlot.LEGS) {
            return Constants.EI_MODID + ":textures/models/armor/exoPlate" + armorMod + "_1.png";
        } else {
            return Constants.EI_MODID + ":textures/models/armor/exoPlate" + armorMod + "_2.png";
        }
    }

    /**
     * @param slot The armor slot that this plate is installed in
     * @param source The damage source
     * @return The damage reduction amount for the slot and the source. Default implementation returns the IRON
     *         damage reduction amount.
     */
    public int getDamageReductionAmount(EntityEquipmentSlot slot, DamageSource source) {
        return ItemArmor.ArmorMaterial.IRON.getDamageReductionAmount(slot);
    }

    public String effect() {
        return I18n.format(effect);
    }
}