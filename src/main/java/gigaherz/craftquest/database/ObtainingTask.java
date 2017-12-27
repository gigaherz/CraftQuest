package gigaherz.craftquest.database;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class ObtainingTask extends Task
{
    private final ItemStack itemToObtain;

    public ObtainingTask(ItemStack output)
    {
        this.itemToObtain = output;
    }

    @Override
    public void display(NonNullList<ITextComponent> list, EntityPlayer player, int[] numberNeeded2)
    {
        list.add(new TextComponentTranslation("text.craftquest.task.obtain", itemToObtain.getTextComponent()));
    }

}
