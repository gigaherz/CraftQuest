package gigaherz.craftquest.database;

import gigaherz.craftquest.recipes.QuantifiedIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CraftingTask extends Task
{
    private final ItemStack itemToCraft;
    private final QuantifiedIngredient[] inputs;

    public CraftingTask(ItemStack itemToCraft, QuantifiedIngredient[] inputs)
    {
        this.itemToCraft = itemToCraft;
        this.inputs = inputs;
    }

    @Override
    public void display(NonNullList<ITextComponent> list, EntityPlayer player, int[] numberNeeded)
    {
        InventoryPlayer inv = player.inventory;
        list.add(new TextComponentTranslation("text.craftquest.task.craft", itemToCraft.getTextComponent()));
        addIngredients(list, inv, inputs, numberNeeded);
    }

}
