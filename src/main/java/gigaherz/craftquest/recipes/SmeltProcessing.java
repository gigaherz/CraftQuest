package gigaherz.craftquest.recipes;

import gigaherz.craftquest.database.ObtainingTask;
import gigaherz.craftquest.database.SmeltingTask;
import gigaherz.craftquest.database.Task;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class SmeltProcessing extends ProcessingBase
{
    private SmeltProcessing(QuantifiedIngredient[] inputs, ItemStack[] outputs)
    {
        this.combinedInputs = inputs;
        this.combinedOutputs = outputs;
    }

    public SmeltProcessing(ItemStack output, ItemStack input)
    {
        combinedOutputs = new ItemStack[] {output};
        combinedInputs = new QuantifiedIngredient[] {QuantifiedIngredient.of(input)};
    }

    @Override
    public Task makeTask()
    {
        return new SmeltingTask(combinedOutputs[0], combinedInputs[0]);
    }

    @Override
    public ProcessingBase copyWithSize(int count)
    {
        SmeltProcessing s = new SmeltProcessing(copyWithSize(combinedInputs, count),
                copyWithSize(combinedOutputs, count));
        s.original = this.original != null ? this.original : this;
        s.parent = parent;
        return s;
    }

    @Override
    public ProcessingBase copyWithSizeAndParent(int count, ProcessingBase parent)
    {
        SmeltProcessing s = new SmeltProcessing(copyWithSize(combinedInputs, count),
                copyWithSize(combinedOutputs, count));
        s.original = this.original != null ? this.original : this;
        s.parent = parent;
        return s;
    }
}
