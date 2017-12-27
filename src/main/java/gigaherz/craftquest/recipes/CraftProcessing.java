package gigaherz.craftquest.recipes;

import com.google.common.collect.Lists;
import gigaherz.craftquest.database.CraftingTask;
import gigaherz.craftquest.database.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import java.util.List;

public class CraftProcessing extends ProcessingBase
{
    private CraftProcessing(QuantifiedIngredient[] inputs, ItemStack[] outputs)
    {
        this.combinedInputs = inputs;
        this.combinedOutputs = outputs;
    }

    public CraftProcessing(IRecipe recipe)
    {
        combinedOutputs = new ItemStack[]{recipe.getRecipeOutput()};

        NonNullList<Ingredient> ingredients = recipe.getIngredients();

        List<QuantifiedIngredient> things = Lists.newArrayList();

        for(Ingredient ingredient : ingredients)
        {
            if (ingredient == null) return;
            if (ingredient == Ingredient.EMPTY) continue;

            boolean found = false;
            for(QuantifiedIngredient qi : things)
            {
                if (qi.tryCombine(ingredient))
                {
                    found = true;
                    break;
                }
            }
            if (!found)
            {
                things.add(QuantifiedIngredient.of(ingredient));
            }
        }

        combinedInputs = things.toArray(new QuantifiedIngredient[things.size()]);
    }

    @Override
    public Task makeTask()
    {
        return new CraftingTask(combinedOutputs[0], combinedInputs);
    }

    @Override
    public ProcessingBase copyWithSize(int count)
    {
        CraftProcessing p = new CraftProcessing(copyWithSize(combinedInputs, count),
                copyWithSize(combinedOutputs, count));
        p.original = this.original != null ? this.original : this;
        return p;
    }

    @Override
    public ProcessingBase copyWithSizeAndParent(int count, ProcessingBase parent)
    {
        CraftProcessing p = new CraftProcessing(copyWithSize(combinedInputs, count),
                copyWithSize(combinedOutputs, count));
        p.original = this.original != null ? this.original : this;
        p.parent = parent;
        return p;
    }
}
