package gigaherz.craftquest;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import gigaherz.craftquest.database.Task;
import gigaherz.craftquest.recipes.CraftProcessing;
import gigaherz.craftquest.recipes.ProcessingBase;
import gigaherz.craftquest.recipes.QuantifiedIngredient;
import gigaherz.craftquest.recipes.SmeltProcessing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.*;

public class GuiOverlayTasks extends GuiScreen
{
    public static void enable()
    {
        MinecraftForge.EVENT_BUS.register(new GuiOverlayTasks());
    }

    public static final Multimap<ResourceLocation, ProcessingBase> sources = ArrayListMultimap.create();
    public static void processRecipes()
    {
        for(IRecipe recipe : CraftingManager.REGISTRY)
        {
            if (recipe.isDynamic()) continue;
            ItemStack result = recipe.getRecipeOutput();
            sources.put(result.getItem().getRegistryName(), new CraftProcessing(recipe));
        }
        for(Map.Entry<ItemStack,ItemStack> recipe : FurnaceRecipes.instance().getSmeltingList().entrySet())
        {
            ItemStack result = recipe.getValue();
            sources.put(result.getItem().getRegistryName(), new SmeltProcessing(result, recipe.getKey()));
        }
    }

    public GuiOverlayTasks()
    {
        fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

    @SubscribeEvent
    public void overlayEvent(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return;

        // TODO: Cache all of this and only re-compute when needed!!!!!!!!!

        ProcessingBase processingGoal = getSource(new ItemStack(Items.COOKIE));
        if (processingGoal == null) return;

        EntityPlayer player = Minecraft.getMinecraft().player;

        int[] numberNeeded = new int[player.inventory.getSizeInventory()];

        // TODO: Aggregate multiple sub-recipes into one bigger step
        //   to avoid repeating the same craft multiple times.
        //Set<ProcessingBase> seen = Sets.newHashSet();
        Deque<ProcessingBase> craftChain = new LinkedList<>();
        Queue<ProcessingBase> pendingChain = new LinkedList<>();
        pendingChain.add(processingGoal);
        while(pendingChain.size() > 0)
        {
            ProcessingBase current = pendingChain.remove();
            craftChain.addFirst(current);
            for(QuantifiedIngredient qi : current.getCombinedInputs())
            {
                if (qi.isAvailable(player.inventory, numberNeeded, null))
                    continue;
                // TODO: Optimize which of the possible sub-ingredients is preferred (by mod or something)
                for (ItemStack stack : qi.ingredient.getMatchingStacks())
                {
                    ProcessingBase qiTemp = getSource(stack, 1);
                    if (qiTemp == null)
                        continue;
                    if (current.hasParent(qiTemp))
                        continue;
                    ProcessingBase qiSource = qiTemp.copyWithSizeAndParent(qi.count, current);
                    pendingChain.add(qiSource);
                    break;
                }
            }
        }

        NonNullList<ITextComponent> textComponents = NonNullList.create();

        int[] numberNeeded2 = new int[player.inventory.getSizeInventory()];
        for(ProcessingBase pb : craftChain)
        {
            // TODO: Cache this too!
            Task task = pb.makeTask();

            task.display(textComponents, Minecraft.getMinecraft().player, numberNeeded2);
        }

        int y = 5;
        for (ITextComponent text : textComponents)
        {
            drawString(fontRenderer, text.getFormattedText(), 5, y, 0xFFFFFFFF);
            y += fontRenderer.FONT_HEIGHT;
        }
    }

    private ProcessingBase getSource(ItemStack itemStack)
    {
        return getSource(itemStack, 1);
    }

    @Nullable
    private ProcessingBase getSource(ItemStack itemStack, int count)
    {
        Collection<ProcessingBase> subset = sources.get(itemStack.getItem().getRegistryName());
        for (ProcessingBase source : subset)
        {
            if (source.produces(itemStack))
            {
                if (count != 1)
                    return source.copyWithSize(count);
                else
                    return source;
            }
        }
        return null;
    }
}
