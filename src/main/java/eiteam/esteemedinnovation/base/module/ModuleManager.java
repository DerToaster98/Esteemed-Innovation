package eiteam.esteemedinnovation.base.module;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    
    Map<String, Module> modules;
    
    public ModuleManager() {
        modules = new HashMap<>();
    }
    
    public Map<String, Module> getModules() {
        return modules;
    }
    
    public Module getModule(String name) {
        return modules.get(name);
    }
    
    public void registerModule(Module module) {
        modules.put(module.getName(), module);
        EsteemedInnovation.LOGGER.debug("Registered Module: " + module.getName());
    }
    
    public void setupConfig(final ForgeConfigSpec.Builder builder) {
        modules.forEach((name, module) -> {
            if (module.hasConfigs()) {
                builder.push(name);
                module.setupConfig(builder);
                builder.pop();
            }
        });
    }
    
    public void reloadConfig() {
        modules.forEach((name, module) -> module.reloadConfig());
    }
    
    public void setup(final FMLCommonSetupEvent event) {
        modules.forEach((name, module) -> module.setup(event));
    }
    
    public void setupClient(final FMLClientSetupEvent event) {
        modules.forEach((name, module) -> module.setupClient(event));
    }
}
