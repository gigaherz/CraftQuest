package gigaherz.craftquest;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CraftQuest.MODID, version = CraftQuest.VERSION)
public class CraftQuest
{
    public static final String MODID = "craftquest";
    public static final String VERSION = "1.0";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GuiOverlayTasks.enable();
    }

    @EventHandler
    public void init(FMLPreInitializationEvent event)
    {
        GuiOverlayTasks.processRecipes();
    }
}
