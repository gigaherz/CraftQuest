package gigaherz.craftquest.recipes;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nullable;

public class QuantifiedIngredient
{
    public final Ingredient ingredient;
    public int count;

    public QuantifiedIngredient(Ingredient ingredient, int count)
    {
        this.ingredient = ingredient;
        this.count = count;
    }

    public static QuantifiedIngredient of(Ingredient ingredient)
    {
        return new QuantifiedIngredient(ingredient, 1);
    }

    public static QuantifiedIngredient of(ItemStack stack)
    {
        return new QuantifiedIngredient(Ingredient.fromStacks(stack), stack.getCount());
    }

    public static QuantifiedIngredient of(Ingredient ingredient, int count)
    {
        return new QuantifiedIngredient(ingredient, count);
    }

    public boolean tryCombine(Ingredient ingredient)
    {
        if (compareItemStackArrays(this.ingredient, ingredient))
        {
            count++;
            return true;
        }
        return false;
    }

    private boolean compareItemStackArrays(Ingredient ingredient1, Ingredient ingredient2)
    {
        ItemStack[] stacks1 = ingredient1.getMatchingStacks();
        ItemStack[] stacks2 = ingredient2.getMatchingStacks();
        if (stacks1.length != stacks2.length) return false;
        for(ItemStack stack : stacks1)
        {
            boolean found = false;
            for(ItemStack stack2 : stacks2)
            {
                if (ItemStack.areItemStacksEqual(stack, stack2))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
                return false;
        }
        return true;
    }

    public QuantifiedIngredient copy()
    {
        return new QuantifiedIngredient(ingredient, count);
    }

    public boolean isAvailable(InventoryPlayer inv, int[] numberNeeded, @Nullable ItemStack[] outPreferred)
    {
        if (this.ingredient == null) return true;
        if (this.ingredient == Ingredient.EMPTY) return true;

        ItemStack[] matching = this.ingredient.getMatchingStacks();
        ItemStack preferred = matching[0];
        boolean found = false;

        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack slot = inv.getStackInSlot(i);
            if (this.ingredient.apply(slot) && slot.getCount() >= (numberNeeded[i] + this.count))
            {
                preferred = slot;
                found = true;
                numberNeeded[i] += this.count;
                break;
            }
        }

        if (outPreferred != null)
            outPreferred[0] = preferred;
        return found;
    }
}
