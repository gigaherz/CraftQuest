package gigaherz.craftquest.database;

import com.google.common.collect.Lists;
import gigaherz.craftquest.recipes.QuantifiedIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;

public abstract class Task
{
    final List<Task> dependencies = Lists.newArrayList();

    public abstract void display(NonNullList<ITextComponent> list, EntityPlayer player, int[] numberNeeded);

    protected void addIngredients(NonNullList<ITextComponent> list, InventoryPlayer inv, QuantifiedIngredient[] sources, int[] numberNeeded)
    {
        ItemStack[] preferred = new ItemStack[1];
        for(QuantifiedIngredient qi : sources)
        {
            if (qi == null ||qi.ingredient == null) continue;
            if (qi.ingredient == Ingredient.EMPTY) continue;
            boolean found = qi.isAvailable(inv, numberNeeded, preferred);

            list.add(new TextComponentTranslation("text.craftquest.task.requirement", found ? 'X' : ' ', preferred[0].getTextComponent() ));
        }
    }
}
