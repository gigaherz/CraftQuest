package gigaherz.craftquest.recipes;

import gigaherz.craftquest.database.Task;
import net.minecraft.item.ItemStack;

public abstract class ProcessingBase
{
    public ProcessingBase original;
    public ProcessingBase parent;

    protected ItemStack[] combinedOutputs;
    protected QuantifiedIngredient[] combinedInputs;

    public abstract Task makeTask();

    public QuantifiedIngredient[] getCombinedInputs()
    {
        return combinedInputs;
    }

    public ItemStack[] getCombinedOutputs()
    {
        return combinedOutputs;
    }

    public boolean produces(ItemStack itemStack)
    {
        for(ItemStack a : combinedOutputs)
        {
            if (ItemStack.areItemsEqual(a, itemStack) && ItemStack.areItemStackTagsEqual(a, itemStack))
                return true;
        }
        return false;
    }

    public abstract ProcessingBase copyWithSize(int count);

    protected QuantifiedIngredient[] copyWithSize(QuantifiedIngredient[] inputs, int count)
    {
        QuantifiedIngredient[] newInputs = new QuantifiedIngredient[inputs.length];
        for(int i=0;i<newInputs.length;i++)
        {
            newInputs[i] = copyWithSize(inputs[i],count);
        }
        return newInputs;
    }

    protected ItemStack[] copyWithSize(ItemStack[] outputs, int count)
    {
        ItemStack[] newOutputs = new ItemStack[combinedInputs.length];
        for(int i=0;i<newOutputs.length;i++)
        {
            newOutputs[i] = copyWithSize(outputs[i],count);
        }
        return newOutputs;
    }

    private QuantifiedIngredient copyWithSize(QuantifiedIngredient input, int count)
    {
        QuantifiedIngredient qi = input.copy();
        qi.count = input.count * count;
        return qi;
    }

    private ItemStack copyWithSize(ItemStack output, int count)
    {
        ItemStack stack = output.copy();
        stack.setCount(stack.getCount() * count);
        return stack;
    }

    public abstract ProcessingBase copyWithSizeAndParent(int count, ProcessingBase current);

    public boolean hasParent(ProcessingBase pb)
    {
        if (pb.original != null)
            pb = pb.original;
        ProcessingBase bb = this;
        while(bb != null)
        {
            ProcessingBase bx = bb;
            if (bx.original != null)
                bx = bb.original;
            if (bx == pb)
                return true;
            bb = bb.parent;
        }
        return false;
    }
}
